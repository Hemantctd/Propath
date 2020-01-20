package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationForAthlete;
import org.ctdworld.propath.activity.ActivityEducationSelfReviewCreate;
import org.ctdworld.propath.adapter.AdapterProgressReportAdapter;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEducationSelfReviewAthleteList extends Fragment {

    private final String TAG = FragmentEducationSelfReviewAthleteList.class.getSimpleName();

    Context mContext;
    RecyclerView recycler_athlete_progress_list;
    FloatingActionButton add_progress_report;
    AdapterProgressReportAdapter adapter;
    String athlete_ids;
    ArrayList<HashMap<String,String>> mProgressList = new ArrayList<>();
    TextView no_data_found;
    ProgressBar mProgressBar;
    public FragmentEducationSelfReviewAthleteList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_education_self_review_athlete_list, container, false);
        init(view);
        setListeners();
        setProgressReportAdapter();
        return view;
    }

    public void init(View view)
    {
        mContext = getContext();
        mProgressBar = view.findViewById(R.id.progress_bar);
        add_progress_report = view.findViewById(R.id.add_progress_report);
        recycler_athlete_progress_list = view.findViewById(R.id.recycler_athlete_progress_list);
        no_data_found = view.findViewById(R.id.no_data_found);
        ActivityEducationForAthlete activity = (ActivityEducationForAthlete) getActivity();
        athlete_ids = activity.getAthleteId();
        Log.d(TAG,"self review athlete_ids -----> " +athlete_ids);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    public void setProgressReportAdapter()
    {
        getSelfReviewList();



    }

    public void setListeners()
    {
        add_progress_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(mContext, ActivityEducationSelfReviewCreate.class));
            }
        });
    }

    public void getSelfReviewList() {

        HashMap<String,String> params = new HashMap<>();

        params.put("get_self_review","1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());

        Log.d(TAG,"params : " + params);
        RemoteServer remoteServer = new RemoteServer(mContext);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                System.out.println("response : " + message);
                 mProgressBar.setVisibility(View.GONE);
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
                    AdapterProgressReportAdapter mAdapter = new AdapterProgressReportAdapter(mContext,mProgressList,SessionHelper.getInstance(mContext).getUser().getUserId());
                    recycler_athlete_progress_list.setLayoutManager(mLayoutManager);
                    recycler_athlete_progress_list.setAdapter(mAdapter);


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

                DialogHelper.showCustomDialog(mContext,error.toString());

            }
        });

    }
}
