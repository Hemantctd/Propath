package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterChooseUserByRole;
import org.ctdworld.propath.contract.ContractGetRegisteredUsers;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterGetRegisteredUsers;

import java.util.ArrayList;
import java.util.List;

public class ActivityChooseUserByRole extends AppCompatActivity implements ContractGetRegisteredUsers.View
{
    private final String TAG = ActivityChooseUserByRole.class.getSimpleName();

    Context mContext;

    // keys to set data in bundle in calling activity
    public static final String KEY_TOOLBAR_TITLE = "toolbar title";
    // role id to filter list, list will be shown for particular role only
    public static final String KEY_ROLE_ID_TO_FILTER = "role id";

    // KEY TO SET RESULT TO BUNDLE TO SEND BACK TO CALLING COMPONENT
    public static final String KEY_USER_RESULT = "user result";



    // data sent from calling activity
    private String mStrToolbarTile = "";  // toolbar title sent from calling component
    private String mStrRoleId = "";      // role id to filter list, list will be shown for particular role only



    RecyclerView mRecycler;
    EditText mEditSearchContacts;
    ImageView mImgSearch;
    // ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshLayout;
    TextView mTxtNoConnection;
    View mLayoutForDetails;
    LinearLayoutManager mLayoutManager;
    AdapterChooseUserByRole mAdapter;


    Toolbar mToolbar;
    TextView mTxtToolbarTitle;

