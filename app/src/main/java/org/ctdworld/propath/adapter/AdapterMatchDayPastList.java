package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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


// this adapter
public class AdapterMatchDayPastList extends RecyclerView.Adapter<AdapterMatchDayPastList.MyViewHolder>
{
    private final String TAG = AdapterMatchDayPastList.class.getSimpleName();
    private Context mContext;


    private OnOptionsMenuClickedListener mListener;


    public AdapterMatchDayPastList(Context context, OnOptionsMenuClickedListener listener)
    {

        this.mContext = context;
        mListener = listener;
    }

    public interface  OnOptionsMenuClickedListener{void onAdapterOptionsMenuClicked();}






    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_match_day_received_layout, parent, false);
        return new MyViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        holder.mName.setText("Event Name");
        holder.mImgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onAdapterOptionsMenuClicked();
            }
        });



    }


    @Override
    public int getItemCount() {
        return 5;
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
            mImgOptionsMenu = itemView.findViewById(R.id.img_options_menu);
            mName = itemView.findViewById(R.id.txt_name);


        }

    }



}
