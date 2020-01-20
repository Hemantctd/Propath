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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMultipleChoiceSurveyQuestionsPreview;
import org.ctdworld.propath.contract.ContractCreateMultipleChoiceSurvey;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.presenter.PresenterCreateMultipleChoiceSurvey;
import org.ctdworld.propath.presenter.PresenterCreateSurvey;

import java.util.List;

public abstract class ActivitySurveyMultipleChoiceQuestionsPreview extends AppCompatActivity implements View.OnClickListener, ContractCreateMultipleChoiceSurvey.View , ContractSurvey.View {

   // private final String TAG = ActivitySurveyMultipleChoiceQuestionsPreview.class.getSimpleName();

    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView mRecyclerView;
    Context mContext;
    Survey.SurveyData.MultipleChoice surveyMultipleChoice;
    List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsArrayList;
    AdapterMultipleChoiceSurveyQuestionsPreview adapterSurveyMultipleChoiceQuestions;
    ContractCreateMultipleChoiceSurvey.Presenter mPresenter;
    String mSurveyType;
    ContractSurvey.Presenter mPresenters;
    LinearLayout mCommonDataLayout;
    TextView surveyDesc, surveyTitle, surveyType;
    ImageView mToolbarImageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_multiple_choice_questions_preview);

        Bundle mBundle = getIntent().getExtras();
        if(mBundle != null)
        {
            mSurveyType = mBundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
            surveyMultipleChoice = (Survey.SurveyData.MultipleChoice) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE);
            if (surveyMultipleChoice != null) {
                surveyQuestionsArrayList = surveyMultipleChoice.getSurveyMultipleQuestionList();
            }
        }

        init();
        setToolbar();
        prepareRecyclerView();
    }

    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRecyclerView = findViewById(R.id.recycler_view_multiple_choice);
        mPresenter = new PresenterCreateMultipleChoiceSurvey(mContext,this);
        mPresenters = new PresenterCreateSurvey(mContext,this);

        surveyTitle = findViewById(R.id.set_survey_title);
        surveyType = findViewById(R.id.set_survey_type);
        surveyDesc = findViewById(R.id.set_survey_desc);
        mCommonDataLayout = findViewById(R.id.common_data_layout);

        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);
        mToolbarImageOptions.setVisibility(View.VISIBLE);
        mToolbarImageOptions.setOnClickListener(this);


            if (surveyMultipleChoice != null) {
                mCommonDataLayout.setVisibility(View.VISIBLE);
                surveyTitle.setText(surveyMultipleChoice.getTitle());
                surveyType.setText(surveyMultipleChoice.getQuestionType());
                surveyDesc.setText(surveyMultipleChoice.getDescription());
            }


    }

    private void prepareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapterSurveyMultipleChoiceQuestions = new AdapterMultipleChoiceSurveyQuestionsPreview(this,surveyQuestionsArrayList);
        mRecyclerView.setAdapter(adapterSurveyMultipleChoiceQuestions);
    }

    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.multiple_choice_preview);
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

                            if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                                Intent intentEdit = new Intent(ActivitySurveyMultipleChoiceQuestionsPreview.this, ActivityCreateSurvey.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                                intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_MULTIPLE_CHOICE);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                                startActivity(intentEdit);
                            }
                            else {
                                Intent intentEdit = new Intent(ActivitySurveyMultipleChoiceQuestionsPreview.this, ActivityCreateSurvey.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                                intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_MULTIPLE_CHOICE);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                                startActivity(intentEdit);
                            }
                            break;

                        case BottomSheetOption.OPTION_SAVE:
                            List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList = adapterSurveyMultipleChoiceQuestions.getSurveyQusetions();
                            if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
                            {
                                mPresenters.editMultipleChoiceSurvey(surveyQuestionsList, surveyMultipleChoice);
                            }
                            else
                            {
                                mPresenter.saveSurvey(surveyQuestionsList, surveyMultipleChoice);
                            }
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

    @Override
    public void onSuccess(String msg) {

        DialogHelper.showSimpleCustomDialog(mContext, msg, new DialogHelper.ShowSimpleDialogListener() {
            @Override
            public void onOkClicked() {
                mContext.startActivity(new Intent(mContext, ActivitySurvey.class));
                finish();
            }
        });
    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg);
    }

    @Override
    public void onReceivedSurvey(Survey.SurveyData surveyData) {

    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {

    }

    @Override
    public void onShowMessage(String message) {

    }
}
