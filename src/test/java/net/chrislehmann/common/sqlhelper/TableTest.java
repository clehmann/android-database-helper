package net.chrislehmann.common.sqlhelper;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TableTest {

    @Test
    public void testGenerateSql() throws Exception {
        Table table = new Table("TEST", null, null);
        assertEquals("create table TEST", table.getCreateString());
    }

    @Test
    public void testGenerateSqlWithColumn() throws Exception {
        Table table = new Table("TEST", null, null).addColumn(new IntegerColumn("COL"));
        assertEquals("create table TEST( COL integer )", table.getCreateString());
    }

    @Test
    public void testGenerateSqlWithMultipleColumns() throws Exception {
        Table table = new Table("TEST", null, null).addColumn(new IntegerColumn("COL")).addColumn(new Column("COL2", Column.Type.STRING));
        assertEquals("create table TEST( COL integer, COL2 text )", table.getCreateString());
    }

}
