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
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class TweetListModel_Table extends ModelAdapter<TweetListModel> {
  /**
   * Primary Key */
  public static final Property<Integer> pageType = new Property<Integer>(TweetListModel.class, "pageType");

  /**
   * Primary Key */
  public static final Property<String> typeRefId = new Property<String>(TweetListModel.class, "typeRefId");

  /**
   * Foreign Key / Primary Key */
  public static final Property<String> tweetId = new Property<String>(TweetListModel.class, "tweetId");

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{pageType,typeRefId,tweetId};

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
      case "`pageType`":  {
        return pageType;
      }
      case "`typeRefId`":  {
        return typeRefId;
      }
      case "`tweetId`":  {
        return tweetId;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
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
      values.put("tweetId", model.tweet.id);
    } else {
      values.putNull("tweetId");
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, TweetListModel model) {
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
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `TweetListModel`(`pageType`,`typeRefId`,`tweetId`) VALUES (?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `TweetListModel`(`pageType`,`typeRefId`,`tweetId`) VALUES (?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `TweetListModel`(`pageType` INTEGER,`typeRefId` TEXT,`tweetId` TEXT, PRIMARY KEY(`pageType`,`typeRefId`,`tweetId`)"+ ", FOREIGN KEY(`tweetId`) REFERENCES " + FlowManager.getTableName(TweetModel.class) + "(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, TweetListModel model) {
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
    int index_tweetId = cursor.getColumnIndex("tweetId");
    if (index_tweetId != -1 && !cursor.isNull(index_tweetId)) {
      model.tweet = SQLite.select().from(TweetModel.class).where()
          .and(TweetModel_Table.id.eq(cursor.getString(index_tweetId)))
          .querySingle();
    } else {
      model.tweet = null;
    }
  }

  @Override
  public final boolean exists(TweetListModel model, DatabaseWrapper wrapper) {
    return SQLite.selectCountOf()
    .from(TweetListModel.class)
    .where(getPrimaryConditionClause(model))
    .hasData(wrapper);
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(TweetListModel model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(pageType.eq(model.pageType));
    clause.and(typeRefId.eq(model.typeRefId));
    if (model.tweet != null) {
      clause.and(tweetId.eq(model.tweet.id));
    } else {
      clause.and(tweetId.eq((com.raizlabs.android.dbflow.sql.language.IConditional) null));
    }
    return clause;
  }

  @Override
  public final TweetListModel newInstance() {
    return new TweetListModel();
  }
}
