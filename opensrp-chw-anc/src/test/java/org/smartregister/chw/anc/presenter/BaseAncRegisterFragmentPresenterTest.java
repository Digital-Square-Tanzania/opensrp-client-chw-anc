package org.smartregister.chw.anc.presenter;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.smartregister.chw.anc.contract.BaseAncRegisterFragmentContract;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.ViewConfiguration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.validateMockitoUsage;

public class BaseAncRegisterFragmentPresenterTest {

    @Mock
    private BaseAncRegisterFragmentContract.View view;

    @Mock
    private BaseAncRegisterFragmentContract.Model model;


    private BaseAncRegisterFragmentPresenter presenter;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        BaseAncRegisterFragmentPresenter objct = new BaseAncRegisterFragmentPresenter(view, model, "abc");
        presenter = Mockito.spy(objct);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }


    @Test
    public void testProcessViewConfigurationsSearchBarTextNotNull() {
        ViewConfiguration configuration = new ViewConfiguration();
        Mockito.when(model.getViewConfiguration("abc"))
                .thenReturn(configuration);

        RegisterConfiguration config = new RegisterConfiguration();
        //
        configuration.setMetadata(config);
        config.setSearchBarText("SearchBarText");

        presenter.processViewConfigurations();

        Mockito.verify(view).updateSearchBarHint(anyString());
    }

    @Test
    public void testProcessViewConfigurationsSearchBarTextNull() {
        ViewConfiguration configuration = new ViewConfiguration();
        RegisterConfiguration config = new RegisterConfiguration();
        config.setSearchBarText(null);
        configuration.setMetadata(config);

        Mockito.when(model.getViewConfiguration(anyString()))
                .thenReturn(configuration);

        presenter.processViewConfigurations();

        Mockito.verify(view, Mockito.never()).updateSearchBarHint(anyString());

    }

    @Test
    public void testGetView() {
        BaseAncRegisterFragmentContract.View myView = presenter.getView();
        Assert.assertEquals(myView, view);
    }


}


