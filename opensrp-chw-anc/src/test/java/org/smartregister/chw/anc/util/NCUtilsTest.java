package org.smartregister.chw.anc.util;

import org.junit.Assert;
import org.junit.Test;
import org.smartregister.chw.anc.domain.VisitDetail;

import java.util.ArrayList;
import java.util.List;

public class NCUtilsTest {

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

}
