package org.ctdworld.propath.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNutritionFeed;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNutritionFeed;
import org.ctdworld.propath.fragment.FragmentSearch;
import org.ctdworld.propath.helper.ActivityHelper;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.model.NutritionFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNutritionFeed;

import java.util.ArrayList;
import java.util.List;

public class ActivityNutritionFeed extends BaseActivity implements ContractNutritionFeed.View, FragmentSearch.SearchListener {
    // # Constants
    private static final String TAG = ActivityNutritionFeed.class.getSimpleName();  // tag used in log

    // # views
    private RecyclerView recyclerView; // to show post list
    private Toolbar mToolbar;  // toolbar
    private TextView mToolbarTitle; // toolbar tile
    private FloatingActionButton mFloatButton; // floating button to create new post


    // # other variables
    private Context mContext;  // context
    private AdapterNutritionFeed mAdapter; // adapter to show post
    private List<NutritionFeed.PostData> mListPostData = new ArrayList<>(); // contains post list came from server, used to filter list
    private ContractNutritionFeed.Presenter mPresenterNutritionFeed; // presenter to make server request


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_feed);


        init();  // initializing
        setToolbar();  // setting toolbar data
        setUpAdapter(); // # setting up adapter

        // adding fragment to filter
        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();


        requestPostDataList();  // loading nutrition feed post list
        mFloatButton.setOnClickListener(onFloatButtonClicked);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    // mAdapter.onScrollingStopped();
                    Log.i(TAG,"scrolling stopped = "+newState);
                }
            }
        });

    }




    // # initializing
    private void init() {
        mContext = this;
        mPresenterNutritionFeed = new PresenterNutritionFeed(mContext, this);
        recyclerView = findViewById(R.id.recycler_news_feed);
        mFloatButton = findViewById(R.id.news_feed_floatingActionButton);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
    }


    // # setting up adapter post list
    private void setUpAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
       // mAdapter = new AdapterNutritionFeed(mContext, mPresenterNutritionFeed);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }


    // # click listener to create new post
    private View.OnClickListener onFloatButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(new Intent(ActivityNutritionFeed.this, ActivityNutritionFeedPost.class));
            i.putExtra(ActivityNutritionFeedPost.KEY_EDIT_OR_CREATE, ActivityNutritionFeedPost.ACTION_CREATE_POST);
            startActivityForResult(i, ConstHelper.RequestCode.CREATE_EDIT_NEWS_FEED_POST);
        }
    };

    // getting news feed data list
    private void requestPostDataList() {
        Log.i(TAG, "Loading news feed data list.......");
        if (mPresenterNutritionFeed != null) {
            showLoader(getString(R.string.message_loading));
            mPresenterNutritionFeed.requestPostDataList(SessionHelper.getUserId(mContext));
        }
    }


    // # setting up toolbar
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        //mImgToolbarOptionsMenu.setImageResource(R.drawable.ic_search);
        mToolbarTitle.setText(R.string.nutrition);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ConstHelper.RequestCode.CREATE_EDIT_NEWS_FEED_POST)
                requestPostDataList();

                // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_COMMENT && data != null && data.getExtras() != null) {
                NutritionFeed.PostData postData = (NutritionFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NEWS_FEED_POST_DATA);
                if (mAdapter != null && postData != null)
                    mAdapter.onPostUpdated(postData);
            }


            // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_SHARE && data != null && data.getExtras() != null) {
                NutritionFeed.PostData postData = (NutritionFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NEWS_FEED_POST_DATA);
                if (mAdapter != null && postData != null)
                    mAdapter.addPost(postData);
            }


        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null && item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // prepareRecyclerView();
        ActivityHelper.getInstance().setNewsFeedVisible(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ActivityHelper.getInstance().setNewsFeedVisible(false);
    }


    // # methods of presenter's View class starts
    @Override
    public void onPostCreated(NutritionFeed.PostData postData) {

    }

    @Override
    public void onPostEdited(NutritionFeed.PostData postData) {

    }

    @Override
    public void onPostListReceived(List<NutritionFeed.PostData> postDataList) {
        hideLoader();

        if (postDataList != null) {
            mListPostData = postDataList;
            if (mAdapter != null)
                mAdapter.addNutritionFeedList(postDataList);
        }

    }

    @Override
    public void onPostLikeUpdated(NutritionFeed.PostData postData) {
        hideLoader();
        mAdapter.onPostUpdated(postData);
    }

    @Override
    public void onPostCommentsReceived(List<NutritionFeed.PostData.PostComment> postCommentList) {

    }

    @Override
    public void onPostCommentAdded(NutritionFeed.PostData.PostComment postComment) {

    }

    @Override
    public void onPostCommentEdited(NutritionFeed.PostData.PostComment postComment) {

    }

    @Override
    public void onPostCommentDeleted(NutritionFeed.PostData.PostComment postComment) {

    }


    @Override
    public void onFailed(int failType, String message) {
        hideLoader();
        String title = getString(R.string.message_failed);
        if (failType == NewsFeed.FAILED_RECEIVING_POST_LIST)
            message = "Failed loading data...";

        if (message != null && !message.isEmpty())
            DialogHelper.showSimpleCustomDialog(mContext, title, message);
        else
            DialogHelper.showSimpleCustomDialog(mContext, title);
    }

    @Override
    public void onPostShared(NutritionFeed.PostData postData) {

    }


    @Override
    public void onSearchToFilter(final String searchedText) {
        // # filtering on new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<NutritionFeed.PostData> filteredList = new ArrayList<>();
                for (NutritionFeed.PostData postData : mListPostData) {
                    //if the existing elements contains the search input
                    String userName = postData.getPostByUserName();
                    if (userName != null)
                        if (userName.toLowerCase().contains(searchedText.toLowerCase()))
                            filteredList.add(postData);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.filterList(filteredList);
                    }
                });

            }
        }).start();

    }

}
