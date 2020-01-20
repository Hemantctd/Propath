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
import org.ctdworld.propath.activity.ActivityCoachSelfReviewDescription;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.model.CoachData;

import java.util.ArrayList;
import java.util.List;


// this adapter
public class AdapterCoachSelfReviewList extends RecyclerView.Adapter<AdapterCoachSelfReviewList.MyViewHolder>
{
    private final String TAG = AdapterCoachSelfReviewList.class.getSimpleName();
    private Context mContext;
    //private String mAthleteID;
    private Listener mListener;
    private List<CoachData> mCoachSelfReviewList;

    public interface Listener{void onCoachSelfReviewOptionClicked(String reviewId, String athleteId);}

    public AdapterCoachSelfReviewList(Context context, Listener listener)
    {
        this.mCoachSelfReviewList = new ArrayList<>();
        this.mContext = context;
        mListener = listener;
    }


    public void setData(List<CoachData> selfReviewList)
    {
       // this.mAthleteID = athleteID;
        this.mCoachSelfReviewList = selfReviewList;

        notifyDataSetChanged();
    }

    public void onDeletedSuccessfully(String id)
    {
        Log.i(TAG,"onDeletedSuccessfully() method called ");
        if (id == null)
            return;

        for (int i=0; i<mCoachSelfReviewList.size(); i++)
        {
            int position =  getPosition(mCoachSelfReviewList,id);
            if (position == ConstHelper.NOT_FOUND)
                return;

            mCoachSelfReviewList.remove(position);
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

       final CoachData coachSelfReview = mCoachSelfReviewList.get(position);
       holder.mDate.setText(DateTimeHelper.getDateTime(coachSelfReview.getDate(),DateTimeHelper.FORMAT_DATE_TIME));

        for(int i = 0 ; i <= mCoachSelfReviewList.size(); i++)
        {
            String listText = "Self Review " + (holder.getAdapterPosition()+1);
            holder.mName.setText(listText);
        }

        holder.mLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CoachData coachData = mCoachSelfReviewList.get(holder.getAdapterPosition());
                Intent seeAthleteReviewIntent = new Intent(mContext, ActivityCoachSelfReviewDescription.class);
                seeAthleteReviewIntent.putExtra(ActivityCoachSelfReviewDescription.ATHLETE_ID,coachSelfReview.getAthleteID());
                seeAthleteReviewIntent.putExtra(ActivityCoachSelfReviewDescription.REVIEW_ID,coachData.getReviewID());
                mContext.startActivity(seeAthleteReviewIntent);
            }
        });

        // it user is looking its own review only then options menu will be visible
        if (mListener != null)
            holder.mImgOptionsMenu.setVisibility(View.VISIBLE);
        else
            holder.mImgOptionsMenu.setVisibility(View.GONE);

        holder.mImgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCoachSelfReviewOptionClicked(coachSelfReview.getReviewID(), coachSelfReview.getAthleteID());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCoachSelfReviewList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mName,mDate;
        private View mLayout;
        private ImageView mImgOptionsMenu;
        View layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            mName = itemView.findViewById(R.id.progress_list_name);
            mDate = itemView.findViewById(R.id.progress_list_date);
            mDate.setVisibility(View.VISIBLE);
            mLayout = itemView.findViewById(R.id.progress_report_layout);
            mImgOptionsMenu = itemView.findViewById(R.id.img_options_menu);
        }

    }


    // # this method returns the position in String array based on selected text in array(Spinner)
    private static int getPosition(List<CoachData> listSelfReview, String id)
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
