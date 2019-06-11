package org.smartregister.chw.anc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.opensrp_chw_anc.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseAncHomeVisitAdapter extends RecyclerView.Adapter<BaseAncHomeVisitAdapter.MyViewHolder> {
    private LinkedHashMap<String, BaseAncHomeVisitAction> ancHomeVisitActionList;
    private Context context;
    private BaseAncHomeVisitContract.View visitContractView;

    public BaseAncHomeVisitAdapter(Context context, BaseAncHomeVisitContract.View view, LinkedHashMap<String, BaseAncHomeVisitAction> myDataset) {
        ancHomeVisitActionList = myDataset;
        this.context = context;
        this.visitContractView = view;
    }

    @NotNull
    @Override
    public BaseAncHomeVisitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.anc_home_visit_item, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {

        BaseAncHomeVisitAction ancHomeVisitAction = new ArrayList<>(ancHomeVisitActionList.values()).get(position);
        holder.titleText.setText(MessageFormat.format("{0}{1}", ancHomeVisitAction.getTitle(), ancHomeVisitAction.isOptional() ? " - " + context.getString(R.string.optional) : ""));
        if (StringUtils.isNotBlank(ancHomeVisitAction.getSubTitle())) {
            holder.descriptionText.setText(ancHomeVisitAction.getSubTitle());
            holder.descriptionText.setVisibility(View.VISIBLE);
        } else {
            holder.descriptionText.setVisibility(View.GONE);
        }

        int color_res = getCircleColor(ancHomeVisitAction);

        holder.circleImageView.setCircleBackgroundColor(context.getResources().getColor(color_res));
        holder.circleImageView.setImageResource(R.drawable.ic_checked);
        holder.circleImageView.setColorFilter(context.getResources().getColor(R.color.white));

        if (color_res == R.color.transparent_gray) {
            holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.light_grey));
        } else {
            holder.circleImageView.setBorderColor(context.getResources().getColor(color_res));
        }

        bindClickListener(holder.getView(), ancHomeVisitAction);
    }

    private int getCircleColor(BaseAncHomeVisitAction ancHomeVisitAction) {
        int color_res;
        switch (ancHomeVisitAction.getActionStatus()) {
            case PENDING:
                color_res = R.color.transparent_gray;
                break;
            case COMPLETED:
                color_res = R.color.alert_complete_green;
                break;
            case PARTIALLY_COMPLETED:
                color_res = R.color.pnc_circle_yellow;
                break;
            default:
                color_res = R.color.alert_complete_green;
                break;
        }
        return color_res;
    }

    private void bindClickListener(View view, final BaseAncHomeVisitAction ancHomeVisitAction) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ancHomeVisitActionList.get(ancHomeVisitAction.getTitle()).setActionStatus(BaseAncHomeVisitAction.Status.COMPLETED);
                if (StringUtils.isNotBlank(ancHomeVisitAction.getFormName())) {
                    visitContractView.startForm(ancHomeVisitAction);
                } else {
                    visitContractView.startFragment(ancHomeVisitAction);
                }
                visitContractView.redrawVisitUI();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ancHomeVisitActionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, descriptionText;
        private CircleImageView circleImageView;
        private View myView;

        private MyViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.customFontTextViewTitle);
            descriptionText = view.findViewById(R.id.customFontTextViewDetails);
            circleImageView = view.findViewById(R.id.circleImageView);
            myView = view;
        }

        public View getView() {
            return myView;
        }
    }

}
