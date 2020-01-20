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
import org.ctdworld.propath.adapter.AdapterTrainingPlanEdit;
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
import org.ctdworld.propath.model.TrainingPlan.PlanData;
import org.ctdworld.propath.presenter.PresenterTrainingPlan;

import java.util.ArrayList;


public class ActivityTrainingPlanEdit extends BaseActivity implements ContractTrainingPlan.View
{
    private static final String TAG = ActivityTrainingPlanEdit.class.getSimpleName();
    private static final int REQUEST_CODE_IMAGE = 100;
    private static final int REQUEST_CODE_VIDEO = 200;

    private static final int REQUEST_CODE_IMAGE_FROM_ADAPTER = 300; // TO CHANGE IMAGE FROM ADAPTER
    private static final int REQUEST_CODE_VIDEO_FROM_ADAPTER = 400;


    Context mContext;
    private PlanData mTrainingPlanData;
    ContractTrainingPlan.Presenter mPresenter;

    // # toolbar views
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    ImageView mToolbarImgSave;


    //views
    EditText mEditPlanTitle, mEditPlanDescription;
    ImageView mImgMicTitle, mImgMicDescription, mImgPlanItemVideoIcon;


    // # views and variables for link list
    RecyclerView mRecyclerAddPlanItems;
    AdapterTrainingPlanEdit mAdapterTrainingPlanEdit;
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


        mAdapterTrainingPlanEdit = new AdapterTrainingPlanEdit(mContext,onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink = new LinearLayoutManager(mContext);
        mRecyclerAddPlanItems.setLayoutManager(layoutManagerLink);
        mRecyclerAddPlanItems.setAdapter(mAdapterTrainingPlanEdit);


        if (getIntent() != null && getIntent().getExtras() != null)
        {
            Log.i(TAG,"getting training plan");
            mTrainingPlanData = (PlanData) getIntent().getExtras().getSerializable(ConstHelper.Key.TRAINING_PLAN);
            setData(mTrainingPlanData);  // setting data
        }
        else
            Log.e(TAG,"getIntent() or getIntent().getExtras() is null in onCreate()");
        
    }



