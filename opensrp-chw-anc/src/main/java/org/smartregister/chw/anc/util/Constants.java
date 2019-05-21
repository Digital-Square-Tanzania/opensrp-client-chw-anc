package org.smartregister.chw.anc.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String NEXT = "next";
    }

    interface EVENT_TYPE {
        String ANC_REGISTRATION = "Anc Registration";
    }

    interface FORMS {
        String ANC_REGISTRATION = "anc_registration";
    }

    interface TABLES {
        String ANC_MEMBERS = "ec_anc_register";
    }

    interface CONFIGURATION {
        String ANC_REGISTER = "anc_register";
    }
}
