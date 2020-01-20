package org.ctdworld.propath.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterCareerUserPlanList;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractCareerPlan;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.presenter.PresenterCareerPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


//  contains list of all career plans of a single user, it's passed from other component
public class ActivityCareerUserPlanList extends BaseActivity implements ContractCareerPlan.View, AdapterCareerUserPlanList.Listener{


    // # Views
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private RecyclerView mRecycler;



    // # other variables
    Context mContext;
    private AdapterCareerUserPlanList mAdapter;
    private ContractCareerPlan.Presenter mPresenter;
    private CareerPlan.CareerUser mCareerUser;  // contains user details who has created career plan and also contains list of career plan objects


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_user_plan_list);

        init();   // initializing varables
        setUpToolbar();  // setting up toolbar
        setUpAdapter();  // setting adapter


    }




    // # initializing variables
    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mPresenter = new PresenterCareerPlan(mContext, this);
        mRecycler = findViewById(R.id.athlete_name_recycler_view);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mCareerUser   = (CareerPlan.CareerUser) bundle.getSerializable(ConstHelper.Key.CAREER_USER); // getting user sent from calling component
    }




    // # setting up toolbar
    private void setUpToolbar()
    {
        setSupportActionBar(mToolbar);

        mToolbarTitle.setText(getString(R.string.career_plan_list));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null )
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }




    // setting up adapter
    private void setUpAdapter()
    {
        mAdapter = new AdapterCareerUserPlanList(mContext, mCareerUser, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }







  /*  // to filter list
    private void filterListByName(String text)
    {
        ArrayList<GetAthletes> filteredList = new ArrayList<>();

        for (CareerPlan.CareerData careerData : mCareerUser.getCareerDataList())
        {
            String userName = user.getName();
            if (userName != null)
            {
                if (text != null && !text.isEmpty() && userName.toLowerCase().contains(text.toLowerCase()))
                {
                    filterdUser.add(user);
                }
            }

        }
        mAdapter.filterList(filterdUser);
    }*/


    /*private void voiceToText()
    {
        Log.i(TAG,"voiceToText() method called ");
        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
        else
        {
            FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
            fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                @Override
                public void onReceiveText(String spokenText)
                {
                    mEditSearchAthleteNames.setText(spokenText);
                    mEditSearchAthleteNames.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearchAthleteNames.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(),"");
        }


    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ConstHelper.RequestCode.ACTIVITY_CAREER_PLAN_VIEW && data != null && data.getExtras() != null)
        {
            CareerPlan.CareerUser careerUser = (CareerPlan.CareerUser) data.getExtras().getSerializable(ConstHelper.Key.CAREER_USER);
            boolean isDeleted = data.getBooleanExtra(ConstHelper.Key.IS_DELETED, false);

            if (mAdapter != null && careerUser != null)
            {
                if (isDeleted)  // if data was deleted then removing that data from list
                {
                    mAdapter.onDataDeleted(careerUser);
                }
                else // if not deleted then may be data was updated so updating data in list each time we come back from view page
                {
                    mAdapter.onDataUpdated(careerUser.getCareerData());
                }
                mCareerUser = mAdapter.getCareerUser(); // getting latest updated career user
            }
        }
    }


    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.CAREER_USER, mCareerUser);
       // intent.putExtra(ConstHelper.Key.IS_DELETED, true);
        setResult(RESULT_OK, intent);
        finish();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }




    // # called when options menu is clicked in adapter
    @Override
    public void onAdapterOptionsMenuClicked(final CareerPlan.CareerUser careerUser) {
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
                        // # for now deleting only one CareerData though we can delete multiple
                        // creating new list of CareerData to put only which is to be deleted
                        List<CareerPlan.CareerData> careerDataList = new ArrayList<>();
                        careerDataList.add(careerUser.getCareerData());
                        careerUser.setCareerDataList(careerDataList);

                        mPresenter.deleteCareerData(careerUser);
                        break;

                    case BottomSheetOption.OPTION_EDIT:
                        Intent intent = new Intent(mContext, ActivityCareerCreateUpdate.class);
                        intent.putExtra(ConstHelper.Key.ACTION_CREATE_UPDATE, ConstHelper.Action.EDIT_OR_UPDATE);
                        intent.putExtra(ConstHelper.Key.CAREER_USER, careerUser);
                        startActivityForResult(intent, ConstHelper.RequestCode.EDIT);
                        break;
                }
            }
        });

        options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
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
    public void onCareerDataDeleted(CareerPlan.CareerUser careerUser) {
        if (mAdapter != null)
        {
            mAdapter.onDataDeleted(careerUser);
            mCareerUser = mAdapter.getCareerUser(); // getting latest updated career user
        }

    }

    @Override
    public void onFailed(String message) {

    }
}
