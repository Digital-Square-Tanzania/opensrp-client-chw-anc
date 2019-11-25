package org.smartregister.chw.anc.fragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.VaccineDisplay;
import org.smartregister.chw.anc.domain.VisitDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseHomeVisitImmunisationFragmentTest extends BaseUnitTest {


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFragmentInstanceInitialisesCorrectCheckboxSelectionState() {
        BaseAncHomeVisitContract.VisitView visitView = null;
        String baseEntityId = "random_base_entity_id";
        Map<String, List<VisitDetail>> details = new HashMap<>();
        List<VaccineDisplay> vaccineDisplays = new ArrayList<>();

        BaseHomeVisitImmunizationFragment fragment = BaseHomeVisitImmunizationFragment.getInstance(visitView, baseEntityId, details, vaccineDisplays);
        Assert.assertTrue(Whitebox.getInternalState(fragment, "vaccinesDefaultChecked"));
    }
}
