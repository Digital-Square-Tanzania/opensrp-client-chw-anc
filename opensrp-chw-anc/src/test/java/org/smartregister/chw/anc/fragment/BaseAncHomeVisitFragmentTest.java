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

public class BaseAncHomeVisitFragmentTest extends BaseUnitTest {

    private BaseAncHomeVisitFragment fragment;

    private Context context = RuntimeEnvironment.application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fragment = new BaseAncHomeVisitFragment();
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        activity.setContentView(R.layout.activity_base_anc_homevisit);
        fragment.show(activity.getFragmentManager(), "HOMEVISIT");
    }


    @Test
    public void testOnCreateView() {
        fragment.onCreateView(LayoutInflater.from(context), null, null);
        assertNotNull(Whitebox.getInternalState(fragment, "radioGroupChoices"));
        assertNotNull(Whitebox.getInternalState(fragment, "datePicker"));
        assertNotNull(Whitebox.getInternalState(fragment, "buttonSave"));
    }

}
