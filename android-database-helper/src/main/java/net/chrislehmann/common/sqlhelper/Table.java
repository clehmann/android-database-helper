package net.chrislehmann.common.sqlhelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Column> columnList = new ArrayList<Column>();
    private String name;

    private final String COLUMN_DEFINITION_SEPERATOR = ", ";
    protected DatabaseHelper databaseHelper;
    private Context context;
    private static final String LOGTAG = "Table";

    public Table(String name, Context context) {
        this(name, null, context);
    }

    public static class SelectionCriteria{
        private String selectionString = "";
        private String[] values = {};

        public SelectionCriteria(String selectionString, String[] values) {
            this.selectionString = selectionString;
            this.values = values;
        }

        public SelectionCriteria() {}

        public String getSelectionString() {
            return selectionString;
        }

        public String[] getValues() {
            return values;
        }
    }

    public Table(String name, DatabaseHelper databaseHelper, Context context) {
        this.name = name;
        this.databaseHelper = databaseHelper;
        if (databaseHelper != null) {
            databaseHelper.addTable(this);
        }
        this.context = context;
    }

    public static <T extends  Object> ContentValues createContentValues(T ... values){
        ContentValues contentValues = new ContentValues();
        for(int i = 0; i < values.length; i+=2){
            Column column = (Column) values[i];
            Object value = values[i + 1];
            if( value instanceof  String ){
                contentValues.put(column.getName(), (String) value);
            } else  if (value instanceof Integer){
                contentValues.put(column.getName(), (Integer) value);
            }else  if (value instanceof Long){
                contentValues.put(column.getName(), (Long) value);
            }else  if (value instanceof Boolean ){
                contentValues.put(column.getName(), (Boolean) value);
            }
        }
        return  contentValues;
    }


    public static <T extends Object> SelectionCriteria createAndSelectionCriteria(T... values) {
        return createSelectionCriteria("AND", values);
    }
    public static <T extends Object> SelectionCriteria createSelectionCriteria(String operator, T... values) {
        List<String> colNames = new ArrayList<String>();
        List<String> valueStrings = new ArrayList<String>();
        for (int i = 0; i < values.length; i += 2) {
            Column column = (Column) values[i];
            Object value = values[i + 1];
            colNames.add(column.getName() + " = ?");
            valueStrings.add(value.toString());
        }

        return new SelectionCriteria(StringUtils.join(colNames, " " + operator + " "), valueStrings.toArray(new String[]{}));
    }

    public static <T extends Object> SelectionCriteria appendSelectionCriteria(SelectionCriteria existingSelectionCriteria, String operator, T... values) {
        SelectionCriteria newCriteria = createSelectionCriteria(operator, values);
        String selectionString = existingSelectionCriteria.getSelectionString() + " " + operator + " " + newCriteria.getSelectionString();
        String seperator = " " + operator + " ";
        if( selectionString.startsWith(seperator) ){
            selectionString = newCriteria.getSelectionString();
        }
        if( selectionString.endsWith(seperator)){
            selectionString = existingSelectionCriteria.getSelectionString();
        }
        return new SelectionCriteria(
                selectionString,
                (String[]) ArrayUtils.addAll(existingSelectionCriteria.getValues(), newCriteria.getValues())
        );

    }


    public static String getStringValue(Column column, Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(column.getName()));
    }

    public static Integer getIntValue(Column column, Cursor cursor){
        return cursor.getInt(cursor.getColumnIndex(column.getName()));
    }

    public String getCreateString() {
        Log.d(LOGTAG, "columns: " + columnList);
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
        Log.d(LOGTAG, "Create table sql: " + sql);

        return sql;
    }

    public void beforeCreate(SQLiteDatabase db) {
    }

    public void afterCreate(SQLiteDatabase db) {
    }

    public void addColumn(Column column) {
        Log.d(LOGTAG, "Added column " + column.getName());
        columnList.add(column);
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

    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
}
