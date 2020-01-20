package org.ctdworld.propath.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNoteViewPager;


/*#this activity contains two tabs "all notes" and "category". it contains plus icon when user is in all notes tab and clicks plus
there would be a list to select category or create new category in new page, note will be created in selected category.
        And if user is in category and clicks on plus then new category will be created.*/

public class ActivityNoteContainer extends AppCompatActivity
{
    private static final String TAG = ActivityNoteContainer.class.getSimpleName();
    Context mContext;
    TabLayout tabNoteLayout;
    ViewPager viewPager;
    TabItem tabCategory, tabUnit;
    AdapterNoteViewPager adapterNotes;

    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_container);
        init();
        setToolbar();
        setViewPagerAdapter();
        setListeners();

    }

    // to initialize
    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
     //   mToolbarImgAdd = findViewById(R.id.toolbar_img_edit);
        tabNoteLayout = findViewById(R.id.tabNotelayout);
        tabCategory = findViewById(R.id.tabCategory);
        tabUnit = findViewById(R.id.tabUnitNotes);
        viewPager = findViewById(R.id.notesViewPager);
    }




    // setting viewpager adapter
    private void setViewPagerAdapter() {
        adapterNotes = new AdapterNoteViewPager(getSupportFragmentManager(), tabNoteLayout.getTabCount());
        viewPager.setAdapter(adapterNotes);

    }


    //# setting listeners
    private void setListeners()
    {
        tabNoteLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.i(TAG,"selected tab = "+tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabNoteLayout));
    }


    // setting toolbar data
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        //    mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        mToolbarTxtTitle.setText(getString(R.string.files_notes));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            super.onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
