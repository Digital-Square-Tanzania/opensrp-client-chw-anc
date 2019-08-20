package org.smartregister.chw.anc.adapter;

import android.content.Context;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BaseAncHomeVisitAdapterTest extends BaseUnitTest {

    @Mock
    private BaseAncHomeVisitContract.View view;

    @Mock
    private LinkedHashMap<String, BaseAncHomeVisitAction> myDataset;

    private Context context = RuntimeEnvironment.application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCircleColorComplete() throws Exception {
        BaseAncHomeVisitAdapter ancHomeVisitAdapter = new BaseAncHomeVisitAdapter(context, view, myDataset);
        BaseAncHomeVisitAction ancHomeVisitAction = mock(BaseAncHomeVisitAction.class);
        Mockito.doReturn(BaseAncHomeVisitAction.Status.COMPLETED).when(ancHomeVisitAction).getActionStatus();

        int res = Whitebox.invokeMethod(ancHomeVisitAdapter, "getCircleColor", ancHomeVisitAction);
        assertEquals(res, R.color.alert_complete_green);
    }

    @Test
    public void testGetCircleColorPending() throws Exception {
        BaseAncHomeVisitAdapter ancHomeVisitAdapter = new BaseAncHomeVisitAdapter(context, view, myDataset);
        BaseAncHomeVisitAction ancHomeVisitAction = mock(BaseAncHomeVisitAction.class);
        Mockito.doReturn(BaseAncHomeVisitAction.Status.PENDING).when(ancHomeVisitAction).getActionStatus();

        int res = Whitebox.invokeMethod(ancHomeVisitAdapter, "getCircleColor", ancHomeVisitAction);
        assertEquals(res, R.color.transparent_gray);
    }

    @Test
    public void testGetCircleColorPartial() throws Exception {
        BaseAncHomeVisitAdapter ancHomeVisitAdapter = new BaseAncHomeVisitAdapter(context, view, myDataset);
        BaseAncHomeVisitAction ancHomeVisitAction = mock(BaseAncHomeVisitAction.class);
        Mockito.doReturn(BaseAncHomeVisitAction.Status.PARTIALLY_COMPLETED).when(ancHomeVisitAction).getActionStatus();

        int res = Whitebox.invokeMethod(ancHomeVisitAdapter, "getCircleColor", ancHomeVisitAction);
        assertEquals(res, R.color.pnc_circle_yellow);
    }

    @Test
    public void testBindClickListener() throws Exception {
        BaseAncHomeVisitAdapter ancHomeVisitAdapter = new BaseAncHomeVisitAdapter(context, view, myDataset);
        View view = mock(View.class);
        BaseAncHomeVisitAction ancHomeVisitAction = mock(BaseAncHomeVisitAction.class);

        Whitebox.invokeMethod(ancHomeVisitAdapter, "bindClickListener", view, ancHomeVisitAction);

        verify(view).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void testGetItemCount() {
        BaseAncHomeVisitAdapter ancHomeVisitAdapter = new BaseAncHomeVisitAdapter(context, view, generateActions(10));

        assertEquals(10, ancHomeVisitAdapter.getItemCount());
    }

    private LinkedHashMap<String, BaseAncHomeVisitAction> generateActions(int count) {
        LinkedHashMap<String, BaseAncHomeVisitAction> map = new LinkedHashMap<>();
        while (count > 0) {
            BaseAncHomeVisitAction action = Mockito.mock(BaseAncHomeVisitAction.class);
            Mockito.doReturn(true).when(action).isValid();
            map.put(String.valueOf(count), action);
            count--;
        }
        return map;
    }
}
