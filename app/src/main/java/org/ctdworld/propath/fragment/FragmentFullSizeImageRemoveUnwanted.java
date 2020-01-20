package org.ctdworld.propath.fragment;


import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFullSizeImageRemoveUnwantedSlider;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFullSizeImageRemoveUnwanted extends DialogFragment
{
    private static final String TAG = FragmentFullSizeImageRemoveUnwanted.class.getSimpleName();

    // keys to set argument
    //public static final String KEY_SELECTED_IMAGE_POSITION = "position";
    public static final String KEY_URL_LIST = "user list";
    public static final String KEY_TITLE_TEXT = "title";
    public static final String KEY_BUTTON_TEXT = "button text";
    public static final String KEY_SELECTED_IMAGE_POSITION = "selected image position";

    private ArrayList<String> mListUrl = new ArrayList<>();
    private String mStrTitle = "Selected Media", mStrButton="Share";

    int mSelectedImagePosition;


    ViewPager mViewPager;
    OnSendClickListner listener;
    AdapterFullSizeImageRemoveUnwantedSlider adapter;
    Dialog dialog;


    // views
    TextView mBtnSend;
    TextView mTxtTile;
    ImageView mImgCancel;


    // listener to return new images list if user has removed some images, in adapter there is option to remove image
    public interface OnSendClickListner
    {
        void onSelectedImagesReceived(List<String> selectedImages, List<String> removedImages);
    }





    public FragmentFullSizeImageRemoveUnwanted()
    {
        // Required empty public constructor
    }


    // setting image list, title text and button text in argument and returning object
    public static FragmentFullSizeImageRemoveUnwanted getInstance(ArrayList<String> listUrl)
    {
        Bundle bundle = new Bundle();
        FragmentFullSizeImageRemoveUnwanted fragmentFullImage = new FragmentFullSizeImageRemoveUnwanted(); // to set argument and return

        bundle.putStringArrayList(KEY_URL_LIST, listUrl);
        bundle.putString(KEY_TITLE_TEXT, "Selected Images");  // setting default title
        bundle.putString(KEY_BUTTON_TEXT, "Share"); // setting default button text
        bundle.putInt(KEY_SELECTED_IMAGE_POSITION, 0); // putting selected image position in bundle

        fragmentFullImage.setArguments(bundle); // setting argument


        return fragmentFullImage;
    }

    // setting image list, title text and button text in argument and returning object
    public static FragmentFullSizeImageRemoveUnwanted getInstance(ArrayList<String> listUrl, String title, String buttonText, int selectedPosition)
    {
        Bundle bundle = new Bundle();
        FragmentFullSizeImageRemoveUnwanted fragmentFullImage = new FragmentFullSizeImageRemoveUnwanted(); // to set argument and return

        bundle.putStringArrayList(KEY_URL_LIST, listUrl);
        bundle.putString(KEY_TITLE_TEXT, title);
        bundle.putString(KEY_BUTTON_TEXT, buttonText);
        bundle.putInt(KEY_SELECTED_IMAGE_POSITION, selectedPosition);
    //    bundle.putInt(KEY_SELECTED_IMAGE_POSITION, selectedImagePosition); // putting selected image position in bundle

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
          mListUrl = bundle.getStringArrayList(KEY_URL_LIST);
          mStrTitle = bundle.getString(KEY_TITLE_TEXT);
          mStrButton = bundle.getString(KEY_BUTTON_TEXT);
          mSelectedImagePosition = bundle.getInt(KEY_SELECTED_IMAGE_POSITION);
        }
        else
            Log.e(TAG,"getArguments is null in onCreate method ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.i(TAG,"Displayed");
        View view = null;
        try
        {
            view =  inflater.inflate(R.layout.fragment_full_size_image_remove_unwanted, container, false);
            dialog = getDialog();

            init(view);
            setAdapter();
            setListener();  // setting listeners

           // setting text on title and button
            mTxtTile.setText(mStrTitle);
            mBtnSend.setText(mStrButton);

           getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           getDialog().getWindow().setGravity(Gravity.CENTER);


            // setting onClickListener to remove this fragment

        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in onCreateView method , "+e.getMessage());
            e.printStackTrace();
        }


        return view;
    }

    private void init(View view)
    {
        mBtnSend = view.findViewById(R.id.chat_selected_images_txt_send);
        mTxtTile = view.findViewById(R.id.chat_selected_images_full_size_txt_title);
        mViewPager = view.findViewById(R.id.chat_selected_images_full_size_view_pager);  // initializing viewPager object
        mImgCancel = view.findViewById(R.id.chat_selected_images_full_size_img_cancel);

    }


    private void setAdapter()
    {
        // creating adapter object
        adapter = new AdapterFullSizeImageRemoveUnwantedSlider(this, getContext(), mListUrl, mViewPager/*,AdapterImageSlider.TYPE_GALLERY*/,new FragmentFullSizeImageRemoveUnwanted());
        mViewPager.setAdapter(adapter);  // setting adapter to viewPager

        if (mSelectedImagePosition >= 0)
            mViewPager.setCurrentItem(mSelectedImagePosition);
    }

    private void setListener()
    {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectedImagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // to send images back after removing unwanted images
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (listener != null && adapter != null)
                    listener.onSelectedImagesReceived(adapter.getImagesPathList(), adapter.getRemovedImageList());  // taking image list and passing
                dismissDialog();
            }
        });


        // to dismiss dialog
        mImgCancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            dismissDialog();
        }
    });



    }

    // this method will be called from adapter when an image is removed
    public void onImageRemoved()
    {
        Log.i(TAG,"onImageRemoved() method called");
        dismissDialog();
    }



    // to dismiss dialog and send back list of images
    public void dismissDialog()
    {
        Dialog dialog = getDialog();
        if (dialog != null)
            dismiss();
        else
            Log.e(TAG,"dialog is null");
    }



    //  setting listener to return new images list if user has removed some images, in adapter there is option to remove image
    public void setOnSendClickListener(OnSendClickListner listener)
    {
        this.listener = listener;
    }




}
