package org.ctdworld.propath.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterCareerEmploymentData;
import org.ctdworld.propath.contract.ContractEmploymentTools;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.CareerEmploymentData;
import org.ctdworld.propath.presenter.PresenterEmploymentTools;

import java.util.ArrayList;
import java.util.List;

public class ActivityCareerEmploymentData extends AppCompatActivity implements ContractEmploymentTools.View
{
    private final String TAG = ActivityCareerEmploymentData.class.getSimpleName();

    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mRefreshLayout;
    AdapterCareerEmploymentData mAdapterCareerEmploymentData;

    PresenterEmploymentTools mPresenter;

    View mLayoutNoConnection;
  //  TextView mTxtRetry;

    BroadcastReceiver mDownloadReceiver;



    // KEY TO SET DATA PUT EXTRAS
    public static final String KEY_EMPLOYMENT_TYPE = "employment type";
    // EMPLOYMENT TYPES
    public static final int TYPE_APPLICATION_LETTER = 1;
    public static final int TYPE_CURRICULUM_VITAE = 2;
    public static final int TYPE_INTERVIEW_CHECKLIST = 3;

  //  private int mEmploymentType = 0; // it will contain employment type sent from calling component

    String mStrEmploymentCateogry;  // it will contain employment type, data has been assigned in initializeFromBundle() method


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_employment_data);
        initializeFromBundle(); // initializing with data sent from calling component
        init();  // initializing variable and views
        setToolbar();  // setting toolbar data
        setListeners();
        attachAdapter(); // attaching adapter, adapter is being initialized in this method

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEmploymentData();
            }
        });

        mRefreshLayout.setRefreshing(true);
        getEmploymentData(); // this method requests to get EmploymentTools list


    }


    // initializing variables with data sent from calling component
    private void initializeFromBundle()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            int selectedType  = bundle.getInt(KEY_EMPLOYMENT_TYPE);
            switch (selectedType)
            {
                case TYPE_APPLICATION_LETTER:
                    mStrEmploymentCateogry = CareerEmploymentData.CATEGORY_APPLICATION_LETTER;
                    break;

                case TYPE_CURRICULUM_VITAE:
                    mStrEmploymentCateogry = CareerEmploymentData.CATEGORY_CURRICULUM;
                    break;

                case TYPE_INTERVIEW_CHECKLIST:
                    mStrEmploymentCateogry = CareerEmploymentData.CATEGORY_INTERVIEW_CHECKLIST;
                    break;

            }
        }
        else
            Log.e(TAG,"bundle is null in initializeFromBundle() method");
    }


    // initializing variables
    private void init()
    {
        mContext = this;
        mPresenter = new PresenterEmploymentTools(mContext, this);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.career_employment_data_recycler_view);
        mLayoutNoConnection = findViewById(R.id.layout_no_connection);
        //mTxtRetry = findViewById(R.id.layout_no_connection_txt_retry);
        mRefreshLayout = findViewById(R.id.career_employment_data_refresh_layout);
    }


    private void setListeners()
    {
        // retry option has been removed for now
       /* mTxtRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmploymentData();
            }
        });*/
    }





    private void getEmploymentData()
    {
        // first i have to check for which category data is to be shown
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    CareerEmploymentData employmentTools = new CareerEmploymentData();
                    employmentTools.setEmploymentCategory(mStrEmploymentCateogry); // category and other data are to be added here
                    mPresenter.requestEmploymentList(employmentTools);
                }
            }).start();
        }
        else
        {
            mRefreshLayout.setRefreshing(false);
            mRecyclerView.setVisibility(View.GONE);
            mLayoutNoConnection.setVisibility(View.VISIBLE);
        }



    }


    // setting adapter
    private void attachAdapter()
    {
        ArrayList<CareerEmploymentData> dataList = new ArrayList<>();

       /* for (int i = 0 ; i<30; i++)
        {
            CareerEmploymentData data = new CareerEmploymentData();
            data.setFileName("file "+i);
            data.setFileDate("00-00-00");
            data.setFileTime("00:00 am");

            dataList.add(data);
        }*/

        mAdapterCareerEmploymentData = new AdapterCareerEmploymentData(mContext, mPresenter, dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapterCareerEmploymentData);
    }

    private void setToolbar()
    {
        setSupportActionBar(mToolbar);


        // setting toolbar tile according to type
        switch (mStrEmploymentCateogry)
        {
            case CareerEmploymentData.CATEGORY_APPLICATION_LETTER:
                mToolbarTxtTitle.setText("Application Letter");
                break;

            case CareerEmploymentData.CATEGORY_CURRICULUM:
                mToolbarTxtTitle.setText("Curriculum Vitae");
                break;

            case CareerEmploymentData.CATEGORY_INTERVIEW_CHECKLIST:
                mToolbarTxtTitle.setText("Interview Checklist");
                break;
        }


        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(mToolbar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    @Override
    public void onReceivedEmploymentList(final List<CareerEmploymentData> employmentToolsList)
    {
        Log.i(TAG,"EmploymentTools list received, now showing...................");
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                mAdapterCareerEmploymentData.updateList(employmentToolsList);
                mRefreshLayout.setRefreshing(false);
            }
        });

    }


    // this method will not be used because here we are not uploading any data
    @Override
    public void onUploadSuccess() {
    }

    @Override
    public void onFailed(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showSimpleCustomDialog(mContext,message);
                mRefreshLayout.setRefreshing(false);
            }
        });

    }



    // registering receiver for download complete
    @Override
    public void registerReceiver(BroadcastReceiver receiver)
    {
        mDownloadReceiver = receiver;
        registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        Log.i(TAG,"registering receiver for download complete");
    }


    // this method is called when file is deleted successfully, file is being deleted in AdapterCareerEmploymentData adapter
    @Override
    public void onFileDeleted(String message, int positionInAdapter)
    {
       // DialogHelper.showSimpleCustomDialog(mContext,message);
        mAdapterCareerEmploymentData.onFileDeleted(positionInAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDownloadReceiver);
    }
}
