package org.smartregister.chw.anc.presenter;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.contract.BaseAncRegisterContract;

public class BaseAncRegisterPresenterTest extends BaseUnitTest {

    @Mock
    private BaseAncRegisterContract.View view;
    @Mock
    private BaseAncRegisterContract.Interactor interactor;
    @Mock
    private BaseAncRegisterContract.Model model;

    private BaseAncRegisterPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        BaseAncRegisterPresenter obj = new BaseAncRegisterPresenter(view, model, interactor);
        presenter = Mockito.spy(obj);
    }

    @Test
    public void testStartForm() throws Exception {
        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        Mockito.doReturn(jsonObject).when(model).getFormAsJson(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        presenter.startForm("formName", "entityId", "metadata", "currentLocationId");
        Mockito.verify(view).startFormActivity(jsonObject);
    }

    @Test
    public void testSaveForm() {
        presenter.saveForm("jsonString", false, "table");
        Mockito.verify(interactor).saveRegistration(Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(BaseAncRegisterPresenter.class), Mockito.anyString());
    }

    @Test
    public void testOnRegistrationSaved() {
        presenter.onRegistrationSaved(false);
        Mockito.verify(view).hideProgressDialog();
    }
}
