package com.cspack.tweety.models;

import android.util.Log;

import com.cspack.tweety.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.Index;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chris on 10/30/16.
 */

@Parcel
@Table(database = TwitterDatabase.class)
public class TweetListModel extends BaseModel {
  public static int RESULT_LIMIT = 50;

  @PrimaryKey
  @Index
  @Column
  //@Unique(uniqueGroups = {1}, onUniqueConflict = ConflictAction.REPLACE)
  Integer pageType;

  @PrimaryKey
  @Index
  @Column
  //@Unique(uniqueGroups = {1}, onUniqueConflict = ConflictAction.REPLACE)
  String typeRefId;

  @PrimaryKey
  @Column
  //@Unique(uniqueGroups = {1}, onUniqueConflict = ConflictAction.REPLACE)
  @ForeignKey(saveForeignKeyModel = true,
      references =
          {@ForeignKeyReference(columnName = "tweetId", columnType = String.class,
          foreignKeyColumnName = "id")})
  TweetModel tweet;

  public TweetListModel() {}

  public TweetListModel(PageType type, String ref, TweetModel tweet) {
    this.pageType = type.ordinal();
    if (ref == null) ref = "";
    this.typeRefId = ref;
    this.tweet = tweet;
  }

  private static ConditionGroup buildCondition(PageType type, String typeRefId) {
    if (typeRefId == null) typeRefId = "";
    return ConditionGroup.clause()
        .and(TweetListModel_Table.pageType.eq(type.ordinal()))
        .and(TweetListModel_Table.typeRefId.eq(typeRefId));
  }

  private static ConditionGroup buildConditionWithTweet(PageType type, String typeRefId,
                                                        String tweetId) {
    if (typeRefId == null) typeRefId = "";
    return ConditionGroup.clause()
        .and(TweetListModel_Table.pageType.eq(type.ordinal()))
        .and(TweetListModel_Table.typeRefId.eq(typeRefId))
        .and(TweetListModel_Table.tweetId.eq(tweetId));
  }

  public static String getMostRecentTweetId(PageType type, String typeRefId) {
    TweetListModel result = new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweetId, false)
        .limit(1).querySingle();
    if (result == null) return null;
    return result.getTweet().getId();
  }

  public static void getMostRecentTweetAsync(
      PageType type, String typeRefId, QueryTransaction.QueryResultSingleCallback<TweetListModel>
      callback) {
    new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweetId, false)
        .limit(1).async().querySingleResultCallback(callback).execute();
  }

  public static String getOldestTweetId(PageType type, String typeRefId) {
    TweetListModel result = new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweetId, true)
        .limit(1).querySingle();
    if (result == null) return null;
    return result.getTweet().getId();
  }

  public static void getOldestTweetAsync(
      PageType type, String typeRefId, QueryTransaction.QueryResultSingleCallback<TweetListModel>
      callback) {
    new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweetId, true)
        .limit(1).async().querySingleResultCallback(callback).execute();
  }

  public static boolean isAssociated(PageType type, String typeRefId, String tweetId) {
    return new Select().from(TweetListModel.class)
        .where(buildConditionWithTweet(type, typeRefId, tweetId)).limit(1).querySingle() != null;
  }

  public static void recentItemsAsync(
      PageType type, String typeRefId, int offset,
      QueryTransaction.QueryResultListCallback<TweetListModel> callback) {
    new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweetId, false)
        .limit(RESULT_LIMIT)
        .offset(offset)
        .async().queryListResultCallback(callback).execute();
  }

  public static synchronized void associateAllAndSave(
      final PageType pageType, final String typeRefId, List<TweetModel> tweets) {
    DatabaseDefinition database = FlowManager.getDatabase(TwitterDatabase.class);
    // Ensure stability since we need to do DB lookups...
    database.getTransactionManager().getSaveQueue().purgeQueue();
    List<TweetListModel> saves = new ArrayList<>(tweets.size());
    for (TweetModel tweet : tweets) {
      // Note: This wouldn't be necessary if @Unique worked as intended.
      // TODO: Look into this later, it forces lookups on the UI thread.
      if (!isAssociated(pageType, typeRefId, tweet.getId())) {
        TweetListModel model = new TweetListModel(pageType, typeRefId, tweet);
        saves.add(model);
        // Using this pattern since I don't trust the transaction with threads being thrown around:
      }
    }

    // Doesn't work because we need .save to be called on tweet (for user):
    /*
    FastStoreModelTransaction fastStoreModelTransaction =
        FastStoreModelTransaction.saveBuilder(FlowManager.getModelAdapter(TweetListModel.class))
        .addAll(saves).build();
    */
    // Doesn't seem to be saving correctly >:(.

    ProcessModelTransaction<TweetListModel> processModelTransaction =
        new ProcessModelTransaction.Builder<>(
            new ProcessModelTransaction.ProcessModel<TweetListModel>() {
              @Override
              public void processModel(TweetListModel tweetListModel) {
                if (!isAssociated(pageType, typeRefId, tweetListModel.getTweet().getId())) {
                  tweetListModel.save();
                  Log.v("TweetListModel", "Saving new tweet from " +
                      tweetListModel.getTweet().getUser().getScreenName());
                } else {
                  Log.v("TweetListModel", "Already saved tweet from " +
                      tweetListModel.getTweet().getUser().getScreenName());
                }
              }
            }).addAll(saves).build();
    Transaction transaction = database.beginTransactionAsync(processModelTransaction)
        .success(new Transaction.Success() {
          @Override
          public void onSuccess(Transaction transaction) {
            Log.v("TweetListModel", "Save completed.");
          }
        })
        .error(new Transaction.Error() {
          @Override
          public void onError(Transaction transaction, Throwable error) {
            Log.e("TweetListModel", "Async save failed!");
          }
        }).build();
    transaction.execute();

    // Doesn't work because it allows dupes:
    //database.getTransactionManager().getSaveQueue().addAll2(saves);
  }

  public Integer getPageType() {
    return pageType;
  }

  public void setPageType(Integer pageType) {
    this.pageType = pageType;
  }

  public PageType getPageTypeEnum() {
    return PageType.values()[pageType];
  }

  public String getTypeRefId() {
    return typeRefId;
  }

  public void setTypeRefId(String typeRefId) {
    this.typeRefId = typeRefId;
  }

  public TweetModel getTweet() {
    return tweet;
  }

  public void setTweet(TweetModel tweet) {
    this.tweet = tweet;
  }

  public enum PageType {
    HOME,
    USER,
    TWEET_REPLY,
    MENTIONS,
  }
}
