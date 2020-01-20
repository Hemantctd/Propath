package org.ctdworld.propath.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNewsFeedPost;
import org.ctdworld.propath.adapter.AdapterNewFeed;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractNewsFeed;
import org.ctdworld.propath.helper.ActivityHelper;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNewsFeed;

import java.util.ArrayList;
import java.util.List;

public class FragmentNewsFeed extends BaseFragment implements ContractNewsFeed.View,
        FragmentSearch.SearchListener {
    // # Constants
    private static final String TAG = FragmentNewsFeed.class.getSimpleName();  // tag used in log

    // # views
    private RecyclerView recyclerView; // to show post list
    private Toolbar mToolbar;  // toolbar
    private TextView mToolbarTitle; // toolbar tile
    private FloatingActionButton mFloatButton; // floating button to create new post


    // # other variables
    private Context mContext;  // context
    private AdapterNewFeed mAdapter; // adapter to show post
    private List<NewsFeed.PostData> mListPostData = new ArrayList<>(); // contains post list came from server, used to filter list
    private ContractNewsFeed.Presenter mPresenterNewsFeed; // presenter to make server request


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_news_feed, container, false);

        init(view);  // initializing
       // setToolbar();  // setting toolbar data
        setUpAdapter(); // # setting up adapter

        // adding fragment to filter
        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();


        requestPostDataList();  // loading news feed post list
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

        return view;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_news_feed);

        init();  // initializing
        setToolbar();  // setting toolbar data
        setUpAdapter(); // # setting up adapter

        // adding fragment to filter
        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();


        requestPostDataList();  // loading news feed post list
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
    }*/


    // # initializing
    private void init(View view) {
        mContext = getContext();
        mPresenterNewsFeed = new PresenterNewsFeed(mContext, this);
        recyclerView = view.findViewById(R.id.recycler_news_feed);
        mFloatButton = view.findViewById(R.id.news_feed_floatingActionButton);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbarTitle = view.findViewById(R.id.toolbar_txt_title);
    }


    // # setting up adapter post list
    private void setUpAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterNewFeed(mContext, mPresenterNewsFeed);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }


    // # click listener to create new post
    private View.OnClickListener onFloatButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(new Intent(getContext(), ActivityNewsFeedPost.class));
            i.putExtra(ActivityNewsFeedPost.KEY_EDIT_OR_CREATE, ActivityNewsFeedPost.ACTION_CREATE_POST);
            startActivityForResult(i, ConstHelper.RequestCode.CREATE_EDIT_NEWS_FEED_POST);
        }
    };


    // getting news feed data list
    private void requestPostDataList() {
        Log.i(TAG, "Loading news feed data list.......");
        if (mPresenterNewsFeed != null) {
            showLoader(getString(R.string.message_loading));
            mPresenterNewsFeed.requestPostDataList(SessionHelper.getUserId(mContext));
        }
    }


    // # setting up toolbar
   /* private void setToolbar() {
        setSupportActionBar(mToolbar);
        //mImgToolbarOptionsMenu.setImageResource(R.drawable.ic_search);
        mToolbarTitle.setText(R.string.news_feed);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }*/


   // called from container activity
   public void onPostUpdated(NewsFeed.PostData postData){
       if (mAdapter != null && postData != null)
           mAdapter.onPostUpdated(postData);
   }


    // called from container activity
    public void addPost(NewsFeed.PostData postData){
        if (mAdapter != null && postData != null)
            mAdapter.addPost(postData);
    }


    // called from container activity
    public void loadPostDataList(){
        requestPostDataList();

    }




   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ConstHelper.RequestCode.CREATE_EDIT_NEWS_FEED_POST)
                requestPostDataList();

                // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_COMMENT && data != null && data.getExtras() != null) {
                NewsFeed.PostData postData = (NewsFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NEWS_FEED_POST_DATA);
                if (mAdapter != null && postData != null)
                    mAdapter.onPostUpdated(postData);
            }


            // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_SHARE && data != null && data.getExtras() != null) {
                NewsFeed.PostData postData = (NewsFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NEWS_FEED_POST_DATA);
                if (mAdapter != null && postData != null)
                    mAdapter.addPost(postData);
            }


        }

    }*/


/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null && item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
*/


    /*@Override
    protected void onResume() {
        super.onResume();
        // prepareRecyclerView();
        ActivityHelper.getInstance().setNewsFeedVisible(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ActivityHelper.getInstance().setNewsFeedVisible(false);
    }*/


    // # methods of presenter's View class starts
    @Override
    public void onPostCreated(NewsFeed.PostData postData) {

    }

    @Override
    public void onPostEdited(NewsFeed.PostData postData) {

    }

    @Override
    public void onPostListReceived(List<NewsFeed.PostData> postDataList) {
        hideLoader();

        if (postDataList != null) {
            mListPostData = postDataList;
            if (mAdapter != null)
                mAdapter.addNewsFeedList(postDataList);
        }

    }

    @Override
    public void onPostLikeUpdated(NewsFeed.PostData postData) {
        hideLoader();
        mAdapter.onPostUpdated(postData);
    }

    @Override
    public void onPostCommentsReceived(List<NewsFeed.PostData.PostComment> postCommentList) {

    }

    @Override
    public void onPostCommentAdded(NewsFeed.PostData.PostComment postComment) {

    }

    @Override
    public void onPostCommentEdited(NewsFeed.PostData.PostComment postComment) {

    }

    @Override
    public void onPostCommentDeleted(NewsFeed.PostData.PostComment postComment) {

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
    public void onPostShared(NewsFeed.PostData postData) {

    }


    @Override
    public void onSearchToFilter(final String searchedText) {
        // # filtering on new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<NewsFeed.PostData> filteredList = new ArrayList<>();
                for (NewsFeed.PostData postData : mListPostData) {
                    //if the existing elements contains the search input
                    String userName = postData.getPostByUserName();
                    if (userName != null)
                        if (userName.toLowerCase().contains(searchedText.toLowerCase()))
                            filteredList.add(postData);
                }

                if (getActivity() != null)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.filterList(filteredList);
                        }
                    });
                }

            }
        }).start();

    }


}
