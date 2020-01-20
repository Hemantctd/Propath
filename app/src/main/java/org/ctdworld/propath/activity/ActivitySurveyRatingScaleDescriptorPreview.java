package org.ctdworld.propath.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Survey;

import java.util.List;


public class ActivitySurveyRatingScaleDescriptorPreview extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private Toolbar mToolbar;   // toolbar
    private TextView mTxtToolbarTitle;  // for toolbar title
    ImageView mToolbarImageOptions;

    Bundle mBundle;
    Survey.SurveyData.RatingScale mRatingScale;
    LinearLayout wordsNumbersLayout1,wordsNumbersLayout2;
    TextView question1;
    List<Survey.SurveyData.RatingScale.Questions> mRatingScaleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_rating_scale_descriptor_preview);
        mBundle = getIntent().getExtras();
        if (mBundle != null)
        {
            mRatingScale = (Survey.SurveyData.RatingScale) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_RATING_SCALE);
        }
        init();
        prepareToolbar();


        mRatingScaleList = mRatingScale.getSurveRatingScaleQuestionList();
        for (int i = 0; i<mRatingScaleList.size(); i++)
        {
            Survey.SurveyData.RatingScale.Questions questions = mRatingScaleList.get(0);
            question1.setText("1. " +questions.getQuestion());

        }


        showDescriptor();
    }


    private void init() {
        question1= findViewById(R.id.question1);
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);
        mToolbarImageOptions.setVisibility(View.VISIBLE);
        mToolbarImageOptions.setOnClickListener(this);
        wordsNumbersLayout1 = findViewById(R.id.words_numbersLayout1);
        wordsNumbersLayout2 = findViewById(R.id.words_numbersLayout2);
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
        if (v.getId() == R.id.toolbar_img_options_menu)
        {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit");
            builder.addOption(BottomSheetOption.OPTION_SAVE, "Save");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    switch (option) {
                        case BottomSheetOption.OPTION_EDIT:

                            break;

                        case BottomSheetOption.OPTION_SAVE:

                            break;
                    }

                }
            });

            try {
                options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

        }

    }



    private void showDescriptor( )
    {

        if (mRatingScale.getRatingScaleFormat().equals("Numbers"))
        {
            showNumbers();
        }
        else if (mRatingScale.getRatingScaleFormat().equals("Graphics"))
        {
            if (mRatingScale.getRatingScaleGraphicsItem().equals("Star"))
            {
                showStar();
            }
            else
            {
                showEmoji();
            }
        }
        else if (mRatingScale.getRatingScaleFormat().equals("Words"))
        {
            showWords();
        }
        else
        {
            showWordsAndNumbers();
        }
    }



    private void showNumbers()
    {
        if (mRatingScale.getRatingScaleRange().equals("1"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("2"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("3"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("4"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("5"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("6"))
        {

        }
        else
        {

        }

    }


    private void showStar()
    {
        if (mRatingScale.getRatingScaleRange().equals("1"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("2"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("3"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("4"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("5"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("6"))
        {

        }
        else
        {

        }

    }


    private void showEmoji()
    {
        if (mRatingScale.getRatingScaleRange().equals("1"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("2"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("3"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("4"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("5"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("6"))
        {

        }
        else
        {

        }

    }

    private void showWords()
    {
        if (mRatingScale.getRatingScaleRange().equals("1"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("2"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("3"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("4"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("5"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("6"))
        {

        }
        else
        {

        }

    }


    private void showWordsAndNumbers()
    {
        if (mRatingScale.getRatingScaleRange().equals("1"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("2"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("3"))
        {
            wordsNumbersLayout1.setVisibility(View.VISIBLE);

        }
        else if (mRatingScale.getRatingScaleRange().equals("4"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("5"))
        {

        }
        else if (mRatingScale.getRatingScaleRange().equals("6"))
        {

        }
        else
        {
            wordsNumbersLayout1.setVisibility(View.VISIBLE);
            wordsNumbersLayout2.setVisibility(View.VISIBLE);
        }

    }


}
