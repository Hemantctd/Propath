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
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.List;


public class AdapterNoteDocumentEdit extends RecyclerView.Adapter<AdapterNoteDocumentEdit.MyViewHolder>
{
    private static final String TAG = AdapterNoteDocumentEdit.class.getSimpleName();
    private Context mContext;
    private List<Notes.Document> mListDocument = new ArrayList<>();
    private ArrayList<Notes.Document> mListRemovedDocumentsCameFromServer;
    private int mType;


    public AdapterNoteDocumentEdit(Context context) {
        this.mContext = context;
      //  this.mType = typeCreateOrEdit;
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

    // adding Document list to show in list (came from other page)
    public void addDocumentList(List<Notes.Document> documentList)
    {
        Log.i(TAG,"document adding method called");
        if (documentList != null)
        {
            Log.i(TAG,"document adding");
            this.mListDocument = documentList;
            notifyDataSetChanged();
        }
    }


    // this method returns list of documents which have been added by user, documents which have not come from server
    public List<Notes.Document> getNewAddedDocumentList()
    {
        ArrayList<Notes.Document> filteredDocumentList = new ArrayList<>();
        for (Notes.Document document : mListDocument)
        {
            // adding only those Documents which are not from server. it will be saved as new documents in Note on server
            if (!document.isDocumentFromServer())
                filteredDocumentList.add(document);
        }
        return filteredDocumentList;
    }


    // this method returns List of Note.Document which have come from server and have been removed from list
    public List<Notes.Document> getRemovedDocumentListCameFromServer()
    {
        return mListRemovedDocumentsCameFromServer;
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
            holder.txtTitle.setText(FileHelper.getFileName(mContext, document.getDocumentUrl()));
        }
        else
            Log.e(TAG,"document is null in onBindViewHolder");



       // removing item from list when user clicks on remove icon
       holder.imgRemove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               // if document has come from server and it's  being removed then it will be added to mListRemovedDocumentsCameFromServer


               if (document != null && document.isDocumentFromServer())
                   mListRemovedDocumentsCameFromServer.add(document);

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
