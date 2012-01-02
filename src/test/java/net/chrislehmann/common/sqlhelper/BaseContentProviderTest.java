package net.chrislehmann.common.sqlhelper;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
@Ignore
public class BaseContentProviderTest {

    private BaseContentProvider contentProvider;

    @Before
    public void setUp() throws Exception {
        contentProvider = new BaseContentProvider();
    }

    @Test
    public void testOnCreateCallsSuper() throws Exception {
        contentProvider.onCreate();
    }
}
