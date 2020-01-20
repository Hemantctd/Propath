package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractEvents;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Event;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InteractorEvents implements ContractEvents.Interactor
{
    private static final String TAG = InteractorEvents.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context mContext;



    // BASE URL TO GET POSTED IMAGE, WE HAVE TO ADD IMAGE NAME IN THIS URL
    private static final String ALL_POST_IMAGE_BASE_URL = "https://ctdworld.org/rahul/sdf/sdf-admin/upload_images/post_image/";  // BASE URL TO GET IMAGE

    // key to send data to server
    private static final String KEY_ALL_EVENTS_DEFAULT_VALUE = "get_all_events"; // to send images to server
    private static final String ALL_EVENTS_DEFAULT_VALUE = "1"; // to send images to server

    // server message key to get data from json sent from server
    private static final String RES_KEY_SUCCESS = "success";
    private static final String RES_VALUE_SUCCESS= "1";
    private static final String RES_VALUE_FAILED= "0";

    private static final String RES_KEY_MESSAGE_ARRAY= "message";

    private static final String RES_KEY_ID= "event_id";
    private static final String RES_KEY_TITLE= "title";
    private static final String RES_KEY_LOCATION= "event_location";
    private static final String RES_KEY_EVENT_DATE_TIME= "eventdate";
    private static final String RES_KEY_LOCATION_LATITUDE= "lat";
    private static final String RES_KEY_LOCATION_LONGITUDE= "long";


    public InteractorEvents(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;
    }


    @Override
    public void requestEventsData()
    {
        Log.i(TAG,"requestEventsData() method called , thread = "+Thread.currentThread().getId());
        Map<String,String> params = new HashMap<>();
        params.put(KEY_ALL_EVENTS_DEFAULT_VALUE,ALL_EVENTS_DEFAULT_VALUE);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
             //   Log.i(TAG,"requestEventsData() method, onSuccessResponse() called , thread = "+Thread.currentThread().getId());
                Log.i(TAG,"server response in requestEventsData() method = "+message.trim());  // to check server response in logcat
                try
                {

                    final JSONObject jsonObject = new JSONObject(message.trim());
                    if (RES_VALUE_SUCCESS.equals(jsonObject.getString(RES_KEY_SUCCESS)))
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    updateUi(jsonObject.getJSONArray(RES_KEY_MESSAGE_ARRAY));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                    else if (RES_VALUE_FAILED.equals(jsonObject.getString(RES_KEY_SUCCESS)))
                        listener.onShowMessage("Failed...");

                }
                catch (JSONException e)
                {
                 //   listener.onShowMessage("Error Occurred...");
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onShowMessage("Connection Error...");
            }
        });
    }


    private void updateUi(JSONArray jsonArray)
    {
        Log.i(TAG,"********************************************************************************************************************");
     //   Log.i(TAG,"updateUi() called , thread = "+Thread.currentThread().getId());
        List<Event> listUpcomingEvents = new ArrayList<>();
        List<Event> listTodayEvents = new ArrayList<>();
        try
        {
            if (jsonArray != null)
            {
                for (int i=0 ; i<jsonArray.length() ; i++)
                {
                  //  Log.i(TAG,"********************************************************************************************************************");
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Event event =  getEventFromJson(jsonObject);   // getting one event from Json

                    // upcoming events
                    Event upcomingEvent = getUpcomingEvent(event);  // getting upcoming events
                    if (upcomingEvent != null)
                    listUpcomingEvents.add(upcomingEvent);


                    // upcoming events
                    Event todayEvent = getTodayEvent(event);  // getting today events
                    if (todayEvent != null)
                        listTodayEvents.add(todayEvent);
                }

                listener.onUpdateUi(listUpcomingEvents, listTodayEvents);
            }
            else
                Log.i(TAG,"jsonArray is null in updateUi() method");

        }
        catch (Exception e)
        {
           // listener.onShowMessage("Error Occurred...");
            Log.e(TAG,"error in updateUi , "+e.getMessage());
            e.printStackTrace();
        }
    }


    private Event getEventFromJson(JSONObject jsonObject)
    {
      //  Log.i(TAG,"getEventFromJson() method called , thread = "+Thread.currentThread().getId());
        Event event = new Event();

        try
        {
            event.setId(jsonObject.getString(RES_KEY_ID));
            event.setDateTime(jsonObject.getString(RES_KEY_EVENT_DATE_TIME));
            event.setLocationName(jsonObject.getString(RES_KEY_LOCATION));
            event.setTitle(jsonObject.getString(RES_KEY_TITLE));

            event.setLatitude(jsonObject.getString(RES_KEY_LOCATION_LATITUDE));
            event.setLongitude(jsonObject.getString(RES_KEY_LOCATION_LONGITUDE));
        }
        catch (JSONException e)
        {
            Log.e(TAG,"Error in getEventFromJson() method , "+e.getMessage());
            e.printStackTrace();
        }
        return event;
    }





    // returns upcoming event
    private Event getUpcomingEvent(Event event)
    {
        //Log.i(TAG,"getUpcomingEvent() method called , thread = "+Thread.currentThread().getId());

        //String eventDateTime = "2018-08-02 09:42:26";
        String eventDateTime = event.getDateTime();



        try {
            SimpleDateFormat formatOriginal = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
            Date eventDateParsed = formatOriginal.parse(eventDateTime);

            SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());

            String strEventDate = eventDateFormat.format(eventDateParsed);
            event.setDate(strEventDate);   // adding date in event
          //  Log.i(TAG,"strEventDate = "+strEventDate);
            Date eventDate = eventDateFormat.parse(strEventDate.trim());
          //  Log.i(TAG,"eventDate = "+eventDate);


            Date currentDate = new Date();
            String stringFormattedCurrent = eventDateFormat.format(currentDate);
            Date formatDateCurrent = eventDateFormat.parse(stringFormattedCurrent);
         //   Log.i(TAG,"currentDate = "+formatDateCurrent);


            if (eventDate.getTime()>formatDateCurrent.getTime())
            {
                Long dateInLong = eventDateParsed.getTime();
                event.setDateInLong(dateInLong);  // adding date in long

                SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm",Locale.getDefault());
                String eventTime = formatTime.format(eventDateParsed);
            //    Log.i(TAG,"event time = "+eventTime);
                event.setTime(eventTime);  // adding event time to event
                return event;
            }
            else
                return null;


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    // returns today event
    private Event getTodayEvent(Event event)
    {
      //  Log.i(TAG,"getTodayEvent() method called , thread = "+Thread.currentThread().getId());
       // String eventDateTime = "2018-08-03 09:42:26";
        String eventDateTime = event.getDateTime();



        try {
            SimpleDateFormat formatOriginal = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
            Date eventDateParsed = formatOriginal.parse(eventDateTime);

            SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());

            String strEventDate = eventDateFormat.format(eventDateParsed);
            event.setDate(strEventDate);   // adding event date to event
        //    Log.i(TAG,"strEventDate = "+strEventDate);
            Date eventDate = eventDateFormat.parse(strEventDate.trim());
        //    Log.i(TAG,"eventDate = "+eventDate);


            Date currentDate = new Date();
            String stringFormattedCurrent = eventDateFormat.format(currentDate);
            Date formatDateCurrent = eventDateFormat.parse(stringFormattedCurrent);
       //     Log.i(TAG,"currentDate = "+formatDateCurrent);


            if (eventDate.getTime() == formatDateCurrent.getTime())
            {
                Long dateInLong = eventDateParsed.getTime();
                event.setDateInLong(dateInLong);    // adding event date in long

                SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm",Locale.getDefault());
                String eventTime = formatTime.format(eventDateParsed);
           //     Log.i(TAG," today event time = "+eventTime);
                event.setTime(eventTime);    //  adding event time to event
                return event;
            }
            else
                return null;


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
