package org.smartregister.chw.anc.presenter;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitFragmentContract;
import org.smartregister.chw.anc.fragment.BaseAncHomeVisitFragment;

import java.util.List;

public class BaseAncHomeVisitFragmentPresenterTest {

    @Mock
    private BaseAncHomeVisitFragmentContract.View view;

    @Mock
    private BaseAncHomeVisitFragmentContract.Model model;

    @Mock
    private List<JSONObject> options;


    private BaseAncHomeVisitFragmentPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        BaseAncHomeVisitFragmentPresenter objct = new BaseAncHomeVisitFragmentPresenter(view, model);
        presenter = Mockito.spy(objct);
    }

    @Test
    public void initialize() {
        presenter.initialize();
        Mockito.verify(model).processJson(view.getJsonObject(), presenter);
    }

    @Test
    public void setTitle() {
        String title = "Title";
        presenter.setTitle(title);
        Mockito.verify(view).setTitle(title);

    }

    @Test
    public void setQuestion() {
        String question = "question?";
        presenter.setQuestion(question);
        Mockito.verify(view).setQuestion(question);

    }

    @Test
    public void setImageRes() {
        Integer img = 12345654;
        presenter.setImageRes(img);
        Mockito.verify(view).setImageRes(img);
    }

    @Test
    public void setQuestionType() {
        BaseAncHomeVisitFragment baseAncHomeVisitFragment = new BaseAncHomeVisitFragment();
        baseAncHomeVisitFragment.setQuestionType(baseAncHomeVisitFragment.getQuestionType());
        presenter.setQuestionType(baseAncHomeVisitFragment.getQuestionType());
        Mockito.verify(view).setQuestionType(baseAncHomeVisitFragment.getQuestionType());
    }

 /*@Test
    public void setInfoIconTitle() {
        String infoIconTile = "StringInfoIcon";
        presenter.setInfoIconTitle(infoIconTile);
        Mockito.verify(view).setInfoIconTitle(infoIconTile);
    }


    @Test
    public void setInfoIconDetails() {
        String iconDetails = "StringIconDetails";
        presenter.setInfoIconDetails(iconDetails);
        Mockito.verify(view).setInfoIconDetails(iconDetails);
    }
*/
    @Test
    public void getView() {
        presenter.getView();
        Mockito.verify(view);
    }

    @Test
    public void writeValue() {
        JSONObject obj = new JSONObject();
        String value = "value";
        presenter.writeValue(obj, value);
        Mockito.verify(model).writeValue(obj, value);

    }

    @Test
    public void setValue() {
        String value = "value";
        presenter.setValue(value);
        Mockito.verify(view).setValue(value);
    }

    @Test
    public void setOptions() {
        options.add(view.getJsonObject());
        presenter.setOptions(options);
        Mockito.verify(view).setOptions(options);
    }
}