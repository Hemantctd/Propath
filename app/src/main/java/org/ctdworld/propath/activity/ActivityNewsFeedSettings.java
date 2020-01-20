package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.NewsFeed;

public class ActivityNewsFeedSettings extends AppCompatActivity {

    //private final String TAG = ActivityNewsFeedSettings.class.getSimpleName();

    // # Views
    private Toolbar mToolbar;  // toolbar
    private View mLayoutVisibilityPublic;  // layout to set post visibility to public
    private View mLayoutVisibilityConnections;   // layout to set post visibility to connections(friends)
    private View mLayoutCommentAllow;
    private View mLayoutCommentDisable;
    private TextView mTxtToolbarTitle; // toolbar title
    private TextView mTxtVisibilityPublic;  // text for public visibility
    private TextView mTxtVisibilityConnections;  // text for connections visibility
    private TextView mTxtCommentAllow;  // text for allow comment
    private TextView mTxtCommentDisable;  // text for disable comment
    private ImageView mImgCommentDisable;  // check icon for disable comment
    private ImageView mImgCommentAllow;  // check icon for allow comment
    private ImageView mImgVisibilityPublic;  // check icon for public visibility
    private ImageView mImgVisibilityConnections;  // check icon for connections visibility


    Context mContext;


    // public static final String KEY_SETTING = "setting";
    // public static final String KEY_COMMENT_STATUS = "comment status";

    int mPostVisibility;  // contains to whom post is visible (connections or public)
    int mCommentAllowPermission;  // contains whether comment is allowed or not

    // # extras
    public static final String KEY_POST_DATA = "post data";
    private NewsFeed.PostData mPostData;  // contains NewsFeed.PostData object passed from calling activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_settings);
        init();
        setToolBar();
        setPostVisibilitySetting();
        setCommentPermissions();

        mLayoutVisibilityPublic.setOnClickListener(onLayoutVisibilityPublicClicked);
        mLayoutVisibilityConnections.setOnClickListener(onLayoutVisibilityConnectionsClicked);
        mLayoutCommentAllow.setOnClickListener(onLayoutCommentAllowClicked);
        mLayoutCommentDisable.setOnClickListener(onLayoutCommentDisableClicked);

    }


    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mLayoutVisibilityPublic = findViewById(R.id.layout_visibility_public);
        mLayoutVisibilityConnections = findViewById(R.id.layout_visibility_connections);
        mTxtVisibilityConnections = findViewById(R.id.txt_visibility_connections);
        mTxtVisibilityPublic = findViewById(R.id.txt_visibility_public);

        mToolbar = findViewById(R.id.toolbar);
        mImgVisibilityConnections = findViewById(R.id.img_visibility_connections);
        mImgVisibilityPublic = findViewById(R.id.img_visibility_public);
        mLayoutCommentAllow = findViewById(R.id.layout_comment_allow);
        mLayoutCommentDisable = findViewById(R.id.layout_comment_disable);
        mImgCommentDisable = findViewById(R.id.img_comment_disable);
        mImgCommentAllow = findViewById(R.id.img_comment_allow);
        mTxtCommentAllow = findViewById(R.id.txt_comment_allow);
        mTxtCommentDisable = findViewById(R.id.txt_comment_disable);


        // getting data from bundle, if NewsFeed.PostData object has not been sent then it will be initialized as new object
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPostData = (NewsFeed.PostData) bundle.getSerializable(KEY_POST_DATA);
            if (mPostData == null)
                mPostData = new NewsFeed.PostData();
        } else
            mPostData = new NewsFeed.PostData();


    }


    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.visibility_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }


    // setting post visibility text, setting for post visibilty and comment allow permission
    private void setPostVisibilitySetting() {
        if (mPostData == null)
            return;

        // # for now sharing all post to connections only, if we need public and connection both then we will just remove this setting
        // mPostData.setPostVisibility(NewsFeed.VISIBLE_TO_CONTACTS);

        switch (mPostData.getPostVisibility()) {
            case NewsFeed.VISIBLE_TO_PUBLIC:
                mTxtVisibilityPublic.setTextColor(getResources().getColor(R.color.colorTheme));
                mTxtVisibilityConnections.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                mImgVisibilityPublic.setVisibility(View.VISIBLE);
                mImgVisibilityConnections.setVisibility(View.GONE);
                break;

            case NewsFeed.VISIBLE_TO_CONTACTS:
                mTxtVisibilityConnections.setTextColor(getResources().getColor(R.color.colorTheme));
                mTxtVisibilityPublic.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                mImgVisibilityPublic.setVisibility(View.GONE);
                mImgVisibilityConnections.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void setCommentPermissions() {
        switch (mPostData.getPostCommentPermission()) {
            case NewsFeed.COMMENT_ALLOWED:
                mTxtCommentAllow.setTextColor(getResources().getColor(R.color.colorTheme));
                mTxtCommentDisable.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                mImgCommentDisable.setVisibility(View.GONE);
                mImgCommentAllow.setVisibility(View.VISIBLE);
                break;

            case NewsFeed.COMMENT_NOT_ALLOWED:
                mTxtCommentDisable.setTextColor(getResources().getColor(R.color.colorTheme));
                mTxtCommentAllow.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                mImgCommentDisable.setVisibility(View.VISIBLE);
                mImgCommentAllow.setVisibility(View.GONE);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.NEWS_FEED_POST_DATA, mPostData);
        setResult(RESULT_OK, intent);
        finish();

//        super.onBackPressed();
    }


    View.OnClickListener onLayoutVisibilityPublicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPostData != null)
                mPostData.setPostVisibility(NewsFeed.VISIBLE_TO_PUBLIC);
            setPostVisibilitySetting();
        }
    };


    View.OnClickListener onLayoutVisibilityConnectionsClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPostData != null)
                mPostData.setPostVisibility(NewsFeed.VISIBLE_TO_CONTACTS);
            setPostVisibilitySetting();
        }
    };


    View.OnClickListener onLayoutCommentAllowClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPostData != null)
                mPostData.setPostCommentPermission(NewsFeed.COMMENT_ALLOWED);
            setCommentPermissions();
        }
    };


    View.OnClickListener onLayoutCommentDisableClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPostData != null)
                mPostData.setPostCommentPermission(NewsFeed.COMMENT_NOT_ALLOWED);
            setCommentPermissions();
        }
    };


}
