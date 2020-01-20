package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNewsFeedComments;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNewsFeed;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNewsFeed;

import java.util.List;


public class ActivityNewsFeedComments extends BaseActivity implements ContractNewsFeed.View {

    private final String TAG = ActivityNewsFeedComments.class.getSimpleName();  // tag used in Log

    // # Views
    private Toolbar mToolbar;
    private EditText mEditComment;  // to enter comment for post
    private Button mBtnPost;   // button to post comment
    private RecyclerView mRecyclerComments;   // recycler to show all comments
    private TextView mTxtToolbarTitle;  // toolbar title
    private TextView txtSharedPostMessage;  // message added while sharing post
    private TextView txtProfileName;    // user name who has posted feed
    private TextView mTxtPostDate;   // posted date
    private TextView mTxtPostMessage;  // message while posting
    private TextView mTxtCountLikeComment; // shows like and comment count below posted image
    private TextView mTxtLikeText;  // like text in layoutLike at bottom
    private TextView mTxtPostShareBy;  // name of the user who has shared post. "like developer has shared this post"
    private ImageView mProfileImage;  // user pic
    private ImageView mImgPost; // posted images
    private ImageView mImgLike; // like icon in post
    private ImageView mImgUserPicBottom; // user pic at left of comment edit box at bottom
    private View mLikeLayout; // layout to like post
    private View mShareLayout;  // layout to share post
    private View mLayoutCommentAtBottom;   // layout at bottom of screen to add comment
    private ProgressBar mProgressBarComments; // progress bar to show while loading comments from server
    private View mLayoutPost;  // layout which contains post details excluding comments;


    // # Variables
    private Context mContext;  // context
    private NewsFeed.PostData.PostComment mPostCommentEdit;  // this object is set when edit option is clicked in comment option menu to edit, this object will be used to edit comment
    boolean mIsEditCommentEnabled = false; // to check if new comment is being added or existing comment is being edited from adapter
    private ContractNewsFeed.Presenter mPresenterNewsFeed;  // presenter to request to server
    private AdapterNewsFeedComments mAdapterComments; // adapter for comments


    // Extras
    public static final String KEY_POST_DATA = "post data";   // # key to set data
    private NewsFeed.PostData mPostData;  // object to contain received data and updated data

