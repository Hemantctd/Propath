package org.ctdworld.propath.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterImageSlider;
import org.ctdworld.propath.model.FullSizeImageVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFullImage extends DialogFragment
{
    private static final String TAG = FragmentFullImage.class.getSimpleName();

    // keys to set argument
    public static final String KEY_SELECTED_IMAGE_POSITION = "position";
    public static final String KEY_URL_LIST = "user list";

    private ArrayList<String> mListUrl = new ArrayList<>();

    int gallerySelectedImagePosition;

    ViewPager viewPager;




    public FragmentFullImage()
    {
        // Required empty public constructor
    }


    // setting argument and returning object
    public static FragmentFullImage getInstance(List<String> listUrl, int selectedImagePosition)
    {
        Bundle bundle = new Bundle();
        FragmentFullImage fragmentFullImage = new FragmentFullImage(); // to set argument and return

        bundle.putStringArrayList(KEY_URL_LIST, (ArrayList<String>) listUrl);
        bundle.putInt(KEY_SELECTED_IMAGE_POSITION, selectedImagePosition); // putting selected image position in bundle

        fragmentFullImage.setArguments(bundle); // setting argument


        return fragmentFullImage;
    }

    // getting argument and initializing
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
          gallerySelectedImagePosition = bundle.getInt(KEY_SELECTED_IMAGE_POSITION);
          mListUrl = bundle.getStringArrayList(KEY_URL_LIST);
        }
        else
            Log.e(TAG,"getArguments is null in onCreate method ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mListUrl != null)
            Log.i(TAG,"Displayed, image list size = "+mListUrl.size());
        else
            Log.e(TAG,"mListUrl is null in onCreateView() method");


        View view = null;
        try
        {
           view =  inflater.inflate(R.layout.fragment_full_image, container, false);

           viewPager = view.findViewById(R.id.viewpager_full_image);  // initializing viewPager object
            // creating adapter object
           AdapterImageSlider adapter = new AdapterImageSlider(getContext(), getFullSizeImageVideoList(mListUrl), viewPager/*,AdapterImageSlider.TYPE_GALLERY*/);
           viewPager.setAdapter(adapter);  // setting adapter to viewPager


           if (gallerySelectedImagePosition >= 0) { // checking if selectedPosition is not less than 0
               viewPager.setCurrentItem(gallerySelectedImagePosition); // selected image in gallery will be shown first
           }

           getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           getDialog().getWindow().setGravity(Gravity.CENTER);

           // setAutoImageSlider();

        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in onCreateView method , "+e.getMessage());
            e.printStackTrace();
        }




        return view;
    }



    private ArrayList<FullSizeImageVideo> getFullSizeImageVideoList(List<String> imageUrls)
    {
        ArrayList<FullSizeImageVideo> list = new ArrayList<>();
        if (imageUrls == null)
            return null;

        for (String url : imageUrls)
        {
            FullSizeImageVideo fullSizeImageVideo = new FullSizeImageVideo(url, FullSizeImageVideo.TYPE_IMAGE);
            list.add(fullSizeImageVideo);
        }

        return list;
    }



}
