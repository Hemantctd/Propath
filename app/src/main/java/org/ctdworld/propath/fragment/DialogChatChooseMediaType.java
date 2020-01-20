package org.ctdworld.propath.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.model.Chat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogChatChooseMediaType extends DialogFragment
{
    private final String TAG = DialogChatChooseMediaType.class.getSimpleName();


    private Context mContext;
    private final int REQUEST_CODE_IMAGE = 100;
    private final int REQUEST_CODE_VIDEO = 200;
    private final int REQUEST_CODE_CAPTURE_IMAGE = 300;
    private final int REQUEST_CODE_CAPTURE_VIDEO = 400;

    private final int VIDEO_MAX_SIZE_MB = 5;
    private final int VIDEO_MAX_CAPTURE_TIME_SECONDS = 5;

    private OnSelectedMediaFilesReceiveListener mMediaFilesReceivedListener;


    // keys and value to set argument
    private static String KEY_SELECT_FROM_DEVICE_OR_CAMERA = "from device or camera";
    public static final int SELECT_FROM_DEVICE = 1;
    public static final int SELECT_FROM_CAMERA = 2;

    private int mSelectFromDevieOrCamera;




    // listener to send selected images
    public interface OnSelectedMediaFilesReceiveListener
    {
        void onSelectedMediaReceived(List<String> listSelectedImagesPath, String mediaType);
    }


    // setting media type from device or camera
    public static DialogChatChooseMediaType getInstance(int fromDeviceOrCamera)
    {
        DialogChatChooseMediaType dialogChatChooseMediaType = new DialogChatChooseMediaType();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SELECT_FROM_DEVICE_OR_CAMERA, fromDeviceOrCamera);

        dialogChatChooseMediaType.setArguments(bundle);
        return dialogChatChooseMediaType;
    }


    // getting media type (from device or camera) from argument


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mSelectFromDevieOrCamera = bundle.getInt(KEY_SELECT_FROM_DEVICE_OR_CAMERA);
        }
        else
            Log.e(TAG,"bundle is null onCreate() method ");
    }


    public DialogChatChooseMediaType() {
    }  // constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_chat_choose_media, container, false);
        manageDialogAndWindow(getDialog());  // to manage dialogs's position and background

        mContext = getContext();

        view.findViewById(R.id.choose_media_type_img_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectVideo();
            }
        });
        view.findViewById(R.id.choose_media_type_img_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



        return view;
    }


    // to manage dialog position and background
    private void manageDialogAndWindow(Dialog dialog)
    {
        if (dialog != null)
        {
           // dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
            if (window != null)
            {
                window.setGravity(Gravity.CENTER|Gravity.BOTTOM);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                // set bottom margin
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.y = 200;  // to set margin from bottom
                window.setAttributes(layoutParams);
            }
        }
    }



    // selecting video from device or capturing video
    private void selectVideo()
    {
        if (mSelectFromDevieOrCamera == SELECT_FROM_DEVICE)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            /*intent.setType("media/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/*", "video/*"});*/

            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, REQUEST_CODE_VIDEO );
        }
        else if (mSelectFromDevieOrCamera == SELECT_FROM_CAMERA)
        {

            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, VIDEO_MAX_CAPTURE_TIME_SECONDS);
            // intent.setType("image/*");
            // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_VIDEO );
        }

    }

    // selecting image from device or capturing image
    private void selectImage()
    {
        if (mSelectFromDevieOrCamera == SELECT_FROM_DEVICE)
        {
            // starting custom gallery to select multiple images
            //startActivityForResult(new Intent(mContext,ActivitySelectMultipleImages.class), REQUEST_CODE_IMAGE );


            //new code to select images
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, REQUEST_CODE_VIDEO );
        }
        else if (mSelectFromDevieOrCamera == SELECT_FROM_CAMERA)
        {
                 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               // intent.setType("image/*");
               // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE );
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == Activity.RESULT_OK && data != null)
        {
            if (requestCode == REQUEST_CODE_IMAGE || requestCode == REQUEST_CODE_VIDEO)
            {
                //# for custom gallery
                /*Bundle bundle = data.getExtras();
                //handleSelectedImage(bundle.getStringArrayList("data"));
                if (bundle != null)
                {
                 //    handleSelectedImagesPath(bundle)
                }
                else
                    Log.e(TAG,"bundle is null in onActivityResult() method ");*/

                handleSelectedMedia(data);
            }
            else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE)
            {
                handleCapturedImage(data);
            }
            else if (requestCode == REQUEST_CODE_CAPTURE_VIDEO)
            {
                handleCapturedVideo(data);
            }

        }
        else
            dismiss();


    }



    // feting selected videos from device storage and setting validation
    private void handleSelectedMedia(Intent data) {
        if (data != null)
        {
            Log.i(TAG,"handleSelectedMedia() method called **************************************************************************");

            List<String> listPath = new ArrayList<>();
            String mediaType = "";

            //if only 1 media has been selected
            if (data.getData() != null)
            {
                Uri uri = data.getData();
                if (mContext.getContentResolver().getType(uri).contains("image")) {
                    mediaType = Chat.MESSAGE_TYPE_IMAGE;
                }
                else if (mContext.getContentResolver().getType(uri).contains("video"))
                {
                    mediaType = Chat.MESSAGE_TYPE_VIDEO;
                }

                Log.i(TAG,"media type = "+mediaType);
                int fileSize = (int)FileHelper.getFileSizeInMb(mContext, uri);

                if (fileSize > 5)
                {
                    DialogHelper.showSimpleCustomDialog(mContext, "Large File...", "Select File Less Than "+VIDEO_MAX_SIZE_MB);
                    return;
                }

                String videoPath = FileHelper.getFilePath(mContext, uri);
                Log.i(TAG,"media path = "+videoPath);
                Log.i(TAG,"media size = "+fileSize);

                listPath.add(videoPath);

            }
            else // if more than 1 files have been selected
            {
                ClipData clipData = data.getClipData();
                if (clipData != null)
                {
                    for (int i=0; i<clipData.getItemCount(); i++)
                    {
                        ClipData.Item clipItem = clipData.getItemAt(i);
                        Uri uriMedia = clipItem.getUri();
                        int fileSize = (int)FileHelper.getFileSizeInMb(mContext, uriMedia);

                        if (mContext.getContentResolver().getType(uriMedia).contains("image")) {
                            mediaType = Chat.MESSAGE_TYPE_IMAGE;
                        }
                        else if (mContext.getContentResolver().getType(uriMedia).contains("video"))
                        {
                            mediaType = Chat.MESSAGE_TYPE_VIDEO;
                        }
                        // validating media size
                        if (fileSize > VIDEO_MAX_SIZE_MB)
                        {
                            DialogHelper.showSimpleCustomDialog(mContext, "Large File...", "Select File Less Than "+VIDEO_MAX_SIZE_MB);
                            return;
                        }
                        String mediaPath = FileHelper.getFilePath(mContext, uriMedia);
                        listPath.add(mediaPath);
/*
                        Log.i(TAG,"media uri = "+uriMedia);
                        Log.i(TAG,"media path = "+mediaPath);
                        Log.i(TAG,"media size = "+(int)FileHelper.getFileSizeInMb(mContext, uriMedia));*/
                    }
                }
                else
                    Log.e(TAG,"clipData is null in handleSelectedVideos() method");
            }



            // showing videos in pager, and sending back to calling component
            FragmentChatSelectedImageOrVideoFullSize fragmentChatSelectedImageOrVideoFullSize = FragmentChatSelectedImageOrVideoFullSize.getInstance((ArrayList<String>) listPath);
            final String finalMediaType = mediaType;
            fragmentChatSelectedImageOrVideoFullSize.setOnSendClickListener(new FragmentChatSelectedImageOrVideoFullSize.OnSendClickListner() {
                @Override
                public void onSelectedImagesReceived(List<String> selectedImagesPath)
                {
                    //showing videos
                    if (mMediaFilesReceivedListener != null)
                        mMediaFilesReceivedListener.onSelectedMediaReceived(selectedImagesPath, finalMediaType);

                }
            });

            fragmentChatSelectedImageOrVideoFullSize.show(getChildFragmentManager(), "");
        }
    }

    // feting captured image
    private void handleCapturedImage(Intent data) {
        if (data != null)
        {
            Log.i(TAG,"handleCapturedImage() method called **************************************************************************");

            List<String> listPath = new ArrayList<>();

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String selectImagePath = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),bitmap,"athlete captured", null);

            String path = FileHelper.getFilePath(mContext,Uri.parse(selectImagePath));

            Log.i(TAG,"media path = "+selectImagePath);
            Log.i(TAG,"media uri = "+Uri.parse(selectImagePath));
            Log.i(TAG,"media pathOther = "+path);


           /* int fileSize = (int)FileHelper.getFileSizeInMb(mContext, uri);

            if (fileSize > 5)
            {
                DialogHelper.showSimpleCustomDialog(mContext, "Large File...", "Select File Less Than 5 MB...");
                return;
            }*/


            listPath.add(path);


            // showing videos in pager, and sending back to calling coponent
            FragmentChatSelectedImageOrVideoFullSize fragmentChatSelectedImageOrVideoFullSize = FragmentChatSelectedImageOrVideoFullSize.getInstance((ArrayList<String>) listPath);
            fragmentChatSelectedImageOrVideoFullSize.setOnSendClickListener(new FragmentChatSelectedImageOrVideoFullSize.OnSendClickListner() {
                @Override
                public void onSelectedImagesReceived(List<String> selectedImagesPath) {
                    //showing videos
                    if (mMediaFilesReceivedListener != null)
                        mMediaFilesReceivedListener.onSelectedMediaReceived(selectedImagesPath, Chat.MESSAGE_TYPE_IMAGE);

                }
            });

            fragmentChatSelectedImageOrVideoFullSize.show(getChildFragmentManager(), "");
        }
    }

    // feting captured image
    private void handleCapturedVideo(Intent data) {
        if (data != null)
        {
            Log.i(TAG,"handleCapturedVideo() method called **************************************************************************");

            List<String> listPath = new ArrayList<>();

            Uri uri = data.getData();
            String path = FileHelper.getFilePath(mContext, uri);

            Log.i(TAG,"media uri = "+uri);
            Log.i(TAG,"media path = "+path);


            int fileSize = (int)FileHelper.getFileSizeInMb(mContext, uri);
            Log.i(TAG,"captured video size = "+fileSize);

            if (fileSize > 5)
            {
                DialogHelper.showSimpleCustomDialog(mContext, "Large File...", "Select File Less Than "+VIDEO_MAX_SIZE_MB);
                return;
            }


            listPath.add(path);


            // showing videos in pager, and sending back to calling coponent
            FragmentChatSelectedImageOrVideoFullSize fragmentChatSelectedImageOrVideoFullSize = FragmentChatSelectedImageOrVideoFullSize.getInstance((ArrayList<String>) listPath);
            fragmentChatSelectedImageOrVideoFullSize.setOnSendClickListener(new FragmentChatSelectedImageOrVideoFullSize.OnSendClickListner() {
                @Override
                public void onSelectedImagesReceived(List<String> selectedImagesPath) {
                    //showing videos
                    if (mMediaFilesReceivedListener != null)
                        mMediaFilesReceivedListener.onSelectedMediaReceived(selectedImagesPath, Chat.MESSAGE_TYPE_VIDEO);

                }
            });

            fragmentChatSelectedImageOrVideoFullSize.show(getChildFragmentManager(), "");
        }
    }



    //# for custom galley
    // this method takes Bundle as parameter which is coming from Activity after selecting images. after taking images
    // (path) these paths are sent to other fragment to show selected images in full size and to remove unwanted
    // images, then we gat back images (path) when user clicks on send button in that fragment
    /*private void handleSelectedImagesPath(Bundle bundle) {
        if (bundle != null)
        {
            ArrayList<String> listImages = bundle.getStringArrayList("data");
            Log.i(TAG,"Total selected images = "+listImages.size());

            // if listImages is not empty then Dialog FragmentChatSelectedImageOrVideoFullSize will be displayed to show selected images
            // in full size, to check if user wants to remove some images from selected images list
            if (listImages.size()>0)
            {
                FragmentChatSelectedImageOrVideoFullSize fragmentSelectedImagesFullSize = FragmentChatSelectedImageOrVideoFullSize.getInstance(listImages);
                fragmentSelectedImagesFullSize.setOnSendClickListener(new FragmentChatSelectedImageOrVideoFullSize.OnSendClickListner()
                {
                    @Override
                    public void onSelectedImagesReceived(List<String> selectedImagesPath)
                    {
                        Log.i(TAG,"selected images size after showing in full size"+selectedImagesPath.size());
                        if (mMediaFilesReceivedListener != null)
                            mMediaFilesReceivedListener.onSelectedMediaReceived(selectedImagesPath, Chat.MESSAGE_TYPE_IMAGE);

                        dismiss();
                    }
                });
                fragmentSelectedImagesFullSize.show(getChildFragmentManager(),"");
            }


        }
    }*/




    // method to initialize OnSelectedMediaFilesReceiveListener imagesReceivedListener
    public void setOnMediaFilesReceivedListener(OnSelectedMediaFilesReceiveListener listener)
    {
        this.mMediaFilesReceivedListener = listener;
    }


}
