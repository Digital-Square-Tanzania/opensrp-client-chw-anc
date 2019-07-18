package org.smartregister.chw.anc.util;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@PrepareForTest(VisitUtils.class)
public class VisitUtilsTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void testVisitsAreGrouped() {
        PowerMockito.mockStatic(VisitUtils.class);
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit());

        List<VisitDetail> visitDetails = new ArrayList<>();

        VisitDetail d1 = new VisitDetail();
        d1.setVisitKey("asd");

        VisitDetail d2 = new VisitDetail();
        d2.setVisitKey("bcd");

        BDDMockito.given(VisitUtils.getVisitsOnly(Mockito.anyString())).willReturn(visits);
        BDDMockito.given(VisitUtils.getVisitDetailsOnly(Mockito.anyString())).willReturn(visitDetails);

        assertEquals(visits.get(0).getVisitDetails().size(), visitDetails.size());
    }
}
