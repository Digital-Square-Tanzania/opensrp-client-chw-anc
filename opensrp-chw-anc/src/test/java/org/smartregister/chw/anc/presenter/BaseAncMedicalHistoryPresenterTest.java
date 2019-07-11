package org.smartregister.chw.anc.presenter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.contract.BaseAncMedicalHistoryContract;

import java.util.List;

public class BaseAncMedicalHistoryPresenterTest extends BaseUnitTest {

    @Mock
    private BaseAncMedicalHistoryContract.Interactor interactor;

    @Mock
    private BaseAncMedicalHistoryContract.View view;

    private Context context = RuntimeEnvironment.application;
    private BaseAncMedicalHistoryPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        String memberID = "12345";
        BaseAncMedicalHistoryPresenter obj = new BaseAncMedicalHistoryPresenter(interactor, view, memberID);
        presenter = Mockito.spy(obj);
        Mockito.doReturn(context).when(view).getViewContext();
    }

    @Test
    public void testInitialize() {
        presenter.initialize();

        Mockito.verify(interactor).getMemberHistory(Mockito.anyString(), Mockito.any(Context.class), Mockito.any(BaseAncMedicalHistoryPresenter.class));
    }

    @Test
    public void testOnDataFetchedNotifiesView() {
        List visits = Mockito.mock(List.class);
        presenter.onDataFetched(visits);

        Mockito.verify(view).onDataReceived(visits);
    }
}
