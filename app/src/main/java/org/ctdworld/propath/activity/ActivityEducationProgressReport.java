package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSchoolProgressReport;
import org.ctdworld.propath.contract.ContractProgressReport;
import org.ctdworld.propath.contract.ContractSchoolReview;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.model.SchoolReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterProgressReport;
import org.ctdworld.propath.presenter.PresenterSchoolReview;

import java.util.ArrayList;
import java.util.List;

public abstract class ActivityEducationProgressReport extends AppCompatActivity implements ContractProgressReport.View,View.OnClickListener, ContractSchoolReview.View
{
    private static final String TAG = ActivityEducationProgressReport.class.getSimpleName();
    public static final String PROGRESS_REVIEW_ID = "review_id";
    public static final String PROGRESS_ATHLETE_ID = "athlete_id";


    Toolbar mToolbar;
    Context mContext;
    Button subjectAdd,submitSchoolReview;
    RecyclerView recyclerSchoolReview;
    TextView mTxtToolbarTitle;
   /* TextView school_review_txt_user_name;
    TextView school_review_created_date;
    TextView school_review_teacher_name;*/
    ContractProgressReport.Presenter mPresenter;
    ContractSchoolReview.Presenter mSchoolReviewPresenter;
   // ImageView school_review_img_profile;
    ImageView mToolbarImageOptions;
    List<SchoolReview> schoolReviewList = new ArrayList<>();
    String athlete_id,review_id;
    AdapterSchoolProgressReport adapter;
    ProgressBar mProgressBar;

    LinearLayout mStrengthLayout;
    LinearLayout mImprovementsLayout;
    LinearLayout mSuggestionsLayout;
    LinearLayout mAssistanceLayout;

