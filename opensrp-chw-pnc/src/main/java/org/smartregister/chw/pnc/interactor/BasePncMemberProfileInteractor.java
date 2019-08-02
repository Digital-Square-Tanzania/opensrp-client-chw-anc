package org.smartregister.chw.pnc.interactor;

import android.util.Pair;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.BaseAncMemberProfileInteractor;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.pnc.PncLibrary;
import org.smartregister.chw.pnc.R;
import org.smartregister.chw.pnc.contract.BasePncMemberProfileContract;
import org.smartregister.chw.pnc.util.Constants;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static org.smartregister.util.Utils.getName;

public class BasePncMemberProfileInteractor extends BaseAncMemberProfileInteractor implements BasePncMemberProfileContract.Interactor {

    @Override
    public String getPncDay(String motherBaseID) {
        String dayPnc = PncLibrary.getInstance().profileRepository().getDeliveryDate(motherBaseID);

        if (dayPnc != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
            dayPnc = String.valueOf(Days.daysBetween(new DateTime(formatter.parseDateTime(dayPnc)), new DateTime()).getDays());
        }
        return dayPnc;
    }

    @Override
    public String getPncMotherNameDetails(MemberObject memberObject, TextView textView, CircleImageView imageView) {

        List<CommonPersonObjectClient> children = pncChildrenUnder29Days(memberObject.getBaseEntityId());
        String nameDetails = memberObject.getMemberName();
        textView.setText(nameDetails);
        textView.setSingleLine(false);
        for (CommonPersonObjectClient childObject : children) {
            try {
                char gender = childObject.getColumnmaps().get(Constants.KEY.GENDER).charAt(0);
                textView.append(" +\n" + childNameDetails(childObject.getColumnmaps().get(DBConstants.KEY.FIRST_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.MIDDLE_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.LAST_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.DOB), gender));
                if (gender == 'M')
                    imageView.setBorderColor(PncLibrary.getInstance().context().getColorResource(R.color.light_blue));
                else
                    imageView.setBorderColor(PncLibrary.getInstance().context().getColorResource(R.color.light_pink));
            } catch (NullPointerException npe) {
                Timber.e(npe);
            }
        }

        return nameDetails;
    }

    @Override
    public List<CommonPersonObjectClient> pncChildrenUnder29Days(String motherBaseID) {
        return PncLibrary.getInstance().profileRepository().getChildrenLessThan29DaysOld(motherBaseID);
    }

    private String childNameDetails(String firstName, String middleName, String surName, String age, char gender) {
        String dayCountString = PncLibrary.getInstance().context().getStringResource(R.string.pnc_day_count);
        String spacer = ", ";
        middleName = middleName != null ? middleName : "";
        String name = getName(firstName, middleName);
        name = getName(name, surName);

        if (StringUtils.isNotBlank(firstName)) {
            return name + spacer + age + dayCountString + spacer + gender;
        }
        return null;
    }

}
