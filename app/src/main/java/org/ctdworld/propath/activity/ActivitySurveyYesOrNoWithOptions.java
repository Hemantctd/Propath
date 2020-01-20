package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterYesOrNoSurveyQuestionsWithOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.utils.UiHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ActivitySurveyYesOrNoWithOptions extends AppCompatActivity implements View.OnClickListener,AdapterYesOrNoSurveyQuestionsWithOptions.OnAdapterSurveyYesNoListener{
    public static final String TAG =  ActivitySurveyYesOrNoWithOptions.class.getSimpleName();
  //  private static final int REQUEST_CODE_IMAGE_FROM_ADAPTER = 300; // TO CHANGE IMAGE FROM ADAPTER

   /* public static final String SURVEY_QUESTIONS_LIST =  "survey_questions_items";
    public static final String SURVEY_YES_NO =  "survey_yes_no_items";
    public static final String KEY_SURVEY_CREATE_EDIT =  "survey_type";*/

    Button mBtnNext;
    Context mContext;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;   // toolbar
    TextView mTxtToolbarTitle;  // for toolbar title
    Bundle mBundle;
    List<Survey.SurveyData.YesNo.Questions> questionsList;
    Survey.SurveyData.YesNo surveyYesNo;
    AdapterYesOrNoSurveyQuestionsWithOptions adapter;
    String mStrSelectedMediaUri = null;
    String mSurveyType;
    String currentPhotoPath = "";
    private UiHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_or_no);


        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            mSurveyType = mBundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
            surveyYesNo = (Survey.SurveyData.YesNo) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_YES_NO);
            if (surveyYesNo != null) {
                questionsList = surveyYesNo.getSurveyYesNoQuestionList();
            }
        }

        init();
        prepareToolbar();
        prepareRecyclerView();
    }

    private void init ()
    {
        mContext =  this;
        uiHelper = new UiHelper();
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mTxtToolbarTitle.setText(R.string.yes_no);
        mBtnNext = findViewById(R.id.btn_next);
        mRecyclerView = findViewById(R.id.recycler_yes_no_questions);
        mBtnNext.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            List<Survey.SurveyData.YesNo.Questions> list = adapter.getSurveyQusetions();
            surveyYesNo.setSurveyYesNoQuestionList(list);

            if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)) {

                Intent intent = new Intent(mContext, ActivitySurveyYesOrNoQuestionsPreview.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                startActivity(intent);
                finish();
            } else if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {

                Intent intent = new Intent(mContext, ActivitySurveyYesOrNoQuestionsPreview.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mContext, ActivitySurveyYesOrNoQuestionsPreview.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);
                startActivity(intent);
                finish();
            }
        }
    }

    private void prepareRecyclerView()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterYesOrNoSurveyQuestionsWithOptions(mContext, questionsList, this);
        mRecyclerView.setAdapter(adapter);

    }
    private void prepareToolbar()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    // to select media from device
    private void selectMedia() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (uiHelper.checkSelfPermissions(this)) {
                Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pictureIntent.setType("image/*");
                pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
                pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), UiHelper.PICK_IMAGE_GALLERY_REQUEST_CODE);

            }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = UCrop.getOutput(data);
                showImage(uri);
            }
        } else if (requestCode == UiHelper.PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Uri sourceUri = data.getData();
                File file = getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                openCropActivity(sourceUri, destinationUri);
            } catch (Exception e) {
                uiHelper.toast(this, "Please select another image");
            }
        }
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        System.out.println(storageDir.getAbsolutePath());
        if (storageDir.exists())
            System.out.println("File exists");
        else
            System.out.println("File not exists");
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }





    private void showImage(Uri imageUri) {
        try {

            mStrSelectedMediaUri = FileHelper.getFilePath(mContext, imageUri);
            if (adapter != null)
            {
                int updatingItemPosition = adapter.getUpdatingItemPosition();
                if (updatingItemPosition == ConstHelper.NOT_FOUND)
                    return;

                Survey.SurveyData.YesNo.Questions surveyQuestions = adapter.getSurveyQusetions().get(updatingItemPosition);
                surveyQuestions.setQuestionImageLink(mStrSelectedMediaUri);
                adapter.updateItem(updatingItemPosition, surveyQuestions);


            }
         /*   if (adapter != null)
            {
                int updatingItemPosition = adapter.getUpdatingItemPosition();
                if (updatingItemPosition == ConstHelper.NOT_FOUND)
                    return;

                Survey.SurveyData.MultipleChoice.Questions surveyQuestions = adapter.getSurveyQusetions().get(updatingItemPosition);
                surveyQuestions.setQuestionImageLink(mStrSelectedMediaUri);

                Log.i(TAG,"optionlist" + surveyQuestions.getOptionList().get(0));

                adapter.updateItem(updatingItemPosition, surveyQuestions);

            }*/


        } catch (Exception e) {
            uiHelper.toast(this, "Please select different profile picture.");
        }
    }


    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, (int) getResources().getDimension(R.dimen.newsFeedPostImageHeight));
        int croppedImageWidth = UtilHelper.getScreenWidth(mContext);
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(16,9)
                .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                .start(this);
    }



    @Override
    public void onChangeImageClickedInAdapter() {
        selectMedia();

    }


/*
    // to select media from device
    private void selectMedia() {
       // String permissionTitle = "Permission Required";
        String permissionMessage = "Without storage permission you can not upload file.";

        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (permissionHelper.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, ActivitySurveyYesOrNoWithOptions.REQUEST_CODE_IMAGE_FROM_ADAPTER);
        } else {
            Log.e(TAG, "storage permission is not granted in selectMedia");
            permissionHelper.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, permissionMessage);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult() method called ");


        if (resultCode == UCrop.RESULT_ERROR)
            Log.e(TAG, "Error: uCrop result Uri, Error = "+UCrop.EXTRA_ERROR);

        if (resultCode == RESULT_OK && data != null && requestCode == REQUEST_CODE_IMAGE_FROM_ADAPTER )
            cropImage(data.getData());

        else if (requestCode == UCrop.REQUEST_CROP) {

            if (data == null)
                return;

            final Uri resultUri = UCrop.getOutput(data);
            mStrSelectedMediaUri = FileHelper.getFilePath(mContext, resultUri);

            if (adapterYesOrNoSurveyQuestions != null)
            {
                int updatingItemPosition = adapterYesOrNoSurveyQuestions.getUpdatingItemPosition();
                if (updatingItemPosition == ConstHelper.NOT_FOUND)
                    return;

                Survey.SurveyData.YesNo.Questions surveyQuestions = adapterYesOrNoSurveyQuestions.getSurveyQusetions().get(updatingItemPosition);
                surveyQuestions.setQuestionImageLink(mStrSelectedMediaUri);
                adapterYesOrNoSurveyQuestions.updateItem(updatingItemPosition, surveyQuestions);


            }

        }
    }


    @Override
    public void onChangeImageClickedInAdapter() {
        selectMedia();

    }

    // # cropping selected image
    private void cropImage(Uri uri)
    {
        int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, (int) getResources().getDimension(R.dimen.newsFeedPostImageHeight));
        int croppedImageWidth = UtilHelper.getScreenWidth(mContext);
        try {
            UCrop.of(uri, Uri.parse(FileHelper.getFilePath(mContext, uri)))
                    .withAspectRatio(16,9)
                    .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                    .start(ActivitySurveyYesOrNoWithOptions.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/





}
