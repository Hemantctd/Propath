package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCoachFeedbackCoachView;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.helper.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterCoachFeedbackChooseAthleteName extends RecyclerView.Adapter<AdapterCoachFeedbackChooseAthleteName.MyViewHolder> {

    //private final String TAG = AdapterCoachFeedbackChooseAthleteName.class.getSimpleName();
    private Context mContext;
    private List<GetAthletes> mUserList;

    public AdapterCoachFeedbackChooseAthleteName(Context context, List<GetAthletes> userList) {
        this.mUserList = userList;
        this.mContext=context;
    }


    public void addUserList(List<GetAthletes> users)
    {
        // list will be shown only when letters are typed in search box
        this.mUserList = users;
    }

    public void filterList(ArrayList<GetAthletes> filterdNames) {
       this.mUserList = filterdNames;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public AdapterCoachFeedbackChooseAthleteName.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_choose_athlete_name, parent, false);

        return new AdapterCoachFeedbackChooseAthleteName.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterCoachFeedbackChooseAthleteName.MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);
        final GetAthletes user = mUserList.get(position);

        holder.txtName.setText(user.getName());
        if (user.getUserId().equals(SessionHelper.getInstance(mContext).getUser().getUserId()))
            holder.layout.setVisibility(View.GONE);



        UtilHelper.loadImageWithGlide(mContext, user.getUserPicUrl(), holder.imgUserPic, R.drawable.ic_profile);


        holder.imgUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ActivityProfileView.class);
               // intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                intent.putExtra(ConstHelper.Key.ID,user.getUserId());
                mContext.startActivity(intent);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = (new Intent(mContext, ActivityCoachFeedbackCoachView.class));
                intent.putExtra("athlete_id", user.getUserId());
                intent.putExtra("athlete_name", user.getName());
                intent.putExtra("athlete_image", user.getUserPicUrl());
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }



    // ViewHolder class
    public class MyViewHolder  extends RecyclerView.ViewHolder
    {
        private TextView txtName;
        private ImageView imgUserPic;
        RelativeLayout relativeLayout;

        View layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtName = itemView.findViewById(R.id.txt_name);
            imgUserPic = itemView.findViewById(R.id.img_user);
            relativeLayout = itemView.findViewById(R.id.recycler_athlete_name_search_layout);
        }


    }
}
