package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterChatGoupProfile;
import org.ctdworld.propath.contract.ContractGroupChatInjury;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentChatSelectedImageOrVideoFullSize;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterGroupChatInjury;

import java.util.ArrayList;
import java.util.List;

public class ActivityChatGroupProfile extends AppCompatActivity implements ContractGroupChatInjury.View
{

    public static final String TAG = ActivityChatGroupProfile.class.getSimpleName();
    private final int REQUEST_CODE_PROFILE_IMAGE = 100;
    public static final int REQUEST_CODE_ADD_PARTICIPANTS = 200;  // TO GET STATUS WHETHER PARTICIPANTS HAVE BEEN ADDED OR NOT

    public static final String KEY_CHAT_DATA = "group data";
    public static final String KEY_IS_GROUP_PROFILE_CHANGED = "is profile changed";
    public static final String KEY_FINISH_CHAT_ACTIVITY = "finish group profile";

    Context mContext;
    AdapterChatGoupProfile mAdapter;
    RecyclerView mRecyclerParticipants;


    ContractGroupChatInjury.Presenter mPresenter;

    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    ImageView mToolbarImgProfile;
    ImageView mImgOptionMenu;

    DialogLoader mLoader;


    private Chat mChat; // contains Group data sent by calling component

    // to check if group name or group pic is changed or not, to send data back to calling activity
    Boolean mIsGroupNameOrPicChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_profile);
        init();
        setToolbar();
        setAdapter();

        requestGroupMemberList();

        mImgOptionMenu.setOnClickListener(onImgOptionsMenuClicked);

    }
    private void init()
    {
        mContext = this;
        mLoader = DialogLoader.getInstance("Loading...");
        mPresenter = new PresenterGroupChatInjury(mContext, this);

        mToolbar = findViewById(R.id.activity_group_profile_toolbar);
        mToolbarTxtTitle = findViewById(R.id.activity_group_profile_toolbar_txt_title);
        mToolbarImgProfile = findViewById(R.id.activity_group_profile_toolbar_img_profile);
        mImgOptionMenu = findViewById(R.id.activity_group_profile_img_options_menu);

        mRecyclerParticipants=findViewById(R.id.activity_group_profile_recycler_view);
        mToolbarTxtTitle = findViewById(R.id.activity_group_profile_toolbar_txt_title);
        mToolbarImgProfile = findViewById(R.id.activity_group_profile_toolbar_img_profile);


        // getting Chat object from Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mChat = (Chat) bundle.getSerializable(KEY_CHAT_DATA);
          //  Log.i(TAG,"group type = "+mChat.getGroupType());
           // Log.i(TAG,"user id = "+mChat.getUserId());
        }
    }


    //setting adapter, to show participants, exit group, add participant
    private void setAdapter()
    {
        ArrayList<String> items = new ArrayList<>();
        mAdapter = new AdapterChatGoupProfile(ActivityChatGroupProfile.this, mPresenter, mChat);
        /*for (int i=0; i<5; i++)
        {
            User user = new User();
            user.setName("Athlete "+i);
            mAdapter.addParticipantInList(user);
        }*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerParticipants.setLayoutManager(layoutManager);
        mRecyclerParticipants.setAdapter(mAdapter);
    }



    private void requestGroupMemberList()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                if (mLoader != null)
                    mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);

                Chat chat = new Chat();
                chat.setChattingToId(mChat.getChattingToId());

                mAdapter.clearOldList(); // # clearing old data
                mPresenter.requestGroupMemberList(chat);
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error while requesting for chat users list , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection", "Please connect to internet");
        }
    }


    // preparing toolbar, setting group name and profile pic
    private void setToolbar()
    {
        Log.i(TAG,"Setting toolbar");
        if (mChat.getGroupAdminId().equals(SessionHelper.getUserId(mContext)))
            mImgOptionMenu.setVisibility(View.VISIBLE);
        else
            mImgOptionMenu.setVisibility(View.GONE);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);



            if (mChat != null)
            {
               showGroupProfile(mChat.getChattingToPicUrl());
            }
            else
                Log.e(TAG,"mChat is null");

    }


    // showing group pic
    private void showGroupProfile(String url)
    {
        mToolbarTxtTitle.setText(mChat.getChattingToName());
        int picDimen = (int)getResources().getDimension(R.dimen.groupChatProfileImgSize);
        int picSize = UtilHelper.convertDpToPixel(mContext,picDimen);

        Log.i(TAG,"group size = "+picSize);

        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                .apply(new RequestOptions().error(R.drawable.img_default_black))
                .apply(new RequestOptions().override(picSize, picSize))
                .apply(new RequestOptions().centerCrop())
                .into(mToolbarImgProfile);
    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (mChat.getGroupAdminId().equals(SessionHelper.getUserId(mContext)))
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.options_menu_group_proifle,menu);

            return true;

        }

        return false;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_group_profile_edit_name:
                editGroupName();
                break;

            case R.id.menu_group_profile_edit_pic:

                break;
        }
        return true;
    }*/




    // to edit group name
    private void editGroupName()
    {
        DialogEditText dialogEditText = DialogEditText.getInstance("Edit Group Name", "Enter Group Name", "Edit",false);
        dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
            @Override
            public void onButtonClicked(String enteredValue) {
                if (enteredValue != null && mChat != null)
                {
                    mLoader = DialogLoader.getInstance("Saving...");
                    mLoader.show(getSupportFragmentManager(),ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
                   /* GroupChatInjury groupChatInjury = new GroupChatInjury();
                    groupChatInjury.setGroupId(mChat.getGroupId());
                    groupChatInjury.setGroupName(enteredValue);*/
                   mChat.setChattingToName(enteredValue);

                    mPresenter.editGroupName(mChat);
                }
                else
                    Log.e(TAG,"entered value or mChat is null in editGroupName() method");
            }
        });
        dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
    }


    // get path of selected image and edith group pic
    private void editGroupPic(Intent intentSelectedImage)
    {
        Uri uri = intentSelectedImage.getData();
        String path = FileHelper.getFilePath(mContext, uri);
        Log.i(TAG,"selected image path = "+path);

        final ArrayList<String> listPath = new ArrayList<>();
        listPath.add(path);
        FragmentChatSelectedImageOrVideoFullSize fullSize = FragmentChatSelectedImageOrVideoFullSize.getInstance(listPath,"Selected Image", "Save");
        fullSize.setOnSendClickListener(new FragmentChatSelectedImageOrVideoFullSize.OnSendClickListner() {
            @Override
            public void onSelectedImagesReceived(List<String> selectedImagesPath) {
               /* GroupChatInjury groupChatInjury = new GroupChatInjury();
                groupChatInjury.setGroupId(mChat.getGroupId());
                groupChatInjury.setGroupPicUrl(selectedImagesPath.get(0));  // setting file path*/
                if (mPresenter != null)
                {
                    mChat.setChattingToPicUrl(listPath.get(0));
                    mPresenter.editGroupImage(mChat);
                }

                mLoader = DialogLoader.getInstance("Saving...");
                if (mLoader != null)
                    mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            }
        });

        fullSize.show(getSupportFragmentManager(), "");


    }


    // sending data back to  calling activity
    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
       // Log.i(TAG, "Setting result");
        intent.putExtra(KEY_CHAT_DATA, mChat);
        intent.putExtra(KEY_IS_GROUP_PROFILE_CHANGED, mIsGroupNameOrPicChanged); // setting if something has been changed
        if (mIsGroupNameOrPicChanged)
            setResult(RESULT_OK,intent);
        else
            setResult(RESULT_CANCELED, intent);


        // #using onBackPressed at the end set Result to send back to calling activity.
        // #if we don't use onBackPressed() method at end then result will not be setsuper.onBackPressed();
        super.onBackPressed();

     //   finish(); // using finish here instead of onBackPressed() at the end set Result to send back to calling activity.
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null)
        {
            if (requestCode == REQUEST_CODE_PROFILE_IMAGE)
            {
               editGroupPic(data);
            }
            // if participants have been added then participants list will be loaded again from server
            if (requestCode == REQUEST_CODE_ADD_PARTICIPANTS)
                requestGroupMemberList();
        }
    }

    // hiding progress
    private void hideProgress()
    {
        if (mLoader !=  null && mLoader.isAdded())
            mLoader.dismiss();
    }



    View.OnClickListener onImgOptionsMenuClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_EDIT, "Edit Group Name")
                    .addOption(BottomSheetOption.OPTION_EDIT_2, "Change Profile Pic");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option)
                {
                    switch (option)
                    {
                        case BottomSheetOption.OPTION_EDIT:
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent,REQUEST_CODE_PROFILE_IMAGE);
                            break;

                        case BottomSheetOption.OPTION_EDIT_2:
                            editGroupName();
                            break;
                    }
                }
            });

            options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
        }
    };


    @Override
    public void onFailed() {
        hideProgress();
        DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
    }

    @Override
    public void onGroupCreated(Chat chat) {
        hideProgress();
    }


    // updating group name on edit group success
    @Override
    public void onEditGroupNameSuccessful(Chat chat) {
        Log.i(TAG,"onEditGroupNameSuccessful() method called , changing group name , group name = "+chat.getChattingToName());
        hideProgress();  // hiding progress
        if (chat != null)
        {
            mIsGroupNameOrPicChanged = true;
            // setting new group name to send back to calling activity
            if (mChat != null)
                mChat.setChattingToName(chat.getChattingToName());

            if (mToolbarTxtTitle != null)
                mToolbarTxtTitle.setText(chat.getChattingToName());
        }
    }

    @Override
    public void onEditGroupImageSuccessful(Chat chat) {
        Log.i(TAG,"onEditGroupNameSuccessful() method called , changing group name , group name = "+chat.getChattingToName());
        hideProgress();  // hiding progress
        if (chat != null)
        {
            mIsGroupNameOrPicChanged = true;
            // setting new group pic to send back to calling activity
            if (mChat != null)
                mChat.setChattingToPicUrl(chat.getChattingToPicUrl());

            if (mToolbarImgProfile != null)
                showGroupProfile(chat.getChattingToPicUrl());
        }
    }

    @Override
    public void onRemoveParticipantSuccess(Chat chat, int positionInAdapter)
    {
        hideProgress();
        if (mAdapter != null)
            mAdapter.onMemberRemoved(positionInAdapter);
    }

    @Override
    public void onGroupExitSuccess(Chat chat)
    {
        hideProgress();  // hiding progress
        Intent intent = new Intent();
        intent.putExtra(KEY_FINISH_CHAT_ACTIVITY, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }

    @Override
    public void onReceivedGroup(Chat chat)
    {

        hideProgress();  // hiding progress
    }

    @Override
    public void onReReceivedGroupMember(Chat chat)
    {
        mAdapter.addParticipantInList(chat);
        hideProgress();  // hiding progress
    }
}
