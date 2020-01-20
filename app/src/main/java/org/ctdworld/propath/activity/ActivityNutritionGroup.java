package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
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
import org.ctdworld.propath.adapter.AdapterGroups;
import org.ctdworld.propath.contract.ContractGetAllGroups;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.GetGroupNames;
import org.ctdworld.propath.model.Nutrition;
import org.ctdworld.propath.model.NutritionFeed;
import org.ctdworld.propath.presenter.PresenterGetAllGroups;

import java.util.ArrayList;
import java.util.List;

public class ActivityNutritionGroup extends AppCompatActivity implements View.OnClickListener, ContractGetAllGroups.View {
    private final String TAG = ActivityNutritionGroup.class.getSimpleName();

    Toolbar mToolbar;
    TextView mToolbarTitle;
    Button groupDone;
    EditText mEditSearchNames;
    AdapterGroups adapterGroup;
    Context mContext;
    LinearLayoutManager mLayoutManager;
    ImageView mImgSearch;
    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mNutritionRecyclerView;
    ArrayList<GetGroupNames> mFilteredGroupsList = new ArrayList<>();
    ContractGetAllGroups.Presenter mPresenterGroups;

    List<GetGroupNames> mgroupsNameList = new ArrayList<>();
    PermissionHelper mPermissionHelper;
    String flagValue;
    //  Nutrition nutrition;
    NutritionFeed.PostData mPostData;
    String groupID, groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_group);

        init();
        setToolBar();
        setListeners();

        prepareRecyclerView();

        mRefreshLayout.setRefreshing(true);
        requestAllGroups();  // getting all users

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mEditSearchNames.setText("");
                requestAllGroups();
            }
        });

    }

    // for initialization
    private void init() {
        mContext = this;
        mPresenterGroups = new PresenterGetAllGroups(mContext, this);
        mPermissionHelper = new PermissionHelper(mContext);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRefreshLayout = findViewById(R.id.contact_search_refresh_layout);
        mEditSearchNames = findViewById(R.id.contact_search_edit_search);
        mImgSearch = findViewById(R.id.contact_search_img_search);
        mNutritionRecyclerView = findViewById(R.id.nutritionRecyclerView);
        groupDone = findViewById(R.id.groupDone);
        groupDone.setOnClickListener(this);


//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
////            flagValue = bundle.getString("flag");
//            mPostData = (NutritionFeed.PostData) bundle.getSerializable(ActivityNutritionFeedPost.KEY_POST_DATA);
//        }

    }


    private void requestAllGroups() {
        if (UtilHelper.isConnectedToInternet(mContext)) {
            try {
                mPresenterGroups.requestAllGroups();
            } catch (Exception e) {
                Log.e(TAG, "Error while requesting for groups , " + e.getMessage());
                e.printStackTrace();
            }
        } else {
//            mLayoutForDetails.setVisibility(View.GONE);
            //           mTxtNoConnection.setVisibility(View.VISIBLE);
            mRefreshLayout.setRefreshing(false);
        }
    }


    // set adapter
    private void prepareRecyclerView() {
        adapterGroup = new AdapterGroups(mContext, mgroupsNameList);
        mLayoutManager = new LinearLayoutManager(mContext);
        mNutritionRecyclerView.setLayoutManager(mLayoutManager);
        mNutritionRecyclerView.setAdapter(adapterGroup);
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
        mEditSearchNames.addTextChangedListener(new TextWatcher() {
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
    private void filterListByName(String text) {
        //new array list that will hold the filtered data
        ArrayList<GetGroupNames> groupList = new ArrayList<>();

        //looping through existing elements
        for (GetGroupNames user : mgroupsNameList) {
            //if the existing elements contains the search input
            String userName = user.getName();
            if (userName != null) {
                if (userName.toLowerCase().contains(text.toLowerCase())) {

                    //adding the element to filtered list
                    groupList.add(user);
                }
            }

        }
        // adapterSurveyGroup.filterList(filterdUser);
    }

    // for converting voice to text
    private void voiceToText() {
        Log.i(TAG, "voiceToText() method called ");
        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
        else {
            FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
            fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                @Override
                public void onReceiveText(String spokenText) {
                    mEditSearchNames.setText(spokenText);
                    mEditSearchNames.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearchNames.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(), "");
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.groupDone) {
            // StringBuilder stringBuilder = new StringBuilder();

            for (GetGroupNames groupNames : mgroupsNameList) {
                if (groupNames.isSelected()) {
                    groupID = groupNames.getGroup_id();
                    groupName = groupNames.getName();
                }
            }

            Intent intent = new Intent();
            NutritionFeed.PostData postData = new NutritionFeed.PostData();
            postData.setGroupID(groupID);
            postData.setGroupName(groupName);
            intent.putExtra(ActivityNutritionFeedPost.KEY_POST_DATA, postData);
            setResult(RESULT_OK, intent);
            finish();

           /* if (flagValue.equals("2")) {
                Intent intent = new Intent(mContext, ActivityNutritionFeedPost.class);
                intent.putExtra(ActivityNutritionFeedPost.KEY_POST_DATA, postData);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(mContext, ActivityNutritionShare.class);
                intent.putExtra(ActivityNutritionFeedPost.KEY_POST_DATA, postData);
                startActivity(intent);
                finish();
            }*/
        }


    }


    // set tool bar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.groups);
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


    @Override
    public void onGetAllGroups(List<GetGroupNames> groupList) {
        mgroupsNameList = groupList;
        adapterGroup.addFriendsAndGroupList(mgroupsNameList);
        adapterGroup.notifyDataSetChanged();
        mFilteredGroupsList.clear();
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

}

