package org.smartregister.chw.anc.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberObject implements Serializable {
    String memberName;
    public MemberObject(String memberName){
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
