package org.smartregister.chw.pnc.interactor;

import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.smartregister.Context;
import org.smartregister.chw.anc.contract.BaseAncMedicalHistoryContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.BaseAncMemberProfileInteractor;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.pnc.PncLibrary;
import org.smartregister.chw.pnc.R;
import org.smartregister.chw.pnc.contract.BasePncMemberProfileContract;
import org.smartregister.chw.pnc.util.Constants;
import org.smartregister.chw.pnc.util.PncUtil;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static org.smartregister.util.Utils.getName;

public class BasePncMemberProfileInteractor extends BaseAncMemberProfileInteractor implements BasePncMemberProfileContract.Interactor {

    protected BaseAncMedicalHistoryContract.InteractorCallBack interactorCallBack;



    @Override
    public String getPncDay(String motherBaseID) {
        String dayPnc = PncLibrary.getInstance().profileRepository().getDeliveryDate(motherBaseID);

        if (dayPnc != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
            dayPnc = String.valueOf(Days.daysBetween(new DateTime(formatter.parseDateTime(dayPnc)), new DateTime()).getDays());
        }
        return dayPnc;
    }

    public String getLastVisitDate(String motherBaseID) {

        Long pncLastVisitdate = PncLibrary.getInstance().profileRepository().getLastVisit(motherBaseID);
        if (pncLastVisitdate != null) {
            Date pncDate = new Date(pncLastVisitdate);
            SimpleDateFormat format = new SimpleDateFormat("dd MMM", Locale.getDefault());
            return format.format(pncDate);
        }

        return null;
    }

    @Override
    public String getPncMotherNameDetails(MemberObject memberObject, TextView textView, CircleImageView imageView) {
        List<CommonPersonObjectClient> children = pncChildrenUnder29Days(memberObject.getBaseEntityId());
        String nameDetails = memberObject.getMemberName();
        textView.setText(nameDetails);
        textView.setSingleLine(false);
        imageView.setImageResource(org.smartregister.chw.opensrp_chw_anc.R.mipmap.ic_member);
        for (CommonPersonObjectClient childObject : children) {
            try {
                char gender = childObject.getColumnmaps().get(Constants.KEY.GENDER).charAt(0);
                textView.append(" +\n" + childNameDetails(childObject.getColumnmaps().get(DBConstants.KEY.FIRST_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.MIDDLE_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.LAST_NAME),
                        String.valueOf(PncUtil.getDaysDifference(childObject.getColumnmaps().get(DBConstants.KEY.DOB))),
                        gender));
                imageView.setBorderWidth(12);
                imageView.setImageResource(R.drawable.pnc_less_twenty_nine_days);
                if (gender == 'M'){
                    imageView.setBorderColor(PncLibrary.getInstance().context().getColorResource(R.color.light_blue));
                }
                else{
                    imageView.setBorderColor(PncLibrary.getInstance().context().getColorResource(R.color.light_pink));
                }

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
