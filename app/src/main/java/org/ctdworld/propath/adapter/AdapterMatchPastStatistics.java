package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.ctdworld.propath.R;

public class AdapterMatchPastStatistics extends RecyclerView.Adapter<AdapterMatchPastStatistics.MyViewHolder>
{
    Context mContext;

    public AdapterMatchPastStatistics(Context context)
    {
       this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_match_past_statistics, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.mLayoutMatchPastStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout mLayoutMatchPastStatistics;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            mLayoutMatchPastStatistics = itemView.findViewById(R.id.layout_match_past_statistics);

        }


    }
}
