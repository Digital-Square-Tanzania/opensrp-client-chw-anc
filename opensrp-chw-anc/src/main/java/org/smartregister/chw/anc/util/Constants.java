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
    }

    interface FORMS {
        String ANC_REGISTRATION = "anc_registration";

        interface HOME_VISIT_FORMS {
            String  ANC_CARD_FORM = "anc_card_form";
        }
    }

    interface TABLES {
        String ANC_MEMBERS = "ec_anc_register";
        String FAMILY_MEMBER = "ec_family_member";
    }

    interface CONFIGURATION {
        String ANC_REGISTER = "anc_register";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String ACTION = "ACTION";
    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
    }

    interface ANC_MEMBER_OBJECTS {
        String MEMBER_PROFILE_OBJECT = "MemberObject";
    }
}
