package org.ctdworld.propath.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;

import java.util.ArrayList;


public class AdapterTrainingPlanItemsView extends RecyclerView.Adapter<AdapterTrainingPlanItemsView.MyViewHolder>
{
    private static final String TAG = AdapterTrainingPlanItemsView.class.getSimpleName();
    private Context mContext;
    private ArrayList<TrainingPlan.PlanData.PlanItem> mListPlanItem = new ArrayList<>();
    private TrainingPlan.PlanData mPlanData;
    private boolean isDeleteModeEnabled = false;
    private OnItemClickedListener mItemClickListener;
    private SparseBooleanArray mSelectedItemsBoolean;


    public AdapterTrainingPlanItemsView(Context context, OnItemClickedListener longClickedListener) {
        this.mContext = context;
        this.mItemClickListener = longClickedListener;
        mSelectedItemsBoolean = new SparseBooleanArray(getItemCount());
    }


    public interface OnItemClickedListener { void onItemClickedListener(PlanData planData, int adapterPosition);}


    public void addTrainingPlan(PlanData planData)
    {
        mPlanData = planData;
        if (planData != null)
        {
            mListPlanItem = planData.getPlanItemList();
           notifyDataSetChanged();
        }
    }



    // to refresh list after deleting note. only self created note will be deleted
    public void onTrainingPlanItemDeleted(TrainingPlan.PlanData.PlanItem planItem)
    {
        if (planItem != null)
        {
            int position = getPlanItemPosition(planItem.getId());
            if (position<0)
                return;

            try {
                mListPlanItem.remove(position);
                notifyItemRemoved(position);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void onTrainingPlanItemEdited(PlanData.PlanItem planItem)
    {
        Log.i(TAG,"onTrainingPlanItemDeleted() method called ");
        if (planItem == null)
            return;


        int position = getPlanItemPosition(planItem.getId());
        if (position <0)
            return;


        mListPlanItem.set(position, planItem);
        notifyItemChanged(position);
    }

    // clearing all data from list
    public void clearTrainingPlanItemList()
    {
        if (mListPlanItem != null)
        {
            mListPlanItem.clear();
            notifyDataSetChanged();
        }
    }


    public void enableDeleteMode()
    {
        isDeleteModeEnabled = true;
        notifyDataSetChanged();
    }

    public void disableDeleteMode()
    {
        isDeleteModeEnabled = false;
        notifyDataSetChanged();
    }


    // returns selected training plans
    public ArrayList<TrainingPlan.PlanData.PlanItem> getSelectedItems()
    {
        Log.i(TAG,"getPlanItemsArray() method called , selected items boolean = "+mSelectedItemsBoolean.toString());
        ArrayList<TrainingPlan.PlanData.PlanItem> selectedPlans = new ArrayList<>();
        if (mSelectedItemsBoolean != null)
        {
            for (int i=0; i<mSelectedItemsBoolean.size(); i++)
                if (mSelectedItemsBoolean.get(i,false))  // selected item, it true
                    selectedPlans.add(mListPlanItem.get(i));
        }
        return selectedPlans;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_training_plan_items_view,parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        final TrainingPlan.PlanData.PlanItem planItem = mListPlanItem.get(position);

        // # setting data to views
        if (planItem != null)
        {
            holder.txtTitle.setText(planItem.getTitle());


            if (isDeleteModeEnabled)
            {
                holder.layoutImage.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.VISIBLE);

                mSelectedItemsBoolean.append(holder.getAdapterPosition(), false);
                if (isDeleteModeEnabled)
                {
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setChecked(false);
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mSelectedItemsBoolean.get(holder.getAdapterPosition()))
                                holder.checkBox.setChecked(false);
                            else
                                holder.checkBox.setChecked(true);
                        }
                    });
                }
                else
                    holder.checkBox.setVisibility(View.GONE);
                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked)
                            mSelectedItemsBoolean.append(holder.getAdapterPosition(), true);
                        else
                            mSelectedItemsBoolean.append(holder.getAdapterPosition(), false);
                    }
                });
            }
            else
            {
                holder.checkBox.setVisibility(View.GONE);

                // # if item title is link then image or video will not be shown
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
                    holder.layoutImage.setVisibility(View.VISIBLE);

                    Glide.with(mContext)
                            .load(link)
                            .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                            .apply(new RequestOptions().error(R.drawable.img_default_black))
                            // .apply(new RequestOptions().centerCrop())
                            .apply(new RequestOptions().override(80, 80))
                            .into(holder.image);

                    if (PlanData.PlanItem.MEDIA_TYPE_VIDEO.equals(planItem.getMediaType()))
                        holder.imgVideoIcon.setVisibility(View.VISIBLE);
                    else
                        holder.imgVideoIcon.setVisibility(View.GONE);

                }

                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemClickListener != null)
                            mItemClickListener.onItemClickedListener(mPlanData, holder.getAdapterPosition());
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return mListPlanItem.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle;
        ImageView image, imgVideoIcon;
         View layout, layoutImage;
         CheckBox checkBox;
        public MyViewHolder(View view)
        {
            super(view);
            layout = view;
            layoutImage = view.findViewById(R.id.recycler_training_plan_layout_image);
            txtTitle = view.findViewById(R.id.recycler_training_plan_txt_title);
            image = view.findViewById(R.id.recycler_training_plan_image_or_video);
            imgVideoIcon = view.findViewById(R.id.recycler_training_plan_img_video_play_icon);
            checkBox = view.findViewById(R.id.recycler_training_plan_layout_checkbox);
        }
    }


    // this method returns plan item position in adapter
    private int getPlanItemPosition(String itemId)
    {
        int position = -1;
        if (mListPlanItem != null)
        {
            for (int i=0; i<mListPlanItem.size(); i++)
            {
                PlanData.PlanItem planItem = mListPlanItem.get(i);
                if (itemId.equals(planItem.getId()))
                {
                    position = i;
                    break;
                }
            }
        }

        return position;
    }


}
