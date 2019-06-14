package org.smartregister.chw.anc.domain;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.util.Utils;

import java.io.Serializable;

import static org.smartregister.util.Utils.getName;

@SuppressWarnings("serial")
public class MemberObject implements Serializable {
    protected String memberName;
    protected String lastMenstrualPeriod;
    protected String address;
    protected String chwMemberId;
    protected String baseEntityId;
    protected String familyBaseEntityId;
    protected String familyHead;
    protected String primaryCareGiver;
    protected String familyName;
    protected String lastContactVisit;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected String dob;

    public MemberObject() {
    }

    public MemberObject(CommonPersonObjectClient pc) {
        lastMenstrualPeriod = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_MENSTRUAL_PERIOD, false);
        baseEntityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.BASE_ENTITY_ID, false);
        lastContactVisit = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_CONTACT_VISIT, false);
        firstName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.FIRST_NAME, true);
        middleName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.MIDDLE_NAME, true);
        lastName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_NAME, true);
        dob = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.DOB, false);
    }

    public MemberObject(String memberName, String lastMenstrualPeriod, String address, String chwMemberId, String baseEntityId, String familyBaseEntityId, String familyHead, String primaryCareGiver, String familyName) {
        this.memberName = memberName;
        this.lastMenstrualPeriod = lastMenstrualPeriod;
        this.address = address;
        this.chwMemberId = chwMemberId;
        this.baseEntityId = baseEntityId;
        this.familyBaseEntityId = familyBaseEntityId;
        this.familyHead = familyHead;
        this.primaryCareGiver = primaryCareGiver;
        this.familyName = familyName;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getLastMenstrualPeriod() {
        return lastMenstrualPeriod;
    }

    public String getAddress() {
        return address;
    }

    public String getChwMemberId() {
        return chwMemberId;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public String getFamilyBaseEntityId() {
        return familyBaseEntityId;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public String getPrimaryCareGiver() {
        return primaryCareGiver;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getLastContactVisit() {
        return lastContactVisit;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public int getAge() {
        return new Period(new DateTime(getDob()), new DateTime()).getYears();
    }

    public String getFullName() {
        return getName(getName(getFirstName(), getMiddleName()), getLastName());
    }
}
