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
    protected String tableName;
    protected int version = 1;

    private Map<Integer, String> indicatorToContentTypeMap = new HashMap<Integer, String>();
    private Map<Integer, Table> indicatorToTableMap = new HashMap<Integer, Table>();

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private String authority;

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
        matchNo++;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext(), tableName, version);
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
        return getTableForUri(uri).insert(uri, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return getTableForUri(uri).delete(uri, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return getTableForUri(uri).update(uri, values, selection, selectionArgs);
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
