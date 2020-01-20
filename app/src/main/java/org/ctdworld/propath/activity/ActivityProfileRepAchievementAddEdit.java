package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;

import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractRepAchievement;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.ImageHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.helper.VideoHelper;
import org.ctdworld.propath.model.RepAchievement;
import org.ctdworld.propath.presenter.PresenterRepAchievement;

import java.io.IOException;
import java.util.List;

public class ActivityProfileRepAchievementAddEdit extends BaseActivity implements View.OnClickListener, ContractRepAchievement.View {

    private final String TAG = ActivityProfileRepAchievementAddEdit.class.getSimpleName();
    public static final String TYPE_EDIT = "edit";

    public static final String KEY_RESPONSE_IS_ADDED_OR_EDITED = "key response";
    public static final String REP_Type ="rep_type";
    public static final String REP_DATA = "rep_data" ;
    public static final String REP_ID ="rep_id";

    //DialogLoader mLoader;

    Context mContext;
    Spinner mSpinRepFromMonth,mSpinRepFromYear,mSpinRepToMonth,mSpinRepToYear;
    EditText mEditRepTeam,mEditRepLocation,mEditRepRole,mEditLink;
    String mTypeAddOrEdit;
    int fromYearPosition=0,toYearPosition = 0,toMonthPosition,fromMonthPosition;
    //Button profile_rep_btn_link;
    ImageView toolBarImageOptionsSave;
    RepAchievement mRepAchievement;
    ImageHelper imageHelper;
    PresenterRepAchievement mPresenter;
    ImageView mMediaImage;


    private String mSelectedRepMedia = null;  // selected rep media will be stored in this
    private int REQUEST_CODE_REP_MEDIA = 200;
    private static final String UPLOAD_IMAGE_TYPE_JPEG = "data:image/jpeg;base64,";
    private static final String UPLOAD_VIDEO_TYPE_MP4 = "data:video/mp4;base64,";

    String strLink = "",getREP_ID; // contains link which will come from FragmentAddRepAchievement

    Toolbar mToolbar;
    TextView mToolbarTitle;

