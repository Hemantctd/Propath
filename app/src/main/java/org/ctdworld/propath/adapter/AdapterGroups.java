package org.ctdworld.propath.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.GetGroupNames;

import java.util.ArrayList;
import java.util.List;

public class AdapterGroups extends RecyclerView.Adapter<AdapterGroups.MyViewHolder> {

    private final String TAG = AdapterGroups.class.getSimpleName();
    Context mContext;
    List<GetGroupNames> checkedGroups;
    private int SELETCED_POS = -1;

    // constructor
    public AdapterGroups(Context context, List<GetGroupNames> groupNames) {
        this.mContext = context;
        this.checkedGroups = groupNames;
    }

    public void addFriendsAndGroupList(List<GetGroupNames> groups) {
        this.checkedGroups = groups;
    }

    // filter all the friends and groups data
    public void filterList(ArrayList<GetGroupNames> filterdNames) {
        this.checkedGroups = filterdNames;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_survey_group_items, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        GetGroupNames groupNames = checkedGroups.get(position);
        holder.groupName.setText(groupNames.getName());
        int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
        Glide.with(mContext)
                .load(groupNames.getProfile_image())
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picDimen, picDimen))
                .into(holder.group_image);

        if (position == SELETCED_POS) {
            holder.groupCheck.setChecked(true);
        } else {
            holder.groupCheck.setChecked(false);
        }

        holder.groupCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SELETCED_POS != -1) {
                    checkedGroups.get(SELETCED_POS).setSelected(false);
                    checkedGroups.get(position).setSelected(true);
                    SELETCED_POS = position;
                } else {
                    checkedGroups.get(position).setSelected(true);
                    SELETCED_POS = position;
                }
                notifyDataSetChanged();
            }
        });
        // holder.bindData(checkedGroups.get(position));

//        holder.groupCheck.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        /*holder.groupCheck.setChecked(checkedGroups.get(position).isSelected());
        holder.groupCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedGroups.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return checkedGroups.size();
    }

    //  for  initialization
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView group_image;
        TextView groupName;
        CheckBox groupCheck;


        public MyViewHolder(View itemView) {
            super(itemView);
            group_image = itemView.findViewById(R.id.group_image);
            groupName = itemView.findViewById(R.id.groupName);
            groupCheck = itemView.findViewById(R.id.groupCheck);

        }

//        public void bindData(GetGroupNames groupNames)
//        {
//            groupName.setText(groupNames.getName());
//
//
//
//        }

    }
}
