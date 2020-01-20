package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Event;

import java.util.ArrayList;
import java.util.List;

public class AdapterEventsUpcoming extends RecyclerView.Adapter<AdapterEventsUpcoming.MyViewHolder>
{
    private static final String TAG = AdapterEventsUpcoming.class.getSimpleName();
    Context mContext;
    List<Event> mListEvents;


    public AdapterEventsUpcoming(Context mContext) {
     //   Log.i(TAG,"AdapterNewsFeed's constructor called ");
        this.mContext = mContext;
        this.mListEvents = new ArrayList<>();
    }

    public void updateDataList(List<Event> listEvents)
    {
        Log.i(TAG,"updateDataList size = "+mListEvents.size());
        this.mListEvents = listEvents;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
      //  Log.i(TAG,"onCreateViewHolder called ");

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.recycler_events_upcoming,null);
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
      //  Log.i(TAG,"onBindViewHolder called ");
        Event event = mListEvents.get(position);

        holder.txtTitle.setText(event.getTitle());
        holder.txtTime.setText(event.getTime());
        holder.txtLocation.setText(event.getLocationName());
        holder.txtDate.setText(event.getDate());
/*
        if (position == mListEvents.size())
            mListEvents.clear();*/

    }

    @Override
    public int getItemCount() {
       // Log.i(TAG,"getItemCount called ");

        if (mListEvents != null)
        return mListEvents.size();
        else
            Log.e(TAG,"mListNewsData is null");

        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle, txtTime, txtLocation, txtDate;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.adapter_upcoming_events_txt_title);
            txtTime = itemView.findViewById(R.id.adapter_upcoming_events_txt_time);
            txtLocation = itemView.findViewById(R.id.adapter_upcoming_events_txt_location);
            txtDate = itemView.findViewById(R.id.adapter_upcoming_events_date);

        }
    }
}
