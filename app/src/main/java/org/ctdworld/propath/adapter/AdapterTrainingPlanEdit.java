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
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.TrainingPlan.PlanData;

import java.util.ArrayList;


public class AdapterTrainingPlanEdit extends RecyclerView.Adapter<AdapterTrainingPlanEdit.MyViewHolder>
{
    private static final String TAG = AdapterTrainingPlanEdit.class.getSimpleName();
    private Context mContext;
    private ArrayList<PlanData.PlanItem> mListPlanItem;
  //  private ArrayList<TrainingPlan.PlanData.PlanItem> mNewAddedPlanItemsList;  // contains list of new added TrainingPlan.PlanData.PlanItem object
    private ArrayList<PlanData.PlanItem> mEditedPlanItemsFromServerList;  // contains list of edited TrainingPlan.PlanData.PlanItem object
    private ArrayList<PlanData.PlanItem> mRemovedPlanItemsFromServerList;   // contains list of removed TrainingPlan.PlanData.PlanItem object
    private OnAdapterTrainingPlanCreateListener mListener;
    private int mUpdatingItemPosition = -1;

    public AdapterTrainingPlanEdit(Context context, OnAdapterTrainingPlanCreateListener listener) {
        this.mContext = context;
        this.mListPlanItem = new ArrayList<>();
        this.mListener = listener;
        mEditedPlanItemsFromServerList = new ArrayList<>();
        mRemovedPlanItemsFromServerList = new ArrayList<>();
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


    // adding TrainingPlan.PlanData.PlanItem list
    public void addPlanItemList(ArrayList<PlanData.PlanItem> planItems)
    {
        if (planItems != null)
        {
            mListPlanItem = planItems;
            notifyDataSetChanged();
        }
        else
            Log.e(TAG,"mListPlanItem is null or planItem is null in addPlanItem() method");
    }


    // this method returns list of newly added TrainingPlan.PlanData.PlanItem objects.
    public ArrayList<PlanData.PlanItem> getNewAddedPlanItemList()
    {
        // checking if any item's title is empty, if title is empty it will not be added in list
        ArrayList<PlanData.PlanItem> newItems = new ArrayList<>();

        for (PlanData.PlanItem planItem : mListPlanItem)
        {
           if (planItem != null && /*!planItem.isItemFromServer()*/ !isItemFromServer(planItem.getId()))
               newItems.add(planItem);
        }

        return newItems;  // returning List of TrainingPlan.PlanData.PlanItem which have been newly added

    }


    public ArrayList<PlanData.PlanItem> getRemovedPlanItemsFromServerList()
    {
        return mRemovedPlanItemsFromServerList;
    }


    public ArrayList<PlanData.PlanItem> getEditedPlanItemsFromSererList()
    {
        return mEditedPlanItemsFromServerList;
    }


    // this method returns latest list of training plan items.
    public ArrayList<PlanData.PlanItem> getPlanItemList()
    {
      /*  // checking if any item's title is empty, if title is empty it will not be added in list
        ArrayList<TrainingPlan.PlanData.PlanItem> filteredList = new ArrayList<>();
        for (int i=0; i<mListPlanItem.size(); i++)
        {
            TrainingPlan.PlanData.PlanItem planItem = mListPlanItem.get(i);
            if (planItem.getTitle() != null && !planItem.getTitle().isEmpty())
                filteredList.add(planItem);
        }

        return filteredList;  // returning filter list of TrainingPlan.PlanData.PlanItem*/
      return mListPlanItem;

    }


    // this method returns position of item which is being updated, like: changing image or video
    public int getUpdatingItemPosition()
    {
        return mUpdatingItemPosition;
    }


    // updating image
    public void updateItem(int position, PlanData.PlanItem planItem/*String url, String mediaType*/)
    {
        /*if (url == null)
            return;

        TrainingPlan.PlanData.PlanItem planItem = mListPlanItem.get(position);
        planItem.setMediaUrl(url);
        planItem.setMediaType(mediaType);*/
        if (planItem == null)
            return;

        mListPlanItem.set(position, planItem);
        notifyItemChanged(position);

        if (/*planItem.isItemFromServer()*/ isItemFromServer(planItem.getId()))
        {
            Log.i(TAG,"adding or updating item in mEditedPlanItemsFromServerList");
            if (!mEditedPlanItemsFromServerList.contains(planItem))
                mEditedPlanItemsFromServerList.add(planItem);
            else
                mEditedPlanItemsFromServerList.set(mEditedPlanItemsFromServerList.indexOf(planItem), planItem);
        }

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


        final PlanData.PlanItem planItem = mListPlanItem.get(holder.getAdapterPosition());

        // # setting data to views
        if (planItem != null)
        {
            holder.ediTitle.setText(planItem.getTitle());

            // setting text changed listener, to add edited item in mEditedPlanItemsList
            setOnEditTextChangedListener(holder.ediTitle, holder);


            String link;
            if (planItem.getLink() != null && !planItem.getLink().isEmpty() && !planItem.getLink().contains("null"))
            {
                link = UtilHelper.getYoutubeVideoThumbnailUrl(planItem.getLink());
                planItem.setMediaType(PlanData.PlanItem.MEDIA_TYPE_VIDEO);
            }
            else
                link = planItem.getMediaUrl();

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


            holder.mImgRemove.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mListPlanItem.remove(holder.getAdapterPosition()); // removing item from main item list
                    notifyItemRemoved(holder.getAdapterPosition());
                   // if (planItem.isItemFromServer())
                    if (isItemFromServer(planItem.getId()))  // # means this item has come from server, it has not been added while editing plan
                    {
                        mRemovedPlanItemsFromServerList.add(planItem);  // adding item to removed items list
                        mEditedPlanItemsFromServerList.remove(planItem);  // removing item from edited list, if it's present there
                    }
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                    {
                        mListener.onChangeImageOrVideoClickedInAdapter();
                        mUpdatingItemPosition = holder.getAdapterPosition();
                    }
                }
            });
        }
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
        View layoutImage, layout;
        public MyViewHolder(View view)
        {
            super(view);
            layout = view;
            ediTitle = view.findViewById(R.id.recycler_training_plan_create_txt_title);
            image = view.findViewById(R.id.recycler_training_plan_create_image_or_video);
            layoutImage = view.findViewById(R.id.recycler_training_plan_create_layout_image);
            imgVideoIcon = view.findViewById(R.id.recycler_training_plan_create_img_video_play_icon);
            mImgRemove = view.findViewById(R.id.recycler_training_plan_create_img_remove);
        }
    }


    // setting new latest title
    private void setOnEditTextChangedListener(EditText editText, final AdapterTrainingPlanEdit.MyViewHolder holder)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                PlanData.PlanItem planItem = mListPlanItem.get(holder.getAdapterPosition());
                planItem.setTitle(charSequence.toString());

                mListPlanItem.set(mListPlanItem.indexOf(planItem), planItem);

                if (/*planItem.isItemFromServer()*/ isItemFromServer(planItem.getId()))
                {
                    Log.i(TAG,"adding or updating item in mEditedPlanItemsFromServerList");
                    if (!mEditedPlanItemsFromServerList.contains(planItem))
                        mEditedPlanItemsFromServerList.add(planItem);
                    else
                        mEditedPlanItemsFromServerList.set(mEditedPlanItemsFromServerList.indexOf(planItem), planItem);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    private boolean isItemFromServer(String itemId)
    {
        return itemId != null && !itemId.isEmpty();  // # means this item has come from server, it has not been added while editing plan

    }

}
