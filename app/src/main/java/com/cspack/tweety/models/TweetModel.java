package com.cspack.tweety.models;

import android.text.Html;

import com.cspack.tweety.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the DBFlow wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Parcel
@Table(database = TwitterDatabase.class)
public class TweetModel extends BaseModel {
  @PrimaryKey
  @Column
  String id;

  @Column
  String text;

  @Column
  @ForeignKey(saveForeignKeyModel = true)
  UserModel user;

  @Column
  String createdAt;

  @Column
  String firstPhotoUrl;

  // V3: START.
  @Column
  int retweetCount;

  @Column
  int favoritesCount;

  @Column
  boolean favorited;

  @Column
  boolean retweeted;

  @Column
  String inReplyToStatusId;

  @Column
  String inReplyToScreenName;

  // V3: END.

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getFirstPhotoUrl() {
    return firstPhotoUrl;
  }

  public void setFirstPhotoUrl(String firstPhotoUrl) {
    this.firstPhotoUrl = firstPhotoUrl;
  }

  public int getRetweetCount() {
    return retweetCount;
  }

  public void setRetweetCount(int retweetCount) {
    this.retweetCount = retweetCount;
  }

  public int getFavoritesCount() {
    return favoritesCount;
  }

  public void setFavoritesCount(int favoritesCount) {
    this.favoritesCount = favoritesCount;
  }

  public boolean isFavorited() {
    return favorited;
  }

  public void setFavorited(boolean favorited) {
    this.favorited = favorited;
  }

  public boolean isRetweeted() {
    return retweeted;
  }

  public void setRetweeted(boolean retweeted) {
    this.retweeted = retweeted;
  }

  public String getInReplyToStatusId() {
    return inReplyToStatusId;
  }

  public void setInReplyToStatusId(String inReplyToStatusId) {
    this.inReplyToStatusId = inReplyToStatusId;
  }

  public String getInReplyToScreenName() {
    return inReplyToScreenName;
  }

  public void setInReplyToScreenName(String inReplyToScreenName) {
    this.inReplyToScreenName = inReplyToScreenName;
  }

  public TweetModel() {
    super();
  }

  public TweetModel(JSONObject object) {
    super();

    try {
      this.id = object.getString("id_str");
      // Use fromHtml to decode html entities "&amp;" "&nbsp;".
      this.text = Html.fromHtml(object.getString("text")).toString();
      this.createdAt = object.getString("created_at");
      this.user = new UserModel(object.getJSONObject("user"));
      this.favoritesCount = object.getInt("favorite_count");
      this.retweetCount = object.getInt("retweet_count");
      this.favorited = object.getBoolean("favorited");
      this.retweeted = object.getBoolean("retweeted");
      this.inReplyToScreenName = object.getString("in_reply_to_screen_name");
      this.inReplyToStatusId = object.getString("in_reply_to_status_id");
      if (object.has("entities")) {
        loadEntities(object.getJSONObject("entities"));
      }
    } catch (JSONException e) {
     e.printStackTrace();
    }
  }

  private void loadEntities(JSONObject entities) {
    if (entities.has("media")) {
      try {
        JSONArray medias = entities.getJSONArray("media");
        if (medias.length() == 0) return;
        List<String> photoUrls = new ArrayList<>();
        for (int i = 0; i < medias.length(); i++) {
          JSONObject media = medias.getJSONObject(i);
          if (media.getString("type").equals("photo")) {
            photoUrls.add(media.getString("media_url_https"));
          }
        }
        this.firstPhotoUrl = photoUrls.get(0);
        // TODO(chris): Support more advanced photo objects. i.e: media table keyed off tweet id.
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }


  public static List<TweetModel> fromJsonArray(JSONArray array) throws JSONException {
    List<TweetModel> tweetList = new ArrayList<>(array.length());
    for (int i = 0; i < array.length(); i++) {
      TweetModel tweet = new TweetModel(array.getJSONObject(i));
      tweetList.add(tweet);
    }
    return tweetList;
  }

  // Record Finders
  public static TweetModel byId(String id) {
    return new Select().from(TweetModel.class).where(TweetModel_Table.id.eq(id)).querySingle();
  }
}
