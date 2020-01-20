package org.ctdworld.propath.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;


import androidx.appcompat.app.ActionBar;


import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractCareerPlan;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterCareerPlan;
import java.util.List;

public class ActivityCareerPlanView extends BaseActivity implements ContractCareerPlan.View
{

    // # Views
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private ImageView mImgOptionsMenu;
    private TextView mTxtFuture;
    private TextView mTxtLocationOrganization;
    private TextView mTxtScholarShip;
    private TextView mTxtInternship;
    private TextView mTxtApprenticeShip;
    private TextView mTxtSubjectsNeeded;
    private TextView mTxtEmail;
    private TextView mTxtPhoneNumber;


    // # Other Variables
    private Context mContext;
    private ContractCareerPlan.Presenter mPresenter;
    // this is being used in whole page so it's modified each time if any changes are made to data like delete, edit
    private CareerPlan.CareerUser mCareerUser = null;



    public ActivityCareerPlanView() {}    // Required empty public constructor



    // getting User sent from calling component
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_career_plan_view);
        init();  // initializing variables
        setUpToolbar();  // setting up toolbar, title
        setViews();  // setting data to views
        showCreatedDataDetailsFragment();  // showing creator details and created or updated details

        // # setting listener
        mImgOptionsMenu.setOnClickListener(onImgOptionsMenuClicked);
    }





    // # initializing variables
    private void init() {

        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        mPresenter = new PresenterCareerPlan(mContext, this);
        mTxtFuture = findViewById(R.id.career_plan_txt_future_career);
        mTxtLocationOrganization = findViewById(R.id.career_plan_txt_where_you_can_study);
        mTxtScholarShip = findViewById(R.id.career_plan_txt_scholarship);
        mTxtInternship = findViewById(R.id.career_plan_txt_internship);
        mTxtApprenticeShip = findViewById(R.id.career_plan_txt_apprenticeship);
        mTxtSubjectsNeeded = findViewById(R.id.career_plan_txt_subject_needed);
        mTxtEmail = findViewById(R.id.career_plan_txt_email);
        mTxtPhoneNumber = findViewById(R.id.career_plan_txt_phone_number);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mCareerUser   = (CareerPlan.CareerUser) bundle.getSerializable(ConstHelper.Key.CAREER_USER); // getting user sent from calling component

    }



    // # setting up toolbar
    private void setUpToolbar()
    {
        setSupportActionBar(mToolbar);

        if (mCareerUser.getUserId().equals(SessionHelper.getUserId(mContext)))
            mImgOptionsMenu.setVisibility(View.VISIBLE);
        else
            mImgOptionsMenu.setVisibility(View.GONE);

        mToolbarTitle.setText(getString(R.string.career_plan_view));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null )
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }



    // showing creator details and created or updated details
    private void showCreatedDataDetailsFragment() {
        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }



    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails()
    {
        if (mCareerUser == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(mCareerUser.getUserName());
        createdDataDetails.setRoleId(mCareerUser.getUserRoleId());
        createdDataDetails.setUserPicUrl(mCareerUser.getUserPicUrl());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(mCareerUser.getCareerData().getCreatedDate(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(null);

        return createdDataDetails;
    }


    private void setViews()
    {
        if (mCareerUser == null || mCareerUser.getCareerData() == null)
            return;

        CareerPlan.CareerData careerData = mCareerUser.getCareerData();

       
        String future = careerData.getFutureCareer();
        String locationOrganization =  careerData.getLocationOrganization();
        String scholarship = careerData.getScholarShip();
        String internship = careerData.getInternShip();
        String apprenticeship = careerData.getApprenticeship();
        String subjectsNeeded = careerData.getSubjectsNeeded();
        String email = careerData.getEmail();
        String phoneNumber = careerData.getPhoneNumber();


        if (!future.isEmpty())
            mTxtFuture.setText(future);
        else
            mTxtFuture.setText(getString(R.string.not_found));


        if (!locationOrganization.isEmpty())
            mTxtLocationOrganization.setText(locationOrganization);
        else
            mTxtLocationOrganization.setText(getText(R.string.not_found));

        if (!scholarship.isEmpty())
            mTxtScholarShip.setText(scholarship);
        else
            mTxtScholarShip.setText(getText(R.string.not_found));


        if (!internship.isEmpty())
            mTxtInternship.setText(internship);
        else
            mTxtInternship.setText(getText(R.string.not_found));


        if (!apprenticeship.isEmpty())
            mTxtApprenticeShip.setText(apprenticeship);
        else
            mTxtApprenticeShip.setText(getText(R.string.not_found));


        if (!subjectsNeeded.isEmpty())
            mTxtSubjectsNeeded.setText(subjectsNeeded);
        else
            mTxtSubjectsNeeded.setText(getText(R.string.not_found));


        if (!email.isEmpty())
            mTxtEmail.setText(email);
        else
            mTxtEmail.setText(getString(R.string.email_not_found));

        if (!phoneNumber.isEmpty())
            mTxtPhoneNumber.setText(phoneNumber);
        else
            mTxtPhoneNumber.setText(getString(R.string.phone_number_not_found));



    }



    View.OnClickListener onImgOptionsMenuClicked = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_DELETE, "Delete")
                    .addOption(BottomSheetOption.OPTION_EDIT, "Edit");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option)
                {
                    switch (option)
                    {
                        case BottomSheetOption.OPTION_DELETE:
                            mPresenter.deleteCareerData(mCareerUser);
                            break;

                        case BottomSheetOption.OPTION_EDIT:
                            Intent intent = new Intent(mContext, ActivityCareerCreateUpdate.class);
                            intent.putExtra(ConstHelper.Key.ACTION_CREATE_UPDATE, ConstHelper.Action.EDIT_OR_UPDATE);
                            intent.putExtra(ConstHelper.Key.CAREER_USER, mCareerUser);
                            startActivityForResult(intent, ConstHelper.RequestCode.EDIT);
                            break;
                    }
                }
            });

            options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ConstHelper.RequestCode.EDIT && data != null && data.getExtras() != null)
        {
            mCareerUser = (CareerPlan.CareerUser) data.getExtras().getSerializable(ConstHelper.Key.CAREER_USER);
            setViews();
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
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.CAREER_USER, mCareerUser);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSavedCareerPlan(CareerPlan.CareerUser careerUser) {

    }

    @Override
    public void onReceivedCareerUsers(List<CareerPlan.CareerUser> careerUserList) {

    }

    @Override
    public void onCareerPlanUpdated(CareerPlan.CareerUser careerUser) {

    }

    @Override
    public void onCareerDataDeleted(CareerPlan.CareerUser careerUser)
    {
        if (mCareerUser != null && mCareerUser.getCareerDataList() != null)
        {
            mCareerUser.getCareerDataList().remove(mCareerUser.getCareerData());

            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.CAREER_USER, mCareerUser);
            intent.putExtra(ConstHelper.Key.IS_DELETED, true);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onFailed(String message) {

    }
}
