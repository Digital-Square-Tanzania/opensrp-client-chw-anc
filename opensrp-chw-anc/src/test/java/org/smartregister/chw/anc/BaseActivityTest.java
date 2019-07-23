package org.smartregister.chw.anc;

import android.app.Activity;
import android.content.Intent;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.chw.anc.application.TestApplication;
import org.smartregister.immunization.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, constants = BuildConfig.class, sdk = 22)
public abstract class BaseActivityTest<T extends Activity> {

    private T activity;
    private ActivityController<T> controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        if (getControllerIntent() == null) {
            controller = Robolectric.buildActivity(getActivityClass()).create().start();
        } else {
            controller = Robolectric.buildActivity(getActivityClass(), getControllerIntent()).create().start();
        }
        activity = controller.get();
    }

    @After
    public void tearDown() {
        try {
            if (getActivity() != null) {
                getActivity().finish();
                getActivityController().pause().stop().destroy(); //destroy controller if we can
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.gc();
    }

    @Test
    public void testActivityExists() {
        Assert.assertNotNull(getActivity());
    }

    protected abstract Class<T> getActivityClass();

    protected T getActivity() {
        return activity;
    }

    protected ActivityController getActivityController() {
        return controller;
    }

    protected Intent getControllerIntent() {
        return null;
    }
}
