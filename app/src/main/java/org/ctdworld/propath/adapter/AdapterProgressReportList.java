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
import android.widget.TextView;
import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationProgressReport;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.SchoolReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import java.util.ArrayList;
import java.util.List;

public class AdapterProgressReportList extends RecyclerView.Adapter<AdapterProgressReportList.MyViewHolder>
{
    private final String TAG = AdapterProgressReportList.class.getSimpleName();
    private Context mContext;
    private String athlete_id;
    private Listener mListener;
    private List<SchoolReview> mProgressReportList ;



    public interface Listener { void onProgressReportOptionClicked(String reviewId, String athleteId);}


    public AdapterProgressReportList(Context context, Listener listener) {
        mProgressReportList = new ArrayList<>();
        this.mContext = context;
        mListener = listener;


    }


    public void setData(List<SchoolReview> progressReportList, String athlete_id)
    {
        this.athlete_id = athlete_id;
        this.mProgressReportList = progressReportList;

        notifyDataSetChanged();
    }


    public void onDeletedSuccessfully(String id)
    {
        Log.i(TAG,"onDeletedSuccessfully() method called ");
        if (id == null)
            return;

        for (int i=0; i<mProgressReportList.size(); i++)
        {
            int position =  getPosition(mProgressReportList,id);
            if (position == ConstHelper.NOT_FOUND)
                return;

            mProgressReportList.remove(position);
            notifyItemRemoved(position);
        }
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_education_self_reivew_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        final SchoolReview schoolReview = mProgressReportList.get(position);
        String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String teacherRoleId = RoleHelper.TEACHER_ROLE_ID;

        if (loggedInUser.equals(teacherRoleId))
        {
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);

            for (int i = 0; i < mProgressReportList.size(); i++)
            {
                String text = "Progress Report " + (position + 1);
                holder.teacherTextView.setText(text);
            }
        }


        else{
            holder.imgOptionsMenu.setVisibility(View.GONE);
            holder.progress_list_given_by.setVisibility(View.VISIBLE);
            holder.progress_list_given_by.setText(String.format("submitted by : %s", schoolReview.getTeacherName()));

            for(int i = 0 ; i < mProgressReportList.size(); i++)
            {
                String text = "Progress Report " + (position+1);
                holder.teacherTextView.setText(text);
            }
        }


        holder.progress_list_date.setText(DateTimeHelper.getDateTime(schoolReview.getDate(),DateTimeHelper.FORMAT_DATE_TIME));

        holder.progress_report_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //map = mProgressReportList.get(position);
                   SchoolReview schoolReview = mProgressReportList.get(position);

                    Intent seeProgressReportIntent = new Intent(mContext, ActivityEducationProgressReport.class);
                    seeProgressReportIntent.putExtra(ActivityEducationProgressReport.PROGRESS_ATHLETE_ID,athlete_id);
                    seeProgressReportIntent.putExtra(ActivityEducationProgressReport.PROGRESS_REVIEW_ID, schoolReview.getReviewID());
                    mContext.startActivity(seeProgressReportIntent);

            }
        });

        holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (mListener != null)
                    mListener.onProgressReportOptionClicked(schoolReview.getReviewID(), schoolReview.getAthleteID());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mProgressReportList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView teacherTextView,progress_list_date,progress_list_given_by;
        View progress_report_layout;
        ImageView imgOptionsMenu;
        View layout;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            progress_list_date = itemView.findViewById(R.id.progress_list_date);
            teacherTextView = itemView.findViewById(R.id.progress_list_name);
            progress_list_date.setVisibility(View.VISIBLE);
            progress_report_layout = itemView.findViewById(R.id.progress_report_layout);
            progress_list_given_by = itemView.findViewById(R.id.progress_list_given_by);
            imgOptionsMenu = itemView.findViewById(R.id.img_options_menu);

        }

    }



    // # this method returns the position
    private static int getPosition(List<SchoolReview> listSelfReview, String id)
    {
        int position = ConstHelper.NOT_FOUND;
        if (listSelfReview == null || listSelfReview.size() == 0  || id == null || id.isEmpty())
            return position;

        try {
            for (int i=0; i<listSelfReview.size(); i++)
            {
                String reviewId = listSelfReview.get(i).getReviewID();
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
