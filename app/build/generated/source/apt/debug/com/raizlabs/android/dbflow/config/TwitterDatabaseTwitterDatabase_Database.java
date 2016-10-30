package com.raizlabs.android.dbflow.config;

import com.cspack.tweety.TwitterDatabase;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetListModel_Table;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.TweetModel_Table;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.models.UserModel_Table;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class TwitterDatabaseTwitterDatabase_Database extends DatabaseDefinition {
  public TwitterDatabaseTwitterDatabase_Database(DatabaseHolder holder) {
    holder.putDatabaseForTable(TweetListModel.class, this);
    holder.putDatabaseForTable(UserModel.class, this);
    holder.putDatabaseForTable(TweetModel.class, this);
    models.add(TweetListModel.class);
    modelTableNames.put("TweetListModel", TweetListModel.class);
    modelAdapters.put(TweetListModel.class, new TweetListModel_Table(holder, this));
    models.add(UserModel.class);
    modelTableNames.put("UserModel", UserModel.class);
    modelAdapters.put(UserModel.class, new UserModel_Table(holder, this));
    models.add(TweetModel.class);
    modelTableNames.put("TweetModel", TweetModel.class);
    modelAdapters.put(TweetModel.class, new TweetModel_Table(holder, this));
  }

  @Override
  public final Class<?> getAssociatedDatabaseClassFile() {
    return TwitterDatabase.class;
  }

  @Override
  public final boolean isForeignKeysSupported() {
    return false;
  }

  @Override
  public final boolean isInMemory() {
    return false;
  }

  @Override
  public final boolean backupEnabled() {
    return false;
  }

  @Override
  public final boolean areConsistencyChecksEnabled() {
    return false;
  }

  @Override
  public final int getDatabaseVersion() {
    return 1;
  }

  @Override
  public final String getDatabaseName() {
    return "TwitterDatabase";
  }
}
