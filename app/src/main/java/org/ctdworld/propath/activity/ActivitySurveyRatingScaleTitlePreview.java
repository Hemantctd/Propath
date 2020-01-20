package org.ctdworld.propath.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Survey;

public class ActivitySurveyRatingScaleTitlePreview extends AppCompatActivity implements View.OnClickListener {


    private Context mContext;
    private Toolbar mToolbar;   // toolbar
    private TextView mTxtToolbarTitle;  // for toolbar title
    private Button mBtnNext;
    Bundle mBundle;
    Survey.SurveyData.RatingScale mRatingScale;
    EditText mEditTitle, mEditDesc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_rating_scale_title_preview);
        mBundle = getIntent().getExtras();
        if (mBundle != null)
        {
            mRatingScale = (Survey.SurveyData.RatingScale) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_RATING_SCALE);
        }
        init();
        prepareToolbar();

        mEditTitle.setText(mRatingScale.getTitle());
        mEditDesc.setText(mRatingScale.getDescription());

    }












    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mEditDesc = findViewById(R.id.descriptionPreview);
        mEditTitle = findViewById(R.id.titlePreview);
        mBtnNext = findViewById(R.id.next_btn);
        mBtnNext.setOnClickListener(this);



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
            case R.id.next_btn :


                Intent intent = new Intent(mContext,ActivitySurveyRatingScaleDescriptorPreview.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_RATING_SCALE, mRatingScale);
                startActivity(intent);
                break;
        }
    }
}
