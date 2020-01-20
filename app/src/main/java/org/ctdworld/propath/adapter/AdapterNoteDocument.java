package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityWebView;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.List;


public class AdapterNoteDocument extends RecyclerView.Adapter<AdapterNoteDocument.MyViewHolder>
{
    private static final String TAG = AdapterNoteDocument.class.getSimpleName();
    private Context mContext;
    private List<Notes.Document> mListDocument = new ArrayList<>();


    public AdapterNoteDocument(Context context) {
        this.mContext = context;
    }


    // adding new document
    public void addDocument(Notes.Document document)
    {
        if (mListDocument!= null && document != null)
        {
            mListDocument.add(document);
            notifyItemInserted(getItemCount()-1);
        }
    }


    // adding DocumentList
    public void addDocumentList(List<Notes.Document> documentList)
    {
        if (documentList != null)
        {
            this.mListDocument = documentList;
            notifyDataSetChanged();
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_note_document,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final Notes.Document document = mListDocument.get(position);

        // # setting data to views
        if (document != null)
        {
            holder.txtTitle.setText(FileHelper.getFileName(mContext,document.getDocumentUrl()));
            holder.txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Patterns.WEB_URL.matcher(document.getDocumentUrl()).matches())
                    {
                        Intent intent = new Intent(mContext, ActivityWebView.class);

                        intent.putExtra(ActivityWebView.KEY_WEB_URL, ActivityWebView.DRIVE_LINK+document.getDocumentUrl());
                        intent.putExtra(ActivityWebView.KEY_TOOLBAR,"Note");
                        mContext.startActivity(intent);
                    }
                }
            });
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
    }


    @Override
    public int getItemCount() {
        return mListDocument.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle;
        public MyViewHolder(View view)
        {
            super(view);
            txtTitle = view.findViewById(R.id.recycler_note_document_txt_title);
        }
    }

}
