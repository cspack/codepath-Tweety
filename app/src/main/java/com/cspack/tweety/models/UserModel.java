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
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public static UserModel byId(String id) {
    return new Select().from(UserModel.class).where(UserModel_Table.id.eq(id)).querySingle();
  }
}
