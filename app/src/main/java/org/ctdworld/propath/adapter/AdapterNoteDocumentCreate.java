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


public class AdapterNoteDocumentCreate extends RecyclerView.Adapter<AdapterNoteDocumentCreate.MyViewHolder>
{
    private static final String TAG = AdapterNoteDocumentCreate.class.getSimpleName();
    private Context mContext;
    private ArrayList<Notes.Document> mListDocument = new ArrayList<>();
    private ArrayList<Notes.Document> mListRemovedDocumentsCameFromServer;


    public AdapterNoteDocumentCreate(Context context) {
        this.mContext = context;
        this.mListRemovedDocumentsCameFromServer = new ArrayList<>();
    }


    // adding Document came from server or added from device(to save on server).
    public void addDocument(Notes.Document document)
    {
        if (mListDocument!= null && document != null)
        {
            mListDocument.add(document);
            notifyItemInserted(getItemCount()-1);
        }
    }


    // this method returns list of documents
    public List<Notes.Document> getDocumentList()
    {
       return mListDocument;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_note_document_create_edit,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final Notes.Document document = mListDocument.get(position);

        // # setting data to views
        if (document != null)
        {
            holder.txtTitle.setText(document.getDocumentTitle());
        }
        else
            Log.e(TAG,"document is null in onBindViewHolder");

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
               // if document has come from server and it's  being removed then it will be added to mListRemovedDocumentsCameFromServer
               mListDocument.remove(holder.getAdapterPosition());
               notifyItemRemoved(holder.getAdapterPosition());
           }
       });

    }


    @Override
    public int getItemCount() {
        return mListDocument.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle;
        ImageView imgRemove;
        public MyViewHolder(View view)
        {
            super(view);
            txtTitle = view.findViewById(R.id.recycler_note_document_create_edit_txt_title);
            imgRemove = view.findViewById(R.id.recycler_note_document_create_edit_img_remove);
        }
    }

}
