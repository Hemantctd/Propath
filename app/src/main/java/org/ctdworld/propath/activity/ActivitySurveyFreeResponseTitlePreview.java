package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Survey;

import java.util.List;

public class ActivitySurveyFreeResponseTitlePreview extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG =  ActivitySurveyFreeResponseTitlePreview.class.getSimpleName();

   /* public static final String SURVEY_QUESTIONS_LIST =  "survey_questions_items";
    public static final String SURVEY_YES_NO =  "survey_yes_no_items";
    public static final String KEY_SURVEY_CREATE_EDIT =  "survey_type";*/
    Toolbar mToolbar;   // toolbar
    TextView mTxtToolbarTitle;  // for toolbar title
    Context mContext;
    Button mNextButton;
    EditText mTitle, mDesc;
    String mSurveyType;
    Survey.SurveyData.FreeResponse freeResponse;
    List<Survey.SurveyData.FreeResponse.Questions> questionsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_yes_or_no_title_preview);
        init();
        prepareToolbar();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null ) {

            mSurveyType = bundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
            freeResponse = (Survey.SurveyData.FreeResponse) bundle.getSerializable(ActivityCreateSurvey.SURVEY_FREE_RESPONSE);
            if (freeResponse != null) {
                questionsList = freeResponse.getQuestionsList();
            }

        }

        if (freeResponse!= null) {
            mTitle.setText(freeResponse.getTitle());
            mDesc.setText(freeResponse.getDescription());

        }
    }


    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mTxtToolbarTitle.setText(R.string.survey_preview);
        mTitle = findViewById(R.id.title_text);
        mDesc = findViewById(R.id.desc_text);
        mNextButton  = findViewById(R.id.next_btn);
        mNextButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_btn) {
            if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)) {
                Intent intent = new Intent(mContext, ActivitySurveyPreviewAndSaveQuestions.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, freeResponse);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                startActivity(intent);
            } else if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                Intent intent = new Intent(mContext, ActivitySurveyPreviewAndSaveQuestions.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, freeResponse);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, ActivitySurveyPreviewAndSaveQuestions.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, freeResponse);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);
                startActivity(intent);
            }
        }
    }
}
