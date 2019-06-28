package org.smartregister.chw.anc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.smartregister.chw.anc.model.BaseUpcomingService;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BaseUpcomingServiceAdapter extends RecyclerView.Adapter<BaseUpcomingServiceAdapter.MyViewHolder> {

    private List<BaseUpcomingService> serviceList;
    private Context context;

    public BaseUpcomingServiceAdapter(Context context, List<BaseUpcomingService> serviceList) {
        this.serviceList = serviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public BaseUpcomingServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.upcoming_service_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseUpcomingServiceAdapter.MyViewHolder holder, int i) {
        BaseUpcomingService service = serviceList.get(i);
        holder.tvName.setText(service.getServiceName());
        holder.tvDue.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(service.getServiceDate()));

        int period = Days.daysBetween(new DateTime(service.getServiceDate()).toLocalDate(), new DateTime().toLocalDate()).getDays();
        holder.tvOverdue.setText(context.getString(R.string.days_overdue, String.valueOf(period)));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDue, tvOverdue, tvName;
        private View myView;

        private MyViewHolder(View view) {
            super(view);
            tvDue = view.findViewById(R.id.due_date);
            tvOverdue = view.findViewById(R.id.overdue_state);
            tvName = view.findViewById(R.id.name);
            myView = view;
        }

        public View getView() {
            return myView;
        }
    }
}
