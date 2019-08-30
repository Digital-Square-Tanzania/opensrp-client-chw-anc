package org.smartregister.chw.anc.util;

import android.content.Context;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.domain.db.Event;
import org.smartregister.domain.db.Obs;

import java.util.ArrayList;
import java.util.List;

public class NCUtilsTest extends BaseUnitTest {

    private Context context = RuntimeEnvironment.application;

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

}
