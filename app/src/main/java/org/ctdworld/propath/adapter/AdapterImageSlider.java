package org.ctdworld.propath.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.FullSizeImageVideo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoView;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by Irshad Ahamd on 01-Nov-17.
 */

public class AdapterImageSlider extends PagerAdapter
{
    private static final String TAG = AdapterImageSlider.class.getSimpleName();
    Context context;
    private ArrayList<FullSizeImageVideo> listUrl;
    private ViewPager viewPager;
    private int currentPage;
   // private ImageView mImgVideoIcon;
    //public static final int TYPE_GALLERY = 1;
    //public static final int TYPE_HEADER = 2;
 //   private static int TYPE = 0;


    public AdapterImageSlider(Context context, ArrayList<FullSizeImageVideo> listUrl, ViewPager viewPager)
    {
        this.context = context;
        this.viewPager = viewPager;
        this.listUrl = listUrl;
      //  createBitmapList();
     //   setAutoImageSlider();  method to slide image automatically

     //   TYPE = type;
          Log.i(TAG,"listUrl size = "+listUrl.size());
    }

    @Override
    public int getCount()
    {
     //   Log.i(TAG,"getCount method = "+listBitmap.size());
        return listUrl.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = null;
        try
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_image_slider,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_slider);
            final VideoView videoView = view.findViewById(R.id.layout_image_slider_video_view);
         /*   mImgVideoIcon = view.findViewById(R.id.layout_image_slider_img_video);
            View layoutVideo = view.findViewById(R.id.layout_image_slider_layout_video);*/

            final FullSizeImageVideo fullSizeImageVideo = listUrl.get(position);
            if (fullSizeImageVideo == null)
                return view;

            Log.i(TAG,"url = "+fullSizeImageVideo.getUrl());
            Log.i(TAG,"type = "+fullSizeImageVideo.getType());

            if (fullSizeImageVideo.getType() == (FullSizeImageVideo.TYPE_IMAGE))
            {
                imageView.setVisibility(View.VISIBLE);
                showImage(fullSizeImageVideo.getUrl(), imageView);
            }
            else if (fullSizeImageVideo.getType() == (FullSizeImageVideo.TYPE_VIDEO))
            {
                Log.i(TAG,"setting click listener");
                ///layoutVideo.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.VISIBLE);
               /* layoutVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {*/
                       // Log.i(TAG,"video clicked");
                        showVideo(fullSizeImageVideo.getUrl(), videoView);
                  /*  }
                });*/
            }


        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in instanciateItem method ");
            e.printStackTrace();
        }
      //  container.addView(imageView,position);
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((View) object);
    }



   // method to slide images automatically on specified time
    private void setAutoImageSlider()
    {
        // Auto start of viewpager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        final Handler handler = new Handler();
        // creating runnable object to use in timer
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == listUrl.size())
                {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        // creating timer to change image automatically
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
              //  Log.i(TAG,"run method called in timer ");
                handler.post(Update);
            }
        }, 1000, 1000);

    }




    // showing video
    private void showVideo(final String mUrl, VideoView mVideo)
    {
        //   mImage.setVisibility(View.GONE);
        //   mVideo.setVisibility(View.VISIBLE);
        Log.i(TAG,"showing video");

        Log.i(TAG,"video url = "+mUrl);
        if (mUrl == null)
        {
            Log.e(TAG,"mUrl is null in showVideo() method");
            return;
        }

        // VideoView videoView = (VideoView) findViewById(R.id.video_view);
        mVideo.setVideoPath(mUrl).getPlayer().start();
        mVideo.setPlayerListener(new tcking.github.com.giraffeplayer2.MediaController() {
            @Override
            public void bind(VideoView videoView) {
                Log.i(TAG," bind()");
            }

            @Override
            public void onPrepared(GiraffePlayer giraffePlayer)
            {
                Log.i(TAG," onPrepared()");
                //hideVideoIcon();
            }

            @Override
            public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {
                Log.i(TAG," onBufferingUpdate()");
                //showVideoIcon();
            }

            @Override
            public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
                Log.i(TAG," onInfo()");

                return false;
            }

            @Override
            public void onCompletion(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onCompletion()");

               // showVideoIcon();

            }

            @Override
            public void onSeekComplete(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onSeekComplete()");
              //  hideVideoIcon();
            }

            @Override
            public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
                Log.i(TAG," onError()");
               // showVideoIcon();
                return false;
            }

            @Override
            public void onPause(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onPause()");
               // showVideoIcon();
            }

            @Override
            public void onRelease(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onRelease()");
              //  showVideoIcon();
            }

            @Override
            public void onStart(GiraffePlayer giraffePlayer) {
                Log.i(TAG," onStart()");
              //  hideVideoIcon();
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
               // showVideoIcon();

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
              //  showVideoIcon();
            }
        });


    }


    // to show image
    private void showImage(String picUrl, ImageView imageView)
    {
        Log.i(TAG,"showing image");
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        Log.i(TAG,"display size = "+width+"*"+height);


        Glide.with(context)
                .load(picUrl)
                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                .apply(new RequestOptions().error(R.drawable.img_default_black))
                // .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(width, height))
                .into(imageView)
                .getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {
                        Log.i(TAG,"image width = "+width+" , height = "+height);
                    }
                });

    }


/*    // to show video icon on video
    private void showVideoIcon()
    {
        if (mImgVideoIcon != null)
            mImgVideoIcon.setVisibility(View.VISIBLE);
    }

    // to hide video icon on video
    private void hideVideoIcon()
    {
        if (mImgVideoIcon != null)
            mImgVideoIcon.setVisibility(View.GONE);
    }*/

}
