package org.ctdworld.propath.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterContactAdd;
import org.ctdworld.propath.contract.ContractGetRegisteredUsers;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterGetRegisteredUsers;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentProfileSearchUser extends Fragment implements ContractGetRegisteredUsers.View/*, AdapterContactAdd.OnUserClickedListener*/
{
    private final String TAG = FragmentProfileSearchUser.class.getSimpleName();

    Context mContext;

  //  EditText mEditSearchContacts;
    ImageView mImgSearch;
   // ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshLayout;
    TextView mTxtNoConnection;
    View mLayoutForDetails;
    RecyclerView mRecycler;
    LinearLayoutManager mLayoutManager;
    AdapterContactAdd mAdapter;
    View mSearchLayout;


    Toolbar mToolbar;
    TextView mToolbarTitle;

    ContractGetRegisteredUsers.Presenter mPresenter;
    List<User> mUserList = new ArrayList<>();
    PermissionHelper mPermissionHelper;

  //  private OnSearchUserListener mSearchUserListener;




  //  public interface OnSearchUserListener{void onSearchedUserSelected(User user);}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_contact_add, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
       // setToolbar();
        mToolbar.setVisibility(View.GONE);
//        mSearchLayout.setVisibility(View.GONE);

        setContactsAdapter();
        //setListeners();


        mRefreshLayout.setRefreshing(true);
        requestAllUsers();  // getting all users

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // mEditSearchContacts.setText("");
                requestAllUsers();
            }
        });

    }


    private void init(View view) {
        mContext = getContext();
        mPresenter = new PresenterGetRegisteredUsers(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mPermissionHelper = new PermissionHelper(mContext);

        mToolbar = view.findViewById(R.id.toolbar);
        mToolbarTitle = view.findViewById(R.id.toolbar_txt_title);

        mImgSearch = view.findViewById(R.id.contact_search_img_search);
        //mEditSearchContacts = view.findViewById(R.id.contact_search_edit_search);
        mRecycler = view.findViewById(R.id.contact_search_recycler_view);
        mLayoutForDetails = view.findViewById(R.id.contact_search_layout_for_details);
//        mSearchLayout = view.findViewById(R.id.layout_search);
        //mProgressBar = view.findViewById(R.id.contact_search_progressbar);
        mTxtNoConnection = view.findViewById(R.id.contact_search_txt_no_connection);
        mRefreshLayout = view.findViewById(R.id.contact_search_refresh_layout);

    }


  /*  public void setOnUserSearchedListener(OnSearchUserListener onUserSearchedListener)
    {
        mSearchUserListener = onUserSearchedListener;
    }


    // # this method is called when user is selected in AdapterContactAdd
    @Override
    public void onUserClicked(User user) {
        if (mSearchUserListener != null)
            mSearchUserListener.onSearchedUserSelected(user);
        else
            Log.e(TAG,"mSearchUserListener is null");
    }
*/

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

 /*   private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Add contacts");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }*/


    private void setContactsAdapter() {
        mAdapter = new AdapterContactAdd(mContext, mUserList, null);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);

    }

   /* private  void setListeners()
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

        // to filter list
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
*/


        // filtering all registered user with by input name
    public void filterListByName(String text)
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



 /*   // filtering all registered users by role after selecting from options menu
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
    }*/



   /* @Override
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
    }*/

    // for converting voice to text, and putting spoken text in search EditText

    /*private void voiceToText()
    {
        Log.i(TAG,"voiceToText() method called ");
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

        fragmentSpeechRecognition.show(getChildFragmentManager(),"");
    }*/



    @Override
    public void onGetRegisteredUsers(List<User> userList)
    {
        mUserList = userList;
        mAdapter.addUserList(userList);
       // mAdapter.notifyDataSetChanged();
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
}
