package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Notes;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.ArrayList;

public class AdapterNoteAll extends RecyclerView.Adapter<AdapterNoteAll.MyViewHolder> {
    private Context mContext;
    private ArrayList<Notes> mListNotes = new ArrayList<>();
    private Listener mListener;

    public AdapterNoteAll(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    // # Listener
    public interface Listener {
        void onNoteClicked(Notes note, int position);
        void onOptionMenuClicked(Notes note);}


    // to add new note
    public void addNoteList(Notes note)
    {

        if (mListNotes != null && note != null) {
            mListNotes.add(0, note);
            notifyItemInserted(0);
        }
       /* if (note != null && note.getNoteList() != null)
        {
            Collections.reverse(note.getNoteList());
            mListNotes.addAll(note.getNoteList());
            notifyDataSetChanged();
        }*/
    }



    public void onNoteDeleted(Notes note)
    {
        if (note == null)
            return;

        int position = getAdapterPosition(note.getNoteId());
        if (position == ConstHelper.NOT_FOUND)
            return;

        mListNotes.remove(position);
        notifyItemRemoved(position);
    }


    // # when note is edited
    public void onNoteUpdated(Notes note)
    {
        if (note == null)
            return;

        int position = getAdapterPosition(note.getNoteId());
        if (position == ConstHelper.NOT_FOUND)
            return;

        mListNotes.set(position, note);
        notifyItemChanged(position, note);
        notifyDataSetChanged();
    }



    // clearing all data from list
    public void clearNotesList()
    {
        if (mListNotes != null)
        {
            mListNotes.clear();
            notifyDataSetChanged();
        }
    }


    public void filterList(ArrayList<Notes> filteredList)
    {
        mListNotes = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_notes_all,null);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        view.setAnimation(animation);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        final Notes note = mListNotes.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        // # setting data to views
        holder.txtTitle.setText(note.getTitle());
        if (note.getCategoryName() != null)

        holder.txtCategory.setText(note.getCategoryName());
        holder.txtDescription.setText(note.getDescription());
        setDateTime(holder.txtDateTime,note);   // setting date time
        setOnRowClickedListener(holder); // setting listener on row
        setLeftImageCreatedOrShared(holder);  // setting image to know who has created note
        setOnOptionMenuClickListener(holder);  // setting click listener

    }

    private void setLeftImageCreatedOrShared(MyViewHolder holder) {
        Notes note = mListNotes.get(holder.getAdapterPosition());
        if (note.getCreatedByUserId().equals(SessionHelper.getUserId(mContext)))
            holder.imgLeftMost.setImageResource(R.drawable.ic_notes_add);   // if note is created by logged in user
        else
            holder.imgLeftMost.setImageResource(R.drawable.ic_notes_shared);  // if note is shared by other user

    }


    @Override
    public int getItemCount() {
        return mListNotes.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgLeftMost;
        ImageView imgOptionMenu;
        TextView txtTitle, txtCategory, txtDescription, txtDateTime;
        View layoutClick, layout;
        public MyViewHolder(View view)
        {
            super(view);
            layout = view;
            layoutClick = view.findViewById(R.id.recycler_notes_all_layout_click);
            txtTitle = view.findViewById(R.id.recycler_notes_all_txt_tile);
            txtCategory = view.findViewById(R.id.recycler_notes_all_txt_category);
            txtDescription = view.findViewById(R.id.recycler_notes_all_txt_description);
            txtDateTime = view.findViewById(R.id.txt_date_time);
            imgLeftMost = view.findViewById(R.id.recycler_notes_all_img_left_most);
            imgOptionMenu = view.findViewById(R.id.img_options_menu);
        }
    }


    private void setDateTime(TextView txtDateTime, Notes note) {
        if (note == null)
            return;

        String createDate = DateTimeHelper.getDateTime(note.getCreatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME);
        String updateDate = DateTimeHelper.getDateTime(note.getUpdatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME);

        try {
            if (UtilHelper.isFirstDateGreater(updateDate, createDate))
                txtDateTime.setText(updateDate);
            else
                txtDateTime.setText(createDate);
        } catch (Exception e) {
            txtDateTime.setText(createDate);
            e.printStackTrace();
        }
    }


    // setting listener on row
    private void setOnRowClickedListener(final MyViewHolder holder)
    {
        holder.layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes note = mListNotes.get(holder.getAdapterPosition());
                if (mListener != null)
                    mListener.onNoteClicked(note, holder.getAdapterPosition());
            }
        });
    }


    // setting listener on options menu image
    private void setOnOptionMenuClickListener(final MyViewHolder holder)
    {
        holder.imgOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes note = mListNotes.get(holder.getAdapterPosition());
                if (mListener != null)
                    mListener.onOptionMenuClicked(note);
            }
        });
    }


    // returns adapter position depending on CareerUser id
    private int getAdapterPosition(String notdId)
    {
        if (notdId == null)
            return ConstHelper.NOT_FOUND;

        for (int i=0; i<mListNotes.size(); i++)
        {
            Notes note = mListNotes.get(i);
            if (note != null && note.getNoteId().equals(notdId))
                return i;
        }
        return ConstHelper.NOT_FOUND;
    }

}
