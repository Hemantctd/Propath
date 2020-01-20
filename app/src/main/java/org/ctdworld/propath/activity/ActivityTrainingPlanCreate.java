package org.ctdworld.propath.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterTrainingPlanCreate;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractTrainingPlan;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterTrainingPlan;



public class ActivityTrainingPlanCreate extends BaseActivity implements ContractTrainingPlan.View
{
    private static final String TAG = ActivityTrainingPlanCreate.class.getSimpleName();
    private static final int REQUEST_CODE_IMAGE = 100;
    private static final int REQUEST_CODE_VIDEO = 200;

    private static final int REQUEST_CODE_IMAGE_FROM_ADAPTER = 300; // TO CHANGE IMAGE FROM ADAPTER
    private static final int REQUEST_CODE_VIDEO_FROM_ADAPTER = 400;



    Context mContext;
    ContractTrainingPlan.Presenter mPresenter;

    // # toolbar views
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    ImageView mToolbarImgSave;


    //views
    EditText mEditPlanTitle, mEditPlanDescription;
    ImageView mImgMicTitle, mImgMicDescription, mImgPlanItemVideoIcon;
   // NestedScrollView mNestedScrollVew;


    // # views and variables for items
    RecyclerView mRecyclerAddPlanItems;
    AdapterTrainingPlanCreate mAdapterTrainingPlanItem;
    ImageView mImgAddPlanItemRow, mImgAddPlanItemImageOrVideo;
    EditText mEditPlanItemTitle;


    DialogLoader mLoader;

