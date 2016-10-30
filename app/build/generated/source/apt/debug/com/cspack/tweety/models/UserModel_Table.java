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
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
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

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{id,name,profileImageUrl,screenName,profileBackgroundImageUrlHttps,profileBackgroundColor,profileTextColor};

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
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, UserModel model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT OR REPLACE INTO `UserModel`(`id`,`name`,`profileImageUrl`,`screenName`,`profileBackgroundImageUrlHttps`,`profileBackgroundColor`,`profileTextColor`) VALUES (?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT OR REPLACE INTO `UserModel`(`id`,`name`,`profileImageUrl`,`screenName`,`profileBackgroundImageUrlHttps`,`profileBackgroundColor`,`profileTextColor`) VALUES (?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `UserModel`(`id` TEXT,`name` TEXT,`profileImageUrl` TEXT,`screenName` TEXT,`profileBackgroundImageUrlHttps` TEXT,`profileBackgroundColor` TEXT,`profileTextColor` TEXT, PRIMARY KEY(`id`)" + ");";
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
