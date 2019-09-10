package org.smartregister.chw.anc.presenter;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.anc.contract.BaseAncMemberProfileContract;
import org.smartregister.chw.anc.domain.MemberObject;

import java.util.Date;

public class BaseAncMemberProfilePresenterTest {

    @Mock
    private BaseAncMemberProfileContract.View view;

    @Mock
    private BaseAncMemberProfileContract.Interactor interactor;

    @Mock
   private MemberObject memberObject;


    private BaseAncMemberProfilePresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        BaseAncMemberProfilePresenter baseAncMemberProfilePresenter = new BaseAncMemberProfilePresenter(view, interactor, memberObject);
        presenter = Mockito.spy(baseAncMemberProfilePresenter);
    }

    @Test
    public void testRefreshProfileTopSection() {
        String entityType = memberObject.getBaseEntityId();
        String memberName = memberObject.getMemberName();
        String memberGA = String.valueOf(memberObject.getGestationAge());
        String memberAddress = memberObject.getAddress();
        String memberChwMemberId = memberObject.getChwMemberId();
        String memberBaseObjectId = memberObject.getBaseEntityId();
        presenter.refreshProfileTopSection(memberObject);
        Mockito.verify(view).setMemberName(memberName);
        Mockito.verify(view).setMemberGA(memberGA);
        Mockito.verify(view).setMemberAddress(memberAddress);
        Mockito.verify(view).setMemberAddress(memberChwMemberId);
        Mockito.verify(view).setProfileImage(memberBaseObjectId, entityType);

    }


    @Test
    public void testRefreshProfileBottom() {
        presenter.refreshProfileBottom();
        Mockito.verify(interactor).refreshProfileInfo(memberObject, presenter);
    }

    @Test
    public void testFetchProfileData() {
        presenter.fetchProfileData();
        Mockito.verify(interactor).refreshProfileView(memberObject, false, presenter);
    }

    @Test
    public void testRefreshLastVisit() {
        Date lastVisitDate = new Date();
        presenter.refreshLastVisit(lastVisitDate);
        Mockito.verify(view).setLastVisit(lastVisitDate);
    }
  /*
  @Test
    public void testRefreshUpComingServicesStatus(){
        String service = anyString();
        Date date = DateTime.now().toDate();

        PowerMockito.mockStatic(AlertStatus.class);

        presenter.refreshUpComingServicesStatus(service,,date);

        Mockito.verify(view).setUpComingServicesStatus(service,alertStatus,date);

    }
    */

    @Test
    public void testGetView() {
        presenter.getView();
        Mockito.verify(view);
    }

}