package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AdapterNoteLinkEdit extends RecyclerView.Adapter<AdapterNoteLinkEdit.MyViewHolder>
{
    private static final String TAG = AdapterNoteLinkEdit.class.getSimpleName();
    private Context mContext;
    private List<Notes.Link> mListLink;
    private ArrayList<Notes.Link> mListRemovedLinksCameFromServer;
    private HashMap<String, Notes.Link> mEditedServerLinksIdList; // to contain IDs of links which have been edited, edited links will be fetched with this

    public AdapterNoteLinkEdit(Context context) {
        this.mContext = context;
        this.mListLink = new ArrayList<>();
        this.mListRemovedLinksCameFromServer = new ArrayList<>();
        this.mEditedServerLinksIdList = new HashMap<>();
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

    // adding link list
    public void addLinkList(List<Notes.Link> linkList)
    {
        Log.i(TAG,"link adding in method");
        if (linkList != null)
        {
            Log.i(TAG,"link adding");
            this.mListLink = linkList;
            notifyDataSetChanged();
        }
        else
            Log.e(TAG,"mListLink is null or link is null in addLink() method");
    }


    // this method returns list of links which have been edited.
    public List<Notes.Link> getEditedLinkList()
    {
        return new ArrayList<>(mEditedServerLinksIdList.values());
    }


    // this method returns list of Link which have been added by user. Links which have not come from server
    public List<Notes.Link> getNewAddedLinkList()
    {
        ArrayList<Notes.Link> filteredList = new ArrayList<>();
        for (Notes.Link link : mListLink)
        {
            // adding only those Links which are not from server. it will be saved as new Link in Note on server
            if (!link.isLinkFromServer())
                filteredList.add(link);
        }
        return filteredList;
    }


    // this method returns List of Note.Link which have come from server and have been removed from list
    public List<Notes.Link> getRemovedDocumentListCameFromServer()
    {
        return mListRemovedLinksCameFromServer;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_note_link_edit,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final Notes.Link link = mListLink.get(holder.getAdapterPosition());

        // # setting data to views
        if (link != null)
        {
            Log.i(TAG,"media url = "+link.getLinkUrl());
            holder.editLink.setText(link.getLinkUrl());
                handleLinkForEditing(holder);
        }
        else
            Log.e(TAG,"link is null in onBindViewHolder");



        // removing item from list when user clicks on remove icon
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // if link has come from server and it's  being removed then it will be added to mListRemovedDocumentsCameFromServer
                if (link != null && link.isLinkFromServer())
                    mListRemovedLinksCameFromServer.add(link);

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
        TextView editLink;
        ImageView imgRemove;
        public MyViewHolder(View view)
        {
            super(view);
            editLink = view.findViewById(R.id.recycler_note_link_create_edit_edit_link);
            imgRemove = view.findViewById(R.id.recycler_note_link_create_edit_img_remove);

        }
    }



    // this method is called if note is being edited. it edits link url and update in list
    private void handleLinkForEditing(final MyViewHolder holder)
    {

        // setting listener to update link url in list when user edits link
        holder.editLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try
                {
                    int adapterPosition = holder.getAdapterPosition();
                    Notes.Link link = mListLink.get(adapterPosition);

                    link.setLinkUrl(charSequence.toString().trim());  // adding edited link url
                    mListLink.set(adapterPosition, link);

                    if (link.isLinkFromServer())  // if link has come from server, only these links will be saved as edited link
                             mEditedServerLinksIdList.put(link.getLinkId(), link); // # if link is already present then existing link will be replaced with new link
                 //   else
                   //     Log.e(TAG,"link is not from server, data = "+charSequence.toString()+", position = "+adapterPosition+" , link id = "+link.getLinkId());
                }
                catch (Exception e)
                {
                    Log.e(TAG,"exception while editing link , "+e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

}
