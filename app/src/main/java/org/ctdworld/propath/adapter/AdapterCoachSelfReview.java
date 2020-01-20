package org.ctdworld.propath.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.SelfReview;

import java.util.ArrayList;

public class AdapterCoachSelfReview extends RecyclerView.Adapter<AdapterCoachSelfReview.MyViewHolder> {

    private final String TAG = AdapterCoachSelfReview.class.getSimpleName();
    Context mContext;

    public AdapterCoachSelfReview(Context context, ArrayList<SelfReview> selfReviewlist, String modeCreateOrEdit)
    {
        this.mContext = context;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_self_review_report_layout,null);

        return new MyViewHolder(view);
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);
            holder.editSubject.setHint("Subject " +(holder.getAdapterPosition()+1));


    }

    @Override
    public int getItemCount()
    {
        return 7;
    }

    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        EditText editSubject;
        ImageView imgDisLike, imgLike;
         View layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            editSubject = itemView.findViewById(R.id.self_review_subjects);
            imgDisLike = itemView.findViewById(R.id.self_review_like_dislike);
            imgLike = itemView.findViewById(R.id.self_review_like);
        }

    }


}