    ContractGetRegisteredUsers.Presenter mPresenter;
    List<User> mFilteredUserList = new ArrayList<>();
    PermissionHelper mPermissionHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_by_role);
        init();
        initializeFromBundle();
        setToolbar();
        setUsersAdapter();
        setListeners();


        mRefreshLayout.setRefreshing(true);
        requestAllUsers();  // getting all users

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mEditSearchContacts.setText("");
                requestAllUsers();
            }
        });


    }
    private void init() {
        mContext = this;
        mPresenter = new PresenterGetRegisteredUsers(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mPermissionHelper = new PermissionHelper(this);

        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);

        mImgSearch = findViewById(R.id.activity_choose_user_by_role_img_search_icon);
        mRecycler = findViewById(R.id.activity_choose_user_by_role_recycler_view);
        mEditSearchContacts = findViewById(R.id.activity_choose_user_by_role_edit_search);
        mLayoutForDetails = findViewById(R.id.activity_choose_user_by_role_layout_details);
        //mProgressBar = findViewById(R.id.contact_search_progressbar);
        mTxtNoConnection = findViewById(R.id.activity_choose_user_by_role_txt_no_connection);
        mRefreshLayout = findViewById(R.id.activity_choose_user_by_role_refresh_layout);

    }


    private void initializeFromBundle()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mStrRoleId = bundle.getString(KEY_ROLE_ID_TO_FILTER);
            mStrToolbarTile = bundle.getString(KEY_TOOLBAR_TITLE);
        }
        else
            Log.e(TAG,"bundle is null initializeFromBundle() method");
    }


    // when user is selected in adapter then the selected user is passed to this method, and this method returns user to calling component by setting user in setResult()method
    public void onUserSelected(User user)
    {
        Intent data = new Intent();
        data.putExtra(KEY_USER_RESULT, user); // setting user

        // setting result to send back to calling activity
        setResult(RESULT_OK,data);
        finish();
    }


    // requesting to get all registered users from server
    private void requestAllUsers()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                mPresenter.requestAllUsers();
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error while requesting for users , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            mLayoutForDetails.setVisibility(View.GONE);
            mTxtNoConnection.setVisibility(View.VISIBLE);
            mRefreshLayout.setRefreshing(false);
        }
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(mStrToolbarTile);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    private void setUsersAdapter() {
        mAdapter = new AdapterChooseUserByRole(mContext , new ArrayList<User>());
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);

    }

    private  void setListeners()
    {
        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    voiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
            }
        });

        // to filter list by name
        mEditSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterListByName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input

            }
        });
    }

    // filtering all registered user with by input name EditText
    private void filterListByName(String text)
    {
        //new array list that will hold the filtered data
        ArrayList<User> filterdUser = new ArrayList<>();

        //looping through existing elements
        for (User user : mFilteredUserList)
        {
            //if the existing elements contains the search input
            String userName = user.getName();
            if (userName != null)
            {
                if (userName.toLowerCase().contains(text.toLowerCase()))
                {
                    //adding the element to filtered list
                    filterdUser.add(user);
                }
            }

        }
        mAdapter.filterList(filterdUser);
    }



    // filtering all registered users by role
    private void filterListByRoleId(List<User> userList, String filterRoleId)
    {
        Log.i(TAG,"role id to filter list = "+filterRoleId);
        //new array list that will hold the filtered data
     //   ArrayList<User> filterdUser = new ArrayList<>();

        //looping through existing role IDs
        for (User user : userList)
        {
            //if the existing elements contains the search input
            String roleId = user.getRoleId();
            if (roleId != null)
            {
                if (roleId.toLowerCase().contains(filterRoleId.toLowerCase()))
                {
                    //adding the element to filtered list
                    mFilteredUserList.add(user);
                }
            }

        }
        mAdapter.filterList(mFilteredUserList);
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // showing all roles in option menu to filter list
        //  String[] roles = getResources().getStringArray(R.array.entries_role);
        //   String[] rolesValue = getResources().getStringArray(R.array.entries_role_value);

        List<String> listRoleNames = RoleHelper.getInstance(mContext).getRoleNameList();
        List<Integer> listRoleIds = RoleHelper.getInstance(mContext).getRoleIdList();

        if (listRoleNames != null && listRoleIds != null)
        {
            if ( listRoleIds.size() == listRoleNames.size())
            {
                for (int i=0 ; i<listRoleNames.size() ; i++)
                {
                    // adding role id and role name
                    //   if (i==0)
                    menu.add(Menu.NONE, listRoleIds.get(i), Menu.NONE, listRoleNames.get(i));   // adding zero because at 0 index there it text role but we need int
                  *//*  else
                        menu.add(Menu.NONE, listRoleIds.get(i), Menu.NONE, listRoleNames.get(i));*//*
                }
            }
            else
                Log.e(TAG,"listRoleName's size and listRoleIds' size are not equal, it means role is not correct ");

        }
        else
            Log.e(TAG," getRoleNameList() or getRoleIdList() method is returning null");

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // to go back on click on back button on toolbar
        if (item.getItemId() == android.R.id.home)
            onBackPressed();


       /* // listRoleIds contains role IDs
        List<Integer> listRoleIds = RoleHelper.getInstance(mContext).getRoleIdList();
        for (int i=0 ; i<listRoleIds.size() ; i++)
        {
            if (listRoleIds.get(i) == item.getItemId())   // checking if item id is roleId or not
                filterListByRoleId(String.valueOf(item.getItemId()));  // here getItemId() returns role id is user role id

        }*/


        return true;
    }

    // for converting voice to text, and putting spoken text in search EditText
    private void voiceToText()
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
                    mEditSearchContacts.setText(spokenText);
                    mEditSearchContacts.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearchContacts.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(),"");
        }

    }



    @Override
    public void onGetRegisteredUsers(List<User> userList)
    {
       // Log.i(TAG,"onGetRegisteredUsers() method called , userLise size = "+userList.size());
       // mFilteredUserList = userList;
      //  mAdapter.addUserList(userList);
      //  mAdapter.notifyDataSetChanged();

        // filtering users  and notifying adapter
        mFilteredUserList.clear();   // clearing already filtered data, to make sure data is not duplicate
        filterListByRoleId(userList, mStrRoleId);  // filtering list by role, list will contain only particular role

    }

    @Override
    public void onFailed() {
        DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
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
    public void onSuccess() {

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
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }



}
