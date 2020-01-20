package org.ctdworld.propath.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.fragment.app.DialogFragment;
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

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentChatSelectedImageOrVideoFullSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Irshad Ahamd on 01-Nov-17.
 */

public class AdapterChatSelectedImagesFullSizeSlider extends PagerAdapter
{
    private static final String TAG = AdapterChatSelectedImagesFullSizeSlider.class.getSimpleName();
    Context context;
    private ArrayList<String> mListImagesPath;
    private ViewPager viewPager;
    private int currentPage;
    private FragmentChatSelectedImageOrVideoFullSize fragmentChatSelectedImageOrVideoFullSize;
    DialogFragment mDialogFragment;

 /*   public static final int TYPE_GALLERY = 1;
    public static final int TYPE_HEADER = 2;
    private static int TYPE = 0;*/


    public AdapterChatSelectedImagesFullSizeSlider(DialogFragment dialogFragment, Context context, ArrayList<String> listUrl, ViewPager viewPager/*, int type*/, FragmentChatSelectedImageOrVideoFullSize imagesFullSize)
    {
        this.context = context;
        this.viewPager = viewPager;
        this.mListImagesPath = listUrl;
        this.fragmentChatSelectedImageOrVideoFullSize = imagesFullSize;
        this.mDialogFragment = dialogFragment;
    }

    //
    public List<String> getImagesPathList()
    {
        return mListImagesPath;
    }

    @Override
    public int getCount()
    {
        return mListImagesPath.size();
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position)
    {
        //ImageView imageView = null;


      //  Log.i(TAG,"position = "+position);
      //  Log.i(TAG,"total images = "+mListImagesPath.size());


        View view = null;
        try
        {
         //   Log.i(TAG,"instantiateItem = "+listBitmap.size());
            if (true)
            {
              //  Log.i(TAG,"inside gallery ");
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.adapter_chat_selected_images_full_size_slider,null);
                ImageView imageView = view.findViewById(R.id.adapter_chat_selected_images_img_image);

                String picUrl = mListImagesPath.get(position);

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;

                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                        .apply(new RequestOptions().error(R.drawable.img_default_black))
                       // .apply(new RequestOptions().centerCrop())
                        .apply(new RequestOptions().override(width, height))
                        .into(imageView)
                        /*.getSize(new SizeReadyCallback() {
                            @Override
                            public void onSizeReady(int width, int height) {
                               // Log.i(TAG,"image width = "+width+" , height = "+height);
                            }
                        })*/;


                // setting listener on cancel image to remove unwanted image
                ImageView imgCancel = view.findViewById(R.id.adapter_chat_selected_images_img_cancel);
                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        mListImagesPath.remove(position);
                        notifyDataSetChanged();
                        if (mListImagesPath.size()<=0)
                        {
                            Log.i(TAG,"closing "+TAG+" , fragment");
                            if (mDialogFragment != null)
                                mDialogFragment.dismiss();
                            else
                                Log.e(TAG,"mDialogFragment is null in instantiateItem() method");
                        }
                      }
                });

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
    public boolean isViewFromObject(View view, Object object)
    {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((View) object);
    }


    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
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
                if (currentPage == mListImagesPath.size())
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

}
