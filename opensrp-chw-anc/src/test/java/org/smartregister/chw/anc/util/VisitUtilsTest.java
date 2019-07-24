package org.smartregister.chw.anc.util;

import android.content.Context;

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
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@PrepareForTest({VisitUtils.class, AncLibrary.class})
public class VisitUtilsTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private AncLibrary ancLibrary;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private VisitDetailsRepository visitDetailsRepository;

    @Mock
    private Context context;

    @Mock
    private org.smartregister.Context smartContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(AncLibrary.class);

        BDDMockito.given(AncLibrary.getInstance()).willReturn(ancLibrary);
        Mockito.doReturn(visitRepository).when(ancLibrary).visitRepository();
        Mockito.doReturn(visitDetailsRepository).when(ancLibrary).visitDetailsRepository();

    }

    private List<Visit> getRandomVisits() {
        List<Visit> totalVisits = new ArrayList<>();
        int x = 10;
        while (x > 0) {
            Visit visit = new Visit();
            visit.setVisitId(String.valueOf(x));

            totalVisits.add(visit);
            x--;
        }
        return totalVisits;
    }

    private List<VisitDetail> getRandomVisitDetails() {
        List<VisitDetail> details = new ArrayList<>();
        int x = 5;
        while (x > 0) {
            details.add(new VisitDetail());
            x--;
        }
        return details;
    }

    @Test
    public void testGetVisitsReturnsVisitsFromDB() {
        List<Visit> totalVisits = getRandomVisits();
        List<VisitDetail> totalVisitDetails = getRandomVisitDetails();

        Mockito.doReturn(totalVisits).when(visitRepository).getVisits(Mockito.anyString(), Mockito.anyString());
        Mockito.doReturn(totalVisitDetails).when(visitDetailsRepository).getVisits(Mockito.anyString());

        String memberID = "12345";

        List<Visit> visits = VisitUtils.getVisits(memberID);
        assertEquals(visits.size(), totalVisits.size());
    }
}
