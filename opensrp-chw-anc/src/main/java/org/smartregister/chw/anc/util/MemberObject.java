package org.smartregister.chw.anc.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberObject implements Serializable {
    String memberName;
    String lastMenstrualPeriod;
    String address;
    String chwMemberId;

    public MemberObject(String memberName, String lastMenstrualPeriod, String address, String chwMemberId) {
        this.memberName = memberName;
        this.lastMenstrualPeriod = lastMenstrualPeriod;
        this.address = address;
        this.chwMemberId = chwMemberId;
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

}
