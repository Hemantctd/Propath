package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterChatUserGroupList;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractChatUsers;
import org.ctdworld.propath.fragment.DialogCreateGroup;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentFooter;
import org.ctdworld.propath.fragment.FragmentSearch;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterChatUsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/* # this Activity is being used to show list of users to whom logged in user has ever chatted(one to one chat) and to show group
    list for group chat(belongs to contact module) And for injury management(this is also group chat but this is different from normal group chat
    , belongs to Injury Management module in dashboard).
    we are using list type here to differentiate Group Chat and Injury Management Group*/
public class ActivityChatUserGroupList extends BaseActivity implements ContractChatUsers.View {
    // # constants
    private final String TAG = ActivityChatUserGroupList.class.getSimpleName();

    // # views
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private ImageView mImgToolbarOptionsMenu;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mTxtNoConnection;
    private View mLayoutForDetails;

    // # other variables
    private Context mContext;
    private List<Chat> mChatUsersList = new ArrayList<>();
    private ContractChatUsers.Presenter mPresenter;
    private List<Chat> mUsersList = new ArrayList<>();
    private AdapterChatUserGroupList mAdapter;

    // # keys to send data and variables to save data
    public static final String KEY_TYPE_CHAT_LIST = "chat list type";
    public static final int VALUE_GROUP_AND_ONE_TO_ONE = 1;
    public static final int VALUE_INJURY_MANAGEMENT = 2;
    private int mTypeChatList = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        init();
        setToolbar();
        setContactsAdapter();
        setListeners();
        getSupportFragmentManager().beginTransaction().add(R.id.container_footer, new FragmentFooter()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getSearchFragment() != null)
                    getSearchFragment().clearSearchedText();

                requestChatUsers();
                mAdapter.clearOldList();
            }
        });


    }

    private void init() {
        mContext = this;
        mPresenter = new PresenterChatUsers(mContext);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgToolbarOptionsMenu = mToolbar.findViewById(R.id.toolbar_img_options_menu);
        mRecycler = findViewById(R.id.contact_message_recycler_view);
        mLayoutForDetails = findViewById(R.id.contact_search_layout_for_details);
        mTxtNoConnection = findViewById(R.id.contact_search_txt_no_connection);
        mRefreshLayout = findViewById(R.id.contact_search_refresh_layout);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
                mTypeChatList = bundle.getInt(KEY_TYPE_CHAT_LIST);
        }

    }


    // requesting to get all users from server
    private void requestChatUsers() {
        if (UtilHelper.isConnectedToInternet(mContext)) {
            try {
                if (mTypeChatList == VALUE_GROUP_AND_ONE_TO_ONE) {
                    {
                        if (mAdapter != null)
                            mAdapter.clearOldList();

                        mPresenter.requestChatUsers(SessionHelper.getInstance(mContext).getUser().getUserId(), Chat.GROUP_TYPE_GROUP_CHAT);
                    }
                } else if (mTypeChatList == VALUE_INJURY_MANAGEMENT) {
                    if (mAdapter != null)
                        mAdapter.clearOldList();

                    mPresenter.requestChatUsers(SessionHelper.getInstance(mContext).getUser().getUserId(), Chat.GROUP_TYPE_INJURY_MANAGEMENT);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error while requesting for chat users list , " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mLayoutForDetails.setVisibility(View.GONE);
            mTxtNoConnection.setVisibility(View.VISIBLE);
        }
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        if (mTypeChatList == VALUE_INJURY_MANAGEMENT)
            mToolbarTitle.setText(getString(R.string.injury_management));
        else
            mToolbarTitle.setText(getString(R.string.messages));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    private void setContactsAdapter() {
        mAdapter = new AdapterChatUserGroupList(ActivityChatUserGroupList.this, mChatUsersList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }


    private void setListeners() {
        // if chat list type is injury management then options menu will be shown in toolbar
        if (VALUE_INJURY_MANAGEMENT == mTypeChatList) {
            mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
            createInjuryManagementGroup();
        }

    }

    // if user has come from injury management in dashboard then there would be options mene to create injury management group
    private void createInjuryManagementGroup() {
        mImgToolbarOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_CREATE_GROUP, "Create Group");

                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option) {
                        if (option == BottomSheetOption.OPTION_CREATE_GROUP) {
                            DialogCreateGroup dialogCreateGroup = DialogCreateGroup.getInstance(Chat.GROUP_TYPE_INJURY_MANAGEMENT);
                            dialogCreateGroup.setOnGroupCreateListener(new DialogCreateGroup.OnGroupCreateListener() {
                                @Override
                                public void onGroupCreated(Chat group) {
                                    if (group != null)
                                        if (mAdapter != null)
                                            requestChatUsers();
                                }
                            });
                            dialogCreateGroup.show(getSupportFragmentManager(), "");
                        }
                    }
                });

                options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "requesting for users and group list in onResume() method");
        mRefreshLayout.setRefreshing(true);
        requestChatUsers();  // getting all users
        mAdapter.clearOldList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // to go back on click on back button on toolbar
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }


    @Override
    public void onChatUserReceived(final Chat chat) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                mUsersList.add(chat);   // adding chat to mUsersList to filter list by entering text in EditText

                // # data will be loaded in adapter only when user types text in search box
                mAdapter.updateChatUserList(chat);
                // mRecycler.smoothScrollToPosition(0);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                // DialogHelper.showSimpleCustomDialog(mContext,"Failed...");

            }
        });
    }


    @Override
    public void onShowMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                DialogHelper.showSimpleCustomDialog(mContext, message);
            }
        });
    }


}
