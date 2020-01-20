package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterRatingScaleQuestions;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Survey;

import java.util.List;

public class ActivityRatingScaleQuestions extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerRatingQuestions;
    Button mButtonNext;
    Context mContext;
    Toolbar mToolbar;   // toolbar
    TextView mTxtToolbarTitle;  // for toolbar title

    Survey.SurveyData.RatingScale surveyRatingScale ;
    AdapterRatingScaleQuestions adapterRatingScaleQuestions;
    ImageView mAddQuestions;
    List<Survey.SurveyData.RatingScale.Questions> mSurveyQuestionList;
    String mSurveyType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_scale_questions);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mSurveyType = bundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
                surveyRatingScale = (Survey.SurveyData.RatingScale) bundle.getSerializable(ActivityCreateSurvey.SURVEY_RATING_SCALE);
            if (surveyRatingScale != null) {
                mSurveyQuestionList = surveyRatingScale.getSurveRatingScaleQuestionList();
            }
        }


        init();
        prepareToolbar();
        setRecyclerAdapter();


        if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)|| mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
        {
            adapterRatingScaleQuestions.addQuestionList(mSurveyQuestionList);
        }
        else
        {
            int noOfQuestions = Integer.parseInt(surveyRatingScale.getQuestionNo());
            for (int i = 0; i < noOfQuestions; i++) {
                adapterRatingScaleQuestions.addQuestion(new Survey.SurveyData.RatingScale.Questions());
            }
        }

    }

    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRecyclerRatingQuestions = findViewById(R.id.recycler_rating_scale_questions);
        mButtonNext = findViewById(R.id.rating_next);
        mButtonNext.setOnClickListener(this);

        mAddQuestions = findViewById(R.id.add_questions);
        mAddQuestions.setOnClickListener(this);
        if (Integer.parseInt(surveyRatingScale.getQuestionNo()) == 20)
        {
            mAddQuestions.setVisibility(View.GONE);
        }
    }

    private void setRecyclerAdapter()
    {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerRatingQuestions.setLayoutManager(linearLayoutManager);
        adapterRatingScaleQuestions = new AdapterRatingScaleQuestions(getApplicationContext());
        mRecyclerRatingQuestions.setAdapter(adapterRatingScaleQuestions);

    }

    @Override
    public void onClick(View v) {


        switch(v.getId())
        {
            case R.id.add_questions :
                adapterRatingScaleQuestions.addQuestion(new Survey.SurveyData.RatingScale.Questions());
                if(adapterRatingScaleQuestions.getItemCount() == 20)
                {
                    mAddQuestions.setVisibility(View.GONE);
                }


                break;

            case R.id.rating_next :
                List<Survey.SurveyData.RatingScale.Questions> list =  adapterRatingScaleQuestions.getSurveyQuestions();

                surveyRatingScale.setSurveyRatingScaleQuestionList(list);
                if (list == null)
                {
                    DialogHelper.showSimpleCustomDialog(this,"Please fill questions ");
                }
                else {
                    if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)) {
                        Intent surveyDataSend = new Intent(this, ActivityRatingScaleSurvey.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_RATING_SCALE, surveyRatingScale);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                        startActivity(surveyDataSend);
                        finish();
                    }
                    else if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                        Intent surveyDataSend = new Intent(this, ActivityRatingScaleSurvey.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_RATING_SCALE, surveyRatingScale);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        startActivity(surveyDataSend);
                        finish();
                    }
                    else {
                        Intent surveyDataSend = new Intent(this, ActivityRatingScaleSurvey.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_RATING_SCALE, surveyRatingScale);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);
                        startActivity(surveyDataSend);
                        finish();
                    }
                }

                break;

    }
    }




    private void prepareToolbar()
    {
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
}
