package org.ctdworld.propath.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterChat;
import org.ctdworld.propath.contract.ContractChat;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.fragment.DialogChatChooseMediaType;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.prefrence.PreferenceChat;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.presenter.PresenterChat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class ActivityChat extends AppCompatActivity implements ContractChat.View
{
    private final String TAG = ActivityChat.class.getSimpleName();
    private final int REQUEST_CODE_GROUP_PROFILE_ACTIVITY = 100; //

    // to set result, to check if profile has been changed or not
    public static final String KEY_IS_SOMETHING_CHANGED = "is changed";
    private boolean mIsProfileChanged = false;  // contains status if group profile has been changed or not, set in setResult() method



    ContractChat.View mListenerChat;



    Context mContext;
    RecyclerView mRecyclerView;
    AdapterChat mAdapter;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout mRefreshLayout;

    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    View mToolbarProfileLayout;

    EditText mEditMessage;
    ImageView mToolbarImgOptionsMenu,mToolbarImgProfile;
    ImageView mImgSendMessage; // to send text message ;
    ImageView mImgAddMedia, mImgCamera;  // to send media (images and videos)
   // ProgressBar mProgressSendMessage;
    ContractChat.Presenter mPresenterChat;
    PermissionHelper mPermissionHelper;


    int mCounterChatRequest = 1; // this contains counter to know how many times request has been made to get previous data accordingly

    // key and value to receive user detail to whom logged in user is going to chat
    public static final String KEY_CHATTING_TO_DATA = "chatting to";  // key to send User class object, to get user detail and show in adapter
    Chat mChattingTo; // this will contain the user details to whom logged in user is chatting


    //Keys to receive chat notification tay
    public static final String KEY_CHAT_NOTIFICATION_ID = "notification id";  // key to put notification id
    public static final String KEY_CHAT = "connection request data";   // key to put Chat object
    public static final String KEY_CHAT_ACTION = "action chat";
    private int mNotificationId = 0;  // default value 0 means no notification id


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mChatReceiver, new IntentFilter("chat_receiver"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_chat);
            initializeFromBundle(); // initializing mUser from bundle sent from calling component
            init();
            setToolbar();
            setChatAdapter();
            setListeners();
            requestChatList();  // getting chat list



            //registering receiver to receive chat from NotificationChatHelper


            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(mChattingTo != null)
                    {
                        // setting chat is being done in shared preference to update chat list form NotificationHelperChat, removing this on onStop()
                        PreferenceChat.setChattingStatus(mContext, true, mChattingTo.getChattingToId());
                        // clearing previous details from preferenceChat of user whom loggedIn user chatting
                        new PreferenceChat(mContext, mChattingTo.getChattingToId()).clearUserChatDetails();// clearing notification details
                    }
                }
            }).start();



            // this requests to get previous chats of some specified days
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    if (mChattingTo != null)
                    {
                        mRefreshLayout.setRefreshing(false);
                        mCounterChatRequest++;
                        Chat chat = new Chat();
                        chat.setMessageFromUserId(SessionHelper.getInstance(mContext).getUser().getUserId()); // fromUserId is my id
                        chat.setChattingToId(mChattingTo.getChattingToId());  //toUserId is user whom we are sending message.
                        //  mPresenterChat.requestPreviousChat(chat,mCounterChatRequest);
                    }

                }
            });



        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in onCreate Activity , "+e.getMessage());
            e.printStackTrace();
        }

   //    getSupportFragmentManager().beginTransaction().add(R.id.container_footer,new BlackFooterFragment()).commit();

    }

    private void setListeners()
    {
        mToolbarProfileLayout.setOnClickListener(new View.OnClickListener()
        {
         @Override
         public void onClick(View view)
         {
             Log.i(TAG,"toolbar profile clicked ");
            if (mChattingTo != null && mChattingTo.getMessageChatType() != null)
            {
                Log.i(TAG,"toolbar profile clicked "+mChattingTo.getGroupType());

                // starting Group profile activity if chat type is not one to one
                if (!Chat.CHAT_TYPE_ONE_TO_ONE.contains(mChattingTo.getMessageChatType()))
                {
                    Intent intent = new Intent(mContext, ActivityChatGroupProfile.class);
                    intent.putExtra(ActivityChatGroupProfile.KEY_CHAT_DATA, mChattingTo);
                    startActivityForResult(intent, REQUEST_CODE_GROUP_PROFILE_ACTIVITY);
                }

            }
         }
        });

        // sending text message
        mImgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (UtilHelper.isConnectedToInternet(mContext))
                {
                    String message = mEditMessage.getText().toString().trim();
                    if (!message.isEmpty())
                    {
                        if (mChattingTo != null)
                        {
                            Chat mChatSent = new Chat();
                          //  mChatSent.setChattingToName(SessionHelper.getInstance(mContext).getUser().getName());
                          //  mChatSent.setMessageFromUserPicUrl(SessionHelper.getInstance(mContext).getUser().getMessageFromUserPicUrl());
                            mChatSent.setMessageFromUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
                            mChatSent.setMessageToUserId(mChattingTo.getChattingToId());
                            mChatSent.setMessage(message);// if type is message then file would be empty,  file will be used to send image or video
                            mChatSent.setMessageType(Chat.MESSAGE_TYPE_MESSAGE); // setting file type, type may be Message, Image or Video
                            mChatSent.setFileArray(new String[]{}); // if type is message then file would be empty,  file will be used to send image or video
                            mChatSent.setMessageDateTime(Calendar.getInstance(Locale.getDefault()).getTime().toString());
//                            mChatSent.setMessageChatType(Chat.CHAT_TYPE_ONE_TO_ONE);
                            mChatSent.setMessageChatType(mChattingTo.getMessageChatType());

                            mChatSent.setPositionInAdapter(mAdapter.getItemCount()); // adding position adapter position to update when message is sent
                            mChatSent.setSendingMessage(true);

                            // # setting user details
                            User user = SessionHelper.getInstance(mContext).getUser();
                            mChatSent.setMessageFromUserId(user.getUserId());
                            mChatSent.setMessageFromUserName(user.getName());
                            mChatSent.setMessageFromUserPicUrl(user.getUserPicUrl());

                            // # setting date and time
                            DateTimeHelper dateTimeHelper = new DateTimeHelper();
                            Date dateTime = Calendar.getInstance().getTime();
                            //Log.i(TAG,"current date time = "+dateTime);
                            mChatSent.setMessageDate(dateTimeHelper.getDateStringDayMonthYear(dateTime));
                            mChatSent.setMessageDateLong(dateTimeHelper.getDateDayMonthYear(dateTime).getTime());
                            mChatSent.setMessageTime(dateTimeHelper.getTime(dateTime));


                            if (!Chat.CHAT_TYPE_ONE_TO_ONE.contains(mChattingTo.getMessageChatType()))
                                mChatSent.setGroupType(mChattingTo.getGroupType());  // setting group type
                            else
                                mChatSent.setGroupType("");  // setting group type empty because it's not group

                            onChatReceived(mChatSent);

                            mPresenterChat.sendChat(mChatSent);

                           // mImgSendMessage.setVisibility(View.GONE);
                           // mProgressSendMessage.setVisibility(View.VISIBLE);  // progressbar is visible now to show message progress
                        }
                    }
                }
                else
                    DialogHelper.showSimpleCustomDialog(mContext,"No Internet Connection...");
            }
        });


        // setting listener on mImgAddMedia to open Dialog to choose media type and get selected files(image or video)
        mImgAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"mImgAddMedia clicked");
                if (mPermissionHelper.isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    selectMediaAndSend(DialogChatChooseMediaType.SELECT_FROM_DEVICE);
                else
                {
                    String message = "Please give permission to select media from device storage...";
                    mPermissionHelper.requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,message);
                }
            }
        });

        // setting listener on mImgAddMedia to open Dialog to choose media type and get selected files(image or video)
        mImgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"camera clicked");
                if (mPermissionHelper.isPermissionGranted(android.Manifest.permission.CAMERA))
                {
                    //permission to write captured image
                    if (mPermissionHelper.isPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        selectMediaAndSend(DialogChatChooseMediaType.SELECT_FROM_CAMERA);
                    else
                    {
                        String message = "Please give permission to select media from device storage...";
                        mPermissionHelper.requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,message);
                    }
                }
                else
                {
                    String message = "Please give permission to capture image or video from camera...";
                    mPermissionHelper.requestPermission(android.Manifest.permission.CAMERA,message);
                }
            }
        });


        mToolbarImgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_ADD_CONTACT, "Add User")
                        .addOption(BottomSheetOption.OPTION_DELETE, "Remove User");

                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option)
                    {
                        Intent intent = new Intent(mContext, ActivityChatGroupProfile.class);
                        intent.putExtra(ActivityChatGroupProfile.KEY_CHAT_DATA, mChattingTo);
                        switch (option)
                        {
                            case BottomSheetOption.OPTION_ADD_CONTACT:
                                startActivity(intent);
                                break;

                            case BottomSheetOption.OPTION_DELETE:
                                startActivity(intent);
                                break;
                        }
                    }
                });

                options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
                /*DialogChatOptionsMenu dialogChatOptionsMenu = new DialogChatOptionsMenu();
                dialogChatOptionsMenu.setOnItemClickListener(new DialogChatOptionsMenu.ChatOptionsMenuListener() {
                    @Override
                    public void onOptionsMenuItemClicked(int itemType)
                    {
                        Intent intent = new Intent(mContext, ActivityChatGroupProfile.class);
                        intent.putExtra(ActivityChatGroupProfile.KEY_CHAT_DATA, mChattingTo);
                        startActivity(intent);
                    }
                });
                dialogChatOptionsMenu.show(getSupportFragmentManager(),"");*/
            }
        });
    }


    //to select media from device or camera and sending chat list and server
    private void selectMediaAndSend(int selectFrom)
    {
        final DialogChatChooseMediaType dialogChatChooseMediaType = DialogChatChooseMediaType.getInstance(selectFrom);
        dialogChatChooseMediaType.setOnMediaFilesReceivedListener(new DialogChatChooseMediaType.OnSelectedMediaFilesReceiveListener()
        {
            @Override
            public void onSelectedMediaReceived(final List<String> listSelectedImagesPath, String mediaType)
            {
                dialogChatChooseMediaType.dismiss();
                Log.i(TAG,"total images = "+listSelectedImagesPath.size());
                if (listSelectedImagesPath != null)
                {
                    if (listSelectedImagesPath.size() >0)
                    {

                        Log.i(TAG,"adding chat, first position = "+mAdapter.getItemCount());
                        // getting first position of files which are being added to update chat list after uploading is complete
                        int firstFilePositionInAdapter = mAdapter.getItemCount();
                        for (int i=0; i<listSelectedImagesPath.size(); i++)
                        {
                            final Chat mChatSent = new Chat();

                            mChatSent.setMessageToUserId(mChattingTo.getChattingToId());
                            // adding media to show in chat list while uploading
                            mChatSent.setMessage(listSelectedImagesPath.get(i));// if type is message then file would be empty,  file will be used to send image or video
                            mChatSent.setMessageType(mediaType); // setting file type, type may be Message, Image or Video
                            // adding listSelectedImagePath(media list) to send on server
                            mChatSent.setFileArray(listSelectedImagesPath.toArray(new String[0])); // if type is message then file would be empty,  file will be used to send image or video
                            //# adding first position of files to update chat list after uploading is complete
                            mChatSent.setFileUploadingFirstPositionInAdapter(firstFilePositionInAdapter);
//                            mChatSent.setMessageChatType(Chat.CHAT_TYPE_ONE_TO_ONE);
                            mChatSent.setMessageChatType(mChattingTo.getMessageChatType());


                            mChatSent.setSendingMessage(true);
                            mChatSent.setMessage(listSelectedImagesPath.get(i)); // setting image path in message to show in adapter

                            // # setting user details
                            User user = SessionHelper.getInstance(mContext).getUser();
                            mChatSent.setMessageFromUserId(user.getUserId());
                            mChatSent.setMessageFromUserName(user.getName());
                            mChatSent.setMessageFromUserPicUrl(user.getUserPicUrl());

                            // # setting date and time
                            DateTimeHelper dateTimeHelper = new DateTimeHelper();
                            Date dateTime = Calendar.getInstance().getTime();
                            Log.i(TAG,"current date time = "+dateTime);
                            mChatSent.setMessageDate(dateTimeHelper.getDateStringDayMonthYear(dateTime));
                            mChatSent.setMessageDateLong(dateTimeHelper.getDateDayMonthYear(dateTime).getTime());
                            mChatSent.setMessageTime(dateTimeHelper.getTime(dateTime));

                            if (!Chat.CHAT_TYPE_ONE_TO_ONE.contains(mChattingTo.getMessageChatType()))
                                mChatSent.setGroupType(mChattingTo.getGroupType());  // setting group type
                            else
                                mChatSent.setGroupType("");  // setting group type empty because it's not group

                            //showing file in chat list to show progress bar while uploading
                            onChatReceived(mChatSent);

                            // uploading chat to server
                            if (i == listSelectedImagesPath.size()-1)
                                sendFilesInChat(mChatSent, mChatSent.getFileArray());
                              // mPresenterChat.sendChat(mChatSent);
                        }
                    }
                }
            }
        });
        dialogChatChooseMediaType.show(getSupportFragmentManager(),"");

    }

    // to initialize variables from bundle sent from calling component
    private void initializeFromBundle()
    {
        mChattingTo = new Chat();
        Intent intent = getIntent();
        if (intent != null)
        {
            // getting Chat object and notification id from bundle sent after clicking chat notification
            if (KEY_CHAT_ACTION.equalsIgnoreCase(intent.getAction()))
            {
                // getting chat sent from notification chat helper
                mChattingTo = (Chat) intent.getSerializableExtra(KEY_CHAT);
                /*Chat chatFromNotification = (Chat) intent.getSerializableExtra(KEY_CHAT);
                // setting details in mChattingTo object from notification details (user who had sent message)
                mChattingTo.setChattingToId(chatFromNotification.getMessageFromUserId());
                mChattingTo.setChattingToName(chatFromNotification.getMessageFromUserName());
                mChattingTo.setMessageChatType(chatFromNotification.getMessageChatType());
                mChattingTo.setGroupType(chatFromNotification.getGroupType());
                mChattingTo.setGroupId(chatFromNotification.getGroupId());
                mChattingTo.setAdminId(chatFromNotification.getAdminId());
                mChattingTo.setChattingToPicUrl(chatFromNotification.getChattingToPicUrl());*/
                // getting notification id to dismiss notification
                mNotificationId = intent.getIntExtra(KEY_CHAT_NOTIFICATION_ID, 0);
            }
            // getting chatting data from other page except notification
            else
            {
                Log.i(TAG,"initializing mChattingTo object ");
                mChattingTo = (Chat) intent.getSerializableExtra(KEY_CHATTING_TO_DATA);
            }
        }
        else
            Log.e(TAG,"intent is null in initializeFromBundle() method ");
    }

    private void init() {
        mListenerChat = this;
        mContext = this;
        mPresenterChat = new PresenterChat(mContext) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };
        mPermissionHelper = new PermissionHelper(mContext);
        //mDialogLoader = DialogLoader.getInstance("Loading...");

        mToolbarImgOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarImgProfile = findViewById(R.id.toolbar_img_profile);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbarProfileLayout = findViewById(R.id.toolbar_layout_profile);
        mRefreshLayout = findViewById(R.id.chat_refresh_layout);
        mEditMessage = findViewById(R.id.chat_edit_message);
        mImgSendMessage = findViewById(R.id.chat_img_send);
        mImgCamera = findViewById(R.id.chat_img_camera);
        mImgAddMedia = findViewById(R.id.chat_add_media);
      //  mProgressSendMessage = findViewById(R.id.chat_progress_bar_send_message);

    }


    //to dismiss notification in notification tay after clicking chat
    private void dismissNotificationAndClearPreferenceChat()
    {
        Log.i(TAG,"dismissNotification() method called , NOTIFICATION_ID = "+mNotificationId);
        NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (manager != null)
            if (mNotificationId != 0)
            {
                Log.i(TAG,"canceling notification");
                manager.cancel(mNotificationId);

                // removing user details from PreferenceChat
                if (mChattingTo != null)
                new PreferenceChat(mContext, mChattingTo.getChattingToId()).clearUserChatDetails();
            }
            else
                Log.e(TAG,"notificationManager is null in dismissNotification() method ");
    }



    private void requestChatList()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            final Chat chat = new Chat();
            chat.setMessageFromUserId(SessionHelper.getInstance(mContext).getUser().getUserId()); // fromUserId is my id

            if (mChattingTo != null)
            {
                chat.setChattingToId(mChattingTo.getChattingToId());
//                chat.setMessageChatType(Chat.CHAT_TYPE_ONE_TO_ONE);
                chat.setMessageChatType(mChattingTo.getMessageChatType());

                // removing notification from notification tray
                dismissNotificationAndClearPreferenceChat();
            }
            else
                return;

            mRefreshLayout.setRefreshing(true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPresenterChat.requestChat(chat);
                }
            }).start();

        }
        else
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
    }


    // setting adapter
    private void setChatAdapter()
    {
        mRecyclerView = findViewById(R.id.recycler_chat);
        layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterChat(mContext, mPresenterChat, getSupportFragmentManager());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    // setting toolbar
    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        if (mChattingTo != null)   // setting toolbar title and profile pic
        {
            mToolbarTxtTitle.setText(mChattingTo.getChattingToName());
            showProfileImage(mChattingTo.getChattingToPicUrl());
            Log.i(TAG,"toolbar profile pic url = "+mChattingTo.getChattingToPicUrl());

            // if chat is not  one to one and then there will be options menu icon to add , remove users.
            if (mChattingTo.getGroupAdminId() != null)
            {
                boolean isLoggedUserAdmin = mChattingTo.getGroupAdminId().equals(SessionHelper.getInstance(mContext).getUser().getUserId());
                if (!mChattingTo.getMessageChatType().equals(Chat.CHAT_TYPE_ONE_TO_ONE) && isLoggedUserAdmin )
                {
                    if (mToolbarImgOptionsMenu != null)
                        mToolbarImgOptionsMenu.setVisibility(View.VISIBLE);
                }
            }

        }


        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    private void showProfileImage(String url)
    {
        mToolbarImgProfile.setVisibility(View.VISIBLE);
        int picDimen = (int)getResources().getDimension(R.dimen.toolbarImgProfile);
        int picSize = UtilHelper.convertDpToPixel(mContext,picDimen);
        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                .apply(new RequestOptions().error(R.drawable.img_default_black))
                .apply(new RequestOptions().override(picSize, picSize))
                .apply(new RequestOptions().centerCrop())
                .into(mToolbarImgProfile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_GROUP_PROFILE_ACTIVITY == requestCode)
        {
            if (resultCode == RESULT_OK)
            {
                Log.i(TAG,"result ok");
                Chat chat = (Chat) data.getExtras().getSerializable(ActivityChatGroupProfile.KEY_CHAT_DATA);
                boolean finishActivity = data.getExtras().getBoolean(ActivityChatGroupProfile.KEY_FINISH_CHAT_ACTIVITY);
                mIsProfileChanged = data.getExtras().getBoolean(ActivityChatGroupProfile.KEY_IS_GROUP_PROFILE_CHANGED);

                if (chat != null)
                {
                    if (mToolbarTxtTitle != null)
                        mToolbarTxtTitle.setText(chat.getChattingToName());
                    // showing new group pic
                    showProfileImage(chat.getChattingToPicUrl());  // showing new changed profile pic

                    // #setting new group name, pic to mChattingTO object
                    mChattingTo.setChattingToName(chat.getChattingToName()); // setting new changed name
                    mChattingTo.setChattingToPicUrl(chat.getChattingToPicUrl());  // setting new changed proifle pic
                }

                if (finishActivity)
                    finish();


            }
            else
                Log.e(TAG,"cancelled");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }




    @Override
    protected void onStop()
    {
        super.onStop();
    //    ActivityHelper.getInstance().setChatVisible(false);  // to make sure ActivityChat is visible only once

        if (mChattingTo != null)
            PreferenceChat.clearingChattingStatus(mContext);
        else
            Log.e(TAG,"mChattingTO is null in onStop() method ");

        try
        {
            unregisterReceiver(mChatReceiver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }




    @Override
    public void onChatReceived(final Chat chat)
    {
        Log.i(TAG,"onChatReceived() method called, position = "+chat.getPositionInAdapter());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addChatToList(chat);
                //  Log.i(TAG,"total chat count = "+mAdapter.getItemCount());
              //  mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                onHideProgress();
            }
        });

    }



    // this method is called to update chat list when chat is sent successfully, chat argume
    @Override
    public void onChatSendSuccess(final Chat chat)
    {
        Log.i(TAG,"onChatSendSuccess() method called , position = "+chat.getPositionInAdapter());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chat.setSendingMessage(false);  // setting sendMessage false to hide progress bar shown while sending chat
                mAdapter.updateChat(chat);
                mEditMessage.setText("");  // clearing EditText

               /* //   Log.i(TAG,"total chat count = "+mAdapter.getItemCount());
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);  //scrolling recyclerview to bottom*/
            }
        });

    }


    // this method is called to show previous messages of some specified days
    @Override
    public void onPreviousChatReceived(final Chat chat)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.showPreviousMessages(chat);
                onHideProgress();
            }
        });
    }


    // this method is called when chat file is sent successfully
    @Override
    public void onFileUploaded(final Chat chat, final int startPosition)
    {
        Log.i(TAG,"onFileUploaded() method called updating list, position = "+startPosition);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chat.setSendingMessage(false);
                mAdapter.onFilesUploaded(chat, startPosition);
            }
        });
    }

    @Override
    public void onFailed(final Chat chat) {
        // when sending message is complete, whether it's sent successfully or not
        Log.i(TAG,"onFailed() method called, position = "+chat.getPositionInAdapter()+" , count = "+mAdapter.getItemCount());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*mProgressSendMessage.setVisibility(View.GONE);
                mImgSendMessage.setVisibility(View.VISIBLE);*/
                onHideProgress();
                if (mAdapter != null && chat != null)
                {
                    Log.e(TAG,"send chat failed, now removing chat from list");
                    chat.setSendingMessage(false); // # setting message sent false
                    mAdapter.removeChatFromList(chat);
                }
                else
                    Log.e(TAG,"onFailed() method called mAdapter or chat is null= ");

            }
        });

    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onBackPressed() {

        // setting result to send back to calling activity to check
        if (mChattingTo != null)
        {
            String chatType =  mChattingTo.getMessageChatType();
            Log.i(TAG,"onBackPressed chatType = "+chatType);
            if (Chat.CHAT_TYPE_GROUP_CHAT.equals(chatType) || Chat.CHAT_TYPE_INJURY_MANAGEMENT.equals(chatType))
            {
                Intent intent = new Intent();
                // Log.i(TAG, "Setting result");
                intent.putExtra(KEY_IS_SOMETHING_CHANGED, mIsProfileChanged);
                Log.i(TAG,"is profile changed  = "+mIsProfileChanged);
                if (mIsProfileChanged)
                    setResult(RESULT_OK,intent);
                else
                    setResult(RESULT_CANCELED, intent);
            }

        }


        super.onBackPressed();
    }

    @Override
    public void onHideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onShowMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onHideProgress();
                DialogHelper.showSimpleCustomDialog(mContext,message);
            }
        });

    }



    // BroadcastReceiver to receive chat from push notification, it will update chat list
    BroadcastReceiver mChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                Chat chat = (Chat) bundle.getSerializable("chat"); // getting Chat object from bundle
                if (chat !=  null)
                    onChatReceived(chat);  // updating chat list
                else
                    Log.e(TAG,"chat is null in chatReceiver");
            }
            else
                Log.e(TAG,"chat is null in chatReceiver");
        }
    };




















    // this method sends image or video file
    private void sendFilesInChat(final Chat chat, final String[] filesPath)
    {
        Log.i(TAG,"sendFilesInChat() method called");
        /*if (true)
        {
            int firstPositionInAdapter = chat.getFileUploadingFirstPositionInAdapter();
            for (int i=0; i<filesPath.length; i++)
            {
                chat.setPositionInAdapter(firstPositionInAdapter);
                //   Log.i(TAG,"file uploaded successfully , position in adapter = "+positionInAdapter+"\n image url = "+chatUpdateList.getMessage());
                // listener.onFileUploaded(chatUpdateList, firstPositionInAdapter);
                listener.onChatSendSuccess(chat);
                firstPositionInAdapter++;
            }
            //  chat.setPositionInAdapter(chat.getFileUploadingFirstPositionInAdapter());
            // chat.setSendingMessage(false);
            //onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
            onChatSendSuccess(chat);

            return;
        }*/

        String uuid = UUID.randomUUID().toString();
        try
        {
           /* Log.i(TAG,"Sending file in chat Param******************************************************************");
            Log.i(TAG,"send_msg = "+"1");
            Log.i(TAG,"from_id = "+chat.getMessageFromUserId());
            Log.i(TAG,"to_id = "+chat.getMessageToUserId());
            Log.i(TAG,"message = "+"");
            Log.i(TAG,"msg_type = "+chat.getMessageType());
            Log.i(TAG,"chat_type = "+chat.getMessageChatType());
            Log.i(TAG,"group_type "+chat.getGroupType()); // setting group type, if it's not fro group then group_type would be empty("").
*/

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

            for(String filePath : filesPath)
            {
                // Log.i(TAG,"addingFileToUpload, file = "+filePath);
                uploadRequest.addFileToUpload(filePath,"file[]");
                //   Log.i(TAG,"file[] = "+filePath);

            }


            uploadRequest.addParameter("send_msg","1");
            uploadRequest.addParameter("from_id",chat.getMessageFromUserId());
            uploadRequest.addParameter("to_id",chat.getMessageToUserId());
            uploadRequest.addParameter("message",""); // when file is being sent then we don't need to send message
            uploadRequest.addParameter("msg_type",chat.getMessageType());
            uploadRequest.addParameter("chat_type", chat.getMessageChatType());
            uploadRequest.addParameter("group_type", chat.getGroupType()); // setting group type, if it's not from group then group_type would be empty("").

            uploadRequest.setNotificationConfig(new UploadNotificationConfig().setRingToneEnabled(false));
            uploadRequest.setAutoDeleteFilesAfterSuccessfulUpload(true);
            uploadRequest.setMaxRetries(3);
            uploadRequest.setDelegate(new UploadStatusDelegate()
            {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                         /*   Log.i(TAG,"progress getProgressPercent = "+uploadInfo.getProgressPercent());
                            Log.i(TAG,"progress getSuccessfullyUploadedFiles = "+uploadInfo.getSuccessfullyUploadedFiles());
                            Log.i(TAG,"progress getUploadRateString = "+uploadInfo.getUploadRateString());
                            Log.i(TAG,"progress getUploadRate = "+uploadInfo.getUploadRate());*/
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception)
                {
                    Log.e(TAG,"failed uploading file to server , "+exception.getMessage());
                    // listener.onFailed(chat);
                    exception.printStackTrace();
                    //  DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
                    onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse)
                {
                    //  DialogHelper.showSimpleCustomDialog(mContext,"File Uploaded Successfully...");
                    String responseString = serverResponse.getBodyAsString();
                    Log.i(TAG,"file upload response getBodyAsString = "+responseString);

                    try
                    {
                        JSONObject objectData = new JSONObject(responseString);
                        if (objectData != null)
                        {
                            if ("1".equals(objectData.getString("res")))
                            {
                                JSONObject object = objectData.getJSONObject("message");

                                //  Chat chatFromServer = new Chat();
                                String id = object.getString("data_id");
                                String fromUserId = object.getString("from_id");
                                String toUserId = object.getString("to_id");
                                String messageType = object.getString("msg_type");
                                String fromName = object.getString("from_name");
                                String profilePic = object.getString("profile_image");

                                // setting date and time
                                String dateTimeFromServer = object.getString("date_time");
                                DateTimeHelper mDateTimeHelper = new DateTimeHelper();

                                String date = mDateTimeHelper.getDateStringDayMonthYear(dateTimeFromServer);
                                String time = mDateTimeHelper.getTime(dateTimeFromServer);
                                long dateLong = mDateTimeHelper.getDateDayMonthYear(dateTimeFromServer).getTime();

                                int firstPositionInAdapter = chat.getFileUploadingFirstPositionInAdapter();
                                // arrayUrls contains array of urls of uploaded file
                                JSONArray arrayUrls = object.getJSONArray("urls");
                                if (arrayUrls != null)
                                {
                                    // listener.onFileUploaded(chatFromServer, chat.getFileUploadingFirstPositionInAdapter());

                                    for (int i=0; i<arrayUrls.length(); i++)
                                    {
                                        Chat chatUpdateList = new Chat();
                                        JSONObject objectUrl = arrayUrls.getJSONObject(i);

                                        chatUpdateList.setId(id);
                                        chatUpdateList.setMessageFromUserId(fromUserId);
                                        chatUpdateList.setMessageToUserId(toUserId);
                                        chatUpdateList.setMessageType(messageType);
                                        chatUpdateList.setMessageFromUserName(fromName);
                                        chatUpdateList.setMessageFromUserPicUrl(profilePic);
                                        chatUpdateList.setMessageDate(date);
                                        chatUpdateList.setMessageTime(time);
                                        chatUpdateList.setMessageDateLong(dateLong);

                                        // setting file url
                                        chatUpdateList.setMessage(objectUrl.getString("description")); // description contains, message, image url and video url
                                        chatUpdateList.setSendingMessage(false);  // setting fileUploading false because file has been uploaded successfully

                                        chatUpdateList.setPositionInAdapter(firstPositionInAdapter);
                                        //   Log.i(TAG,"file uploaded successfully , position in adapter = "+positionInAdapter+"\n image url = "+chatUpdateList.getMessage());
                                        // listener.onFileUploaded(chatUpdateList, firstPositionInAdapter);
                                        mListenerChat.onChatSendSuccess(chatUpdateList);
                                        firstPositionInAdapter++;
                                    }


                                }
                                else
                                    onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
                            }
                            else
                                onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
                        }
                        else
                            onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
                    }
                    catch (JSONException e)
                    {
                        onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    DialogHelper.showSimpleCustomDialog(mContext,"Cancelled...");
                    onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
                }
            });
            Log.i(TAG,"starting uploading file");
            uploadRequest.startUpload();

        }
        catch (FileNotFoundException e)
        {
            Log.e(TAG,"FileNotFoundException in uploadFile() method = "+e.getMessage());
            onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);

            e.printStackTrace();
        } catch (MalformedURLException e)
        {
            onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), filesPath.length);
            Log.e(TAG,"MalformedURLException in uploadFile() method = "+e.getMessage());
            e.printStackTrace();
        }
    }


    // to remove files from chat list if uploading file gets failed
    private void onUploadingFileFailed(int firstPosition, int totalFiles)
    {
        // int firstPosition = chat.getFileUploadingFirstPositionInAdapter();
        // firstPosition = firstPosition+totalFiles;
        for (int i=0; i<totalFiles; i++)
        {
            Chat chat = new Chat();
            chat.setPositionInAdapter(firstPosition);
            chat.setSendingMessage(false);
            mListenerChat.onFailed(chat);
            //  firstPosition--;
        }
    }




   /* ContractGroupChatInjury.Presenter presenterGroupChatInjury = new PresenterGroupChatInjury(mContext, new ContractGroupChatInjury.View()
    {
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
    });*/




}
