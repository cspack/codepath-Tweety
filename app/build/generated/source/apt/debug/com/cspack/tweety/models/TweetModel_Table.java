package com.cspack.tweety.models;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.config.FlowManager;
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
public final class TweetModel_Table extends ModelAdapter<TweetModel> {
  /**
   * Primary Key */
  public static final Property<String> id = new Property<String>(TweetModel.class, "id");

  public static final Property<String> text = new Property<String>(TweetModel.class, "text");

  /**
   * Foreign Key */
  public static final Property<String> user_id = new Property<String>(TweetModel.class, "user_id");

  public static final Property<String> createdAt = new Property<String>(TweetModel.class, "createdAt");

  public static final Property<String> firstPhotoUrl = new Property<String>(TweetModel.class, "firstPhotoUrl");

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{id,text,user_id,createdAt,firstPhotoUrl};

  public TweetModel_Table(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
    super(databaseDefinition);
  }

  @Override
  public final Class<TweetModel> getModelClass() {
    return TweetModel.class;
  }

  public final String getTableName() {
    return "`TweetModel`";
  }

  @Override
  public final BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`text`":  {
        return text;
      }
      case "`user_id`":  {
        return user_id;
      }
      case "`createdAt`":  {
        return createdAt;
      }
      case "`firstPhotoUrl`":  {
        return firstPhotoUrl;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }

  @Override
  public final void saveForeignKeys(TweetModel model, DatabaseWrapper wrapper) {
    if (model.user != null) {
      model.user.save(wrapper);
    }
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return ALL_COLUMN_PROPERTIES;
  }

  @Override
  public final void bindToInsertValues(ContentValues values, TweetModel model) {
    values.put("id", model.id != null ? model.id : null);
    values.put("text", model.text != null ? model.text : null);
    if (model.user != null) {
      values.put("user_id", model.user.id);
    } else {
      values.putNull("user_id");
    }
    values.put("createdAt", model.createdAt != null ? model.createdAt : null);
    values.put("firstPhotoUrl", model.firstPhotoUrl != null ? model.firstPhotoUrl : null);
  }

  @Override
  public final void bindToContentValues(ContentValues values, TweetModel model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, TweetModel model, int start) {
    if (model.id != null)  {
      statement.bindString(1 + start, model.id);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.text != null)  {
      statement.bindString(2 + start, model.text);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.user != null) {
      statement.bindString(3 + start, model.user.id);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.createdAt != null)  {
      statement.bindString(4 + start, model.createdAt);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.firstPhotoUrl != null)  {
      statement.bindString(5 + start, model.firstPhotoUrl);
    } else {
      statement.bindNull(5 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, TweetModel model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `TweetModel`(`id`,`text`,`user_id`,`createdAt`,`firstPhotoUrl`) VALUES (?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `TweetModel`(`id`,`text`,`user_id`,`createdAt`,`firstPhotoUrl`) VALUES (?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `TweetModel`(`id` TEXT,`text` TEXT,`user_id` TEXT,`createdAt` TEXT,`firstPhotoUrl` TEXT, PRIMARY KEY(`id`)"+ ", FOREIGN KEY(`user_id`) REFERENCES " + FlowManager.getTableName(UserModel.class) + "(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, TweetModel model) {
    int index_id = cursor.getColumnIndex("id");
    if (index_id != -1 && !cursor.isNull(index_id)) {
      model.id = cursor.getString(index_id);
    } else {
      model.id = null;
    }
    int index_text = cursor.getColumnIndex("text");
    if (index_text != -1 && !cursor.isNull(index_text)) {
      model.text = cursor.getString(index_text);
    } else {
      model.text = null;
    }
    int index_user_id = cursor.getColumnIndex("user_id");
    if (index_user_id != -1 && !cursor.isNull(index_user_id)) {
      model.user = SQLite.select().from(UserModel.class).where()
          .and(UserModel_Table.id.eq(cursor.getString(index_user_id)))
          .querySingle();
    } else {
      model.user = null;
    }
    int index_createdAt = cursor.getColumnIndex("createdAt");
    if (index_createdAt != -1 && !cursor.isNull(index_createdAt)) {
      model.createdAt = cursor.getString(index_createdAt);
    } else {
      model.createdAt = null;
    }
    int index_firstPhotoUrl = cursor.getColumnIndex("firstPhotoUrl");
    if (index_firstPhotoUrl != -1 && !cursor.isNull(index_firstPhotoUrl)) {
      model.firstPhotoUrl = cursor.getString(index_firstPhotoUrl);
    } else {
      model.firstPhotoUrl = null;
    }
  }

  @Override
  public final boolean exists(TweetModel model, DatabaseWrapper wrapper) {
    return SQLite.selectCountOf()
    .from(TweetModel.class)
    .where(getPrimaryConditionClause(model))
    .hasData(wrapper);
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(TweetModel model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(id.eq(model.id));
    return clause;
  }

  @Override
  public final TweetModel newInstance() {
    return new TweetModel();
  }
}
