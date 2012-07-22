package net.chrislehmann.common.sqlhelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class BaseContentProvider extends ContentProvider {

    private Integer matchNo = 0;
    protected DatabaseHelper databaseHelper;
    protected String databaseName;
    protected int version = 2;
    protected String authority;

    private Map<Integer, String> indicatorToContentTypeMap = new HashMap<Integer, String>();
    private Map<Integer, Table> indicatorToTableMap = new HashMap<Integer, Table>();

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    public Table getTableForUri(Uri theUri) {
        Integer match = uriMatcher.match(theUri);
        Table table = indicatorToTableMap.get(match);
        if (table == null) {
            throw new IllegalArgumentException("Don't know how to handle url '" + theUri + "'");
        }
        return table;
    }

    public void addTable(String uri, Table table, String contentType) {
        indicatorToContentTypeMap.put(matchNo, contentType);
        indicatorToTableMap.put(matchNo, table);
        uriMatcher.addURI(authority, uri, matchNo);
        databaseHelper.addTable(table);
        matchNo++;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext(), databaseName, version);
        return true;
    }


    @Override
    public String getType(Uri uri) {
        int uriType = uriMatcher.match(uri);
        if (!indicatorToContentTypeMap.containsKey(uriType)) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return indicatorToContentTypeMap.get(uriType);
    }

    @Override
    public Cursor query(Uri theUri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return getTableForUri(theUri).query(theUri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri = getTableForUri(uri).insert(uri, values);
        getContext().getContentResolver().notifyChange(uri, null);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int numUpdated = getTableForUri(uri).update(uri, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return numUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numDeleted =  getTableForUri(uri).delete(uri, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return numDeleted;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
