package net.chrislehmann.common.sqlhelper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import net.chrislehmann.common.sqlhelper.Column;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ContentProviderUtils {
    public static boolean rowExists(ContentResolver contentResolver, Uri uri, SelectionCriteria criteria) {

        Cursor cursor = contentResolver.query(uri, null, criteria.getSelectionString(), criteria.getValues(), null);
        return cursor.getCount() > 0;
    }

    public static <T extends Object> ContentValues createContentValues(T... values) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < values.length; i += 2) {
            Column column = (Column) values[i];
            Object value = values[i + 1];
            if (value instanceof String) {
                contentValues.put(column.getName(), (String) value);
            } else if (value instanceof Integer) {
                contentValues.put(column.getName(), (Integer) value);
            } else if (value instanceof Long) {
                contentValues.put(column.getName(), (Long) value);
            } else if (value instanceof Boolean) {
                contentValues.put(column.getName(), (Boolean) value);
            }
        }
        return contentValues;
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
            if (column != null && value != null) {
                colNames.add(column.getName() + " = ?");
                valueStrings.add(value.toString());
            }
        }

        return new SelectionCriteria(StringUtils.join(colNames, " " + operator + " "), valueStrings.toArray(new String[]{}));
    }

    public static <T extends Object> SelectionCriteria appendSelectionCriteria(SelectionCriteria existingSelectionCriteria, String operator, T... values) {
        SelectionCriteria newCriteria = createSelectionCriteria(operator, values);
        String selectionString = existingSelectionCriteria.getSelectionString() + " " + operator + " " + newCriteria.getSelectionString();
        String seperator = " " + operator + " ";
        if (selectionString.startsWith(seperator)) {
            selectionString = newCriteria.getSelectionString();
        }
        if (selectionString.endsWith(seperator)) {
            selectionString = existingSelectionCriteria.getSelectionString();
        }
        return new SelectionCriteria(
                selectionString,
                (String[]) ArrayUtils.addAll(existingSelectionCriteria.getValues(), newCriteria.getValues())
        );

    }

    public static String getStringValue(Column column, Cursor cursor) {
        return isCursorValid(cursor) && ArrayUtils.contains(cursor.getColumnNames(), column.getName()) ? cursor.getString(cursor.getColumnIndex(column.getName())) : null;
    }

    public static Integer getIntValue(Column column, Cursor cursor) {
        return isCursorValid(cursor) && ArrayUtils.contains(cursor.getColumnNames(), column.getName()) ? cursor.getInt(cursor.getColumnIndex(column.getName())) : null;
    }

    public static Long getLongValue(Column column, Cursor cursor) {
        return isCursorValid(cursor) && ArrayUtils.contains(cursor.getColumnNames(), column.getName()) ? cursor.getLong(cursor.getColumnIndex(column.getName())) : null;
    }

    public static boolean isCursorValid(Cursor cursor) {
        return cursor != null && !cursor.isClosed() && !cursor.isBeforeFirst() && !cursor.isAfterLast();
    }

    public static SelectionCriteria createInCriteria(Column columnName, String... values) {
        String string = columnName.getName() + " in ( ";
        for (String value : values) {
            string += " ?,";
        }
        if (string.endsWith(",")) {
            string = string.substring(0, string.length() - 1);
        }
        string += ")";
        return new SelectionCriteria(string, values);
    }

    public static class SelectionCriteria {
        private String selectionString = "";
        private String[] values = {};

        public SelectionCriteria(String selectionString, String[] values) {
            this.selectionString = selectionString;
            this.values = values;
        }

        public SelectionCriteria() {
        }

        public String getSelectionString() {
            return selectionString;
        }

        public String[] getValues() {
            return values;
        }
    }
}

