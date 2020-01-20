package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractSelfReview;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.SelfReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorSelfReview implements ContractSelfReview.Interactor {
    private static final String TAG = InteractorSelfReview.class.getSimpleName();

    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;
    private String messages = "";
    private int savedItemCount = 0;


    public InteractorSelfReview(OnFinishedListener listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }

    @Override
    public void saveSelfReview(final List<SelfReview> list) {
        Log.i(TAG, "SelfReview() method called...........................");
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> params = getParamsToGetSelfReview(list.get(i));
            save(params, list.size());
        }

    }


    private Map<String, String> getParamsToGetSelfReview(SelfReview selfReview) {

        Map<String, String> params = new HashMap<>();

        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("comments", "test");
        params.put("add_self_review", "1");
        params.put("label", selfReview.getQualification());
        params.put("subject"+selfReview.getSubject(), selfReview.getGrade());
        params.put("assistance_requested", selfReview.getAssistance());
        params.put("suggestions", selfReview.getSuggestions());
        params.put("improvements_needed", selfReview.getImprovements());
        params.put("strengths",selfReview.getStrengths());

        Log.i(TAG, "params to send self review = " + params);

        return params;
    }

    private void save(Map<String, String> params, final int totalItems) {

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                savedItemCount++;

                try {
                    JSONObject jsonObject = new JSONObject(message.trim());
                    Log.d(TAG, "server message = " + message);


                    if ("1".equals(jsonObject.getString("success"))) {
                        messages = jsonObject.getString("message");
                        mListener.onSuccess(messages);


                    }
                    if ("0".equals(jsonObject.getString("success"))) {
                        messages = jsonObject.getString("message");
                    }


                    if (totalItems == savedItemCount)
                        mListener.onSuccess(messages);

                } catch (JSONException e) {
                    Log.e(TAG, "Error in updateSelfReview() method , " + e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed(messages);
                }

                finally {
                    mListener.onHideProgress();
                    mListener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                savedItemCount++;
                Log.e(TAG, "onVolleyErrorResponse" + error);
                mListener.onHideProgress();
                mListener.onSetViewsEnabledOnProgressBarGone();
                if (totalItems == savedItemCount)
                    mListener.onSuccess(messages);

            }
        });
    }


    @Override
    public void editSelfReview(List<SelfReview> selfReviewList, SelfReview selfReview)
    {
        Log.i(TAG, "editSelfReview() method called...........................");

        Map<String, String> params = new HashMap<>();
        params.put("review_id", selfReview.getReviewId());
        params.put("comments", "test");
        params.put("edit_self_review", "1");
        params.put("label", selfReview.getQualification());
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("assistance_requested", selfReview.getAssistance());
        params.put("suggestions", selfReview.getSuggestions());
        params.put("improvements_needed", selfReview.getImprovements());
        params.put("strengths",selfReview.getStrengths());


       // String[] subArr = new String[selfReviewList.size()];
        List<SelfReview> addNewSubjectWithGrade = new ArrayList<>();
        List<SelfReview> editSubjectWithGrade = new ArrayList<>();

        for (int i = 0; i < selfReviewList.size(); i++)
        {
            SelfReview selfReview1 = selfReviewList.get(i);
            if (selfReview1.getSubjectID() != null && !selfReview1.getSubjectID().isEmpty()) {
                editSubjectWithGrade.add(selfReview1);
            }
            else
            {
                addNewSubjectWithGrade.add(selfReview1);
            }

            //subArr[i] = selfReview1.getSubjectID();
        }

        for (int i = 0; i < addNewSubjectWithGrade.size(); i++)
        {
            SelfReview selfReview1 = addNewSubjectWithGrade.get(i);
                params.put("add_subject[" + i + "]", selfReview1.getSubject());
                params.put("add_grade[" + i + "]", selfReview1.getGrade());
            //subArr[i] = selfReview1.getSubjectID();
        }


        for (int i = 0; i < editSubjectWithGrade.size(); i++)
        {
            SelfReview selfReview1 = editSubjectWithGrade.get(i);
             params.put("edit_subject[" + i + "]", selfReview1.getSubject());
                params.put("edit_subject_id[" + i + "]", selfReview1.getSubjectID());
                params.put("edit_grade[" + i + "]", selfReview1.getGrade());
            //subArr[i] = selfReview1.getSubjectID();
        }




        Log.i(TAG, "params to editSelfReview  = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {

                Log.i(TAG, "server response to editSelfReview  = " + message);
                savedItemCount++;

                try {
                    JSONObject jsonObject = new JSONObject(message.trim());
                    Log.d(TAG, "server message = " + message);


                    if ("1".equals(jsonObject.getString("success"))) {
                        messages = jsonObject.getString("message");

                    }
                    if ("0".equals(jsonObject.getString("success"))) {
                        messages = jsonObject.getString("message");
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Error in updateSelfReview() method , " + e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed(messages);
                }

                finally {
                    mListener.onHideProgress();
                    mListener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.i(TAG, "server response error to editSelfReview  = " + error);
                error.printStackTrace();
                savedItemCount++;
                Log.e(TAG, "onVolleyErrorResponse" + error);
                mListener.onHideProgress();
                mListener.onSetViewsEnabledOnProgressBarGone();
            }
        });

    }


    @Override
    public void getSelfReview(String athlete_id) {

        Map<String, String> params = new HashMap<>();
        params.put("get_self_review","1");
        params.put("user_id",athlete_id);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"getSelfReview : " + message);
                try {
                   JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res")))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        parseSelfReviewJsonAndUpdateUi(jsonArray);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.e(TAG,"onVolleyErrorResponse" +error);
                error.printStackTrace();
            }
        });
    }

    @Override
    public void deleteReview(final String id) {

        Map<String,String> params = new HashMap<>();
        params.put("delete_self_review", String.valueOf(1));
        params.put("delete_self_review_id["+0+"]", id);
        Log.d(TAG,"params to delete items " + params);


        RemoteServer remoteServer = new RemoteServer(mContext);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG, "server message to delete self review(progress report given by teacher) = "+message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("success"))) {
                        mListener.onSelfListDeleted(id);
                    }
                    else if("0".equals(jsonObject.getString("success")))
                        mListener.onShowMessage(jsonObject.getString("message"));
                    else
                        mListener.onFailed("Failed...");

                    // mAdapter.onDeletedSuccessfully(id);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                DialogHelper.showCustomDialog(mContext,"Connection Error..");
            }
        });
    }


    private void parseSelfReviewJsonAndUpdateUi(JSONArray jsonArray)
    {
        List<SelfReview> selfReviewList = new ArrayList<>();
        for(int i = 0; i < jsonArray.length() ; i++) {
            try {
                SelfReview selfReview = new SelfReview();
               JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                selfReview.setReviewId(jsonObject1.getString("id"));
                selfReview.setDate(jsonObject1.getString("date"));

                selfReviewList.add(selfReview);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        mListener.onReceivedSelfReportList(selfReviewList);

    }
}
