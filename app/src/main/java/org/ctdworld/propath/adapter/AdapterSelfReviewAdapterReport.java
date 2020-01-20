package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.SelfReview;

import java.util.List;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class AdapterSelfReviewAdapterReport extends RecyclerView.Adapter<AdapterSelfReviewAdapterReport.MyViewHolder> {

    Context mContext;
    private List<SelfReview> selfReviewList;

    public AdapterSelfReviewAdapterReport(Context context,List<SelfReview> selfReviewList)
    {
        this.mContext = context;
        this.selfReviewList = selfReviewList;

    }

    public  void updateList(List<SelfReview> selfReview)
    {
        this.selfReviewList = selfReview;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_self_review_report_layout,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        SelfReview selfReview = selfReviewList.get(position);
        Log.d(TAG,"subjects = "+selfReview.getSubject());
        holder.selfReviewSubjects.setText(selfReview.getSubject());
        String grade = selfReview.getGrade();
        if(grade.equals("0"))
        {
           /* holder.imgDislike.setImageResource(R.drawable.ic_dislike_icon);
            holder.imgDislike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));*/
            DrawableCompat.setTint(holder.imgDisLike.getDrawable(), mContext.getResources().getColor(R.color.colorTheme));
        }
        if (grade.equals("1"))
        {
           // holder.imgDislike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
            DrawableCompat.setTint(holder.imgLike.getDrawable(), mContext.getResources().getColor(R.color.colorTheme));
        }
    }

    @Override
    public int getItemCount() {
        return selfReviewList.size();
    }

    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        EditText selfReviewSubjects;
        ImageView imgDisLike, imgLike;
        View layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            selfReviewSubjects = itemView.findViewById(R.id.self_review_subjects);
            imgDisLike = itemView.findViewById(R.id.self_review_like_dislike);
            imgLike = itemView.findViewById(R.id.self_review_like);
            selfReviewSubjects.setFocusable(false);

        }

    }
}