    // # variables
    String mStrSelectedMediaUri = null;
    String mStrSelectedMediaType = null;
    String mLink;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan_create);


        init();
        setToolbar();
        showCreatedDataDetailsFragment();  // showing creator details and created or updated details


        // adding default icon, if icon is added in xml then tint color doesn't work in CircleImageView,
        mImgAddPlanItemImageOrVideo.setImageResource(R.drawable.img_default_black);
        setListener();

    }


    // to initialize
    private void init()
    {
        mContext = this;
        mPresenter = new PresenterTrainingPlan(mContext, this);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbarImgSave = findViewById(R.id.toolbar_img_1_customisable);

        mImgMicTitle = findViewById(R.id.activity_training_plan_create_img_mic_plan_title);
        mImgMicDescription = findViewById(R.id.activity_training_plan_create_img_mic_plan_description);
       // mTxtChangeCategory = findViewById(R.id.activity_note_create_txt_change_category);
        mEditPlanTitle = findViewById(R.id.activity_training_plan_create_edit_plan_title);
        mEditPlanDescription = findViewById(R.id.activity_training_plan_create_edit_description);
        mEditPlanItemTitle = findViewById(R.id.activity_training_plan_create_edit_plan_item_title);
        mImgAddPlanItemImageOrVideo = findViewById(R.id.activity_training_plan_create__plan_item_image_or_video);
        mImgPlanItemVideoIcon = findViewById(R.id.activity_training_plan_create__plan_item_img_video_icon);
        mImgAddPlanItemRow = findViewById(R.id.activity_training_plan_create_img_add_plan_item_row);
        mRecyclerAddPlanItems = findViewById(R.id.activity_training_plan_create_recycler_plan_item);


        mAdapterTrainingPlanItem = new AdapterTrainingPlanCreate(mContext,onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink = new LinearLayoutManager(mContext);
        mRecyclerAddPlanItems.setLayoutManager(layoutManagerLink);
        mRecyclerAddPlanItems.setAdapter(mAdapterTrainingPlanItem);

    }


    AdapterTrainingPlanCreate.OnAdapterTrainingPlanCreateListener onAdapterTrainingPlanCreateListener = new AdapterTrainingPlanCreate.OnAdapterTrainingPlanCreateListener() {
        @Override
        public void onChangeImageOrVideoClickedInAdapter()
        {
            Log.i(TAG,"onChangeImageOrVideoClickedInAdapter() method called");

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
                                    mLink = enteredValue;
                                    if (mAdapterTrainingPlanItem != null)
                                    {

                                        if (enteredValue != null && !Patterns.WEB_URL.matcher(enteredValue).matches())
                                            DialogHelper.showSimpleCustomDialog(mContext, "Enter valid link");
                                        else
                                        {
                                            int updatingItemPosition = mAdapterTrainingPlanItem.getUpdatingItemPosition();
                                            if (updatingItemPosition == ConstHelper.NOT_FOUND)
                                                return;

                                            TrainingPlan.PlanData.PlanItem planItem = mAdapterTrainingPlanItem.getPlanItemList().get(updatingItemPosition);
                                            if (planItem != null)
                                            {
                                                planItem.setLink(enteredValue);
                                                planItem.setMediaType(TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO);
                                                mAdapterTrainingPlanItem.updateItem(updatingItemPosition, planItem);

                                            }
                                        }

                                    }
                                }
                            });
                            dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);

                            break;

                        case BottomSheetOption.OPTION_IMAGE:
                            selectMedia(REQUEST_CODE_IMAGE_FROM_ADAPTER,"image/*");
                            break;

                        case BottomSheetOption.OPTION_VIDEO:
                            selectMedia(REQUEST_CODE_VIDEO_FROM_ADAPTER,"video/*");
                            break;
                    }
                }
            });

            options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

        }
    };

    private void setListener()
    {
        mImgAddPlanItemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String itemTitle = mEditPlanItemTitle.getText().toString().trim();
                if (!itemTitle.isEmpty())
                {
                    mEditPlanItemTitle.setText("");
                    Log.i(TAG,"adding plan item to list");
                    TrainingPlan.PlanData.PlanItem planItem = new TrainingPlan.PlanData.PlanItem();
                    planItem.setTitle(itemTitle);

                    planItem.setMediaUrl(mStrSelectedMediaUri);
                    planItem.setMediaType(mStrSelectedMediaType);
                    planItem.setLink(mLink);

                    // clearing previous data
                    mStrSelectedMediaUri = null;
                    mStrSelectedMediaType = null;
                    mLink = null;
                    mImgAddPlanItemImageOrVideo.setImageResource(R.drawable.img_default_black);
                    mImgPlanItemVideoIcon.setVisibility(View.GONE);

                    mAdapterTrainingPlanItem.addPlanItem(planItem);
                   // mRecyclerAddPlanItems.scrollToPosition(0);

                //   mNestedScrollVew.fullScroll(View.FOCUS_DOWN);

                }
                else
                    Snackbar.make(mImgAddPlanItemRow, "Please enter item title", Snackbar.LENGTH_SHORT).show();

            }
        });



        mToolbarImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // createNote();
                if (mAdapterTrainingPlanItem != null)
                {
                    if (isConnectedToInternet(mContext))
                        savePlan();
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.title_no_connection), getString(R.string.message_no_connection));
                }
            }
        });



        mImgMicTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
                else
                {
                    FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                    fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                        @Override
                        public void onReceiveText(String spokenText) {
                            if (spokenText != null && !spokenText.isEmpty())
                                mEditPlanTitle.setText(spokenText);
                            mEditPlanTitle.requestFocus(EditText.FOCUS_RIGHT);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    fragmentSpeechRecognition.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.SPEECH_RECOGNITION);
                }

            }
        });


        mImgMicDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
                else
                {
                    FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                    fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                        @Override
                        public void onReceiveText(String spokenText) {
                            if (spokenText != null && !spokenText.isEmpty())
                                mEditPlanDescription.setText(spokenText);
                            mEditPlanDescription.requestFocus(EditText.FOCUS_RIGHT);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    fragmentSpeechRecognition.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.SPEECH_RECOGNITION);
                }

            }
        });


        // adding item image or video
        mImgAddPlanItemImageOrVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_LINK, "Add Link")
                        .addOption(BottomSheetOption.OPTION_IMAGE, "Select Image")
                        .addOption(BottomSheetOption.OPTION_VIDEO, "Select Video");

                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option) {
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
                                            if (enteredValue == null)
                                                return;

                                            mLink = enteredValue;
                                            String thumbnailUrl = UtilHelper.getYoutubeVideoThumbnailUrl(mLink);
                                            UtilHelper.loadImageWithGlide(mContext,thumbnailUrl,200,200, mImgAddPlanItemImageOrVideo);
                                        }
                                        }
                                });
                                dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);

                                break;


                            case BottomSheetOption.OPTION_IMAGE:

                                selectMedia(REQUEST_CODE_IMAGE,"image/*");
                                break;

                            case BottomSheetOption.OPTION_VIDEO:

                                selectMedia(REQUEST_CODE_VIDEO,"video/*");
                                break;
                        }
                    }
                });

                options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

            }
        });

    }




    // showing creator details and created or updated details
    private void showCreatedDataDetailsFragment() {
        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id._fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }




    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails()
    {
        User user = SessionHelper.getInstance(mContext).getUser();
        if (user == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(user.getName());
        createdDataDetails.setRoleId(user.getRoleId());
        createdDataDetails.setUserPicUrl(user.getUserPicUrl());
        createdDataDetails.setCreatedDate(DateTimeHelper.getCurrentSystemDateTime());
        createdDataDetails.setUpdatedDate(null);

        return createdDataDetails;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }



    // setting toolbar data
    private void setToolbar() {
        setSupportActionBar(mToolbar);

        mToolbarTxtTitle.setText(getString(R.string.create_training_plan));


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbarImgSave.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mToolbarImgSave.setVisibility(View.VISIBLE);
        mToolbarImgSave.setLayoutParams(params);
        mToolbarImgSave.setImageResource(R.drawable.ic_notes_save);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }



    // to select media from device
    private void selectMedia(int requestCode, String type)
    {
        String permissionMessage = "Without storage permission you can not upload file.";

        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (permissionHelper.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType(type);
            startActivityForResult(intent, requestCode);
        }
        else
        {
            Log.e(TAG,"storage permission is not granted in selectMedia");
            permissionHelper.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, permissionMessage);
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() method called ");
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null )
        {
           // mTrainingPlanItem.setMediaUrl(FileHelper.getFilePath(mContext, data.getData()));
            mStrSelectedMediaUri = FileHelper.getFilePath(mContext, data.getData());
            int imageSize = UtilHelper.convertDpToPixel(mContext, (int) getResources().getDimension(R.dimen.trainingPlanItemImageSize));

            Log.i(TAG,"mStrSelectedMediaUri = "+mStrSelectedMediaUri);

            if (mStrSelectedMediaUri == null)
                return;

            if (requestCode == REQUEST_CODE_IMAGE)
            {
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUri,imageSize, imageSize,mImgAddPlanItemImageOrVideo);
                mStrSelectedMediaType = TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_IMAGE;
            }

            else if (requestCode == REQUEST_CODE_VIDEO)
            {
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUri,imageSize, imageSize,mImgAddPlanItemImageOrVideo);
                mStrSelectedMediaType = TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO;
                mImgPlanItemVideoIcon.setVisibility(View.VISIBLE);
            }

            // if image requested from adapter
            if (requestCode == REQUEST_CODE_IMAGE_FROM_ADAPTER)
            {
                if (mAdapterTrainingPlanItem != null)
                {
                    int updatingItemPosition = mAdapterTrainingPlanItem.getUpdatingItemPosition();
                    if (updatingItemPosition == ConstHelper.NOT_FOUND)
                        return;

                    TrainingPlan.PlanData.PlanItem planItem = mAdapterTrainingPlanItem.getPlanItemList().get(updatingItemPosition);
                    planItem.setMediaUrl(mStrSelectedMediaUri);
                    planItem.setMediaType(TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_IMAGE);

                    mAdapterTrainingPlanItem.updateItem(updatingItemPosition, planItem);

                }
            }

            // if video requested from adapter
            else if (requestCode == REQUEST_CODE_VIDEO_FROM_ADAPTER)
            {
               /* if (mAdapterTrainingPlanItem != null)
                    mAdapterTrainingPlanItem.updateItem(mAdapterTrainingPlanItem.getUpdatingItemPosition(), mStrSelectedMediaUri,TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO);*/

                if (mAdapterTrainingPlanItem != null)
                {
                    int updatingItemPosition = mAdapterTrainingPlanItem.getUpdatingItemPosition();
                    if (updatingItemPosition == ConstHelper.NOT_FOUND)
                        return;

                    TrainingPlan.PlanData.PlanItem planItem = mAdapterTrainingPlanItem.getPlanItemList().get(updatingItemPosition);
                    planItem.setMediaUrl(mStrSelectedMediaUri);
                    planItem.setMediaType(TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO);

                    mAdapterTrainingPlanItem.updateItem(updatingItemPosition, planItem);

                }
            }
        }
    }


    // # saving training plan
    private void savePlan()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            if (!mEditPlanTitle.getText().toString().trim().isEmpty())
            {
                showLoader(getString(R.string.message_saving));
                TrainingPlan.PlanData trainingPlanData = new TrainingPlan.PlanData();
                trainingPlanData.setTitle(mEditPlanTitle.getText().toString().trim());
                trainingPlanData.setDescription(mEditPlanDescription.getText().toString().trim());

                trainingPlanData.setPlanItemList(mAdapterTrainingPlanItem.getPlanItemList());

                mPresenter.createTrainingPlan(trainingPlanData);
            }
            else
                DialogHelper.showSimpleCustomDialog(mContext, "Please enter title...");
        }
        else
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.title_no_connection));
    }



    // to show dialog loader or change title of loader
    public void showLoader(String title)
    {
        if (mLoader == null)
        {
            mLoader = DialogLoader.getInstance(title);
            mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        }
        else if (mLoader.isAdded())
            mLoader.changeProgressTitle(title);


    }

    // to hide dialog loader
    public void hideLoader()
    {
        if (mLoader != null && mLoader.isAdded())
            mLoader.dismiss();
    }


    @Override
    public void onTrainingPlanCreated(TrainingPlan.PlanData trainingPlanData)
    {
        Log.i(TAG,"onTrainingPlanCreated() method called, item size = "+ trainingPlanData.getPlanItemList().size());
        hideLoader();
        // sending created training plan back

        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.TRAINING_PLAN, trainingPlanData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTrainingPlanReceived(TrainingPlan trainingPlan) {

    }


    @Override
    public void onTrainingPlanDeleted(TrainingPlan.PlanData trainingPlanData) {
        hideLoader();
    }

    @Override
    public void onTrainingPlanEdited(TrainingPlan.PlanData trainingPlanData) {
        hideLoader();
    }

    @Override
    public void onTrainingPlanShared() {
        hideLoader();
    }

    @Override
    public void onFailed(String message) {
        hideLoader();
    }

    @Override
    public void onShowMessage(String message) {
        hideLoader();
    }

}
