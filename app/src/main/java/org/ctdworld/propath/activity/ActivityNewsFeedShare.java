package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;

import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNewsFeed;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNewsFeed;

import java.util.List;


public class ActivityNewsFeedShare extends BaseActivity implements ContractNewsFeed.View {


    private final String TAG = ActivityNewsFeedShare.class.getSimpleName();


    // Views
    Toolbar mToolbar;  // toolbar
    TextView mTxtToolbarTitle;  // toolbar title
    private TextView mTxtUserName;  // user name of the user who is sharing the post
    private TextView mTxtPostUserName;   // user name of the received post
    private TextView mTxtPostMessage;   // user name of the received post
    private TextView mTxtPostDate;   // date time of the received post
    TextView mTxtShareDate;
    private TextView mTxtPostVisibilitySetting;  // shows post visibility(public or connections)
    private ImageView mImgPost;  // user pic of the  received post
    private ImageView mImgPostUserPic;  // user pic of the  received post
    private ImageView mImgUserPic;  // user pic of the user who is sharing the post
    private ImageView mImgPostVisibilitySetting; // shows post visibility icon (public or connections)
    private View mLayoutPostVisibilitySetting;  // layout for post visibility setting
    private EditText mEditShareMessage;  // to enter share message
    private Button mBtnShare;  /// button to share post
    private View mLayoutPost;

    private VideoView mNewsfeedSharePostVideo;
    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView mYoutubePlayer;