    //  #Video
    private VideoView news_feed_comment_video_view;
    private ImageView mute, unmute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_comments);
        init();
        setToolBar(); // setting toolbar
        setUpCommentAdapter(); // setting comment adapter
        requestPostCommentList();  // getting post comment list from server and showing
        setViews();  // setting data on views

        // # setting animation on post layout
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        mLayoutPost.setAnimation(animation);


        // # setting listener
        mShareLayout.setOnClickListener(onShareLayoutClicked);  // listener to share post
        mBtnPost.setOnClickListener(onPostButtonClicked);  // listener to add new comment on post
        mLikeLayout.setOnClickListener(onLikeLayoutClicked);   // listener to like or remove if already liked

    }


    @Override
    protected void onDestroy() {

        if (mPostData != null && mAdapterComments != null) {
            mPostData.setPostCommentCount(String.valueOf(mAdapterComments.getItemCount()));
            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.NEWS_FEED_POST_DATA, mPostData);
            setResult(RESULT_OK, intent);
            finish();
        }

        super.onDestroy();
    }

    // # initializing
    private void init() {
        mContext = this;
        mLayoutPost = findViewById(R.id.layout_post);
        mPresenterNewsFeed = new PresenterNewsFeed(mContext, this);


        txtSharedPostMessage = findViewById(R.id.share_by_text);
        mTxtPostShareBy = findViewById(R.id.shared_by);
        mProfileImage = findViewById(R.id.profile_image);
        txtProfileName = findViewById(R.id.profile_name);
        mTxtPostDate = findViewById(R.id.date);
        mTxtPostMessage = findViewById(R.id.message);
        mImgPost = findViewById(R.id.news_feed_image);
        mTxtCountLikeComment = findViewById(R.id.count);
        mLikeLayout = findViewById(R.id.layout_like);
        mShareLayout = findViewById(R.id.comment_layout);
        mShareLayout = findViewById(R.id.share_layout);
        mImgLike = findViewById(R.id.like_btn);
        mTxtLikeText = findViewById(R.id.like_text);


        news_feed_comment_video_view = findViewById(R.id.video_view);
        unmute = findViewById(R.id.unmute);
        mute = findViewById(R.id.mute);

        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRecyclerComments = findViewById(R.id.recycler_comments);
        mImgUserPicBottom = findViewById(R.id.img_user_pic_at_bottom);
        mEditComment = findViewById(R.id.edit_comment);
        mBtnPost = findViewById(R.id.btn_post);
        mLayoutCommentAtBottom = findViewById(R.id.layout_comment_at_bottom);
        mProgressBarComments = findViewById(R.id.progress_bar);


        Bundle mBundle = getIntent().getExtras();
        try {
            if (mBundle != null) {
                mPostData = (NewsFeed.PostData) mBundle.getSerializable(KEY_POST_DATA);
                if (mPostData == null)
                    mPostData = new NewsFeed.PostData();
            } else
                mPostData = new NewsFeed.PostData();
        } catch (Exception e) {
            Log.e(TAG, "exception while fetching NewsFee.PostData object");
            e.printStackTrace();
        }

    }


    // # getting post comment list
    private void requestPostCommentList() {
        NewsFeed.PostData.PostComment postComment = new NewsFeed.PostData.PostComment();
        postComment.setPostId(mPostData.getPostId());
        mPresenterNewsFeed.requestPostComments(postComment);
    }


    // # setting toolbar data
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.comment);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }


    // # set up recycler mAdapterComments for comment
    private void setUpCommentAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapterComments = new AdapterNewsFeedComments(mContext);
        mRecyclerComments.setLayoutManager(layoutManager);
        mRecyclerComments.setAdapter(mAdapterComments);
    }


    // # setting views data
    private void setViews() {
        if (mPostData == null)
            return;

        txtProfileName.setText(mPostData.getPostByUserName());  // setting user name who has posted
        mTxtPostMessage.setText(mPostData.getPostMessage());
        UtilHelper.loadImageWithGlide(mContext, mPostData.getPostByUserProfilePic(), mImgUserPicBottom, R.drawable.ic_profile);  // user pic at bottom
        UtilHelper.loadImageWithGlide(mContext, mPostData.getPostByUserProfilePic(), mProfileImage, R.drawable.ic_profile);  // user pic at top

        if (mPostData.getPostMediaUrl() != null && !mPostData.getPostMediaUrl().isEmpty()) {
            String mediaUrl = mPostData.getPostMediaUrl();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = MimeTypeMap.getFileExtensionFromUrl(mediaUrl);
            if (mimeTypeMap != null && extension != null) {
                String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
                if (mimeType != null && mimeType.contains("video"))  // showing video
                {
                    Log.i(TAG, "showing video");

                    news_feed_comment_video_view.setVisibility(View.VISIBLE);
                    mImgPost.setVisibility(View.GONE);

                    news_feed_comment_video_view.setVideoURI(Uri.parse(mPostData.getPostMediaUrl()));
                    news_feed_comment_video_view.requestFocus();
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    unmute.setVisibility(View.VISIBLE);
                    mute.setVisibility(View.GONE);
                    news_feed_comment_video_view.start();

                    findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (news_feed_comment_video_view.isPlaying()) {
                                AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                                unmute.setVisibility(View.GONE);
                                mute.setVisibility(View.VISIBLE);
                            } else {
                                Snackbar.make(news_feed_comment_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                    findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (news_feed_comment_video_view.isPlaying()) {
                                AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                                unmute.setVisibility(View.VISIBLE);
                                mute.setVisibility(View.GONE);
                            } else {
                                Snackbar.make(news_feed_comment_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else // showing image
                {
                    Log.i(TAG, "showing image");

                    news_feed_comment_video_view.setVisibility(View.GONE);
                    mute.setVisibility(View.GONE);
                    unmute.setVisibility(View.GONE);
                    mImgPost.setVisibility(View.VISIBLE);
                    UtilHelper.loadImageWithGlide(mContext, mPostData.getPostMediaUrl(), mImgPost);  // post image
                }
            }
        }

        // # setting post shared by
        if (!mPostData.getPostSharedMessage().equals(NewsFeed.POST_NOT_SHARED)) {
            mTxtPostShareBy.setVisibility(View.VISIBLE);
            txtSharedPostMessage.setVisibility(View.VISIBLE);
            mTxtPostShareBy.setText(String.format("%s shared this post", mPostData.getPostByUserName()));
            txtSharedPostMessage.setText(mPostData.getPostSharedMessage());
        }

        setLikeCommentCount();
        setPostLikedStatus(); // setting if post is liked on not


        // setting comment layout visibility based comment permission
        if (mPostData.getPostCommentPermission() == NewsFeed.COMMENT_ALLOWED)  // # showing comment layout if comment is allowed
            mLayoutCommentAtBottom.setVisibility(View.VISIBLE);
        else   // # hiding comment layout if comment is not allowed
            mLayoutCommentAtBottom.setVisibility(View.GONE);


        // # setting post date
        mTxtPostDate.setText(DateTimeHelper.getDateTime(mPostData.getPostDateTime(), DateTimeHelper.FORMAT_DATE_TIME));


        // showing comment layout if comment is allowed otherwise hiding comment layout
        if (mPostData.getPostCommentPermission() != NewsFeed.COMMENT_ALLOWED) {
            mLayoutCommentAtBottom.setVisibility(View.GONE);
        } else if (mPostData.getPostCommentPermission() == NewsFeed.COMMENT_ALLOWED) {
            mLayoutCommentAtBottom.setVisibility(View.VISIBLE);

        }

    }


    // # setting post liked or not liked
    private void setPostLikedStatus() {
        if (mPostData.getLikeStatus().equals(NewsFeed.POST_NOT_LIKED)) {
            mImgLike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));
            mTxtLikeText.setTextColor(mContext.getResources().getColor(R.color.colorDarkGrey));
            mTxtLikeText.setText(R.string.like);  // like text if post is not liked

        } else if (mPostData.getLikeStatus().equals(NewsFeed.POST_LIKED)) {
            mImgLike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
            mTxtLikeText.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
            mTxtLikeText.setText(R.string.liked);  // Liked text if post is liked

        }
    }


    private void setLikeCommentCount() {

        mTxtCountLikeComment.setText(String.format("%s Likes | %s Comments", mPostData.getPostLikeCount(), mPostData.getPostCommentCount()));

        String likeText = "Like"; // like text if post is not liked
        String commentText = "Comment"; // comment text if there is only 1 comment

        if (Integer.parseInt(mPostData.getPostLikeCount()) > 1)
            likeText = "Likes";  // comment text if there are more than 1 comment

        if (Integer.parseInt(mPostData.getPostCommentCount()) > 1)
            commentText = "Comments";  // comment text if there are more than 1 comment

        mTxtCountLikeComment.setText(String.format("%s %s | %s %s", mPostData.getPostLikeCount(), likeText, mPostData.getPostCommentCount(), commentText));

    }


    private View.OnClickListener onLikeLayoutClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPostData == null)
                return;


            int likeCount = Integer.parseInt(mPostData.getPostLikeCount());
            if (mPostData.getLikeStatus().equals(NewsFeed.POST_NOT_LIKED)) {
                mPostData.setLikeStatus(NewsFeed.POST_LIKED);
                mPostData.setPostLikeCount(String.valueOf(likeCount + 1));
            } else if (mPostData.getLikeStatus().equals(NewsFeed.POST_LIKED)) {
                mPostData.setLikeStatus(NewsFeed.POST_NOT_LIKED);
                mPostData.setPostLikeCount(String.valueOf(likeCount - 1));
            }

            setPostLikedStatus();  // setting like status, liked or not liked
            setLikeCommentCount();


            mPresenterNewsFeed.updateLikeOnPost(mPostData);
        }
    };


    // # setting listener on button to post comment
    private View.OnClickListener onPostButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UtilHelper.hideKeyboard(ActivityNewsFeedComments.this);


            Log.i(TAG, "posting comment**********************************************");
            String commentMessage = mEditComment.getText().toString().trim();

            if (mIsEditCommentEnabled && mPostCommentEdit != null) // # editing existing comment
            {
                mPostCommentEdit.setCommentMessage(commentMessage);
                mPostCommentEdit.setPostId(mPostData.getPostId());
                mPostCommentEdit.setCommentUserId(SessionHelper.getUserId(mContext));

                mPresenterNewsFeed.editPostComment(mPostCommentEdit);
                mIsEditCommentEnabled = false;  // disabling edit mode
            } else  // # adding new comment
            {
                NewsFeed.PostData.PostComment postComment = new NewsFeed.PostData.PostComment();
                postComment.setCommentMessage(commentMessage);
                postComment.setPostId(mPostData.getPostId());
                postComment.setCommentUserId(SessionHelper.getUserId(mContext));

                mPostData.setPostComment(postComment);  // setting PostComment object
                mPresenterNewsFeed.addPostComment(postComment);
            }


        }
    };


    // # setting click listener on share layout
    private View.OnClickListener onShareLayoutClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, ActivityNewsFeedShare.class);
            i.putExtra(ActivityNewsFeedShare.KEY_POST_DATA, mPostData);
            mContext.startActivity(i);
        }
    };


    // called when comment's options menu is clicked
    public void onCommentOptionsMenuClicked(final NewsFeed.PostData.PostComment postComment) {
        BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_DELETE, "Delete Comment")
                .addOption(BottomSheetOption.OPTION_EDIT, "Edit Comment");

        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int option) {
                switch (option) {
                    case BottomSheetOption.OPTION_EDIT:
                        mEditComment.setText(postComment.getCommentMessage());
                        mEditComment.requestFocus(View.FOCUS_RIGHT);
                        mIsEditCommentEnabled = true;  // enabling edit mode
                        mPostCommentEdit = postComment;  // initializing object ot to edit comment
                        // # api will be hit when post button is clicked
                        break;

                    case BottomSheetOption.OPTION_DELETE:
                        String title = getString(R.string.are_you_sure);
                        String message = "Your comment will be deleted.";
                        DialogHelper.showCustomDialog(mContext, title, message, "Delete", "Close", new DialogHelper.ShowDialogListener() {
                            @Override
                            public void onOkClicked() {
                                mPresenterNewsFeed.deletePostComment(postComment);
                            }

                            @Override
                            public void onCancelClicked() {

                            }
                        });
                        break;
                }
            }
        });

        options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
    }


    @Override
    public void hideLoader() {
        super.hideLoader();
        mProgressBarComments.setVisibility(View.GONE);
    }

    // # presenter methods starts here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        if (mPostData != null && mAdapterComments != null) {
            mPostData.setPostCommentCount(String.valueOf(mAdapterComments.getItemCount()));
            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.NEWS_FEED_POST_DATA, mPostData);
            setResult(RESULT_OK, intent);
            finish();
        }


        /*Intent i = new Intent(ActivityNewsFeedComments.this,ActivityNewsFeed.class);
        startActivity(i);
        finish();*/
        super.onBackPressed();
    }

    @Override
    public void onPostCreated(NewsFeed.PostData postData) {
        hideLoader();
    }

    @Override
    public void onPostEdited(NewsFeed.PostData postData) {
        hideLoader();
    }

    @Override
    public void onPostListReceived(List<NewsFeed.PostData> postDataList) {
        hideLoader();
    }

    @Override
    public void onPostLikeUpdated(NewsFeed.PostData postData) {
        hideLoader();
        mPostData = postData;  // assigning new data after updating like
    }

    @Override
    public void onPostCommentsReceived(List<NewsFeed.PostData.PostComment> postCommentList) {
        hideLoader();
        Log.i(TAG, "news feed post comments ist received successfully");
        mAdapterComments.addPostCommentList(postCommentList);
    }

    @Override
    public void onPostCommentAdded(NewsFeed.PostData.PostComment postComment) {
        hideLoader();
        Log.i(TAG, "news feed post comment added successfully");
        if (mEditComment != null)
            mEditComment.setText("");

        /*if (mAdapterComments != null)
            mAdapterComments.onCommentAdded(postComment);*/
        requestPostCommentList();  // loading comment list again
    }

    @Override
    public void onPostCommentEdited(NewsFeed.PostData.PostComment postComment) {
        hideLoader();
        if (mAdapterComments != null && postComment != null)
            mAdapterComments.onCommentUpdated(postComment);

        if (mEditComment != null)
            mEditComment.setText("");


        // if comment  is disabled then comment layout will be invisible after existing comment is edited
        if (mPostData != null && mPostData.getPostCommentPermission() == NewsFeed.COMMENT_NOT_ALLOWED)
            mLayoutCommentAtBottom.setVisibility(View.GONE);

    }

    @Override
    public void onPostCommentDeleted(NewsFeed.PostData.PostComment postComment) {
        hideLoader();
        Log.i(TAG, "post comment deleted successfully");
        if (mAdapterComments != null && postComment != null)
            mAdapterComments.onCommentDeleted(postComment);
    }

    @Override
    public void onFailed(int failType, String message) {
        hideLoader();
        if (failType != NewsFeed.FAILED_GETTING_COMMENTS)
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_failed));
    }

    @Override
    public void onPostShared(NewsFeed.PostData postData) {
        hideLoader();
    }

    // # presenter methods ends here


}
