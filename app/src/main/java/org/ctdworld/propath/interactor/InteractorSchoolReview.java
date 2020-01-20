package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractSchoolReview;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.model.SchoolReview;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorSchoolReview implements ContractSchoolReview.Interactor {
    private static final String TAG = InteractorSchoolReview.class.getSimpleName();

    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;
    private String messages = "";
    private int savedItemCount = 0;


    public InteractorSchoolReview(OnFinishedListener listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }

    @Override
    public void saveSchoolReview(final List<SchoolReview> list)
    {
        Log.i(TAG, "SchoolReview() method called...........................");

        for (int i = 0; i < list.size(); i++)
        {
            Map<String, String> params = getParamsToGetSchoolReview(list.get(i));
            save(params, list.size());
        }

    }

    @Override
    public void editSchoolReview(List<SchoolReview> schoolReviewList, SchoolReview schoolReview)
    {
        Log.i(TAG, "editSchoolReview() method called...........................");

        Map<String, String> params = new HashMap<>();
        params.put("review_id",schoolReview.getReviewID());
        params.put("user_id", schoolReview.getAthleteID());
        params.put("comments","test");
        params.put("edit_school_review", "1");
        params.put("assistance_requested", schoolReview.getAssistance());
        params.put("suggestions", schoolReview.getSuggestions());
        params.put("improvements_needed", schoolReview.getImprovements());
        params.put("strengths",schoolReview.getStrengths());
        params.put("qualification",schoolReview.getQualification());

        List<SchoolReview> addNewSubjectWithGrade = new ArrayList<>();
        List<SchoolReview> editSubjectWithGrade = new ArrayList<>();

        for (int i = 0; i < schoolReviewList.size(); i++)
        {
            SchoolReview schoolReview1 = schoolReviewList.get(i);

            if (schoolReview1.getSubjectID() != null && !schoolReview1.getSubjectID().isEmpty())
            {
                editSubjectWithGrade.add(schoolReview1);
            }
            else {

                addNewSubjectWithGrade.add(schoolReview1);

            }

        }


        for (int i = 0; i < addNewSubjectWithGrade.size(); i++)
        {
            SchoolReview schoolReview1 = addNewSubjectWithGrade.get(i);
            params.put("add_subject[" + i + "]", schoolReview1.getSubject());
            params.put("add_grade[" + i + "]", schoolReview1.getGrade());
        }


        for (int i = 0; i < editSubjectWithGrade.size(); i++)
        {
            SchoolReview schoolReview1 = editSubjectWithGrade.get(i);
            params.put("edit_subject[" + i + "]", schoolReview1.getSubject());
            params.put("edit_subject_id[" + i + "]", schoolReview1.getSubjectID());
            params.put("edit_grade[" + i + "]", schoolReview1.getGrade());
        }


        //params.put("subject_id", Arrays.toString(subArr));


        //  subArr[i] = schoolReview1.getSubjectID();
     //   String[] subArr = new String[schoolReviewList.size()];

       /* String[] subArr = new String[schoolReviewList.size()];
        Map<String, String> subjectsMap = new HashMap<>();
        for (int i = 0; i < schoolReviewList.size(); i++)
        {
            SchoolReview schoolReview1 = schoolReviewList.get(i);
            subjectsMap.put(schoolReview1.getSubject(), schoolReview1.getGrade());

            subArr[i] = schoolReview1.getSubjectID();

            // params.put("subject_id["+i+"]",schoolReview1.getSubjectID());
        }

        // converting subjects map to associative array
        String openBraceRemoved = subjectsMap.toString().replace("{","[");
        String closeBraceRemoved = openBraceRemoved.replace("}","]");
        String subjectsAssociativeArr = closeBraceRemoved.replaceAll("=","=>");

        params.put("subject", subjectsAssociativeArr);
        params.put("subject_id", Arrays.toString(subArr));*/

        Log.i(TAG, "params to editSchoolReview  = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {

                Log.i(TAG, "server response to editSchoolReview  = " + message);


             //   savedItemCount++;
                //JSONObject jsonObject;

                try
                {
                    JSONObject jsonObject = new JSONObject(message.trim());
                    Log.d(TAG,"server message = "+message);


                    if ("1".equals(jsonObject.getString("success")))
                    {
                       // Log.d(TAG,"saved subject = "+savedItemCount);
                        mListener.onSuccess(jsonObject.getString("message"));

                    }
                    if ("0".equals(jsonObject.getString("success")))
                    {
                        messages = jsonObject.getString("message");
                    }


                    /*if (totalItems == savedItemCount)
                        mListener.onSuccess(messages);*/

                } catch (JSONException e) {
                    Log.e(TAG, "Error in updateSchoolReview() method , " + e.getMessage());
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
                Log.i(TAG, "server response error to editSchoolReview  = " + error);
                error.printStackTrace();
                savedItemCount++;
                Log.e(TAG,"onVolleyErrorResponse" +error);
                mListener.onHideProgress();
                mListener.onSetViewsEnabledOnProgressBarGone();
            }
        });

    }

    @Override
    public void getSchoolReview(String athlete_id) {

        Map<String, String> params = new HashMap<>();
        params.put("get_school_review","1");
        params.put("user_id",athlete_id);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"getSchoolReview : " + message);
                try {
                    JSONObject  jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res")))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        parseProgressReportJsonAndUpdateUi(jsonArray);
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
        params.put("delete_school_review", String.valueOf(1));
        params.put("delete_school_review_id["+0+"]", id);
        Log.d(TAG,"params to delete items " + params);


        RemoteServer remoteServer = new RemoteServer(mContext);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG, "server message to delete school review(progress report given by teacher) = "+message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("success"))) {
                        mListener.onProgressListDeleted(id);
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

    private void parseProgressReportJsonAndUpdateUi(JSONArray jsonArray)
    {
        List<SchoolReview> progressReportList = new ArrayList<>();

        for(int i = 0; i < jsonArray.length() ; i++) {
            try {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                String loggedInUser = SessionHelper.getInstance(mContext).getUser().getRoleId();
                String teacherRoleId = RoleHelper.TEACHER_ROLE_ID;
                SchoolReview schoolReview = new SchoolReview();

                if (loggedInUser.equals(teacherRoleId))
                {
                    if (jsonObject1.get("teacher_id").equals(SessionHelper.getInstance(mContext).getUser().getUserId())) {
                        schoolReview.setReviewID(jsonObject1.getString("id"));
                        schoolReview.setDate(jsonObject1.getString("date"));
                        schoolReview.setTeacherName(jsonObject1.getString("teacher_name"));
                        schoolReview.setTeacherID(jsonObject1.getString("teacher_id"));
                        schoolReview.setAthleteID(jsonObject1.getString("athlete_id"));
                        progressReportList.add(schoolReview);

                    }
                }
                else {
                schoolReview.setReviewID(jsonObject1.getString("id"));
                    schoolReview.setDate(jsonObject1.getString("date"));
                    schoolReview.setTeacherName(jsonObject1.getString("teacher_name"));
                    schoolReview.setTeacherID(jsonObject1.getString("teacher_id"));
                    schoolReview.setAthleteID(jsonObject1.getString("athlete_id"));
                    progressReportList.add(schoolReview);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        mListener.onReceivedProgressReportList(progressReportList);

        }


    private Map<String, String> getParamsToGetSchoolReview(SchoolReview schoolReview) {

        Map<String, String> params = new HashMap<>();
        params.put("teacher_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("teacher_name", SessionHelper.getInstance(mContext).getUser().getName());
        params.put("user_id", schoolReview.getAthleteID());
        params.put("comments", "test");
        params.put("add_school_review", "1");
        params.put("assistance_requested", schoolReview.getAssistance());
        params.put("suggestions", schoolReview.getSuggestions());
        params.put("improvements_needed", schoolReview.getImprovements());
        params.put("strengths",schoolReview.getStrengths());
        params.put("qualification",schoolReview.getQualification());

        params.put("subject"+schoolReview.getSubject(), schoolReview.getGrade());

        Log.i(TAG, "params to send school review = " + params);

        return params;
    }



    private void save(Map<String, String> params, final int totalItems) {

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                savedItemCount++;
                JSONObject jsonObject;

                try
                {
                    jsonObject = new JSONObject(message.trim());
                      Log.d(TAG,"server message = "+message);


                        if ("1".equals(jsonObject.getString("success")))
                        {
                        //  Log.d(TAG,"saved subject = "+savedItemCount);
                         messages = jsonObject.getString("message");
                      //   Log.d(TAG,"messages interactor:  " +messages);
                            mListener.onSuccess(messages);

//                            mListener.onSuccess(subject);
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            if (jsonArray != null)
//                            {
//                               //saveSchoolReview(jsonArray);
//                            }
                        }
                        if ("0".equals(jsonObject.getString("success")))
                        {
                            messages = jsonObject.getString("message");
                        }


                    if (totalItems == savedItemCount)
                        mListener.onSuccess(messages);

                } catch (JSONException e) {
                    Log.e(TAG, "Error in updateSchoolReview() method , " + e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed(messages);
                }

                finally {
                    mListener.onHideProgress();
                    mListener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                savedItemCount++;
                Log.e(TAG,"onVolleyErrorResponse" +error);
                mListener.onHideProgress();
                mListener.onSetViewsEnabledOnProgressBarGone();
                if (totalItems == savedItemCount)
                    mListener.onSuccess(messages);

            }
        });

    }
}
