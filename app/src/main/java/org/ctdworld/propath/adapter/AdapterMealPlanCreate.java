package org.ctdworld.propath.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.MealPlan;

import java.util.ArrayList;

public class AdapterMealPlanCreate extends RecyclerView.Adapter<AdapterMealPlanCreate.MyViewHolder> {

    private static final String TAG = AdapterTrainingPlanCreate.class.getSimpleName();
    private Context mContext;
    private ArrayList<MealPlan.Plan.MealPlanItem> mListPlanItem;
    private AdapterMealPlanCreate.OnAdapterAdapterMealPlanCreateListener mListener;
    private int mUpdatingItemPosition = ConstHelper.NOT_FOUND;

    public AdapterMealPlanCreate(Context context, AdapterMealPlanCreate.OnAdapterAdapterMealPlanCreateListener listener) {
        this.mContext = context;
        this.mListPlanItem = new ArrayList<>();
        this.mListener = listener;
    }

    public interface OnAdapterAdapterMealPlanCreateListener {
        void onChangeImageOrVideoClickedInAdapter();
    }

    // adding TrainingPlan.Plan.PlanItem
    public void addPlanItem(MealPlan.Plan.MealPlanItem planItem) {
        if (mListPlanItem != null && planItem != null) {
            // planItem.setId(String.valueOf(getItemCount())); // for testing only, it has to be removed
            // planItem.setItemFromServer(true); // for testing only, it has to be removed
            mListPlanItem.add(planItem);
            notifyItemInserted(getItemCount());
        } else
            Log.e(TAG, "mListPlanItem is null or planItem is null in addPlanItem() method");
    }


    // adding TrainingPlan.Plan.PlanItem list
    public void addPlanItemList(ArrayList<MealPlan.Plan.MealPlanItem> planItems) {
        if (planItems != null) {
            mListPlanItem = planItems;
            notifyDataSetChanged();
        } else
            Log.e(TAG, "mListPlanItem is null or planItem is null in addPlanItem() method");
    }


    // this method returns latest list of training plan items.
    public ArrayList<MealPlan.Plan.MealPlanItem> getPlanItemList() {
        // checking if any item's title is empty, if title is empty it will not be added in list
       /* ArrayList<TrainingPlan.Plan.PlanItem> filteredList = new ArrayList<>();
        for (int i=0; i<mListPlanItem.size(); i++)
        {
            TrainingPlan.Plan.PlanItem planItem = mListPlanItem.get(i);
            if (planItem.getTitle() != null && !planItem.getTitle().isEmpty())
                filteredList.add(planItem);
        }

        return filteredList;  // returning filter list of TrainingPlan.Plan.PlanItem*/
        return mListPlanItem;

    }


    // this method returns position of item which is being updated, like: changing image or video
    public int getUpdatingItemPosition() {
        return mUpdatingItemPosition;
    }


    // updating image
    public void updateItem(int position, MealPlan.Plan.MealPlanItem planItem/*String url, String mediaType*/) {
        /*if (url == null)
            return;

        TrainingPlan.Plan.PlanItem planItem = mListPlanItem.get(position);
        planItem.setMediaUrl(url);
        planItem.setMediaType(mediaType);*/
        if (planItem == null)
            return;

        mListPlanItem.set(position, planItem);
        notifyItemChanged(position);
    }


    @Override
    public AdapterMealPlanCreate.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_training_plan_create, null);

        return new AdapterMealPlanCreate.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final AdapterMealPlanCreate.MyViewHolder holder, int position) {
        final MealPlan.Plan.MealPlanItem planItem = mListPlanItem.get(position);

        Log.i(TAG, "adapter id = " + planItem.getId());
        Log.i(TAG, "adapter title = " + planItem.getTitle());
        Log.i(TAG, "adapter mediaType = " + planItem.getMediaType());
        Log.i(TAG, "adapter mediaUrl = " + planItem.getMediaUrl());
        Log.i(TAG, "adapter position in adapter = " + planItem.getPositionInAdapter());

        // # setting data to views
        if (planItem != null) {
            holder.ediTitle.setText(planItem.getTitle());
            setOnEditTextChangedListener(holder.ediTitle, holder); // setting text changed listener


            String link = ConstHelper.NOT_FOUND_STRING;
            if (planItem.getLink() != null && !planItem.getLink().isEmpty()) {
                link = UtilHelper.getYoutubeVideoThumbnailUrl(planItem.getLink());
                planItem.setMediaType(MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO);
            } else
                link = planItem.getMediaUrl();

            Log.i(TAG, "mediaUrl = " + link);

            if (link != null && !link.isEmpty()) {
                int imageSize = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.trainingPlanItemImageSize));
                Glide.with(mContext)
                        .load(link)
                        .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                        .apply(new RequestOptions().error(R.drawable.img_default_black))
                        // .apply(new RequestOptions().centerCrop())
                        .apply(new RequestOptions().override(imageSize, imageSize))
                        .into(holder.image);

                if (MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO.equals(planItem.getMediaType()))
                    holder.imgVideoIcon.setVisibility(View.VISIBLE);
                else
                    holder.imgVideoIcon.setVisibility(View.GONE);
            }


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        Log.i(TAG, "add image or video clicked");
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


    }


    @Override
    public int getItemCount() {
        return mListPlanItem.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        // ImageView imgLeftMost;
        EditText ediTitle/*, txtCategory, txtDescription*/;
        ImageView image, imgVideoIcon, mImgRemove;

        public MyViewHolder(View view) {
            super(view);
            ediTitle = view.findViewById(R.id.recycler_training_plan_create_txt_title);
            image = view.findViewById(R.id.recycler_training_plan_create_image_or_video);
            imgVideoIcon = view.findViewById(R.id.recycler_training_plan_create_img_video_play_icon);
            mImgRemove = view.findViewById(R.id.recycler_training_plan_create_img_remove);
        }
    }


    // setting new latest title
    private void setOnEditTextChangedListener(EditText editText, final AdapterMealPlanCreate.MyViewHolder holder) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MealPlan.Plan.MealPlanItem planItem = mListPlanItem.get(holder.getAdapterPosition());
                planItem.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
