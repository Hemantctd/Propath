package org.ctdworld.propath.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.NotificationChatHelper;
import org.ctdworld.propath.helper.NotificationRequestHelper;
import org.ctdworld.propath.prefrence.PreferenceNotification;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private final String TAG = MyFirebaseMessagingService.class.getCanonicalName();

//    public static final int NOTIFICATION_REQUEST_CODE = 300;
   // public static int NOTIFICATION_ID = 1;


    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);


        Log.i(TAG,"onMessageReceived() method called");
        if (remoteMessage != null)
        {
            try {
              //  Log.i(TAG,"remoteMessage data = "+remoteMessage.getData().toString()+"remote message end***************************************************************");
                new Thread(new Runnable()
                        {
                    @Override
                    public void run()
                    {
                        if (!RemoteServer.isJsonValid(remoteMessage.getData().toString()))
                            return;

                        handleData(remoteMessage);
                    }
                }).start();
            }
            catch (Exception e)
            {
                Log.e(TAG,"error in onMessageReceived() method , "+e.getMessage());
            }

        }
        else
            Log.i(TAG,"remoteMessage is null");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.i(TAG,"onDeletedMessages() method called ");
    }

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        Log.i(TAG,"onNewToken() method called , token = "+s);
        SessionHelper.getInstance(getApplicationContext()).setFirebaseRegistrationToken(s);
    }



    // method to handle data came from server
    private void handleData(RemoteMessage remoteMessage)
    {
        try
        {
           if (remoteMessage.getData() != null)
           {
               JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());

               String separator = "************************************************************************************************";


               JSONObject objectData = jsonObject.getJSONObject("data");
               String title = objectData.getString("title");
               Log.i(TAG,separator+" push notification data JsonObject = "+objectData);
               Log.i(TAG," push notification title = "+title);

               if (title != null && !title.isEmpty())
               {
                   if ("Request".equalsIgnoreCase(title))
                   {
                       PreferenceNotification.addNotificationReceived();
                       Log.i(TAG,"showing notification for Request");
                       NotificationRequestHelper notificationRequestHelper = new NotificationRequestHelper(getApplicationContext());
                       notificationRequestHelper.handleData(remoteMessage);
                   }

                   if ("Request Response".equalsIgnoreCase(title))
                   {
                       PreferenceNotification.addNotificationReceived();
                       Log.i(TAG,"showing notification for Request response");
                       NotificationRequestHelper notificationRequestHelper = new NotificationRequestHelper(getApplicationContext());
                       notificationRequestHelper.handleData(remoteMessage);
                   }
                   /*// #if request is from user to see athletes progress report
                   if (title.equalsIgnoreCase("Request for School Report"))
                   {
                       Log.i(TAG,"showing notification for progress report request");
                       NotificationRequestForProgressReport notificationRequestForProgressReport = new NotificationRequestForProgressReport(getApplicationContext());
                       notificationRequestForProgressReport.handleData(objectData);
                   }
                   // checking if push notification is for connection request.
                   else if (title.equalsIgnoreCase("Friend Request send."))
                   {
                       Log.i(TAG,"showing notification for friend request request");
                       NotificationRequestHelper notificationContactHelper = new NotificationRequestHelper(getApplicationContext());
                       notificationContactHelper.handleData(remoteMessage);
                   }


                   //checking if push notification is for response to connection request.
                   else if (title.equalsIgnoreCase("Friend Request Accepted.") || title.equalsIgnoreCase("Friend Request Rejected."))
                   {
                       Log.i(TAG,"showing notification for friend request responce");
                       NotificationRequestHelper notificationContactHelper = new NotificationRequestHelper(getApplicationContext());
                       notificationContactHelper.handleData(remoteMessage);
                   }*/


                   //checking if push notification is for chat.
                   else if (title.equalsIgnoreCase("chat_detail"))
                   {
                       Log.i(TAG,"showing notification for chat");
                       NotificationChatHelper notificationChat = new NotificationChatHelper(getApplicationContext());
                       notificationChat.handleData(remoteMessage);
                   }

               }
               else
               {
                   Log.e(TAG,"title is null or empty in handleData() method ");
               }


                   /*Log.i(TAG,"jsonObject = "+jsonObject);   // get main JsonObject
                   JSONObject jsonObjectData = jsonObject.getJSONObject("data");    // getting data json object from outer most json object
                    String strMessageJson = jsonObjectData.getString("message"); // contains all details of sender
                   JSONObject jsonObjectMessage = new JSONObject(strMessageJson);
                   notificationTitle = jsonObjectData.getString("title");

                   switch (notificationTitle)
                   {
                       // when receive connection request notification
                       case "You are added by Athelete":
                            showConnectionRequestNotification(jsonObjectMessage);
                           break;

                           // connection request response, when connection request accepted, rejected or pending
                       case "You are added":
                            showConnectionRequestResponseNotification(jsonObjectMessage);
                           break;*/


                  /* JSONObject jsonObjectPayload = jsonObjectData.getJSONObject("payload");  // getting payload json object from outer data object
                //   Log.i(TAG,"jsonPayload = "+jsonPayload);
                   String strMsg = jsonObjectPayload.getString("Msg");
                   JSONObject jsonMessage = new JSONObject(strMsg);  // getting Msg json object from msg string
               //    Log.i(TAG,"jsonMessage object = "+jsonMessage);



                   // this jsonObject contains user details
                   JSONObject jsonUserDetails = jsonMessage.getJSONObject("data");  // getting data json object from Msg object*/





                   // showing notification
                   //showConnectionRequestNotification(jsonObjectMessage);

               }
           } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }






 /*   // it creates notification for connection request which contains two buttons Accept and Reject
    private void showConnectionRequestNotification(JSONObject jsonObjectMessage*//*String title, String message, String toUserId, String requestId*//*)
    {

        Log.i(TAG,"showConnectionRequestNotification() method called");
        String title = "Connection Request From";  // default title for connection request
        String message = "";  // default message for connection request
        String requestFromName = "";
        String requestToUserId = "";  // toUserId is who has sent request.
        String requestId = "";



        // fetching user details from inner data json object, this object contains all user details
        try
        {
            JSONObject jsonObjectData = jsonObjectMessage.getJSONObject("data");
            requestFromName = jsonObjectData.getString("user_name");
            requestToUserId = jsonObjectData.getString("user_id");  // toUserId is who has sent request
            message = requestFromName.toUpperCase();

        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(TAG,"Error while parsing json in showConnectionRequestNotification() method ");
        }





        // intent to start ActivityNotification when user clicks on notification. it means request is in pending
        Intent intent = getIntentForConnectionRequest(requestToUserId,requestId,Request.REQUEST_STATUS_PENDING);

        // intent to start ActivityNotification when user clicks on Accept button
        Intent intentAcceptRequest = getIntentForConnectionRequest(requestToUserId,requestId,Request.REQUEST_STATUS_ACCEPT);  // status 1 is for accepting request
        // intent to start ActivityNotification when user clicks on Reject button
        Intent intentRejectRequest = getIntentForConnectionRequest(requestToUserId,requestId,Request.REQUEST_STATUS_REJECT);  // status 2 is for rejecting request


        // pending intent for pending request
        PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        // pending intent for accepting request
        PendingIntent pendingIntentAccept = PendingIntent.getActivity(this,NOTIFICATION_ID,intentAcceptRequest,PendingIntent.FLAG_UPDATE_CURRENT);
        //pending intent for rejecting request
        PendingIntent pendingIntentReject = PendingIntent.getActivity(this,NOTIFICATION_ID,intentRejectRequest,PendingIntent.FLAG_UPDATE_CURRENT);


        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);  // getting default notification URI to notify



        // creating notification Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"10")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                *//* .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))*//*  // to show icon on right side
                .setAutoCancel(false)
                .setVibrate(pattern)
                .setLights(Color.YELLOW,1,1)
                .setSound(defaultSoundUri)
                .addAction(R.drawable.ic_like,"Accept", pendingIntentAccept)
                .addAction(R.drawable.ic_cancel,"Reject", pendingIntentReject)

                .setContentIntent(pendingIntent);


                Log.i(TAG,"notification id = "+NOTIFICATION_ID);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = getApplicationContext().getResources().getString(R.string.default_notification_chanel_id);
            NotificationChannel notificationChannel = new NotificationChannel(channelId,"Connection Request",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel");

            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setChannelId(channelId);
        }


        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());


    }


    // this method returns intent object for starting ActivityNotification, for connection request
    private Intent getIntentForConnectionRequest(String toUserId, String requestId, String status)
    {
        Log.i(TAG,"getIntentForConnectionRequest() method called");

        // Log.i(TAG,"status = "+status);
        Intent intentRequest = new Intent(this, ActivityNotification.class);
        Request connectionRequest = new Request();

        connectionRequest.setFromId(SessionHelper.getInstance(getBaseContext()).getUser().getUserId());  // fromId is the request receiver
      //  connectionRequest.setRequestId(requestId);
        connectionRequest.setStatus(status);  // status is for connection request whether request has been accepted or not
        connectionRequest.setToId(toUserId);  // toUserId is who has sent rquest

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
        intentRequest.putExtra(ActivityNotification.KEY_NOTIFICATION_ID,NOTIFICATION_ID);
        intentRequest.putExtra(ActivityNotification.KEY_NOTIFICATION_TYPE, ActivityNotification.TYPE_CONNECTION_REQUEST);  // setting notification type
        intentRequest.putExtra(ActivityNotification.KEY_CONNECTION_REQUEST_RESPONSE_DATA, connectionRequest);   // putting Request object, to send

        return intentRequest;
    }



    // it creates notification for connection request response which contains message whether request has been accepted or not
    private void showConnectionRequestResponseNotification(JSONObject jsonObjectMessage)
    {
        Log.i(TAG,"showConnectionRequestResponseNotification() method called");

        String title = "Connection Request Accepted";  // default title for connection request
        String message = "";  // contains user name
        String name = "";
        String userId = "";  // toUserId is who has sent connection request.
        String status = "";  // contains status of request whether request has been accepted, rejected or kept pending


        // fetching user details from inner data json object, this object contains all user details
        try
        {
            status = jsonObjectMessage.getString("status");
            JSONObject jsonObjectData = jsonObjectMessage.getJSONObject("data");
            name = jsonObjectData.getString("user_name");
            userId = jsonObjectData.getString("user_id");  // toUserId is who has sent request
            message = name.toUpperCase();


            switch (status)
            {
                case "0":
                    title = "Connection Request Pending";
                    break;

                case "1":
                    title = "Connection Request Accepted";
                    break;

                case "2":
                    title = "Connection Request Rejected";
                    break;
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(TAG,"Error while parsing json in showConnectionRequestResponse() method ");
        }



        // Log.i(TAG,"status = "+status);
        Intent intent = new Intent(this, ActivityContact.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

          // pending intent for pending request
        PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

      *//*  // intent to start ActivityNotification when user clicks on Accept button
        Intent intentAcceptRequest = getIntentForConnectionRequest(requestToUserId,requestId,Request.REQUEST_STATUS_ACCEPT);  // status 1 is for accepting request
        // intent to start ActivityNotification when user clicks on Reject button
        Intent intentRejectRequest = getIntentForConnectionRequest(requestToUserId,requestId,Request.REQUEST_STATUS_REJECT);  // status 2 is for rejecting request

        // pending intent for accepting request
        PendingIntent pendingIntentAccept = PendingIntent.getActivity(this,NOTIFICATION_ID,intentAcceptRequest,PendingIntent.FLAG_UPDATE_CURRENT);
        //pending intent for rejecting request
        PendingIntent pendingIntentReject = PendingIntent.getActivity(this,NOTIFICATION_ID,intentRejectRequest,PendingIntent.FLAG_UPDATE_CURRENT);
 *//*

        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);  // getting default notification URI to notify



        Log.i(TAG,"creating notification builder");
        // creating notification Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"10")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                *//* .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))*//*  // to show icon on right side
                .setAutoCancel(false)
                .setVibrate(pattern)
                .setLights(Color.YELLOW,1,1)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);


     //   Log.i(TAG,"notification id = "+NOTIFICATION_ID);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.i(TAG,"showing notification");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = getApplicationContext().getResources().getString(R.string.default_notification_chanel_id);
            NotificationChannel notificationChannel = new NotificationChannel(channelId,"Connection Request",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel");

            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setChannelId(channelId);
        }

        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());


    }



    // it contains jsonObject for testing only
   *//* private JSONObject getJsonForTesting()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject("{\n" +
                          "  \"data\": {\n" +
                          "    \"image\": \"\",\n" +
                          "    \"is_background\": false,\n" +
                          "    \"payload\": {\n" +
                          "      \"Msg\": {\n" +
                          "        \"status\": \"1\",\n" +
                          "        \"message\": \"Request for adding\",\n" +
                          "        \"id\": 26,\n" +
                          "        \"data\": {\n" +
                          "          \"user_id\": \"78\",\n" +
                          "          \"user_name\": \"ranjeet yadav\",\n" +
                          "          \"user_email\": \"ranjit@gmail.com\",\n" +
                          "          \"user_password\": \"77b116c99bce06fb11dfb6d79814f718\",\n" +
                          "          \"athelete_spot_code\": \"\",\n" +
                          "          \"address\": \"\",\n" +
                          "          \"profile_image\": \"\",\n" +
                          "          \"bio_details\": \"\",\n" +
                          "          \"user_otp\": \"720613\",\n" +
                          "          \"AuthToken\": \"MTA:\",\n" +
                          "          \"user_role\": \"1\",\n" +
                          "          \"user_status\": \"1\",\n" +
                          "          \"regi_date\": \"2018-09-20 00:51:38\",\n" +
                          "          \"user_activity\": \"1\",\n" +
                          "          \"mobile_token\": \"djU2GI38bQQ:APA91bHi96YdXtrvUMZnj5rFN8BYvE763oJ6QJHWndoZSfghMS0UGiav-6-pkE3D2O-gWrdRcKu98_NDsWG0CKSDH36nDT1Nui3GdFaxmqjnpXTB7k44mMES9pVyJIqgReRRY_Mo_O8i\",\n" +
                          "          \"athlete_playlist\": \"\",\n" +
                          "          \"athlete_highlight\": \"\"\n" +
                          "        }\n" +
                          "      }\n" +
                          "    },\n" +
                          "    \"title\": \"You are added by Athelete\",\n" +
                          "    \"message\": {\n" +
                          "      \"status\": \"1\",\n" +
                          "      \"message\": \"Request for adding\",\n" +
                          "      \"id\": 26,\n" +
                          "      \"data\": {\n" +
                          "        \"user_id\": \"78\",\n" +
                          "        \"user_name\": \"ranjeet yadav\",\n" +
                          "        \"user_email\": \"ranjit@gmail.com\",\n" +
                          "        \"user_password\": \"77b116c99bce06fb11dfb6d79814f718\",\n" +
                          "        \"athelete_spot_code\": \"\",\n" +
                          "        \"address\": \"\",\n" +
                          "        \"profile_image\": \"\",\n" +
                          "        \"bio_details\": \"\",\n" +
                          "        \"user_otp\": \"720613\",\n" +
                          "        \"AuthToken\": \"MTA:\",\n" +
                          "        \"user_role\": \"1\",\n" +
                          "        \"user_status\": \"1\",\n" +
                          "        \"regi_date\": \"2018-09-20 00:51:38\",\n" +
                          "        \"user_activity\": \"1\",\n" +
                          "        \"mobile_token\": \"djU2GI38bQQ:APA91bHi96YdXtrvUMZnj5rFN8BYvE763oJ6QJHWndoZSfghMS0UGiav-6-pkE3D2O-gWrdRcKu98_NDsWG0CKSDH36nDT1Nui3GdFaxmqjnpXTB7k44mMES9pVyJIqgReRRY_Mo_O8i\",\n" +
                          "        \"athlete_playlist\": \"\",\n" +
                          "        \"athlete_highlight\": \"\"\n" +
                          "      }\n" +
                          "    },\n" +
                          "    \"timestamp\": \"2018-09-20 7:52:14\"\n" +
                          "  }\n" +
                          "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }*//*

*/
}