    TextView mStrengthsText;
    TextView mImproventsText;
    TextView mSuggestionsText;
    TextView mAssistanceText;
    TextView mAssistance;
    //TextView mRole;
    TextView mQualification;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_progress_report);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null )
        {
            athlete_id = bundle.getString(PROGRESS_ATHLETE_ID);
            review_id = bundle.getString(PROGRESS_REVIEW_ID);


        }

        Log.d(TAG,"review_id" +review_id);
        Log.d(TAG,"athlete_id" +athlete_id);

        init();
        prepareToolbar();
        setProgressReportAdapter();



        String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String athleteRoleId= RoleHelper.ATHLETE_ROLE_ID;
        String teacherRoleId= RoleHelper.TEACHER_ROLE_ID;

        if (loggedInUser.equals(athleteRoleId)) {
            athlete_id = SessionHelper.getInstance(mContext).getUser().getUserId();
            mPresenter.getProgressReport(athlete_id,review_id);

        }
        else
        {
            mPresenter.getProgressReport(athlete_id,review_id);

        }

        if (loggedInUser.equals(athleteRoleId) || loggedInUser.equals(teacherRoleId))
        {
            mStrengthLayout.setVisibility(View.VISIBLE);
            mImprovementsLayout.setVisibility(View.VISIBLE);
            mSuggestionsLayout.setVisibility(View.VISIBLE);
            mAssistanceLayout.setVisibility(View.VISIBLE);

            if (loggedInUser.equals(athleteRoleId))
            {
                mAssistance.setText(R.string.assistance_requested);
            }
        }
        else {
            mStrengthLayout.setVisibility(View.GONE);
            mImprovementsLayout.setVisibility(View.GONE);
            mSuggestionsLayout.setVisibility(View.GONE);
            mAssistanceLayout.setVisibility(View.GONE);
        }

    }
    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mProgressBar = findViewById(R.id.progress_bar);
        mPresenter = new PresenterProgressReport(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mSchoolReviewPresenter = new PresenterSchoolReview(mContext,this);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
      //  school_review_img_profile = findViewById(R.id.school_review_img_profile);
       // school_review_txt_user_name = findViewById(R.id.school_review_txt_user_name);
        recyclerSchoolReview=findViewById(R.id.recycler_school_review);
        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);
      //  comments = findViewById(R.id.comments_given_by_teacher);
        //school_review_created_date = findViewById(R.id.school_review_created_date);
        //school_review_teacher_name = findViewById(R.id.school_review_teacher_name);
        subjectAdd = findViewById(R.id.subjectAdd);
        submitSchoolReview = findViewById(R.id.submitSchoolReview);
        mProgressBar.setVisibility(View.VISIBLE);

        mStrengthLayout = findViewById(R.id.strengths_layout);
        mImprovementsLayout = findViewById(R.id.improvements_layout);
        mSuggestionsLayout = findViewById(R.id.suggestions_layout);
        mAssistanceLayout = findViewById(R.id.assistance_layout);

        mStrengthsText = findViewById(R.id.strength_text);
        mImproventsText = findViewById(R.id.improvements_text);
        mSuggestionsText = findViewById(R.id.suggestions_text);
        mAssistanceText = findViewById(R.id.assistance_text);
        mAssistance = findViewById(R.id.assistance);
        //mRole = findViewById(R.id.role);
        mQualification = findViewById(R.id.qualification_text);

        if(SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.TEACHER_ROLE_ID))
        {
            mToolbarImageOptions.setVisibility(View.VISIBLE);
            mToolbarImageOptions.setOnClickListener(this);

        }

    }

    private void setProgressReportAdapter()
    {
        adapter = new AdapterSchoolProgressReport(ActivityEducationProgressReport.this,schoolReviewList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSchoolReview.setLayoutManager(layoutManager);
        recyclerSchoolReview.setAdapter(adapter);


    }
    private void prepareToolbar()
    {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.progress_report);
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
    public void getOnReceivedProgressReport(SchoolReview progressReport, List<SchoolReview> progressReportList)
    {

        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails(progressReport));
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();
       /* int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);
        int picHeight = UtilHelper.convertDpToPixel(mContext,picDimen);

        Glide.with(mContext)
                .load(progressReport.getUserPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth,picHeight))
                .into(school_review_img_profile);*/


      //  school_review_txt_user_name.setText(progressReport.getAthleteName());
        mAssistanceText.setText(progressReport.getAssistance());
        mSuggestionsText.setText(progressReport.getSuggestions());
        mStrengthsText.setText(progressReport.getStrengths());
        mImproventsText.setText(progressReport.getImprovements());
        mQualification.setText(progressReport.getQualification());


     /*   String creatorName = "By" + " : "+ progressReport.getTeacherName();
        school_review_teacher_name.setText(creatorName);
        String role = "Role" + " : "+ "Teacher";
        mRole.setText(role);
        school_review_created_date.setText(DateTimeHelper.getDateTime(progressReport.getDate(),DateTimeHelper.FORMAT_DATE_TIME));
*/
        schoolReviewList = progressReportList;

        adapter.updateList(schoolReviewList);

        }


    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails(SchoolReview schoolReview)
    {
        if (schoolReview == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(schoolReview.getAthleteName());
        createdDataDetails.setRoleId(schoolReview.getAthleteID());
        createdDataDetails.setRole("Teacher");
        createdDataDetails.setUserPicUrl(schoolReview.getUserPicUrl());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(schoolReview.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(null);

        //if (!SessionHelper.getInstance(mContext).getUser().getUserId().equals(schoolReview.getTeacherID())) {
            createdDataDetails.setCreated_by(schoolReview.getTeacherName());
       // }

        return createdDataDetails;
    }

    @Override
    public void onFailed() {
        DialogHelper.showSimpleCustomDialog(mContext,"Failed");
    }

    @Override
    public void onShowProgress() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onHideProgress() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onShowMessage(String message) {
     DialogHelper.showSimpleCustomDialog(mContext,message);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_img_options_menu)
        {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            builder.addOption(BottomSheetOption.OPTION_EDIT,"Edit");
            builder.addOption(BottomSheetOption.OPTION_DELETE,"Delete");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
            {
                @Override
                public void onOptionSelected(int option)
                {
                    switch (option)
                    {
                        case BottomSheetOption.OPTION_DELETE:

                            DialogHelper.showSimpleCustomDialog(mContext, "Are you sure want to delete this ?", new
                                    DialogHelper.ShowSimpleDialogListener() {
                                        @Override
                                        public void onOkClicked() {
                                            mSchoolReviewPresenter.deleteReview(review_id);
                                            Intent intentdelete = new Intent(mContext,ActivityProgressReportList.class);
                                            intentdelete.putExtra("athlete_id",athlete_id);
                                            startActivity(intentdelete);
                                            finish();
                                        }
                                    });

                            break;

                        case BottomSheetOption.OPTION_EDIT:
                            Intent intent = new Intent(mContext, ActivityEducationSchoolReviewCreate.class);
                            intent.putExtra(ActivityEducationSchoolReviewCreate.KEY_ATHLETE_ID, athlete_id);
                            intent.putExtra(ActivityEducationSchoolReviewCreate.KEY_REVIEW_ID, review_id);
                            intent.putExtra(ActivityEducationSchoolReviewCreate.KEY_MODE_CREATE_OR_EDIT, ActivityEducationSchoolReviewCreate.VALUE_MODE_EDIT);
                            startActivity(intent);
                            break;
                    }

                }
            });

            try {
                options.show(((AppCompatActivity)mContext).getSupportFragmentManager(),   ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            }catch (ClassCastException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public void onProgressListDeleted(String id) {

    }

    @Override
    public void onReceivedProgressReportList(List<SchoolReview> schoolReviewList) {

    }
}
