package org.ctdworld.propath.activity;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterImageSlider;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.FullSizeImageVideo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityChatShowImageAndVideo extends AppCompatActivity
{
    private final String TAG = ActivityChatShowImageAndVideo.class.getSimpleName();

    Context mContext;
  //  ImageView mImage;
  //  VideoView mVideo;
   // MediaController mMediaController;
    DialogLoader mDialogLoader;

    Toolbar mToolbar;
    TextView mToolbarTitle;




    // keys to set argument
    public static final String KEY_SELECTED_IMAGE_POSITION = "position";
    public static final String KEY_URL_LIST = "user list";

    private ArrayList<FullSizeImageVideo> mListData = new ArrayList<>();

    int gallerySelectedImagePosition;

    ViewPager viewPager;





    public ActivityChatShowImageAndVideo() {

    }  // Required empty public constructor

   // public static final String KEY_TYPE_IMAGE_OR_VIDEO = "type";
   // public static final String KEY_URL = "url";
    public static final String KEY_TOOLBAR_TITLE = "toolbar";

  //  private String mTypeImageOrVideo = "";
  //  private String mUrl =  null;
    private String mStrToolbarTitle = "Image";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_show_imag_and_video);

        mContext = this;
       /* mImage = findViewById(R.id.dialog_chat_show_image);
        mVideo = findViewById(R.id.dialog_chat_show_video);*/
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        // mMediaController = view.findViewById(R.id.dialog_chat_media_controller);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
                gallerySelectedImagePosition = bundle.getInt(KEY_SELECTED_IMAGE_POSITION);
                mListData = bundle.getParcelableArrayList(KEY_URL_LIST);

           /* mTypeImageOrVideo = bundle.getString(KEY_TYPE_IMAGE_OR_VIDEO);
            mUrl = bundle.getString(KEY_URL);
            mStrToolbarTitle = bundle.getString(KEY_TOOLBAR_TITLE);
*/
//            Log.i(TAG,"toolbar title");
            Log.i(TAG,"selected item position = "+gallerySelectedImagePosition);


        }
        else
            Log.e(TAG,"bundle is null in onCreate() method ");




        viewPager = findViewById(R.id.viewpager_full_image);  // initializing viewPager object
        // creating adapter object
        AdapterImageSlider adapter = new AdapterImageSlider(mContext, mListData, viewPager);
        viewPager.setAdapter(adapter);  // setting adapter to viewPager


        if (gallerySelectedImagePosition >= 0) { // checking if selectedPosition is not less than 0
            viewPager.setCurrentItem(gallerySelectedImagePosition); // selected image in gallery will be shown first
        }



       /* switch (mTypeImageOrVideo)
        {
            case Chat.MESSAGE_TYPE_IMAGE:
                mStrToolbarTitle = "Image";
                showImage();
                break;

            case Chat.MESSAGE_TYPE_VIDEO:
               // mDialogLoader.show(getSupportFragmentManager(),"");
                showVideo();
                mStrToolbarTitle = "Video";
                break;
        }*/

        //setting toolbar
        setToolbar();
    }



    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(mStrToolbarTitle);  // setting toolbar title

        if (getSupportActionBar() == null)
            return;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    // showing video
    private void showVideo()
    {
       /* mImage.setVisibility(View.GONE);
        mVideo.setVisibility(View.VISIBLE);*/



       // Log.i(TAG,"video url = "+mUrl);
       /* if (mUrl == null)
        {
            Log.e(TAG,"mUrl is null in showVideo() method");
            return;
        }*/

       // VideoView videoView = (VideoView) findViewById(R.id.video_view);
      //  mVideo.setVideoPath(mUrl).getPlayer().start();
        /*mVideo.setPlayerListener(new tcking.github.com.giraffeplayer2.MediaController() {
            @Override
            public void bind(VideoView videoView) {
                Log.i(TAG," bind()");
            }

            @Override
            public void onPrepared(GiraffePlayer giraffePlayer)
            {
                Log.i(TAG," onPrepared()");
            }

            @Override
            public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {
                Log.i(TAG," onBufferingUpdate()");
            }

            @Override
            public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
                Log.i(TAG," onInfo()");

                return false;
            }

            @Override
            public void onCompletion(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onCompletion()");

            }

            @Override
            public void onSeekComplete(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onSeekComplete()");

            }

            @Override
            public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
                Log.i(TAG," onError()");

                return false;
            }

            @Override
            public void onPause(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onPause()");

            }

            @Override
            public void onRelease(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onRelease()");

            }

            @Override
            public void onStart(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onStart()");

            }

            @Override
            public void onTargetStateChange(int oldState, int newState) {
                Log.i(TAG," onTargetStateChange");

            }

            @Override
            public void onCurrentStateChange(int oldState, int newState) {
                Log.i(TAG," onCurrentStateChange");

            }

            @Override
            public void onDisplayModelChange(int oldModel, int newModel) {
                Log.i(TAG,"onDisplayModelChange");

            }

            @Override
            public void onPreparing(GiraffePlayer giraffePlayer) {
                Log.i(TAG,"onPreparing");

            }

            @Override
            public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {
                Log.i(TAG,"onPreparing");

            }

            @Override
            public void onLazyLoadProgress(GiraffePlayer giraffePlayer, int progress) {
                Log.i(TAG,"onLazyLoadProgress");
            }

            @Override
            public void onLazyLoadError(GiraffePlayer giraffePlayer, String message) {
                Log.i(TAG,"onLazyLoadError");

            }
        });*/


        //mVideo.setVideoPath("https://ctdworld.co/athlete/uploads/chat_data/5c57cbb7ab906SampleVideo_1280x720_1mb.mp4"/*mUrl*/);
/*        mVideo.setKeepScreenOn(true);
        mVideo.setMediaController(mMediaController);
        mVideo.setDrawingCacheEnabled(true);
        mVideo.setDrawingCacheQuality(MediaController.DRAWING_CACHE_QUALITY_AUTO);

        mVideo.setOnErrorListener(mMediaErrorListener);
        mVideo.setOnPreparedListener(mMediaPreparedListener);
        mVideo.setVideoURI(Uri.parse(mUrl));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mVideo.setOnInfoListener(mMediaPlayerInfoListener);
        }

        mMediaController = new MediaController(mContext, true);
        mMediaController.setKeepScreenOn(true);
        mMediaController.setAnchorView(mVideo);*/
//        mMediaController.show();

        // mVideo.start();
    }


   /* private MediaPlayer.OnErrorListener mMediaErrorListener = new MediaPlayer.OnErrorListener()
    {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            Log.e(TAG,"Error in showVideo() method , error value = "+i);
            DialogHelper.showSimpleCustomDialog(mContext,"Error Occurred....");
            return true;
        }
    };


    private MediaPlayer.OnPreparedListener mMediaPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer)
        {
            Log.i(TAG,"onPrepared() method called");
           *//* if (mVideo !=  null)
                mVideo.start();
            else
                Log.e(TAG,"mVideo is null in mMediaPreparedListener");*//*
        }
    };


    private MediaPlayer.OnInfoListener mMediaPlayerInfoListener = new MediaPlayer.OnInfoListener()
    {
        @Override
        public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra)
        {
            Log.i(TAG,"onInfo() method called , what = "+what+" , extra = "+extra);

            switch (what)
            {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.i(TAG,"MEDIA_INFO_BUFFERING_START...................................................................................................");
                    //DialogLoader.getInstance("Buffering").show(getSupportFragmentManager(),"");
                    showLoader("Buffering...");
                    break;

                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.i(TAG,"MEDIA_INFO_BUFFERING_END:...................................................................................................");
                    hideLoader();
                    break;

                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Log.i(TAG,"MEDIA_INFO_VIDEO_RENDERING_START...................................................................................................");
                    hideLoader();
                    break;
            }
            return true;
        }
    };*/



    // showing image
/*    private void showImage()
    {
       *//* mVideo.setVisibility(View.GONE);
        mImage.setVisibility(View.VISIBLE);*//*
       *//* Glide.with(mContext)
                .load(mUrl)
                .apply(new RequestOptions().placeholder(R.drawable.img_default_image))
                .apply(new RequestOptions().error(R.drawable.img_default_image))
                .into(mImage);*//*
    }*/



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);

    }


 /*   @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideo != null)
            mVideo.rele

    }*/


    // to hide loader
    private void showLoader(String message)
    {

        mDialogLoader = DialogLoader.getInstance(message);
        if (mDialogLoader != null && !mDialogLoader.isAdded())
        {
            mDialogLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        }
    }


    // to hide loader
    private void hideLoader()
    {
        if (mDialogLoader != null && mDialogLoader.isAdded())
            mDialogLoader.dismiss();
    }


}
