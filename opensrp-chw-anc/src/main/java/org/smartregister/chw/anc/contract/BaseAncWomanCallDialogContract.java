package org.smartregister.chw.anc.contract;

import android.content.Context;

public interface BaseAncWomanCallDialogContract {

    interface View {

        Dialer getPendingCallRequest();

        void setPendingCallRequest(Dialer dialer);

        Presenter initializePresenter();

        Context getCurrentContext();
    }

    interface Presenter {

        void initalize();

    }

    interface Model {

        String getName();

        void setName(String name);

        String getRole();

        void setRole(String role);

        String getPhoneNumber();

        void setPhoneNumber(String phoneNumber);
    }

    interface Dialer {
        void callMe();
    }

}
