package org.smartregister.chw.pnc.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.smartregister.chw.pnc.repository.PncCloseDateRepository;
import org.smartregister.clientandeventmodel.DateUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class PncUtil {

    public static int getDaysDifference(String date) {

        return Days.daysBetween(new DateTime(getDate(date)), new DateTime()).getDays();
    }

    private static Date getDate(String eventDateStr) {
        Date date = null;
        if (StringUtils.isNotBlank(eventDateStr)) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
                date = dateFormat.parse(eventDateStr);
            } catch (ParseException e) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    date = dateFormat.parse(eventDateStr);
                } catch (ParseException pe) {
                    try {
                        date = DateUtil.parseDate(eventDateStr);
                    } catch (ParseException pee) {
                        Timber.e(pee, pee.toString());
                    }
                }
            }
        }
        return date;
    }

    public static void updatePregancyOutcome(Integer numberOfDays, PncCloseDateRepository pncCloseDateRepository) {
        pncCloseDateRepository.closeOldPNCRecords(numberOfDays);
    }
}
