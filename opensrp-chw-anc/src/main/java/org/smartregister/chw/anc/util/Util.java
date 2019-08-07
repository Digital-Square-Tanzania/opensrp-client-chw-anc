package org.smartregister.chw.anc.util;

import android.content.Context;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import net.sqlcipher.database.SQLiteDatabase;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.chw.opensrp_chw_anc.R;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.domain.db.EventClient;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.chw.anc.AncLibrary.getInstance;
import static org.smartregister.chw.anc.util.JsonFormUtils.cleanString;
import static org.smartregister.util.Utils.getAllSharedPreferences;

public class Util {

    private static String[] default_obs = {"start", "end", "deviceid", "subscriberid", "simserial", "phonenumber"};

    private static SimpleDateFormat getSourceDateFormat() {
        return new SimpleDateFormat(getInstance().getSourceDateFormat(), Locale.getDefault());
    }

    private static SimpleDateFormat getSaveDateFormat() {
        return new SimpleDateFormat(getInstance().getSaveDateFormat(), Locale.getDefault());
    }

    public static void addEvent(AllSharedPreferences allSharedPreferences, Event baseEvent) throws Exception {
        if (baseEvent != null) {
            JsonFormUtils.tagEvent(allSharedPreferences, baseEvent);
            JSONObject eventJson = new JSONObject(JsonFormUtils.gson.toJson(baseEvent));
            getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson);
        }
    }

    public static void processEvent(String baseEntityID, JSONObject eventJson) throws Exception {
        if (eventJson != null) {
            getSyncHelper().addEvent(baseEntityID, eventJson);

            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unprocessed));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        }
    }

    public static void startClientProcessing() throws Exception {
        long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
        Date lastSyncDate = new Date(lastSyncTimeStamp);
        getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unprocessed));
        getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
    }

    public static ECSyncHelper getSyncHelper() {
        return getInstance().getEcSyncHelper();
    }

    public static ClientProcessorForJava getClientProcessorForJava() {
        return getInstance().getClientProcessorForJava();
    }

    public static Visit eventToVisit(Event event, String visitID) throws JSONException {
        List<String> exceptions = Arrays.asList(default_obs);

        Visit visit = new Visit();
        visit.setVisitId(visitID);
        visit.setBaseEntityId(event.getBaseEntityId());
        visit.setDate(event.getEventDate());
        visit.setVisitType(event.getEventType());
        visit.setEventId(event.getEventId());
        visit.setFormSubmissionId(event.getFormSubmissionId());
        visit.setJson(new JSONObject(JsonFormUtils.gson.toJson(event)).toString());
        visit.setProcessed(false);
        visit.setCreatedAt(new Date());
        visit.setUpdatedAt(new Date());

        Map<String, List<VisitDetail>> details = new HashMap<>();
        if (event.getObs() != null) {
            for (Obs obs : event.getObs()) {
                if (!exceptions.contains(obs.getFormSubmissionField())) {
                    VisitDetail detail = new VisitDetail();
                    detail.setVisitDetailsId(JsonFormUtils.generateRandomUUIDString());
                    detail.setVisitId(visit.getVisitId());
                    detail.setVisitKey(obs.getFormSubmissionField());

                    if (detail.getVisitKey().contains("date")) {
                        // parse the
                        detail.setDetails(getFormattedDate(getSourceDateFormat(), getSaveDateFormat(), cleanString(obs.getValues().toString())));
                        detail.setHumanReadable(getFormattedDate(getSourceDateFormat(), getSaveDateFormat(), cleanString(obs.getHumanReadableValues().toString())));
                    } else {
                        detail.setDetails(cleanString(obs.getValues().toString()));
                        detail.setHumanReadable(cleanString(obs.getHumanReadableValues().toString()));
                    }

                    detail.setJsonDetails(new JSONObject(JsonFormUtils.gson.toJson(obs)).toString());
                    detail.setProcessed(false);
                    detail.setCreatedAt(new Date());
                    detail.setUpdatedAt(new Date());

                    List<VisitDetail> currentList = details.get(detail.getVisitKey());
                    if (currentList == null)
                        currentList = new ArrayList<>();

                    currentList.add(detail);
                    details.put(detail.getVisitKey(), currentList);
                }
            }
        }

        visit.setVisitDetails(details);
        return visit;
    }

    public static String getFormattedDate(SimpleDateFormat source_sdf, SimpleDateFormat dest_sdf, String value) {
        try {
            Date date = source_sdf.parse(value);
            return dest_sdf.format(date);
        } catch (Exception e) {
            Timber.e(e);
        }
        return value;
    }

    // executed before processing
    public static Visit eventToVisit(Event event) throws JSONException {
        return eventToVisit(event, JsonFormUtils.generateRandomUUIDString());
    }

    public static void processAncHomeVisit(EventClient baseEvent) {
        processAncHomeVisit(baseEvent, null);
    }

    public static void processAncHomeVisit(EventClient baseEvent, SQLiteDatabase database) {
        try {
            Visit visit = getInstance().visitRepository().getVisitByFormSubmissionID(baseEvent.getEvent().getFormSubmissionId());
            if (visit == null) {
                visit = eventToVisit(baseEvent.getEvent());
                if (database != null) {
                    getInstance().visitRepository().addVisit(visit, database);
                } else {
                    getInstance().visitRepository().addVisit(visit);
                }
                if (visit.getVisitDetails() != null) {
                    for (Map.Entry<String, List<VisitDetail>> entry : visit.getVisitDetails().entrySet()) {
                        if (entry.getValue() != null) {
                            for (VisitDetail detail : entry.getValue()) {
                                if (database != null) {
                                    getInstance().visitDetailsRepository().addVisitDetails(detail, database);
                                } else {
                                    getInstance().visitDetailsRepository().addVisitDetails(detail);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    // executed by event client processor
    public static Visit eventToVisit(org.smartregister.domain.db.Event event) throws JSONException {
        List<String> exceptions = Arrays.asList(default_obs);

        Visit visit = new Visit();
        visit.setVisitId(org.smartregister.chw.anc.util.JsonFormUtils.generateRandomUUIDString());
        visit.setBaseEntityId(event.getBaseEntityId());
        visit.setDate(event.getEventDate().toDate());
        visit.setVisitType(event.getEventType());
        visit.setEventId(event.getEventId());
        visit.setFormSubmissionId(event.getFormSubmissionId());
        visit.setJson(new JSONObject(org.smartregister.chw.anc.util.JsonFormUtils.gson.toJson(event)).toString());
        visit.setProcessed(true);
        visit.setCreatedAt(new Date());
        visit.setUpdatedAt(new Date());

        Map<String, List<VisitDetail>> details = new HashMap<>();
        if (event.getObs() != null) {
            for (org.smartregister.domain.db.Obs obs : event.getObs()) {
                if (!exceptions.contains(obs.getFormSubmissionField())) {
                    VisitDetail detail = new VisitDetail();
                    detail.setVisitDetailsId(org.smartregister.chw.anc.util.JsonFormUtils.generateRandomUUIDString());
                    detail.setVisitId(visit.getVisitId());
                    detail.setVisitKey(obs.getFormSubmissionField());

                    if (detail.getVisitKey().contains("date")) {
                        // parse the
                        detail.setDetails(getFormattedDate(getSourceDateFormat(), getSaveDateFormat(), cleanString(obs.getValues().toString())));
                        detail.setHumanReadable(getFormattedDate(getSourceDateFormat(), getSaveDateFormat(), cleanString(obs.getHumanReadableValues().toString())));
                    } else {
                        detail.setDetails(cleanString(obs.getValues().toString()));
                        detail.setHumanReadable(cleanString(obs.getHumanReadableValues().toString()));
                    }

                    detail.setProcessed(true);
                    detail.setCreatedAt(new Date());
                    detail.setUpdatedAt(new Date());

                    List<VisitDetail> currentList = details.get(detail.getVisitKey());
                    if (currentList == null)
                        currentList = new ArrayList<>();

                    currentList.add(detail);
                    details.put(detail.getVisitKey(), currentList);
                }
            }
        }

        visit.setVisitDetails(details);
        return visit;
    }

    public static int getMemberProfileImageResourceIDentifier(String entityType) {
        return R.mipmap.ic_member;
    }

    public static String gestationAgeString(String lmp, Context context, boolean full) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        int ga = Days.daysBetween(formatter.parseDateTime(lmp), new DateTime()).getDays() / 7;
        if (full)
            return String.format(context.getString(R.string.gest_age), String.valueOf(ga)) + " " + context.getString(R.string.gest_age_weeks);
        return String.valueOf(ga);
    }

    @Nullable
    public static JSONObject getVisitJSONFromWrapper(String entityID, Map<VaccineWrapper, String> vaccineWrapperDateMap) {

        try {
            JSONObject jsonObject = JsonFormUtils.getFormAsJson(Constants.FORMS.IMMUNIZATIOIN_VISIT);
            jsonObject.put("entity_id", entityID);
            JSONArray jsonArray = jsonObject.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS);


            for (Map.Entry<VaccineWrapper, String> entry : vaccineWrapperDateMap.entrySet()) {
                JSONObject field = new JSONObject();
                field.put(JsonFormConstants.KEY, removeSpaces(entry.getKey().getName()));
                field.put(JsonFormConstants.OPENMRS_ENTITY_PARENT, "");
                field.put(JsonFormConstants.OPENMRS_ENTITY, "concept");
                field.put(JsonFormConstants.OPENMRS_ENTITY_ID, removeSpaces(entry.getKey().getName()));
                field.put(JsonFormConstants.VALUE, entry.getValue());

                jsonArray.put(field);
            }

            return jsonObject;
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    public static String removeSpaces(String s) {
        return s.replace(" ", "_").toLowerCase();
    }
}
