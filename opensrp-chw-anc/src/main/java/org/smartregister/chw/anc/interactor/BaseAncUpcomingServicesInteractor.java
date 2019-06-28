package org.smartregister.chw.anc.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.anc.contract.BaseAncUpcomingServicesContract;
import org.smartregister.chw.anc.model.BaseUpcomingService;
import org.smartregister.chw.anc.util.AppExecutors;

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
    public void getUpComingServices(final String memberID, final BaseAncUpcomingServicesContract.InteractorCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // save it
                final List<BaseUpcomingService> services = new ArrayList<>();
                try {
                    services.addAll(getMemberServices(memberID));
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
     * Override and load all services
     *
     * @param memberID
     * @return
     */
    protected List<BaseUpcomingService> getMemberServices(String memberID) {
        List<BaseUpcomingService> services = new ArrayList<>();
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        BaseUpcomingService s = new BaseUpcomingService();
        s.setServiceName("Anc Service");
        s.setServiceDate(new Date(System.currentTimeMillis() - (7 * DAY_IN_MS)));
        services.add(s);
        return services;
    }
}
