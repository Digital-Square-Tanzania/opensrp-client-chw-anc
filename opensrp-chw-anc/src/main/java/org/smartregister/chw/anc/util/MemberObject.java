package org.smartregister.chw.anc.util;

import java.io.Serializable;

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
}
