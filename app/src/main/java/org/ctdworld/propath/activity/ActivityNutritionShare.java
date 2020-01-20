package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNutritionFeed;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNutritionFeed;
import org.ctdworld.propath.contract.ContractShareNutrition;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Nutrition;
import org.ctdworld.propath.model.NutritionFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNutritionFeed;
import org.ctdworld.propath.presenter.PresenterNutritionShare;

import java.util.List;

public class ActivityNutritionShare extends BaseActivity implements ContractNutritionFeed.View {

    public static final String TAG = ActivityNutritionShare.class.getSimpleName();
    public static final String NUTRITION_DATA = "nutrition data";

    public static final String POST_SHARE_TO = "groups";


    // Views
    Toolbar mToolbar;  // toolbar
    TextView mTxtToolbarTitle;  // toolbar title
    private TextView mTxtUserName;  // user name of the user who is sharing the post
    private TextView mTxtPostUserName;   // user name of the received post
    private TextView mTxtPostMessage;   // user name of the received post
    private TextView mTxtPostDate;   // date time of the received post
    TextView mTxtShareDate;
    private TextView mTxtPostVisibilitySetting;  // shows post visibility(groups)
    private ImageView mImgPost;  // user pic of the  received post
    private ImageView mImgPostUserPic;  // user pic of the  received post
    private ImageView mImgUserPic;  // user pic of the user who is sharing the post
    private ImageView mImgPostVisibilitySetting; // shows post visibility icon (groups)
    private View mLayoutPostVisibilitySetting;  // layout for post visibility setting
    private EditText mEditShareMessage;  // to enter share message
    private Button mBtnShare;  /// button to share post
    private View mLayoutPost;
    String groupID;

    private VideoView mNutritionSharePostVideo;

    // # Variables
    private Context mContext;
    public static final String KEY_POST_DATA = "post data";
    private NutritionFeed.PostData mPostData = new NutritionFeed.PostData();
    private ContractNutritionFeed.Presenter mPresenterNutritionFeed;


    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView mYoutubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_share);
        init();

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        mLayoutPost.setAnimation(animation);

        setToolBar();
        setViews();
        //setListeners();

        mLayoutPostVisibilitySetting.setOnClickListener(onPostVisibilitySettingClicked);  // starting post setting page
        mBtnShare.setOnClickListener(onBtnSharePostClicked);  // listener to share post

    }

    private void init() {


        mContext = this;
        mPresenterNutritionFeed = new PresenterNutritionFeed(mContext, this);
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

        mNutritionSharePostVideo = findViewById(R.id.video_view);
        mYoutubePlayer = findViewById(R.id.youtube_player);

        mImgPostVisibilitySetting = findViewById(R.id.news_feed_post_type_img);
        mTxtPostVisibilitySetting = findViewById(R.id.news_feed_post_type);
        mTxtShareDate = findViewById(R.id.txt_share_date);

        // getting data from bundle, if NewsFeed.PostData object has not been sent then it will be initialized as new object
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPostData = (NutritionFeed.PostData) bundle.getSerializable(KEY_POST_DATA);
            if (mPostData == null)
                mPostData = new NutritionFeed.PostData();

            groupID = bundle.getString(POST_SHARE_TO);
            if (groupID != null) {
                Log.d(TAG, "group_id..." + groupID);
            }


        } else
            mPostData = new NutritionFeed.PostData();

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
                    mNutritionSharePostVideo.setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    mYoutubePlayer.setVisibility(View.GONE);

                    showVideo();
                } else // showing image
                {
                    mImgPost.setVisibility(View.VISIBLE);
                    mNutritionSharePostVideo.setVisibility(View.GONE);
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
                mNutritionSharePostVideo.setVisibility(View.GONE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                findViewById(R.id.unmute).setVisibility(View.GONE);
                mYoutubePlayer.setVisibility(View.VISIBLE);
                showYouTubeVideo();
            }
        }
    }

    // # showing youtube video thumbnail and playing video
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

        mNutritionSharePostVideo.setVideoURI(Uri.parse(mPostData.getPostMediaUrl()));
        mNutritionSharePostVideo.requestFocus();
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        findViewById(R.id.mute).setVisibility(View.GONE);
        findViewById(R.id.unmute).setVisibility(View.VISIBLE);


        findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNutritionSharePostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(mNutritionSharePostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNutritionSharePostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                } else {
                    Snackbar.make(mNutritionSharePostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mNutritionSharePostVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPreparedListener called");
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                mNutritionSharePostVideo.start();
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

            if (mPostData != null && mPresenterNutritionFeed != null) {
                showLoader(getString(R.string.message_sharing));
                mPostData.setPostSharedMessage(mEditShareMessage.getText().toString().trim());
                mPostData.setPostSharedBy(SessionHelper.getUserId(mContext));
                mPresenterNutritionFeed.sharePost(mPostData);
            }
        }
    };


    private View.OnClickListener onPostVisibilitySettingClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, ActivityNutritionGroup.class);
            i.putExtra(ActivityNutritionShare.KEY_POST_DATA, mPostData);
            startActivityForResult(i, ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_SETTING_PAGE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_SETTING_PAGE && data != null) {
            Bundle bundle = data.getExtras();
            NutritionFeed.PostData postdata = (NutritionFeed.PostData) bundle.get(ActivityNutritionFeedPost.KEY_POST_DATA);
            if (postdata != null) {
                mPostData.setGroupID(postdata.getGroupID());
                mPostData.setGroupName(postdata.getGroupName());
                mTxtPostVisibilitySetting.setText(mPostData.getGroupName());
            }
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
        super.onBackPressed();
    }


    @Override
    public void onPostCreated(NutritionFeed.PostData postData) {

    }

    @Override
    public void onPostEdited(NutritionFeed.PostData postData) {

    }

    @Override
    public void onPostListReceived(List<NutritionFeed.PostData> postDataList) {

    }

    @Override
    public void onPostLikeUpdated(NutritionFeed.PostData postData) {

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


    // called for any type of failure while requesting to server
    @Override
    public void onFailed(int failType, String message) {
        hideLoader();
        if (failType == NutritionFeed.FAILED_SHARING_POST) {
            String title = getString(R.string.message_failed);
            if (message == null || message.isEmpty())
                DialogHelper.showSimpleCustomDialog(mContext, title);
            else
                DialogHelper.showSimpleCustomDialog(mContext, title, message);
        }
    }


    @Override
    public void onPostShared(NutritionFeed.PostData postData) {
        hideLoader();
        Toast toast = Toast.makeText(mContext, "Post shared successfully", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        if (postData != null) {
            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.NUTRITION_FEED_POST_DATA, postData);
            setResult(RESULT_OK, intent);
            finish();
        } else
            finish();
    }
}
