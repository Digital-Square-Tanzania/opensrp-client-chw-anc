package org.smartregister.chw.anc.util;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.domain.VisitDetail;

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
    public void testGetStringResourceByName(){

        // existing string
        String existing_string = NCUtils.getStringResourceByName("opv_0", context);
        Assert.assertEquals(existing_string,"OPV 0");

        String null_context = NCUtils.getStringResourceByName("opv_0", null);
        Assert.assertEquals(null_context,"opv_0");

        String none_existing = NCUtils.getStringResourceByName("opv_10", null);
        Assert.assertEquals(none_existing,"opv_10");
    }

}
