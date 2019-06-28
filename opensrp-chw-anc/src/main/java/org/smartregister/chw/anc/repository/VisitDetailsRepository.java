package org.smartregister.chw.anc.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class VisitDetailsRepository extends BaseRepository {

    public static final String VISIT_DETAILS_TABLE = "visit_details";
    private static final String VISIT_DETAILS_ID = "visit_details_id";
    private static final String VISIT_ID = "visit_id";
    private static final String VISIT_KEY = "visit_key";
    private static final String DETAILS = "details";
    private static final String HUMAN_READABLE = "human_readable_details";
    private static final String JSON_DETAILS = "json_details";
    private static final String PROCESSED = "processed";
    private static final String UPDATED_AT = "updated_at";
    private static final String CREATED_AT = "created_at";

    private static final String CREATE_VISIT_TABLE =
            "CREATE TABLE " + VISIT_DETAILS_TABLE + "("
                    + VISIT_DETAILS_ID + " VARCHAR NULL, "
                    + VISIT_ID + " VARCHAR NULL, "
                    + VISIT_KEY + " VARCHAR NULL, "
                    + JSON_DETAILS + " VARCHAR NULL, "
                    + DETAILS + " VARCHAR NULL, "
                    + HUMAN_READABLE + " VARCHAR NULL, "
                    + PROCESSED + " Integer NULL, "
                    + UPDATED_AT + " DATETIME NULL, "
                    + CREATED_AT + " DATETIME NULL)";

    private static final String VISIT_ID_INDEX = "CREATE INDEX " + VISIT_DETAILS_TABLE + "_" + VISIT_ID + "_index ON " + VISIT_DETAILS_TABLE
            + "("
            + VISIT_ID + " COLLATE NOCASE "
            + ");";


    private String[] VISIT_DETAILS_COLUMNS = {VISIT_ID, VISIT_KEY, VISIT_DETAILS_ID, JSON_DETAILS, DETAILS, PROCESSED, UPDATED_AT, CREATED_AT};

    public VisitDetailsRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_VISIT_TABLE);
        database.execSQL(VISIT_ID_INDEX);
    }

    private ContentValues createValues(VisitDetail visitDetail) {
        ContentValues values = new ContentValues();
        values.put(VISIT_DETAILS_ID, visitDetail.getVisitDetailsId());
        values.put(VISIT_ID, visitDetail.getVisitId());
        values.put(VISIT_KEY, visitDetail.getVisitKey());
        values.put(JSON_DETAILS, visitDetail.getJsonDetails());
        values.put(DETAILS, visitDetail.getDetails());
        values.put(HUMAN_READABLE, visitDetail.getHumanReadable());
        values.put(PROCESSED, visitDetail.getProcessed() ? 1 : 0);
        values.put(UPDATED_AT, visitDetail.getUpdatedAt().getTime());
        values.put(CREATED_AT, visitDetail.getCreatedAt().getTime());
        return values;
    }

    public void addVisitDetails(VisitDetail visitDetail) {
        if (visitDetail == null) {
            return;
        }
        SQLiteDatabase database = getWritableDatabase();
        // Handle updated home visit details
        database.insert(VISIT_DETAILS_TABLE, null, createValues(visitDetail));
    }

    public void completeProcessing(String visitDetailsID) {
        try {
            ContentValues values = new ContentValues();
            values.put(PROCESSED, 1);
            getWritableDatabase().update(VISIT_DETAILS_TABLE, values, VISIT_DETAILS_ID + " = ?", new String[]{visitDetailsID});
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }
    }

    public List<VisitDetail> getVisits(String visitID) {
        List<VisitDetail> visitDetails = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(VISIT_DETAILS_TABLE, VISIT_DETAILS_COLUMNS, VISIT_ID + " = ? ", new String[]{visitID}, null, null, null, null);
            visitDetails = readVisitDetails(cursor);
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return visitDetails;
    }

    private List<VisitDetail> readVisitDetails(Cursor cursor) {
        List<VisitDetail> visitDetailList = new ArrayList<>();

        try {

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    VisitDetail visitDetail = new VisitDetail();
                    visitDetail.setVisitId(cursor.getString(cursor.getColumnIndex(VISIT_ID)));
                    visitDetail.setVisitDetailsId(cursor.getString(cursor.getColumnIndex(VISIT_DETAILS_ID)));
                    visitDetail.setVisitKey(cursor.getString(cursor.getColumnIndex(VISIT_KEY)));
                    visitDetail.setJsonDetails(cursor.getString(cursor.getColumnIndex(JSON_DETAILS)));
                    visitDetail.setDetails(cursor.getString(cursor.getColumnIndex(DETAILS)));
                    visitDetail.setProcessed(cursor.getInt(cursor.getColumnIndex(PROCESSED)) == 1);
                    visitDetail.setCreatedAt(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATED_AT)))));
                    visitDetail.setUpdatedAt(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(UPDATED_AT)))));

                    visitDetailList.add(visitDetail);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            cursor.close();
        }
        return visitDetailList;
    }

}