    // # changing media from adapter
    AdapterTrainingPlanEdit.OnAdapterTrainingPlanCreateListener onAdapterTrainingPlanCreateListener = new AdapterTrainingPlanEdit.OnAdapterTrainingPlanCreateListener() {
        @Override
        public void onChangeImageOrVideoClickedInAdapter()
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
                                    mLink = enteredValue;
                                    if (mAdapterTrainingPlanEdit != null)
                                    {

                                        if (enteredValue != null && !Patterns.WEB_URL.matcher(enteredValue).matches())
                                            DialogHelper.showSimpleCustomDialog(mContext, "Enter valid link");
                                        else
                                        {
                                            int updatingItemPosition = mAdapterTrainingPlanEdit.getUpdatingItemPosition();
                                            if (updatingItemPosition == ConstHelper.NOT_FOUND)
                                                return;

                                            PlanData.PlanItem planItem = mAdapterTrainingPlanEdit.getPlanItemList().get(updatingItemPosition);

                                            if (planItem != null)
                                            {
                                                if (enteredValue == null || enteredValue.isEmpty())
                                                {
                                                    planItem.setLink("");
                                                    planItem.setMediaType("");
                                                }
                                                else
                                                {
                                                    planItem.setLink(enteredValue);
                                                    planItem.setMediaType(PlanData.PlanItem.MEDIA_TYPE_VIDEO);
                                                }
                                                mAdapterTrainingPlanEdit.updateItem(updatingItemPosition, planItem);

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
        /*adding link to note*/
        mImgAddPlanItemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String itemTitle = mEditPlanItemTitle.getText().toString().trim();
                if (!itemTitle.isEmpty())
                {
                    mEditPlanItemTitle.setText("");
                    Log.i(TAG,"adding plan item to list");
                    PlanData.PlanItem planItem = new PlanData.PlanItem();
                    planItem.setTitle(itemTitle);


                    planItem.setMediaUrl(mStrSelectedMediaUri);
                    planItem.setMediaType(mStrSelectedMediaType);
                    planItem.setLink(mLink);


                    // clearing media Uri and media type
                    mStrSelectedMediaUri = null;
                    mStrSelectedMediaType = null;
                    mLink = null;
                    mImgAddPlanItemImageOrVideo.setImageResource(R.drawable.img_default_black);
                    mImgPlanItemVideoIcon.setVisibility(View.GONE);

                    mAdapterTrainingPlanEdit.addPlanItem(planItem);
                    mRecyclerAddPlanItems.scrollToPosition(0);
                }
                else
                    Snackbar.make(mImgAddPlanItemRow, "Please enter item title", Snackbar.LENGTH_SHORT).show();

            }
        });



        mToolbarImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // createNote();
                if (isConnectedToInternet(mContext))
                    save();
                else
                    DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.title_no_connection), getString(R.string.message_no_connection));
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
                                               mLink = null;
                                            else
                                            {
                                                mLink = enteredValue;
                                                mStrSelectedMediaType = null;  // setting null because youtube link is being added
                                                mStrSelectedMediaUri = null;  // setting null because youtube link is being added

                                                String thumbnailUrl = UtilHelper.getYoutubeVideoThumbnailUrl(mLink);
                                                UtilHelper.loadImageWithGlide(mContext,thumbnailUrl,200,200, mImgAddPlanItemImageOrVideo);
                                            }

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

        mToolbarTxtTitle.setText(getString(R.string.edit_training_plan));


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbarImgSave.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mToolbarImgSave.setVisibility(View.VISIBLE);
        mToolbarImgSave.setLayoutParams(params);
        mToolbarImgSave.setImageResource(R.drawable.ic_notes_save);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    // showing creator details and created or updated details
    private void showCreatedDataDetailsFragment()
    {
        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id._fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }


    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails()
    {
        if (mTrainingPlanData == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();

        createdDataDetails.setName(mTrainingPlanData.getCreatedByName());
        createdDataDetails.setRoleId(mTrainingPlanData.getCreatedByRoleId());
        createdDataDetails.setUserPicUrl(mTrainingPlanData.getCreatedByProfilePic());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(mTrainingPlanData.getCreatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(DateTimeHelper.getDateTime(mTrainingPlanData.getCreatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME));

        return createdDataDetails;
    }


    // setting data
    private void setData(PlanData trainingPlanData)
    {
        Log.i(TAG,"setting data in setData() method");
        if (trainingPlanData != null)
        {
            mEditPlanTitle.setText(trainingPlanData.getTitle());
            mEditPlanDescription.setText(trainingPlanData.getDescription());
            if (mAdapterTrainingPlanEdit != null && trainingPlanData.getPlanItemList() != null)
                mAdapterTrainingPlanEdit.addPlanItemList(trainingPlanData.getPlanItemList());
            else
                Log.e(TAG,"mAdapterTrainingPlanEdit or trainingPlanData.getPlanItemArrayList() is null in setData() method");
        }
        else
            Log.e(TAG,"trainingPlanData is null in setData() method");
    }

    

    // to select media from device
    private void selectMedia(int requestCode, String type)
    {
        String permissionTitle = "Permission Required";
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
            permissionHelper.requestPermission(permissionTitle, permissionMessage);
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
                mStrSelectedMediaType = PlanData.PlanItem.MEDIA_TYPE_IMAGE;

                mLink = null;
            }

            else if (requestCode == REQUEST_CODE_VIDEO)
            {
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUri,imageSize, imageSize,mImgAddPlanItemImageOrVideo);
                mStrSelectedMediaType = PlanData.PlanItem.MEDIA_TYPE_VIDEO;
                mImgPlanItemVideoIcon.setVisibility(View.VISIBLE);

                mLink = null;
            }

            // if image requested from adapter
            if (requestCode == REQUEST_CODE_IMAGE_FROM_ADAPTER)
            {
                int updatingItemPosition = mAdapterTrainingPlanEdit.getUpdatingItemPosition();
                if (updatingItemPosition == ConstHelper.NOT_FOUND)
                    return;

                PlanData.PlanItem planItem = mAdapterTrainingPlanEdit.getPlanItemList().get(updatingItemPosition);
                planItem.setMediaUrl(mStrSelectedMediaUri);
                planItem.setMediaType(PlanData.PlanItem.MEDIA_TYPE_IMAGE);
                planItem.setLink(null); // setting null because media from device has been added

                mAdapterTrainingPlanEdit.updateItem(updatingItemPosition, planItem);
            }

            // if video requested from adapter
            else if (requestCode == REQUEST_CODE_VIDEO_FROM_ADAPTER)
            {
               /* if (mAdapterTrainingPlanEdit != null)
                    mAdapterTrainingPlanEdit.updateItem(mAdapterTrainingPlanEdit.getUpdatingItemPosition(), mStrSelectedMediaUri,TrainingPlan.PlanData.PlanItem.MEDIA_TYPE_VIDEO);*/

                if (mAdapterTrainingPlanEdit != null)
                {
                    int updatingItemPosition = mAdapterTrainingPlanEdit.getUpdatingItemPosition();
                    if (updatingItemPosition == ConstHelper.NOT_FOUND)
                        return;

                    TrainingPlan.PlanData.PlanItem planItem = mAdapterTrainingPlanEdit.getPlanItemList().get(updatingItemPosition);
                    planItem.setMediaUrl(mStrSelectedMediaUri);
                    planItem.setMediaType(PlanData.PlanItem.MEDIA_TYPE_VIDEO);
                    planItem.setLink(null);   // setting null because media from device has been added

                    mAdapterTrainingPlanEdit.updateItem(updatingItemPosition, planItem);

                }
            }
        }
    }



    private void save()
    {
        if (mAdapterTrainingPlanEdit != null)
        {
            if (mEditPlanTitle != null && !mEditPlanTitle.getText().toString().trim().isEmpty())
            {
                showLoader(getString(R.string.message_saving));

               // TrainingPlan.PlanData trainingPlan = new TrainingPlan.PlanData();
                mTrainingPlanData.setTitle(mEditPlanTitle.getText().toString().trim());
                mTrainingPlanData.setDescription(mEditPlanDescription.getText().toString().trim());

                mTrainingPlanData.setPlanItemList(mAdapterTrainingPlanEdit.getPlanItemList());
          

                ArrayList<TrainingPlan.PlanData.PlanItem> mEditedPlanItemsList = mAdapterTrainingPlanEdit.getEditedPlanItemsFromSererList();
                for (TrainingPlan.PlanData.PlanItem planItem : mEditedPlanItemsList)
                {
                    Log.i(TAG,"edited item title = "+planItem.getTitle());
                    Log.i(TAG,"edited item image path = "+planItem.getMediaUrl());
                    Log.i(TAG,"edited item link = "+planItem.getLink());
                }

                ArrayList<TrainingPlan.PlanData.PlanItem> mRemovedPlanItemsList = mAdapterTrainingPlanEdit.getRemovedPlanItemsFromServerList();
                for (TrainingPlan.PlanData.PlanItem planItem : mRemovedPlanItemsList)
                {
                    Log.i(TAG,"removed item title = "+planItem.getTitle());
                    Log.i(TAG,"removed item image path = "+planItem.getMediaUrl());
                }

                ArrayList<PlanData.PlanItem> newAddedPlanItemsList = mAdapterTrainingPlanEdit.getNewAddedPlanItemList();
                for (PlanData.PlanItem planItem : newAddedPlanItemsList)
                {
                    Log.i(TAG,"new added item title = "+planItem.getTitle());
                    Log.i(TAG,"new added item image path = "+planItem.getMediaUrl());
                }

                mPresenter.editTrainingPlan(mTrainingPlanData,mEditedPlanItemsList,mRemovedPlanItemsList,newAddedPlanItemsList);
            }
            else
                DialogHelper.showSimpleCustomDialog(mContext,"Please enter title...");

        }
        else
            Log.e(TAG,"mAdapterTrainingPlanEdit is null in save() method");
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

   /* // to change dialog loader title
    public void changeLoaderTitle(String title, FragmentManager fragmentManager)
    {
        if (mLoader == null)
            mLoader = (DialogLoader) fragmentManager.findFragmentByTag(ConstHelper.Tag.Fragment.DIALOG_PROGRESS);

        if (mLoader != null && mLoader.isAdded())
            mLoader.changeProgressTitle(title);
    }*/





    @Override
    public void onTrainingPlanCreated(PlanData trainingPlanData)
    {
        hideLoader();
    }

    @Override
    public void onTrainingPlanReceived(TrainingPlan trainingPlan) {
        hideLoader();
    }

    @Override
    public void onTrainingPlanDeleted(PlanData trainingPlanData) {
        hideLoader();
    }

    @Override
    public void onTrainingPlanEdited(TrainingPlan.PlanData trainingPlanData) {
       // Log.i(TAG,"onTrainingPlanEdited() method called, item size = "+trainingPlanData.getPlanItemList().size());
        hideLoader();
        // sending edited training plan back
        if (trainingPlanData != null)
        {
            //setData(trainingPlanData);
            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.TRAINING_PLAN, trainingPlanData);
            setResult(RESULT_OK, intent);
            finish();
        }
        else
            Log.e(TAG,"trainingPlanData is null in onTrainingPlanEdited() method");
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
