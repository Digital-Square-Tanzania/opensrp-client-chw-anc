package org.smartregister.chw.anc.interactor;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.contract.BaseAncUpcomingServicesContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.model.BaseUpcomingService;
import org.smartregister.chw.anc.util.AppExecutors;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseAncUpcomingServicesInteractor implements BaseAncUpcomingServicesContract.Interactor {

    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseAncUpcomingServicesInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAncUpcomingServicesInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void getUpComingServices(final MemberObject memberObject, final Context context, final BaseAncUpcomingServicesContract.InteractorCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // save it
                final List<BaseUpcomingService> services = new ArrayList<>();
                try {
                    services.addAll(getMemberServices(context, memberObject));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDataFetched(services);
                    }
                });
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    /**
     * Compute services
     *
     * @param context
     * @param memberObject
     * @return
     */
    protected List<BaseUpcomingService> getMemberServices(Context context, MemberObject memberObject) {
        List<BaseUpcomingService> services = new ArrayList<>();
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        BaseUpcomingService s = new BaseUpcomingService();
        s.setServiceName(context.getString(R.string.anc_home_visit));
        s.setServiceDate(new Date(System.currentTimeMillis() - (7 * DAY_IN_MS)));
        services.add(s);
        services.add(s);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return services;
    }
}
