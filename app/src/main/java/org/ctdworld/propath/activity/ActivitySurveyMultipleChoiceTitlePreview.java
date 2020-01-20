package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyQusetions;

import java.util.ArrayList;
import java.util.List;

public class ActivitySurveyMultipleChoiceTitlePreview extends AppCompatActivity implements View.OnClickListener {


    private final String TAG = ActivitySurveyMultipleChoiceTitlePreview.class.getSimpleName();

    Toolbar mToolbar;   // toolbar
    TextView mTxtToolbarTitle;  // for toolbar title
    Context mContext;
    Button mNextButton;
    String get_qusetionType, get_number_of_qusetions, getSurveyTitle, getSurveyDescription, survey_type, survey_id;
    ArrayList<SurveyQusetions> questionList = new ArrayList<>();
    EditText mSurveyTitle, mSurveyDesc;

    Survey.SurveyData.MultipleChoice surveyMultipleChoice ;
    String mSurveyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_multiple_choice_title_preview);

        init();
        prepareToolbar();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null ) {

            mSurveyType = bundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
            surveyMultipleChoice = (Survey.SurveyData.MultipleChoice) bundle.getSerializable(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE);
        }

        if (surveyMultipleChoice != null) {
            mSurveyTitle.setText(surveyMultipleChoice.getTitle());
            mSurveyDesc.setText(surveyMultipleChoice.getDescription());

        }

    }


    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mTxtToolbarTitle.setText("Preview Survey");
        mNextButton  = findViewById(R.id.next_btn);
        mNextButton.setOnClickListener(this);
        mSurveyTitle = findViewById(R.id.titlePreview);
        mSurveyDesc = findViewById(R.id.descriptionPreview);


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
                Intent intent = new Intent(mContext, ActivitySurveyMultipleChoiceQuestionsPreview.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                startActivity(intent);
            } else if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                Intent intent = new Intent(mContext, ActivitySurveyMultipleChoiceQuestionsPreview.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, ActivitySurveyMultipleChoiceQuestionsPreview.class);
                intent.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);
                startActivity(intent);
            }
        }
    }
}
