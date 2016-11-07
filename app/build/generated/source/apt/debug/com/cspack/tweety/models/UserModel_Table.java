package com.cspack.tweety.models;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class UserModel_Table extends ModelAdapter<UserModel> {
  /**
   * Primary Key */
  public static final Property<String> id = new Property<String>(UserModel.class, "id");

  public static final Property<String> name = new Property<String>(UserModel.class, "name");

  public static final Property<String> profileImageUrl = new Property<String>(UserModel.class, "profileImageUrl");

  public static final Property<String> screenName = new Property<String>(UserModel.class, "screenName");

  public static final Property<String> profileBackgroundImageUrlHttps = new Property<String>(UserModel.class, "profileBackgroundImageUrlHttps");

  public static final Property<String> profileBackgroundColor = new Property<String>(UserModel.class, "profileBackgroundColor");

  public static final Property<String> profileTextColor = new Property<String>(UserModel.class, "profileTextColor");

  public static final IntProperty followerCount = new IntProperty(UserModel.class, "followerCount");

  public static final IntProperty friendCount = new IntProperty(UserModel.class, "friendCount");

  public static final IntProperty favoriteCount = new IntProperty(UserModel.class, "favoriteCount");

  public static final Property<String> tagLine = new Property<String>(UserModel.class, "tagLine");

  public static final Property<Boolean> verified = new Property<Boolean>(UserModel.class, "verified");

  public static final Property<Boolean> following = new Property<Boolean>(UserModel.class, "following");

  public static final Property<Boolean> followRequestSent = new Property<Boolean>(UserModel.class, "followRequestSent");

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{id,name,profileImageUrl,screenName,profileBackgroundImageUrlHttps,profileBackgroundColor,profileTextColor,followerCount,friendCount,favoriteCount,tagLine,verified,following,followRequestSent};

  public UserModel_Table(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
    super(databaseDefinition);
  }

  @Override
  public final Class<UserModel> getModelClass() {
    return UserModel.class;
  }

  public final String getTableName() {
    return "`UserModel`";
  }

  @Override
  public final BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`name`":  {
        return name;
      }
      case "`profileImageUrl`":  {
        return profileImageUrl;
      }
      case "`screenName`":  {
        return screenName;
      }
      case "`profileBackgroundImageUrlHttps`":  {
        return profileBackgroundImageUrlHttps;
      }
      case "`profileBackgroundColor`":  {
        return profileBackgroundColor;
      }
      case "`profileTextColor`":  {
        return profileTextColor;
      }
      case "`followerCount`":  {
        return followerCount;
      }
      case "`friendCount`":  {
        return friendCount;
      }
      case "`favoriteCount`":  {
        return favoriteCount;
      }
      case "`tagLine`":  {
        return tagLine;
      }
      case "`verified`":  {
        return verified;
      }
      case "`following`":  {
        return following;
      }
      case "`followRequestSent`":  {
        return followRequestSent;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return ALL_COLUMN_PROPERTIES;
  }

  @Override
  public final void bindToInsertValues(ContentValues values, UserModel model) {
    values.put("id", model.id != null ? model.id : null);
    values.put("name", model.name != null ? model.name : null);
    values.put("profileImageUrl", model.profileImageUrl != null ? model.profileImageUrl : null);
    values.put("screenName", model.screenName != null ? model.screenName : null);
    values.put("profileBackgroundImageUrlHttps", model.profileBackgroundImageUrlHttps != null ? model.profileBackgroundImageUrlHttps : null);
    values.put("profileBackgroundColor", model.profileBackgroundColor != null ? model.profileBackgroundColor : null);
    values.put("profileTextColor", model.profileTextColor != null ? model.profileTextColor : null);
    values.put("followerCount", model.followerCount);
    values.put("friendCount", model.friendCount);
    values.put("favoriteCount", model.favoriteCount);
    values.put("tagLine", model.tagLine != null ? model.tagLine : null);
    values.put("verified", model.verified ? 1 : 0);
    values.put("following", model.following ? 1 : 0);
    values.put("followRequestSent", model.followRequestSent ? 1 : 0);
  }

  @Override
  public final void bindToContentValues(ContentValues values, UserModel model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, UserModel model, int start) {
    if (model.id != null)  {
      statement.bindString(1 + start, model.id);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.name != null)  {
      statement.bindString(2 + start, model.name);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.profileImageUrl != null)  {
      statement.bindString(3 + start, model.profileImageUrl);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.screenName != null)  {
      statement.bindString(4 + start, model.screenName);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.profileBackgroundImageUrlHttps != null)  {
      statement.bindString(5 + start, model.profileBackgroundImageUrlHttps);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.profileBackgroundColor != null)  {
      statement.bindString(6 + start, model.profileBackgroundColor);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.profileTextColor != null)  {
      statement.bindString(7 + start, model.profileTextColor);
    } else {
      statement.bindNull(7 + start);
    }
    statement.bindLong(8 + start, model.followerCount);
    statement.bindLong(9 + start, model.friendCount);
    statement.bindLong(10 + start, model.favoriteCount);
    if (model.tagLine != null)  {
      statement.bindString(11 + start, model.tagLine);
    } else {
      statement.bindNull(11 + start);
    }
    statement.bindLong(12 + start, model.verified ? 1 : 0);
    statement.bindLong(13 + start, model.following ? 1 : 0);
    statement.bindLong(14 + start, model.followRequestSent ? 1 : 0);
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, UserModel model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT OR REPLACE INTO `UserModel`(`id`,`name`,`profileImageUrl`,`screenName`,`profileBackgroundImageUrlHttps`,`profileBackgroundColor`,`profileTextColor`,`followerCount`,`friendCount`,`favoriteCount`,`tagLine`,`verified`,`following`,`followRequestSent`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT OR REPLACE INTO `UserModel`(`id`,`name`,`profileImageUrl`,`screenName`,`profileBackgroundImageUrlHttps`,`profileBackgroundColor`,`profileTextColor`,`followerCount`,`friendCount`,`favoriteCount`,`tagLine`,`verified`,`following`,`followRequestSent`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `UserModel`(`id` TEXT,`name` TEXT,`profileImageUrl` TEXT,`screenName` TEXT,`profileBackgroundImageUrlHttps` TEXT,`profileBackgroundColor` TEXT,`profileTextColor` TEXT,`followerCount` INTEGER,`friendCount` INTEGER,`favoriteCount` INTEGER,`tagLine` TEXT,`verified` INTEGER,`following` INTEGER,`followRequestSent` INTEGER, PRIMARY KEY(`id`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, UserModel model) {
    int index_id = cursor.getColumnIndex("id");
    if (index_id != -1 && !cursor.isNull(index_id)) {
      model.id = cursor.getString(index_id);
    } else {
      model.id = null;
    }
    int index_name = cursor.getColumnIndex("name");
    if (index_name != -1 && !cursor.isNull(index_name)) {
      model.name = cursor.getString(index_name);
    } else {
      model.name = null;
    }
    int index_profileImageUrl = cursor.getColumnIndex("profileImageUrl");
    if (index_profileImageUrl != -1 && !cursor.isNull(index_profileImageUrl)) {
      model.profileImageUrl = cursor.getString(index_profileImageUrl);
    } else {
      model.profileImageUrl = null;
    }
    int index_screenName = cursor.getColumnIndex("screenName");
    if (index_screenName != -1 && !cursor.isNull(index_screenName)) {
      model.screenName = cursor.getString(index_screenName);
    } else {
      model.screenName = null;
    }
    int index_profileBackgroundImageUrlHttps = cursor.getColumnIndex("profileBackgroundImageUrlHttps");
    if (index_profileBackgroundImageUrlHttps != -1 && !cursor.isNull(index_profileBackgroundImageUrlHttps)) {
      model.profileBackgroundImageUrlHttps = cursor.getString(index_profileBackgroundImageUrlHttps);
    } else {
      model.profileBackgroundImageUrlHttps = null;
    }
    int index_profileBackgroundColor = cursor.getColumnIndex("profileBackgroundColor");
    if (index_profileBackgroundColor != -1 && !cursor.isNull(index_profileBackgroundColor)) {
      model.profileBackgroundColor = cursor.getString(index_profileBackgroundColor);
    } else {
      model.profileBackgroundColor = null;
    }
    int index_profileTextColor = cursor.getColumnIndex("profileTextColor");
    if (index_profileTextColor != -1 && !cursor.isNull(index_profileTextColor)) {
      model.profileTextColor = cursor.getString(index_profileTextColor);
    } else {
      model.profileTextColor = null;
    }
    int index_followerCount = cursor.getColumnIndex("followerCount");
    if (index_followerCount != -1 && !cursor.isNull(index_followerCount)) {
      model.followerCount = cursor.getInt(index_followerCount);
    } else {
      model.followerCount = 0;
    }
    int index_friendCount = cursor.getColumnIndex("friendCount");
    if (index_friendCount != -1 && !cursor.isNull(index_friendCount)) {
      model.friendCount = cursor.getInt(index_friendCount);
    } else {
      model.friendCount = 0;
    }
    int index_favoriteCount = cursor.getColumnIndex("favoriteCount");
    if (index_favoriteCount != -1 && !cursor.isNull(index_favoriteCount)) {
      model.favoriteCount = cursor.getInt(index_favoriteCount);
    } else {
      model.favoriteCount = 0;
    }
    int index_tagLine = cursor.getColumnIndex("tagLine");
    if (index_tagLine != -1 && !cursor.isNull(index_tagLine)) {
      model.tagLine = cursor.getString(index_tagLine);
    } else {
      model.tagLine = null;
    }
    int index_verified = cursor.getColumnIndex("verified");
    if (index_verified != -1 && !cursor.isNull(index_verified)) {
      model.verified = cursor.getInt(index_verified) == 1 ? true : false;
    } else {
      model.verified = false;
    }
    int index_following = cursor.getColumnIndex("following");
    if (index_following != -1 && !cursor.isNull(index_following)) {
      model.following = cursor.getInt(index_following) == 1 ? true : false;
    } else {
      model.following = false;
    }
    int index_followRequestSent = cursor.getColumnIndex("followRequestSent");
    if (index_followRequestSent != -1 && !cursor.isNull(index_followRequestSent)) {
      model.followRequestSent = cursor.getInt(index_followRequestSent) == 1 ? true : false;
    } else {
      model.followRequestSent = false;
    }
  }

  @Override
  public final boolean exists(UserModel model, DatabaseWrapper wrapper) {
    return SQLite.selectCountOf()
    .from(UserModel.class)
    .where(getPrimaryConditionClause(model))
    .hasData(wrapper);
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(UserModel model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(id.eq(model.id));
    return clause;
  }

  @Override
  public final UserModel newInstance() {
    return new UserModel();
  }

  @Override
  public final ConflictAction getInsertOnConflictAction() {
    return ConflictAction.REPLACE;
  }
}
