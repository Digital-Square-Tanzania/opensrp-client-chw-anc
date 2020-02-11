package org.smartregister.chw.anc.util;

import android.content.Context;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.domain.db.Event;
import org.smartregister.domain.db.Obs;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static org.mockito.ArgumentMatchers.any;

public class NCUtilsTest extends BaseUnitTest {

    private Context context = RuntimeEnvironment.application;

    private String childFields = "[{\"key\":\"same_as_fam_name\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"\",\"openmrs_data_type\":\"text\",\"type\":\"check_box\",\"options\":[{\"key\":\"same_as_fam_name\",\"text\":\"Surname same as family name\",\"text_size\":\"18px\",\"value\":true}],\"step\":\"step1\",\"is-rule-check\":true,\"value\":[\"same_as_fam_name\"]},{\"key\":\"first_name\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\",\"openmrs_entity_id\":\"first_name\",\"type\":\"edit_text\",\"hint\":\"First name\",\"edit_type\":\"name\",\"value\":\"ggg\"}]";

    @Mock
    private NCUtils ncUtils;

    private JSONArray childJsonArray;

    @Before
    public void setUp() {
        ncUtils = Mockito.spy(new NCUtils());
        try {
            childJsonArray = new JSONArray(childFields);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    @Test
    public void testGetText() {
        List<VisitDetail> details = new ArrayList<>();

        VisitDetail visitDetail1 = new VisitDetail();
        visitDetail1.setHumanReadable("test1");
        details.add(visitDetail1);

        VisitDetail visitDetail2 = new VisitDetail();
        visitDetail2.setHumanReadable("test2");
        details.add(visitDetail2);

        VisitDetail visitDetail3 = new VisitDetail();
        details.add(visitDetail3);

        details.add(null);

        String val = NCUtils.getText(details).trim();
        String expected = "test1, test2";

        Assert.assertEquals(expected, val);
    }

    @Test
    public void testGetTextOneParam() {

        VisitDetail visitDetail1 = new VisitDetail();
        visitDetail1.setHumanReadable("test1");

        String val = NCUtils.getText(visitDetail1).trim();
        String expected = "test1";

        Assert.assertEquals(expected, val);
    }

    @Test
    public void testGetStringResourceByName() {

        // existing string
        String existing_string = NCUtils.getStringResourceByName("opv_0", context);
        Assert.assertEquals(existing_string, "OPV 0");

        String null_context = NCUtils.getStringResourceByName("opv_0", null);
        Assert.assertEquals(null_context, "opv_0");

        String none_existing = NCUtils.getStringResourceByName("opv_10", null);
        Assert.assertEquals(none_existing, "opv_10");
    }

    public <T> List<T> toList(T... ts) {
        return new ArrayList<>(java.util.Arrays.asList(ts));
    }

    @Test
    public void testEventToVisit() throws JSONException {

        Event event = new Event();
        event.setBaseEntityId("12345");
        event.setEventDate(new DateTime());
        event.setEventType("Sample");
        event.setEventId("123456");
        event.setFormSubmissionId("345345345");

        Obs obs = new Obs();
        obs.setFormSubmissionField("test");
        obs.setParentCode("parent");
        obs.setValues(toList((Object) "value"));
        obs.setHumanReadableValues(toList((Object) "value"));


        Obs date_obs = new Obs();
        date_obs.setFormSubmissionField("vitamina_date");
        date_obs.setParentCode("parent");
        date_obs.setValues(toList((Object) "24-01-2010"));
        date_obs.setHumanReadableValues(toList((Object) ""));

        event.setObs(toList(obs, date_obs));

        Visit visit = NCUtils.eventToVisit(event);

        Assert.assertEquals(event.getBaseEntityId(), visit.getBaseEntityId());
        Assert.assertEquals(visit.getVisitDetails().size(), 2);
    }

    @Test
    public void getAncMemberProfileImageReturnsCorrectResourceIdentifier() {
        String memberType = "anc";
        int ancImageResourceIdentifier = R.drawable.anc_woman;
        Assert.assertEquals(ancImageResourceIdentifier, NCUtils.getMemberProfileImageResourceIDentifier(memberType));
    }

    @Test
    public void getPncemberProfileImageReturnsCorrectResourceIdentifier() {
        String memberType = "pnc";
        int pncImageResourceIdentifier = R.drawable.pnc_woman;
        Assert.assertEquals(pncImageResourceIdentifier, NCUtils.getMemberProfileImageResourceIDentifier(memberType));
    }

    @Test
    public void getMemberProfileImageReturnsCorrectResourceIDentifier() {
        int memberImageResourceIdentifier = R.mipmap.ic_member;
        Assert.assertEquals(memberImageResourceIdentifier, NCUtils.getMemberProfileImageResourceIDentifier(null));
    }


    @Test
    public void whenSaveVaccineCalledAnswered() {

        Mockito.doAnswer(invocation -> {
            Assert.assertEquals(childJsonArray, invocation.getArgument(0));
            Assert.assertEquals("motherBaseId", invocation.getArgument(1));
            Assert.assertEquals("familyBaseEntityId", invocation.getArgument(2));
            return null;
        }).when(ncUtils).saveVaccineEvents(any(JSONArray.class), any(String.class), any(String.class));
        ncUtils.saveVaccineEvents(childJsonArray, "motherBaseId", "familyBaseEntityId");
    }

}
