package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentNotesCategory;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.Notes;


//# this activity is being used to select category and return to calling component
public class ActivityNoteSelectCategory extends AppCompatActivity implements FragmentNotesCategory.OnNotesCategorySelectedListener
{
    private final String TAG = ActivityNoteSelectCategory.class.getSimpleName();

    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    View mToolbarProfileLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_notes_select_category);
            init();
            setToolbar();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_notes_select_category_container_fragment, new FragmentNotesCategory()
                    ,ConstHelper.Tag.Fragment.NOTES_CATEGORY).commit();



           /* // # setting onClickListener to create new category
            mTxtAddNewCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    FragmentNotesCategory fragmentNotesCategory = (FragmentNotesCategory) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.NOTES_CATEGORY);
                    if (fragmentNotesCategory != null)
                    {
                        fragmentNotesCategory.onCreateCategory();
                    }
                    else
                        Log.e(TAG,"fragmentNotesCategory is null");
                }
            });*/



        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in onCreate Activity , "+e.getMessage());
            e.printStackTrace();
        }


    }

    private void init() {
        mContext = this;

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbarProfileLayout = findViewById(R.id.toolbar_layout_profile);
       // mTxtAddNewCategory = findViewById(R.id.activity_notes_select_category_txt_add_new_category);

    }



    /*private void setAdapterForCategory()
    {
        ArrayList<Note> categoryList = new ArrayList<>();
        AdapterNoteCategory adapter = new AdapterNoteCategory(this,new ArrayList<Note>());

        for (int i=0; i<5; i++)
        {
            Note notes = new Note();
            notes.setCategoryName("Category "+(i+1));
            adapter.addCategory(notes);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setLayoutManager(layoutManager);
        notesRecyclerView.setAdapter(adapter);

        adapter.setOnNotesCategorySelectedListener(new AdapterNoteCategory.OnNotesCategorySelectedListener() {
            @Override
            public void onCategorySelected(Note notes) {

            }
        });
    }*/



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }


    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTxtTitle.setText(getString(R.string.select_category));
    }



    // receives note category
    @Override
    public void onCategorySelected(Notes note) {

        Log.i(TAG,"onCategorySelected() method called");
        if (note == null)
            return;
        Log.i(TAG,"category name = "+ note.getCategoryName());
        Intent intent = new Intent();
        // Log.i(TAG, "Setting result");
        intent.putExtra(ConstHelper.Key.SELECT, note);
        setResult(RESULT_OK,intent);

        finish();
    }





}
