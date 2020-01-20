package org.ctdworld.propath.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMatchDayContact;
import org.ctdworld.propath.adapter.AdapterMatchDayContactAdd;
import org.ctdworld.propath.fragment.FragmentFooter;
import org.ctdworld.propath.fragment.FragmentSearch;
import org.ctdworld.propath.helper.ConstHelper;

public class ActivityMatchDayFuture8 extends AppCompatActivity implements FragmentSearch.SearchListener {

    private final String TAG = ActivityMatchDayFuture8.class.getSimpleName();

    Context mContext;

    RecyclerView mRecycler;
    // EditText mEditSearchContacts;
    // ImageView mImgSearch;
    // ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshLayout;
    TextView mTxtNoConnection;
    View mLayoutForDetails;
    LinearLayoutManager mLayoutManager;
    AdapterMatchDayContact mAdapter;


    Toolbar mToolbar;
    TextView mToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_day_future8);
        init();
        setToolbar();
        setContactsAdapter();
        setListeners();
        getSupportFragmentManager().beginTransaction().add(R.id.container_footer,new FragmentFooter()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_search_container,new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();




    }
    private void init() {
        mContext = this;

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);

        //   mImgSearch = findViewById(R.id.contact_search_img_search);
        //   mEditSearchContacts = findViewById(R.id.contact_search_edit_search);
        mRecycler = findViewById(R.id.contact_search_recycler_view);
        mLayoutForDetails = findViewById(R.id.contact_search_layout_for_details);
        //mProgressBar = findViewById(R.id.contact_search_progressbar);
        mTxtNoConnection = findViewById(R.id.contact_search_txt_no_connection);
        mRefreshLayout = findViewById(R.id.contact_search_refresh_layout);

    }



    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Match Day");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    private void setContactsAdapter() {
        mAdapter = new AdapterMatchDayContact(ActivityMatchDayFuture8.this, null);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);

    }

    private  void setListeners() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);

            }
        });

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // to go back on click on back button on toolbar
        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return true;
    }



    @Override
    public void onSearchToFilter(String searchedText) {

    }
}
