package net.chrislehmann.common.sqlhelper.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ProviderTestCase2;
import net.chrislehmann.common.sqlhelper.BaseContentProvider;

public class HelloAndroidActivityTest extends ProviderTestCase2<BaseContentProvider> {

    public HelloAndroidActivityTest() {
        super(BaseContentProvider.class, "");
    }

    public void testActivity() {
        BaseContentProvider provider = getProvider();
        assertNotNull(provider);
    }
}