    // # Variables
    private Context mContext;
    public static final String KEY_POST_DATA = "post data";
    private NewsFeed.PostData mPostData = new NewsFeed.PostData();
    private ContractNewsFeed.Presenter mPresenterNewsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_share);

        init();

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        mLayoutPost.setAnimation(animation);
        ;

        setToolBar();
        setPostVisibilitySetting();  // # setting post visibility and comment setting
        setViews();
        //setListeners();

        mLayoutPostVisibilitySetting.setOnClickListener(onPostVisibilitySettingClicked);  // starting post setting page
        mBtnShare.setOnClickListener(onBtnSharePostClicked);  // listener to share post

    }


    // # initializing
    private void init() {
        mContext = this;
        mPresenterNewsFeed = new PresenterNewsFeed(mContext, this);
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgUserPic = findViewById(R.id.img_user_pic_sharing_post);
        mTxtUserName = findViewById(R.id.news_feed_profile_name);
        mLayoutPostVisibilitySetting = findViewById(R.id.news_feed_post_type_layout);
        mEditShareMessage = findViewById(R.id.news_feed_post_share_data);
        mBtnShare = findViewById(R.id.new_feed_share_button);
        mLayoutPost = findViewById(R.id.layout_post);
        mImgPostUserPic = findViewById(R.id.profile_image);
        mTxtPostUserName = findViewById(R.id.profile_name);
        mTxtPostMessage = findViewById(R.id.message);
        mImgPost = findViewById(R.id.news_feed_image);
        mTxtPostDate = findViewById(R.id.date);

        mNewsfeedSharePostVideo = findViewById(R.id.video_view);
        mYoutubePlayer = findViewById(R.id.youtube_player);

        mImgPostVisibilitySetting = findViewById(R.id.news_feed_post_type_img);
        mTxtPostVisibilitySetting = findViewById(R.id.news_feed_post_type);
        mTxtShareDate = findViewById(R.id.txt_share_date);

        // getting data from bundle, if NewsFeed.PostData object has not been sent then it will be initialized as new object
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPostData = (NewsFeed.PostData) bundle.getSerializable(KEY_POST_DATA);
            if (mPostData == null)
                mPostData = new NewsFeed.PostData();
        } else
            mPostData = new NewsFeed.PostData();

        mTxtShareDate.setText(DateTimeHelper.getCurrentSystemDateTime());

    }


    // # setting toolbar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.share);
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
        mPostData.setPostVisibility(NewsFeed.VISIBLE_TO_CONTACTS);


        switch (mPostData.getPostVisibility()) {
            case NewsFeed.VISIBLE_TO_PUBLIC:
                mTxtPostVisibilitySetting.setText(getResources().getString(R.string.news_feed_setting_public));
                mTxtPostVisibilitySetting.setTextColor(getResources().getColor(R.color.colorTheme));
                mImgPostVisibilitySetting.setColorFilter(getResources().getColor(R.color.colorTheme));
                break;

            case NewsFeed.VISIBLE_TO_CONTACTS:
                mTxtPostVisibilitySetting.setText(getResources().getString(R.string.news_feed_setting_connection));
                mTxtPostVisibilitySetting.setTextColor(getResources().getColor(R.color.colorTheme));
                mImgPostVisibilitySetting.setColorFilter(getResources().getColor(R.color.colorTheme));
                break;
        }
    }


    // # setting views data
    private void setViews() {

        // # setting views for the user who is sharing ths post
        mTxtUserName.setText(SessionHelper.getInstance(mContext).getUser().getName());  // name of the user who is sharing the post
        UtilHelper.loadImageWithGlide(mContext, SessionHelper.getInstance(mContext).getUser().getUserPicUrl(), mImgUserPic);  // pic of the user who is sharing the post


        // # setting views for received post being shared

        if (mPostData == null)
            return;

        mTxtPostUserName.setText(mPostData.getPostByUserName());  // setting user name of the received post
        mTxtPostMessage.setText(mPostData.getPostMessage());  //  setting post message of the received post
        mTxtPostDate.setText(DateTimeHelper.getDateTime(mPostData.getPostDateTime(), DateTimeHelper.FORMAT_DATE_TIME));

        UtilHelper.loadImageWithGlide(mContext, mPostData.getPostByUserProfilePic(), mImgPostUserPic);  // user pic at top

        if (!mPostData.getPostMediaUrl().isEmpty() && mPostData.getPostMediaUrl() != null) {

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = MimeTypeMap.getFileExtensionFromUrl(mPostData.getPostMediaUrl());

            if (mimeTypeMap != null && extension != null) {
                String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
                if (mimeType != null && mimeType.contains("video"))  // showing video
                {
                    mImgPost.setVisibility(View.GONE);
                    mNewsfeedSharePostVideo.setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    mYoutubePlayer.setVisibility(View.GONE);

                    showVideo();
                } else // showing image
                {
                    mImgPost.setVisibility(View.VISIBLE);
                    mNewsfeedSharePostVideo.setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    mYoutubePlayer.setVisibility(View.GONE);

//                    int picDimen1 = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
                    mImgPost.setVisibility(View.VISIBLE);
                    UtilHelper.loadImageWithGlide(mContext, mPostData.getPostMediaUrl(), mImgPost);  // post image
                }
            }

            if (UtilHelper.isYoutubeUrl(mPostData.getPostMessage())) {
                mImgPost.setVisibility(View.GONE);
                mNewsfeedSharePostVideo.setVisibility(View.GONE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                findViewById(R.id.unmute).setVisibility(View.GONE);
                mYoutubePlayer.setVisibility(View.VISIBLE);
                showYouTubeVideo();
            }
        }
    }

    private void showYouTubeVideo() {
        Log.i(TAG, "you tube url : " + mPostData.getPostMessage());
        final String url = UtilHelper.getYoutubeVideoThumbnailUrl(mPostData.getPostMessage());
        Log.i(TAG, "you tube image : " + url);
        if (url.equals(ConstHelper.NOT_FOUND_STRING))
            return;


        mYoutubePlayer.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {

                String videoId = UtilHelper.getYoutubeVideoID(mPostData.getPostMessage());
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0);
                    // youTubePlayer.play();
                }
            }
        });
    }

    private void showVideo() {

        Log.i(TAG, "showing video");

        mNewsfeedSharePostVideo.setVideoURI(Uri.parse(mPostData.getPostMediaUrl()));
        mNewsfeedSharePostVideo.requestFocus();
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        findViewById(R.id.mute).setVisibility(View.GONE);
        findViewById(R.id.unmute).setVisibility(View.VISIBLE);


        findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewsfeedSharePostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(mNewsfeedSharePostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewsfeedSharePostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                } else {
                    Snackbar.make(mNewsfeedSharePostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mNewsfeedSharePostVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPreparedListener called");
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                mNewsfeedSharePostVideo.start();
                mp.setLooping(true);
            }
        });
    }


    // listener to share post
    private View.OnClickListener onBtnSharePostClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!UtilHelper.isConnectedToInternet(mContext)) // checking internet connection
            {
                DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.title_no_connection), getString(R.string.message_no_connection));
                return;
            }

            if (mPostData != null && mPresenterNewsFeed != null) {
                showLoader(getString(R.string.message_sharing));
                mPostData.setPostSharedMessage(mEditShareMessage.getText().toString().trim());
                mPostData.setPostSharedBy(SessionHelper.getUserId(mContext));
                mPresenterNewsFeed.sharePost(mPostData);
            }
        }
    };


    // called after coming from ActivityNewsFeedSettings, setting post setting to mPostData object
    private void onPostVisibilitySettingChanged(Intent intentData) {
        if (intentData == null)
            return;

        NewsFeed.PostData postData = (NewsFeed.PostData) intentData.getSerializableExtra(ActivityNewsFeedSettings.KEY_POST_DATA);

        if (mPostData != null && postData != null) {
            mPostData.setPostVisibility(postData.getPostVisibility());
            mPostData.setPostCommentPermission(postData.getPostCommentPermission());
            setPostVisibilitySetting();
        }
    }


    private View.OnClickListener onPostVisibilitySettingClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, ActivityNewsFeedSettings.class);
            i.putExtra(ActivityNewsFeedSettings.KEY_POST_DATA, mPostData);
            startActivityForResult(i, ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_SETTING_PAGE);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // setting post visibility setting data after coming from post setting page
        if (resultCode == RESULT_OK && data != null && requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_SETTING_PAGE)
            onPostVisibilitySettingChanged(data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onPostCreated(NewsFeed.PostData postData) {

    }

    @Override
    public void onPostEdited(NewsFeed.PostData postData) {

    }

    @Override
    public void onPostListReceived(List<NewsFeed.PostData> postDataList) {

    }

    @Override
    public void onPostLikeUpdated(NewsFeed.PostData postData) {

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


    // called for any type of failure while requesting to server
    @Override
    public void onFailed(int failType, String message) {
        hideLoader();
        if (failType == NewsFeed.FAILED_SHARING_POST) {
            String title = getString(R.string.message_failed);
            if (message == null || message.isEmpty())
                DialogHelper.showSimpleCustomDialog(mContext, title);
            else
                DialogHelper.showSimpleCustomDialog(mContext, title, message);
        }
    }


    @Override
    public void onPostShared(NewsFeed.PostData postData) {
        hideLoader();
        Toast toast = Toast.makeText(mContext, "Post shared successfully", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        if (postData != null) {
            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.NEWS_FEED_POST_DATA, postData);
            setResult(RESULT_OK, intent);
            finish();
        } else
            finish();
    }
}
