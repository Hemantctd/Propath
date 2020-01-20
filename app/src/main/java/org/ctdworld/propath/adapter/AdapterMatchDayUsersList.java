package org.ctdworld.propath.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.GetGroupNames;

import java.util.ArrayList;
import java.util.List;

public class AdapterMatchDayUsersList extends RecyclerView.Adapter<AdapterMatchDayUsersList.MyViewHolder>{

    private final String TAG = AdapterMatchDayUsersList.class.getSimpleName();
    Context mContext;
    List<GetGroupNames> checkedGroups;

    // constructor
    public AdapterMatchDayUsersList(Context context)
    {
         this.mContext = context;
    }

    public void addFriendsAndGroupList(List<GetGroupNames> groups)
    {
        this.checkedGroups = groups;
    }

    // filter all the friends and groups data
    public void filterList(ArrayList<GetGroupNames> filterdNames)
    {
        this.checkedGroups = filterdNames;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_survey_group_items,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       holder.groupName.setText("User Name");
//        holder.bindData(checkedGroups.get(position));
//
//        holder.groupCheck.setOnCheckedChangeListener(null);
//
//        //if true, your checkbox will be selected, else unselected
//        holder.groupCheck.setChecked(checkedGroups.get(position).isSelected());
//        holder.groupCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                checkedGroups.get(holder.getAdapterPosition()).setSelected(isChecked);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

//  for  initialization
    public static class MyViewHolder  extends RecyclerView.ViewHolder {
        ImageView group_image;
        TextView groupName;
        CheckBox groupCheck;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            group_image = itemView.findViewById(R.id.group_image);
            groupName = itemView.findViewById(R.id.groupName);
            groupCheck = itemView.findViewById(R.id.groupCheck);

        }

        public void bindData(GetGroupNames groupNames)
        {
            groupName.setText("User Name");
        }

    }
}