    ImageView profile_rep_btn_upload;
    private PermissionHelper mPermissionHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_rep_achievement_add_edit);
        init();
        setToolbar();
    }

    private void init()
    {
        mContext = this;

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);

        mPresenter = new PresenterRepAchievement(mContext,this);
        mPermissionHelper = new PermissionHelper(mContext);
        imageHelper = new ImageHelper(mContext);
        mEditRepLocation = findViewById(R.id.profile_rep_edit_location);
        mEditRepTeam = findViewById(R.id.profile_rep_edit_team);
        mSpinRepFromMonth = findViewById(R.id.profile_rep_spin_from_month);
        mSpinRepFromYear = findViewById(R.id.profile_rep_spin_from_year);
        mSpinRepToMonth = findViewById(R.id.profile_rep_spin_to_month);
        mSpinRepToYear = findViewById(R.id.profile_rep_spin_to_year);
        mEditRepRole = findViewById(R.id.profile_rep_edit_role_position);
        mEditLink = findViewById(R.id.link);
        profile_rep_btn_upload = findViewById(R.id.profile_rep_btn_upload);
      //  profile_rep_btn_link = findViewById(R.id.profile_rep_btn_link);
        mMediaImage = findViewById(R.id.media_image);
      //  profile_rep_btn_add_achievement = findViewById(R.id.profile_rep_btn_add_achievement);

        toolBarImageOptionsSave = findViewById(R.id.toolbar_img_options_menu);
        toolBarImageOptionsSave.setImageResource(R.drawable.ic_notes_save);
        toolBarImageOptionsSave.setVisibility(View.VISIBLE);
        toolBarImageOptionsSave.setOnClickListener(this);
        profile_rep_btn_upload.setOnClickListener(this);
    //    profile_rep_btn_link.setOnClickListener(this);

      Bundle mBundle = getIntent().getExtras();
      if (mBundle !=  null)
      {
          mTypeAddOrEdit = mBundle.getString(REP_Type);
          getREP_ID = mBundle.getString(REP_ID);
          mRepAchievement = (RepAchievement) mBundle.getSerializable(REP_DATA);

      }

      if (mTypeAddOrEdit.equals(TYPE_EDIT))
      {

         // profile_rep_btn_add_achievement.setText("Edit Achievement");
          mEditRepTeam.setText(mRepAchievement.getRepTeam());
          mEditRepLocation.setText(mRepAchievement.getRepLocation());
          mEditRepRole.setText(mRepAchievement.getRepRole());
          mEditLink.setText(mRepAchievement.getRepLink());

          String yearArray[] = getResources().getStringArray(R.array.entries_year);
          String monthArray[] = getResources().getStringArray(R.array.entries_month);

          for (int x = 0; x < monthArray.length; x++)
          {
              if (mRepAchievement.getRepFromMonth().contains(monthArray[x]))
              {
                  fromMonthPosition = x;
                  break;
              }
          }

          for (int y = 0; y < monthArray.length ; y++)
          {

              if (mRepAchievement.getRepToMonth().contains(monthArray[y]))
              {
                  toMonthPosition = y;
                  break;
              }

          }
          for (int i=0; i<yearArray.length; i++)
          {
              if (mRepAchievement.getRepFromYear().contains(yearArray[i]))
              {
                  fromYearPosition = i;
                  break;
              }
          }

          for (int j = 0 ; j<yearArray.length ; j++)
          {
              if (mRepAchievement.getRepToYear().contains(yearArray[j]))
              {
                  toYearPosition = j;
                  break;
              }
          }

          mSpinRepFromYear.setSelection(fromYearPosition);
          mSpinRepToYear.setSelection(toYearPosition);
          mSpinRepToMonth.setSelection(toMonthPosition);
          mSpinRepFromMonth.setSelection(fromMonthPosition);


          int imageWidth = UtilHelper.convertDpToPixel(mContext,80);
          int imageHeight = UtilHelper.convertDpToPixel(mContext,80);
          // mMediaImage.setImageResource();
          Glide.with(mContext)
                  .load(mRepAchievement.getRepMediaUrl())
                  .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                  .apply(new RequestOptions().error(R.drawable.ic_profile))
                  .apply(new RequestOptions().override(imageWidth, imageHeight))
                  .into(mMediaImage);


      }
      else if (mTypeAddOrEdit.equals("add"))
      {
          Log.d(TAG,"add called");
      }

    }


    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        if (TYPE_EDIT.contains(mTypeAddOrEdit))
            mToolbarTitle.setText("Edit Rep Achievement");
        else
            mToolbarTitle.setText("Add Rep Achievement");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    // button clickable
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.toolbar_img_options_menu :
                addOrEditRepAchievement();
                break;

            case R.id.profile_rep_btn_upload :
                chooseRepMedia();
                break;

           /* case R.id.profile_rep_btn_link :

                DialogEditText dialogEditText = DialogEditText.getInstance("Insert Link", "Insert Link", "Save",false);
                dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                    @Override
                    public void onButtonClicked(String enteredValue) {
                        Log.i(TAG,"link name = "+enteredValue);
                        strLink = enteredValue;
                    }
                });
                dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
           *//* DialogProfileAddLink dialogProfileAddLink = new DialogProfileAddLink();
            dialogProfileAddLink.setListener(new DialogProfileAddLink.AddLinkListener()
            {
                @Override
                public void onLinkReceived(String link) {
                    Log.i(TAG,"link name = "+link);
                    strLink = link;
                }
            });

            dialogProfileAddLink.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROFILE_ADD_LINK);*//*
            break;*/
        }

    }


    public void addOrEditRepAchievement()
    {
        addRepAchievement();
    }



    public void addRepAchievement()
    {

        if (UtilHelper.isConnectedToInternet(mContext))
        {

            if (!isAllFieldEmpty())
            {
              /*  mLoader = DialogLoader.getInstance("Saving...");
                if (! mLoader.isAdded())
                    mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);*/

              showLoader(getString(R.string.message_saving));


                if (mTypeAddOrEdit.equals(TYPE_EDIT))
                {
                  mPresenter.editRepAchievement(getRepAchievement());
                }
                else {
                    mPresenter.addRepAchievement(getRepAchievement());
                }
            }
            else
                DialogHelper.showSimpleCustomDialog(mContext,"Fill Fields...","All fields of representative achievement are empty");
        }
        else
        {
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
            //mActivityProfileUpdate.onHideProgress();
        }
    }


    // get all fields text for sending to server
    private RepAchievement getRepAchievement()
    {
        RepAchievement repAchievement = new RepAchievement();

        repAchievement.setRepTeam(mEditRepTeam.getText().toString().trim());
        repAchievement.setRepLocation(mEditRepLocation.getText().toString().trim());
        repAchievement.setRepFromMonth(mSpinRepFromMonth.getSelectedItem().toString().trim());
        repAchievement.setRepFromYear(mSpinRepFromYear.getSelectedItem().toString().trim());
        repAchievement.setRepToMonth(mSpinRepToMonth.getSelectedItem().toString().trim());
        repAchievement.setRepToYear(mSpinRepToYear.getSelectedItem().toString().trim());
        repAchievement.setRepRole(mEditRepRole.getText().toString().trim());
        repAchievement.setRepLink(mEditLink.getText().toString().trim());

        if (mSelectedRepMedia == null)
            repAchievement.setRepMediaBase64("");
        else
            repAchievement.setRepMediaBase64(mSelectedRepMedia);

        if (mTypeAddOrEdit.equals(TYPE_EDIT))
        {
            repAchievement.setRepID(getREP_ID);
        }

        return repAchievement;
    }


    // selecting media when user clicks on media button
    private void chooseRepMedia()
    {
        String mPermissionExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String mPermissionMessage = "Please give read external storage permission to select image from storage";
        if (mPermissionHelper.isPermissionGranted(mPermissionExternalStorage))
        {
            try {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_REP_MEDIA);
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error in getting image from storage , "+e.getMessage());
                e.printStackTrace();
            }

        }
        else
            mPermissionHelper.requestPermission(mPermissionExternalStorage, mPermissionMessage);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() method called ");
        try
        {
            if (resultCode == RESULT_OK)
            {
                // finding selected media
                if (requestCode == REQUEST_CODE_REP_MEDIA)
                    handleSelectedRepMedia(data);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in onActivityResult() method , "+e.getMessage());
            e.printStackTrace();
        }
    }

    // checking if any field is empty or not, if empty then true will be returned othewise false
    private boolean isAllFieldEmpty()
    {

        if (!mEditRepTeam.getText().toString().trim().isEmpty())
            return false;

        if (!mEditRepLocation.getText().toString().trim().isEmpty())
            return false;

        if (!mSpinRepFromMonth.getSelectedItem().toString().equals(getResources().getStringArray(R.array.entries_month)[0]))
            return false;

        if (!mSpinRepFromYear.getSelectedItem().toString().equals(getResources().getStringArray(R.array.entries_year)[0]))
            return false;

        if (!mSpinRepToMonth.getSelectedItem().toString().equals(getResources().getStringArray(R.array.entries_month)[0]))
            return false;

        if (!mSpinRepToYear.getSelectedItem().toString().equals(getResources().getStringArray(R.array.entries_year)[0]))
            return false;

        if (!mEditRepRole.getText().toString().trim().isEmpty())
            return false;

        if (!mEditLink.getText().toString().trim().isEmpty())
            return false;

        if (mSelectedRepMedia != null)
        {
            if (!mSelectedRepMedia.trim().isEmpty())
                return false;
        }




        return true;
    }
    // finding selected media and initializing to mSelectedRepMedia
    private void handleSelectedRepMedia(Intent data)
    {
        // final ImageHelper imageHelper = new ImageHelper(mContext);
        //  mSelectedImageBase64 = null;
        if (data != null && data.getData() != null)
        {
            Uri uri = data.getData();
            String mimeType = mContext.getContentResolver().getType(uri);  // getting media type
            Cursor cursor = mContext.getContentResolver().query(uri,null,null,null,null);
            String fileName = "Media";
            if (cursor != null)
            {
                int fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                if (fileNameIndex >= 0)
                    fileName = cursor.getString(fileNameIndex);
            }

            //mTxtRepMedia.setText(fileName);

            if (mimeType != null)
            {
                Log.i(TAG,"mimeType = "+mimeType);
                if ( !(mimeType.contains("video") || mimeType.contains("image")) )
                {
                    Log.i(TAG,"mimeType inside = "+mimeType);
                    DialogHelper.showSimpleCustomDialog(mContext, "Please...", "Select image or video only", new DialogHelper.ShowSimpleDialogListener() {
                        @Override
                        public void onOkClicked() {
                            chooseRepMedia();
                        }
                    });
                }
                else
                {
                    if (mimeType.contains("video"))
                    {
                        Uri uriRepMedia = data.getData();
                        VideoHelper videoHelper = new VideoHelper(mContext);
                        try
                        {
                            mSelectedRepMedia = UPLOAD_VIDEO_TYPE_MP4+videoHelper.encodeFileToBase64Binary(uriRepMedia);
                            Log.i(TAG,"videoBase64 = "+mSelectedRepMedia);
                        } catch (IOException e) {
                            Log.e(TAG,"Error while converting video to Base64 , "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    if (mimeType.contains("image"))
                    {
                        Uri uriRepImage = data.getData();
                        int imageWidth = UtilHelper.convertDpToPixel(mContext,80);
                        int imageHeight = UtilHelper.convertDpToPixel(mContext,80);
                       // mMediaImage.setImageResource();
                        Glide.with(mContext)
                                .load(data.getData())
                                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                                .apply(new RequestOptions().error(R.drawable.ic_profile))
                                .apply(new RequestOptions().override(imageWidth, imageHeight))
                                .into(mMediaImage);

                        imageHelper.requestBase64Image(data.getData(), imageWidth, imageHeight,  new ImageHelper.Base64EncodedListener() {
                            @Override
                            public void onBase64ImageReceived(String imageBase64)
                            {
                                Log.i(TAG,"imageBase64 = "+imageBase64);
                                if (imageBase64 != null) {
                                    mSelectedRepMedia = UPLOAD_IMAGE_TYPE_JPEG + imageBase64;

                                }
                            }
                        });
                    }
                }

            }

            Log.i(TAG,"Selected Rep Media Type = "+mimeType);

        }
        else
            Log.e(TAG,"data is null in onActivityResult() method");
    }


   /* private void hideLoader()
    {
        if (mLoader != null && mLoader.isAdded())
            mLoader.dismiss();
    }*/



    @Override
    public void onShowProgress() {
        hideLoader();
    }

    @Override
    public void onHideProgress() {
        hideLoader();
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        hideLoader();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        hideLoader();
    }

    @Override
    public void onShowMessage(String message) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }

    @Override
    public void onReceivedRepList(List<RepAchievement> listRepAchievement)
    {
        hideLoader();
    }

    @Override
    public void onDeleted(int position) {
        hideLoader();
    }

    @Override
    public void onAddRepSuccessfully() {
        mEditRepTeam.setText("");
        mEditRepLocation.setText("");
        mEditRepRole.setText("");


        DialogHelper.showSimpleCustomDialog(mContext,"Achievement added successfully...");

    }

    @Override
    public void onEditSuccessfully() {

        Intent intent = new Intent();
        intent.putExtra(KEY_RESPONSE_IS_ADDED_OR_EDITED, true);
        setResult(RESULT_OK, intent);
        finish();

    }
}
