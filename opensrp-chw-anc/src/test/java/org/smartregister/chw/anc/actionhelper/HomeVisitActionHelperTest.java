package org.smartregister.chw.anc.actionhelper;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Ensure default helper methods are inert to home visit action
 */
public class HomeVisitActionHelperTest extends BaseUnitTest {

    private HomeVisitActionHelper helper;

    @Before
    public void setUp() {
        helper = Mockito.mock(HomeVisitActionHelper.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testOnJsonFormLoadedInitializesContext() {
        Context context = Mockito.mock(Context.class);
        helper.onJsonFormLoaded(null, context, null);

        assertEquals(context, helper.getContext());
    }

    @Test
    public void testGetPreProcessedIsNull() {
        assertNull(helper.getPreProcessed());
    }

    @Test
    public void testGetPreProcessedStatus() {
        assertEquals(BaseAncHomeVisitAction.ScheduleStatus.DUE, helper.getPreProcessedStatus());
    }

    @Test
    public void testGetPreProcessedSubTitle() {
        assertNull(helper.getPreProcessedSubTitle());
    }

    @Test
    public void testPostProcessIsInert() {
        assertNull(helper.postProcess("Random string"));
    }

    @Test
    public void testOnPayloadReceived() {

        BaseAncHomeVisitAction action = Mockito.mock(BaseAncHomeVisitAction.class);
        int hashCode = action.hashCode();

        helper.onPayloadReceived(action);

        assertEquals(hashCode, action.hashCode());
    }
}
