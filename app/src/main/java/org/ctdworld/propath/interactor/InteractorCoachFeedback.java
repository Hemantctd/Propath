package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractCoachFeedback;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorCoachFeedback implements ContractCoachFeedback.Interactor {
    private static final String TAG = InteractorCoachFeedback.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context context;


    public InteractorCoachFeedback(OnFinishedListener onFinishedListener, Context context) {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }

    @Override
    public void getCoachFeedbackDescription(String userId, String review_id) {

        Log.d(TAG, " review_id :" + review_id);

        Log.d(TAG, " getCoachFeedbackDescription called :");
        remoteServer.sendData(RemoteServer.URL, getParamsOfSelfReview(userId, review_id), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                JSONObject jsonObject;

                Log.i(TAG, "server response while getting coach feedback desc = " + message);

                try {
                    jsonObject = new JSONObject(message.trim());
                    Log.d(TAG, "server message = " + message);
                    if ("1".equals(jsonObject.getString("res"))) {
                        Log.d(TAG, "called");
                        parseJsonAndUpdateUI(jsonObject.getJSONObject("data"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error in getCoachFeedbackDescription() method , " + e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                DialogHelper.showCustomDialog(context, "Connection Error...");

            }
        });
    }


    @Override
    public void getCoachFeedback(String athlete_id, String roleId) {
        HashMap<String, String> params = new HashMap<>();

        params.put("get_all_feedback_list", "1");
        params.put("user_id", athlete_id);
        params.put("role_id", roleId);

        Log.d(TAG, "params : " + params);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG, "getCoachFeedback : " + message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res"))) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        parseCoachFeedbackJsonAndUpdateUi(jsonArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                DialogHelper.showCustomDialog(context, "Connection Error...");

            }
        });

    }

    @Override
    public void deleteCoachFeedback(final String id, final String roleId) {
        Map<String, String> params = new HashMap<>();
        params.put("delete_coach_feedback", String.valueOf(1));
        params.put("delete_coach_feedback_id[" + 0 + "]", id);
        params.put("role_id", roleId);
        Log.d(TAG, "params to deleteCoachFeedback items " + params);


        RemoteServer remoteServer = new RemoteServer(context);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {

                Log.i(TAG, "server message to deleteCoachFeedback = " + message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("success"))) {
                        listener.onCoachFeedbackDeleted(id);
                    } else if ("0".equals(jsonObject.getString("success")))
                        listener.onSuccess(jsonObject.getString("message"));
                    else
                        listener.onFailed();

                    // mAdapter.onDeletedSuccessfully(id);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                DialogHelper.showCustomDialog(context, "Connection Error..");
            }
        });
    }

    @Override
    public void editCoachFeedback(CoachData coachData) {
        remoteServer.sendData(RemoteServer.URL, getParams(coachData), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG, "server message to editCoachFeedback coach feedback = " + message);

                Log.i(TAG, "server message = " + message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                        listener.onSuccess(jsonObject.getString("message"));
                    else {
                        if ("0".equals(jsonObject.getString("success")))
                            listener.onFailed();
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "Error in editCoachFeedback() method , " + e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                } finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG, "editCoachFeedback() method volley error = " + error.getMessage());
                error.printStackTrace();
                //listener.onHideProgress();
                // listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    @Override
    public void createCoachSelfReview(CoachData coachData) {
        Map<String, String> params = new HashMap<>();

        params.put("create_match_self_review", "1");
        params.put("strengths", coachData.getStrenths());
        params.put("event_match", coachData.getEvents());
        params.put("user_id", SessionHelper.getInstance(context).getUser().getUserId());
        params.put("improvements_needed", coachData.getImprovements());
        params.put("suggestions", coachData.getSuggestions());
        params.put("assistance_requested", coachData.getAssistanceRequired());

        Log.i(TAG, "params = " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG, "server message to add coach feedback self review = " + response);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.trim());

                    if ("1".equals(jsonObject.getString("success")))
                        listener.onSuccess(jsonObject.getString("message"));
                    else {
                        if ("0".equals(jsonObject.getString("success")))
                            listener.onFailed();
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "Error in createCoachSelfReview() method , " + e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                } finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG, "createCoachSelfReview() method volley error = " + error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });

    }

    @Override
    public void getCoachSelfReview(String athleteId) {
        Map<String, String> params = new HashMap<>();
        params.put("get_all_match_feedback", "1");
        params.put("user_id", athleteId);
        Log.i(TAG, "params = " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG, "server message to getCoachSelfReview = " + response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.trim());
                    if ("1".equals(jsonObject.getString("res"))) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        parseCoachFeedbackSelfReviewJsonAndUpdateUi(jsonArray);
                    } else if ("0".equals(jsonObject.getString("res"))) {
                        listener.onFailed();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Error in getCoachSelfReview() method , " + e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                } finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG, "getCoachSelfReview() method volley error = " + error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onSetViewsEnabledOnProgressBarGone();

            }
        });
    }

    @Override
    public void getCoachSelfReviewDescription(String userId, String review_id) {

        Map<String, String> params = new HashMap<>();
        params.put("get_match_self_review_description", "1");
        params.put("user_id", userId);
        params.put("review_id", review_id);

        Log.i(TAG, "params to getCoachSelfReviewDescription = " + params);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                JSONObject jsonObject;

                Log.i(TAG, "server response while getting coachSelfReviewDescription= " + message);

                try {
                    jsonObject = new JSONObject(message.trim());
                    Log.d(TAG, "server message = " + message);
                    if ("1".equals(jsonObject.getString("res"))) {
                        parseCoachSelfReviewDescJsonAndUpdateUi(jsonObject.getJSONObject("data"));

                    } else
                        listener.onFailed();
                } catch (JSONException e) {
                    Log.e(TAG, "Error in getCoachSelfReviewDescription() method , " + e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                } finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onHideProgress();
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    @Override
    public void editCoachSelfReviewFeedback(CoachData coachData) {
        Map<String, String> params = new HashMap<>();

        params.put("edit_match_self_review", "1");
        params.put("strengths", coachData.getStrenths());
        params.put("event_match", coachData.getEvents());
        params.put("improvements_needed", coachData.getImprovements());
        params.put("suggestions", coachData.getSuggestions());
        params.put("assistance_requested", coachData.getAssistanceRequired());
        params.put("review_id", coachData.getReviewID());

        Log.i(TAG, "params = " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG, "server message to editCoachSelfReviewFeedback = " + response);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.trim());

                    if ("1".equals(jsonObject.getString("success")))
                        listener.onSuccess(jsonObject.getString("message"));
                    else {
                        if ("0".equals(jsonObject.getString("success")))
                            listener.onFailed();
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "Error in editCoachSelfReviewFeedback() method , " + e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                } finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG, "editCoachSelfReviewFeedback() method volley error = " + error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    @Override
    public void deleteCoachSelfReview(final String id, String athleteId) {
        Map<String, String> params = new HashMap<>();
        params.put("delete_match_self_review", String.valueOf(1));
        params.put("review_id", id);
        params.put("user_id", athleteId);
        Log.d(TAG, "params to delete_match_self_review items " + params);


        RemoteServer remoteServer = new RemoteServer(context);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {

                Log.i(TAG, "server message to delete_match_self_review = " + message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res"))) {
                        listener.onCoachFeedbackDeleted(id);
                    } else if ("0".equals(jsonObject.getString("res")))
                        listener.onSuccess(jsonObject.getString("message"));
                    else
                        listener.onFailed();


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                DialogHelper.showCustomDialog(context, "Connection Error..");
            }
        });
    }


    private Map<String, String> getParams(CoachData coachData) {

        Map<String, String> params = new HashMap<>();

        params.put("edit_coach_feedback", "1");
        params.put("workons", coachData.getWorkons());
        params.put("strenths", coachData.getStrenths());
        params.put("event", coachData.getEvents());
        params.put("athlete_id", coachData.getAthleteID());
        params.put("coach_id", SessionHelper.getInstance(context).getUser().getUserId());
        params.put("coach_feedback_id", coachData.getReviewID());
        params.put("improvement_needed", coachData.getImprovements());
        params.put("suggestions", coachData.getSuggestions());
        params.put("assistance_requested", coachData.getAssistanceRequired());
        params.put("assistance_offered", coachData.getAssistanceOffered());

        Log.i(TAG, "params = " + params);

        return params;
    }

    private Map<String, String> getParamsOfSelfReview(String userId, String review_id) {

        Map<String, String> params = new HashMap<>();
        params.put("show_coach_feedback", "1");
        params.put("user_id", userId);
        params.put("id", review_id);

        Log.i(TAG, "params to get school report = " + params);

        return params;
    }


    private void parseJsonAndUpdateUI(JSONObject jsonObject) {
        CoachData coachData = new CoachData();
        Log.d(TAG, "jsonObject :" + jsonObject);
        try {
            coachData.setStrenths(jsonObject.getString("strenths"));
            coachData.setWorkons(jsonObject.getString("workons"));
            coachData.setEvents(jsonObject.getString("event"));
            coachData.setDate(jsonObject.getString("date_time"));
            coachData.setAthleteName(jsonObject.getString("user_name"));
            coachData.setCoachName(jsonObject.getString("coach_name"));
            coachData.setAssistanceOffered(jsonObject.getString("assistance_offered"));
            coachData.setImprovements(jsonObject.getString("improvement_needed"));
            coachData.setSuggestions(jsonObject.getString("suggestions"));

            coachData.setAthleteImg(RemoteServer.BASE_IMAGE_URL + jsonObject.getString("profile_image"));

            listener.onReceivedCoachFeedbackDescription(coachData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void parseCoachFeedbackJsonAndUpdateUi(JSONArray jsonArray) {
        List<CoachData> coachDataList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1;
            try {
                jsonObject1 = jsonArray.getJSONObject(i);

                String loggedInUser = SessionHelper.getInstance(context).getUser().getRoleId();
                String coachRoleId = RoleHelper.COACH_ROLE_ID;

                if (loggedInUser.equals(coachRoleId)) {
                    if (jsonObject1.get("coach_id").equals(SessionHelper.getInstance(context).getUser().getUserId())) {
                        CoachData coachData = new CoachData();

                        coachData.setReviewID(jsonObject1.get("id").toString());
                        coachData.setDate(jsonObject1.get("date_time").toString());
                        coachData.setCoachName(jsonObject1.get("user_name").toString());
                        coachDataList.add(coachData);

                    }
                } else {
                    CoachData coachData = new CoachData();
                    coachData.setReviewID(jsonObject1.get("id").toString());
                    coachData.setDate(jsonObject1.get("date_time").toString());
                    coachData.setCoachName(jsonObject1.get("user_name").toString());
                    coachDataList.add(coachData);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listener.onReceivedCoachFeedbackData(coachDataList);
    }


    private void parseCoachFeedbackSelfReviewJsonAndUpdateUi(JSONArray jsonArray) {
        List<CoachData> coachReviewList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                CoachData coachData = new CoachData();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                coachData.setReviewID(jsonObject1.getString("id"));
                coachData.setAthleteID(jsonObject1.getString("athlete_id"));
                coachData.setDate(jsonObject1.getString("created_at"));

                coachReviewList.add(coachData);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        listener.onReceivedCoachSelfReview(coachReviewList);

    }

    private void parseCoachSelfReviewDescJsonAndUpdateUi(JSONObject jsonObject) {
        CoachData coachData = new CoachData();
        try {
            coachData.setReviewID(jsonObject.getString("id"));
            coachData.setSuggestions(jsonObject.getString("suggestion"));
            coachData.setAssistanceRequired(jsonObject.getString("assistance_requested"));
            coachData.setStrenths(jsonObject.getString("strength"));
            coachData.setEvents(jsonObject.getString("event_match"));
            coachData.setImprovements(jsonObject.getString("improvement_needed"));
            coachData.setDate(jsonObject.getString("created_at"));
            coachData.setUserName(jsonObject.getString("user_name"));
            String picUrl = RemoteServer.BASE_IMAGE_URL + jsonObject.getString("profile_image");
            coachData.setProfileImage(picUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener.onReceivedCoachSelfReviewDescription(coachData);
    }
}
