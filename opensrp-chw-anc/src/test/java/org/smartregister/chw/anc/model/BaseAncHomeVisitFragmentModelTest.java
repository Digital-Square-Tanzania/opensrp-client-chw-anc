package org.smartregister.chw.anc.model;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitFragmentContract;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;

import java.util.List;

public class BaseAncHomeVisitFragmentModelTest extends BaseUnitTest {
    private String jsonString = "{\"step1\":{\"title\":\"Mid-upper arm circumference (MUAC)\",\"fields\":[{\"key\":\"muac\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"1719AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_data_type\":\"select one\",\"type\":\"radio\",\"image\":\"form_received_card\",\"label_info_title\":\"Test Title\",\"label_info_text\":\"Test text\",\"hint\":\"What is the color of the MUAC tape for the child?\",\"options\":[{\"key\":\"chk_green\",\"text\":\"Green\",\"value\":false,\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},{\"key\":\"chk_yellow\",\"text\":\"Yellow\",\"value\":false,\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"161156AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},{\"key\":\"chk_red\",\"text\":\"Red\",\"value\":false,\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"1356AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"}],\"value\":\"chk_green\"}]}}";
    private BaseAncHomeVisitFragmentModel ancRegisterFragmentModel;

    @Mock
    private BaseAncHomeVisitFragmentContract.Presenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ancRegisterFragmentModel = new BaseAncHomeVisitFragmentModel();
    }

    @Test
    public void testProcessJson() throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        BaseAncHomeVisitFragmentModel model = Mockito.spy(ancRegisterFragmentModel);
        model.processJson(jsonObject, presenter);

        Mockito.verify(presenter).setTitle(Mockito.anyString());
        Mockito.verify(presenter).setQuestion(Mockito.anyString());
        Mockito.verify(presenter).setImageRes(Mockito.anyInt());
        Mockito.verify(presenter).setQuestionType(Mockito.any(BaseAncHomeVisitFragment.QuestionType.class));
        Mockito.verify(presenter).setInfoIconTitle(Mockito.anyString());
        Mockito.verify(presenter).setInfoIconDetails(Mockito.anyString());
        Mockito.verify(presenter).setOptions(Mockito.any(List.class));
        Mockito.verify(presenter).setValue(Mockito.anyString());
    }

    @Test
    public void testWriteValue() throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        ancRegisterFragmentModel.writeValue(jsonObject, "Ona Data");
        Assert.assertNotNull(jsonObject);
        Assert.assertEquals(jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS).getJSONObject(0).getString(JsonFormConstants.VALUE), "Ona Data");

    }

    @Test
    public void testGetTitle() throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        String title = Whitebox.invokeMethod(ancRegisterFragmentModel, "getTitle", jsonObject);
        Assert.assertNotNull(title);
        Assert.assertEquals(title, "Mid-upper arm circumference (MUAC)");

    }

    @Test
    public void testGetQuestion() throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        String question = Whitebox.invokeMethod(ancRegisterFragmentModel, "getQuestion", jsonObject);
        Assert.assertNotNull(question);
        Assert.assertEquals(question, "What is the color of the MUAC tape for the child?");
    }

    @Test
    public void testGetOptions() throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        List<JSONObject> getOptions = Whitebox.invokeMethod(ancRegisterFragmentModel, "getOptions", jsonObject);
        Assert.assertNotNull(getOptions);
        Assert.assertEquals(3, getOptions.size());
    }

    @Test
    public void testGetValue() throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        String getValue = Whitebox.invokeMethod(ancRegisterFragmentModel, "getValue", jsonObject);
        Assert.assertNotNull(getValue);
        Assert.assertEquals(getValue, "chk_green");
    }

    @Test
    public void testGetInfoIconTitle() throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        String getValue = Whitebox.invokeMethod(ancRegisterFragmentModel, "getInfoIconTitle", jsonObject);
        Assert.assertNotNull(getValue);
        Assert.assertEquals(getValue, "Test Title");
    }

    @Test
    public void testGetInfoIconDetails() throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        String getValue = Whitebox.invokeMethod(ancRegisterFragmentModel, "getInfoIconDetails", jsonObject);
        Assert.assertNotNull(getValue);
        Assert.assertEquals(getValue, "Test text");
    }

}
