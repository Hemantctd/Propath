package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyGroup;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.contract.ContractGetAllFriendsAndGroups;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.GetGroupNames;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.presenter.PresenterCreateSurvey;
import org.ctdworld.propath.presenter.PresenterGetAllFriendsAndGroups;

import java.util.ArrayList;
import java.util.List;

public abstract class ActivitySurveyGroup extends AppCompatActivity implements View.OnClickListener, ContractGetAllFriendsAndGroups.View, ContractSurvey.View {
    private final String TAG = ActivitySurveyGroup.class.getSimpleName();

    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView surveyGroupRecyclerView;
    Button surveyDone;
    EditText mEditSearchAthleteNames;
    AdapterSurveyGroup adapterSurveyGroup;
    Context mContext;
    LinearLayoutManager mLayoutManager;
   ImageView mImgSearch;
    SwipeRefreshLayout mRefreshLayout;

    ArrayList<GetGroupNames> mFilteredFriendsAndGroupsList = new ArrayList<>();
    ContractSurvey.Presenter mPresenterShareSurvey;

    ContractGetAllFriendsAndGroups.Presenter mPresenter;
    List<GetGroupNames> mFriendsAndGroupsNameList = new ArrayList<>();
    PermissionHelper mPermissionHelper;
    String mSurveyId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_group);

        Intent i = getIntent();
       mSurveyId= i.getStringExtra("survey_id");


        init();
        setToolBar();
        setListeners();

        prepareRecyclerView();

        mRefreshLayout.setRefreshing(true);
        requestAllFriendsAndGroups();  // getting all users

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mEditSearchAthleteNames.setText("");
                requestAllFriendsAndGroups();
            }
        });

    }

    // for initialization
    private void init()
    {
        mContext = this;
        mPresenter = new PresenterGetAllFriendsAndGroups(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mPresenterShareSurvey = new PresenterCreateSurvey(mContext,this);
        mPermissionHelper = new PermissionHelper(mContext);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        surveyGroupRecyclerView = findViewById(R.id.surveyGroupRecyclerView);
        mRefreshLayout = findViewById(R.id.contact_search_refresh_layout);
        surveyDone = findViewById(R.id.surveyDone);
        mEditSearchAthleteNames = findViewById(R.id.contact_search_edit_search);
        mImgSearch = findViewById(R.id.contact_search_img_search);
        surveyDone.setOnClickListener(this);
    }



    private void requestAllFriendsAndGroups()
    {
        if (UtilHelper.isConnectedToInternet(mContext)) {
            try {
                mPresenter.requestAllFriendsAndGroups(mSurveyId);
            } catch (Exception e) {
                Log.e(TAG, "Error while requesting for friends and groups , " + e.getMessage());
                e.printStackTrace();
            }
        } else {
//            mLayoutForDetails.setVisibility(View.GONE);
 //           mTxtNoConnection.setVisibility(View.VISIBLE);
            mRefreshLayout.setRefreshing(false);
        }
    }


    // set adapter
    private void prepareRecyclerView()
    {
        adapterSurveyGroup = new AdapterSurveyGroup(mContext, mFriendsAndGroupsNameList);
        mLayoutManager = new LinearLayoutManager(mContext);
        surveyGroupRecyclerView.setLayoutManager(mLayoutManager);
        surveyGroupRecyclerView.setAdapter(adapterSurveyGroup);
     }


     // button clickable
    private void setListeners() {
        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    voiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, "Permission required voice feature");
            }
        });

        // to filter list
        mEditSearchAthleteNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterListByName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    // filtering all registered user with by input name
    private void filterListByName(String text)
    {
        //new array list that will hold the filtered data
        ArrayList<GetGroupNames> filterdUser = new ArrayList<>();

        //looping through existing elements
        for (GetGroupNames user : mFriendsAndGroupsNameList)
        {
            //if the existing elements contains the search input
            String userName = user.getName();
            if (userName != null)
            {
                if (text != null && !text.isEmpty() && userName.toLowerCase().contains(text.toLowerCase()))
                {

                    //adding the element to filtered list
                    filterdUser.add(user);
                }
            }

        }
        adapterSurveyGroup.filterList(filterdUser);
    }

// for converting voice to text
    private void voiceToText() {
        Log.i(TAG, "voiceToText() method called ");
        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
        else
        {
            FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
            fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                @Override
                public void onReceiveText(String spokenText) {
                    mEditSearchAthleteNames.setText(spokenText);
                    mEditSearchAthleteNames.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearchAthleteNames.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(), "");
        }

    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.surveyDone) {
            mRefreshLayout.setEnabled(true);
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder userIdBuilder = new StringBuilder();

            for (GetGroupNames groupName : mFriendsAndGroupsNameList) {
                if (groupName.isSelected()) {
                    if (stringBuilder.length() > 0)
                        stringBuilder.append(", ");
                    stringBuilder.append(groupName.getGroup_id());


                    if (userIdBuilder.length() > 0)
                        userIdBuilder.append(", ");
                    userIdBuilder.append(groupName.getUser_id());

                }
            }

            GetGroupNames groupNames = new GetGroupNames();
            groupNames.setSurvey_id(mSurveyId);
            groupNames.setUser_id(userIdBuilder.toString());
            groupNames.setGroup_id(stringBuilder.toString());

//                  Log.d(TAG,"GROUP_ID"+ stringBuilder.toString());
//                  Log.d(TAG,"USER_ID"+ userIdBuilder.toString());

            mPresenterShareSurvey.shareSurvey(groupNames);
        }
//
//                List<GetGroupNames> list = mFriendsAndGroupsNameList;
//
//
//                for (int j = 0; j < list.size(); j++) {
//                    if (j == list.size() - 1) {
//                        GetGroupNames groupNames = new GetGroupNames();
//                        groupNames = list.get(j);
//                        if (groupNames.isSelected()) {
//                            stringBuffer.append(groupNames.getGroup_id());
//                        }
//                    } else {
//                        GetGroupNames groupNames = new GetGroupNames();
//                        groupNames = list.get(j);
//                        if (groupNames.isSelected()) {
//                            stringBuffer.append(groupNames.getGroup_id());
//                            stringBuffer.append(",");
//                        }
//                    }



        }



    // set tool bar
    private void setToolBar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey_contacts);
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


    // getting all the friends and groups
    @Override
    public void onGetAllFriendsAndGroups(List<GetGroupNames> friendsAndGroupList)
    {
        mFriendsAndGroupsNameList = friendsAndGroupList;
        adapterSurveyGroup.addFriendsAndGroupList(friendsAndGroupList);
      //  adapterSurveyGroup.notifyDataSetChanged();  // user wil appear only when letter is typed
        mFilteredFriendsAndGroupsList.clear();
    }

    @Override
    public void onFailed() {
        DialogHelper.showSimpleCustomDialog(mContext, "Failed...");
    }

    @Override
    public void onShowProgress() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mRefreshLayout.setEnabled(false);
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mRefreshLayout.setEnabled(true);
    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext, message);
    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg);

    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg);

    }

    @Override
    public void onReceivedSurvey(Survey.SurveyData surveyData) {

    }
}

