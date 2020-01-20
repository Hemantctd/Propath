package org.ctdworld.propath.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.tcking.giraffecompressor.GiraffeCompressor;
import com.google.android.material.snackbar.Snackbar;
import com.yalantis.ucrop.UCrop;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNutrition;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.ImageHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Nutrition;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNutrition;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class ActivityNutritionPost extends BaseActivity implements View.OnClickListener, ContractNutrition.View {

    private static final String TAG = ActivityNutritionPost.class.getSimpleName();
    public static final String POST_TYPE = "post_type";
    public static final String TYPE_EDIT = "edit";
    public static final String POST_SHARE_TO = "groups";
    private static final int REQUEST_PERM_WRITE_STORAGE = 104;
    private int REQUEST_CODE_IMAGE = 100;
    private static final int REQUEST_CODE_VIDEO = 200;


    String mSelectedImagePath;
    Toolbar mToolbar;
    Context mContext;
    ProgressBar progressBar;
    ImageView profile_image, nutrition_post_image, nutrition_attach_icon, img_camera;
    VideoView mNutritionPostVideo;
    TextView profile_name, mTxtToolbarTitle;
    EditText nutrition_post_text;
    Button nutrition_post_button;
    ContractNutrition.Presenter mPresenter;
    String mTypePostOrEdit, media;
    ImageHelper imageHelper;
    int sendImageStatus = 0;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    PermissionHelper mPermissionHelper;
    FrameLayout mFrameLayout;
    //Uri to store the image uri
    private Uri filePath;

    private static final int REQUSET_PERM_WRITE_STORAGE = 104;
    Nutrition nutrition_data;
    LinearLayout groups_layout;
    String group_id;

    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView mYoutubePlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_post);
        init();
        setToolBar();
    }

    public void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);

        img_camera = findViewById(R.id.img_camera);
        imageHelper = new ImageHelper(mContext);
        mPermissionHelper = new PermissionHelper(mContext);
        mPresenter = new PresenterNutrition(mContext, this);
        groups_layout = findViewById(R.id.groups_layout);
        profile_image = findViewById(R.id.nutrition_profile_icon);
        profile_name = findViewById(R.id.nutrition_profile_name);
        progressBar = findViewById(R.id.progress_bar);
        nutrition_post_text = findViewById(R.id.nutrition_post_data);
        nutrition_post_image = findViewById(R.id.nutrition_image_post);
        mNutritionPostVideo = findViewById(R.id.nutrition_video_view);
        nutrition_post_button = findViewById(R.id.nutrition_post_button);
        mFrameLayout = findViewById(R.id.frame_layout);
        nutrition_attach_icon = findViewById(R.id.nutrition_attach_icon);
        nutrition_post_button.setOnClickListener(this);
        nutrition_attach_icon.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        mYoutubePlayer = findViewById(R.id.youtube_player);

        profile_name.setText(SessionHelper.getInstance(mContext).getUser().getName());
        int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
        Glide.with(mContext)
                .load(SessionHelper.getInstance(mContext).getUser().getUserPicUrl())
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picDimen, picDimen))
                .into(profile_image);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityProfileView.class);
                intent.putExtra(ConstHelper.Key.ID, SessionHelper.getInstance(mContext).getUser().getUserId());
                mContext.startActivity(intent);
            }
        });

        Intent i = getIntent();
        group_id = i.getStringExtra(POST_SHARE_TO);
        if (group_id != null) {
            Log.d(TAG, "group_id..." + group_id);
        }


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mTypePostOrEdit = mBundle.getString(POST_TYPE);
            nutrition_data = (Nutrition) mBundle.getSerializable("nutrition_data");
            media = mBundle.getString("media");

        }

        if (nutrition_data != null) {
            Log.d(TAG, "url_image" + media);

            nutrition_post_text.setText(nutrition_data.getTitle());
            if (!media.equals("")) {

                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String extension = MimeTypeMap.getFileExtensionFromUrl(media);

                if (mimeTypeMap != null && extension != null) {
                    String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
                    if (mimeType != null && mimeType.contains("video"))  // showing video
                    {
                        nutrition_post_image.setVisibility(View.GONE);
                        mNutritionPostVideo.setVisibility(View.VISIBLE);
                        findViewById(R.id.mute).setVisibility(View.GONE);
                        findViewById(R.id.unmute).setVisibility(View.GONE);

                        showVideo();
                    } else // showing image
                    {
                        nutrition_post_image.setVisibility(View.VISIBLE);
                        mNutritionPostVideo.setVisibility(View.GONE);
                        findViewById(R.id.mute).setVisibility(View.GONE);
                        findViewById(R.id.unmute).setVisibility(View.GONE);

                        int picDimen1 = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
                        Glide.with(mContext)
                                .load(media)
                                .apply(new RequestOptions().error(R.drawable.ic_profile))
                                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                                .apply(new RequestOptions().override(picDimen1, picDimen1))
                                .into(nutrition_post_image);
                    }
                }

                /*if (UtilHelper.isYoutubeUrl(nutrition_data.getMedia_url())) {
                    mImgPost.setVisibility(View.GONE);
                    mNutritionSharePostVideo.setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    mYoutubePlayer.setVisibility(View.VISIBLE);
                    showYouTubeVideo();
                }*/
            }
        }

        groups_layout.setOnClickListener(this);


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

        mNutritionPostVideo.setVideoURI(Uri.parse(media));
        mNutritionPostVideo.requestFocus();
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        findViewById(R.id.mute).setVisibility(View.GONE);
        findViewById(R.id.unmute).setVisibility(View.VISIBLE);


        findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNutritionPostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(mNutritionPostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNutritionPostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                } else {
                    Snackbar.make(mNutritionPostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mNutritionPostVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPreparedListener called");
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                mNutritionPostVideo.start();
                mp.setLooping(true);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.nutrition_attach_icon:
                selectImage();
                break;

            case R.id.groups_layout:
                Intent intent = new Intent(mContext, ActivityNutritionGroup.class);
                intent.putExtra("flag", "2");
                intent.putExtra("nutritiondata", nutrition_data);
                startActivity(intent);

                break;

            case R.id.nutrition_post_button:
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(nutrition_post_button.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

//                {//                if (mTypePostOrEdit.equals(TYPE_EDIT))
//                    editPost();
//                }
//                else
//                {
                postOrEditNutrition();
                //}

                break;

            case R.id.img_camera:
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
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, ConstHelper.RequestCode.CAMERA_IMAGE);
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
                break;
        }
    }

    public void postOrEditNutrition() {
        postNutritionData();
    }


    public void postNutritionData() {

        // showLoader("Posting...");
        if (UtilHelper.isConnectedToInternet(mContext)) {
            //getting name for the image

            String name = nutrition_post_text.getText().toString().trim();

            //getting the actual path of the image
            // String path = getPath(filePath);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                //  Log.i(TAG, "filePath = " + filePath);
                Log.i(TAG, "mSelectedImagePath path is null while posting = " + mSelectedImagePath);

                //Creating a multi part request
                MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, RemoteServer.URL);
                progressBar.setVisibility(View.VISIBLE);

                if (mSelectedImagePath != null && !mSelectedImagePath.isEmpty())
                    multipartUploadRequest.addFileToUpload(mSelectedImagePath, "file"); //Adding file

                multipartUploadRequest.addParameter("group_id[]", Arrays.toString(new String[]{group_id}));
                Log.d(TAG, "groups_idsssssss " + Arrays.toString(new String[]{group_id}));
                multipartUploadRequest.addParameter("title", name); //Adding text parameter to the request
                multipartUploadRequest.addParameter("add_nutrition_feed", "1");
                multipartUploadRequest.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
                multipartUploadRequest.addParameter("comment_status", "1");


                multipartUploadRequest.setNotificationConfig(new UploadNotificationConfig());
                multipartUploadRequest.setMaxRetries(2);
                multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                    @Override
                    public void onProgress(Context context, UploadInfo uploadInfo) {
                    }

                    @Override
                    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                        DialogHelper.showSimpleCustomDialog(context, "Failed...");
                    }

                    @Override
                    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                        Toast.makeText(context, "File Uploaded Successfully ...", Toast.LENGTH_SHORT).show();
                        String response = serverResponse.getBodyAsString();
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject serverResponse1 = new JSONObject(response);
                            if (serverResponse1.get("res").toString().equals("1")) {
                                startActivity(new Intent(mContext, ActivityNutrition.class));
                                 /*   setResult(RESULT_OK);
                                    finish();*/
                            } else
                                DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_failed));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(Context context, UploadInfo uploadInfo) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Cancelled...", Toast.LENGTH_SHORT).show();

                    }
                })
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
            }
        } else {
            progressBar.setVisibility(View.GONE);
            DialogHelper.showSimpleCustomDialog(mContext, "No Connection...");
        }
    }


    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText("Food Diary");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    // get all fields text for sending to server
    private Nutrition getNutrition() {
        String path = getPath(filePath);

        Log.d(TAG, "path value...." + path);
        Nutrition nutritionData = new Nutrition();
        nutritionData.setTitle(nutrition_post_text.getText().toString().trim());
        nutritionData.setCommentStatus("1");

        if (path != null && !path.isEmpty())
            nutritionData.setFile(path);


        return nutritionData;
    }


    private void selectImage() {
        // setting listener on mImgAddMedia to open Dialog to choose media type and get selected files(image or video)
      /*  nutrition_attach_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"mImgAddMedia clicked");
*/
               /* if (mPermissionHelper.isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    */
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_chat_choose_media);
        final ImageView selectImage = dialog.findViewById(R.id.choose_media_type_img_image);
        final ImageView selectVideo = dialog.findViewById(R.id.choose_media_type_img_video);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                            nutrition_post_image.setVisibility(View.VISIBLE);
                mNutritionPostVideo.setVisibility(View.GONE);
                dialog.dismiss();
                chooseMediaFromGallery(REQUEST_CODE_IMAGE, "image/*");
            }
        });

        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nutrition_post_image.setVisibility(View.GONE);
                nutrition_post_image.setVisibility(View.GONE);

                dialog.dismiss();
                chooseMediaFromGallery(REQUEST_CODE_VIDEO, "video/*");
            }
        });
        dialog.show();
        //  }
              /*  else
                {
                    String message = "Please give permission to select media from device storage...";
                    mPermissionHelper.requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,message);
                }
            }*/
        /*    });*/


       /* final CharSequence[] items = {"Select Image from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (items[i].equals("Select Image from Gallery")) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ActivityNutritionPost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUSET_PERM_WRITE_STORAGE);
                    } else {
                        choosePictureFromGallery();

                    }

                }


            }
        });
        builder.show();*/
    }


    private void chooseMediaFromGallery(int requestCode, String type) {
        Log.i(TAG, "chooseMediaFromGallery() method called");
        String permissionMessage = "Without storage permission you can not upload file.";

       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);*/
        if (mPermissionHelper.isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType(type);
            startActivityForResult(intent, requestCode);
        } else {
            Log.e(TAG, "storage permission is not granted in selectMedia");
            mPermissionHelper.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, permissionMessage);
        }
    }


    //    //method to get the file path from uri
    public String getPath(Uri uri) {

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            return path;

        } else
            return null;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {


        int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, 230);
        int croppedImageWidth = UtilHelper.getScreenWidth(mContext);
        Log.i(TAG, "uCrop width = " + croppedImageWidth);
        Log.i(TAG, "uCrop height = " + croppedImageHeight);

        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult() method called ");

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            mSelectedImagePath = FileHelper.getFilePath(mContext, resultUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                if (bitmap != null)
                    nutrition_post_image.setVisibility(View.VISIBLE);
                nutrition_post_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "uCrop result Uri = " + resultUri + " , path = " + mSelectedImagePath);


        } else if (resultCode == UCrop.RESULT_ERROR) {
            Log.e(TAG, "Error: uCrop result Uri, Error = " + UCrop.EXTRA_ERROR);

        }


     /*   if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                nutrition_post_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mSelectedImagePath = FileHelper.getFilePath(mContext, data.getData());
            mNutritionPostVideo.setVisibility(View.GONE);
            nutrition_post_image.setVisibility(View.VISIBLE);
            // Bitmap bitmap = null;
            try {
                //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                nutrition_post_image.setVisibility(View.VISIBLE);
                UCrop.of(data.getData(), Uri.parse(mSelectedImagePath + "uCrop"))
                        .withAspectRatio(16, 9)
                        .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                        .start(ActivityNutritionPost.this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_VIDEO) {
            showLoaderOrChangeTitle("Please wait..");
            mNutritionPostVideo.setVisibility(View.VISIBLE);
            nutrition_post_image.setVisibility(View.GONE);

            Log.i(TAG, "video selected from gallery");

            mNutritionPostVideo.setVisibility(View.VISIBLE);
            nutrition_post_image.setVisibility(View.GONE);

            if (data != null) {
                mSelectedImagePath = FileHelper.getFilePath(mContext, data.getData());
            }
            mNutritionPostVideo.setVideoURI(Uri.parse(mSelectedImagePath));

            mNutritionPostVideo.requestFocus();
            AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            findViewById(R.id.unmute).setVisibility(View.VISIBLE);
            findViewById(R.id.mute).setVisibility(View.GONE);
            mNutritionPostVideo.start();
            hideLoader();

            findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNutritionPostVideo.isPlaying()) {
                        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                        findViewById(R.id.unmute).setVisibility(View.GONE);
                        findViewById(R.id.mute).setVisibility(View.VISIBLE);
                    } else {
                        Snackbar.make(mNutritionPostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNutritionPostVideo.isPlaying()) {
                        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                        findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                        findViewById(R.id.mute).setVisibility(View.GONE);
                    } else {
                        Snackbar.make(mNutritionPostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (requestCode == ConstHelper.RequestCode.CAMERA_IMAGE && data != null && data.getExtras() != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mNutritionPostVideo.setVisibility(View.GONE);
            nutrition_post_image.setVisibility(View.VISIBLE);
            if (bitmap == null)
                return;

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String selectImagePath = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "athlete_news_feed_post", null);
            mSelectedImagePath = FileHelper.getFilePath(mContext, Uri.parse(selectImagePath));


            // nutrition_post_image.setVisibility(View.VISIBLE);

            try {
                //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                UCrop.of(data.getData(), Uri.parse(mSelectedImagePath + "uCrop"))
                        .withAspectRatio(16, 9)
                        .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                        .start(ActivityNutritionPost.this);

            } catch (Exception e) {
                e.printStackTrace();
            }

            int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));

            Glide.with(mContext)
                    .load(mSelectedImagePath)
                    .apply(new RequestOptions().error(R.drawable.ic_profile))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                    .apply(new RequestOptions().override(picDimen, picDimen))
                    .into(nutrition_post_image);


            // news_feed_images.setImageBitmap(bitmap);
           /* try {
                //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                UCrop.of(data.getData(), Uri.parse(mSelectedImagePath+"uCrop"))
                        .withAspectRatio(16, 9)
                        .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                        .start(ActivityNewsFeedPost.this);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else if (resultCode == RESULT_OK && data != null && requestCode == ConstHelper.RequestCode.CAMERA_VIDEO) {
            showLoaderOrChangeTitle("Please wait..");
            onVideoCaptured(data);
        }
    }

    private void onVideoCaptured(final Intent data) {

        Log.i(TAG, "Compressing video captured by camera");

        // Compress video

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
                        hideLoader();
                        Log.i(TAG, "Video compressed successfully");

                        Log.i(TAG, "Show video captured by camera");
                        //show video on video view
                        showTheCapturedVideo(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideLoader();
                        Log.i(TAG, "Error while compressing video : " + e.getMessage());

                        Log.i(TAG, "Show video captured by camera");
                        //show video on video view
                        showTheCapturedVideo(data);
                    }

                    @Override
                    public void onNext(GiraffeCompressor.Result s) {
                        mSelectedImagePath = String.valueOf(new File(s.getOutput()));
                    }
                });
    }

    private void showTheCapturedVideo(Intent data) {

        mNutritionPostVideo.setVisibility(View.VISIBLE);
        nutrition_post_image.setVisibility(View.GONE);

        mSelectedImagePath = FileHelper.getFilePath(mContext, data.getData());
        mNutritionPostVideo.setVideoURI(data.getData());

        mNutritionPostVideo.requestFocus();
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        findViewById(R.id.unmute).setVisibility(View.VISIBLE);
        findViewById(R.id.mute).setVisibility(View.GONE);
        mNutritionPostVideo.start();

        findViewById(R.id.unmute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNutritionPostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    findViewById(R.id.unmute).setVisibility(View.GONE);
                    findViewById(R.id.mute).setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(mNutritionPostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNutritionPostVideo.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                    findViewById(R.id.mute).setVisibility(View.GONE);
                } else {
                    Snackbar.make(mNutritionPostVideo, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mNutritionPostVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                findViewById(R.id.unmute).setVisibility(View.VISIBLE);
                findViewById(R.id.mute).setVisibility(View.GONE);
                mNutritionPostVideo.start();
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


    @Override
    public void onGetReceivedNutritionComments(List<Nutrition> nutritionList) {

    }

    @Override
    public void onGetReceivedNutritionData(List<Nutrition> nutritionList) {

    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg);
    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg);

    }

    @Override
    public void onLikeDislike(int position, Nutrition nutrition) {

    }


    @Override
    public void onShowProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {

    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext, message);
    }


    public void editPost() {
        String name = nutrition_post_text.getText().toString().trim();
        String path = getPath(filePath);
        Log.d(TAG, "  path :   ...... " + path);

        try {
            String uploadId = UUID.randomUUID().toString();
            Log.i(TAG, "filePath = " + filePath);
            progressBar.setVisibility(View.VISIBLE);

            if (path == null) {
                path = "";
            } else
                sendImageStatus = 1;

            // Creating a multi part request
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, RemoteServer.URL);

            if (!path.isEmpty())
                multipartUploadRequest.addFileToUpload(path, "file"); //Adding file

            multipartUploadRequest.addParameter("title", name); //Adding text parameter to the request
            multipartUploadRequest.addParameter("edit_nutrition_feed", "1");
            multipartUploadRequest.addParameter("comment_status", "1");
            multipartUploadRequest.addParameter("post_id", nutrition_data.getPost_id());
            multipartUploadRequest.addParameter("image_status", String.valueOf(sendImageStatus));

            multipartUploadRequest.setNotificationConfig(new UploadNotificationConfig());
            multipartUploadRequest.setMaxRetries(2);
            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    DialogHelper.showSimpleCustomDialog(context, "Failed...");
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    Log.i(TAG, "file Edited successfully");
                    Log.d(TAG, "serverResponse" + serverResponse);

                    Toast.makeText(context, "File Edited Successfully ...", Toast.LENGTH_SHORT).show();
                    String response = serverResponse.getBodyAsString();
                    Log.d(TAG, "response : " + response);
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject serverResponse1 = new JSONObject(response);
                        if (serverResponse1.get("res").toString().equals("1")) {
                            Toast.makeText(mContext, serverResponse1.get("result").toString(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(mContext, ActivityNutrition.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Toast.makeText(context, "Cancelled...", Toast.LENGTH_SHORT).show();

                }
            });
            multipartUploadRequest.startUpload(); //Starting the upload


        } catch (Exception exc) {
            Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
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

}
