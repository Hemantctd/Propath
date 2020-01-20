package org.ctdworld.propath.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.MealPlan;

import java.util.ArrayList;


public class AdapterMealPlanner extends RecyclerView.Adapter<AdapterMealPlanner.MyViewHolder> {

    private static final String TAG = AdapterMealPlanner.class.getSimpleName();
    private Context mContext;
    private OnAdapterMealPlanListener mAdapterListener;
    private ArrayList<MealPlan.Plan> mListMealPlan;


    public AdapterMealPlanner(Context context, OnAdapterMealPlanListener longClickedListener) {
        mListMealPlan = new ArrayList<>();
        this.mContext = context;
        this.mAdapterListener = longClickedListener;
    }

    public interface OnAdapterMealPlanListener {
        void onMealPlanClicked(MealPlan.Plan mealPlan);

        void onMealPlanOptionsMenuClicked(MealPlan.Plan mealPlan);
    }

    public void onMealPlanReceived(MealPlan.Plan mealPlan) {
        if (mListMealPlan != null && mealPlan != null) {

            // showing latest plan at top.
            mListMealPlan.add(getItemCount(), mealPlan);
            notifyItemInserted(getItemCount());
        }
    }

    public void onMealPlanCreated(MealPlan.Plan mealPlan) {
        if (mListMealPlan != null && mealPlan != null) {

            mListMealPlan.add(getItemCount(), mealPlan);
            notifyItemInserted(getItemCount());
        }
    }


    // to refresh list after deleting note. only self created note will be deleted
    public void onMealPlanDeleted(MealPlan.Plan mealPlan) {
        if (mListMealPlan != null && mListMealPlan.size() > 0) {
            int position = getPosition(mealPlan.getId());

            Log.i(TAG, "deleted plan position = " + position);
            mListMealPlan.remove(position);
            notifyItemRemoved(position);
        }

    }

    // to refresh list after deleting note. only self created note will be deleted
    public void onMealPlanEdited(MealPlan.Plan mealPlan) {
        Log.i(TAG, "updating edited training plan, planId = " + mealPlan.getId());
        int position = getPosition(mealPlan.getId());
        if (position < 0)
            return;

        mListMealPlan.set(position, mealPlan);
        notifyItemChanged(position, mealPlan);
    }

    // clearing all data from list
    public void clearMealPlanList() {
        if (mListMealPlan != null) {
            mListMealPlan.clear();
            notifyDataSetChanged();
        }
    }

    private int getPosition(String planId) {
        int position = -1;

        if (planId == null) {
            Log.e(TAG, "planId is null in getPosition, planId = " + planId);
            return position;
        }
        for (int i = 0; i < mListMealPlan.size(); i++) {
            MealPlan.Plan mealPlan = mListMealPlan.get(i);
            if (planId.equals(mealPlan.getId())) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_match_day_received_layout, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        view.setLayoutParams(layoutParams);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.txtTitle.setText(mListMealPlan.get(position).getTitle());
        holder.imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterListener != null)
                    mAdapterListener.onMealPlanOptionsMenuClicked(mListMealPlan.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mListMealPlan.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDescription;
        View layout;
        CheckBox checkBox;
        ImageView imgOptions;
        TextView txtDateTime;

        public MyViewHolder(View view) {
            super(view);
            ///layout = view.findViewById(R.id.layout_data);
            txtTitle = view.findViewById(R.id.txt_name);
            imgOptions = view.findViewById(R.id.img_options_menu);
           /* txtDescription = view.findViewById(R.id.recycler_training_plan_received_txt_description);
            checkBox = view.findViewById(R.id.recycler_training_plan_checkbox);

            txtDateTime = view.findViewById(R.id.recycler_training_plan_txt_date_time);
*/
        }
    }


}
