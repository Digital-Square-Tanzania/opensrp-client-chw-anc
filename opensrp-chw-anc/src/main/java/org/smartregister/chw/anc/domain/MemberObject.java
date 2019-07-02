package org.smartregister.chw.anc.domain;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
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
    protected String lastInteractedWith;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected String dob;
    protected String phoneNumber;
    protected int confirmedContacts = 0;
    protected String dateCreated;

    public MemberObject() {
    }

    public MemberObject(CommonPersonObjectClient pc) {
        memberName = getAncMemberNameAndAge(
                pc.getColumnmaps().get(DBConstants.KEY.FIRST_NAME),
                pc.getColumnmaps().get(DBConstants.KEY.MIDDLE_NAME),
                pc.getColumnmaps().get(DBConstants.KEY.LAST_NAME),
                pc.getColumnmaps().get(DBConstants.KEY.DOB));

        lastMenstrualPeriod = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_MENSTRUAL_PERIOD, false);
        baseEntityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.BASE_ENTITY_ID, false);
        lastContactVisit = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_CONTACT_VISIT, false);
        firstName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.FIRST_NAME, true);
        middleName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.MIDDLE_NAME, true);
        lastName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.LAST_NAME, true);
        dob = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.DOB, false);
        dateCreated = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.DATE_CREATED, false);
        address = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.VILLAGE_TOWN, false);
        chwMemberId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.UNIQUE_ID, false);

        familyBaseEntityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.RELATIONAL_ID, false);
        familyHead = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.FAMILY_HEAD, false);
        primaryCareGiver = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.PRIMARY_CAREGIVER, false);
        familyName = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.FAMILY_NAME, false);
        phoneNumber = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.PHONE_NUMBER, false);

        String visits = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.CONFIRMED_VISITS, false);
        if (StringUtils.isNotBlank(visits)) {
            confirmedContacts = Integer.parseInt(visits);
        }
    }

    private String getAncMemberNameAndAge(String firstName, String middleName, String surName, String age) {
        int integerAge = new Period(new DateTime(age), new DateTime()).getYears();

        String name = Utils.getName(firstName, middleName);
        name = Utils.getName(name, surName);

        if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(middleName) && StringUtils.isNotBlank(age)) {
            return name + ", " + integerAge;
        }
        return "";
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    public int getConfirmedContacts() {
        return confirmedContacts;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getAge() {
        return new Period(new DateTime(getDob()), new DateTime()).getYears();
    }

    public int getGestationAge() {
        return Days.daysBetween(DateTimeFormat.forPattern("dd-MM-yyyy").parseDateTime(lastMenstrualPeriod), new DateTime()).getDays() / 7;
    }

    public String getFullName() {
        return getName(getName(getFirstName(), getMiddleName()), getLastName());
    }
    public String getLastInteractedWith() {
        return lastInteractedWith;
    }
}
