package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;


import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractEmploymentTools;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.model.CareerEmploymentData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InteracterEmploymentTools implements ContractEmploymentTools.Interactor
{
    private static final String TAG = InteracterEmploymentTools.class.getSimpleName();
    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;


    public InteracterEmploymentTools(OnFinishedListener listener, Context context)
    {
        this.mListener = listener;
        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }



    // getting EmploymentTools list from server
    @Override
    public void requestEmploymentList(CareerEmploymentData employmentTools)
    {
        Map<String, String> params = new HashMap<>();
        params.put("welfare_get_docs","1");
        params.put("categroy",employmentTools.getEmploymentCategory());

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"remote message for employment tools list = " +message);
                try
                {
                    final JSONObject jsonObject = new JSONObject(message);
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run() {
                            parseJsonAndUpdateUi(jsonObject);
                        }
                    }).start();

                }
                catch (JSONException e)
                {
                    mListener.onFailed("Failed...");
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onFailed("");
                error.printStackTrace();
                Log.e(TAG,"error while getting list of employment tools");
            }
        });
    }



    // uploading employment tools on server
    @Override
    public void uploadEmployment(final CareerEmploymentData employmentTools)
    {
        Log.i(TAG,"uploadEmployment() method called...........................");

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                    Map<String,String> params = new HashMap<>();

                    params.put("","1");
                    params.put("file_name", employmentTools.getFileName());
                 //   params.put("file", employmentTools.getFileBase64());

                    Log.i(TAG,"params to upload employment file = "+params);

                    mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
                    {
                        @Override
                        public void onVolleySuccessResponse(String message)
                        {
                            Log.i(TAG,"remote message for employment tools list = " +message);
                            try
                            {
                                JSONObject jsonObject = new JSONObject(message);
                                if (jsonObject != null)
                                {
                                    if ("1".contains(jsonObject.getString("")))
                                    {
                                        mListener.onUploadSuccess();
                                    }
                                    else
                                        mListener.onFailed("");
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onVolleyErrorResponse(VolleyError error)
                        {
                            mListener.onFailed("");
                            error.printStackTrace();
                            Log.e(TAG,"error while getting list of employment tools");
                        }
                    });

            }
        }).start();
    }

    @Override
    public void deleteFile(String fileId, final int positionInAdapter)
    {

        // below 1 line is just for testing
     //   mListener.onFileDeleted("File Deleted Successfully...", positionInAdapter);

          Map<String, String> params = new HashMap<>();
        params.put("welfare_delete_docs","1");
        params.put("docs_id",fileId);


        Log.i(TAG,"params to delete employment file = "+params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"remote message for deleting = " +message);
                try
                {
                    final JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("success")))
                    {
                        mListener.onFileDeleted("File Deleted Successfully", positionInAdapter);
                    }
                    else
                    {
                        mListener.onFailed(jsonObject.getString("message"));
                    }

                }
                catch (JSONException e)
                {
                    mListener.onFailed("File Not Deleted...");
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onFailed("File Not Deleted...");
                error.printStackTrace();
                Log.e(TAG,"error while getting list of employment tools");
            }
        });
    }


    // this method parses json and passing data to activity to update
    private void parseJsonAndUpdateUi(JSONObject jsonObject)
    {
        try
        {
            DateTimeHelper dateTimeHelper = new DateTimeHelper();
            ArrayList<CareerEmploymentData> listEmploymentTools = new ArrayList<>();
            if (jsonObject != null)
            {
                if ("0".equals(jsonObject.getString("res")))
                {
                    mListener.onFailed(jsonObject.getString("message"));
                    return;
                }


                JSONArray jsonArray = jsonObject.getJSONArray("data");


                if (jsonArray != null)
                {
                    for (int i=0 ; i<jsonArray.length() ; i++)
                    {
                        JSONObject objectData = jsonArray.getJSONObject(i);
                        if (objectData != null)
                        {
                            CareerEmploymentData employmentTools = new CareerEmploymentData();
                            employmentTools.setFileId(objectData.getString("id"));
                            employmentTools.setFileName(objectData.getString("file_name"));

                            String date = objectData.getString("date_time");
                            employmentTools.setFileDate(dateTimeHelper.getDateStringDayMonthYear(date));
                            employmentTools.setFileTime(dateTimeHelper.getTime(date));
                            employmentTools.setFileUrl(objectData.getString("file_url"));

                            listEmploymentTools.add(employmentTools);
                            if (i == (jsonArray.length()-1))  // checking whether i is last index or not
                                mListener.onReceivedEmploymentList(listEmploymentTools);

                        }
                    }
                }
                else
                    mListener.onFailed(null);

            }
            else
                mListener.onFailed(null);

        }
        catch (JSONException e)
        {
            mListener.onFailed("Failed...");
            e.printStackTrace();
        }
    }








}
