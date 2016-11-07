package com.cspack.tweety;

import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.UserModel;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Database(name = TwitterDatabase.NAME, version = TwitterDatabase.VERSION,
          foreignKeysSupported = true)
public class TwitterDatabase {
  // Renaming because uninstalling app apparently doesn't delete the db file... wat.
  public static final String NAME = "TwitterWeek4Final";
  public static final int VERSION = 4;

  @Migration(version = 2, database = TwitterDatabase.class)
  public static class UpgradeUserToV2 extends AlterTableMigration<UserModel> {
    public UpgradeUserToV2(Class<UserModel> table) {
      super(table);
    }
    @Override
    public void onPreMigrate() {
      addColumn(SQLiteType.INTEGER, "followerCount");
      addColumn(SQLiteType.INTEGER, "friendCount");
      addColumn(SQLiteType.INTEGER, "favoriteCount");
      addColumn(SQLiteType.TEXT, "tagLine");
      addColumn(SQLiteType.INTEGER, "verified");
    }
  }

  @Migration(version = 3, database = TwitterDatabase.class)
  public static class UpgradeTweetToV3 extends AlterTableMigration<TweetModel> {
    public UpgradeTweetToV3(Class<TweetModel> table) {
      super(table);
    }
    @Override
    public void onPreMigrate() {
      addColumn(SQLiteType.INTEGER, "retweetCount");
      addColumn(SQLiteType.INTEGER, "favoritesCount");
      addColumn(SQLiteType.INTEGER, "favorited");
      addColumn(SQLiteType.INTEGER, "retweeted");
      addColumn(SQLiteType.TEXT, "inReplyToStatusId");
      addColumn(SQLiteType.TEXT, "inReplyToScreenName");
    }
  }



  @Migration(version = 4, database = TwitterDatabase.class)
  public static class UpgradeUserToV4 extends AlterTableMigration<UserModel> {
    public UpgradeUserToV4(Class<UserModel> table) {
      super(table);
    }
    @Override
    public void onPreMigrate() {
      addColumn(SQLiteType.INTEGER, "following");
      addColumn(SQLiteType.INTEGER, "followRequestSent");
    }
  }
}
