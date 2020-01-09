package org.smartregister.chw.pnc.repository;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.chw.pnc.util.Constants;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.Date;

public class PncCloseDateRepository extends BaseRepository {

    public void closeOldPNCRecords(int duration) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("is_closed", 1);
        contentValues.put("last_interacted_with", new Date().getTime());

        String where = " cast(julianday(datetime('now')) - julianday(datetime(substr(delivery_date, 7,4) " +
                " || '-' || substr(delivery_date, 4,2) || '-' || substr(delivery_date, 1,2))) as integer) >= ? ";

        String[] whereArgs = new String[]{String.valueOf(duration)};
        database.update(Constants.TABLES.EC_PREGNANCY_OUTCOME, contentValues, where, whereArgs);
    }

}

