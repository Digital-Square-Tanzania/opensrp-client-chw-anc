package org.smartregister.chw.anc.application;

import org.smartregister.chw.opensrp_chw_anc.R;

import timber.log.Timber;

public class TestApplication extends SampleDefaultApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.Theme_AppCompat); //or just R.style.Theme_AppCompat
    }

    @Override
    public void logoutCurrentUser() {
        Timber.v("logoutCurrentUser");
    }
}
