package net.chrislehmann.common.sqlhelper;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ColumnTest {
    @Test
    public void testCreateReturnsCorrectSqlForInteger() throws Exception {
        Column integerColumn = new Column("TEST", Column.Type.INTEGER);
        String expectedSql = "TEST integer";
        assertEquals(expectedSql, integerColumn.getCreateString());
    }

    @Test
    public void testCreateReturnsCorrectSqlForString() throws Exception {
        Column integerColumn = new Column("TEST", Column.Type.STRING);
        String expectedSql = "TEST text";
        assertEquals(expectedSql, integerColumn.getCreateString());
    }

    @Test
    public void testCreateReturnsCorrectNotNull() throws Exception {
        Column integerColumn = new Column("TEST", Column.Type.STRING).setNullable(false);
        String expectedSql = "TEST text not null";
        assertEquals(expectedSql, integerColumn.getCreateString());
    }

    @Test
    public void testPrimaryKeyGeneratesCorrectSql() throws Exception {
        Column integerColumn = new Column("TEST", Column.Type.INTEGER).setPrimaryKey(true);
        assertEquals("TEST integer primary key", integerColumn.getCreateString());
    }
}
