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
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterChatSelectedImagesFullSizeSlider;
import org.ctdworld.propath.helper.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChatSelectedImageOrVideoFullSize extends DialogFragment
{
    private static final String TAG = FragmentChatSelectedImageOrVideoFullSize.class.getSimpleName();

    // keys to set argument
    //public static final String KEY_SELECTED_IMAGE_POSITION = "position";
    public static final String KEY_URL_LIST = "user list";
    public static final String KEY_TITLE_TEXT = "title";
    public static final String KEY_BUTTON_TEXT = "button text";

    private ArrayList<String> mListUrl = new ArrayList<>();
    private String mStrTitle = "Selected Media", mStrButton="Share";

  //  int gallerySelectedImagePosition;

    ViewPager viewPager;
    OnSendClickListner listener;
    AdapterChatSelectedImagesFullSizeSlider adapter;
    Dialog dialog;



    // listener to return new images list if user has removed some images, in adapter there is option to remove image
    public interface OnSendClickListner
    {
        void onSelectedImagesReceived(List<String> selectedImagesPath);
    }





    public FragmentChatSelectedImageOrVideoFullSize()
    {
        // Required empty public constructor
    }


    // setting image list, title text and button text in argument and returning object
    public static FragmentChatSelectedImageOrVideoFullSize getInstance(ArrayList<String> listUrl)
    {
        Bundle bundle = new Bundle();
        FragmentChatSelectedImageOrVideoFullSize fragmentFullImage = new FragmentChatSelectedImageOrVideoFullSize(); // to set argument and return

        bundle.putStringArrayList(KEY_URL_LIST, listUrl);
        bundle.putString(KEY_TITLE_TEXT, "Selected Images");  // setting default title
        bundle.putString(KEY_BUTTON_TEXT, "Share"); // setting default button text
        //    bundle.putInt(KEY_SELECTED_IMAGE_POSITION, selectedImagePosition); // putting selected image position in bundle

        fragmentFullImage.setArguments(bundle); // setting argument


        return fragmentFullImage;
    }

    // setting image list, title text and button text in argument and returning object
    public static FragmentChatSelectedImageOrVideoFullSize getInstance(ArrayList<String> listUrl, String title, String buttonText)
    {
        Bundle bundle = new Bundle();
        FragmentChatSelectedImageOrVideoFullSize fragmentFullImage = new FragmentChatSelectedImageOrVideoFullSize(); // to set argument and return

        bundle.putStringArrayList(KEY_URL_LIST, listUrl);
        bundle.putString(KEY_TITLE_TEXT, title);
        bundle.putString(KEY_BUTTON_TEXT, buttonText);
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
         // gallerySelectedImagePosition = bundle.getInt(KEY_SELECTED_IMAGE_POSITION);
          mListUrl = bundle.getStringArrayList(KEY_URL_LIST);
          mStrTitle = bundle.getString(KEY_TITLE_TEXT);
          mStrButton = bundle.getString(KEY_BUTTON_TEXT);
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
           view =  inflater.inflate(R.layout.fragment_chat_selected_images_full_size, container, false);
           dialog = getDialog();

           TextView btnSend = view.findViewById(R.id.chat_selected_images_txt_send);
           TextView txtTitle = view.findViewById(R.id.chat_selected_images_full_size_txt_title);

           // setting text on title and button
            txtTitle.setText(mStrTitle);
            btnSend.setText(mStrButton);


            viewPager = view.findViewById(R.id.chat_selected_images_full_size_view_pager);  // initializing viewPager object
            // creating adapter object
            adapter = new AdapterChatSelectedImagesFullSizeSlider(this, getContext(), mListUrl, viewPager/*,AdapterImageSlider.TYPE_GALLERY*/,new FragmentChatSelectedImageOrVideoFullSize());
            viewPager.setAdapter(adapter);  // setting adapter to viewPager

           getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           getDialog().getWindow().setGravity(Gravity.CENTER);


            // to send images back after removing unwanted images
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (adapter.getCount() >5)
                        DialogHelper.showSimpleCustomDialog(getContext(), "Reduce Files", "Maximum 5 images can be shared...");
                    else
                        {
                            if (listener != null)
                                listener.onSelectedImagesReceived(adapter.getImagesPathList());  // taking image list and passing
                            dismissDialog();
                        }
                }
            });


            // setting onClickListener to remove this fragment
            view.findViewById(R.id.chat_selected_images_full_size_img_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                }
            });

        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in onCreateView method , "+e.getMessage());
            e.printStackTrace();
        }


        return view;
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
