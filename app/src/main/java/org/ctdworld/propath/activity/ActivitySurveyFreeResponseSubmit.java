package org.ctdworld.propath.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyFreeResponseSubmit;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Survey;


public class ActivitySurveyFreeResponseSubmit extends AppCompatActivity implements View.OnClickListener{

    Toolbar mToolbar;
    TextView mToolbarTitle;
    // Button btnSurveySave, btnSurveyEdit;
    Context mContext;
    RecyclerView mRecyclerView;
    LinearLayout mCommonDataLayout;
    TextView mSurveyDesc, mSurveyTitle, mSurveyType;
    AdapterSurveyFreeResponseSubmit adapter;
    String msurveyType;
    ProgressBar mProgressBar;
    Bundle mBundle = null;
    Survey.SurveyData.FreeResponse mSurveyFreeResponse;
    ImageView mToolbarImageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_free_response_submit);
        init();
        setToolBar();
        setSurveyAdapter();

    }

    //for set adapter
    private void setSurveyAdapter() {

        adapter = new AdapterSurveyFreeResponseSubmit(ActivitySurveyFreeResponseSubmit.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    // for initialization
    private void init() {
        mContext = this;
        mProgressBar = findViewById(R.id.progress_bar);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
       /* btnSurveyEdit = findViewById(R.id.btn_survey_edit);
        btnSurveySave = findViewById(R.id.btn_survey_save);*/
        mRecyclerView = findViewById(R.id.survey_questions_preview_recycler_view);
        mSurveyTitle = findViewById(R.id.set_survey_title);
        mSurveyType = findViewById(R.id.set_survey_type);
        mSurveyDesc = findViewById(R.id.set_survey_desc);
        mCommonDataLayout = findViewById(R.id.common_data_layout);
        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);
        mToolbarImageOptions.setVisibility(View.VISIBLE);
        mToolbarImageOptions.setOnClickListener(this);

    }



    // button clickable
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.toolbar_img_options_menu) {

            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            builder.addOption(BottomSheetOption.OPTION_SAVE, "Save");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    switch (option) {
                        case BottomSheetOption.OPTION_EDIT:


                        case BottomSheetOption.OPTION_SAVE:
                            saveSurveyData();
                           // mProgressBar.setVisibility(View.VISIBLE);
                            break;
                    }

                }
            });

            try {
                options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

           /* case R.id.btn_survey_save:
                saveSurveyData();
                mProgressBar.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_survey_edit:

                   if (msurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
                    {
                        Intent intentEdit = new Intent(ActivitySurveyPreviewAndSaveQuestions.this, ActivityCreateSurvey.class);
                        intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        intentEdit.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                        intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_FREE_RESPONSE);
                        startActivity(intentEdit);
                    }
                   else {
                       Intent intentEdit = new Intent(ActivitySurveyPreviewAndSaveQuestions.this, ActivityCreateSurvey.class);
                       intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                       intentEdit.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                       intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_FREE_RESPONSE);
                       startActivity(intentEdit);
                   }


                break;*/
        }
    }

    // to save survey
    public void saveSurveyData() {



    }




    // set tool bar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey);
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
