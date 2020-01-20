package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.List;


public class AdapterNoteLinkCreate extends RecyclerView.Adapter<AdapterNoteLinkCreate.MyViewHolder>
{
    private static final String TAG = AdapterNoteLinkCreate.class.getSimpleName();
    private Context mContext;
    private ArrayList<Notes.Link> mListLink;

    public AdapterNoteLinkCreate(Context context) {
        this.mContext = context;
        this.mListLink = new ArrayList<>();
    }


    // adding new link while creating new note or editing note
    public void addLink(Notes.Link link)
    {
        if (mListLink!= null && link != null)
        {
            mListLink.add(0, link);
            notifyItemInserted(0);
        }
        else
            Log.e(TAG,"mListLink is null or link is null in addLink() method");
    }


    // this method returns latest list of links.
    public List<Notes.Link> getLinkList()
    {
        // checking if any link url is empty, if link is empty it will be removed from mList
        List<Notes.Link> filteredList = new ArrayList<>();
        for (int i=0; i<mListLink.size(); i++)
        {
            Notes.Link link = mListLink.get(i);
            if (!link.getLinkUrl().isEmpty())
                filteredList.add(link);
        }

        return filteredList;  // returning filter Note.Link list

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_note_link_create,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
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



        // removing item from list when user clicks on remove icon
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mListLink.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        return mListLink.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtLink;
        ImageView imgRemove;
        public MyViewHolder(View view)
        {
            super(view);
            txtLink = view.findViewById(R.id.recycler_note_link_create_txt_link);
            imgRemove = view.findViewById(R.id.recycler_note_link_create_img_remove);
        }
    }

}
