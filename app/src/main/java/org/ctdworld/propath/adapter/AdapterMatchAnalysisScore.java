package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.ctdworld.propath.R;

public class AdapterMatchAnalysisScore extends RecyclerView.Adapter<AdapterMatchAnalysisScore.MyViewHolder>
{
    Context mContext;

    public AdapterMatchAnalysisScore(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_match_analysis_score, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.mScoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.mScoreName.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        });
        myViewHolder.mScoreStat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.mScoreStat1.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        });
        myViewHolder.mScoreStat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.mScoreStat2.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        });
        myViewHolder.mScoreStat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.mScoreStat3.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText mScoreName, mScoreStat1, mScoreStat2, mScoreStat3;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            mScoreName = itemView.findViewById(R.id.score_name);
            mScoreStat1 = itemView.findViewById(R.id.score_stat1);
            mScoreStat2 = itemView.findViewById(R.id.score_stat2);
            mScoreStat3 = itemView.findViewById(R.id.score_stat3);

        }


    }
}
