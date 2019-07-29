package org.smartregister.chw.pnc.interactor;

import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.interactor.BaseAncMemberProfileInteractor;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.pnc.PncLibrary;
import org.smartregister.chw.pnc.R;
import org.smartregister.chw.pnc.contract.BasePncMemberProfileContract;
import org.smartregister.chw.pnc.util.Constants;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.smartregister.chw.pnc.PncLibrary.getInstance;
import static org.smartregister.util.Utils.getName;

public class BasePncMemberProfileInteractor extends BaseAncMemberProfileInteractor implements BasePncMemberProfileContract.Interactor {

    @Override
    public String getPncMotherNameDetails(MemberObject memberObject, TextView textView, CircleImageView imageView) {

        List<CommonPersonObjectClient> children = getInstance().profileRepository().getChildrenLessThan29DaysOld(memberObject.getBaseEntityId());
        String nameDetails = memberObject.getMemberName();
        textView.setText(nameDetails);
        if (children.size() > 0) {
            textView.setSingleLine(false);
            char gender;
            CommonPersonObjectClient childObject;
            for (int i = 0; i < children.size(); i++) {
                childObject = children.get(i);
                gender = childObject.getColumnmaps().get(Constants.KEY.GENDER).charAt(0);
                textView.append(" +\n" + childNameDetails(childObject.getColumnmaps().get(DBConstants.KEY.FIRST_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.MIDDLE_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.LAST_NAME),
                        childObject.getColumnmaps().get(DBConstants.KEY.DOB), gender));
                if (gender == 'M')
                    imageView.setBorderColor(PncLibrary.getInstance().context().getColorResource(R.color.light_blue));
                else
                    imageView.setBorderColor(PncLibrary.getInstance().context().getColorResource(R.color.light_pink));
            }
        }

        return nameDetails;
    }

    private String childNameDetails(String firstName, String middleName, String surName, String age, char gender) {
        String dayCountString = PncLibrary.getInstance().context().getStringResource(R.string.pnc_day_count);
        String spacer = ", ";
        String name = getName(firstName, middleName);
        name = getName(name, surName);

        if (StringUtils.isNotBlank(firstName)) {
            return name + spacer + age + dayCountString + spacer + gender;
        }
        return null;
    }
}
