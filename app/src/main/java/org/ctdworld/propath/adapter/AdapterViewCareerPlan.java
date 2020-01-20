package org.ctdworld.propath.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.R;

import java.util.ArrayList;

public class AdapterViewCareerPlan extends RecyclerView.Adapter<AdapterViewCareerPlan.MyViewHolder> {

    Context context;
    ArrayList<String> arrayList;
    ArrayList<String> careerDate;

    public AdapterViewCareerPlan(Context context, ArrayList<String> arrayList, ArrayList<String> careerDate) {
        this.context = context;
        this.arrayList=arrayList;
        this.careerDate=careerDate;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_career_plan,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.careerText.setText(arrayList.get(position));
        holder.careerDate.setText(careerDate.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView careerText;
        public TextView careerDate;
        public MyViewHolder(View view)
        {

            super(view);
            careerText = (TextView) view.findViewById(R.id.careerPlan);
            careerDate=(TextView)view.findViewById(R.id.date);

        }
    }

}
