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
import org.ctdworld.propath.activity.ActivityEducationSelfReviewReport;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.model.SelfReview;
import java.util.ArrayList;
import java.util.List;



// this adapter
public class AdapterEducationSelfReviewList extends RecyclerView.Adapter<AdapterEducationSelfReviewList.MyViewHolder>
{
    private final String TAG = AdapterEducationSelfReviewList.class.getSimpleName();
    private Context mContext;
    private String athlete_id;
    private Listener mListener;
    private List<SelfReview> mSelfReviewList;

    public interface Listener{void deleteSelfReview(String reviewId);}

    public AdapterEducationSelfReviewList(Context context,Listener listener)
    {
        this.mSelfReviewList = new ArrayList<>();
        this.mContext = context;
        mListener = listener;
    }


    public void setData(List<SelfReview> selfReviewList, String athlete_id)
    {
        this.athlete_id = athlete_id;
        this.mSelfReviewList = selfReviewList;

        notifyDataSetChanged();
    }

    public void onDeletedSuccessfully(String id)
    {
        Log.i(TAG,"onDeletedSuccessfully() method called ");
        if (id == null)
            return;

        for (int i=0; i<mSelfReviewList.size(); i++)
        {
            int position =  getPosition(mSelfReviewList,id);
            if (position == ConstHelper.NOT_FOUND)
                return;

            mSelfReviewList.remove(position);
            notifyItemRemoved(position);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_education_self_reivew_list, parent, false);
        return new MyViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,  int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        final SelfReview selfReview = mSelfReviewList.get(position);

        holder.progress_list_date.setText(DateTimeHelper.getDateTime(selfReview.getDate(),DateTimeHelper.FORMAT_DATE_TIME));

        for(int i = 0 ; i <= mSelfReviewList.size(); i++)
        {
            String listText = "Self Review " + (holder.getAdapterPosition()+1);
            holder.progress_list_name.setText(listText);
        }

        holder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               SelfReview selfReview = mSelfReviewList.get(holder.getAdapterPosition());
                Intent seeProgressReportIntent = new Intent(mContext, ActivityEducationSelfReviewReport.class);
                seeProgressReportIntent.putExtra(ActivityEducationSelfReviewReport.ATHLETE_ID,athlete_id);
                seeProgressReportIntent.putExtra(ActivityEducationSelfReviewReport.REVIEW_ID,selfReview.getReviewId());
                mContext.startActivity(seeProgressReportIntent);
            }
        });



       // it user is looking its own review only then options menu will be visible
       if (mListener != null)
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);
       else
           holder.imgOptionsMenu.setVisibility(View.GONE);


       holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (mListener != null)
                   mListener.deleteSelfReview(selfReview.getReviewId());
           }
       });


    }


    @Override
    public int getItemCount() {
        return mSelfReviewList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView progress_list_name,progress_list_date;
        ImageView imgOptionsMenu;
        View layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            progress_list_name = itemView.findViewById(R.id.progress_list_name);
            progress_list_date = itemView.findViewById(R.id.progress_list_date);
            progress_list_date.setVisibility(View.VISIBLE);
            imgOptionsMenu = itemView.findViewById(R.id.img_options_menu);
            //checkBox = itemView.findViewById(R.id.checkbox);
        }

    }


    // # this method returns the position in String array based on selected text in array(Spinner)
    private static int getPosition(List<SelfReview> listSelfReview, String id)
    {
        int position = ConstHelper.NOT_FOUND;
        if (listSelfReview == null || listSelfReview.size() == 0  || id == null || id.isEmpty())
            return position;

        try {
            for (int i=0; i<listSelfReview.size(); i++)
            {
                String reviewId = listSelfReview.get(i).getReviewId();
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
