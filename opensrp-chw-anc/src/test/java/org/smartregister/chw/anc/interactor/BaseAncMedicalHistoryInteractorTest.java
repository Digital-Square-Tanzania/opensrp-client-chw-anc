package org.smartregister.chw.anc.interactor;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.chw.anc.contract.BaseAncMedicalHistoryContract;
import org.smartregister.chw.anc.util.AppExecutors;

import java.util.concurrent.Executor;
@RunWith(MockitoJUnitRunner.class)
public class BaseAncMedicalHistoryInteractorTest implements Executor {

    private BaseAncMedicalHistoryInteractor interactor;

    @Mock
    private BaseAncMedicalHistoryContract.InteractorCallBack callBack;

    @Mock
    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AppExecutors appExecutors = new AppExecutors(this, this, this);
        interactor = Mockito.spy(new BaseAncMedicalHistoryInteractor(appExecutors));
    }

    @Test
    public void testGetMemberHistory() {
        interactor.getMemberHistory("12345", context, callBack);
        Mockito.verify(callBack).onDataFetched(Mockito.anyList());
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
