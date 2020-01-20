package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterEducationAthleteReviewList;
import org.ctdworld.propath.adapter.AdapterEducationSelfReviewList;
import org.ctdworld.propath.contract.ContractSelfReview;
import org.ctdworld.propath.model.SelfReview;
import org.ctdworld.propath.presenter.PresenterSelfReview;
import java.util.List;

public class ActivitySelfReviewListForOtherRoles extends AppCompatActivity implements ContractSelfReview.View {

    private final String TAG = ActivitySelfReviewListForOtherRoles.class.getSimpleName();
    Toolbar mToolbar;
    ImageView mImgToolbarOptionsMenu,toolbar_img_options_menu;
    TextView mToolbarTitle;
    Context mContext;
    RecyclerView recycler_athlete_progress_list;
    AdapterEducationAthleteReviewList adapter;
    String athlete_ids;
    TextView no_data_found;
    AdapterEducationSelfReviewList mAdapter;
    ContractSelfReview.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_review_list_for_other_roles);
        init();
        setToolBar();
        setProgressReportAdapter();

        Intent i = getIntent();
        athlete_ids = i.getStringExtra(ActivityEducationSelfReviewReport.ATHLETE_ID);
        Log.d(TAG,"self review athlete_ids -----> " +athlete_ids);

        mPresenter.getSelfReview(athlete_ids);

    }

    public void init()
    {
        mContext = this;
        mPresenter = new PresenterSelfReview(mContext,this);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        recycler_athlete_progress_list = findViewById(R.id.recycler_athlete_progress_list);
        no_data_found = findViewById(R.id.no_data_found);


    }

    private void setToolBar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.self_review);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }

    public void setProgressReportAdapter()
    {
        //getSelfReviewList();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterEducationSelfReviewList(mContext,null);
        recycler_athlete_progress_list.setLayoutManager(mLayoutManager);
        recycler_athlete_progress_list.setAdapter(mAdapter);


    }

   /* public void getSelfReviewList() {

        HashMap<String,String> params = new HashMap<>();

        params.put("get_self_review","1");
        params.put("user_id",athlete_ids);

        Log.d(TAG,"params : " + params);
        RemoteServer remoteServer = new RemoteServer(mContext);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                System.out.println("response : " + message);

                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res"))) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HashMap<String, String> progressListData = new HashMap<String, String>();

                            progressListData.put("Review_ID", jsonObject1.get("id").toString());
                            progressListData.put("Date", jsonObject1.get("date").toString());

                            Log.d(TAG, "Review_ID" + jsonObject1.get("id").toString());

                            mProgressList.add(progressListData);

                        }
                    }
                    if ("0".equals(jsonObject.getString("res")))
                    {
                        no_data_found.setVisibility(View.VISIBLE);
                    }


                    Log.d(TAG,"mProgress List : " + mProgressList);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                  //  AdapterEducationSelfReviewList mAdapter = new AdapterEducationSelfReviewList(mContext,mProgressList,athlete_ids, null);
                    recycler_athlete_progress_list.setLayoutManager(mLayoutManager);
                  //  recycler_athlete_progress_list.setAdapter(mAdapter);


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

                DialogHelper.showCustomDialog(mContext,error.toString());

            }
        });

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public void onSelfListDeleted(String id) {

    }

    @Override
    public void onReceivedSelfReportList(List<SelfReview> selfReviewList) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.setData(selfReviewList,athlete_ids);
    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {

    }

    @Override
    public void onShowMessage(String message) {

    }
}
