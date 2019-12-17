package org.smartregister.chw.anc.interactor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.chw.anc.contract.BaseAncRegisterContract;
import org.smartregister.chw.anc.util.AppExecutors;

import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class BaseAncRegisterInteractorTest implements Executor {

    private BaseAncRegisterInteractor interactor;

    @Mock
    private BaseAncRegisterContract.Model model;

    @Mock
    private BaseAncRegisterContract.InteractorCallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AppExecutors appExecutors = new AppExecutors(this, this, this);
        interactor = Mockito.spy(new BaseAncRegisterInteractor(appExecutors));
    }

    @Test
    public void testGetModel() {
        BaseAncRegisterContract.Model ancModel = interactor.getModel();
        Assert.assertNotNull(ancModel);

        interactor.setModel(model);
        Assert.assertEquals(model, interactor.getModel());
    }

    @Test
    public void testSaveRegistration() {
        interactor.saveRegistration("{}", false, callBack, "ec_anc_register");

        Mockito.verify(callBack).onRegistrationSaved(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean());
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
