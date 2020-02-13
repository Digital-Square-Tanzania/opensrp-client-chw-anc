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
//    private String jsonString = "[{\"key\":\"first_name_640c9321e2f84bbb9fb71868f44ad1fd\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\",\"openmrs_entity_id\":\"first_name\",\"type\":\"edit_text\",\"hint\":\"First name\",\"edit_type\":\"name\",\"v_required\":{\"value\":\"true\",\"err\":\"Please enter the first name\"},\"v_regex\":{\"value\":\"[A-Za-z\\\\u00C0-\\\\u017F\\\\s\\\\u00C0-\\\\u017F\\\\.\\\\-]*\",\"err\":\"Please enter a valid name\"},\"value\":\"tet\"},{\"key\":\"middle_name_640c9321e2f84bbb9fb71868f44ad1fd\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\",\"openmrs_entity_id\":\"middle_name\",\"type\":\"edit_text\",\"hint\":\"Middle name\",\"edit_type\":\"name\",\"v_regex\":{\"value\":\"[A-Za-z\\\\u00C0-\\\\u017F\\\\s\\\\u00C0-\\\\u017F\\\\.\\\\-]*\",\"err\":\"Please enter a valid name\"},\"value\":\"te\"},{\"key\":\"dob_640c9321e2f84bbb9fb71868f44ad1fd\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\",\"openmrs_entity_id\":\"birthdate\",\"type\":\"hidden\",\"value\":\"\"}]";
//
//    private String jsonTestString = "{\"openmrs_entity\":\"person\",\"hint\":\"First name\",\"openmrs_entity_id\":\"first_name\",\"edit_type\":\"name\",\"v_required\":{\"err\":\"Please enter the first name\",\"value\":\"true\"},\"openmrs_entity_parent\":\"\",\"type\":\"edit_text\",\"value\":\"tet\",\"key\":\"first_name_640c9321e2f84bbb9fb71868f44ad1fd\",\"v_regex\":{\"err\":\"Please enter a valid name\"," +
//            "\"value\":\"[A-Za-z\\\\u00C0-\\\\u017F\\\\s\\\\u00C0-\\\\u017F\\\\.\\\\-]*\"}}";
//
//    private String childFields = "[{\"key\":\"same_as_fam_name\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"\",\"openmrs_data_type\":\"text\",\"type\":\"check_box\",\"options\":[{\"key\":\"same_as_fam_name\",\"text\":\"Surname same as family name\",\"text_size\":\"18px\",\"value\":true}],\"step\":\"step1\",\"is-rule-check\":true,\"value\":[\"same_as_fam_name\"]},{\"key\":\"first_name\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\",\"openmrs_entity_id\":\"first_name\",\"type\":\"edit_text\",\"hint\":\"First name\",\"edit_type\":\"name\",\"value\":\"ggg\"}]";


    private Map<String, List<JSONObject>> jsonObjectMap;
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
