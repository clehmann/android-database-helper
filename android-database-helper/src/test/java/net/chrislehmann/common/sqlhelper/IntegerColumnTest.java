package net.chrislehmann.common.sqlhelper;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class IntegerColumnTest {

    @Test
    public void testAutoIncrementWorks() throws Exception {
        IntegerColumn integerColumn = new IntegerColumn("TEST").setAutoIncrement(true);
        assertEquals( "TEST integer autoincrement", integerColumn.getCreateString() );
    }
}
