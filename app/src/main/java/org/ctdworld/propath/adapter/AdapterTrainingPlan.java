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

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.model.TrainingPlan;

import java.util.ArrayList;

public class AdapterTrainingPlan extends RecyclerView.Adapter<AdapterTrainingPlan.MyViewHolder>
{
    private static final String TAG = AdapterTrainingPlan.class.getSimpleName();
    private Context mContext;
    private ArrayList<TrainingPlan.PlanData> mListTrainingPlanData = new ArrayList<>();
    private boolean isDeleteModeEnabled = false;
    private OnAdapterTrainingPlanListener mAdapterListener;
    private SparseBooleanArray mSelectedItemsBoolean;



    public AdapterTrainingPlan(Context context, OnAdapterTrainingPlanListener longClickedListener) {
        this.mContext = context;
        this.mAdapterListener = longClickedListener;
        this.mSelectedItemsBoolean = new SparseBooleanArray(getItemCount());
    }


    public interface OnAdapterTrainingPlanListener
    {
        void onTrainingPlanClicked(TrainingPlan.PlanData trainingPlanData);
        void onTrainingPlanOptionsMenuClicked(TrainingPlan.PlanData trainingPlanData);
    }


    // to add training plan received from server
    public void onTrainingPlanReceived(TrainingPlan.PlanData trainingPlanData)
    {
        if (mListTrainingPlanData != null && trainingPlanData != null)
        {
            mListTrainingPlanData.add(getItemCount(), trainingPlanData);
            notifyItemInserted(getItemCount());
        }
    }

    // to add newly created training plan, adding plan at position
    public void onTrainingPlanCreated(TrainingPlan.PlanData trainingPlanData)
    {
        if (mListTrainingPlanData != null && trainingPlanData != null)
        {
            // trainingPlanData.setId(String.valueOf(getItemCount()));
            mListTrainingPlanData.add(getItemCount(), trainingPlanData);
            notifyItemInserted(getItemCount());
        }
    }


    // to refresh list after deleting note. only self created note will be deleted
    public void onTrainingPlanDeleted(TrainingPlan.PlanData trainingPlanData)
    {
        if (mListTrainingPlanData != null && mListTrainingPlanData.size()>0)
        {
            int position = getPosition(trainingPlanData.getId());

            Log.i(TAG,"deleted plan position = "+position);
            mListTrainingPlanData.remove(position);
            notifyItemRemoved(position);
        }

    }

    // to refresh list after deleting note. only self created note will be deleted
    public void onTrainingPlanEdited(TrainingPlan.PlanData trainingPlanData)
    {
        Log.i(TAG,"updating edited training plan, planId = "+ trainingPlanData.getId());
        int position = getPosition(trainingPlanData.getId());
        if (position < 0)
            return;

        mListTrainingPlanData.set(position, trainingPlanData);
        notifyItemChanged(position, trainingPlanData);
    }

    // clearing all data from list
    public void clearTrainingPlanList()
    {
        if (mListTrainingPlanData != null)
        {
            mListTrainingPlanData.clear();
            notifyDataSetChanged();
        }
    }


    // to delete multiple items
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
    public ArrayList<TrainingPlan.PlanData> getSelectedTrainingPlans()
    {
        Log.i(TAG,"getSelectedTrainingPlans() method called , selected items boolean = "+mSelectedItemsBoolean.toString());
        ArrayList<TrainingPlan.PlanData> selectedPlanData = new ArrayList<>();
        if (mSelectedItemsBoolean != null)
        {
            for (int i=0; i<mSelectedItemsBoolean.size(); i++)
            {
                if (mSelectedItemsBoolean.get(i,false))  // selected item, if true
                    selectedPlanData.add(mListTrainingPlanData.get(i));
            }
        }

        return selectedPlanData;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_training_plan,parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        view.setLayoutParams(layoutParams);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        final TrainingPlan.PlanData trainingPlanData = mListTrainingPlanData.get(holder.getAdapterPosition());

        // # setting data to views
        if (trainingPlanData != null)
        {
            holder.txtTitle.setText(trainingPlanData.getTitle());
            setDescriptionView(holder.txtDescription, trainingPlanData.getDescription());
            holder.txtDateTime.setText(DateTimeHelper.getDateTime(trainingPlanData.getUpdatedDate(), DateTimeHelper.FORMAT_DATE_TIME));



            holder.layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mAdapterListener != null)
                        mAdapterListener.onTrainingPlanClicked(trainingPlanData);
                }
            });


            holder.imgOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterListener != null)
                        mAdapterListener.onTrainingPlanOptionsMenuClicked(trainingPlanData);
                }
            });


            // adding false to mSelectedItemsBoolean by default for all items
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
                    {
                        mSelectedItemsBoolean.append(holder.getAdapterPosition(), true);
                        // mListTrainingPlanData.get(holder.getAdapterPosition()).setPositionInAdapter(holder.getAdapterPosition());
                    }
                    else
                        mSelectedItemsBoolean.append(holder.getAdapterPosition(), false);

                }
            });

        }


    }


    @Override
    public int getItemCount() {
        return mListTrainingPlanData.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle, txtDescription;
        View layout;
        CheckBox checkBox;
        ImageView imgOptions;
        TextView txtDateTime;

        public MyViewHolder(View view)
        {
            super(view);
            layout = view.findViewById(R.id.layout_data);
            txtTitle = view.findViewById(R.id.recycler_training_plan_received_txt_title);
            txtDescription = view.findViewById(R.id.recycler_training_plan_received_txt_description);
            checkBox = view.findViewById(R.id.recycler_training_plan_checkbox);
            imgOptions = view.findViewById(R.id.img_options_menu);
            txtDateTime = view.findViewById(R.id.recycler_training_plan_txt_date_time);

        }
    }


    // returns position in list from training plan id
    private int getPosition(String planId)
    {
        int position = -1;

        if (planId == null)
        {
            Log.e(TAG,"planId is null in getPosition, planId = "+planId);
            return position;
        }
        for (int i = 0; i< mListTrainingPlanData.size(); i++)
        {
            TrainingPlan.PlanData trainingPlanData = mListTrainingPlanData.get(i);
            if (planId.equals(trainingPlanData.getId()))
            {
                position = i;
                break;
            }
        }

        return position;
    }


    private void setDescriptionView(TextView textView, String description)
    {
        if (description == null || description.isEmpty())
            textView.setVisibility(View.GONE);
        else
            textView.setText(description);
    }



}
