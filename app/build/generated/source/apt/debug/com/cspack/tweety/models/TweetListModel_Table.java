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
import java.lang.Integer;
import java.lang.Number;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class TweetListModel_Table extends ModelAdapter<TweetListModel> {
  /**
   * Primary Key AutoIncrement */
  public static final Property<Integer> id = new Property<Integer>(TweetListModel.class, "id");

  public static final Property<Integer> pageType = new Property<Integer>(TweetListModel.class, "pageType");

  public static final Property<String> typeRefId = new Property<String>(TweetListModel.class, "typeRefId");

  /**
   * Foreign Key */
  public static final Property<String> tweet_id = new Property<String>(TweetListModel.class, "tweet_id");

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{id,pageType,typeRefId,tweet_id};

  public TweetListModel_Table(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
    super(databaseDefinition);
  }

  @Override
  public final Class<TweetListModel> getModelClass() {
    return TweetListModel.class;
  }

  public final String getTableName() {
    return "`TweetListModel`";
  }

  @Override
  public final BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`pageType`":  {
        return pageType;
      }
      case "`typeRefId`":  {
        return typeRefId;
      }
      case "`tweet_id`":  {
        return tweet_id;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }

  public final void updateAutoIncrement(TweetListModel model, Number id) {
    model.id = id.intValue();
  }

  @Override
  public final Number getAutoIncrementingId(TweetListModel model) {
    return model.id;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "id";
  }

  @Override
  public final void saveForeignKeys(TweetListModel model, DatabaseWrapper wrapper) {
    if (model.tweet != null) {
      model.tweet.save(wrapper);
    }
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return ALL_COLUMN_PROPERTIES;
  }

  @Override
  public final void bindToInsertValues(ContentValues values, TweetListModel model) {
    values.put("pageType", model.pageType != null ? model.pageType : null);
    values.put("typeRefId", model.typeRefId != null ? model.typeRefId : null);
    if (model.tweet != null) {
      values.put("tweet_id", model.tweet.id);
    } else {
      values.putNull("tweet_id");
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, TweetListModel model) {
    values.put("id", model.id != null ? model.id : null);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, TweetListModel model, int start) {
    if (model.pageType != null)  {
      statement.bindLong(1 + start, model.pageType);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.typeRefId != null)  {
      statement.bindString(2 + start, model.typeRefId);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.tweet != null) {
      statement.bindString(3 + start, model.tweet.id);
    } else {
      statement.bindNull(3 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, TweetListModel model) {
    int start = 0;
    if (model.id != null)  {
      statement.bindLong(1 + start, model.id);
    } else {
      statement.bindNull(1 + start);
    }
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `TweetListModel`(`pageType`,`typeRefId`,`tweet_id`) VALUES (?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `TweetListModel`(`id`,`pageType`,`typeRefId`,`tweet_id`) VALUES (?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `TweetListModel`(`id` INTEGER PRIMARY KEY AUTOINCREMENT,`pageType` INTEGER UNIQUE ON CONFLICT REPLACE,`typeRefId` TEXT UNIQUE ON CONFLICT REPLACE,`tweet_id` TEXT"+ ", FOREIGN KEY(`tweet_id`) REFERENCES " + FlowManager.getTableName(TweetModel.class) + "(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, TweetListModel model) {
    int index_id = cursor.getColumnIndex("id");
    if (index_id != -1 && !cursor.isNull(index_id)) {
      model.id = cursor.getInt(index_id);
    } else {
      model.id = null;
    }
    int index_pageType = cursor.getColumnIndex("pageType");
    if (index_pageType != -1 && !cursor.isNull(index_pageType)) {
      model.pageType = cursor.getInt(index_pageType);
    } else {
      model.pageType = null;
    }
    int index_typeRefId = cursor.getColumnIndex("typeRefId");
    if (index_typeRefId != -1 && !cursor.isNull(index_typeRefId)) {
      model.typeRefId = cursor.getString(index_typeRefId);
    } else {
      model.typeRefId = null;
    }
    int index_tweet_id = cursor.getColumnIndex("tweet_id");
    if (index_tweet_id != -1 && !cursor.isNull(index_tweet_id)) {
      model.tweet = SQLite.select().from(TweetModel.class).where()
          .and(TweetModel_Table.id.eq(cursor.getString(index_tweet_id)))
          .querySingle();
    } else {
      model.tweet = null;
    }
  }

  @Override
  public final boolean exists(TweetListModel model, DatabaseWrapper wrapper) {
    return (model.id != null && model.id > 0 || model.id == null)
    && SQLite.selectCountOf()
    .from(TweetListModel.class)
    .where(getPrimaryConditionClause(model))
    .hasData(wrapper);
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(TweetListModel model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(id.eq(model.id));
    return clause;
  }

  @Override
  public final TweetListModel newInstance() {
    return new TweetListModel();
  }
}
