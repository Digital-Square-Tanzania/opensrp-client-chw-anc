package org.smartregister.chw.anc.presenter;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.util.JsonFormUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@PrepareForTest(JsonFormUtils.class)
public class BaseAncHomeVisitPresenterTest extends BaseUnitTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    @Mock
    private MemberObject memberObject;
    @Mock
    private BaseAncHomeVisitContract.View view;
    @Mock
    private BaseAncHomeVisitContract.Interactor interactor;

    private BaseAncHomeVisitPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        BaseAncHomeVisitPresenter obj = new BaseAncHomeVisitPresenter(memberObject, view, interactor);
        presenter = Mockito.spy(obj);
    }

    @Test
    public void testStartFormOpensForm() throws Exception {
        PowerMockito.mockStatic(JsonFormUtils.class);
        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        BDDMockito.given(JsonFormUtils.getFormAsJson(Mockito.anyString())).willReturn(jsonObject);

        presenter.startForm(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(view).startFormActivity(jsonObject);
    }

    @Test
    public void testInitializeLoadsActions() {
        presenter.initialize();
        Mockito.verify(interactor).calculateActions(view, memberObject, presenter);
    }

    @Test
    public void testSubmitVisit() {
        presenter.submitVisit();
        Map<String, BaseAncHomeVisitAction> map = new HashMap<>();
        Mockito.doReturn(false).when(view).getEditMode();
        Mockito.doReturn(map).when(view).getAncHomeVisitActions();

        Mockito.verify(view, Mockito.times(2)).displayProgressBar(true);
        Mockito.verify(interactor).submitVisit(false, null, map, presenter);
    }

    @Test
    public void testPreloadActions() {
        LinkedHashMap<String, BaseAncHomeVisitAction> map = new LinkedHashMap<>();
        presenter.preloadActions(map);

        Mockito.verify(view).initializeActions(map);
    }

    @Test
    public void testOnSubmitted() {

        presenter.onSubmitted(true);
        Mockito.verify(view).displayProgressBar(false);
        Mockito.verify(view).close();
    }

}
