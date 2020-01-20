package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterChatAddParticipantToGroup;
import org.ctdworld.propath.contract.ContractChatAddParticipantsToGroup;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterAddParticipantsToGroup;

import java.util.ArrayList;
import java.util.List;

public class ActivityChatAddParticipantToGroup extends AppCompatActivity implements ContractChatAddParticipantsToGroup.View
{
    private final String TAG = ActivityChatAddParticipantToGroup.class.getSimpleName();
    Context mContext;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    ImageView mImgToolbarOptionsMenu, mImgSerach;
    EditText mEditSearch;
    TextView mToolbarTitle;
    FloatingActionButton mFloatAddParicipants;
    SwipeRefreshLayout mRefreshLayout;
    DialogLoader mDialogLoader;

    AdapterChatAddParticipantToGroup mAdapterChatAddParticipantToGroup;  // adapter to show Users and add participants
    List<User> mUserList = new ArrayList<>();  // list of users to add in group

    //  public static final String KEY_GROUP_TYPE = "";
    public static final String KEY_CHAT_DATA = "group";
    // # key to put boolean value whether participants have been added or not
    public static final String KEY_STATUS_PARTICIPANTS_ADDED = "participants added";

 //   public static final String GROUP_TYPE_GROUP_CHAT = GroupChatInjury.GROUP_TYPE_GROUP_CHAT;
 //   public static final String GROUP_TYPE_INJURY_MANAGEMENT = GroupChatInjury.GROUP_TYPE_INJURY_MANAGEMENT;
    private Chat mChat = new Chat();

    ContractChatAddParticipantsToGroup.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant_to_group);
        init(); // to initialize all variable
        setToolbar();
        setUsersAdapter();
        setListeners();

        // requesting for contact users
        mRefreshLayout.setRefreshing(true);
        requestContactUsersList();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestContactUsersList();
            }
        });

    }
    private void init()
    {
        mContext = this;
        mPresenter = new PresenterAddParticipantsToGroup(mContext, this);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgToolbarOptionsMenu = mToolbar.findViewById(R.id.toolbar_img_options_menu);
        mRefreshLayout = findViewById(R.id.activity_add_participants_refresh_layout);
        mImgSerach = findViewById(R.id.add_participant_to_group_img_search);
        mEditSearch = findViewById(R.id.add_participant_to_group_edit_search);
        mRecyclerView=findViewById(R.id.activity_add_participants_recycler_view);
        mFloatAddParicipants = findViewById(R.id.activity_add_participants_floating_button);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mChat = (Chat) bundle.getSerializable(KEY_CHAT_DATA);
        }

    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Add participants");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }

    // setting users adapter
    private void setUsersAdapter() {
      /*  mListItems.add("Patrick Mailata");
        mListItems.add("Rachael Tuwhangi");
        mListItems.add("Kimiora");
        mListItems.add("Gaurav Sharma");
        mListItems.add("Amorangi");
        mListItems.add("Sakshi Kumari");
        mListItems.add("Malesala");
        mListItems.add("Ranginui");
        mListItems.add("Iulieta");
        mListItems.add("Syed Irshad");*/

       /* if (mGroupChatInjury != null)
        {*/
        mAdapterChatAddParticipantToGroup = new AdapterChatAddParticipantToGroup(mContext,mUserList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapterChatAddParticipantToGroup);
       // }
    }


    // requesting to get users list depending on group type (group_chat, injury_management)
    private void requestContactUsersList()
    {

        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                mRefreshLayout.setRefreshing(true);
                // clearing old data, to show latest users
                mAdapterChatAddParticipantToGroup.clearOldUserList();
                mPresenter.requestContactUsers(mChat);


            }
            catch (Exception e)
            {
                Log.e(TAG,"Error while requesting for contacts , "+e.getMessage());
                mRefreshLayout.setRefreshing(false);
                e.printStackTrace();
            }
        }
        else
        {
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...","Please establish internet connection");
            mRefreshLayout.setRefreshing(false);
        }
    }


    private void setListeners() {

        mImgToolbarOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ActivityChatAddParticipantToGroup.this, ActivityContactAdd.class));
            }
        });


        mImgSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voiceToText();
            }
        });



        // to filter names
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });


        mFloatAddParicipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addParticipantsToGroup();
            }
        });
    }


    // adding participants to group
    private void addParticipantsToGroup()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            mDialogLoader = DialogLoader.getInstance("Adding...");
            mDialogLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    List<User> listUsers;
                    if (mAdapterChatAddParticipantToGroup != null)
                    {
                        listUsers = mAdapterChatAddParticipantToGroup.getSelectedUsersList();
                        if (listUsers != null)
                        {
                            Log.i(TAG,"total participants = "+listUsers.size());
                             mPresenter.addParticipantsToGroup(mChat, listUsers);
                        }
                        else
                            Log.e(TAG,"listUsers is null in addParticipants() group");
                    }
                    else
                        Log.e(TAG,"mAdapterChatAddParticipantToGroup is null in addParticipants() method");

                }
            }).start();

        }
        else
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
    }


    private void voiceToText()
    {
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
                    mEditSearch.setText(spokenText);
                    mEditSearch.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearch.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(),"");
        }


    }



    private void filter(String text)
    {
        //new array list that will hold the filtered data
        ArrayList<User> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (User user : mUserList) {
            //if the existing elements contains the search input
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(user);
            }
        }
        mAdapterChatAddParticipantToGroup.filterList(filterdNames);
    }


    // to hide progress
    private void hideProgressDialog()
    {
        if (mDialogLoader != null && mDialogLoader.isAdded())
            mDialogLoader.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }


    @Override
    public void onShowMessage(String message)
    {
        DialogHelper.showSimpleCustomDialog(mContext, message);
    }

    @Override
    public void onFailed(String message)
    {
        hideProgressDialog();
        if (message != null)
            DialogHelper.showSimpleCustomDialog(mContext, "Failed...", message);
        else
            DialogHelper.showSimpleCustomDialog(mContext, "Failed...");
    }

    @Override
    public void onReceivedContactUsers(List<User> userList) {

        hideProgressDialog();
        if (mAdapterChatAddParticipantToGroup != null)
            mAdapterChatAddParticipantToGroup.addUserList(userList);

        if (mRefreshLayout != null)
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onParticipantsAddedSuccessfully()
    {
        hideProgressDialog();
        mDialogLoader = DialogLoader.getInstance("Added Successfully\n Refreshing list");
       // requestContactUsersList();

        // setting result to send back status of adding participants
        Intent intent = new Intent();
        intent.putExtra(KEY_STATUS_PARTICIPANTS_ADDED, true);
        setResult(RESULT_OK, intent);
        finish();
    }
}
