package org.smartregister.chw.pnc.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.anc.contract.BaseAncRegisterFragmentContract;

import static org.junit.Assert.*;

public class BasePncRegisterFragmentPresenterTest {

    @Mock
    private BaseAncRegisterFragmentContract.View view;

    @Mock
    private  BaseAncRegisterFragmentContract.Model model;

    private BasePncRegisterFragmentPresenter presenter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        BasePncRegisterFragmentPresenter objct = new BasePncRegisterFragmentPresenter(view, model, null);
        presenter = Mockito.spy(objct);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testUpdateSortAndFilter() {


    }

    @Test
    public void testGetMainCondition() {


    }

    @Test
    public void getDefaultSortQuery() {
    }

    @Test
    public void getMainTable() {
    }

    @Test
    public void getDueFilterCondition() {
    }
}