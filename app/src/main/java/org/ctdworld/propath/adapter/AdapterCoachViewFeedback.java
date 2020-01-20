package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCoachViewFeedbackDescription;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterCoachViewFeedback extends RecyclerView.Adapter<AdapterCoachViewFeedback.MyViewHolder>
{
    private static final String TAG = AdapterCoachViewFeedback.class.getSimpleName();
    Context mContext;
  //  ArrayList<HashMap<String,String>> mCoachFeedbackList = new ArrayList<>();
  private String mAthleteId;
   // HashMap<String,String> map = new HashMap<>();
    private Listener mListener;
    private List<CoachData> mCoachDataList ;

    public interface Listener{
        void onCoachFeedbackOptionClicked(String reviewId, String athleteId);
    }

    public AdapterCoachViewFeedback(Context context, Listener listener) {
        mCoachDataList = new ArrayList<>();
        this.mContext = context;
        mListener = listener;


    }


    public void setData(List<CoachData> coachDataList, String athlete_id)
    {
        this.mAthleteId = athlete_id;
        this.mCoachDataList = coachDataList;

        notifyDataSetChanged();
    }


    public void onDeletedSuccessfully(String id)
    {
        Log.i(TAG,"onDeletedSuccessfully() method called ");
        if (id == null)
            return;

        for (int i=0; i<mCoachDataList.size(); i++)
        {
            int position =  getPosition(mCoachDataList,id);
            if (position == ConstHelper.NOT_FOUND)
                return;

            mCoachDataList.remove(position);
            notifyItemRemoved(position);
        }
    }
    /* public AdapterCoachViewFeedback(Context context, ArrayList<HashMap<String,String>> mCoachFeedbackList, String athlete_id)
    {
        this.context = context;
        this.athlete_id = athlete_id;
        this.mCoachFeedbackList = mCoachFeedbackList;
    }
*/
    @NonNull
    @Override
    public AdapterCoachViewFeedback.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_coach_feedback_data,parent,false);


        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        return new AdapterCoachViewFeedback.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCoachViewFeedback.MyViewHolder holder, final int position)
    {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        final CoachData coachData = mCoachDataList.get(position);
        //map = mCoachFeedbackList.get(position);

        String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String coachRoleId = RoleHelper.COACH_ROLE_ID;
        String athleteRoleId = RoleHelper.ATHLETE_ROLE_ID;
        if (loggedInUser.equals(coachRoleId))
        {
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);

            for (int i = 0; i < mCoachDataList.size(); i++)
            {
                holder.coachFeedbackData.setText("Match Feedback " + (position + 1));
            }
        }
        else if (loggedInUser.equals(athleteRoleId))
        {
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);
            holder.coach_list_given_by.setVisibility(View.VISIBLE);
            holder.coach_list_given_by.setText("submitted by : " + coachData.getCoachName());

            for(int i = 0 ; i < mCoachDataList.size(); i++)
            {
                holder.coachFeedbackData.setText("Coach Feedback " + (position + 1));
            }
        }
        else {
            holder.imgOptionsMenu.setVisibility(View.GONE);
            holder.coach_list_given_by.setVisibility(View.VISIBLE);
            holder.coach_list_given_by.setText("submitted by : " + coachData.getCoachName());

            for(int i = 0 ; i < mCoachDataList.size(); i++)
            {
                holder.coachFeedbackData.setText("Coach Feedback " + (position + 1));
            }
        }


      /*  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String datedata =coachData.getDate();
        Date date1=null;
        try
        {
            date1 = simpleDateFormat.parse(datedata);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault());
        String dateInReqFormat = newformat.format(date1);*/

        holder.training_plan_date.setText(DateTimeHelper.getDateTime(coachData.getDate(),DateTimeHelper.FORMAT_DATE_TIME));

    /*    for(int i = 0 ; i <= mCoachFeedbackList.size(); i++)
        {
            holder.coachFeedbackData.setText("Coach Feedback " + (position+1));
        }
*/
        holder.coach_feedback_data_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CoachData coachData1 = mCoachDataList.get(position);
                Intent sendIntent = new Intent(mContext, ActivityCoachViewFeedbackDescription.class);
                sendIntent.putExtra("athlete_id",mAthleteId);
                sendIntent.putExtra("Review_ID",coachData1.getReviewID());
                mContext.startActivity(sendIntent);
            }
        });



       /* if (mListener != null)
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);
        else
            holder.imgOptionsMenu.setVisibility(View.GONE);*/

        holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCoachFeedbackOptionClicked(coachData.getReviewID(),mAthleteId);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return mCoachDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
         private TextView coachFeedbackData,training_plan_date,coach_list_given_by;
         private RelativeLayout coach_feedback_data_layout;
         private ImageView imgOptionsMenu;
         View layout;

        public MyViewHolder(View view)
        {
            super(view);
            layout = itemView;
            coachFeedbackData = view.findViewById(R.id.training_plan_data);
            training_plan_date = view.findViewById(R.id.training_plan_date);
            coach_feedback_data_layout = view.findViewById(R.id.coach_feedback_data_layout);
            coach_list_given_by = view.findViewById(R.id.coach_list_given_by);
            imgOptionsMenu = view.findViewById(R.id.img_options_menu);
        }
    }


    // # this method returns the position
    private static int getPosition(List<CoachData> listCoachData, String id)
    {
        int position = ConstHelper.NOT_FOUND;
        if (listCoachData == null || listCoachData.size() == 0  || id == null || id.isEmpty())
            return position;

        try {
            for (int i=0; i<listCoachData.size(); i++)
            {
                String reviewId = listCoachData.get(i).getReviewID();
                if (id.equals(reviewId))
                {
                    position = i;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return position;
    }
}
