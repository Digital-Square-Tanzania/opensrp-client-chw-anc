package org.smartregister.chw.anc.fragment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.opensrp_chw_anc.R;

import static org.junit.Assert.assertNotNull;

public class BaseHomeVisitImmunizationFragmentTest extends BaseUnitTest {

    private BaseHomeVisitImmunizationFragment fragment;

    private Context context = RuntimeEnvironment.application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fragment = new BaseHomeVisitImmunizationFragment();
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        activity.setContentView(R.layout.activity_base_anc_homevisit);
        fragment.show(activity.getFragmentManager(), "IMMUNIZATION");
    }


    @Test
    public void testOnCreateViewLoadsViews() {
        fragment.onCreateView(LayoutInflater.from(context), null, null);
        assertNotNull(Whitebox.getInternalState(fragment, "multipleVaccineDatePickerView"));
        assertNotNull(Whitebox.getInternalState(fragment, "singleVaccineAddView"));
        assertNotNull(Whitebox.getInternalState(fragment, "vaccinationNameLayout"));
        assertNotNull(Whitebox.getInternalState(fragment, "textViewAddDate"));
        assertNotNull(Whitebox.getInternalState(fragment, "singleDatePicker"));
    }
}
