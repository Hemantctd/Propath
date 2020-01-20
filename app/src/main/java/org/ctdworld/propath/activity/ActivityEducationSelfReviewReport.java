package org.ctdworld.propath.activity;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSelfReviewAdapterReport;
import org.ctdworld.propath.contract.ContractSelfReview;
import org.ctdworld.propath.contract.ContractSelfReviewReport;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.model.SelfReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterSelfReview;
import org.ctdworld.propath.presenter.PresenterSelfReviewReport;

import java.util.ArrayList;
import java.util.List;

public class ActivityEducationSelfReviewReport extends AppCompatActivity implements ContractSelfReviewReport.View, View.OnClickListener, ContractSelfReview.View {
    private static final String TAG = ActivityEducationSelfReviewReport.class.getSimpleName();
    public static final String REVIEW_ID = "review_id";
    public static final String ATHLETE_ID = "athlete_id";

    RecyclerView recyclerSelfReviewReport;
    Context mContext;
    Toolbar mToolbar;
    TextView mTxtToolbarTitle;
    //TextView self_review_txt_user_name;
   // TextView self_Review_date;
    TextView qualification_text;
    List<SelfReview> selfReviewList = new ArrayList<>();
    ContractSelfReviewReport.Presenter mPresenter;
    ContractSelfReview.Presenter mPresenterReview;
    String review_id,athlete_id;
    AdapterSelfReviewAdapterReport adapter;
  //  ImageView self_review_img_profile;
    ImageView mImgToolbarOptionsMenu;
    ProgressBar mProgressBar;
  //  TextView mRole;
    TextView mStrengthsText;
    TextView mAssitanceText;
    TextView mSuggestionsText;
    TextView mImprovementsText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_self_review_report);
        init();
        setSelfReviewAdapterReport();
        prepareToolbar();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null )
        {
            review_id = bundle.getString(REVIEW_ID);
            athlete_id= bundle.getString(ATHLETE_ID);
        }

        Log.d(TAG,"review_id" +review_id);
        Log.d(TAG,"athlete_id" +athlete_id);
        String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String athleteRoleId= RoleHelper.ATHLETE_ROLE_ID;

        if (loggedInUser.equals(athleteRoleId)) {
            athlete_id = SessionHelper.getInstance(mContext).getUser().getUserId();
            mPresenter.getSelfReview(athlete_id,review_id);
        }
        else
        {
            mPresenter.getSelfReview(athlete_id,review_id);
        }


    }

    public void init()
    {
        mContext = this;
        mPresenter = new PresenterSelfReviewReport(mContext,this);
        mPresenterReview = new PresenterSelfReview(mContext,this);
        //self_Review_date = findViewById(R.id.self_Review_date);
       // self_review_img_profile = findViewById(R.id.self_review_img_profile);
        recyclerSelfReviewReport = findViewById(R.id.recycler_self_review_report);
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        //self_review_txt_user_name = findViewById(R.id.self_review_txt_user_name);
        //comments_given_by_self= findViewById(R.id.comments_given_by_self);
        qualification_text = findViewById(R.id.qualification_text);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
      //  mRole = findViewById(R.id.role);

        mStrengthsText =  findViewById(R.id.strength_text);
        mAssitanceText = findViewById(R.id.assistance_text);
        mImprovementsText = findViewById(R.id.improvements_text);
        mSuggestionsText = findViewById(R.id.suggestion_text);


        //String role = "Role" + " : "+ "Athlete";
       // mRole.setText(role);

        mImgToolbarOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID)) {
            mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
            mImgToolbarOptionsMenu.setOnClickListener(this);
        }

    }

    public void setSelfReviewAdapterReport()
    {
       adapter = new AdapterSelfReviewAdapterReport(mContext,selfReviewList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerSelfReviewReport.setLayoutManager(layoutManager);
        recyclerSelfReviewReport.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }




    private void prepareToolbar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.self_review_report);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }



    @Override
    public void getOnReceivedSelfReview(SelfReview selfReview, List<SelfReview> selfReviewListReport) {

        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails(selfReview));
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();

      /*  int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);
        int picHeight = UtilHelper.convertDpToPixel(mContext,picDimen);
*/
        /*Glide.with(mContext)
                .load(selfReview.getUserPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth,picHeight))
                .into(self_review_img_profile);*/

       // self_review_txt_user_name.setText(selfReview.getAthlete_name());
      //  comments_given_by_self.setText(selfReview.getComments());
        mAssitanceText.setText(selfReview.getAssistance());
        mSuggestionsText.setText(selfReview.getSuggestions());
        mStrengthsText.setText(selfReview.getStrengths());
        mImprovementsText.setText(selfReview.getImprovements());
      //  self_Review_date.setText(DateTimeHelper.getDateTime(selfReview.getDate(),DateTimeHelper.FORMAT_DATE_TIME));
        qualification_text.setText(selfReview.getQualification());

        selfReviewList = selfReviewListReport;

        adapter.updateList(selfReviewList);

    }

    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails(SelfReview selfReview)
    {
        if (selfReview == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(selfReview.getAthleteName());
        createdDataDetails.setRoleId(selfReview.getAthleteID());
        createdDataDetails.setRole("Athlete");
        createdDataDetails.setUserPicUrl(selfReview.getUserPicUrl());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(selfReview.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(null);

        if (!SessionHelper.getInstance(mContext).getUser().getUserId().equals(athlete_id)) {
            createdDataDetails.setCreated_by(selfReview.getAthleteName());
        }

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
                    switch (option) {
                        case BottomSheetOption.OPTION_DELETE:
                            DialogHelper.showSimpleCustomDialog(mContext, "Are you sure want to delete this ?",
                                    new DialogHelper.ShowSimpleDialogListener() {
                                        @Override
                                        public void onOkClicked() {
                                            mPresenterReview.deleteReview(review_id);
                                            startActivity(new Intent(mContext,ActivityEducationForAthlete.class));
                                            finish();
                                        }
                                    });
                            break;

                        case BottomSheetOption.OPTION_EDIT:
                            Intent intent = new Intent(mContext, ActivityEducationSelfReviewCreate.class);
                            intent.putExtra(ActivityEducationSelfReviewCreate.KEY_REVIEW_ID, review_id);
                            intent.putExtra(ActivityEducationSelfReviewCreate.KEY_MODE_CREATE_OR_EDIT, ActivityEducationSelfReviewCreate.VALUE_MODE_EDIT );
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
    public void onSelfListDeleted(String id) {

    }

    @Override
    public void onReceivedSelfReportList(List<SelfReview> selfReviewList) {

    }
}
