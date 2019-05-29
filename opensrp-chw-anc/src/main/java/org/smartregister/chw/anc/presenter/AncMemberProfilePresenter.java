package org.smartregister.chw.anc.presenter;

import org.smartregister.chw.anc.activity.AncMemberProfileActivity;
import org.smartregister.chw.anc.contract.AncMemberProfileContract;
import org.smartregister.chw.anc.interactor.AncMemberProfileInteractor;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.view.contract.BaseProfileContract;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Map;

public class AncMemberProfilePresenter implements BaseProfileContract.Presenter {

    private static final String TAG = AncMemberProfilePresenter.class.getCanonicalName();

    private WeakReference<AncMemberProfileContract.View> view;
    private AncMemberProfileContract.Interactor interactor;
    private AncMemberProfileContract.Model model;

    private String childBaseEntityId;
    private String dob;
    private String familyID;
    private String familyName;
    private String familyHeadID;
    private String primaryCareGiverID;

    public AncMemberProfilePresenter(AncMemberProfileContract.View view, AncMemberProfileContract.Model model, String childBaseEntityId) {
        this.view = new WeakReference<>(view);
        this.interactor = new AncMemberProfileInteractor();
        this.model = model;
        this.childBaseEntityId = childBaseEntityId;
    }

    public AncMemberProfileContract.Model getModel() {
        return model;
    }

    public String getFamilyID() {
        return familyID;
    }



    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyHeadID() {
        return familyHeadID;
    }

    public void setFamilyHeadID(String familyHeadID) {
        this.familyHeadID = familyHeadID;
    }

    public String getPrimaryCareGiverID() {
        return primaryCareGiverID;
    }

    public void setPrimaryCareGiverID(String primaryCareGiverID) {
        this.primaryCareGiverID = primaryCareGiverID;
    }

    public CommonPersonObjectClient getChildClient() {
        return ((AncMemberProfileInteractor) interactor).getpClient();
    }

    public Map<String, Date> getVaccineList() {
        return ((AncMemberProfileInteractor) interactor).getVaccineList();
    }

    public String getFamilyId() {
        return familyID;
    }

    public String getDateOfBirth() {
        return dob;
    }


    @Override
    public void onDestroy(boolean b) {

    }
}
