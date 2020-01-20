package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyQuestions;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Survey;
import java.util.List;

public class ActivitySurveyQuestionsActivity extends AppCompatActivity implements View.OnClickListener {
   //private final String TAG = ActivitySurveyQuestionsActivity.class.getSimpleName();

    Button btnSurveyPreview;
    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView survey_questions_recycler_view;
    String survey_type;
    AdapterSurveyQuestions adapter;
    LinearLayout edit_save_Layout;
    List<Survey.SurveyData.FreeResponse.Questions> questionList;
    Survey.SurveyData.FreeResponse surveyFreeResponse;
    ImageView imgAddQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_questions);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null )
        {
            surveyFreeResponse = (Survey.SurveyData.FreeResponse) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_FREE_RESPONSE);
            if (surveyFreeResponse != null) {
                questionList = surveyFreeResponse.getQuestionsList();
            }
            survey_type = mBundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
        }


        init();
        prepareRecyclerView();
        setToolbar();

        if (survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT) || survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
        {
            adapter.addQuestionList(questionList);
        }
        else
        {
            int noOfQuestions = Integer.parseInt(surveyFreeResponse.getQuestionNo());
            for (int i = 0; i < noOfQuestions; i++) {
                adapter.addQuestion(new Survey.SurveyData.FreeResponse.Questions());
            }
        }

    }

    // for initialization
    private  void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        btnSurveyPreview = findViewById(R.id.btnSurveyPreview);
//        edit_save_Layout = findViewById(R.id.edit_save_Layout);
        imgAddQuestions = findViewById(R.id.img_add_questions);
        btnSurveyPreview.setOnClickListener(this);
        survey_questions_recycler_view = findViewById(R.id.survey_questions_recycler_view);
        imgAddQuestions.setOnClickListener(this);




        if (Integer.parseInt(surveyFreeResponse.getQuestionNo()) == 20)
        {
            imgAddQuestions.setVisibility(View.GONE);
        }
    }

    // setting adapter
    private void prepareRecyclerView()
    {
        adapter = new AdapterSurveyQuestions(ActivitySurveyQuestionsActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        survey_questions_recycler_view.setLayoutManager(layoutManager);
        survey_questions_recycler_view.setAdapter(adapter);

    }


    // setting toolbar
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        //mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(R.string.survey_questions);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }

    }

    // button clickable
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSurveyPreview:

                List<Survey.SurveyData.FreeResponse.Questions> list = adapter.getSurveyQusetions();
                surveyFreeResponse.setQuestionsList(list);

                if (list == null) {
                    DialogHelper.showSimpleCustomDialog(mContext, "Please fill questions ");
                }
                else {

                    if (survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)) {
                        Intent surveyDataSend = new Intent(ActivitySurveyQuestionsActivity.this, ActivitySurveyFreeResponseWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                        startActivity(surveyDataSend);
                    }
                    if (survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                        Intent surveyDataSend = new Intent(ActivitySurveyQuestionsActivity.this, ActivitySurveyFreeResponseWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);

                        startActivity(surveyDataSend);
                    } else {
                        Intent surveyDataSend = new Intent(ActivitySurveyQuestionsActivity.this, ActivitySurveyFreeResponseWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);
                        startActivity(surveyDataSend);
                    }

                }
                break;


            case R.id.img_add_questions:

                adapter.addQuestion(new Survey.SurveyData.FreeResponse.Questions());
                if (adapter.getItemCount() == 20) {
                    imgAddQuestions.setVisibility(View.GONE);
                }

                break;
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