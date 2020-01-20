package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;

import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractProfileUpdate;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.ImageHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Profile;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterProfileUpdate;

import java.io.File;


public class ActivityProfileUpdate extends BaseActivity implements ContractProfileUpdate.View {

    // # constants
    private static final String TAG = ActivityProfileUpdate.class.getSimpleName();
    private static int REQUEST_CODE_IMAGE = 100;
    private final static int PROFILE_PIC_SIZE = 300;


    // # views
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private View mLayoutAddProfilePic;
    private ImageView mImgProfilePic;
    private EditText mEditName, mEditSportCode, mEditAddress, mEditAthleteBio, mEditHighlightReel, mEditPlaylist;
    // private DialogLoader mLoader;
    private ImageView mImgUpdate;


    // # other variables
    private ImageHelper imageHelper;
    private Context mContext;
    private PresenterProfileUpdate mPresenter;
    private String mSelectedProfilePicBase64 = null;  // selected profile pic will be stored in this
    private PermissionHelper mPermissionHelper;




    Profile mProfile; // it will contain previous profile sent from calling component
    public static final String KEY_PROFILE_DATA = "data";  // key to get

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        init();
        setToolbar();
        setListeners();
        showPreviousData();
    }



    // this method sets previous updated profile data in fields
    private void showPreviousData()
    {
        Log.i(TAG,"showPreviousData() method called ");
        if (mProfile != null)
        {

            if (mProfile.getName() != null)
                if (!mProfile.getName().isEmpty())
                    mEditName.setText(mProfile.getName());

            if (mProfile.getSportName() != null)
                if (!mProfile.getSportName().isEmpty())
                    mEditSportCode.setText(mProfile.getSportName());

            if (mProfile.getAddress() != null)
                if (!mProfile.getAddress().isEmpty())
                    mEditAddress.setText(mProfile.getAddress());

            if (mProfile.getAthleteBio() != null)
                if (!mProfile.getAthleteBio().isEmpty())
                    mEditAthleteBio.setText(mProfile.getAthleteBio());

            if (mProfile.getHighlightReel() != null)
                if (!mProfile.getHighlightReel().isEmpty())
                    mEditHighlightReel.setText(mProfile.getHighlightReel());

            if (mProfile.getPlaylist() != null)
                if (!mProfile.getPlaylist().isEmpty())
                    mEditPlaylist.setText(mProfile.getPlaylist());


            int picWidth = UtilHelper.convertDpToPixel(mContext, 190);

            Glide.with(mContext)
                    .load(mProfile.getPicUrl())
                    .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                    .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                    .apply(new RequestOptions().centerCrop())
                    .apply(new RequestOptions().override(picWidth))
                    .into(mImgProfilePic);

        }
        else
            Log.e(TAG,"mProfile is null in showPreviousData() method");

    }


    private void updateProfile()
    {
        Log.i(TAG,"updateProfile() method called ");
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            if (!isAllFieldEmpty())
            {
                showLoader(getString(R.string.message_updating));
                mPresenter.updateProfile(getUserProfile());
            }
            else
            {
                onHideProgress();
                DialogHelper.showSimpleCustomDialog(mContext,"Fill All fields...","All fields of profile are empty");
            }
        }
        else
        {
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
            onHideProgress();
        }

    }

    private void setListeners()
    {
        mLayoutAddProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePic();
            }
        });


        mImgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }


    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mImgUpdate = findViewById(R.id.toolbar_img_options_menu);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mPermissionHelper = new PermissionHelper(mContext);
        mPresenter = new PresenterProfileUpdate(mContext,this);
        imageHelper = new ImageHelper(mContext);
        mImgProfilePic = findViewById(R.id.profile_img_profile_pic);
        mLayoutAddProfilePic = findViewById(R.id.profile_layout_add_profile_pic);
        mEditName = findViewById(R.id.profile_edit_name);
        mEditSportCode = findViewById(R.id.profile_edit_sport_code);
        mEditAddress =findViewById(R.id.profile_edit_address);
        mEditAthleteBio = findViewById(R.id.profile_edit_athlete_bio);
        mEditHighlightReel = findViewById(R.id.profile_edit_highlight_reel);
        mEditPlaylist = findViewById(R.id.profile_edit_playlist);
        //  mBtnUpdate = findViewById(R.id.profile_edit_btn_update_profile);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mProfile = (Profile) bundle.getSerializable(KEY_PROFILE_DATA);
    }


    private void setToolbar()
    {
        mToolbarTitle.setText(getString(R.string.edit_profile));

        // showing save icon
        mImgUpdate.setImageResource(R.drawable.ic_notes_save);
        mImgUpdate.setVisibility(View.VISIBLE);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }



    private void chooseProfilePic()
    {
        String mPermissionExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String mPermissionMessage = "Please give read external storage permission to select image from storage";
        if (mPermissionHelper.isPermissionGranted(mPermissionExternalStorage))
            startActivityForResult(UtilHelper.getPickImageIntent(),REQUEST_CODE_IMAGE);
        else
            mPermissionHelper.requestPermission(mPermissionExternalStorage, mPermissionMessage);
    }


    // if no field is empty then false will be returned otherwise true;
    private boolean isAllFieldEmpty()
    {

        if (!mEditName.getText().toString().trim().isEmpty())
            return false;

        if (!mEditSportCode.getText().toString().trim().isEmpty())
            return false;

        if (!mEditAddress.getText().toString().trim().isEmpty())
            return false;

        if (!mEditAthleteBio.getText().toString().trim().isEmpty())
            return false;

        if (!mEditHighlightReel.getText().toString().trim().isEmpty())
            return false;


        if (!mEditPlaylist.getText().toString().trim().isEmpty())
            return false;


        if (mSelectedProfilePicBase64 != null)
        {
            return mSelectedProfilePicBase64.trim().isEmpty();
        }




        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() method called ");
        try
        {
            if (resultCode == RESULT_OK && data != null)
            {
                if (requestCode == REQUEST_CODE_IMAGE && data.getData() != null)
                {


                   /* String path = Environment.getExternalStorageDirectory().toString()+"/uCrop/";
                    File folder = new File(path);
                    if (!folder.exists())
                        folder.mkdirs();*/


                    File file = new File(FileHelper.TEMP_FILES_FOLDER, "image.jpeg");
                    Uri uri = Uri.parse(file.getAbsolutePath());

                    UCrop.of(data.getData(), uri)
                            .withOptions(UtilHelper.getUCropOptions(mContext))
                            .withMaxResultSize(PROFILE_PIC_SIZE, PROFILE_PIC_SIZE)
                            .start(ActivityProfileUpdate.this);
                }

                if (requestCode == UCrop.REQUEST_CROP)
                    handleSelectedProfilePic(UCrop.getOutput(data));
            }

            if (resultCode == UCrop.RESULT_ERROR)
            {
                Throwable throwable = UCrop.getError(data);
                if (throwable == null)
                    return;

                Log.i(TAG, "getMessage = "+throwable.getMessage());
                Log.i(TAG, "getLocalizedMessage = "+throwable.getLocalizedMessage());
                Log.i(TAG, "getCause = "+throwable.getCause());
                throwable.printStackTrace();

            }
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in onActivityResult() method , "+e.getMessage());
            e.printStackTrace();
        }

    }

    private void handleSelectedProfilePic(Uri imageUri)
    {
        UtilHelper.loadImageWithGlide(mContext, FileHelper.getFilePath(mContext, imageUri), R.drawable.ic_profile, PROFILE_PIC_SIZE, PROFILE_PIC_SIZE, mImgProfilePic);

        // getting image in base64
        mSelectedProfilePicBase64 = null;
        if (imageUri != null)
        {
            imageHelper.requestBase64Image(imageUri, PROFILE_PIC_SIZE, PROFILE_PIC_SIZE, new ImageHelper.Base64EncodedListener() {
                @Override
                public void onBase64ImageReceived(String imageBase64)
                {
                    if (imageBase64 != null)
                        mSelectedProfilePicBase64 = RemoteServer.BASE64_IMAGE_TYPE_JPEG+imageBase64;
                }
            });
        }
    }



    // # it returns Profile object which will be saved
    private Profile getUserProfile()
    {
        Profile profile = new Profile();

        if (mSelectedProfilePicBase64 != null && !mSelectedProfilePicBase64.equals("")) {
            profile.setProfilePicBase64(mSelectedProfilePicBase64);
        }
        else {
            profile.setPicUrl(mProfile.getPicUrl());
        }

        profile.setName(mEditName.getText().toString().trim());
        profile.setSportName(mEditSportCode.getText().toString().trim());
        profile.setAddress(mEditAddress.getText().toString().trim());
        profile.setAthleteBio(mEditAthleteBio.getText().toString().trim());
        profile.setHighlightReel(mEditHighlightReel.getText().toString().trim());
        profile.setPlaylist(mEditPlaylist.getText().toString().trim());

        return profile;
    }



    @Override
    public void onComplete(String message)
    {
        hideLoader();
        Log.i(TAG,"Profile updated now showing profile page , user id = "+SessionHelper.getInstance(mContext).getUser().getUserId());

        Intent intent = new Intent(mContext, ActivityProfileView.class);
        intent.putExtra(ConstHelper.Key.ID, SessionHelper.getInstance(mContext).getUser().getUserId());
        setResult(RESULT_OK, intent);
        finish();

    }


    @Override
    public void onShowProgress() {
        //  mProgressBar.setVisibility(View.VISIBLE);
        // mProgress.setVisibility(View.VISIBLE);
        // mActivityProfileUpdate.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        hideLoader();
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        hideLoader();
    }

    @Override
    public void onShowMessage(String message)
    {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }
}
