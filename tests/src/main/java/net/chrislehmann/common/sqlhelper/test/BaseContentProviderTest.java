package net.chrislehmann.common.sqlhelper.test;

import android.net.Uri;
import android.test.ProviderTestCase2;
import net.chrislehmann.common.sqlhelper.BaseContentProvider;
import net.chrislehmann.common.sqlhelper.Table;

public class BaseContentProviderTest extends ProviderTestCase2<BaseContentProvider> {

    private BaseContentProvider provider;
    private static final String AUTHORITY = "authority";
    public static final Uri VALID_URI = Uri.parse("content://" + AUTHORITY + "/videos");
    private static final String CONTENT_TYPE = "contentType";

    private Table table;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        provider = getProvider();
        provider.setAuthority(AUTHORITY);
        table = new Table("myTable", null, null);
        provider.addTable("videos", table, CONTENT_TYPE);
    }

    public BaseContentProviderTest() {
        super(BaseContentProvider.class, "");
    }

    public void testActivity() {
        assertNotNull(provider);
    }

    public void testGetTableForUri_tableMatches_returnsTable() {
        Table actualTable = provider.getTableForUri(VALID_URI);
        assertEquals(table, actualTable);
    }

    public void testGetTableForUri_tableDoesMatches_throwsException() {
        try {
            provider.getTableForUri(Uri.parse("content://mybadurl"));
            fail("Should have thrown");
        } catch (IllegalArgumentException iee) {
        }
    }

    public void testGetType_tableMatches_returnsType() {
        assertEquals(CONTENT_TYPE, provider.getType(VALID_URI));
    }

    public void testGetType_tableDoesNotMatch_throwsException() {
        try {
            provider.getType(Uri.parse("content://urithatshouldnevermatch"));
            fail("Should have thrown");
        } catch (IllegalArgumentException iae) {
        }
    }

}

