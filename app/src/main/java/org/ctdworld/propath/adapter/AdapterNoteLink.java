package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.List;


public class AdapterNoteLink extends RecyclerView.Adapter<AdapterNoteLink.MyViewHolder>
{
    private static final String TAG = AdapterNoteLink.class.getSimpleName();
    private Context mContext;
    private List<Notes.Link> mListLink = new ArrayList<>();


    public AdapterNoteLink(Context context) {
        this.mContext = context;
    }


    // adding new link
    public void addLink(Notes.Link link)
    {
        if (mListLink!= null && link != null)
        {
            mListLink.add(0, link);
            notifyItemInserted(0);
        }
    }


    // adding Link list
    public void addLinkList(List<Notes.Link> linkList)
    {
        if (linkList != null)
        {
            this.mListLink = linkList;
            notifyDataSetChanged();
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_note_link,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final Notes.Link link = mListLink.get(position);

        // # setting data to views
        if (link != null)
        {
            holder.txtLink.setText(link.getLinkUrl());
        }
        else
            Log.e(TAG,"link is null in onBindViewHolder");

       /* holder.layoutClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ActivityNoteView.class);
                intent.putExtra(ActivityNoteView.KEY_NOTES_DATA, notes);
                mContext.startActivity(intent);
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return mListLink.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtLink;
        public MyViewHolder(View view)
        {
            super(view);
            txtLink = view.findViewById(R.id.recycler_note_link_txt_link);
        }
    }

}
