package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationSelfReviewReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


// this adapter
public class AdapterEducationAthleteReviewList extends RecyclerView.Adapter<AdapterEducationAthleteReviewList.MyViewHolder>
{
    private final String TAG = AdapterEducationAthleteReviewList.class.getSimpleName();
    private Context mContext;
    private ArrayList<HashMap<String,String>> mProgressReportList;
    private String athlete_id;
    private HashMap<String,String> map = new HashMap<>();
    private String review_id;
 //   private boolean mIsDeleteModeEnabled = false;
 //   private SparseBooleanArray mSelectedItems;



    public AdapterEducationAthleteReviewList(Context context, ArrayList<HashMap<String,String>> mProgressReportList, String athlete_id )
    {
        this.mProgressReportList = mProgressReportList;
        this.mContext = context;
        this.athlete_id = athlete_id;
        //mSelectedItems = new SparseBooleanArray();
    }



  /*  public void enableDeleteMode()
    {
        mIsDeleteModeEnabled = true;
        notifyDataSetChanged();
    }

    public void disableDeleteMode()
    {
        mIsDeleteModeEnabled = false;
        notifyDataSetChanged();
    }


    public List<String> getSelectedItemsIdList()
    {
        List<String> listIds = new ArrayList<>();
        if (mSelectedItems != null)
        {
            for (int i=0; i<mSelectedItems.size(); i++)
            {
                if (mSelectedItems.get(i))
                    listIds.add(mProgressReportList.get(i).get("Review_ID"));
            }
        }

        return listIds;
    }
*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_education_self_reivew_list, parent, false);
        return new MyViewHolder(view);
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        map = mProgressReportList.get(position);

      /*  if (mIsDeleteModeEnabled)
            holder.checkBox.setVisibility(View.VISIBLE);
        else
            holder.checkBox.setVisibility(View.GONE);

        mSelectedItems.put(holder.getAdapterPosition(), false);*/


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


       /* holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSelectedItems.put(holder.getAdapterPosition(), isChecked);

                    Log.i(TAG,"selected items = "+getSelectedItemsIdList().size());
            }
        });
*/
    }


    @Override
    public int getItemCount() {
        return mProgressReportList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView progress_list_name,progress_list_date;
        View progress_report_layout;
      // CheckBox checkBox;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            progress_list_name = itemView.findViewById(R.id.progress_list_name);
            progress_list_date = itemView.findViewById(R.id.progress_list_date);
            progress_list_date.setVisibility(View.VISIBLE);
            progress_report_layout = itemView.findViewById(R.id.progress_report_layout);
          //  checkBox = itemView.findViewById(R.id.checkbox);
        }

    }
}
