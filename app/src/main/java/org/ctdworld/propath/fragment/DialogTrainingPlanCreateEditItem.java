package org.ctdworld.propath.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogTrainingPlanCreateEditItem extends DialogFragment
{
    Context mContext;
    private static final String TAG = DialogTrainingPlanCreateEditItem.class.getSimpleName();
    private static final int REQUEST_CODE_IMAGE = 100;
    private static final int REQUEST_CODE_VIDEO = 200;

    public static final String KEY_TRAINING_PLAN_ITEM = "training plan";
    public static final String KEY_TYPE_CREATE_OR_EDIT = "create or edit";

    public static final int EXTRA_CREATE = 1;
    public static final int EXTRA_EDIT = 2;


 //   private TrainingPlan.PlanData mTrainingPlan;
    private PlanData.PlanItem mPlanItem;
    private int mTypeCreateOrEdit = 0;
//    private String mSelectedMediaPath;


    TextView mTxtAddImageVideo, mTxtTitle;
    EditText mEditText;
    Button mBtnAddItem;
    ImageView mImgImageOrVideo;
    View mLayoutAddImageVideo;

/*
    String mStrPositiveButton;
    String mStrTitle;
    String mStrEditHint;*/




    public DialogTrainingPlanCreateEditItem() { }  // constructor



    public interface OnButtonClickListener   // listener
    {
        void onAddButtonClicked(PlanData.PlanItem planItem);
    }


    private OnButtonClickListener mListener;

    // to get instance and set arguments for button title
    public static DialogTrainingPlanCreateEditItem getInstance(PlanData.PlanItem planItem, int typeCreateEdit)
    {
        DialogTrainingPlanCreateEditItem dialogTrainingPlanCreateEditItem = new DialogTrainingPlanCreateEditItem();
        Bundle bundle = new Bundle();
       // bundle.putInt(KEY_VALUE_TYPE,createType);
        bundle.putSerializable(KEY_TRAINING_PLAN_ITEM,planItem);
        bundle.putInt(KEY_TYPE_CREATE_OR_EDIT,typeCreateEdit);
        dialogTrainingPlanCreateEditItem.setArguments(bundle);

        return dialogTrainingPlanCreateEditItem;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            Bundle bundle = getArguments();
            try
            {
                //mTrainingPlan = bundle.getParcelable(KEY_TRAINING_PLAN);
                mPlanItem = (PlanData.PlanItem) bundle.getSerializable(KEY_TRAINING_PLAN_ITEM);
                mTypeCreateOrEdit = bundle.getInt(KEY_TYPE_CREATE_OR_EDIT);
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error in onCreate() method , "+e.getMessage());
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Log.i(TAG,"onCreateView() method called");
        return inflater.inflate(R.layout.dialog_training_plan_create_item, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try
        {
            Log.i(TAG,"onViewCreated() method called");
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().setCanceledOnTouchOutside(false);

            mContext =  getContext();
            mTxtAddImageVideo = view.findViewById(R.id.dialog_training_plan_create_item_txt_add_image_video);
            mEditText = view.findViewById(R.id.dialog_training_plan_create_item_edit_title);
            mBtnAddItem =  view.findViewById(R.id.dialog_training_plan_create_item_btn_create);
            mLayoutAddImageVideo = view.findViewById(R.id.dialog_training_plan_create_item_layout_add_image_video);
            mImgImageOrVideo = view.findViewById(R.id.dialog_training_plan_create_item_img_add_image_video);
            mTxtTitle = view.findViewById(R.id.dialog_training_plan_create_item_txt_title);


            if (mTypeCreateOrEdit == EXTRA_EDIT)
            {
                mTxtTitle.setText("Update Training PlanData Item");
                mBtnAddItem.setText("Update");
            }


            if (mPlanItem != null) {
                mEditText.setText(mPlanItem.getTitle());
                mEditText.requestFocus(View.FOCUS_RIGHT);

                /*Glide.with(mContext)
                        .load(mPlanItem.getMediaUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                        .into(mImgImageOrVideo);*/


                String link = "";
                if (mPlanItem.getLink() != null && !mPlanItem.getLink().isEmpty()) {
                    link = UtilHelper.getYoutubeVideoThumbnailUrl(mPlanItem.getLink());
                    mPlanItem.setMediaType(PlanData.PlanItem.MEDIA_TYPE_VIDEO);
                } else
                    link = mPlanItem.getMediaUrl();

                if (link != null && !link.isEmpty()) {
                   /* if (TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO.equals(mPlanItem.getMediaType()))
                        imgVideoIcon.setVisibility(View.VISIBLE);
                    else
                        imgVideoIcon.setVisibility(View.GONE);*/
                    UtilHelper.loadImageWithGlide(mContext, link, 200, 200, mImgImageOrVideo);

                }
            }

            mLayoutAddImageVideo.setOnClickListener(onAddMediaClicked);  // adding media image or video
            mBtnAddItem.setOnClickListener(onCreateButtonClicked);   // adding training plan item


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() method called");
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
        {
            if (mPlanItem == null)
               return;

            mPlanItem.setMediaUrl(FileHelper.getFilePath(mContext, data.getData()));

            if (mImgImageOrVideo != null)
            {
                    Log.i(TAG,"showing image or video with glide in onActivityResult() method");
                        Glide.with(mContext)
                                .load(data.getData())
                                .apply(new RequestOptions().error(R.drawable.img_default_black))
                                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                                .into(mImgImageOrVideo);

            }

            if (requestCode == REQUEST_CODE_IMAGE)
            {
                mTxtAddImageVideo.setText(FileHelper.getFileName(mContext, data.getData()));
                mPlanItem.setMediaType(TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_IMAGE);
                mPlanItem.setLink(null);
            }

            else if (requestCode == REQUEST_CODE_VIDEO)
            {
                mTxtAddImageVideo.setText(FileHelper.getFileName(mContext, data.getData()));
                mPlanItem.setMediaType(PlanData.PlanItem.MEDIA_TYPE_VIDEO);
                mPlanItem.setLink(null);
            }
        }
    }


    // listener to create training plan item
    View.OnClickListener onCreateButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG,"onClick() method called... ");

            String planItemTitle = mEditText.getText().toString().trim();
            if (planItemTitle.isEmpty() /* || mPlanItem.getMediaUrl() == null|| mPlanItem.getMediaUrl().isEmpty()*/)
                DialogHelper.showSimpleCustomDialog(mContext,"Please enter title or link");
            else
                {
                    Log.i(TAG,"media path = "+mPlanItem.getMediaUrl());
                    Log.i(TAG,"media type = "+mPlanItem.getMediaType());

                    mPlanItem.setTitle(planItemTitle);
                   // mTrainingPlan.setPlanItem(mPlanItem);

                    if (mListener != null)
                        mListener.onAddButtonClicked(mPlanItem);

                    dismiss();
                }
        }
    };


    // listener to select media from device
    View.OnClickListener onAddMediaClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG,"onClick() method called... ");

            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_LINK, "Add Link")
                    .addOption(BottomSheetOption.OPTION_IMAGE, "Select Image")
                    .addOption(BottomSheetOption.OPTION_VIDEO, "Select Video");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    Log.i(TAG,"onOptionSelected() method called, option = "+option);
                    switch (option)
                    {
                        case BottomSheetOption.OPTION_LINK:

                            DialogEditText dialogEditText = DialogEditText.getInstance("Item Link", "Add Link", false, "Add", true);
                            dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                                @Override
                                public void onButtonClicked(String enteredValue) {

                                    if (enteredValue != null && !Patterns.WEB_URL.matcher(enteredValue).matches())
                                        DialogHelper.showSimpleCustomDialog(mContext, "Enter valid link");
                                    else
                                    {
                                        Log.i(TAG,"youtube link = "+enteredValue);

                                        if (enteredValue == null || enteredValue.isEmpty())
                                            mPlanItem.setLink(null);
                                        else
                                        {
                                            mPlanItem.setLink(enteredValue);
                                            mPlanItem.setMediaType(null);  // setting null because youtube link is being added
                                            mPlanItem.setMediaUrl(null);  // setting null because youtube link is being added

                                            String thumbnailUrl = UtilHelper.getYoutubeVideoThumbnailUrl(enteredValue);
                                            UtilHelper.loadImageWithGlide(mContext,thumbnailUrl,200,200, mImgImageOrVideo);
                                        }

                                    }
                                }
                            });
                            dialogEditText.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);

                            break;

                        case BottomSheetOption.OPTION_IMAGE:
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, REQUEST_CODE_IMAGE);
                            break;

                        case BottomSheetOption.OPTION_VIDEO:
                            Intent intentVideo = new Intent(Intent.ACTION_GET_CONTENT);
                            intentVideo.setType("video/*");
                            startActivityForResult(intentVideo, REQUEST_CODE_VIDEO);
                            break;

                        /*case BottomSheetOption.OPTION_REMOVE:
                            Uri uri = Uri.parse("android.resource://your.package.here/drawable/img_default_black");
                            Log.i(TAG,"removed image URI = "+uri);

                            String path = FileHelper.getFilePath(mContext, uri);
                            Log.i(TAG,"removed image Path = "+path);

                            break;*/

                    }
                }
            });

            options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

          /* FragmentOptionsBottomSheet fragmentOptionsBottomSheet = new FragmentOptionsBottomSheet();
           fragmentOptionsBottomSheet.setListener(onMediaTypeOptionSelected);
           fragmentOptionsBottomSheet.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_CREATE_TRAINING_PLAN_ITEM);*/
        }
    };


    // listener, when image or video option is selected in bottom sheet fragment
/*    FragmentOptionsBottomSheet.OnOptionSelectedListener onMediaTypeOptionSelected = new FragmentOptionsBottomSheet.OnOptionSelectedListener() {
        @Override
        public void onOptionSelected(int selectedItem)
        {
            String permissionTitle = "Permission Required";
            String permissionMessage = "Without storage permission you can not upload file.";

            PermissionHelper permissionHelper = new PermissionHelper(mContext);
            if (permissionHelper.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);

                switch (selectedItem)
                {
                    case FragmentOptionsBottomSheet.OPTION_IMAGE:
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE_IMAGE);
                        break;

                    case FragmentOptionsBottomSheet.OPTION_VIDEO:
                        intent.setType("video/*");
                        startActivityForResult(intent, REQUEST_CODE_VIDEO);
                        break;
                }

            }
            else
                permissionHelper.requestPermission(permissionTitle, permissionMessage);
        }
    };*/




    public void setOnButtonClickListener(OnButtonClickListener listener)
    {
        this.mListener = listener;
    }

}
