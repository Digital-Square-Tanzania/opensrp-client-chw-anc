package org.smartregister.chw.anc.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String NEXT = "next";
        String ENCOUNTER_TYPE = "encounter_type";
    }

    interface EVENT_TYPE {
        String ANC_REGISTRATION = "Anc Registration";
        String ANC_HOME_VISIT = "ANC Home Visit";
        String UPDATE_EVENT_CONDITION = "Update";
    }

    interface FORMS {
        String ANC_REGISTRATION = "anc_registration";
    }

    interface TABLES {
        String ANC_MEMBERS = "ec_anc_register";
        String FAMILY_MEMBER = "ec_family_member";
        String PREGNANCY_OUTCOME = "ec_pregnancy_outcome";
    }

    interface CONFIGURATION {
        String ANC_REGISTER = "anc_register";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String TABLE_NAME = "TABLE";
    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
    }

    interface ANC_MEMBER_OBJECTS {
        String EDIT_MODE = "editMode";
        String MEMBER_PROFILE_OBJECT = "MemberObject";
        String FAMILY_HEAD_NAME = "familyHeadName";
        String FAMILY_HEAD_PHONE = "familyHeadPhoneNumber";
    }
}
