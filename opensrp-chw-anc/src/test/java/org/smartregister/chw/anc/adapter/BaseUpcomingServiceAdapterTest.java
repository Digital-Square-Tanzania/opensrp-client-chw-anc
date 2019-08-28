package org.smartregister.chw.anc.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.model.BaseUpcomingService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BaseUpcomingServiceAdapterTest extends BaseUnitTest {

    @Mock
    private BaseUpcomingServiceAdapter.MyViewHolder holder;

    private Context context = RuntimeEnvironment.application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItemCount() {
        List<BaseUpcomingService> serviceList = new ArrayList<>();

        int max = 20;
        int min = 5;

        int x = (int) (Math.random() * (max - min));
        while (x < 0) {
            serviceList.add(new BaseUpcomingService());
            x--;
        }
        BaseUpcomingServiceAdapter adapter = new BaseUpcomingServiceAdapter(context, serviceList);

        assertEquals(serviceList.size(), adapter.getItemCount());
    }

 /*   @Test
    public void testOnBindViewHolder() {
        List<BaseUpcomingService> serviceList = new ArrayList<>();
        BaseUpcomingService service = new BaseUpcomingService();
        service.setServiceDate(new Date());
        service.setServiceName("My service");

        BaseUpcomingService spyService = Mockito.spy(service);
        serviceList.add(spyService);


        Whitebox.setInternalState(holder, "tvDue", Mockito.mock(TextView.class));
        Whitebox.setInternalState(holder, "tvOverdue", Mockito.mock(TextView.class));
       // Whitebox.setInternalState(holder, "tvName", Mockito.mock(TextView.class));
        Whitebox.setInternalState(holder, "myView", Mockito.mock(View.class));

        BaseUpcomingServiceAdapter adapter = new BaseUpcomingServiceAdapter(context, serviceList);

        adapter.onBindViewHolder(holder, 0);
        Mockito.verify(spyService, Mockito.times(2)).getServiceDate();
    }*/
}
