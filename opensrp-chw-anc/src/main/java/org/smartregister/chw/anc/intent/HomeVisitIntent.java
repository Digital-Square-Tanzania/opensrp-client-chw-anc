package org.smartregister.chw.anc.intent;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONObject;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;
import org.smartregister.chw.anc.util.Util;

import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class HomeVisitIntent extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private VisitRepository visitRepository;
    private VisitDetailsRepository visitDetailsRepository;

    public HomeVisitIntent(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            processVisits();
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        visitRepository = AncLibrary.getInstance().visitRepository();
        visitDetailsRepository = AncLibrary.getInstance().visitDetailsRepository();
        return super.onStartCommand(intent, flags, startId);
    }

    private void processVisits() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -12);

        List<Visit> visits = visitRepository.getAllUnSynced(calendar.getTime().getTime());
        for (Visit v : visits) {
            if (v.getProcessed()) {
                // add to events table
                Util.processEvent(v.getBaseEntityId(), new JSONObject(v.getJson()));

                visitRepository.completeProcessing(v.getVisitId());
                // process details
                processVisitDetails(v.getVisitId());
            }
        }
    }

    private void processVisitDetails(String visitID) {
        List<VisitDetail> visitDetailList = visitDetailsRepository.getVisits(visitID);
        for (VisitDetail visitDetail : visitDetailList) {
            if (!visitDetail.getProcessed()) {
                // create vaccines

                // create services

                visitDetailsRepository.completeProcessing(visitDetail.getVisitId());
            }
        }
    }

}
