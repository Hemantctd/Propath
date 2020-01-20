package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;


import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNoteView;
import org.ctdworld.propath.fragment.FragmentNotesOptionsMenu;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;

public class AdapterNoteAllOfCategory extends RecyclerView.Adapter<AdapterNoteAllOfCategory.MyViewHolder> {
    private static final String TAG = AdapterNoteAllOfCategory.class.getSimpleName();
    private Context mContext;
    private ArrayList<Notes> mListNotes = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private FragmentNotesOptionsMenu.OnNotesOptionsListener mNotesOptionsListener;


    public AdapterNoteAllOfCategory(Context context, FragmentManager fragmentManager, FragmentNotesOptionsMenu.OnNotesOptionsListener listener) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mNotesOptionsListener = listener;
    }


    // to add new note
    public void addNotes(Notes note)
    {
        if (mListNotes != null && note != null)
        {
            mListNotes.add(note);
            notifyItemInserted(mListNotes.size()-1);
        }
    }


    // when note is deleted
    public void onNoteDeleted(int deletedItemPosition)
    {
        if (mListNotes != null)
        {
            mListNotes.remove(deletedItemPosition);
            notifyItemRemoved(deletedItemPosition);
        }
    }


    // to clear all notes from list
    public void clearList()
    {
        if (mListNotes != null)
        {
            mListNotes.clear();
            notifyDataSetChanged();
        }

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_notes_all_notes_of_category,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        final Notes note = mListNotes.get(position);

        // # setting data to views
        if (note != null)
        {
            holder.txtTitle.setText(note.getTitle());
           // holder.txtCategory.setText(note.getCategoryName());
            holder.txtDescription.setText(note.getDescription());
        }


        //# setting onClickListener on options menu icon to show dialog to choose option
        onOptionsMenuItemClicked(holder, note);


        holder.layoutClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Notes notes = mListNotes.get(holder.getAdapterPosition());
                Intent intent = new Intent(mContext, ActivityNoteView.class);
                intent.putExtra(ConstHelper.Key.DATA_MODAL, notes);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mListNotes.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle, txtCategory, txtDescription;
        ImageView imgOptions;
        View layoutClick;
        public MyViewHolder(View view)
        {
            super(view);
            layoutClick = view.findViewById(R.id.recycler_notes_all_notes_of_category_layout_click);
            txtTitle = view.findViewById(R.id.recycler_notes_all_notes_of_category_txt_tile);
            imgOptions = view.findViewById(R.id.recycler_notes_all_notes_of_category_img_options);
            txtCategory = view.findViewById(R.id.recycler_notes_all_notes_of_category_txt_category);
            txtDescription = view.findViewById(R.id.recycler_notes_all_notes_of_category_txt_description);
        }
    }



    //# setting onClickListener on options menu icon to show dialog to choose option
    private void onOptionsMenuItemClicked(final MyViewHolder holder, final Notes note)
    {
        holder.imgOptions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"showing options menu fragment, mNotesOptionsListener = "+mNotesOptionsListener);
                FragmentNotesOptionsMenu fragmentNotesOptionsMenu = FragmentNotesOptionsMenu.newInstance(note, holder.getAdapterPosition());
                fragmentNotesOptionsMenu.setOnNotesOptionListener(mNotesOptionsListener);
                fragmentNotesOptionsMenu.show(mFragmentManager, ConstHelper.Tag.Fragment.NOTES_CATEGORY);
            }
        });
    }


}
