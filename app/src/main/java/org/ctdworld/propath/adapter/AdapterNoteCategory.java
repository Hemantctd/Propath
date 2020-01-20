package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNoteCategoryNotesList;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentNotesCategory;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.List;

public class AdapterNoteCategory extends RecyclerView.Adapter<AdapterNoteCategory.MyViewHolder> {



    private static final String TAG = AdapterNoteCategory.class.getSimpleName();
    private Context mContext;
    private List<Notes> mListCategory;
    private AdapterNoteCategoryListener mAdapterListener;



    // #listener to send data back if any page has requested to get selected category
    private FragmentNotesCategory.OnNotesCategorySelectedListener mListenerSelectedCategory =  null;

    // # adapter listener
    public interface AdapterNoteCategoryListener { void onBottomSheetOptionSelected(int option, Notes note, int position);}

    public AdapterNoteCategory(Context context, ArrayList<Notes> categoryList, AdapterNoteCategoryListener listener) {
        this.mContext = context;
        this.mListCategory = categoryList;
        this.mAdapterListener = listener;
    }

    public void setOnNotesCategorySelectedListener(FragmentNotesCategory.OnNotesCategorySelectedListener listener)
    {
        this.mListenerSelectedCategory = listener;
    }


    public void addCategory(List<Notes> categoryList)
    {
        if (categoryList != null)
        {
            mListCategory = categoryList;
            notifyDataSetChanged();
        }
    }


    public void addCategory(Notes note)
    {
        if (mListCategory != null)
        {
            mListCategory.add(note);
            notifyItemInserted(mListCategory.size()-1);

        }
    }


    // this method is called when category is deleted to refresh list
    public void onCategoryDeleted(int position)
    {
        if (mListCategory != null && position >=0)
        {
            mListCategory.remove(position);
            notifyItemRemoved(position);
        }
        else
            Log.e(TAG,"mListCategory is null or position is < 0");
    }


    // clearing category list
    public void clearCategoryList()
    {
       if (mListCategory != null)
       {
           mListCategory.clear();
           notifyDataSetChanged();
       }
    }


    public void filterList(ArrayList<Notes> filteredList)
    {
        mListCategory = filteredList;
        notifyDataSetChanged();
    }

    public List<Notes> getNotesCategoryList()
    {
        return mListCategory;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_notes_category_list,null);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {

        final Notes note = mListCategory.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        holder.txtTitle.setText(note.getCategoryName());
        holder.txtDateTime.setText(DateTimeHelper.getDateTime(note.getCreatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME));

        //setting onClickListener on category layout,
        holder.notesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.i(TAG,"category clicked");
                if (mListenerSelectedCategory != null)
                {
                    Log.i(TAG,"category selected");
                    mListenerSelectedCategory.onCategorySelected(note);
                    mListenerSelectedCategory = null; // making null so that category is not
                    return;
                }

                // if mListenerSelectedCategory is null then starting ActivityNoteCategoryNotesList to show list of note of category
                Intent intent = new Intent(mContext, ActivityNoteCategoryNotesList.class);
                intent.putExtra(ActivityNoteCategoryNotesList.KEY_NOTES_DATA, note);
                mContext.startActivity(intent);
            }
        });

        holder.imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_EDIT, "Edit")
                        .addOption(BottomSheetOption.OPTION_SHARE, "Share")
                        .addOption(BottomSheetOption.OPTION_DELETE, "Delete ");

                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option) {
                        if (mAdapterListener != null)
                            mAdapterListener.onBottomSheetOptionSelected(option, note, holder.getAdapterPosition());
                    }
                });

                try {
                    options.show(((AppCompatActivity)mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
                }catch (ClassCastException ignored){}
            }
        });

        /*holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view)
            {
                if (mAdapterListener != null)
                    mAdapterListener.onCategoryItemLongPressed(note, holder.getAdapterPosition());

                return true;
            }
        });*/
    }

    @Override
    public int getItemCount()
    {
        return mListCategory.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle, txtDateTime;
        ImageView imgOptions;
        View notesLayout, layout;
        MyViewHolder(View view)
        {
            super(view);
            layout = view;
            txtTitle = view.findViewById(R.id.categoryText);
            txtDateTime = view.findViewById(R.id.txt_date_time);
            notesLayout = view.findViewById(R.id.notesPage);
            imgOptions = view.findViewById(R.id.ic_options_menu);
        }
    }


}
