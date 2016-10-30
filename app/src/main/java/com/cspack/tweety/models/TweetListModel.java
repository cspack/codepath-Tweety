package com.cspack.tweety.models;

import com.cspack.tweety.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.Index;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chris on 10/30/16.
 */

@Table(database = TwitterDatabase.class)
public class TweetListModel extends BaseModel {

  public enum PageType {
    HOME,
    USER,
    TWEET_REPLY,
  }
  @PrimaryKey(autoincrement = true)
  @Column
  Integer id;

  @Index
  @Column
  @Unique(uniqueGroups = {1}, onUniqueConflict = ConflictAction.REPLACE)
  Integer pageType;

  @Index
  @Column
  @Unique(uniqueGroups = {1}, onUniqueConflict = ConflictAction.REPLACE)
  String typeRefId;

  @Column
  @Unique(uniqueGroups = {1}, onUniqueConflict = ConflictAction.REPLACE)
  @ForeignKey(saveForeignKeyModel = true)
  TweetModel tweet;

  public Integer getPageType() {
    return pageType;
  }

  public PageType getPageTypeEnum() {
    return PageType.values()[pageType];
  }

  public void setPageType(Integer pageType) {
    this.pageType = pageType;
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
        .and(TweetListModel_Table.tweet_id.eq(tweetId));
  }

  public static String getMostRecentTweetId(PageType type, String typeRefId) {
    TweetListModel result = new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweet_id, false)
        .limit(1).querySingle();
    if (result == null) return null;
    return result.getTweet().getId();
  }

  public static String getOldestTweetId(PageType type, String typeRefId) {
    TweetListModel result = new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweet_id, true)
        .limit(1).querySingle();
    if (result == null) return null;
    return result.getTweet().getId();
  }

  public static boolean isAssociated(PageType type, String typeRefId, String tweetId) {
    return new Select().from(TweetListModel.class)
        .where(buildConditionWithTweet(type, typeRefId, tweetId)).limit(1).querySingle() != null;
  }


  public static List<TweetModel> recentItems(PageType type, String typeRefId) {
    List<TweetListModel> tweets = new Select().from(TweetListModel.class)
        .where(buildCondition(type, typeRefId))
        .orderBy(TweetListModel_Table.tweet_id, false)
        .limit(300)
        .queryList();
    List<TweetModel> resTweets = new ArrayList<>();
    for (TweetListModel tweet : tweets) resTweets.add(tweet.getTweet());
    return resTweets;
  }

  public static synchronized void associateAllAndSave(
      PageType pageType, String typeRefId, List<TweetModel> tweets) {
    List<TweetListModel> saves = new ArrayList<>();
    for (TweetModel tweet : tweets) {
      // Note: This wouldn't be necessary if @Unique worked as intended.
      // TODO: Look into this later.
      if (!isAssociated(pageType, typeRefId, tweet.getId())) {
        TweetListModel model = new TweetListModel(pageType, typeRefId, tweet);
        // saves.add(model);
        // Using this pattern since I don't trust the transaction with threads being thrown around:
        model.async().save();
      }
    }
    /*
    FastStoreModelTransaction fastStoreModelTransaction =
        FastStoreModelTransaction.insertBuilder(FlowManager.getModelAdapter(TweetListModel.class))
        .addAll(saves).build();
    DatabaseDefinition database = FlowManager.getDatabase(TweetListModel.class);
    Transaction transaction = database.beginTransactionAsync(fastStoreModelTransaction).build();
    transaction.execute();
    */
  }
}
