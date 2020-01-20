package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.TrainingPlan;

import java.util.ArrayList;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoView;
import tv.danmaku.ijk.media.player.IjkTimedText;


public class AdapterTrainingPlanItemViewSlider extends PagerAdapter
{
    private static final String TAG = AdapterTrainingPlanItemViewSlider.class.getSimpleName();
    Context mContext;
    private ArrayList<TrainingPlan.PlanData.PlanItem> mListPlanItems;

    public AdapterTrainingPlanItemViewSlider(Context context, ArrayList<TrainingPlan.PlanData.PlanItem> listPlanItems)
    {
        this.mContext = context;
        this.mListPlanItems = listPlanItems;
    }


    // updating plan item
    public void updateItem(TrainingPlan.PlanData.PlanItem planItem)
    {
        if (planItem == null)
            return;

        int position = TrainingPlan.PlanData.PlanItem.getPlanItemPosition(mListPlanItems, planItem.getId());
        if (position == ConstHelper.NOT_FOUND)
            return;

        mListPlanItems.set(position, planItem);
        notifyDataSetChanged();
    }


    public ArrayList<TrainingPlan.PlanData.PlanItem> getPlanItemList()
    {
        return mListPlanItems;
    }


    public void onItemDeleted(TrainingPlan.PlanData.PlanItem planItem)
    {
        if (planItem == null)
            return;

        int position = TrainingPlan.PlanData.PlanItem.getPlanItemPosition(mListPlanItems, planItem.getId());
        if (position == ConstHelper.NOT_FOUND)
            return;

        mListPlanItems.remove(position);
        notifyDataSetChanged();
    }



    @Override
    public int getCount()
    {
        return mListPlanItems.size();
    }


    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position)
    {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_training_plan_item_view_slider,null);
        try
        {



            ImageView imageView = view.findViewById(R.id.adapter_training_plan_item_view_image);
            ImageView imgVideoIcon = view.findViewById(R.id.adapter_training_plan_item_view_img_video_icon_play);
            View layoutImage = view.findViewById(R.id.layout_image_video_icon);
            final VideoView videoView = view.findViewById(R.id.adapter_training_plan_item_view_video);
            TextView txtTitle = view.findViewById(R.id.adapter_training_plan_item_view_txt_title);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            imageView.setLayoutParams(params);

            final TrainingPlan.PlanData.PlanItem planItem = mListPlanItems.get(position);
            if (planItem != null)
            {
                txtTitle.setText(planItem.getTitle());

                String link;
                if (planItem.getLink() != null && !planItem.getLink().isEmpty() && !planItem.getLink().contains("null"))
                {
                    link = UtilHelper.getYoutubeVideoThumbnailUrl(planItem.getLink());
                    planItem.setMediaType(TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO);
                }
                else
                    link = planItem.getMediaUrl();

                if (link != null && !link.isEmpty())
                {
                    if (TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO.equals(planItem.getMediaType()))
                        imgVideoIcon.setVisibility(View.VISIBLE);
                    else
                        imgVideoIcon.setVisibility(View.GONE);

                    UtilHelper.loadImageWithGlide(mContext,link, imageView);  // loading image

                    layoutImage.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            if (planItem.getLink() != null && !planItem.getLink().isEmpty() && !planItem.getLink().contains("null"))
                            {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(planItem.getLink()));
                                mContext.startActivity(intent);
                            }
                            else
                            {
                                playVideo(planItem.getMediaUrl(), videoView);
                            }
                        }
                    });
                }


            }
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in instanciateItem method ");
            e.printStackTrace();
        }
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

       container.removeView((View) object);
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }



  /*  // # showing image
    private void showImage(String url, ImageView imageView)
    {
        Glide.with(mContext)
                .load(url  )
                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                .apply(new RequestOptions().error(R.drawable.img_default_black))
                // .apply(new RequestOptions().centerCrop())
                //    .apply(new RequestOptions().override(width, height))
                .into(imageView)
                    *//*.getSize(new SizeReadyCallback() {
                        @Override
                        public void onSizeReady(int width, int height) {
                           // Log.i(TAG,"image width = "+width+" , height = "+height);
                        }
                    })*//*;
    }*/


    // # showing video
    private void playVideo(final String mUrl, VideoView mVideo)
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


   // method to slide images automatically on specified time
   /* private void setAutoImageSlider()
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
                if (currentPage == mListPlanItems.size())
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

    }*/

}
