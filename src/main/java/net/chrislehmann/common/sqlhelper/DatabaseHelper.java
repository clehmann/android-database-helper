package net.chrislehmann.common.sqlhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "MessageDatabase";
    private static final int DB_VERSION = 1;

    java.util.List<net.chrislehmann.common.sqlhelper.Table> tables = new java.util.ArrayList<net.chrislehmann.common.sqlhelper.Table>();
    private static final String LOGTAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOGTAG, "Created DatabaseHelper");
    }


    public void addTable(Table table) {
        Log.d(LOGTAG, "Adding table " + table.getName());
        tables.add(table);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOGTAG, "Creating Database");
        for (Table table : tables) {
            Log.d(LOGTAG, "Creating table " +  table.getName());
            Log.d(LOGTAG, "Calling beforeCreate");
            table.beforeCreate(db);

            Log.d(LOGTAG, "Creating table");
            Log.d(LOGTAG, table.getCreateString());
            db.execSQL(table.getCreateString());

            Log.d(LOGTAG, "Calling afterCreate");
            table.afterCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOGTAG, "Upgrading Database");

    }

}
