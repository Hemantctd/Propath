package org.ctdworld.propath.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNotification;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.Collections;
import java.util.List;



 /*# Earlier this activity was being used to show only request status, but now it will be used for all notifications so some code
 will have to be changed later. For now code has been done only for request and request response*/
public class ActivityNotification extends BaseActivity implements ContractRequest.View
{

    // # Views
    private RecyclerView mRecyclerView;  // to show request data list
    private Toolbar mToolbar;  // toolbar
    private TextView mToolbarTxtTitle;  // toolbar title
    private TextView mTxtNoConnection;  // to show messages like no internet connection
    private SwipeRefreshLayout mRefreshLayout;  // to reload data from server
    private View mLayoutDetails;   // layout which contains all data



    // # Variables
    private final String TAG = ActivityNotification.class.getSimpleName();
    private ContractRequest.Presenter mPresenter;
    private Context mContext;  // context
    private Request.Data mRequestData;
    private AdapterNotification mAdapter;  // to show request list and notification data



    // # keys to set data in bundle
    public static final String KEY_REQUEST_DATA = "request data";   // key to put Request.Data object, to respond to request



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Log.i(TAG,"onCreate() method called ");
        init();
        setToolbar();
        prepareAdapter();   // preparing adapter to show notifications and requests.
        showData();  // showing data


        // # setting listeners
        mRefreshLayout.setOnRefreshListener(onRefreshingLayout);

    }


    // initializing
    private void init()
    {
        mContext = this;
        mPresenter = new PresenterRequest(mContext, this);
        mRefreshLayout = findViewById(R.id.connection_request_status_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_notification);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbar = findViewById(R.id.toolbar);
        mLayoutDetails = findViewById(R.id.connection_request_status_layout_details);
        mTxtNoConnection = findViewById(R.id.connection_request_status_txt_no_connections);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mRequestData = (Request.Data) bundle.getSerializable(KEY_REQUEST_DATA);
    }




    // to load and show data from server, also responds to request if this activity has been opened through notification
    private void showData()
    {
        requestConnectionRequestStatusList();

        if (mRequestData == null)
            return;


        dismissNotification();  // to dismiss notification
        switch (mRequestData.getNotificationType())
        {
            case Request.NOTIFICATION_TYPE_REQUEST:
                respondToRequest();  // responding to request
                break;

                //to show simple message
                case Request.NOTIFICATION_TYPE_SIMPLE_MESSAGE:

                    break;
        }
    }




    // requesting to get all connection request status from server
    private void requestConnectionRequestStatusList()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                mRefreshLayout.setRefreshing(true);
                mPresenter.getAllRequests();
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error while requesting for connection request status list , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            mLayoutDetails.setVisibility(View.GONE);
            mTxtNoConnection.setVisibility(View.VISIBLE);
            mRefreshLayout.setRefreshing(false);
        }
    }




    
    // preparing adapter to show all notifications and requests
    private void prepareAdapter()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AdapterNotification(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }




    // setting toolbar
    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTxtTitle.setText(getString(R.string.notifications));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        setSupportActionBar(mToolbar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }




    // responding to request of any type depending on the button clicked in notification tray
    private void respondToRequest()
    {
        Log.i(TAG,"click action = "+getIntent().getAction());
        if (mRequestData != null)
            mPresenter.respondToRequest(mRequestData);  // responding to request of any type
    }

    
    
    
    

    //to dismiss notification in notification tay after clicking accept or reject button
    private void dismissNotification()
    {
        if (mRequestData == null)
            return;

        NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        if (manager != null && mRequestData.getNotificationId() <= 0)
            manager.cancel(mRequestData.getNotificationId());
    }



    // refreshing to load data from server
    private SwipeRefreshLayout.OnRefreshListener onRefreshingLayout = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            requestConnectionRequestStatusList();
        }
    };




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }




    @Override
    public void onRequestSentSuccessfully() 
    {

    }


    @Override
    public void onReceivedAllRequests(List<Request.Data> requestList) {
        Log.i(TAG,"requestList size = "+ requestList.size());
        Collections.reverse(requestList);
        mAdapter.addRequestDataList(requestList);
        mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRespondedSuccessfully() {
// requesting all notifications after response to request is complete,
        if (mRequestData == null)
            return;

        requestConnectionRequestStatusList();
        switch (mRequestData.getRequestStatus())
        {
            case Request.REQUEST_STATUS_ACCEPT:
                DialogHelper.showSimpleCustomDialog(mContext,"Request Accepted");
                break;
            case Request.REQUEST_STATUS_REJECT:
                DialogHelper.showSimpleCustomDialog(mContext,"Request Rejected");
                break;
            case Request.REQUEST_STATUS_PENDING:
                DialogHelper.showSimpleCustomDialog(mContext,"Request Pending");
                break;
        }
    }



    @Override
    public void onFailed(String message) {
        hideLoader();
        mLayoutDetails.setVisibility(View.GONE);
        mTxtNoConnection.setVisibility(View.VISIBLE);
        mTxtNoConnection.setText(getString(R.string.failed_title));
        mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onShowMessage(String message) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext, message);
    }



}
