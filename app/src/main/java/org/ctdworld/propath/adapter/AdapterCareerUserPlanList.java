package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCareerPlanView;
import org.ctdworld.propath.activity.ActivityCareerUserPlanList;
import org.ctdworld.propath.base.BaseViewHolder;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterCareerUserPlanList extends RecyclerView.Adapter<AdapterCareerUserPlanList.MyViewHolder>
{
    private Context mContext;
    private List<CareerPlan.CareerData> mCareerDataList;
    private CareerPlan.CareerUser mCareerUser;
    private Listener mListener;


    public interface Listener{void onAdapterOptionsMenuClicked(CareerPlan.CareerUser careerUser);}

    public AdapterCareerUserPlanList(Context context, CareerPlan.CareerUser careerUser, Listener adapterListener)
    {
        mContext = context;
        mCareerUser = careerUser;
        mCareerDataList = mCareerUser.getCareerDataList();
        mListener = adapterListener;
    }


    public void filterList(ArrayList<CareerPlan.CareerData> filteredList)
    {
        this.mCareerDataList = filteredList;
        notifyDataSetChanged();
    }


    public CareerPlan.CareerUser getCareerUser()
    {
        return mCareerUser;
    }


    // updating career data
    public void onDataUpdated(CareerPlan.CareerData careerData)
    {
        if (careerData == null)
            return;

        int position = getAdapterPosition(careerData.getCareerId());

        if (position == ConstHelper.NOT_FOUND)
            return;

        mCareerDataList.set(position, careerData);
        notifyItemChanged(position, careerData);
    }



    // deleting CareerData from list, careerUser contains list CareerData which have been deleted
    public void onDataDeleted(CareerPlan.CareerUser careerUser)
    {
        if (careerUser == null)
            return;

        // removing data
        for (CareerPlan.CareerData careerData : careerUser.getCareerDataList())
        {
            if (careerData != null && careerData.getCareerId() != null)
            {
                mCareerDataList.remove(careerData);
                notifyItemRemoved(getAdapterPosition(careerData.getCareerId()));
            }
        }

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_career_user_plan_list, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        final CareerPlan.CareerData careerData = mCareerDataList.get(position);

        if (careerData == null)
            return;


        String text = "Career PlanData "+(position+1);
        holder.txtTitle.setText(text);
        setPlanClickListener(holder);  // setting click listener on each row
        setOptionsMenu(holder, mCareerUser);

    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        if (mCareerDataList == null)
            return 0;

        return mCareerDataList.size();
    }



    // ViewHolder class
    public class MyViewHolder extends BaseViewHolder
    {
        TextView txtTitle;
        ImageView imgOptionsMenu;
        View layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            txtTitle = itemView.findViewById(R.id.txt_tile);
            imgOptionsMenu = itemView.findViewById(R.id.img_options_menu);
        }
    }




    // setting click listener on row
    private void setPlanClickListener(final MyViewHolder holder)
    {
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CareerPlan.CareerData careerData = mCareerDataList.get(holder.getAdapterPosition());
                mCareerUser.setCareerData(careerData);

                Intent intent = new Intent(mContext, ActivityCareerPlanView.class);
                intent.putExtra(ConstHelper.Key.CAREER_USER, mCareerUser);
                ((ActivityCareerUserPlanList)mContext).startActivityForResult(intent, ConstHelper.RequestCode.ACTIVITY_CAREER_PLAN_VIEW);
            }
        });
    }




    // setting options menu, if options menu is clicked Listener method will be called
    private void setOptionsMenu(final MyViewHolder holder, final CareerPlan.CareerUser careerUser)
    {
        if (careerUser.getUserId().equals(SessionHelper.getUserId(mContext)))
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);
        else
            holder.imgOptionsMenu.setVisibility(View.GONE);


        holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mListener != null)
                {
                    CareerPlan.CareerData careerData = mCareerDataList.get(holder.getAdapterPosition());
                    careerUser.setCareerData(careerData);
                    mListener.onAdapterOptionsMenuClicked(careerUser);
                }
            }
        });
    }





    // returns adapter position depending on CareerData id
    private int getAdapterPosition(String careerDataId)
    {
        if (careerDataId == null)
            return ConstHelper.NOT_FOUND;

        for (int i=0; i<mCareerDataList.size(); i++)
        {
            CareerPlan.CareerData careerData = mCareerDataList.get(i);
            if (careerData != null && careerData.getCareerId().equals(careerDataId))
                return i;
        }
        return ConstHelper.NOT_FOUND;
    }

}