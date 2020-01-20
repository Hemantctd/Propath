package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityMatchPersonalStatistics;

import java.util.ArrayList;

public class AdapterMatchReceivedStatistics extends RecyclerView.Adapter<AdapterMatchReceivedStatistics.MyViewHolder>
{
    Context mContext;
    ArrayList<String> arrayList = new ArrayList<>();

    public AdapterMatchReceivedStatistics(Context context, ArrayList<String> arrayList)
    {
       this.mContext = context;
       this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_match_analysis_received_statistics, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.mTextView.setText(arrayList.get(i));
        myViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               mContext.startActivity(new Intent(mContext, ActivityMatchPersonalStatistics.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mTextView;
        private LinearLayout mLinearLayout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            mTextView = itemView.findViewById(R.id.team_list_text);
            mLinearLayout = itemView.findViewById(R.id.layout_received_statistics);
        }


    }
}
