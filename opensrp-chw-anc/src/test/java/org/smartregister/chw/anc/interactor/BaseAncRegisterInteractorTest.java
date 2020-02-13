package org.smartregister.chw.anc.interactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.anc.contract.BaseAncRegisterContract;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.anc.util.NCUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import timber.log.Timber;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(MockitoJUnitRunner.class)
public class BaseAncRegisterInteractorTest implements Executor {
    private BaseAncRegisterInteractor interactor;

    @Mock
    private BaseAncRegisterContract.Model model;

    @Mock
    private BaseAncRegisterContract.InteractorCallBack callBack;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AppExecutors appExecutors = new AppExecutors(this, this, this);
        interactor = Mockito.spy(new BaseAncRegisterInteractor(appExecutors));
    }

    @Test
    public void testGetModel() {
        BaseAncRegisterContract.Model ancModel = interactor.getModel();
        Assert.assertNotNull(ancModel);

        interactor.setModel(model);
        Assert.assertEquals(model, interactor.getModel());
    }

    @Test
    public void testSaveRegistration() {
        interactor.saveRegistration("{}", false, callBack, "ec_anc_register");

        Mockito.verify(callBack).onRegistrationSaved(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean());
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    @Test
    public void testGetChildFieldMaps() throws Exception {
        JSONArray jsonArray = new JSONArray();
        Map<String, List<JSONObject>> getValue = Whitebox.invokeMethod(interactor, "getChildFieldMaps", jsonArray);
        Assert.assertNotNull(getValue);
        Assert.assertEquals(0, getValue.size());
        for (Map.Entry<String, List<JSONObject>> entry : getValue.entrySet()) {
            Assert.assertEquals(any(String.class), entry.getValue().get(0).toString());
            break;
        }
    }

    @Test
    public void testGetVaccineList() throws Exception {
        JSONArray jsonArray = new JSONArray();
        Map<String, List<JSONObject>> getValue = Whitebox.invokeMethod(interactor, "getChildFieldMaps", jsonArray);
        Assert.assertNotNull(getValue);
        Assert.assertEquals(0, getValue.size());
        for (Map.Entry<String, List<JSONObject>> entry : getValue.entrySet()) {
            Assert.assertEquals(any(String.class), entry.getValue().get(0).toString());
            break;
        }
    }

    @Test
    public void whenGenerateAndSaveFormsForEachChildCalledAnswered() {
        Map map = new HashMap<String, List<JSONObject>>();
        Mockito.doAnswer(invocation -> {
            Assert.assertEquals(new HashMap<String, List<JSONObject>>(), invocation.getArgument(0));
            Assert.assertEquals("motherBaseId", invocation.getArgument(1));
            Assert.assertEquals("familyBaseEntityId", invocation.getArgument(2));
            Assert.assertEquals("dob", invocation.getArgument(3));
            Assert.assertEquals("familyName", invocation.getArgument(4));
            return null;
        }).when(interactor).generateAndSaveFormsForEachChild(any(Map.class), any(String.class), any(String.class), any(String.class), any(String.class));
        interactor.generateAndSaveFormsForEachChild(map, "motherBaseId", "familyBaseEntityId", "dob", "familyName");
    }

    @Test
    public void testSameASFamilyNameCheck() throws Exception {
        JSONArray jsonArray = new JSONArray();
        boolean sameASFamilyNameCheck = Whitebox.invokeMethod(interactor, "sameASFamilyNameCheck", jsonArray);
        Assert.assertFalse(sameASFamilyNameCheck);
    }

    @Test
    public void testVaccinationDate() throws Exception {
        Date date = Whitebox.invokeMethod(interactor, "vaccinationDate", "02-02-2020");
        Assert.assertNotNull(date);
    }

}
