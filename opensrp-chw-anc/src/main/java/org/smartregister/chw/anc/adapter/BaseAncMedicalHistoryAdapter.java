package org.smartregister.chw.anc.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.chw.anc.model.BaseHomeVisitHistoricAction;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseAncMedicalHistoryAdapter extends RecyclerView.Adapter<BaseAncMedicalHistoryAdapter.MyViewHolder> {

    protected List<List<BaseHomeVisitHistoricAction>> actionList;
    protected List<String> titles;


    public BaseAncMedicalHistoryAdapter(Map<String, List<BaseHomeVisitHistoricAction>> actionList) {
        this.actionList = new ArrayList<>(actionList.values());
        this.titles = new ArrayList<>(actionList.keySet());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.medical_history_row, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        holder.titleText.setText(Html.fromHtml(titles.get(i)));

    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, detailsText;
        private View myView;

        private MyViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.customFontTextViewTitle);
            detailsText = view.findViewById(R.id.customFontTextViewDetails);
            myView = view;
        }

        public View getView() {
            return myView;
        }
    }

}
