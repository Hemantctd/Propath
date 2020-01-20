package org.ctdworld.propath.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Role;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleHelper
{
    public static final String COACH_ROLE_ID =  "1";
    static final String MANAGER_ROLE_ID =  "2";
    public static final String TRAINER_ROLE_ID =  "3";
    private static final String PHYSIO_ROLE_ID =  "4";
    private static final String DOCTOR_ROLE_ID =  "5";
    static final String CLUB_MANAGEMENT_ROLE_ID =  "6";
    static final String PLAYER_AGENT_ROLE_ID =  "7";
    public static final String NUTRITIONIST_ROLE_ID =  "8";
    public static final String STATISTICIAN_ROLE_ID =  "9";
    public static final String TEACHER_ROLE_ID =  "10";
    public static final String PLAYER_WELFARE_ROLE_ID =  "11";
    static final String PARENT_GUARDIAN_ROLE_ID =  "12";
    public static final String ATHLETE_ROLE_ID =  "13";
    static final String MENTOR_ROLE_ID =  "14";




    private final String TAG = RoleHelper.class.getSimpleName();

    private RemoteServer remoteServer;
    private static RoleHelper roleHelper = null;
    private List<Role> roleList;
    private static OnReceivedRoleListListener roleListListener;


    public interface OnReceivedRoleListListener
    {
        void onRoleListReceived(List<Role> roleList);
    }


    // private constructor
    private RoleHelper(Context context)
    {
        Log.i(TAG,"RoleHelper constructor called ");
        remoteServer = new RemoteServer(context);
        this.roleList = new ArrayList<>();
    }



    public static RoleHelper getInstance(Context context)
    {
        if (roleHelper == null)
            roleHelper = new RoleHelper(context);
        return roleHelper;
    }



    //  returns instance and receives OnReceivedRoleListListener reference to provide Role list
    public static RoleHelper getInstance(Context context, OnReceivedRoleListListener roleListener)
    {
        roleListListener = roleListener;
        if (roleHelper == null)
            roleHelper = new RoleHelper(context);
        return roleHelper;
    }






    // getting role list from server
    public void createRoleFromServer()
    {

        Log.i(TAG,"createRoleFromServer() method called ");

        Map<String,String> params = new HashMap<>();
        params.put("get_all_roles","1");

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get all roles = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("res")))
                        createRoleList(jsonObject.getJSONArray("message"));
                    else
                        Log.e(TAG,"No role found in createRoleFromServer() method ");

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in createRoleFromServer() method , "+e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestAllUsers() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });

    }



    // creating role list from JsonObject
    private void createRoleList(JSONArray jsonArray)
    {
        // clearing roleList before adding role to ensure no duplicate data is being added
        roleList.clear();
        if (jsonArray != null)
        {
            for (int i=0 ; i<jsonArray.length(); i++)
            {

                try
                {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Role role = new Role();
                    role.setId(jsonObject.getString("role_id"));
                    role.setName(jsonObject.getString("role_name"));
                    role.setStatus(jsonObject.getString("status"));

                    roleList.add(role);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


            }
        }

        // if roleListListener is not null then role roleList will be passed to listener method
        if (roleListListener != null)
        {
            roleListListener.onRoleListReceived(roleList);
        }
    }



    public List<String> getRoleNameList()
    {
        List<String> listName = new ArrayList<>();
        if (roleList != null)
        {
            for (int i=0 ; i<roleList.size() ; i++)
            {
                listName.add(roleList.get(i).getName());
            }
        }

        return listName;
    }

    public List<Integer> getRoleIdList()
    {
        List<Integer> listRoleId = new ArrayList<>();
        if (roleList != null)
        {
            for (int i=0 ; i<roleList.size() ; i++)
            {
                int roleId = Integer.parseInt(roleList.get(i).getId());
                listRoleId.add(roleId);
            }
        }

        return listRoleId;
    }

    public List<String> getRoleIdLists()
    {
        List<String> listRoleId = new ArrayList<>();
        if (roleList != null)
        {
            for (int i=0 ; i<roleList.size() ; i++)
            {
                String roleId = roleList.get(i).getId();
                listRoleId.add(roleId);
            }
        }

        return listRoleId;
    }

    public String getRoleNameById(String roleID)
    {
        String roleName = "";

        switch (roleID)
        {
            case COACH_ROLE_ID :
                roleName = "Coach";
                break;
                
            case MANAGER_ROLE_ID :
                roleName = "Manager";
                break;

            case TRAINER_ROLE_ID :
                roleName = "Trainer";
                break;

            case PHYSIO_ROLE_ID :
                roleName = "Physio";
                break;

            case DOCTOR_ROLE_ID :
                roleName = "Doctor";
                break;

            case CLUB_MANAGEMENT_ROLE_ID :
                roleName = "Club Management";
                break;

            case PLAYER_AGENT_ROLE_ID :
                roleName = "Player Agent";
                break;

            case NUTRITIONIST_ROLE_ID :
                roleName = "Nutritionist";
                break;

            case STATISTICIAN_ROLE_ID :
                roleName = "Statistician";
                break;

            case TEACHER_ROLE_ID :
                roleName = "Teacher";
                break;

            case PLAYER_WELFARE_ROLE_ID :
                roleName = "Player Welfare";
                break;

            case PARENT_GUARDIAN_ROLE_ID :
                roleName = "Parent/Guardian";
                break;

            case ATHLETE_ROLE_ID :
                roleName = "Athlete";
                break;

            case MENTOR_ROLE_ID :
                roleName = "Mentor";
                break;
        }

        return roleName;
    }



    // returns role id based on role name
    public String getRoleIdByName(String roleName)
    {
        String roleID = "0";

        switch (roleName)
        {
            case  "Coach":
                roleID = COACH_ROLE_ID;
                break;

            case  "Manager":
                roleID = MANAGER_ROLE_ID;
                break;

            case  "Trainer":
                roleID = TRAINER_ROLE_ID;
                break;

            case  "Physio":
                roleID = PHYSIO_ROLE_ID;
                break;

            case  "Doctor":
                roleID = DOCTOR_ROLE_ID;
                break;

            case  "Club Management":
                roleID = CLUB_MANAGEMENT_ROLE_ID;
                break;

            case  "Player Agent":
                roleID = PLAYER_AGENT_ROLE_ID;
                break;

            case  "Nutritionist":
                roleID = NUTRITIONIST_ROLE_ID;
                break;

            case  "Statistician":
                roleID = STATISTICIAN_ROLE_ID;
                break;

            case "Teacher" :
                roleID = TEACHER_ROLE_ID;
                break;

            case  "Player Welfare":
                roleID = PLAYER_WELFARE_ROLE_ID;
                break;

            case  "Parent/Guardian":
                roleID = PARENT_GUARDIAN_ROLE_ID;
                break;

            case  "Athlete":
                roleID = ATHLETE_ROLE_ID;
                break;

            case  "Mentor":
                roleID = MENTOR_ROLE_ID;
                break;
        }

        return roleID;
    }


}