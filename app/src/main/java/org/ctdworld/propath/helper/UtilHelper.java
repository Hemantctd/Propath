package org.ctdworld.propath.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;

import org.ctdworld.propath.R;

import java.util.Date;

public class UtilHelper
{

    public static UtilHelper getInstance()
    {
        return new UtilHelper();
    }

    public static void hideKeyboard(AppCompatActivity activity)
    {
        try {
            if (activity == null)
                return;

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static int getScreenWidth(Context context)
    {
        if (context == null)
            return ConstHelper.NOT_FOUND;

        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        if (display == null)
            return ConstHelper.NOT_FOUND;

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);

        return width;
    }


    // returns original size in dimen resource
    public static int getOriginalDimension(Context context, int getDimension)
    {
        float scaleRatio = context.getResources().getDisplayMetrics().density;

        return (int) (getDimension/scaleRatio);
    }



    // returns Options to be used with UCrop to crop image
    public static UCrop.Options getUCropOptions(Context context)
    {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameStrokeWidth(3);
        options.setFreeStyleCropEnabled(true);
        if (context != null) {
            options.setCropFrameColor(context.getResources().getColor(R.color.colorTheme));
            options.setActiveControlsWidgetColor(context.getResources().getColor(R.color.colorTheme));
            options.setCropGridColor(context.getResources().getColor(R.color.colorProfileBg));
        }
        return options;
    }


    // returns Intent object to pick image
    public static Intent getPickImageIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        return intent;
    }


    public static void showKeyboard(AppCompatActivity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(),0);
        }
    }



    public static boolean isConnectedToInternet(Context context)
    {
        /*if (context == null)
            return */
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null;
        }

        return false;
    }


    // checks, if first date is greater then second then returns true otherwise false. in case any problem first date will be considered as greater
    public static boolean isFirstDateGreater(String firstDate, String secondDate) throws Exception
    {
        if (firstDate == null || secondDate == null || firstDate.isEmpty() || secondDate.isEmpty())
            throw new Exception();

        Date firstD = DateTimeHelper.getDateObject(firstDate, DateTimeHelper.FORMAT_DATE_TIME);
        Date secondD = DateTimeHelper.getDateObject(secondDate, DateTimeHelper.FORMAT_DATE_TIME);
        return firstD != null && secondD != null && firstD.getTime() > secondD.getTime();

    }



    public static int convertPixelsToDp( Context context ,int pixel){
        float density = context.getResources().getDisplayMetrics().density;
        float dp = pixel / density;
        return (int) dp;
    }



    public static int convertDpToPixel( Context context ,int dp){
        float density = context.getResources().getDisplayMetrics().density;

      //  Log.i(TAG, "density = "+density);
        return (int) (dp * density);
    }



    // # setting up spinner
    public static void setUpSpinner(final Context context, Spinner spinner)
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                view.setPadding(0, textView.getPaddingTop(),textView.getPaddingRight(),textView.getPaddingBottom());
                textView.setTextColor(context.getResources().getColor(R.color.colorTextBlack));
                textView.setTextSize(UtilHelper.getOriginalDimension(context,context.getResources().getDimensionPixelSize(R.dimen.txtSize)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // setting dropdown icon
        spinner.setBackground(context.getResources().getDrawable(R.drawable.bg_dropdown_icon));
    }



    public static String getYoutubeVideoThumbnailUrl(String youtubeLink)
    {
        if (youtubeLink == null || youtubeLink.isEmpty())
            return ConstHelper.NOT_FOUND_STRING;

        String videoId = null;

        if (youtubeLink.contains("youtu.be"))
        {
            String[] split = youtubeLink.split("youtu.be/");  // if link is added from youtube app.
            if (split != null && split.length>=2)
                videoId = split[1];
        }
        else
        {
            String[] split = youtubeLink.split("\"?v=");
            if (split != null && split.length>=2)
                videoId = split[1];
        }


        return "https://img.youtube.com/vi/"+videoId+"/0.jpg";
    }

    public static String getYoutubeVideoID(String youtubeLink)
    {
        if (youtubeLink == null || youtubeLink.isEmpty())
            return ConstHelper.NOT_FOUND_STRING;

        String videoId = null;

        if (youtubeLink.contains("youtu.be"))
        {
            String[] split = youtubeLink.split("youtu.be/");  // if link is added from youtube app.
            if (split != null && split.length>=2)
                videoId = split[1];
        }
        else
        {
            String[] split = youtubeLink.split("\"?v=");
            if (split != null && split.length>=2)
                videoId = split[1];
        }


        return videoId;
    }



    public static String getViewPagerItemTag(int currentItem)
    {
        return "android:switcher:"+R.id.activity_contact_view_pager+":"+currentItem;
    }



    public static boolean isYoutubeUrl(String youtubeLink)
    {
        if (youtubeLink == null || youtubeLink.isEmpty())
            return false;

        return youtubeLink.contains("youtu.be") || youtubeLink.contains("youtube");
    }




    // # this method returns the position in String array based on selected text in array(Spinner)
    public static int getPositionInStringArray(String[] strArray, String selectedText)
    {
        int position = ConstHelper.NOT_FOUND;
        if (strArray == null || strArray.length == 0  || selectedText == null || selectedText.isEmpty())
            return position;

        try {
            for (int i=0; i<strArray.length; i++)
            {
                String arrString = strArray[i];
                if (selectedText.equals(arrString))
                {
                    position = i;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return position;
    }









    /*################################################### Loading images with glide ###################################################################*/

    public static void loadImageWithGlide(Context context, String url, int width, int height, ImageView imageView)
    {
        if (context == null || url == null || imageView == null)
            return;

        Glide.with(context)
                .load(url)
                .apply(getGlideRequestOptions(R.drawable.img_default_black, width, height, true))
                .into(imageView);
    }


    public static void loadImageWithGlide(Context context, String url, int defaultImage, int width, int height, ImageView imageView)
    {
        if (context == null || url == null || imageView == null)
            return;
        Glide.with(context)
                .load(url)
                .apply(getGlideRequestOptions(defaultImage, width, height, true))
                .into(imageView);
    }


    public static void loadImageWithGlide(Context context, String url, ImageView imageView)
    {
        if (context == null || url == null || imageView == null)
            return;

        Glide.with(context)
                .load(url)
                .apply(getGlideRequestOptions(R.drawable.img_default_black, 0, 0, true))
                .into(imageView);
    }


    public static void loadImageWithGlide(Context context, String url, ImageView imageView, int defaultImage)
    {
        if (context == null || url == null || imageView == null)
            return;

        Glide.with(context)
                .load(url)
                .apply(getGlideRequestOptions(defaultImage, 0, 0, true))
                .into(imageView);
    }


    /*returns RequestOptions to be used with Glide
    * pass zero for int parameters if parameter is not to be used*/
    @SuppressLint("CheckResult")
    public static RequestOptions getGlideRequestOptions(int defaultImage, int width, int height, boolean centerCrop)
    {
         RequestOptions requestOptions = new RequestOptions();
         requestOptions.skipMemoryCache(true);
         requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);

         if (width != 0 && height != 0)
            requestOptions.override(width, height);
         if (defaultImage != 0)
         {
             requestOptions.error(defaultImage);
             requestOptions.placeholder(defaultImage);
         }
         if (centerCrop)
             requestOptions.centerCrop();

        return requestOptions;
    }

}
