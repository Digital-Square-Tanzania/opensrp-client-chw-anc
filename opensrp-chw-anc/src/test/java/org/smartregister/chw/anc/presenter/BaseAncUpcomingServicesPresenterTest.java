package org.smartregister.chw.anc.presenter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.contract.BaseAncUpcomingServicesContract;
import org.smartregister.chw.anc.domain.MemberObject;

import java.util.List;

public class BaseAncUpcomingServicesPresenterTest extends BaseUnitTest {

    @Mock
    private MemberObject memberObject;

    @Mock
    private BaseAncUpcomingServicesContract.Interactor interactor;

    @Mock
    private BaseAncUpcomingServicesContract.View view;

    private Context context = RuntimeEnvironment.application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitialize() {
        BaseAncUpcomingServicesPresenter presenter = new BaseAncUpcomingServicesPresenter(memberObject, interactor, view);
        Mockito.doReturn(context).when(view).getContext();

        BaseAncUpcomingServicesPresenter spy = Mockito.spy(presenter);

        spy.initialize();

        Mockito.verify(interactor).getUpComingServices(Mockito.any(MemberObject.class), Mockito.any(Context.class), Mockito.any(BaseAncUpcomingServicesPresenter.class));
    }

    @Test
    public void testOnDataFetched() {
        BaseAncUpcomingServicesPresenter presenter = new BaseAncUpcomingServicesPresenter(memberObject, interactor, view);
        Mockito.doReturn(context).when(view).getContext();

        List serviceList = Mockito.mock(List.class);

        BaseAncUpcomingServicesPresenter spy = Mockito.spy(presenter);
        spy.onDataFetched(serviceList);

        Mockito.verify(view).refreshServices(serviceList);
    }
}
