package org.smartregister.chw.anc.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class VisitRepository extends BaseRepository {

    public static final String VISIT_TABLE = "visit_table";
    private static final String VISIT_ID = "visit_id";
    private static final String VISIT_TYPE = "visit_type";
    private static final String BASE_ENTITY_ID = "base_entity_id";
    private static final String VISIT_DATE = "visit_date";
    private static final String VISIT_JSON = "visit_json";
    private static final String FORM_SUBMISSION_ID = "form_submission_id";
    private static final String PROCESSED = "processed";
    private static final String UPDATED_AT = "updated_at";
    private static final String CREATED_AT = "created_at";

    private String[] VISIT_COLUMNS = {VISIT_ID, VISIT_TYPE, BASE_ENTITY_ID, VISIT_DATE, VISIT_JSON, FORM_SUBMISSION_ID, PROCESSED, UPDATED_AT, CREATED_AT};

    private static final String CREATE_VISIT_TABLE =
            "CREATE TABLE " + VISIT_TABLE + "("
                    + VISIT_ID + " VARCHAR NOT NULL, "
                    + VISIT_TYPE + " VARCHAR NOT NULL, "
                    + BASE_ENTITY_ID + " VARCHAR NOT NULL, "
                    + VISIT_DATE + " VARCHAR NOT NULL, "
                    + VISIT_JSON + " VARCHAR NOT NULL, "
                    + FORM_SUBMISSION_ID + " VARCHAR NOT NULL, "
                    + PROCESSED + " Integer, "
                    + UPDATED_AT + " DATETIME, "
                    + CREATED_AT + " DATETIME NOT NULL)";

    private static final String BASE_ENTITY_ID_INDEX = "CREATE INDEX " + VISIT_TABLE + "_" + BASE_ENTITY_ID + "_index ON " + VISIT_TABLE
            + "("
            + BASE_ENTITY_ID + " COLLATE NOCASE , "
            + VISIT_TYPE + " COLLATE NOCASE , "
            + VISIT_DATE + " COLLATE NOCASE"
            + ");";


    public VisitRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_VISIT_TABLE);
        database.execSQL(BASE_ENTITY_ID_INDEX);
    }

    private ContentValues createValues(Visit visit) {
        ContentValues values = new ContentValues();
        values.put(VISIT_ID, visit.getVisitId());
        values.put(VISIT_TYPE, visit.getVisitType());
        values.put(BASE_ENTITY_ID, visit.getBaseEntityId());
        values.put(VISIT_DATE, visit.getDate() != null ? visit.getDate().getTime() : null);
        values.put(VISIT_JSON, visit.getJson());
        values.put(FORM_SUBMISSION_ID, visit.getFormSubmissionId());
        values.put(PROCESSED, visit.getProcessed());
        values.put(UPDATED_AT, visit.getUpdatedAt().getTime());
        values.put(CREATED_AT, visit.getCreatedAt().getTime());
        return values;
    }

    public void addVisit(Visit visit) {
        if (visit == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        // Handle updated home visit details
        database.insert(VISIT_TABLE, null, createValues(visit));
    }

    public void deleteVisit(String visitID) {
        try {
            getWritableDatabase().delete(VISIT_TABLE, VISIT_ID + "= ?", new String[]{visitID});
            getWritableDatabase().delete(VisitDetailsRepository.VISIT_DETAILS_TABLE, VISIT_ID + "= ?", new String[]{visitID});
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }
    }

    public void completeProcessing(String visitID) {
        try {
            ContentValues values = new ContentValues();
            values.put(PROCESSED, 1);
            getWritableDatabase().update(VISIT_TABLE, values, VISIT_ID + " = ?", new String[]{visitID});
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }
    }

    private List<Visit> readVisits(Cursor cursor) {
        List<Visit> visits = new ArrayList<>();

        try {

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    Visit visit = new Visit();
                    visit.setVisitId(cursor.getString(cursor.getColumnIndex(VISIT_ID)));
                    visit.setVisitType(cursor.getString(cursor.getColumnIndex(VISIT_TYPE)));
                    visit.setBaseEntityId(cursor.getString(cursor.getColumnIndex(BASE_ENTITY_ID)));
                    visit.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(VISIT_DATE)))));
                    visit.setJson(cursor.getString(cursor.getColumnIndex(VISIT_JSON)));
                    visit.setFormSubmissionId(cursor.getString(cursor.getColumnIndex(FORM_SUBMISSION_ID)));
                    visit.setProcessed(cursor.getInt(cursor.getColumnIndex(PROCESSED)) == 1);
                    visit.setCreatedAt(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATED_AT)))));
                    visit.setUpdatedAt(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(UPDATED_AT)))));

                    visits.add(visit);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            cursor.close();
        }
        return visits;
    }

    public List<Visit> getAllUnSynced(Long last_edit_time) {
        List<Visit> visits = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(VISIT_TABLE, VISIT_COLUMNS, PROCESSED + " = ? AND UPDATED_AT <= ? ", new String[]{"0", last_edit_time.toString()}, null, null, VISIT_DATE + " DESC ", null);
            visits = readVisits(cursor);
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return visits;
    }

    public List<Visit> getVisits(String baseEntityID, String visitType) {
        List<Visit> visits = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(VISIT_TABLE, VISIT_COLUMNS, BASE_ENTITY_ID + " = ? " + VISIT_TYPE + " = ? ", new String[]{baseEntityID, visitType}, null, null, VISIT_DATE + " DESC ", null);
            visits = readVisits(cursor);
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return visits;
    }

    public Visit getLatestVisit(String baseEntityID, String visitType) {
        List<Visit> visits = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(VISIT_TABLE, VISIT_COLUMNS, BASE_ENTITY_ID + " = ? " + VISIT_TYPE + " = ? ", new String[]{baseEntityID, visitType}, null, null, VISIT_DATE + " DESC ", "1");
            visits = readVisits(cursor);
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return (visits.size() > 0) ? visits.get(0) : null;
    }
}
