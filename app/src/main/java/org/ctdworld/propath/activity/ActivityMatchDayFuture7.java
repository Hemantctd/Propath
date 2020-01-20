package org.ctdworld.propath.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterContactAdd;
import org.ctdworld.propath.adapter.AdapterMatchDayContactAdd;
import org.ctdworld.propath.contract.ContractGetRegisteredUsers;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentFooter;
import org.ctdworld.propath.fragment.FragmentSearch;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterGetRegisteredUsers;

import java.util.ArrayList;
import java.util.List;

public class ActivityMatchDayFuture7 extends AppCompatActivity implements FragmentSearch.SearchListener{

    private final String TAG = ActivityMatchDayFuture7.class.getSimpleName();

    Context mContext;

    RecyclerView mRecycler;
    // EditText mEditSearchContacts;
    // ImageView mImgSearch;
    // ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshLayout;
    TextView mTxtNoConnection;
    View mLayoutForDetails;
    ImageView toolBarOptions;
    LinearLayoutManager mLayoutManager;
    AdapterMatchDayContactAdd mAdapter;


    Toolbar mToolbar;
    TextView mToolbarTitle;
    Button mBtnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_day_future7);
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
        toolBarOptions = findViewById(R.id.toolbar_img_options_menu);
        //   mImgSearch = findViewById(R.id.contact_search_img_search);
        //   mEditSearchContacts = findViewById(R.id.contact_search_edit_search);
        mRecycler = findViewById(R.id.contact_search_recycler_view);
        mLayoutForDetails = findViewById(R.id.contact_search_layout_for_details);
        //mProgressBar = findViewById(R.id.contact_search_progressbar);
        mTxtNoConnection = findViewById(R.id.contact_search_txt_no_connection);
        mRefreshLayout = findViewById(R.id.contact_search_refresh_layout);

        toolBarOptions.setVisibility(View.VISIBLE);
        toolBarOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<BottomSheetOption> bottomSheetOptions  = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_SAVE,"Save").build();


                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
                {

                    @Override
                    public void onOptionSelected(int option)
                    {
                        switch (option)
                        {
                            case BottomSheetOption.OPTION_SAVE:

                                break;


                        }
                    }
                });
                if (getFragmentManager() != null) {
                    options.show(getSupportFragmentManager(), "options");
                }
            }
        });

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
        mAdapter = new AdapterMatchDayContactAdd(ActivityMatchDayFuture7.this, null);
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
