package net.chrislehmann.common.sqlhelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Column> columnList = new ArrayList<Column>();
    private String name;

    private final String COLUMN_DEFINITION_SEPERATOR = ", ";
    private DatabaseHelper databaseHelper;
    private Context context;
    private static final String LOGTAG = "Table";

    public Table(String name, DatabaseHelper databaseHelper, Context context) {
        this.name = name;
        this.databaseHelper = databaseHelper;
        if (databaseHelper != null) {
            databaseHelper.addTable(this);
        }
        this.context = context;
    }

    public String getCreateString() {
        String sql = "create table " + name;
        if (columnList.size() > 0) {
            sql += "( ";
            for (Column column : columnList) {
                sql += column.getCreateString();
                sql += COLUMN_DEFINITION_SEPERATOR;
            }

            sql = StringUtils.removeEnd(sql, COLUMN_DEFINITION_SEPERATOR);
            sql += " )";
        }
        return sql;
    }


    public void beforeCreate(SQLiteDatabase db) {
    }

    public void afterCreate(SQLiteDatabase db) {
    }

    public Table addColumn(Column column) {
        columnList.add(column);
        return this;
    }

    public String getName() {
        return name;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return databaseHelper.getReadableDatabase().query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
    }


    public Uri insert(Uri uri, ContentValues values) {
        long id = databaseHelper.getWritableDatabase().insertOrThrow(getName(), "ID", values);
        context.getContentResolver().notifyChange(uri, null);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numDeleted = databaseHelper.getWritableDatabase().delete(getName(), selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);
        return numDeleted;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int numUpdated = databaseHelper.getWritableDatabase().update(getName(), values, selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);
        return numUpdated;
    }
}
