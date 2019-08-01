package org.smartregister.chw.pnc.repository;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.pnc.util.Constants;
import org.smartregister.chw.pnc.util.PncUtil;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class ProfileRepository extends BaseRepository {


    private static final String MOHTER_ENTITY_ID = "mother_entity_id";
    private static final String DELIVERY_DATE = "delivery_date";

    public ProfileRepository(Repository repository) {
        super(repository);
    }

    private CommonPersonObjectClient getChildMember(Cursor cursor, int childAgeInDays) {
        String[] columnNames = cursor.getColumnNames();
        Map<String, String> details = new HashMap<>();

        for (String columnName : columnNames) {
            details.put(columnName, cursor.getString(cursor.getColumnIndex(columnName)));
        }

        details.put(DBConstants.KEY.DOB, String.valueOf(childAgeInDays));
        CommonPersonObjectClient commonPersonObject = new CommonPersonObjectClient("", details, "");
        commonPersonObject.setColumnmaps(details);
        commonPersonObject.setCaseId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY.BASE_ENTITY_ID)));

        return commonPersonObject;

    }

    public List<CommonPersonObjectClient> getChildrenLessThan29DaysOld(String motherBaseEntityID) {
        List<CommonPersonObjectClient> childMemberObjects = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        net.sqlcipher.Cursor cursor = null;
        try {
            if (database == null) {
                return null;
            }
            cursor = database.rawQuery("SELECT * fROM " + Constants.TABLES.EC_CHILD + " WHERE " + MOHTER_ENTITY_ID + "=?",
                    new String[]{motherBaseEntityID});
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String dob = cursor.getString(cursor.getColumnIndex(DBConstants.KEY.DOB));
                    int childAgeInDays = PncUtil.getDaysDifference(dob);
                    if (childAgeInDays < 29) {
                        childMemberObjects.add(getChildMember(cursor, childAgeInDays));
                    }
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return childMemberObjects;
    }

    public String getDeliveryDate(String motherBaseEntityID) {

        SQLiteDatabase database = getReadableDatabase();

        String delivery_date = null;

        net.sqlcipher.Cursor cursor = null;
        try {
            if (database == null) {
                return null;
            }
            cursor = database.query(Constants.TABLES.EC_PREGNANCY_OUTCOME, new String[]{DELIVERY_DATE}, DBConstants.KEY.BASE_ENTITY_ID + " = ? " + COLLATE_NOCASE, new String[]{motherBaseEntityID}, null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                delivery_date = cursor.getString(cursor.getColumnIndex(DELIVERY_DATE));
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return delivery_date;
    }

}
