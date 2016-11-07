package com.cspack.tweety.models;

import com.cspack.tweety.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by chris on 10/28/16.
 */

@Parcel
@Table(database = TwitterDatabase.class, insertConflict = ConflictAction.REPLACE)
public class UserModel extends BaseModel {
  @PrimaryKey
  @Column
  String id;

  @Column
  String name;

  @Column
  String profileImageUrl;

  @Column
  String screenName;

  @Column
  String profileBackgroundImageUrlHttps;

  @Column
  String profileBackgroundColor;

  @Column
  String profileTextColor;

  @Column
  int followerCount;

  @Column
  int friendCount;

  @Column
  int favoriteCount;

  @Column
  String tagLine;

  @Column
  boolean verified;

  @Column
  boolean following;

  @Column
  boolean followRequestSent;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getProfileImageUrlBigger() {
    return profileImageUrl.replace("_normal", "_bigger");
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public String getScreenName() {
    return screenName;
  }

  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

  public String getProfileBackgroundImageUrlHttps() {
    return profileBackgroundImageUrlHttps;
  }

  public void setProfileBackgroundImageUrlHttps(String profileBackgroundImageUrlHttps) {
    this.profileBackgroundImageUrlHttps = profileBackgroundImageUrlHttps;
  }

  public String getProfileBackgroundColor() {
    return profileBackgroundColor;
  }

  public void setProfileBackgroundColor(String profileBackgroundColor) {
    this.profileBackgroundColor = profileBackgroundColor;
  }

  public String getProfileTextColor() {
    return profileTextColor;
  }

  public void setProfileTextColor(String profileTextColor) {
    this.profileTextColor = profileTextColor;
  }

  public int getFollowerCount() {
    return followerCount;
  }

  public void setFollowerCount(int followerCount) {
    this.followerCount = followerCount;
  }

  public int getFriendCount() {
    return friendCount;
  }

  public void setFriendCount(int friendCount) {
    this.friendCount = friendCount;
  }

  public String getTagLine() {
    return tagLine;
  }

  public void setTagLine(String tagLine) {
    this.tagLine = tagLine;
  }

  public int getFavoriteCount() {
    return favoriteCount;
  }

  public void setFavoriteCount(int favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public boolean isFollowing() {
    return following;
  }

  public void setFollowing(boolean following) {
    this.following = following;
  }

  public boolean isFollowRequestSent() {
    return followRequestSent;
  }

  public void setFollowRequestSent(boolean followRequestSent) {
    this.followRequestSent = followRequestSent;
  }

  public UserModel() {
    super();
  }

  public UserModel(JSONObject object) {
    super();
    try {
      this.id = object.getString("id_str");
      this.name = object.getString("name");
      this.profileImageUrl = object.getString("profile_image_url_https");
      this.screenName = object.getString("screen_name");
      if (object.has("profile_banner_url")) {
        this.profileBackgroundImageUrlHttps = object.getString("profile_banner_url");
      }
      if (object.has("profile_background_color")) {
        this.profileBackgroundColor = object.getString("profile_background_color");
      }
      if (object.has("profile_text_color")) {
        this.profileTextColor = object.getString("profile_text_color");
      }
      if (object.has("followers_count")) {
        this.followerCount = object.getInt("followers_count");
      }
      if (object.has("friends_count")) {
        this.friendCount = object.getInt("friends_count");
      }
      if (object.has("favourites_count")) {
        this.favoriteCount = object.getInt("favourites_count");
      }
      if (object.has("description")) {
        this.tagLine = object.getString("description");
      }
      if (object.has("verified")) {
        this.verified = object.getBoolean("verified");
      }
      if (object.has("following")) {
        this.following = object.getBoolean("following");
      }
      if (object.has("follow_request_sent")) {
        this.followRequestSent = object.getBoolean("follow_request_sent");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public static UserModel byId(String id) {
    return new Select().from(UserModel.class).where(UserModel_Table.id.eq(id)).querySingle();
  }
  public static UserModel byScreenName(String screenName) {
    return new Select().from(UserModel.class).where(UserModel_Table.screenName.eq(screenName))
        .querySingle();
  }
}
