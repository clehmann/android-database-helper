package net.chrislehmann.common.sqlhelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {


    java.util.List<net.chrislehmann.common.sqlhelper.Table> tables = new java.util.ArrayList<net.chrislehmann.common.sqlhelper.Table>();
    private static final String LOGTAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        Log.d(LOGTAG, "Created DatabaseHelper");
    }


    public void addTable(Table table) {
        Log.d(LOGTAG, "Adding table " + table.getName());
        if (!tables.contains(table)) {
            tables.add(table);
        }
        table.setDatabaseHelper(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOGTAG, "Creating Database");
        for (Table table : tables) {
            Log.d(LOGTAG, "Creating table " + table.getName());
            Log.d(LOGTAG, "Calling beforeCreate");
            table.beforeCreate(db);

            Log.d(LOGTAG, "Creating table");
            Log.d(LOGTAG, table.getCreateString());
            try {
                db.execSQL(table.getCreateString());
            } catch (SQLException e) {
                Log.e(LOGTAG, "Could not create table " + table.getName(), e);
            }

            Log.d(LOGTAG, "Calling afterCreate");
            table.afterCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOGTAG, "Upgrading Database");

        //TODO - This obviously needs to be better than this...
        if (oldVersion < newVersion) {
            try {
                Log.d(LOGTAG, "Dropping old tables");
                for (Table table : tables) {
                    String sql = String.format("drop table %s", table.getName());
                    db.execSQL(sql);
                }
            } catch (SQLException e) {
                Log.e(LOGTAG, "Error dropping tables.", e);
            }
            onCreate(db);
        }

    }
}
