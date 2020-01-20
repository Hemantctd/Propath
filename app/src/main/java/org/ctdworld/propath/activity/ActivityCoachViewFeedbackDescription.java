package org.ctdworld.propath.activity;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public abstract class ActivityCoachViewFeedbackDescription extends AppCompatActivity implements ContractCoachFeedback.View, View.OnClickListener
{
    private static final String TAG = ActivityCoachViewFeedbackDescription.class.getSimpleName();

    Context mContext;
    Toolbar mToolbar;
    TextView mTxtToolbarTitle,coach_events,coach_strengths;
/*
    ,athlete_name,coach_name,date;
*/
    String review_id,athlete_id;
    ImageView mToolbarImageOptions;
    ContractCoachFeedback.Presenter mPresenter;
    LinearLayout mImprovementsLayout;
    LinearLayout mAssistanceLayout;
    LinearLayout mSuggestionLayout;

    TextView mSuggestionsText;
    TextView mImprovementsText;
    TextView mAssistanceText;
    TextView mAssistanceCoachText;
 //   TextView mRole;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_view_feedback_description);




        Bundle bundle = getIntent().getExtras();
        if(bundle != null )
        {
            review_id = bundle.getString("Review_ID");
            athlete_id= bundle.getString("athlete_id");
        }

        Log.d(TAG,"review_id" +review_id);

        String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String athleteRoleId= RoleHelper.ATHLETE_ROLE_ID;



        init();
        prepareToolbar();


        String coachRoleId = RoleHelper.COACH_ROLE_ID;

        if (loggedInUser.equals(coachRoleId) || loggedInUser.equals(athleteRoleId))
        {
            mSuggestionLayout.setVisibility(View.VISIBLE);
            mImprovementsLayout.setVisibility(View.VISIBLE);
            mAssistanceLayout.setVisibility(View.VISIBLE);

            if (loggedInUser.equals(athleteRoleId)) {
                mAssistanceText.setText(R.string.assistance_requested);
            }
        }
        else
        {
            mSuggestionLayout.setVisibility(View.GONE);
            mImprovementsLayout.setVisibility(View.GONE);
            mAssistanceLayout.setVisibility(View.GONE);
        }


        if (loggedInUser.equals(athleteRoleId)) {
            athlete_id = SessionHelper.getInstance(mContext).getUser().getUserId();
            mPresenter.getCoachFeedbackDescription(athlete_id,review_id);
        }
        else
        {
            mPresenter.getCoachFeedbackDescription(athlete_id,review_id);
        }

    }

    private void init()
    {
        mContext = this;
        mPresenter = new PresenterCoachFeedback(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        coach_events = findViewById(R.id.coach_events);
        coach_strengths = findViewById(R.id.coach_strengths);
       // coach_workons = findViewById(R.id.workons);

        mAssistanceText = findViewById(R.id.assistance_text);
        mAssistanceCoachText = findViewById(R.id.coach_assitance);
        mImprovementsText = findViewById(R.id.coach_improvements);
        mSuggestionsText = findViewById(R.id.coach_suggestion);
        //mRole = findViewById(R.id.role);

        mAssistanceLayout = findViewById(R.id.assistance_layout);
        mImprovementsLayout = findViewById(R.id.improvements_layout);
        mSuggestionLayout = findViewById(R.id.suggestion_layout);

     /*   athlete_name = findViewById(R.id.athlete_name);
        coach_name = findViewById(R.id.coach_name);
        coach_feedback_profile_img = findViewById(R.id.coach_feedback_profile_img);
        date = findViewById(R.id.coach_feedback_created_date);*/
        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);

        if(SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.COACH_ROLE_ID))
        {
            mToolbarImageOptions.setVisibility(View.VISIBLE);
            mToolbarImageOptions.setOnClickListener(this);

        }



    }


    private void prepareToolbar()
    {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.view_coach_feedback_title);
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

        return true;
    }



    @Override
    public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {

    }

    @Override
    public void onReceivedCoachFeedbackDescription(CoachData coachData) {



        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails(coachData));
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();
        coach_events.setText(coachData.getEvents());
        coach_strengths.setText(coachData.getStrenths());
        //coach_workons.setText(coachData.getWorkons());
        //athlete_name.setText(coachData.getAthlete_name());
        mSuggestionsText.setText(coachData.getSuggestions());
        mAssistanceCoachText.setText(coachData.getAssistanceOffered());
        mImprovementsText.setText(coachData.getImprovements());

       /* String creatorName = "By" + " : "+ coachData.getCoach_name();
        String role = "Role" + " : "+ "Coach";
        mRole.setText(role);
        coach_name.setText(creatorName);*/

/*
        date.setText(DateTimeHelper.getDateTime(coachData.getDate(), DateTimeHelper.FORMAT_DATE_TIME));

        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);
        int picHeight = UtilHelper.convertDpToPixel(mContext,picDimen);

        Glide.with(mContext)
                .load(coachData.getAthlete_img())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth,picHeight))
                .into(coach_feedback_profile_img);*/


    }

    @Override
    public void onFailed() {

        DialogHelper.showSimpleCustomDialog(mContext,"Failed");
    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
    }

    @Override
    public void onCoachFeedbackDeleted(String id) {

    }

    @Override
    public void onReceivedCoachSelfReview(List<CoachData> coachData) {

    }

    @Override
    public void onReceivedCoachSelfReviewDescription(CoachData coachData) {

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_img_options_menu)
        {

            final String loggedinUser = SessionHelper.getInstance(mContext).getUser().getRoleId();

            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            builder.addOption(BottomSheetOption.OPTION_EDIT,"Edit");
            builder.addOption(BottomSheetOption.OPTION_DELETE,"Delete");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
            {
                @Override
                public void onOptionSelected(int option)
                {
                    switch (option )
                    {
                        case BottomSheetOption.OPTION_EDIT :
                            Intent intent = new Intent(mContext, ActivityCoachFeedbackCoachView.class);
                            intent.putExtra(ActivityCoachFeedbackCoachView.KEY_ATHLETE_ID, athlete_id);
                            intent.putExtra(ActivityCoachFeedbackCoachView.KEY_REVIEW_ID, review_id);
                            intent.putExtra(ActivityCoachFeedbackCoachView.KEY_MODE_CREATE_OR_EDIT, ActivityCoachFeedbackCoachView.VALUE_MODE_EDIT);
                            startActivity(intent);
                            break;



                            case BottomSheetOption.OPTION_DELETE:
                                DialogHelper.showSimpleCustomDialog(mContext, "Are you sure want to delete this ?",new DialogHelper.ShowSimpleDialogListener() {
                                    @Override
                                    public void onOkClicked() {
                                        mPresenter.deleteCoachFeedback(review_id,loggedinUser);
                                        Intent i = new Intent(mContext,ActivityCoachFeedback.class);
                                        i.putExtra(ActivityCoachFeedback.KEY_IS_FRAGMENT_SINGLE, true);
                                        i.putExtra("athlete_id", athlete_id);
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


    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails(CoachData coachData)
    {
        if (coachData == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(coachData.getAthleteName());
        createdDataDetails.setRoleId(coachData.getAthleteID());
        createdDataDetails.setRole("Coach");
        createdDataDetails.setUserPicUrl(coachData.getAthleteImg());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(coachData.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(null);
        createdDataDetails.setCreated_by(coachData.getCoachName());


        return createdDataDetails;
    }

}
