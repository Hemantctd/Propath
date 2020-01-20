package org.ctdworld.propath.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCoachFeedbackCoachView;
import org.ctdworld.propath.adapter.AdapterCoachViewFeedback;
import org.ctdworld.propath.contract.ContractCoachFeedback;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterCoachFeedback;
import java.util.List;

public abstract class FragmentCoachViewFeedback extends Fragment implements ContractCoachFeedback.View
{
    private final  String TAG = FragmentCoachViewFeedback.class.getSimpleName();

    Context mContext;
    public Toolbar mToolbar;
    public TextView mTxtToolbarTitle;
    private RecyclerView mRecyclerView;
    AdapterCoachViewFeedback mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
   // ArrayList<HashMap<String,String>> coachFeedbackData = new ArrayList<>();

    private String athlete_id;
   // List<CoachData> mCoachDataList;
    ContractCoachFeedback.Presenter mPresenter;
    Intent intent;
 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_coach_view_feedback);

    }
*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_view_feedback, container, false);

        Log.i(TAG, "onCreateView method called ");


        if (getActivity() != null)
            intent = getActivity().getIntent();


        String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String athleteRoleId= RoleHelper.ATHLETE_ROLE_ID;

        if (loggedInUser.equals(athleteRoleId)) {
            athlete_id = SessionHelper.getInstance(mContext).getUser().getUserId();
        }
        else
        {
            athlete_id = intent.getStringExtra("athlete_id");
        }
        Log.d(TAG,"athlete_id : " + athlete_id);


        init(view);
        //prepareToolbar();
        prepareRecyclerView();
        String loggedInRole = SessionHelper.getInstance(mContext).getUser().getRoleId();
       // String coachRoleId = RoleHelper.COACH_ROLE_ID;
      //  String athleteRoleId = RoleHelper.ATHLETE_ROLE_ID;

            mPresenter.getCoachFeedback(athlete_id,loggedInRole);
        return view;
    }

    private void prepareRecyclerView()
    {
        mAdapter = new AdapterCoachViewFeedback(mContext, adapterListener);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

       // getCoachFeedbackList();
    }
    private void init(View view)
    {
        mContext = getContext();
        mPresenter = new PresenterCoachFeedback(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mToolbar = view.findViewById(R.id.toolbar);
        mTxtToolbarTitle = view.findViewById(R.id.toolbar_txt_title);
        mRecyclerView = view.findViewById(R.id.recycler_view_career_feedback_athlete_view);



    }


    private AdapterCoachViewFeedback.Listener adapterListener = new AdapterCoachViewFeedback.Listener() {
        @Override
        public void onCoachFeedbackOptionClicked(final String reviewId, final String athleteId) {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();

                   final String loggedinUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
                   final String coachRoleId = RoleHelper.COACH_ROLE_ID;
                    final String athleteRoleId = RoleHelper.ATHLETE_ROLE_ID;

            if (loggedinUser.equals(coachRoleId)) {
                builder.addOption(BottomSheetOption.OPTION_DELETE, "Delete");
                builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit");
            }
            else if (loggedinUser.equals(athleteRoleId)) {
                builder.addOption(BottomSheetOption.OPTION_DELETE, "Delete");
            }

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {

                    switch (option)
                    {
                        case BottomSheetOption.OPTION_DELETE:
                           /* if (loggedinUser.equals(coachRoleId))
                            {*/
                                mPresenter.deleteCoachFeedback(reviewId,loggedinUser);
                           /* }
                            else if (loggedinUser.equals(athleteRoleId))
                            {
                                mPresenter.deleteCoachFeedback(reviewId,athleteRoleId);
                            }*/
                          //  mPresenter.deleteCoachFeedback(reviewId,coachRoleId);
                            break;

                        case BottomSheetOption.OPTION_EDIT:
                            Intent intent = new Intent(mContext, ActivityCoachFeedbackCoachView.class);
                            intent.putExtra(ActivityCoachFeedbackCoachView.KEY_ATHLETE_ID, athleteId);
                            intent.putExtra(ActivityCoachFeedbackCoachView.KEY_REVIEW_ID, reviewId);
                            intent.putExtra(ActivityCoachFeedbackCoachView.KEY_MODE_CREATE_OR_EDIT, ActivityCoachFeedbackCoachView.VALUE_MODE_EDIT);
                            startActivity(intent);

                            break;
                    }

                }
            });
            if (getFragmentManager() != null) {
                options.show(getFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            }
        }
    };

    @Override
    public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {
      //  onHideProgress();
      //  this.mCoachDataList = coachDataList;

        if (mAdapter != null)
            mAdapter.setData(coachDataList,athlete_id);
    }

    @Override
    public void onReceivedCoachFeedbackDescription(CoachData coachData) {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
    }

    @Override
    public void onCoachFeedbackDeleted(String id) {
        if (mAdapter != null)
            mAdapter.onDeletedSuccessfully(id);
    }

    @Override
    public void onReceivedCoachSelfReview(List<CoachData> coachData) {

    }

    @Override
    public void onReceivedCoachSelfReviewDescription(CoachData coachData) {

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


    /*public void getCoachFeedbackList() {

        HashMap<String, String> params = new HashMap<>();

        params.put("get_all_feedback_list", "1");
        params.put("user_id", athlete_id);

        Log.d(TAG, "params : " + params);
        RemoteServer remoteServer = new RemoteServer(mContext);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
              //  Log.d(TAG,"response : " + message);

                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res"))) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            HashMap<String, String> coachListData = new HashMap<String, String>();
                            Log.d(TAG,"coachListData : " + message);

                            String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
                            String coachRoleId = RoleHelper.COACH_ROLE_ID;

                            if (loggedInUser.equals(coachRoleId))
                            {
                                if (jsonObject1.get("coach_id").equals(SessionHelper.getInstance(mContext).getUser().getUserId())) {
                                    coachListData.put("Review_ID", jsonObject1.get("id").toString());
                                    coachListData.put("Date", jsonObject1.get("date_time").toString());
                                    coachListData.put("Coach_Name", jsonObject1.get("user_name").toString());

                                    coachFeedbackData.add(coachListData);
                                }
                            }
                            else {
                                coachListData.put("Review_ID", jsonObject1.get("id").toString());
                                coachListData.put("Date", jsonObject1.get("date_time").toString());
                                coachListData.put("Coach_Name", jsonObject1.get("user_name").toString());

                                coachFeedbackData.add(coachListData);

                            }


                        }
                    }
                    if ("0".equals(jsonObject.getString("res"))) {
                        // no_data_found.setVisibility(View.VISIBLE);
                    }


                    Log.d(TAG, "mCoachFeedback List : " + coachFeedbackData);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    mRecyclerAdapter = new AdapterCoachViewFeedback(mContext, coachFeedbackData,athlete_id);
                    mLayoutManager = new LinearLayoutManager(mContext);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mRecyclerView.setAdapter(mRecyclerAdapter);


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

                DialogHelper.showCustomDialog(mContext, error.toString());

            }
        });
    }*/
}
