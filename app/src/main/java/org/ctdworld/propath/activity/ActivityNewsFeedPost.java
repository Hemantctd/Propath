package org.ctdworld.propath.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.tcking.giraffecompressor.GiraffeCompressor;
import com.google.android.material.snackbar.Snackbar;
import com.yalantis.ucrop.UCrop;

import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNewsFeed;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNewsFeed;
import org.ctdworld.propath.utils.UiHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class ActivityNewsFeedPost extends BaseActivity implements ContractNewsFeed.View {
    private final String TAG = ActivityNewsFeedPost.class.getSimpleName();

    // # request codes
    // private static final int REQUEST_PERM_WRITE_STORAGE = 100; // request code for write storage permission
    private static final int REQUEST_CODE_IMAGE = 101;  // request code to select image
    private static final int REQUEST_CODE_VIDEO = 201;  // request code to select image

    private static final int REQUEST_CODE_POST_SETTING_ACTIVITY = 102;  // request code start post setting activity

    // # views
    private Toolbar mToolbar; // toolbar
    private PermissionHelper mPermissionHelper;  // PermissionHelper to check permission and request single permission
    private TextView mTxtToolbarTitle;  // toolbar title
    private TextView mTxtPostVisibilitySetting;  // shows post visibility(public or connections)
    private TextView mTxtUserName;  // show user name who is posting or edit post
    TextView mTxtDate;
    private Button mBtnPost;  // button to post
    private ImageView mImgPost;  // shows selected image being posted
    private ImageView mImgAttachMedia;  // to select media(image)
    private ImageView mImgCamera;  // to capture image from camera
    private ImageView imgPostVisibilitySetting; // shows post visibility icon (public or connections)
    private ImageView imgUserPic;  // shows user pic who is posting
    private EditText mEditPostMessage;  // EditText to enter post message
    private View mLayoutPostVisibilitySetting;  // layout for post visibility setting
    private VideoView news_feed_video_view;
    private String mSelectedImagePath;
    String currentPhotoPath = "";

    // # variables
    private Context mContext;  // contains activity context
    private ContractNewsFeed.Presenter mPresenterNewsFeed;
    //   private String mSelectedImagePath;  // contains post image path
    private NewsFeed.PostData mPostData;  // contains NewsFeed.PostData object passed from calling activity
    //  private int mPostVisibility = NewsFeed.VISIBLE_TO_CONTACTS;  // contains to whom post is visible (connections or public)
    //  private int mCommentAllowPermission =  NewsFeed.COMMENT_ALLOWED;  // contains whether comment is allowed or not


    // # extras
    public static final String KEY_POST_DATA = "post data";
    public static final String KEY_EDIT_OR_CREATE = "edit or create";   // CONTAINS ACTION EDIT OR CREATE
    public static final int ACTION_CREATE_POST = 1;  // TO CREATE NEW POST
    public static final int ACTION_EDIT_POST = 2;  // TO EDIT POST
    private int ACTION_CREATE_OR_EDIT = 0;   // CONTAINS ACTION EDIT OR CREATE

    UiHelper uiHelper;
    String mStrSelectedMediaUri = null;

    //  int flagValue = 0;   // flag value 0 means create new post and 1 means edit existing post


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_post);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();  // initializing
        setToolBar(); // setting toolbar
        setViews();  // # setting views
        setPostVisibilitySetting();  // # setting post visibility and comment setting

        // # removing image came from server, to check if new image is set or not. it must not be removed otherwise edit post will not work
        mPostData.setPostMediaUrl(null);

        // listeners
        mLayoutPostVisibilitySetting.setOnClickListener(onPostVisibilitySettingClicked);
        mImgAttachMedia.setOnClickListener(onAttachMediaClicked);
        mImgCamera.setOnClickListener(onImgCameraClicked);
        mBtnPost.setOnClickListener(onPostButtonClicked);  // creating new post or saving edited post

        GiraffeCompressor.init(this);
    }

    private void init() {
        mContext = this;
        uiHelper = new UiHelper();
        mPresenterNewsFeed = new PresenterNewsFeed(mContext, this);
        mPermissionHelper = new PermissionHelper(mContext);
        mToolbar = findViewById(R.id.toolbar);
        mTxtDate = findViewById(R.id.date);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mTxtPostVisibilitySetting = findViewById(R.id.txt_visibility);
        mImgAttachMedia = findViewById(R.id.img_attach_media);
        mImgCamera = findViewById(R.id.img_camera);
        mImgPost = findViewById(R.id.img_post);
        imgPostVisibilitySetting = findViewById(R.id.img_visibility_setting);
        mBtnPost = findViewById(R.id.new_feed_post_button);
        imgUserPic = findViewById(R.id.img_user_pic);
        mTxtUserName = findViewById(R.id.txt_user_name);
        mEditPostMessage = findViewById(R.id.edit_post_message);
        mLayoutPostVisibilitySetting = findViewById(R.id.layout_post_visibility_setting);
        news_feed_video_view = findViewById(R.id.news_feed_video_view);

        mTxtDate.setText(DateTimeHelper.getCurrentSystemDateTime());
        // getting data from bundle, if NewsFeed.PostData object has not been sent then it will be initialized as new object
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ACTION_CREATE_OR_EDIT = bundle.getInt(KEY_EDIT_OR_CREATE);

            mPostData = (NewsFeed.PostData) bundle.getSerializable(KEY_POST_DATA);
            if (mPostData == null)
                mPostData = new NewsFeed.PostData();
        } else
            mPostData = new NewsFeed.PostData();
    }


    // setting data of bundle and default data
    private void setViews() {
        if (mPostData == null)
            return;


        mTxtUserName.setText(SessionHelper.getInstance(this).getUser().getName()); // # setting user name
        UtilHelper.loadImageWithGlide(mContext, SessionHelper.getInstance(this).getUser().getUserPicUrl(), imgUserPic); // setting user pic

        if (mPostData == null)
            return;


        // if post is being edited
        if (ACTION_CREATE_OR_EDIT == ACTION_EDIT_POST) {
            // setting post image
            if (mPostData.getPostMediaUrl() == null || mPostData.getPostMediaUrl().equals("")) {
                mImgPost.setVisibility(View.GONE);
                news_feed_video_view.setVisibility(View.GONE);
                findViewById(R.id.unmute).setVisibility(View.GONE);
                findViewById(R.id.mute).setVisibility(View.GONE);
            } else {
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String extension = MimeTypeMap.getFileExtensionFromUrl(mPostData.getPostMediaUrl());

                if (mimeTypeMap != null && extension != null) {
                    String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
                    if (mimeType != null && mimeType.contains("video"))  // showing video
                    {
                        mImgPost.setVisibility(View.GONE);
                        news_feed_video_view.setVisibility(View.VISIBLE);
                        findViewById(R.id.unmute).setVisibility(View.GONE);
                        findViewById(R.id.mute).setVisibility(View.GONE);

                        showVideo();
                    } else // showing image
                    {
                        mImgPost.setVisibility(View.VISIBLE);
                        news_feed_video_view.setVisibility(View.GONE);
                        findViewById(R.id.unmute).setVisibility(View.GONE);
                        findViewById(R.id.mute).setVisibility(View.GONE);
                        UtilHelper.loadImageWithGlide(mContext, mPostData.getPostMediaUrl(), mImgPost);
                    }
                }
            }

            // checking if this is shared post(not created by the user who is editing this post), if yes then only shared text message can be edited
            if (mPostData.getPostSharedBy() != null && !mPostData.getPostSharedBy().isEmpty() && !mPostData.getPostSharedBy().equals(NewsFeed.POST_NOT_SHARED)) {
                mImgAttachMedia.setEnabled(false);
                mImgCamera.setEnabled(false);
                mEditPostMessage.setText(mPostData.getPostSharedMessage());
            } else
                mEditPostMessage.setText(mPostData.getPostMessage());

        }
    }

    private void showVideo() {

        Log.i(TAG, "showing video");
            /*MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            HashMap<String, String> map = new HashMap<String, String>();
            retriever.setDataSource(String.valueOf(Uri.parse(postData.getPostMediaUrl())), map);
            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            retriever.release();


            Log.i(TAG, "video height and width" + height + "&" + width);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            holder.mVideoView.setLayoutParams(params);*/

        news_feed_video_view.setVideoURI(Uri.parse(mPostData.getPostMediaUrl()));
        news_feed_video_view.requestFocus();
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        findViewById(R.id.mute).setVisibility(View.GONE);
        findViewById(R.id.unmute).setVisibility(View.VISIBLE);


        findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news_feed_video_view.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(news_feed_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news_feed_video_view.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                } else {
                    Snackbar.make(news_feed_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        news_feed_video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPreparedListener called");
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                news_feed_video_view.start();
                mp.setLooping(true);
            }
        });
    }


    // setting post visibility text, setting for post visibilty and comment allow permission
    private void setPostVisibilitySetting() {
        if (mPostData == null)
            return;


        // # for now sharing all post to connections only, if we need public and connection both then we will just remove this setting
        //   mPostData.setPostVisibility(NewsFeed.VISIBLE_TO_CONTACTS);


        //mPostData.getPostVisibility();
        switch (mPostData.getPostVisibility()) {
            case NewsFeed.VISIBLE_TO_PUBLIC:
                mTxtPostVisibilitySetting.setText(getResources().getString(R.string.news_feed_setting_public));
                mTxtPostVisibilitySetting.setTextColor(getResources().getColor(R.color.colorTheme));
                imgPostVisibilitySetting.setColorFilter(getResources().getColor(R.color.colorTheme));
                mPostData.setPostVisibility(NewsFeed.VISIBLE_TO_PUBLIC);
                break;

            case NewsFeed.VISIBLE_TO_CONTACTS:
                mTxtPostVisibilitySetting.setText(getResources().getString(R.string.news_feed_setting_connection));
                mTxtPostVisibilitySetting.setTextColor(getResources().getColor(R.color.colorTheme));
                imgPostVisibilitySetting.setColorFilter(getResources().getColor(R.color.colorTheme));
                mPostData.setPostVisibility(NewsFeed.VISIBLE_TO_CONTACTS);
                break;
        }
    }


    // selecting image from storage to post
    private void selectImage() {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_chat_choose_media);
        final ImageView selectImage = dialog.findViewById(R.id.choose_media_type_img_image);
        final ImageView selectVideo = dialog.findViewById(R.id.choose_media_type_img_video);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                selectMedia();
                //chooseMediaFromGallery(ActivityNewsFeedPost.REQUEST_CODE_IMAGE, "image/*");
            }
        });

        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                chooseMediaFromGallery(REQUEST_CODE_VIDEO, "video/*");
            }
        });

        dialog.show();
    }


    private void chooseMediaFromGallery(int requestCode, String type) {
        Log.i(TAG, "chooseMediaFromGallery() method called");
        String permissionMessage = "Without storage permission you can not upload file.";

        if (mPermissionHelper.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType(type);
            startActivityForResult(intent, requestCode);
        } else {
            Log.e(TAG, "storage permission is not granted in selectMedia");
            mPermissionHelper.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, permissionMessage);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        Log.i(TAG, "onActivityResult() method called ");


        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = UCrop.getOutput(data);
                showImage(uri);
            }
        } else if (requestCode == UiHelper.PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Uri sourceUri = data.getData();
                File file = getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                openCropActivity(sourceUri, destinationUri);
            } catch (Exception e) {
                uiHelper.toast(this, "Please select another image");
            }
        }

        // if cropping image gets failed
        if (resultCode == UCrop.RESULT_ERROR)
            Log.e(TAG, "Error: uCrop result Uri, Error = " + UCrop.EXTRA_ERROR);


      /*  super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || data != null || data.getData() == null)
        {
            Log.e(TAG,"result is not ok or data is null or data.getData() is null in onActivityResult()");
            return;
        }
*/

       /* // getting selected post image and staring other activity to crop it
        if (resultCode == RESULT_OK && data != null && requestCode == REQUEST_CODE_IMAGE)
            cropImage(data.getData());*/


            // getting selected post image, this was used to crop image
       /* else if (requestCode == UCrop.REQUEST_CROP)
            onImageCropped(data);*/


            // setting post visibility setting data after coming from post setting page
        else if (resultCode == RESULT_OK && data != null && requestCode == REQUEST_CODE_POST_SETTING_ACTIVITY)
            onPostVisibilitySettingChanged(data);


            // getting image captured from camera
        else if (resultCode == RESULT_OK && data != null && requestCode == ConstHelper.RequestCode.CAMERA_IMAGE && data.getExtras() != null)
            onImageCaptured(data);

            //getting video selected from gallery
        else if (resultCode == RESULT_OK && data != null && requestCode == REQUEST_CODE_VIDEO) {
            showLoaderInActivity();
            onVideoUploaded(data);
        }

        //getting video captured by camera
        else if (resultCode == RESULT_OK && data != null && requestCode == ConstHelper.RequestCode.CAMERA_VIDEO) {
            showLoaderInActivity();
            onVideoCaptured(data);

        }

    }


    private void onVideoCaptured(final Intent data) {

        Log.i(TAG, "Compress video captured by camera");

        // compress video

        GiraffeCompressor.create()
                .input(getRealPathFromURI(data.getData()))
                .output(remaneFile(data.getData()))
                .bitRate(2073600)
                .resizeFactor(Float.parseFloat(String.valueOf(1.0)))
                .ready()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GiraffeCompressor.Result>() {
                    @Override
                    public void onCompleted() {
                        dismissLoaderInActivity();
                        Log.i(TAG, "Video compressed successfully");

                        // show video
                        Log.i(TAG, "Show video captured by camera");
                        showTheVideoCapturedByCamera(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoaderInActivity();
                        e.printStackTrace();
                        Log.i(TAG, "Error while compressing video : " + e.getMessage());

                        // show video
                        Log.i(TAG, "Show video captured by camera");
                        showTheVideoCapturedByCamera(data);
                    }

                    @Override
                    public void onNext(GiraffeCompressor.Result s) {
                        mPostData.setPostMediaUrl(String.valueOf(new File(s.getOutput())));
                    }
                });
    }

    private void showTheVideoCapturedByCamera(Intent data) {

        news_feed_video_view.setVisibility(View.VISIBLE);
        mImgPost.setVisibility(View.GONE);

        mSelectedImagePath = FileHelper.getFilePath(mContext, data.getData());
        news_feed_video_view.setVideoURI(data.getData());

        mPostData.setPostMediaUrl(mSelectedImagePath);

        news_feed_video_view.requestFocus();
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        findViewById(R.id.unmute).setVisibility(View.VISIBLE);
        findViewById(R.id.mute).setVisibility(View.GONE);

        findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news_feed_video_view.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(news_feed_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news_feed_video_view.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                } else {
                    Snackbar.make(news_feed_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        news_feed_video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                news_feed_video_view.start();
                mp.setLooping(true);
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private File remaneFile(Uri uri) {
        File dir = Environment.getExternalStorageDirectory();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        File from = new File(dir, cursor.getString(column_index));
        File to = new File(dir, "newCompressed.mp4");
        if (dir.exists()) {
            if (from.exists())
                from.renameTo(to);
        }
        return from;
    }

    private void onVideoUploaded(Intent data) {

        Log.i(TAG, "Video selected from gallery");

        news_feed_video_view.setVisibility(View.VISIBLE);
        mImgPost.setVisibility(View.GONE);

        mSelectedImagePath = FileHelper.getFilePath(mContext, data.getData());
        news_feed_video_view.setVideoURI(Uri.parse(mSelectedImagePath));

        mPostData.setPostMediaUrl(mSelectedImagePath);

        news_feed_video_view.requestFocus();
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        findViewById(R.id.unmute).setVisibility(View.VISIBLE);
        findViewById(R.id.mute).setVisibility(View.GONE);
        news_feed_video_view.start();
        dismissLoaderInActivity();

        findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news_feed_video_view.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(news_feed_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news_feed_video_view.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                } else {
                    Snackbar.make(news_feed_video_view, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    // # cropping selected image
    private void cropImage(Uri uri) {
        int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, (int) getResources().getDimension(R.dimen.newsFeedPostImageHeight));
        int croppedImageWidth = UtilHelper.getScreenWidth(mContext);
        try {
            UCrop.of(uri, Uri.parse(FileHelper.getFilePath(mContext, uri)))
                    .withAspectRatio(16, 9)
                    .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                    .start(ActivityNewsFeedPost.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* // # called after cropping selected image, setting image path to mPostData object
    private void onImageCropped(Intent intentData) {
        mImgPost.setVisibility(View.VISIBLE);
        news_feed_video_view.setVisibility(View.GONE);
        findViewById(R.id.unmute).setVisibility(View.GONE);
        findViewById(R.id.mute).setVisibility(View.GONE);
        final Uri resultUri = UCrop.getOutput(intentData);
        String mediaPath = FileHelper.getFilePath(mContext, resultUri);
        mPostData.setPostMediaUrl(mediaPath);   // setting media in NewsFee.Postdata object
        UtilHelper.loadImageWithGlide(mContext, mPostData.getPostMediaUrl(), mImgPost);
    }
*/

    // called after image has been captured from camera
    private void onImageCaptured(Intent intentData) {
        if (intentData.getExtras() == null)
            return;

        Bitmap bitmap = (Bitmap) intentData.getExtras().get("data");
        if (bitmap == null)
            return;

        //   mImgPost.setVisibility(View.VISIBLE);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String imageUri = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "athlete_news_feed_post", null);
        cropImage(Uri.parse(imageUri));

        //   mPostData.setPostMediaUrl(imagePath);
        // UtilHelper.loadImageWithGlide(mContext, mPostData.getPostMediaUrl(), mImgPost);
    }

    // called after coming from ActivityNewsFeedSettings
    private void onPostVisibilitySettingChanged(Intent intentData) {
        if (intentData == null)
            return;

        NewsFeed.PostData postData = (NewsFeed.PostData) intentData.getSerializableExtra(ActivityNewsFeedSettings.KEY_POST_DATA);

        if (mPostData != null && postData != null) {
            mPostData.setPostVisibility(postData.getPostVisibility());
            mPostData.setPostCommentPermission(postData.getPostCommentPermission());
            setPostVisibilitySetting();
           setViewsAfterSelectingGroup();
        }
    }

    private void setViewsAfterSelectingGroup() {

        if (mPostData.getPostMediaUrl() == null || mPostData.getPostMediaUrl().equals("")) {
            mImgPost.setVisibility(View.GONE);
            news_feed_video_view.setVisibility(View.GONE);
            findViewById(R.id.unmute).setVisibility(View.GONE);
            findViewById(R.id.mute).setVisibility(View.GONE);
        } else {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = MimeTypeMap.getFileExtensionFromUrl(mPostData.getPostMediaUrl());

            if (mimeTypeMap != null && extension != null) {
                String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
                if (mimeType != null && mimeType.contains("video"))  // showing video
                {
                    mImgPost.setVisibility(View.GONE);
                    news_feed_video_view.setVisibility(View.VISIBLE);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.GONE);

                    showVideo();
                } else // showing image
                {
                    mImgPost.setVisibility(View.VISIBLE);
                    news_feed_video_view.setVisibility(View.GONE);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                    UtilHelper.loadImageWithGlide(mContext, mPostData.getPostMediaUrl(), mImgPost);
                }
            }
        }

        // checking if this is shared post(not created by the user who is editing this post), if yes then only shared text message can be edited
        if (mPostData.getPostSharedBy() != null && !mPostData.getPostSharedBy().isEmpty() && !mPostData.getPostSharedBy().equals(NewsFeed.POST_NOT_SHARED)) {
            mImgAttachMedia.setEnabled(false);
            mImgCamera.setEnabled(false);
            mEditPostMessage.setText(mPostData.getPostSharedMessage());
        } else
            mEditPostMessage.setText(mPostData.getPostMessage());

    }

    private View.OnClickListener onPostButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            UtilHelper.hideKeyboard(ActivityNewsFeedPost.this);

            if (!UtilHelper.isConnectedToInternet(mContext)) // checking internet connection
            {
                DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.title_no_connection), getString(R.string.message_no_connection));
                return;
            }

            if (mPresenterNewsFeed == null || mPostData == null)
                return;

            String postMessage = mEditPostMessage.getText().toString().trim();

            if (ACTION_CREATE_OR_EDIT == ACTION_EDIT_POST) {
                Log.i(TAG, "saving edited post.......");
                showLoaderOrChangeTitle("Updating...");
                mPostData.setPostMessage(postMessage);
                mPresenterNewsFeed.editPost(mPostData);
            } else {  // # creating new post
                Log.i(TAG, "creating new post.......");
                showLoaderOrChangeTitle("Posting...");
                mPostData.setPostMessage(postMessage);
                mPresenterNewsFeed.createPost(mPostData);
            }
        }
    };


    private View.OnClickListener onImgCameraClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPermissionHelper.isPermissionGranted(Manifest.permission.CAMERA)) {
                //permission to write captured image
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_chat_choose_media);
                    final ImageView selectImage = dialog.findViewById(R.id.choose_media_type_img_image);
                    final ImageView selectVideo = dialog.findViewById(R.id.choose_media_type_img_video);
                    selectImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, ConstHelper.RequestCode.CAMERA_IMAGE);
                        }
                    });

                    selectVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            startActivityForResult(intent, ConstHelper.RequestCode.CAMERA_VIDEO);
                        }
                    });

                    dialog.show();

                } else {
                    String message = "Please give permission to select media from device storage...";
                    mPermissionHelper.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, message);
                }
            } else {
                String message = "Please give permission to capture image or video from camera...";
                mPermissionHelper.requestPermission(Manifest.permission.CAMERA, message);
            }
        }
    };


    private View.OnClickListener onAttachMediaClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mImgPost.setVisibility(View.VISIBLE);
            selectImage();
        }
    };


    private View.OnClickListener onPostVisibilitySettingClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(ActivityNewsFeedPost.this, ActivityNewsFeedSettings.class);
            i.putExtra(KEY_POST_DATA, mPostData);
            //  i.putExtra(ActivityNewsFeedSettings.KEY_SETTING, mPostVisibility);
            //  i.putExtra(ActivityNewsFeedSettings.KEY_COMMENT_STATUS, mCommentAllowPermission);
            //  i.putExtra("flagSettings","2");

            startActivityForResult(i, REQUEST_CODE_POST_SETTING_ACTIVITY);
        }
    };


    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.post);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
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


    // # methods of presenter's View class starts
    @Override
    public void onPostCreated(NewsFeed.PostData postData) {
        startActivity(new Intent(ActivityNewsFeedPost.this, ActivityNewsFeed.class));
        finish();  // destroying this activity
    }

    @Override
    public void onPostEdited(NewsFeed.PostData postData) {
        hideLoader();
        startActivity(new Intent(ActivityNewsFeedPost.this, ActivityNewsFeed.class));
        finish();
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


    @Override
    public void onFailed(int failType, String message) {
        hideLoader();
        String title = getString(R.string.message_failed);
        switch (failType) {
            case NewsFeed.FAILED_CREATING_POST:
                title = "Failed creating post...";
                break;

            case NewsFeed.FAILED_EDITING_POST:
                title = "Failed Editing post...";
                break;
        }

        if (message == null || message.isEmpty())
            DialogHelper.showSimpleCustomDialog(mContext, title);
        else
            DialogHelper.showSimpleCustomDialog(mContext, title, message);

    }

    @Override
    public void onPostShared(NewsFeed.PostData postData) {

    }




    // to select media from device
    private void selectMedia() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (uiHelper.checkSelfPermissions(this)) {
                Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pictureIntent.setType("image/*");
                pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
                pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), UiHelper.PICK_IMAGE_GALLERY_REQUEST_CODE);

            }
    }



    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        System.out.println(storageDir.getAbsolutePath());
        if (storageDir.exists())
            System.out.println("File exists");
        else
            System.out.println("File not exists");
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }





    private void showImage(Uri imageUri) {
        try {

            mImgPost.setVisibility(View.VISIBLE);
            news_feed_video_view.setVisibility(View.GONE);
            findViewById(R.id.unmute).setVisibility(View.GONE);
            findViewById(R.id.mute).setVisibility(View.GONE);
            // final Uri resultUri = UCrop.getOutput(intentData);
            String mediaPath = FileHelper.getFilePath(mContext, imageUri);
            mPostData.setPostMediaUrl(mediaPath);   // setting media in NewsFee.Postdata object
            UtilHelper.loadImageWithGlide(mContext, mPostData.getPostMediaUrl(), mImgPost);
          /*
            mStrSelectedMediaUri = FileHelper.getFilePath(mContext, imageUri);
            if (mAdapter != null)
            {
                int updatingItemPosition = mAdapter.getUpdatingItemPosition();
                if (updatingItemPosition == ConstHelper.NOT_FOUND)
                    return;

                Survey.SurveyData.FreeResponse.Questions surveyQuestions = mAdapter.getSurveyQusetions().get(updatingItemPosition);
                surveyQuestions.setQuestionImg(mStrSelectedMediaUri);
                mAdapter.updateItem(updatingItemPosition, surveyQuestions);


            }*/

        } catch (Exception e) {
            uiHelper.toast(this, "Please select different profile picture.");
        }
    }


    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, (int) getResources().getDimension(R.dimen.newsFeedPostImageHeight));
        int croppedImageWidth = UtilHelper.getScreenWidth(mContext);
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(16,9)
                .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                .start(this);
    }

    // # methods of presenter's View class ends
}
