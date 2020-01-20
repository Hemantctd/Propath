package org.ctdworld.propath.helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNotification;
import org.ctdworld.propath.activity.ActivityContact;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;


// this helper class handles all types of requests and response to request came in push notification.
public class NotificationRequestHelper
{
    private final String TAG = NotificationRequestHelper.class.getSimpleName();
    private static final int NOTIFICATION_ID = NotificationHelper.getNotificationId();
    private Context mContext;


    public NotificationRequestHelper(Context mContext) {
        this.mContext = mContext;
    }




    // method to handle data received from push notification
    public void handleData(RemoteMessage remoteMessage)
    {
        try
        {
            String notificationTitle = "";
            if (remoteMessage.getData() != null)
            {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                Log.i(TAG,"jsonObject = "+jsonObject);   // get main JsonObject
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");    // getting data json object from outer most json object
                String strMessageJson = jsonObjectData.getString("message"); // contains all details of sender
                JSONObject jsonObjectMessage = new JSONObject(strMessageJson);
                notificationTitle = jsonObjectData.getString("title");
                switch (notificationTitle)
                {
                    // to show requestData notification, this requestData has been sent by user to add as friend
                    case "Request":
                        showRequestNotification(jsonObjectMessage);
                        break;
                    // connection requestData response, when connection requestData is accepted, rejected or pending
                    case "Request Response":
                        showRequestResponseNotification(jsonObjectMessage);
                        break;
                  /*  case "Friend Request Rejected.":
                        showRequestResponseNotification(jsonObjectMessage, notificationTitle);
                        break;*/
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG,"error in sendNotification() method , "+e.getMessage());
        }
    }







    // #it creates notification for connection requestData which contains two buttons Accept and Reject
    private void showRequestNotification(JSONObject jsonObjectMessage/*String title, String message, String toUserId, String requestId*/)
    {
        // #fetching user details from inner data json object, this object contains all user details
        try
        {
            Log.i(TAG,"showRequestNotification() method called");

            JSONObject jsonObjectData = jsonObjectMessage.getJSONObject("data");
            String requestFromName = jsonObjectData.getString("user_name");
            String requestToUserId = jsonObjectData.getString("user_id");  // toUserId is who has sent requestData
            String requestFor = jsonObjectData.getString("request_for");
            String message = getRequestMessageForNotification(requestFor, requestFromName);
            String title = getRequestTitleForNotification(requestFor, requestFromName);


            // #creating Intent and PendingIntent for pending request. by default request is pending
            Intent intent = getIntentForRequest(requestToUserId, Request.REQUEST_STATUS_PENDING, requestFor);
            PendingIntent pendingIntentRequestPending = PendingIntent.getActivity(mContext,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);


            // #creating Intent and PendingIntent to accept requestData. this pending intent will be set in addAction() method
            Intent intentAcceptRequest = getIntentForRequest(requestToUserId, Request.REQUEST_STATUS_ACCEPT, requestFor);  // status 1 is for accepting requestData
            PendingIntent pendingIntentAccept = PendingIntent.getActivity(mContext,NOTIFICATION_ID,intentAcceptRequest,PendingIntent.FLAG_UPDATE_CURRENT);


            // creating Intent and PendingIntent to reject requestData. this pending intent will be set in addAction() method
            Intent intentRejectRequest = getIntentForRequest(requestToUserId, Request.REQUEST_STATUS_REJECT, requestFor);  // status 2 is for rejecting requestData
            PendingIntent pendingIntentReject = PendingIntent.getActivity(mContext,NOTIFICATION_ID,intentRejectRequest,PendingIntent.FLAG_UPDATE_CURRENT);


           // int notificationId = NotificationHelper.getNotificationId();
            Log.i(TAG,"notification id for requestData = "+NOTIFICATION_ID);
            NotificationHelper notificationHelper = new NotificationHelper(mContext);
            NotificationCompat.Builder builder = notificationHelper.getNotificationBuilder(title,message, NotificationHelper.CHANNEL_REQUEST, false);
            builder.addAction(R.drawable.ic_like,"Accept", pendingIntentAccept);
            builder.addAction(R.drawable.ic_cancel,"Reject", pendingIntentReject);
            builder.setContentIntent(pendingIntentRequestPending);

            Log.i(TAG,"showing notification for requestData = "+NOTIFICATION_ID);
            notificationHelper.showRequestNotification(builder, NotificationHelper.CHANNEL_REQUEST, NOTIFICATION_ID); // showing notification

        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(TAG,"Error while parsing json in showRequestNotification() method ");
        }
    }





    // it creates notification for connection requestData response which contains message whether requestData has been accepted or not
    private void showRequestResponseNotification(JSONObject jsonObjectMessage)
    {
        try
        {
            Log.i(TAG, "showRequestResponseNotification() method called");

            String notificationTitle = "Request Response";  // default title for connection requestData
            // #fetching user details from inner data json object, this object contains all user details
            JSONObject jsonObjectData = jsonObjectMessage.getJSONObject("data");
            String responseFromName = jsonObjectData.getString("user_name");
            String requestFor = jsonObjectData.getString("request_for");
            String requestStatus = jsonObjectData.getString("request_status");  // contains status of requestData whether requestData has been accepted, rejected or kept pending
            String notificationMessage = getResponseMessageForNotification(requestFor, responseFromName, requestStatus); // setting name for message
            if (responseFromName != null)
                notificationTitle = "Request response from "+responseFromName.toUpperCase();


            Intent intent = new Intent(mContext, ActivityContact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);


           // int notificationId = NotificationHelper.getNotificationId();
            Log.i(TAG,"notification id for requestData response by other user = "+NOTIFICATION_ID);
            NotificationHelper notificationHelper = new NotificationHelper(mContext);
            NotificationCompat.Builder builder = notificationHelper.getNotificationBuilder(notificationTitle,notificationMessage, NotificationHelper.CHANNEL_REQUEST,true);
            builder.setContentIntent(pendingIntent);

            Log.i(TAG,"showing notification for requestData response by other user = "+NOTIFICATION_ID);
            notificationHelper.showRequestNotification(builder, NotificationHelper.CHANNEL_REQUEST, NOTIFICATION_ID); // showing notification

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error while parsing json in showConnectionRequestResponse() method ");
        }
    }






    // this method returns intent object for starting ActivityNotification, for received request
    private Intent getIntentForRequest(String toUserId, String status, String requestFor)
    {
        Log.i(TAG,"getIntentForRequest() method called");
        Intent intentRequest = new Intent(mContext, ActivityNotification.class);
        Request.Data requestData = new Request.Data();

        requestData.setRequestFromUserId(SessionHelper.getInstance(mContext).getUser().getUserId());  // fromId is the requestData receiver
        requestData.setRequestStatus(status);  // status is request response(pending, accepted or rejected)
        requestData.setRequestToUserId(toUserId);  // toUserId is who has sent requestData
        requestData.setRequestFor(requestFor);  // request type, it may be one type of mentioned above
        requestData.setNotificationId(NOTIFICATION_ID);
        requestData.setNotificationType(Request.NOTIFICATION_TYPE_REQUEST);

        // setting to intent
        switch (status)
        {
            case Request.REQUEST_STATUS_ACCEPT:
                intentRequest.setAction("accept");
                break;
            case Request.REQUEST_STATUS_REJECT:
                intentRequest.setAction("reject");
                break;
            case Request.REQUEST_STATUS_PENDING:
                intentRequest.setAction("pending");
                break;
        }

        intentRequest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentRequest.putExtra(ActivityNotification.KEY_REQUEST_DATA, requestData);   // putting Request object, to send

        return intentRequest;
    }






    // this method returns requestData title to show in notification depending on "requestFor" from push notification
    private String getRequestTitleForNotification(String requestFor, String userName)
    {
        String title = "Request";

        if (userName != null)
            title = userName+" sent request";

        return title;
    }






    // this method returns request  message to show in notification depending on "requestFor" from push notification
    private String getRequestMessageForNotification(String requestFor, String userName)
    {
        String message = "Permission Request"; // default value
        switch (requestFor)
        {
            case Request.REQUEST_FOR_COACH_FEEDBACK:
                if (userName != null)
                    message = userName+" needs permission to see your feedback given by coach.";
                break;

            case Request.REQUEST_FOR_EDUCATION:
                if (userName != null)
                    message = userName+" needs permission to see your progress report given by teacher";
                break;

            case Request.REQUEST_FOR_TEACHER_GIVE_REVIEW:
                if (userName != null)
                    message = userName+" needs permission to give you school review.";
                break;
            case Request.REQUEST_FOR_SELF_REVIEW:
                if (userName != null)
                    message = userName+" needs permission to see your self review.";
                break;

            case Request.REQUEST_FOR_FRIEND:
                if (userName != null)
                    message = userName+" has sent request to be friend.";
                break;

            case Request.REQUEST_FOR_COACH_SELF_REVIEW:
                if (userName != null)
                    message = userName+" has sent to see your coach self review .";
                break;

            case Request.REQUEST_FOR_CAREER_PLAN_VIEW:
                if (userName != null)
                    message = userName+" has sent to see your career plan.";
                break;
        }


        return message;
    }






     /*this method returns response (given by other user) message to show in notification depending on requestFor(for which requestData was sent)
     from push notification*/
    private String getResponseMessageForNotification(String requestFor, String userName, String requestStatus)
    {
        Log.i(TAG," getResponseMessageForNotification() method called");
        Log.i(TAG," requestData for = "+requestFor);
        Log.i(TAG," user name = "+userName);
        Log.i(TAG," requestData status = "+requestStatus);

        String message = "Permission Response"; // default value
        switch (requestFor)
        {
            case Request.REQUEST_FOR_COACH_FEEDBACK:
                if (userName != null)
                {
                    if (Request.REQUEST_STATUS_PENDING.equalsIgnoreCase(requestStatus))
                        message = "Request is pending to see "+userName+", feedback given by coach.";
                    else if (Request.REQUEST_STATUS_ACCEPT.equalsIgnoreCase(requestStatus))
                        message = "Request is accepted to see "+userName+", feedback given by coach.";
                    else if (Request.REQUEST_STATUS_REJECT.equalsIgnoreCase(requestStatus))
                        message = "Request is rejected to see "+userName+", feedback given by coach.";
                }
                break;

            case Request.REQUEST_FOR_EDUCATION:
                if (userName != null)
                {
                    if (Request.REQUEST_STATUS_PENDING.equalsIgnoreCase(requestStatus))
                        message = "Request is pending to see "+userName+"' progress report given by teacher";
                    else if (Request.REQUEST_STATUS_ACCEPT.equalsIgnoreCase(requestStatus))
                        message = "Request is accepted to see "+userName+"' progress report given by teacher";
                    else if (Request.REQUEST_STATUS_REJECT.equalsIgnoreCase(requestStatus))
                        message = "Request is rejected to see "+userName+"' progress report given by teacher";
                }
                break;

            case Request.REQUEST_FOR_TEACHER_GIVE_REVIEW:
                if (userName != null)
                {
                    if (Request.REQUEST_STATUS_PENDING.equalsIgnoreCase(requestStatus))
                        message = "Request is pending to give school review to "+userName;
                    else if (Request.REQUEST_STATUS_ACCEPT.equalsIgnoreCase(requestStatus))
                        message = "Request is accepted to give school review to "+userName;
                    else if (Request.REQUEST_STATUS_REJECT.equalsIgnoreCase(requestStatus))
                        message = "Request is rejected to give school review to "+userName;
                }
                break;

            case Request.REQUEST_FOR_FRIEND:
                if (userName != null)
                {
                    if (Request.REQUEST_STATUS_PENDING.equalsIgnoreCase(requestStatus))
                        message = "Friend Request is pending sent to "+userName;
                    else if (Request.REQUEST_STATUS_ACCEPT.equalsIgnoreCase(requestStatus))
                        message = "Friend Request is accepted sent to "+userName;
                    else if (Request.REQUEST_STATUS_REJECT.equalsIgnoreCase(requestStatus))
                        message = "Friend Request is rejected sent to "+userName;
                }
                break;

            case Request.REQUEST_FOR_SELF_REVIEW:
                if (userName != null)
                {
                    if (Request.REQUEST_STATUS_PENDING.equalsIgnoreCase(requestStatus))
                        message = "Request is pending to see "+userName+"' self review";
                    else if (Request.REQUEST_STATUS_ACCEPT.equalsIgnoreCase(requestStatus))
                        message = "Request is accepted to see "+userName+"' self review";
                    else if (Request.REQUEST_STATUS_REJECT.equalsIgnoreCase(requestStatus))
                        message = "Request is rejected to see "+userName+"' self review";
                }
                break;


            case Request.REQUEST_FOR_COACH_SELF_REVIEW:
                if (userName != null)
                {
                    if (Request.REQUEST_STATUS_PENDING.equalsIgnoreCase(requestStatus))
                        message = "Request is pending to see "+userName+"' coach self review";
                    else if (Request.REQUEST_STATUS_ACCEPT.equalsIgnoreCase(requestStatus))
                        message = "Request is accepted to see "+userName+"' coach self review";
                    else if (Request.REQUEST_STATUS_REJECT.equalsIgnoreCase(requestStatus))
                        message = "Request is rejected to see "+userName+"' coach self review";
                }
                break;

            case Request.REQUEST_FOR_CAREER_PLAN_VIEW:
                if (userName != null)
                {
                    if (Request.REQUEST_STATUS_PENDING.equalsIgnoreCase(requestStatus))
                        message = "Request is pending to see "+userName+"' career plan";
                    else if (Request.REQUEST_STATUS_ACCEPT.equalsIgnoreCase(requestStatus))
                        message = "Request is accepted to see "+userName+"' career plan";
                    else if (Request.REQUEST_STATUS_REJECT.equalsIgnoreCase(requestStatus))
                        message = "Request is rejected to see "+userName+"' career plan";
                }
                break;
        }



        return message;
    }


}
