package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyGraphics;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.utils.UiHelper;

import java.io.File;
import java.io.IOException;

public class ActivityRatingScaleSurvey extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    Spinner select_rating_scale_format, select_rating_scale_graphic,select_rating_scale_range;
    LinearLayout graphic_layout, select_rating_scale_descriptor;
    private int formatPosition, rangePosition;
    private String strSelectedScaleFormat;
    private int strSelectedScaleRange;

    Survey.SurveyData.RatingScale mRatingScale;
    Bundle mBundle;

    private Toolbar mToolbar;   // toolbar
    private TextView mTxtToolbarTitle;  // for toolbar title
    Button mBtnNext;
    private RelativeLayout mLayout;
    int flag = 0;
    ImageView mImgAttach;
    String mStrSelectedMediaUri = null;
    String currentPhotoPath = "";
    private UiHelper uiHelper;
    TextView txtGraphics;
    String selectedGraphic;



    RelativeLayout rLayout1;
    RelativeLayout rLayout2;
    RelativeLayout rLayout3;
    RelativeLayout rLayout4;
    RelativeLayout rLayout5;
    RelativeLayout rLayout6;
    RelativeLayout rLayout7;
    RelativeLayout rLayout8;
    RelativeLayout rLayout9;

    TextView txtNumber2;
    TextView txtNumber3;
    TextView txtNumber4;
    TextView txtNumber5;
    TextView txtNumber6;
    TextView txtNumber7;
    TextView txtNumber8;
    TextView txtNumber9;
    TextView txtNumber10;
    TextView txtNumber11;
    TextView txtNumber12;
    TextView txtNumber13;
    TextView txtNumber14;
    TextView txtNumber15;
    TextView txtNumber16;
    TextView txtNumber17;
    TextView txtNumber18;
    TextView txtNumber19;

    EditText editWord2;
    EditText editWord3;
    EditText editWord4;
    EditText editWord5;
    EditText editWord6;
    EditText editWord7;
    EditText editWord8;
    EditText editWord9;
    EditText editWord10;

    EditText editWord11;
    EditText editWord12;
    EditText editWord13;
    EditText editWord14;
    EditText editWord15;
    EditText editWord16;
    EditText editWord17;
    EditText editWord18;
    EditText editWord19;


    ImageView imgStar1, imgEmoji1;
    ImageView imgStar2, imgEmoji2;
    ImageView imgStar3, imgEmoji3;
    ImageView imgStar4, imgEmoji4;
    ImageView imgStar5, imgEmoji5;
    ImageView imgStar6, imgEmoji6;
    ImageView imgStar7, imgEmoji7;
    ImageView imgStar8, imgEmoji8;
    ImageView imgStar9, imgEmoji9;

    LinearLayout wordNumbersLayout;
    LinearLayout numberLayout;
    LinearLayout wordsLayout;
    LinearLayout starLayout;
    LinearLayout emojiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_scale_survey);

        mBundle = getIntent().getExtras();
        if (mBundle != null)
        {
            mRatingScale = (Survey.SurveyData.RatingScale) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_RATING_SCALE);
        }

        init();
        setFormatSpinnerAdapter();
        setGraphicSpinnerAdapter();

        prepareToolbar();









    }



    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        select_rating_scale_format = findViewById(R.id.select_rating_scale_format);
        select_rating_scale_range = findViewById(R.id.select_rating_scale_range);
        select_rating_scale_graphic = findViewById(R.id.select_rating_scale_graphics);
        graphic_layout = findViewById(R.id.graphic_layout);
        mBtnNext = findViewById(R.id.btn_next);
        mLayout = findViewById(R.id.layout);
        select_rating_scale_descriptor = findViewById(R.id.select_rating_scale_descriptor);
        mImgAttach = findViewById(R.id.img);
        mImgAttach.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        select_rating_scale_descriptor.setOnClickListener(this);
        uiHelper = new UiHelper();
        txtGraphics =  findViewById(R.id.graphic_text);
        wordNumbersLayout = findViewById(R.id.wordNumbersLayout);


        txtNumber2 = findViewById(R.id.ninth);
        txtNumber3 = findViewById(R.id.eighth);
        txtNumber4 = findViewById(R.id.seventh);
        txtNumber5 = findViewById(R.id.sixth);
        txtNumber6 = findViewById(R.id.fifth);
        txtNumber7 = findViewById(R.id.forth);
        txtNumber8 = findViewById(R.id.third);
        txtNumber9 = findViewById(R.id.second);
        txtNumber10 = findViewById(R.id.first);


        txtNumber11 = findViewById(R.id.txtnine);
        txtNumber12 = findViewById(R.id.txteight);
        txtNumber13 = findViewById(R.id.txtSeven);
        txtNumber14 = findViewById(R.id.txtSix);
        txtNumber15 = findViewById(R.id.txtfive);
        txtNumber16 = findViewById(R.id.txtfour);
        txtNumber17 = findViewById(R.id.txtThree);
        txtNumber18 = findViewById(R.id.txtTwo);
        txtNumber19 = findViewById(R.id.txtFirst);

        editWord2 = findViewById(R.id.descriptor2);
        editWord3 = findViewById(R.id.descriptor3);
        editWord4 = findViewById(R.id.descriptor4);
        editWord5 = findViewById(R.id.descriptor5);
        editWord6 = findViewById(R.id.descriptor6);
        editWord7 = findViewById(R.id.descriptor7);
        editWord8 = findViewById(R.id.descriptor8);
        editWord9 = findViewById(R.id.descriptor9);
        editWord10 = findViewById(R.id.descriptor10);

        editWord11 = findViewById(R.id.wordsEdit1);
        editWord12 = findViewById(R.id.wordsEdit2);
        editWord13 = findViewById(R.id.wordsEdit3);
        editWord14 = findViewById(R.id.wordsEdit4);
        editWord15 = findViewById(R.id.wordsEdit5);
        editWord16 = findViewById(R.id.wordsEdit6);
        editWord17 = findViewById(R.id.wordsEdit7);
        editWord18 = findViewById(R.id.wordsEdit8);
        editWord19 = findViewById(R.id.wordsEdit9);

        rLayout1 = findViewById(R.id.rl2);
        rLayout2 = findViewById(R.id.rl3);
        rLayout3 = findViewById(R.id.rl4);
        rLayout4 = findViewById(R.id.rl5);
        rLayout5 = findViewById(R.id.rl6);
        rLayout6 = findViewById(R.id.rl7);
        rLayout7 = findViewById(R.id.rl8);
        rLayout8 = findViewById(R.id.rl9);
        rLayout9 = findViewById(R.id.rl10);

        imgStar1 = findViewById(R.id.imgnine);
        imgStar2 = findViewById(R.id.imgeight);
        imgStar3 = findViewById(R.id.imgseven);
        imgStar4 = findViewById(R.id.imgsix);
        imgStar5 = findViewById(R.id.imgfive);
        imgStar6 = findViewById(R.id.imgfour);
        imgStar7 = findViewById(R.id.imgthree);
        imgStar8 = findViewById(R.id.imgtwo);
        imgStar9 = findViewById(R.id.imgone);


        imgEmoji1 = findViewById(R.id.img_emoji1);
        imgEmoji2 = findViewById(R.id.img_emoji2);
        imgEmoji3 = findViewById(R.id.img_emoji3);
        imgEmoji4 = findViewById(R.id.img_emoji4);
        imgEmoji5 = findViewById(R.id.img_emoji5);
        imgEmoji6 = findViewById(R.id.img_emoji6);
        imgEmoji7 = findViewById(R.id.img_emoji7);
        imgEmoji8 = findViewById(R.id.img_emoji8);
        imgEmoji9 = findViewById(R.id.img_emoji9);

        starLayout = findViewById(R.id.starLayout);
        numberLayout = findViewById(R.id.numbersLayout);
        wordsLayout = findViewById(R.id.wordsLayout);
        emojiLayout = findViewById(R.id.emojiLayout);

    }



    private void setFormatSpinnerAdapter() {

        String[] noOfQuestionsArray = getResources().getStringArray(R.array.entries_rating_scale_format);

        for (int x = 0; x < noOfQuestionsArray.length; x++) {
            formatPosition = x;

            break;

        }


        select_rating_scale_format.setSelection(formatPosition);

        select_rating_scale_format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strSelectedScaleFormat = (String) adapterView.getItemAtPosition(i);
                setRangeSpinnerAdapter(strSelectedScaleFormat);
                layoutVisibilityGone();


                if (strSelectedScaleFormat.equals("Graphics")) {
                    graphic_layout.setVisibility(View.VISIBLE);
                    txtGraphics.setVisibility(View.VISIBLE);
                }
                else {
                    graphic_layout.setVisibility(View.GONE);
                    txtGraphics.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    private void setGraphicSpinnerAdapter() {

        int[] Flags = {R.drawable.ic_star, R.drawable.ic_emoji};
        AdapterSurveyGraphics adapterSurveyGraphics = new AdapterSurveyGraphics(mContext, Flags);
        select_rating_scale_graphic.setAdapter(adapterSurveyGraphics);

        selectedGraphic = "Star";
        select_rating_scale_graphic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("position " , String.valueOf(position));
                if (position == 0)
                {
                    selectedGraphic = "Star";
                    layoutVisibilityGone();
                }
                else
                {
                    selectedGraphic = "Emoji";
                    layoutVisibilityGone();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRangeSpinnerAdapter(final String format)
    {
        String[] scaleRangeArray = getResources().getStringArray(R.array.entries_rating_scale_range);

        for (int x = 0; x < scaleRangeArray.length; x++) {
            rangePosition = x;

            break;

        }


        select_rating_scale_range.setSelection(rangePosition);


        select_rating_scale_range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strSelectedScaleRange = i;

                layoutVisibilityGone();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    private void prepareToolbar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.rating_scale_title);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img :
                selectMedia();
                break;

            case R.id.btn_next :
                if (strSelectedScaleFormat.equals("Select Rating Scale Format"))
                {
                    DialogHelper.showSimpleCustomDialog(this,"Please Select Scale Format ");
                }
                else if (selectedGraphic.equals("")) {
                    DialogHelper.showSimpleCustomDialog(this,"Please select graphics ");
                }
                else if (strSelectedScaleRange == 0)
                {
                    DialogHelper.showSimpleCustomDialog(this,"Please Select Rating Range ");
                }
                else
                {
                    mRatingScale.setRatingScaleFormat(strSelectedScaleFormat);
                    mRatingScale.setRatingScaleGraphicsItem(selectedGraphic);
                    mRatingScale.setRatingScaleRange(String.valueOf(strSelectedScaleRange));
                    Intent intent = new Intent(ActivityRatingScaleSurvey.this,ActivitySurveyRatingScaleTitlePreview.class);
                    intent.putExtra(ActivityCreateSurvey.SURVEY_RATING_SCALE, mRatingScale);
                    intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);

                    startActivity(intent);
                }
                break;

            case R.id.select_rating_scale_descriptor :
                if (flag == 0 )
                {

                       if (strSelectedScaleFormat.equals("Words"))
                       {
                           mLayout.setVisibility(View.VISIBLE);
                           wordsLayout.setVisibility(View.VISIBLE);
                           showWords(strSelectedScaleRange);


                       }
                       else if (strSelectedScaleFormat.equals("Numbers"))
                       {
                           mLayout.setVisibility(View.VISIBLE);
                           numberLayout.setVisibility(View.VISIBLE);
                           showNumbers(strSelectedScaleRange);



                       }
                       else if (strSelectedScaleFormat.equals("Words + Numbers"))
                       {
                           mLayout.setVisibility(View.VISIBLE);
                           wordNumbersLayout.setVisibility(View.VISIBLE);
                           showWordsAndNumbers(strSelectedScaleRange);


                       }
                       else
                       {
                           if (selectedGraphic.equals("Emoji"))
                           {
                               mLayout.setVisibility(View.VISIBLE);
                               emojiLayout.setVisibility(View.VISIBLE);
                               showEmoji(strSelectedScaleRange);
                           }
                           else
                           {
                               mLayout.setVisibility(View.VISIBLE);
                               starLayout.setVisibility(View.VISIBLE);
                               showStars(strSelectedScaleRange);
                           }


                       }


                    flag = 1;

                    // if (strSelectedScaleRange.equals("1-5"))

                }
                else
                {
                    mLayout.setVisibility(View.GONE);
                    flag = 0;
                }
                break;
        }
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
            int croppedImageHeight = UtilHelper.convertDpToPixel(mContext,270);
            int croppedImageWidth = UtilHelper.getScreenWidth(mContext);

            Glide.with(mContext)
                    .load(mStrSelectedMediaUri)
                    .apply(new RequestOptions().override(croppedImageWidth, croppedImageHeight))
                    .apply(new RequestOptions().centerCrop())
                    .into(mImgAttach);



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


    private void showWords(int i){
        if (i == 1)
        {
            editWord19.setVisibility(View.VISIBLE);
            editWord18.setVisibility(View.VISIBLE);
            editWord17.setVisibility(View.VISIBLE);


        }else if (i == 2)
        {

            editWord19.setVisibility(View.VISIBLE);
            editWord18.setVisibility(View.VISIBLE);
            editWord17.setVisibility(View.VISIBLE);
            editWord16.setVisibility(View.VISIBLE);


        }
        else if (i == 3)
        {

            editWord19.setVisibility(View.VISIBLE);
            editWord18.setVisibility(View.VISIBLE);
            editWord17.setVisibility(View.VISIBLE);
            editWord16.setVisibility(View.VISIBLE);
            editWord15.setVisibility(View.VISIBLE);


        }

        else if (i == 4)
        {

            editWord19.setVisibility(View.VISIBLE);
            editWord18.setVisibility(View.VISIBLE);
            editWord17.setVisibility(View.VISIBLE);
            editWord16.setVisibility(View.VISIBLE);
            editWord15.setVisibility(View.VISIBLE);
            editWord14.setVisibility(View.VISIBLE);



        }
        else if (i == 5)
        {
            editWord19.setVisibility(View.VISIBLE);
            editWord18.setVisibility(View.VISIBLE);
            editWord17.setVisibility(View.VISIBLE);
            editWord16.setVisibility(View.VISIBLE);
            editWord15.setVisibility(View.VISIBLE);
            editWord14.setVisibility(View.VISIBLE);
            editWord13.setVisibility(View.VISIBLE);


        }
        else if (i == 6)
        {

            editWord19.setVisibility(View.VISIBLE);
            editWord18.setVisibility(View.VISIBLE);
            editWord17.setVisibility(View.VISIBLE);
            editWord16.setVisibility(View.VISIBLE);
            editWord15.setVisibility(View.VISIBLE);
            editWord14.setVisibility(View.VISIBLE);
            editWord13.setVisibility(View.VISIBLE);
            editWord12.setVisibility(View.VISIBLE);

        }

        else if (i == 7)
        {

            editWord19.setVisibility(View.VISIBLE);
            editWord18.setVisibility(View.VISIBLE);
            editWord17.setVisibility(View.VISIBLE);
            editWord16.setVisibility(View.VISIBLE);
            editWord15.setVisibility(View.VISIBLE);
            editWord14.setVisibility(View.VISIBLE);
            editWord13.setVisibility(View.VISIBLE);
            editWord12.setVisibility(View.VISIBLE);
            editWord11.setVisibility(View.VISIBLE);

        }

    }




    private void showNumbers(int i){

        if (i == 1)
        {

            txtNumber19.setVisibility(View.VISIBLE);
            txtNumber18.setVisibility(View.VISIBLE);
            txtNumber17.setVisibility(View.VISIBLE);


        }else if (i == 2)
        {

            txtNumber19.setVisibility(View.VISIBLE);
            txtNumber18.setVisibility(View.VISIBLE);
            txtNumber17.setVisibility(View.VISIBLE);
            txtNumber16.setVisibility(View.VISIBLE);


        }
        else if (i == 3)
        {

            txtNumber19.setVisibility(View.VISIBLE);
            txtNumber18.setVisibility(View.VISIBLE);
            txtNumber17.setVisibility(View.VISIBLE);
            txtNumber16.setVisibility(View.VISIBLE);
            txtNumber15.setVisibility(View.VISIBLE);


        }

        else if (i == 4)
        {

            txtNumber19.setVisibility(View.VISIBLE);
            txtNumber18.setVisibility(View.VISIBLE);
            txtNumber17.setVisibility(View.VISIBLE);
            txtNumber16.setVisibility(View.VISIBLE);
            txtNumber15.setVisibility(View.VISIBLE);
            txtNumber14.setVisibility(View.VISIBLE);



        }
        else if (i == 5)
        {

            txtNumber10.setVisibility(View.VISIBLE);
            txtNumber9.setVisibility(View.VISIBLE);
            txtNumber8.setVisibility(View.VISIBLE);
            txtNumber7.setVisibility(View.VISIBLE);
            txtNumber6.setVisibility(View.VISIBLE);
            txtNumber5.setVisibility(View.VISIBLE);
            txtNumber4.setVisibility(View.VISIBLE);


        }
        else if (i == 6)
        {

            txtNumber19.setVisibility(View.VISIBLE);
            txtNumber18.setVisibility(View.VISIBLE);
            txtNumber17.setVisibility(View.VISIBLE);
            txtNumber16.setVisibility(View.VISIBLE);
            txtNumber15.setVisibility(View.VISIBLE);
            txtNumber14.setVisibility(View.VISIBLE);
            txtNumber13.setVisibility(View.VISIBLE);
            txtNumber12.setVisibility(View.VISIBLE);

        }

        else if (i == 7)
        {

            txtNumber19.setVisibility(View.VISIBLE);
            txtNumber18.setVisibility(View.VISIBLE);
            txtNumber17.setVisibility(View.VISIBLE);
            txtNumber16.setVisibility(View.VISIBLE);
            txtNumber15.setVisibility(View.VISIBLE);
            txtNumber14.setVisibility(View.VISIBLE);
            txtNumber13.setVisibility(View.VISIBLE);
            txtNumber12.setVisibility(View.VISIBLE);
            txtNumber11.setVisibility(View.VISIBLE);


        }


    }


    private void showWordsAndNumbers(int i){
        if (i == 1)
        {
            rLayout9.setVisibility(View.VISIBLE);
            rLayout8.setVisibility(View.VISIBLE);
            rLayout7.setVisibility(View.VISIBLE);



        }
        else if (i == 2)
        {
            rLayout9.setVisibility(View.VISIBLE);
            rLayout8.setVisibility(View.VISIBLE);
            rLayout7.setVisibility(View.VISIBLE);
            rLayout6.setVisibility(View.VISIBLE);

        }
        else if (i == 3)
        {
            rLayout9.setVisibility(View.VISIBLE);
            rLayout8.setVisibility(View.VISIBLE);
            rLayout7.setVisibility(View.VISIBLE);
            rLayout6.setVisibility(View.VISIBLE);
            rLayout5.setVisibility(View.VISIBLE);


        }

        else if (i == 4)
        {
            rLayout9.setVisibility(View.VISIBLE);
            rLayout8.setVisibility(View.VISIBLE);
            rLayout7.setVisibility(View.VISIBLE);
            rLayout6.setVisibility(View.VISIBLE);
            rLayout5.setVisibility(View.VISIBLE);
            rLayout4.setVisibility(View.VISIBLE);

        }
        else if (i == 5)
        {
            rLayout9.setVisibility(View.VISIBLE);
            rLayout8.setVisibility(View.VISIBLE);
            rLayout7.setVisibility(View.VISIBLE);
            rLayout6.setVisibility(View.VISIBLE);
            rLayout5.setVisibility(View.VISIBLE);
            rLayout4.setVisibility(View.VISIBLE);
            rLayout3.setVisibility(View.VISIBLE);

        }
        else if (i == 6)
        {
            rLayout9.setVisibility(View.VISIBLE);
            rLayout8.setVisibility(View.VISIBLE);
            rLayout7.setVisibility(View.VISIBLE);
            rLayout6.setVisibility(View.VISIBLE);
            rLayout5.setVisibility(View.VISIBLE);
            rLayout4.setVisibility(View.VISIBLE);
            rLayout3.setVisibility(View.VISIBLE);
            rLayout2.setVisibility(View.VISIBLE);

        }

        else if (i == 7)
        {
            rLayout9.setVisibility(View.VISIBLE);
            rLayout8.setVisibility(View.VISIBLE);
            rLayout7.setVisibility(View.VISIBLE);
            rLayout6.setVisibility(View.VISIBLE);
            rLayout5.setVisibility(View.VISIBLE);
            rLayout4.setVisibility(View.VISIBLE);
            rLayout3.setVisibility(View.VISIBLE);
            rLayout2.setVisibility(View.VISIBLE);
            rLayout1.setVisibility(View.VISIBLE);

        }
    }


    private void showStars(int i)
    {

        if (i == 1)
        {
            imgStar9.setVisibility(View.VISIBLE);
            imgStar8.setVisibility(View.VISIBLE);
            imgStar7.setVisibility(View.VISIBLE);



        }
        else if (i == 2)
        {
            imgStar9.setVisibility(View.VISIBLE);
            imgStar8.setVisibility(View.VISIBLE);
            imgStar7.setVisibility(View.VISIBLE);
            imgStar6.setVisibility(View.VISIBLE);

        }
        else if (i == 3)
        {
            imgStar9.setVisibility(View.VISIBLE);
            imgStar8.setVisibility(View.VISIBLE);
            imgStar7.setVisibility(View.VISIBLE);
            imgStar6.setVisibility(View.VISIBLE);
            imgStar5.setVisibility(View.VISIBLE);


        }

        else if (i == 4)
        {
            imgStar9.setVisibility(View.VISIBLE);
            imgStar8.setVisibility(View.VISIBLE);
            imgStar7.setVisibility(View.VISIBLE);
            imgStar6.setVisibility(View.VISIBLE);
            imgStar5.setVisibility(View.VISIBLE);
            imgStar4.setVisibility(View.VISIBLE);

        }
        else if (i == 5)
        {
            imgStar9.setVisibility(View.VISIBLE);
            imgStar8.setVisibility(View.VISIBLE);
            imgStar7.setVisibility(View.VISIBLE);
            imgStar6.setVisibility(View.VISIBLE);
            imgStar5.setVisibility(View.VISIBLE);
            imgStar4.setVisibility(View.VISIBLE);
            imgStar3.setVisibility(View.VISIBLE);

        }
        else if (i == 6)
        {
            imgStar9.setVisibility(View.VISIBLE);
            imgStar8.setVisibility(View.VISIBLE);
            imgStar7.setVisibility(View.VISIBLE);
            imgStar6.setVisibility(View.VISIBLE);
            imgStar5.setVisibility(View.VISIBLE);
            imgStar4.setVisibility(View.VISIBLE);
            imgStar3.setVisibility(View.VISIBLE);
            imgStar2.setVisibility(View.VISIBLE);

        }

        else if (i == 7)
        {
            imgStar9.setVisibility(View.VISIBLE);
            imgStar8.setVisibility(View.VISIBLE);
            imgStar7.setVisibility(View.VISIBLE);
            imgStar6.setVisibility(View.VISIBLE);
            imgStar5.setVisibility(View.VISIBLE);
            imgStar4.setVisibility(View.VISIBLE);
            imgStar3.setVisibility(View.VISIBLE);
            imgStar2.setVisibility(View.VISIBLE);
            imgStar1.setVisibility(View.VISIBLE);

        }
    }



    private void showEmoji(int i)
    {

        if (i == 1)
        {
            imgEmoji9.setVisibility(View.VISIBLE);
            imgEmoji8.setVisibility(View.VISIBLE);
            imgEmoji7.setVisibility(View.VISIBLE);



        }
        else if (i == 2)
        {
            imgEmoji9.setVisibility(View.VISIBLE);
            imgEmoji8.setVisibility(View.VISIBLE);
            imgEmoji7.setVisibility(View.VISIBLE);
            imgEmoji6.setVisibility(View.VISIBLE);

        }
        else if (i == 3)
        {
            imgEmoji9.setVisibility(View.VISIBLE);
            imgEmoji8.setVisibility(View.VISIBLE);
            imgEmoji7.setVisibility(View.VISIBLE);
            imgEmoji6.setVisibility(View.VISIBLE);
            imgEmoji5.setVisibility(View.VISIBLE);


        }

        else if (i == 4)
        {
            imgEmoji9.setVisibility(View.VISIBLE);
            imgEmoji8.setVisibility(View.VISIBLE);
            imgEmoji7.setVisibility(View.VISIBLE);
            imgEmoji6.setVisibility(View.VISIBLE);
            imgEmoji5.setVisibility(View.VISIBLE);
            imgEmoji4.setVisibility(View.VISIBLE);

        }
        else if (i == 5)
        {
            imgEmoji9.setVisibility(View.VISIBLE);
            imgEmoji8.setVisibility(View.VISIBLE);
            imgEmoji7.setVisibility(View.VISIBLE);
            imgEmoji6.setVisibility(View.VISIBLE);
            imgEmoji5.setVisibility(View.VISIBLE);
            imgEmoji4.setVisibility(View.VISIBLE);
            imgEmoji3.setVisibility(View.VISIBLE);

        }
        else if (i == 6)
        {
            imgEmoji9.setVisibility(View.VISIBLE);
            imgEmoji8.setVisibility(View.VISIBLE);
            imgEmoji7.setVisibility(View.VISIBLE);
            imgEmoji6.setVisibility(View.VISIBLE);
            imgEmoji5.setVisibility(View.VISIBLE);
            imgEmoji4.setVisibility(View.VISIBLE);
            imgEmoji3.setVisibility(View.VISIBLE);
            imgEmoji2.setVisibility(View.VISIBLE);

        }

        else if (i == 7)
        {
            imgEmoji9.setVisibility(View.VISIBLE);
            imgEmoji8.setVisibility(View.VISIBLE);
            imgEmoji7.setVisibility(View.VISIBLE);
            imgEmoji6.setVisibility(View.VISIBLE);
            imgEmoji5.setVisibility(View.VISIBLE);
            imgEmoji4.setVisibility(View.VISIBLE);
            imgEmoji3.setVisibility(View.VISIBLE);
            imgEmoji2.setVisibility(View.VISIBLE);
            imgEmoji1.setVisibility(View.VISIBLE);

        }
    }


    private void layoutVisibilityGone()
    {
        flag = 0;
        rLayout9.setVisibility(View.GONE);
        rLayout8.setVisibility(View.GONE);
        rLayout7.setVisibility(View.GONE);
        rLayout6.setVisibility(View.GONE);
        rLayout5.setVisibility(View.GONE);
        rLayout4.setVisibility(View.GONE);
        rLayout3.setVisibility(View.GONE);
        rLayout2.setVisibility(View.GONE);
        rLayout1.setVisibility(View.GONE);
        mLayout.setVisibility(View.GONE);
        txtNumber19.setVisibility(View.GONE);
        txtNumber18.setVisibility(View.GONE);
        txtNumber17.setVisibility(View.GONE);
        txtNumber16.setVisibility(View.GONE);
        txtNumber15.setVisibility(View.GONE);
        txtNumber14.setVisibility(View.GONE);
        txtNumber13.setVisibility(View.GONE);
        txtNumber12.setVisibility(View.GONE);
        txtNumber11.setVisibility(View.GONE);
        editWord19.setVisibility(View.GONE);
        editWord18.setVisibility(View.GONE);
        editWord17.setVisibility(View.GONE);
        editWord16.setVisibility(View.GONE);
        editWord15.setVisibility(View.GONE);
        editWord14.setVisibility(View.GONE);
        editWord13.setVisibility(View.GONE);
        editWord12.setVisibility(View.GONE);
        editWord11.setVisibility(View.GONE);

        imgStar9.setVisibility(View.GONE);
        imgStar8.setVisibility(View.GONE);
        imgStar7.setVisibility(View.GONE);
        imgStar6.setVisibility(View.GONE);
        imgStar5.setVisibility(View.GONE);
        imgStar4.setVisibility(View.GONE);
        imgStar3.setVisibility(View.GONE);
        imgStar2.setVisibility(View.GONE);
        imgStar1.setVisibility(View.GONE);



        imgEmoji9.setVisibility(View.GONE);
        imgEmoji8.setVisibility(View.GONE);
        imgEmoji7.setVisibility(View.GONE);
        imgEmoji6.setVisibility(View.GONE);
        imgEmoji5.setVisibility(View.GONE);
        imgEmoji4.setVisibility(View.GONE);
        imgEmoji3.setVisibility(View.GONE);
        imgEmoji2.setVisibility(View.GONE);
        imgEmoji1.setVisibility(View.GONE);

    }

}
