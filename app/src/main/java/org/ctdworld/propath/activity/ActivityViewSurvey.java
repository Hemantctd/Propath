package org.ctdworld.propath.activity;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyPreviewQuestions;
import org.ctdworld.propath.model.Survey;

public class ActivityViewSurvey extends AppCompatActivity {

   // private final String TAG = ActivityCreateSurvey.class.getSimpleName();
    public static final String SURVEY_FREE_RESPONSE= "free_response_data";



    Toolbar mToolbar;
    TextView mToolbarTitle;
    Context mContext;
    RecyclerView survey_questions_preview_recycler_view;
    TextView surveyDesc, surveyTitle, surveyType;
    AdapterSurveyPreviewQuestions adapter;
    Bundle mBundle = null;

    Survey.SurveyData.FreeResponse surveyFreeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_survey);
        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            surveyFreeResponse = (Survey.SurveyData.FreeResponse) mBundle.getSerializable(SURVEY_FREE_RESPONSE);
            /*get_survey_title = mBundle.getString(SURVEY_TITLE);
            get_survey_type = mBundle.getString(SURVEY_QUESTION_TYPE);
            get_survey_desc_ = mBundle.getString(SURVEY_DESCRIPTION);
            get_survey_no_of_questions = mBundle.getString(SURVEY_NO_OF_QUESTIONS);
            questionsList = mBundle.getParcelableArrayList(SURVEY_QUESTION_LIST);


            if (questionsList != null) {
                for (int i = 0; i < questionsList.size(); i++) {
                    surveyQusetions = questionsList.get(i);
                    Log.d(TAG, "SurveyQusetions List : " + surveyQusetions.getQuestions());

                }
            }*/
        }

        init();
        setToolBar();
        setSurveyAdapter();
    }

    //for set adapter
    private void setSurveyAdapter() {
     /*   adapter = new AdapterSurveyPreviewQuestions(mContext, (ArrayList<Survey.SurveyData.FreeResponse.Questions>) surveyFreeResponse.get());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        survey_questions_preview_recycler_view.setLayoutManager(layoutManager);
        survey_questions_preview_recycler_view.setAdapter(adapter);*/
    }

    // for initialization
    private void init() {
        mContext = this;

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);

        survey_questions_preview_recycler_view = findViewById(R.id.survey_questions_preview_recycler_view);
        surveyTitle = findViewById(R.id.set_survey_title);
        surveyType = findViewById(R.id.set_survey_type);
        surveyDesc = findViewById(R.id.set_survey_desc);

        surveyTitle.setText(surveyFreeResponse.getTitle());
        surveyType.setText(surveyFreeResponse.getQuestionType());
        surveyDesc.setText(surveyFreeResponse.getDescription());


    }

    // set tool bar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey_preview);
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

