package org.smartregister.chw.anc.util;

import org.joda.time.DateTime;
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
import org.smartregister.Context;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.repository.AllSharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@PrepareForTest({AncLibrary.class, JsonFormUtils.class, ImmunizationLibrary.class})
public class VisitNCUtilsTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private AncLibrary ancLibrary;

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private VisitDetailsRepository visitDetailsRepository;

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private Context context;

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(AncLibrary.class);
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.mockStatic(JsonFormUtils.class);

        BDDMockito.given(AncLibrary.getInstance()).willReturn(ancLibrary);
        Mockito.doReturn(visitRepository).when(ancLibrary).visitRepository();
        Mockito.doReturn(visitDetailsRepository).when(ancLibrary).visitDetailsRepository();

        BDDMockito.given(ImmunizationLibrary.getInstance()).willReturn(immunizationLibrary);
        BDDMockito.given(NCUtils.context()).willReturn(context);
        Mockito.doReturn(allSharedPreferences).when(context).allSharedPreferences();
        Mockito.doReturn(recurringServiceRecordRepository).when(immunizationLibrary).recurringServiceRecordRepository();
        Mockito.doReturn(vaccineRepository).when(immunizationLibrary).vaccineRepository();

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
    public void testIsVisitWithin24HoursReturnsAppropriateBoolean() {
        Date visitDate = new Date();
        assertFalse(VisitUtils.isVisitWithin24Hours(null));
        Visit visit = new Visit();
        visit.setDate(visitDate);
        assertTrue(VisitUtils.isVisitWithin24Hours(visit));
        String dateString = "January 2, 2019";
        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        try {
            visitDate = dateFormat.parse(dateString);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        visit.setDate(visitDate);
        assertFalse(VisitUtils.isVisitWithin24Hours(visit));
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

    private List<ServiceWrapper> getRandomWrappers(Long dbKey) {
        List<ServiceWrapper> details = new ArrayList<>();
        int x = 5;
        while (x > 0) {
            ServiceWrapper sw = new ServiceWrapper();
            if (dbKey != null)
                sw.setDbKey(dbKey);
            sw.setUpdatedVaccineDate(new DateTime(), true);
            details.add(sw);
            x--;
        }
        return details;
    }

    @Test
    public void testSaveServicesCreatesInServicesRepository() {
        List<ServiceWrapper> wrappers = getRandomWrappers(null);

        VisitUtils.saveServices(wrappers, "12345");
        Mockito.verify(recurringServiceRecordRepository, Mockito.times(wrappers.size())).add(Mockito.any(ServiceRecord.class));
    }

    @Test
    public void testSaveServicesUpdatesInServicesRepository() {
        List<ServiceWrapper> wrappers = getRandomWrappers(0L);

        VisitUtils.saveServices(wrappers, "12345");
        Mockito.verify(recurringServiceRecordRepository, Mockito.times(wrappers.size())).find(0L);
        Mockito.verify(recurringServiceRecordRepository, Mockito.times(wrappers.size())).add(Mockito.any(ServiceRecord.class));
    }

    private List<VaccineWrapper> getRandomVaccineWrappers(Long dbKey) {
        List<VaccineWrapper> details = new ArrayList<>();
        int x = 5;
        while (x > 0) {
            VaccineWrapper sw = new VaccineWrapper();
            if (dbKey != null)
                sw.setDbKey(dbKey);
            sw.setName("Test Vac");
            sw.setUpdatedVaccineDate(new DateTime(), true);
            details.add(sw);
            x--;
        }
        return details;
    }

    @Test
    public void testSaveVaccinesCreatesInRepository() {
        List<VaccineWrapper> wrappers = getRandomVaccineWrappers(null);

        VisitUtils.saveVaccines(wrappers, "12345");
        Mockito.verify(vaccineRepository, Mockito.times(wrappers.size())).add(Mockito.any(Vaccine.class));
    }
}
