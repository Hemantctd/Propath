package org.ctdworld.propath.activity;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractCoachFeedback;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterCoachFeedback;

import java.util.List;

public abstract class ActivityCoachSelfReviewDescription extends AppCompatActivity implements ContractCoachFeedback.View, View.OnClickListener{

   // private static final String TAG = ActivityCoachSelfReviewDescription.class.getSimpleName();
    public static final String REVIEW_ID = "review_id";
    public static final String ATHLETE_ID = "athlete_id";

    Context mContext;

    Toolbar mToolbar;
    TextView mTxtToolbarTitle;
    ImageView mToolBarImageOptionsMenu;

    TextView mEventText;
    TextView mStrengthsText;
    TextView mImprovementsText;
    TextView mSuggestionText;
    TextView mAssistanceText;
    String mReviewID, mAthleteID;

    ProgressBar mProgressBar;

    ContractCoachFeedback.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_self_review_description);

        init();
        prepareToolbar();
        //setTextAndImage();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null )
        {
            mReviewID = bundle.getString(REVIEW_ID);
            mAthleteID= bundle.getString(ATHLETE_ID);
        }

        String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String athleteRoleId= RoleHelper.ATHLETE_ROLE_ID;

        if (loggedInUser.equals(athleteRoleId)) {
            mAthleteID = SessionHelper.getInstance(mContext).getUser().getUserId();
            onShowProgress();
            mPresenter.getCoachSelfReviewDescription(mAthleteID,mReviewID);
        }
        else
        { onShowProgress();

            mPresenter.getCoachSelfReviewDescription(mAthleteID,mReviewID);
        }
    }



    // for initialization
    private void init()
    {
        mContext = this;
        mPresenter = new PresenterCoachFeedback(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mProgressBar = findViewById(R.id.progress_bar);
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mEventText = findViewById(R.id.event_edit);
        mImprovementsText = findViewById(R.id.improvements_edit);
        mStrengthsText = findViewById(R.id.strengths_edit);
        mSuggestionText = findViewById(R.id.suggestion_edit);
        mAssistanceText = findViewById(R.id.assistance_edit);
        mToolBarImageOptionsMenu = findViewById(R.id.toolbar_img_options_menu);


        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID))
        {
            mToolBarImageOptionsMenu.setVisibility(View.VISIBLE);
            mToolBarImageOptionsMenu.setOnClickListener(this);
        }
    }

    //setting toolbar
    private void prepareToolbar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.self_review);
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
    public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {

    }

    @Override
    public void onReceivedCoachFeedbackDescription(CoachData coachData) {

    }

    @Override
    public void onFailed() {
        DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onCoachFeedbackDeleted(String id) {

    }

    @Override
    public void onReceivedCoachSelfReview(List<CoachData> coachData) {

    }

    @Override
    public void onReceivedCoachSelfReviewDescription(CoachData coachData) {

        onHideProgress();


        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails(coachData));
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();

        mAssistanceText.setText(coachData.getAssistanceRequired());
        mSuggestionText.setText(coachData.getSuggestions());
        mStrengthsText.setText(coachData.getStrenths());
        mImprovementsText.setText(coachData.getImprovements());
        mEventText.setText(coachData.getEvents());
    }



    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails(CoachData coachData)
    {
        if (coachData == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(coachData.getUserName());
        createdDataDetails.setRoleId(coachData.getAthleteID());
        createdDataDetails.setRole("Athlete");
        createdDataDetails.setUserPicUrl(coachData.getProfileImage());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(coachData.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(null);

        if (!SessionHelper.getInstance(mContext).getUser().getUserId().equals(mAthleteID)) {
            createdDataDetails.setCreated_by(coachData.getUserName());
        }

        return createdDataDetails;
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
    public void onSetViewsEnabledOnProgressBarGone()
    {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onShowMessage(String message) {

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
                        case BottomSheetOption.OPTION_EDIT :
                            Intent intent = new Intent(mContext, ActivityCoachCreateSelfReview.class);
                            intent.putExtra(ActivityCoachCreateSelfReview.KEY_ATHLETE_ID, mAthleteID);
                            intent.putExtra(ActivityCoachCreateSelfReview.KEY_REVIEW_ID, mReviewID);
                            intent.putExtra(ActivityCoachCreateSelfReview.KEY_MODE_CREATE_OR_EDIT, ActivityCoachCreateSelfReview.VALUE_MODE_EDIT);
                            startActivity(intent);
                            break;

                        case BottomSheetOption.OPTION_DELETE :
                            DialogHelper.showSimpleCustomDialog(mContext, "Are you sure want to delete this ?", new DialogHelper.ShowSimpleDialogListener() {
                                @Override
                                public void onOkClicked() {
                                    mPresenter.deleteCoachSelfReview(mReviewID,mAthleteID);
                                    Intent i = new Intent(mContext,ActivityCoachFeedback.class);
                                    i.putExtra(ActivityCoachFeedback.KEY_IS_FRAGMENT_SINGLE, false);
                                    i.putExtra(ActivityCoachFeedback.KEY_FRAGMENT_TYPE, ActivityCoachFeedback.FRAGMENT_COACH_VIEW_FEEDBACK);
                                    startActivity(i);
                                    finish();
                                }
                            });

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
}
