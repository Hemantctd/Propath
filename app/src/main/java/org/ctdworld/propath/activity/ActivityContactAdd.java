package org.ctdworld.propath.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterContactAdd;
import org.ctdworld.propath.contract.ContractGetRegisteredUsers;
import org.ctdworld.propath.fragment.FragmentFooter;
import org.ctdworld.propath.fragment.FragmentSearch;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterGetRegisteredUsers;

import java.util.ArrayList;
import java.util.List;

public abstract class ActivityContactAdd extends AppCompatActivity implements ContractGetRegisteredUsers.View, FragmentSearch.SearchListener
{
    private final String TAG = ActivityContactAdd.class.getSimpleName();

    Context mContext;

    RecyclerView mRecycler;
   // EditText mEditSearchContacts;
   // ImageView mImgSearch;
   // ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshLayout;
    TextView mTxtNoConnection;
    View mLayoutForDetails;
    LinearLayoutManager mLayoutManager;
    AdapterContactAdd mAdapter;


    Toolbar mToolbar;
    TextView mToolbarTitle;

    ContractGetRegisteredUsers.Presenter mPresenter;
    List<User> mUserList = new ArrayList<>();
    PermissionHelper mPermissionHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        init();
        setToolbar();
        setContactsAdapter();
        setListeners();
        getSupportFragmentManager().beginTransaction().add(R.id.container_footer,new FragmentFooter()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_search_container,new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();

        requestAllUsers();


      /*  mRefreshLayout.setRefreshing(true);
        requestAllUsers();  // getting all users

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mEditSearchContacts.setText("");
                requestAllUsers();
            }
        });*/


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
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);

     //   mImgSearch = findViewById(R.id.contact_search_img_search);
     //   mEditSearchContacts = findViewById(R.id.contact_search_edit_search);
        mRecycler = findViewById(R.id.contact_search_recycler_view);
        mLayoutForDetails = findViewById(R.id.contact_search_layout_for_details);
        //mProgressBar = findViewById(R.id.contact_search_progressbar);
        mTxtNoConnection = findViewById(R.id.contact_search_txt_no_connection);
        mRefreshLayout = findViewById(R.id.contact_search_refresh_layout);

    }


    // requesting to get all registered users from server
    private void requestAllUsers()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                mRefreshLayout.setRefreshing(true);
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
        mToolbarTitle.setText(getString(R.string.add_contact));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    private void setContactsAdapter() {
        mAdapter = new AdapterContactAdd(ActivityContactAdd.this,mUserList, null);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);

    }

    private  void setListeners()
    {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestAllUsers();
            }
        });

        /*mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                voiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
            }
        });

        // to filter list
        mEditSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filterListByName(editable.toString());

            }
        });*/
        }

  /*      // filtering all registered user with by input name
    private void filterListByName(String text)
    {
        //new array list that will hold the filtered data
        ArrayList<User> filterdUser = new ArrayList<>();

        //looping through existing elements
        for (User user : mUserList)
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
        mAdapter.filterList(filterdUser);


    }
*/


    // filtering all registered users by role after selecting from options menu
    private void filterListByRoleId(String filterRoleId)
    {
        Log.i(TAG,"filterRoleId = "+filterRoleId);
        //new array list that will hold the filtered data
        ArrayList<User> filterdUser = new ArrayList<>();

        //looping through existing role IDs
        for (User user : mUserList)
        {
            //if the existing elements contains the search input
            String roleId = user.getRoleId();
            if (roleId != null)
            {
                if (roleId.toLowerCase().contains(filterRoleId.toLowerCase()))
                {
                    //adding the element to filtered list
                    filterdUser.add(user);
                }
            }

        }
        mAdapter.filterList(filterdUser);
    }



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
                  /*  else
                        menu.add(Menu.NONE, listRoleIds.get(i), Menu.NONE, listRoleNames.get(i));*/
                }
            }
            else
                Log.e(TAG,"listRoleName's size and listRoleIds' size are not equal, it means role is not correct ");

        }
        else
            Log.e(TAG," getRoleNameList() or getRoleIdList() method is returning null");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // to go back on click on back button on toolbar
        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        // listRoleIds containss role IDs
        List<Integer> listRoleIds = RoleHelper.getInstance(mContext).getRoleIdList();
        for (int i=0 ; i<listRoleIds.size() ; i++)
        {
            if (listRoleIds.get(i) == item.getItemId())   // checking if item id is roleId or not
                filterListByRoleId(String.valueOf(item.getItemId()));  // here item id is user role id

        }


        return true;
    }

/*    // for converting voice to text, and putting spoken text in search EditText
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

    }*/



    @Override
    public void onGetRegisteredUsers(List<User> userList)
    {
        mUserList = userList;
       // mAdapter.addUserList(userList);
       //mAdapter.notifyDataSetChanged();
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



    @Override
    public void onSearchToFilter(String searchedText) {
        //new array list that will hold the filtered data
        ArrayList<User> filterdUser = new ArrayList<>();

        //looping through existing elements
        for (User user : mUserList)
        {
            //if the existing elements contains the search input
            String userName = user.getName();
            if (userName != null)
            {

                if (searchedText != null && !searchedText.isEmpty() && userName.toLowerCase().contains(searchedText.toLowerCase()))
                {
                    //adding the element to filtered list
                    filterdUser.add(user);
                }
            }

        }
        mAdapter.filterList(filterdUser);
    }
}
