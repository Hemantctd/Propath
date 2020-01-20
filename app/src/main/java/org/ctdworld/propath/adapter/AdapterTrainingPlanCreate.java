package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;

import java.util.ArrayList;


public class AdapterTrainingPlanCreate extends RecyclerView.Adapter<AdapterTrainingPlanCreate.MyViewHolder>
{
    private static final String TAG = AdapterTrainingPlanCreate.class.getSimpleName();
    private Context mContext;
    private ArrayList<PlanData.PlanItem> mListPlanItem;
    private OnAdapterTrainingPlanCreateListener mListener;
    private int mUpdatingItemPosition = ConstHelper.NOT_FOUND;

    public AdapterTrainingPlanCreate(Context context, OnAdapterTrainingPlanCreateListener listener) {
        this.mContext = context;
        this.mListPlanItem = new ArrayList<>();
        this.mListener = listener;
    }

    public interface OnAdapterTrainingPlanCreateListener{void onChangeImageOrVideoClickedInAdapter();}

    // adding TrainingPlan.PlanData.PlanItem
    public void addPlanItem(PlanData.PlanItem planItem)
    {
        if (mListPlanItem!= null && planItem != null)
        {
            mListPlanItem.add(planItem);
            notifyItemInserted(getItemCount());
        }
        else
            Log.e(TAG,"mListPlanItem is null or planItem is null in addPlanItem() method");
    }



    // this method returns latest list of training plan items.
    public ArrayList<TrainingPlan.PlanData.PlanItem> getPlanItemList()
    {
       return mListPlanItem;
    }


    // this method returns position of item which is being updated, like: changing image or video
    public int getUpdatingItemPosition()
    {
        return mUpdatingItemPosition;
    }


    // updating plan item
    public void updateItem(int position, PlanData.PlanItem planItem)
    {
        if (planItem == null)
            return;

        mListPlanItem.set(position, planItem);
        notifyItemChanged(position);
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_training_plan_create,null);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        final PlanData.PlanItem planItem = mListPlanItem.get(position);

        // # setting data to views
        holder.ediTitle.setText(planItem.getTitle());
        setOnEditTextChangedListener(holder.ediTitle, holder); // setting text changed listener


        String link;
        if (planItem.getLink() != null && !planItem.getLink().isEmpty())
        {
            link = UtilHelper.getYoutubeVideoThumbnailUrl(planItem.getLink());
            planItem.setMediaType(PlanData.PlanItem.MEDIA_TYPE_VIDEO);
        }
        else
            link = planItem.getMediaUrl();

        Log.i(TAG,"mediaUrl = "+link);

        if (link != null && !link.isEmpty())
        {
            int imageSize = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.trainingPlanItemImageSize));
            Glide.with(mContext)
                    .load(link)
                    .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                    .apply(new RequestOptions().error(R.drawable.img_default_black))
                    // .apply(new RequestOptions().centerCrop())
                    .apply(new RequestOptions().override(imageSize, imageSize))
                    .into(holder.image);

            if (PlanData.PlanItem.MEDIA_TYPE_VIDEO.equals(planItem.getMediaType()))
                holder.imgVideoIcon.setVisibility(View.VISIBLE);
            else
                holder.imgVideoIcon.setVisibility(View.GONE);
        }


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                {
                    Log.i(TAG,"add image or video clicked");
                    mListener.onChangeImageOrVideoClickedInAdapter();
                    mUpdatingItemPosition = holder.getAdapterPosition();
                }
            }
        });


        holder.mImgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListPlanItem.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });


    }


    @Override
    public int getItemCount() {
        return mListPlanItem.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        // ImageView imgLeftMost;
        EditText ediTitle/*, txtCategory, txtDescription*/;
        ImageView image, imgVideoIcon, mImgRemove;
        View layout;
        public MyViewHolder(View view)
        {
            super(view);
            layout = view;
            ediTitle = view.findViewById(R.id.recycler_training_plan_create_txt_title);
            image = view.findViewById(R.id.recycler_training_plan_create_image_or_video);
            imgVideoIcon = view.findViewById(R.id.recycler_training_plan_create_img_video_play_icon);
            mImgRemove = view.findViewById(R.id.recycler_training_plan_create_img_remove);
        }
    }


    // setting new latest title
    private void setOnEditTextChangedListener(EditText editText, final AdapterTrainingPlanCreate.MyViewHolder holder)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                PlanData.PlanItem planItem = mListPlanItem.get(holder.getAdapterPosition());
                planItem.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
