package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterContactAdd;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractGetRegisteredUsers;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentProfileView;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Profile;
import org.ctdworld.propath.model.RepAchievement;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterGetRegisteredUsers;

import java.util.ArrayList;
import java.util.List;

public class ActivityProfileView extends BaseActivity implements ContractGetRegisteredUsers.View, AdapterContactAdd.OnUserClickedListener, FragmentProfileView.Listener
{
    // # constants
    private final String TAG = ActivityProfileView.class.getSimpleName();


    // # views
    private TextView mToolbarTitle;
    private ImageView mImgToolbarOptionsMenu;
    private EditText mSearchView;
    private RecyclerView mRecycler;
    private View mFragmentContainer;
    Toolbar mToolbar;


    // # other variables
    private Context mContext;
    private ContractGetRegisteredUsers.Presenter mPresenter;
    private List<User> mUserList = new ArrayList<>();
    private AdapterContactAdd mAdapter;
    boolean mIsSelfProfile = true;  // to check profile is of logged in user or not


    private String USER_ID = ""; //  string to contain user id, to get profile


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        init();
        setToolbar();
        setUpAdapterForUsers();
        loadData();  // loading data and adding fragment to show profile


        // # listeners
        mImgToolbarOptionsMenu.setOnClickListener(onToolbarOptionsMenuClicked);
        mSearchView.addTextChangedListener(onSearchQueryTextListener);

    }



    // # initializing variables
    private void init()
    {
        mContext = this;
        mPresenter = new PresenterGetRegisteredUsers(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mImgToolbarOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        mFragmentContainer = findViewById(R.id.profile_view_fragment_container);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mSearchView = findViewById(R.id.toolbar_search_view);
        mRecycler = findViewById(R.id.recycler_user_list);
        mToolbar = findViewById(R.id.toolbar);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            USER_ID = bundle.getString(ConstHelper.Key.ID);
            if (USER_ID != null  ) {
                mIsSelfProfile = USER_ID.equals(SessionHelper.getInstance(mContext).getUser().getUserId());
            }
        }
    }




    // adding fragment to show profile data of single user. Loading all users list also, to show when user searches in toolbar
    private void loadData() {
        if (USER_ID == null)
            return;

        // # adding fragment to show profile data of single user for whom this activity has been started
        FragmentProfileView fragmentProfileView = FragmentProfileView.getInstance(USER_ID);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_view_fragment_container,fragmentProfileView, ConstHelper.Tag.Fragment.PROFILE_VIEW).commit();

        requestAllUsers();   // requesting to get all users from server
    }




    // setting up adapter for user who will be displayed after text is entered in EditText to filter
    private void setUpAdapterForUsers()
    {
        mAdapter = new AdapterContactAdd(mContext, mUserList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);
    }




    // filtering all registered user with by input name
    public void filterListByName(String text)
    {
        ArrayList<User> filterdUser = new ArrayList<>();


        //filter by user name
        for (User user : mUserList)
        {
            //if the existing elements contains the search input
            // String userRole = RoleHelper.getInstance(mContext).getRoleNameById(user.getRoleId());
            String userName = user.getName();
            if (userName != null && text != null && !text.isEmpty() && userName.equalsIgnoreCase(text))
            {
                //if (!text.isEmpty() && userRole.toLowerCase().contains(text.toLowerCase()))
                boolean userFoundInFilterList = false;
                for (User filter : filterdUser)
                {
                    if (filter.getUserId().equalsIgnoreCase(user.getUserId()))
                        userFoundInFilterList = true;
                }
                if (!userFoundInFilterList)
                    filterdUser.add(user);
            }
        }


        //filter by role name
        for (User user : mUserList)
        {
            //if the existing elements contains the search input
            String userRole = RoleHelper.getInstance(mContext).getRoleNameById(user.getRoleId());
            // String userName = user.getName();
            if (userRole != null && text != null && !text.isEmpty() && userRole.equalsIgnoreCase(text))
            {
                //if (!text.isEmpty() && userRole.toLowerCase().contains(text.toLowerCase()))
                boolean userFoundInFilterList = false;
                for (User filter : filterdUser)
                {
                    if (filter.getUserId().equalsIgnoreCase(user.getUserId()))
                        userFoundInFilterList = true;
                }
                if (!userFoundInFilterList)
                    filterdUser.add(user);

            }
        }


        //filter by sports
        for (User user : mUserList)
        {
            //if the existing elements contains the search input
            // String userRole = RoleHelper.getInstance(mContext).getRoleNameById(user.getRoleId());
            String userSports = user.getSports();
            if (userSports != null && text != null && !text.isEmpty() && userSports.equalsIgnoreCase(text))
            {
                //if (!text.isEmpty() && userRole.toLowerCase().contains(text.toLowerCase()))
                boolean userFoundInFilterList = false;
                for (User filter : filterdUser)
                {
                    if (filter.getUserId().equalsIgnoreCase(user.getUserId()))
                        userFoundInFilterList = true;
                }
                if (!userFoundInFilterList)
                    filterdUser.add(user);
            }
        }


        //filter by location
        for (User user : mUserList)
        {
            //if the existing elements contains the search input
            // String userRole = RoleHelper.getInstance(mContext).getRoleNameById(user.getRoleId());
            String useLocation = user.getLocation();
            if (useLocation != null && text != null && !text.isEmpty() && useLocation.equalsIgnoreCase(text))
            {
                //if (!text.isEmpty() && userRole.toLowerCase().contains(text.toLowerCase()))
                boolean userFoundInFilterList = false;
                for (User filter : filterdUser)
                {
                    if (filter.getUserId().equalsIgnoreCase(user.getUserId()))
                        userFoundInFilterList = true;
                }
                if (!userFoundInFilterList)
                    filterdUser.add(user);
            }
        }

       /* //filter by sports name
        for (User user : mUserList)
        {
            //if the existing elements contains the search input
            String sportName = "boxing";
            // String userName = user.getName();
            if (sportName != null && text != null)
            {
                //if (!text.isEmpty() && userRole.toLowerCase().contains(text.toLowerCase()))
                if (!text.isEmpty() && sportName.equalsIgnoreCase(text))
                {
                                        boolean userFoundInFilterList = false;
                    for (User filter : filterdUser)
                    {
                        if (filter.getUserId().equalsIgnoreCase(user.getUserId()))
                            userFoundInFilterList = true;
                    }
                    if (!userFoundInFilterList)
                        filterdUser.add(user);

                }
            }
        }*/



        mAdapter.filterList(filterdUser);
    }


    // showing options menu icon
    public void enableOptionsMenu()
    {
        mIsSelfProfile = true;

        Log.i(TAG,"enableEdit() method called ");
        if (mImgToolbarOptionsMenu != null)
            mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
    }

    // hiding options menu icon
    public void disableOptionsMenu()
    {
        Log.i(TAG,"disableEdit() method called ");
        mIsSelfProfile = false;

        if (mImgToolbarOptionsMenu != null)
            mImgToolbarOptionsMenu.setVisibility(View.GONE);

    }



    // setting toolbar
    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        mSearchView.setVisibility(View.VISIBLE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onBackPressed() {

        // # if user is currently looking other's profile and this profile page was opened to see self profile then when user clicks back button
        // then first user's profile will be shown for whom this page was opened.
        if (!mIsSelfProfile && USER_ID.equals(SessionHelper.getUserId(mContext)))
        {
            FragmentProfileView profileView = (FragmentProfileView) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.PROFILE_VIEW);
            if (profileView != null)
                profileView.requestProfileDataFromServer(USER_ID);
        }
        else
            super.onBackPressed();
    }




    public void changeFragment(Fragment fragment, String tag)
    {
        if (tag.equals(ConstHelper.Tag.Fragment.PROFILE_VIEW))
            showSearchView();
        else
            hideSearchView();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profile_view_fragment_container,fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }


    private void requestAllUsers()
    {
        Log.i(TAG,"requestAllUsers() method called");
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
            onHideProgress();
        }
    }

    public void showToolbarTitle(String title)
    {
        if (title != null && !title.isEmpty())
        {
            mToolbarTitle.setVisibility(View.VISIBLE);
            mToolbarTitle.setText(title);
        }
    }


    public void hideToolbarTitle()
    {
        mToolbarTitle.setVisibility(View.GONE);
    }



    public void showSearchView()
    {
        if (mSearchView != null)
            mSearchView.setVisibility(View.VISIBLE);
    }

    public void hideSearchView()
    {
        if (mSearchView != null)
            mSearchView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoader() {
        super.hideLoader();

    }





    /*   // # this method is called when user is searched in FragmentProfileSearchUser
    @Override
    public void onSearchedUserSelected(User user)
    {
        FragmentProfileView profileView = (FragmentProfileView) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.PROFILE_VIEW);
        if (profileView != null && user.getUserId()!= null)
        {
            fragmentManager.popBackStack();
            profileView.requestProfileDataFromServer(user.getUserId());
        }
        else
            Log.e(TAG,"profileView or user.getUserId() is null in onSearchedUserSelected() method");
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ConstHelper.RequestCode.PROFILE_UPDATE && data != null)
        {
            String userId = data.getStringExtra(ConstHelper.Key.ID);
            if (userId != null)   // loading new user data if profile was updated
            {
                USER_ID = userId;
                loadData();
            }

        }
    }

    @Override
    public void onUserClicked(User user)
    {
        if (user == null || user.getUserId() == null)
        {
            Log.e(TAG,"user is null in onUserClicked() method");
            return;
        }
        mRecycler.setVisibility(View.GONE);
        mFragmentContainer.setVisibility(View.VISIBLE);
        UtilHelper.hideKeyboard(this); // hiding keyboard


        FragmentProfileView fragmentProfileView = (FragmentProfileView) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.PROFILE_VIEW);

        if (fragmentProfileView == null || !fragmentProfileView.isAdded() || !fragmentProfileView.isVisible())
        {
            // it will show FragmentProfileView and user details
            changeFragment(FragmentProfileView.getInstance(user.getUserId()), ConstHelper.Tag.Fragment.PROFILE_VIEW);
        }
        else
            fragmentProfileView.requestProfileDataFromServer(user.getUserId());

    }



    @Override
    public void onGetRegisteredUsers(List<User> userList)
    {
        //   mAdapter.addUserList(userList);
        // mAdapter.notifyDataSetChanged();
        mUserList = userList;
    }

    @Override
    public void onFailed() {
        DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
    }

    @Override
    public void onShowProgress() {
        // mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideProgress() {
        // mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        // mRefreshLayout.setEnabled(false);

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        // mRefreshLayout.setEnabled(true);
    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }

    @Override
    public void onUserFriendStatusUpdated(String userId, String friendStatus) {
        mAdapter.updateUser(userId, friendStatus);
    }





    View.OnClickListener onToolbarOptionsMenuClicked = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            if (!mIsSelfProfile)
                return;

            builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit Profile");
            builder.addOption(BottomSheetOption.OPTION_ADD_ACHIEVEMENT, "Add Achievement");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
            {
                @Override
                public void onOptionSelected(int option)
                {
                    switch (option)
                    {
                        case BottomSheetOption.OPTION_EDIT:
                            Profile profile = new Profile();
                            FragmentProfileView fragmentProfile = (FragmentProfileView) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.PROFILE_VIEW);
                            if (fragmentProfile != null)
                                profile = fragmentProfile.getProfileData();

                            Intent intent = new Intent(mContext, ActivityProfileUpdate.class);
                            intent.putExtra(ActivityProfileUpdate.KEY_PROFILE_DATA, profile);
                            startActivityForResult(intent, ConstHelper.RequestCode.PROFILE_UPDATE);

                            break;

                        case BottomSheetOption.OPTION_ADD_ACHIEVEMENT:
                            RepAchievement repAchievement = new RepAchievement();
                            Intent intentAchievement = new Intent(new Intent(mContext, ActivityProfileRepAchievementAddEdit.class));
                            intentAchievement.putExtra(ActivityProfileRepAchievementAddEdit.REP_Type,"add");
                            intentAchievement.putExtra(ActivityProfileRepAchievementAddEdit.REP_ID,"");
                            intentAchievement.putExtra(ActivityProfileRepAchievementAddEdit.REP_DATA,repAchievement);

                            startActivity(intentAchievement);

                            break;

                    }

                }
            });
            options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
        }
    };




    TextWatcher onSearchQueryTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(TAG,"onQueryTextSubmit() method called");

            if (s != null && !s.toString().isEmpty())
            {
                mFragmentContainer.setVisibility(View.GONE);
                mRecycler.setVisibility(View.VISIBLE);

                //  if (s.toString().equals(RoleHelper.))
                filterListByName(s.toString());
            }
            else
            {
                mFragmentContainer.setVisibility(View.VISIBLE);
                mRecycler.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };











   /* private void setToolBar()
    {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }*/


}
