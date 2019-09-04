package org.smartregister.chw.anc.model;

import android.content.Context;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.actionhelper.DangerSignsHelper;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;
import org.smartregister.chw.anc.util.JsonFormUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@PrepareForTest(JsonFormUtils.class)
public class BaseAncHomeVisitActionTest extends BaseUnitTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = BaseAncHomeVisitAction.ValidationException.class)
    public void testValidateMeError() throws BaseAncHomeVisitAction.ValidationException {
        Context context = RuntimeEnvironment.application;
        new BaseAncHomeVisitAction.Builder(context, "Visit title")
                .build();
    }

    @Test
    public void testInitializerReadsHelper() throws Exception {
        PowerMockito.mockStatic(JsonFormUtils.class);
        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        BDDMockito.given(JsonFormUtils.getFormAsJson(Mockito.anyString())).willReturn(jsonObject);


        Context context = RuntimeEnvironment.application;
        Map<String, List<VisitDetail>> details = new HashMap<>();

        DangerSignsHelper dangerSignsHelper = Mockito.spy(new DangerSignsHelper());
        String title = "Visit title";

        new BaseAncHomeVisitAction.Builder(context, title)
                .withDetails(details)
                .withSubtitle("Hello")
                .withOptional(false)
                .withFormName("danger_signs")
                .withHelper(dangerSignsHelper)
                .withScheduleStatus(BaseAncHomeVisitAction.ScheduleStatus.OVERDUE)
                .build();

        // verify that the danger signs helper is called
        Mockito.verify(dangerSignsHelper).getPreProcessedSubTitle();
        Mockito.verify(dangerSignsHelper).getPreProcessed();
        Mockito.verify(dangerSignsHelper).getPreProcessedStatus();

    }

    @Test
    public void testBuilderInitializesObject() throws Exception {
        PowerMockito.mockStatic(JsonFormUtils.class);
        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        BDDMockito.given(JsonFormUtils.getFormAsJson(Mockito.anyString())).willReturn(jsonObject);


        Context context = RuntimeEnvironment.application;
        Map<String, List<VisitDetail>> details = new HashMap<>();

        DangerSignsHelper dangerSignsHelper = Mockito.spy(new DangerSignsHelper());
        String title = "Visit title";
        String subTitle = "Hello";
        String formName = "danger_signs";

        BaseAncHomeVisitFragment fragment = Mockito.mock(BaseAncHomeVisitFragment.class);


        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, title)
                .withDetails(details)
                .withSubtitle(subTitle)
                .withOptional(false)
                .withFormName(formName)
                .withDestinationFragment(fragment)
                .withHelper(dangerSignsHelper)
                .withScheduleStatus(BaseAncHomeVisitAction.ScheduleStatus.OVERDUE)
                .build();


        assertEquals(action.getTitle(), title);
        assertEquals(action.getSubTitle(), subTitle);
        assertEquals(action.getDestinationFragment(), fragment);
        assertFalse(action.isOptional());
    }

    @Test
    public void testSetJsonPayloadNotifiesHelper() throws Exception {
        PowerMockito.mockStatic(JsonFormUtils.class);
        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        BDDMockito.given(JsonFormUtils.getFormAsJson(Mockito.anyString())).willReturn(jsonObject);


        Context context = RuntimeEnvironment.application;
        Map<String, List<VisitDetail>> details = new HashMap<>();

        BaseAncHomeVisitAction.AncHomeVisitActionHelper homeVisitActionHelper = Mockito.mock(BaseAncHomeVisitAction.AncHomeVisitActionHelper.class);
        String title = "Visit title";
        String subTitle = "Hello";
        String formName = "danger_signs";

        BaseAncHomeVisitFragment fragment = Mockito.mock(BaseAncHomeVisitFragment.class);

        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, title)
                .withDetails(details)
                .withSubtitle(subTitle)
                .withOptional(false)
                .withFormName(formName)
                .withDestinationFragment(fragment)
                .withHelper(homeVisitActionHelper)
                .withScheduleStatus(BaseAncHomeVisitAction.ScheduleStatus.OVERDUE)
                .build();

        String payload = "test payload";
        action.setJsonPayload(payload);
        Mockito.verify(homeVisitActionHelper).onPayloadReceived(payload);
    }
}
