package org.smartregister.chw.anc.util;

import org.json.JSONException;
import org.json.JSONObject;
<<<<<<< HEAD
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
=======
import org.smartregister.chw.opensrp_chw_anc.R;
>>>>>>> 108cf8d3de7238405061fea9ef0462bc94a1cc7e
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.smartregister.chw.anc.AncLibrary.getInstance;
import static org.smartregister.util.Utils.getAllSharedPreferences;

public class Util {

    public static void processEvent(AllSharedPreferences allSharedPreferences, Event baseEvent) throws Exception {
        if (baseEvent != null) {
            JsonFormUtils.tagEvent(allSharedPreferences, baseEvent);
            JSONObject eventJson = new JSONObject(JsonFormUtils.gson.toJson(baseEvent));
            processEvent(baseEvent.getBaseEntityId(), eventJson);
        }
    }

    public static void processEvent(String baseEntityID, JSONObject eventJson) throws Exception {
        if (eventJson != null) {
            getSyncHelper().addEvent(baseEntityID, eventJson);

            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unsynced));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        }
    }

    public static ECSyncHelper getSyncHelper() {
        return getInstance().getEcSyncHelper();
    }

    public static ClientProcessorForJava getClientProcessorForJava() {
        return getInstance().getClientProcessorForJava();
    }

<<<<<<< HEAD
    public static Visit eventToVisit(Event event) throws JSONException {
        Visit visit = new Visit();
        visit.setVisitId(JsonFormUtils.generateRandomUUIDString());
        visit.setBaseEntityId(event.getBaseEntityId());
        visit.setDate(event.getEventDate());
        visit.setEventId(event.getEventId());
        visit.setFormSubmissionId(event.getFormSubmissionId());
        visit.setJson(new JSONObject(JsonFormUtils.gson.toJson(event)).toString());
        visit.setProcessed(false);
        visit.setCreatedAt(new Date());
        visit.setUpdatedAt(new Date());

        Map<String, VisitDetail> details = new HashMap<>();
        if (event.getObs() != null) {
            for (Obs obs : event.getObs()) {
                VisitDetail detail = new VisitDetail();
                detail.setVisitDetailsId(JsonFormUtils.generateRandomUUIDString());
                detail.setVisitId(visit.getVisitId());
                detail.setVisitKey(obs.getFormSubmissionField());
                detail.setJsonDetails(new JSONObject(JsonFormUtils.gson.toJson(obs)).toString());
                detail.setProcessed(false);
                detail.setCreatedAt(new Date());
                detail.setUpdatedAt(new Date());
                details.put(detail.getVisitKey(), detail);
            }
        }

        visit.setVisitDetails(details);
        return visit;
    }
=======
    public static int getMemberProfileImageResourceIDentifier(String entityType) {
        return R.mipmap.ic_member;
    }

>>>>>>> 108cf8d3de7238405061fea9ef0462bc94a1cc7e
}
