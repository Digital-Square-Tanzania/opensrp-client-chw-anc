package org.smartregister.chw.anc;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.chw.anc.application.TestChwApplication;
import org.smartregister.chw.opensrp_chw_anc.BuildConfig;


@RunWith(RobolectricTestRunner.class)
@Config(application = TestChwApplication.class, constants = BuildConfig.class, sdk = 22)
public abstract class BaseUnitTest {

}
