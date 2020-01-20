package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationSelfReviewReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdapterProgressReportAdapter  extends RecyclerView.Adapter<AdapterProgressReportAdapter.MyViewHolder>
{
    private final String TAG = AdapterProgressReportAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<HashMap<String,String>> mProgressReportList;
    String athlete_id;
    HashMap<String,String> map = new HashMap<>();
    String review_id;

    public AdapterProgressReportAdapter(Context context,  ArrayList<HashMap<String,String>> mProgressReportList, String athlete_id )
    {
        this.mProgressReportList = mProgressReportList;
        this.mContext = context;
        this.athlete_id = athlete_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_progress_report_athlete_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {

        map = mProgressReportList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String datedata = map.get("Date");
        Date date1=null;
        try
        {
            date1 = simpleDateFormat.parse(datedata);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault());
        String dateInReqFormat = newformat.format(date1);
        holder.progress_list_date.setText(dateInReqFormat);

        for(int i = 0 ; i <= mProgressReportList.size(); i++)
        {
            holder.progress_list_name.setText("Self Review " + (position+1));
        }

        holder.progress_report_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                map = mProgressReportList.get(position);
                Intent seeProgressReportIntent = new Intent(mContext, ActivityEducationSelfReviewReport.class);
                seeProgressReportIntent.putExtra("athlete_id",athlete_id);
                seeProgressReportIntent.putExtra("Review_ID",map.get("Review_ID"));
                mContext.startActivity(seeProgressReportIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mProgressReportList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView progress_list_name,progress_list_date;
        LinearLayout progress_report_layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            progress_list_name = itemView.findViewById(R.id.progress_list_name);
            progress_list_date = itemView.findViewById(R.id.progress_list_date);
            progress_list_date.setVisibility(View.VISIBLE);
            progress_report_layout = itemView.findViewById(R.id.progress_report_layout);
        }

    }
}
