package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;

public class AdapterMatchAnalysisIntervalSummary extends RecyclerView.Adapter<AdapterMatchAnalysisIntervalSummary.MyViewHolder>
{
    Context mContext;

    public AdapterMatchAnalysisIntervalSummary(Context context)
    {
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_match_analysis_interval_summary, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount()
    {
        return 4;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        public MyViewHolder(View itemView)
        {
            super(itemView);

        }


    }
}
