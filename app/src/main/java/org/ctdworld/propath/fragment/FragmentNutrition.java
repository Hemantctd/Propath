package org.ctdworld.propath.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNutritionFeed;
import org.ctdworld.propath.activity.ActivityNutritionFeedPost;
import org.ctdworld.propath.adapter.AdapterNutritionFeed;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractNutritionFeed;
import org.ctdworld.propath.helper.ActivityHelper;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.NutritionFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNutritionFeed;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


// it's name has been changed to food diary
public class FragmentNutrition extends BaseFragment implements ContractNutritionFeed.View, FragmentSearch.SearchListener {

    // # Constants
    private static final String TAG = ActivityNutritionFeed.class.getSimpleName();  // tag used in log

    // # views
    private RecyclerView recyclerView; // to show post list
    private FloatingActionButton mFloatButton; // floating button to create new post


    // # other variables
    private Context mContext;  // context
    private AdapterNutritionFeed mAdapter; // adapter to show post
    private List<NutritionFeed.PostData> mListPostData = new ArrayList<>(); // contains post list came from server, used to filter list
    private ContractNutritionFeed.Presenter mPresenterNutritionFeed; // presenter to make server request

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        init(view);  // initializing
        setUpAdapter(); // # setting up adapter

        // adding fragment to filter
        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();


        requestPostDataList();  // loading nutrition feed post list
        mFloatButton.setOnClickListener(onFloatButtonClicked);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // mAdapter.onScrollingStopped();
                    Log.i(TAG, "scrolling stopped = " + newState);
                }
            }
        });
        return view;
    }


    private void init(View view) {
        mContext = getContext();
        mPresenterNutritionFeed = new PresenterNutritionFeed(mContext, this);
        recyclerView = view.findViewById(R.id.recycler_news_feed);
        mFloatButton = view.findViewById(R.id.news_feed_floatingActionButton);

    }

    // # setting up adapter post list
    private void setUpAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterNutritionFeed(mContext, mPresenterNutritionFeed, FragmentNutrition.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    // # click listener to create new post
    private View.OnClickListener onFloatButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(new Intent(getContext(), ActivityNutritionFeedPost.class));
            i.putExtra(ActivityNutritionFeedPost.KEY_EDIT_OR_CREATE, ActivityNutritionFeedPost.ACTION_CREATE_POST);
            startActivityForResult(i, ConstHelper.RequestCode.CREATE_EDIT_NUTRITION_FEED_POST);
        }
    };

    // getting news feed data list
    private void requestPostDataList() {
        Log.i(TAG, "Loading Nutrition feed data list.......");
        if (mPresenterNutritionFeed != null) {
            showLoader(getString(R.string.message_loading));
            mPresenterNutritionFeed.requestPostDataList(SessionHelper.getUserId(mContext));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ConstHelper.RequestCode.CREATE_EDIT_NUTRITION_FEED_POST) {
//                requestPostDataList();
                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .detach(this)
                            .attach(this)
                            .commit();
                }
            }
            // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NUTRITION_FEED_COMMENT && data != null && data.getExtras() != null) {
                NutritionFeed.PostData postData = (NutritionFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NUTRITION_FEED_POST_DATA);
                if (mAdapter != null && postData != null)
                    mAdapter.onPostUpdated(postData);
            }


            // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NUTRITION_FEED_SHARE && data != null && data.getExtras() != null) {
                NutritionFeed.PostData postData = (NutritionFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NUTRITION_FEED_POST_DATA);
                if (mAdapter != null && postData != null)
                    mAdapter.addPost(postData);
            }

        }
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
        if (failType == NutritionFeed.FAILED_RECEIVING_POST_LIST)
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

                if (getActivity() != null) {
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

    @Override
    public void onResume() {
        super.onResume();
        ActivityHelper.getInstance().setNutritionFeedVisible(true);
    }


    @Override
    public void onStop() {
        super.onStop();
        ActivityHelper.getInstance().setNutritionFeedVisible(false);
    }


}
