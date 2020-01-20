package org.ctdworld.propath.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractCareerPlan;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterCareerPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


// to create and update career plan
public class ActivityCareerCreateUpdate extends BaseActivity implements ContractCareerPlan.View {


    // # Views
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private EditText mEditFuture;
    private EditText mEditLocationOrganization;
    private EditText mEditScholarShip;
    private EditText mEditInternship;
    private EditText mEditApprenticeShip;
    private EditText mEditSubjectsNeeded;
    private EditText mEditEmail;
    private ImageView mImgOptionsMenu;
    private EditText mEditPhoneNumber;

    //  private Spinner mSpinnerScholarShip;
    //private Button mBtnSubmit;


    // # Other Variables
    private Context mContext;
    private final String TAG = ActivityCareerCreateUpdate.class.getSimpleName();
    private ContractCareerPlan.Presenter mPresenter;
    private int mActionCreateUpdate;
    private CareerPlan.CareerUser mCareerUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_career_plan_create_update);

        init();  // initializing variables
        setUpToolbar();  // setting up toolbar
        setViews();  // setting views data


        // setting listener
        mImgOptionsMenu.setOnClickListener(onOptionMenuClicked);
        //mSpinnerScholarShip.setOnItemSelectedListener(onScholarShipItemSelected);

    }


    // # initializing variables
    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mPresenter = new PresenterCareerPlan(mContext, this);
        mEditFuture = findViewById(R.id.career_plan_edit_future_career);
        mEditLocationOrganization = findViewById(R.id.career_plan_edit_where_you_can_study);
        mEditScholarShip = findViewById(R.id.career_plan_edit_scholarship);
        mEditInternship = findViewById(R.id.career_plan_edit_internship);
        mEditApprenticeShip = findViewById(R.id.career_plan_edit_apprenticeship);
        mEditSubjectsNeeded = findViewById(R.id.career_plan_edit_subject_needed);
        mEditEmail = findViewById(R.id.career_plan_edit_email);
        mEditPhoneNumber = findViewById(R.id.career_plan_edit_phone_number);
        mImgOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        mImgOptionsMenu.setVisibility(View.VISIBLE);


        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mActionCreateUpdate = intent.getIntExtra(ConstHelper.Key.ACTION_CREATE_UPDATE, ConstHelper.Action.CREATE); // default value would be for creating career plan
            mCareerUser = (CareerPlan.CareerUser) intent.getExtras().getSerializable(ConstHelper.Key.CAREER_USER);
        }

        // if null then initializing new object
        if (mCareerUser == null) {
            CareerPlan.CareerData careerData = new CareerPlan.CareerData();
            mCareerUser = new CareerPlan.CareerUser();
            mCareerUser.setCareerData(careerData);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    // # setting up toolbar
    private void setUpToolbar() {
        setSupportActionBar(mToolbar);


        // # setting toolbar title
        if (mActionCreateUpdate == ConstHelper.Action.CREATE)
            mToolbarTitle.setText(getString(R.string.create_career_plan));
        else
            mToolbarTitle.setText(getString(R.string.update_career_plan));


        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    // setting data to views from mCareerUser which may have been assigned data from bundle came from other component.
    private void setViews() {
        if (mCareerUser == null || mCareerUser.getCareerData() == null)
            return;

        CareerPlan.CareerData careerData = mCareerUser.getCareerData();

        mEditFuture.setText(careerData.getFutureCareer());
        mEditLocationOrganization.setText(careerData.getLocationOrganization());
        mEditScholarShip.setText(careerData.getScholarShip());
        mEditInternship.setText(careerData.getInternShip());
        mEditApprenticeShip.setText(careerData.getApprenticeship());
        mEditSubjectsNeeded.setText(careerData.getSubjectsNeeded());
        mEditEmail.setText(careerData.getEmail());
        mEditPhoneNumber.setText(careerData.getPhoneNumber());

      /*   int selectedScholarshipPosition = UtilHelper.getPositionInStringArray(getResources().getStringArray(R.array.entries_scholarship), careerData.getScholarShip());
        if (mSpinnerScholarShip != null && selectedScholarshipPosition != ConstHelper.NOT_FOUND)
            mSpinnerScholarShip.setSelection(selectedScholarshipPosition);*/

    }


    // # checking if any fields is empty or not, if any field is empty then returns false. it also sets data to mCareerUser object
    private boolean validateAndSetCareerUser() {
       /* if (mCareerUser == null || mCareerUser.getCareerData() == null)
            return false;*/

        CareerPlan.CareerData careerData = mCareerUser.getCareerData();

        String future = mEditFuture.getText().toString().trim();
        String locationOrganization = mEditLocationOrganization.getText().toString().trim();
        String scholarship = mEditScholarShip.getText().toString().trim();
        String internship = mEditInternship.getText().toString().trim();
        String apprenticeship = mEditApprenticeShip.getText().toString().trim();
        String subjectsNeeded = mEditSubjectsNeeded.getText().toString().trim();
        String email = mEditEmail.getText().toString().trim();
        String phoneNumber = mEditPhoneNumber.getText().toString().trim();

        if (!future.isEmpty())
            careerData.setFutureCareer(future);
        else
            return false;


        if (!locationOrganization.isEmpty())
            careerData.setLocationOrganization(locationOrganization);
        else
            return false;


        if (!scholarship.isEmpty())
            careerData.setScholarShip(scholarship);
        else
            return false;


        if (!internship.isEmpty())
            careerData.setInternShip(internship);
        else
            return false;


        if (!apprenticeship.isEmpty())
            careerData.setApprenticeship(apprenticeship);
        else
            return false;


        if (!subjectsNeeded.isEmpty())
            careerData.setSubjectsNeeded(subjectsNeeded);
        else
            return false;


        if (!email.isEmpty())
            careerData.setEmail(email);
        else
            return false;


        if (!phoneNumber.isEmpty())
            careerData.setPhoneNumber(phoneNumber);
        else
            return false;


        return true;
    }

    public void CarrierCreateData() {
        Log.i(TAG, "submit button clicked");
        if (UtilHelper.isConnectedToInternet(mContext)) {
            if (validateAndSetCareerUser()) {
                if (mActionCreateUpdate == ConstHelper.Action.EDIT_OR_UPDATE) {
                    showLoader(getString(R.string.message_updating));
                    mPresenter.updateCareerPlan(mCareerUser);
                } else {
                    showLoader(getString(R.string.message_saving));
                    mPresenter.saveCareerPlan(mCareerUser);
                }
            } else
                DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_all_fields_required));
        } else
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_no_connection));

    }


    View.OnClickListener onOptionMenuClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_SAVE, "Save");
            ArrayList<BottomSheetOption> bottomSheetOptions = builder.build();

            Log.i(TAG,"bottom sheet options size = "+bottomSheetOptions.size());

            // List<BottomSheetOption> bottomSheetOptions = builder.build();
            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
            {
                @Override
                public void onOptionSelected(int option)
                {
                    Log.i(TAG,"bottom sheet options selected, option = "+option);
                    switch (option)
                    {
                        case BottomSheetOption.OPTION_SAVE:

                            CarrierCreateData();
                            break;
                    }
                }
            });
            options.show(getSupportFragmentManager(), "options");
           }


    };
    // listener on submit button to create or update career plan
    /*View.OnClickListener onBtnSubmitClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "submit button clicked");
            if (UtilHelper.isConnectedToInternet(mContext)) {
                if (validateAndSetCareerUser()) {
                    if (mActionCreateUpdate == ConstHelper.Action.EDIT_OR_UPDATE) {
                        showLoader(getString(R.string.message_updating));
                        mPresenter.updateCareerPlan(mCareerUser);
                    } else {
                        showLoader(getString(R.string.message_saving));
                        mPresenter.saveCareerPlan(mCareerUser);
                    }
                } else
                    DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_all_fields_required));
            } else
                DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_no_connection));
        }
    };*/





/*    AdapterView.OnItemSelectedListener onScholarShipItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            TextView textView = (TextView) view;
            if (textView == null)
                return;

            textView.setPadding(0,0,0,0);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };*/


    // this method will be called if career plan is updated or created
    @Override
    public void onSavedCareerPlan(CareerPlan.CareerUser careerUser) {
        hideLoader();
        Toast.makeText(mContext, "Career plan created", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.CAREER_USER, careerUser);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onReceivedCareerUsers(List<CareerPlan.CareerUser> careerUserList) {

    }


    @Override
    public void onCareerPlanUpdated(CareerPlan.CareerUser careerUser) {
        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.CAREER_USER, careerUser);   // careerUser contains updated data
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCareerDataDeleted(CareerPlan.CareerUser careerUser) {

    }


    @Override
    public void onFailed(String message) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext, message);
    }
}
