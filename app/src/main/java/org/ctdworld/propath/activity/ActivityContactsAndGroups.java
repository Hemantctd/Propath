package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterContactsAndGroupList;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.ContactAndGroup;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// # this activity  is being used to get all friends and groups of user
public class ActivityContactsAndGroups extends AppCompatActivity
{
    private final String TAG = ActivityContactsAndGroups.class.getSimpleName();
    public static final int REQUEST_CODE_ACTIVITY_CHAT = 100 ; //# to get data back from ActivityChat

    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTitle;
    EditText mEditFilterList;
    AdapterContactsAndGroupList mAdapter;
    DialogLoader mLoader;

    RecyclerView mRecycler;
    SwipeRefreshLayout mRefreshLayout;

    List<ContactAndGroup> mListContactsAndGroups = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_and_groups_list);
        init();
        setToolbar();
        setUpAdapter();
        loadContactsAndGroups();

        // to filter list
        mEditFilterList.addTextChangedListener(onTextTypedToFilterList);
    }

    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRefreshLayout = findViewById(R.id.activity_contact_and_group_list_refresh_layout);
        mRecycler = findViewById(R.id.activity_contact_and_groups_list_recycler);
        mEditFilterList = findViewById(R.id.edit_search);
    }



    TextWatcher onTextTypedToFilterList = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    filterList(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            };


    // to filter contacts and groups by name
    private void filterList(String filterText)
    {
        //new array list that will hold the filtered data
        ArrayList<ContactAndGroup> filteredList = new ArrayList<>();

        //looping through existing elements
        for (ContactAndGroup contactAndGroup : mListContactsAndGroups)
        {
            //if the existing elements contains the search input
            String name = contactAndGroup.getName();
            if (name != null)
            {
                if (filterText != null && !filterText.isEmpty() && name.toLowerCase().contains(filterText.toLowerCase()))
                {

                    //adding the element to filtered list
                    filteredList.add(contactAndGroup);
                }
            }

        }
        mAdapter.filterList(filteredList);
    }

      private void setUpAdapter()
    {
        List<ContactAndGroup> mListItems = new ArrayList<>();

         mAdapter = new AdapterContactsAndGroupList(mContext,mListItems, adapterListener);
       // contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);

    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Select contact or group");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }


    AdapterContactsAndGroupList.AdapterContactsAndGroupListListener adapterListener = new AdapterContactsAndGroupList.AdapterContactsAndGroupListListener() {
        @Override
        public void onSelected(ContactAndGroup contactAndGroup)
        {
            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.DATA_MODAL, contactAndGroup);
            setResult(RESULT_OK, intent);
            finish();
        }
    };



    private void loadContactsAndGroups()
    {
        Log.i(TAG,"loadContactsAndGroups() method called");

        RemoteServer remoteServer = new RemoteServer(mContext);

        Map<String,String> params = new HashMap<>();
        params.put("get_frnd_group_notes","1");
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());


        Log.i(TAG,"params to get all friends = "+ params);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get all contacts and group of user = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("res")))
                    {
                        mListContactsAndGroups = getAllContactsAndGroups(jsonObject.getJSONArray("result"));
                        mAdapter.addUserList(mListContactsAndGroups);
                    }
                    else if("0".equals(jsonObject.getString("res")))
                        DialogHelper.showSimpleCustomDialog(mContext,jsonObject.getString("message"));
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.failed_title));

                    hideLoader();

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in requestAllContacts() method , "+e.getMessage());
                    hideLoader();
                    e.printStackTrace();
                    DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.failed_title));
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestAllContacts() method volley error = "+error.getMessage());
                error.printStackTrace();
                hideLoader();
                DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.failed_title));
            }
        });
    }



    // hiding loader
    private void hideLoader()
    {
        if (mLoader != null && mLoader.isAdded())
            mLoader.dismiss();
    }

    private List<ContactAndGroup> getAllContactsAndGroups(JSONArray jsonArray)
    {
        List<ContactAndGroup> contactAndGroupsList = new ArrayList<>();
        for (int i=0 ; i<jsonArray.length() ; i++)
        {
            try
            {
                JSONObject object = jsonArray.getJSONObject(i);
                ContactAndGroup contactAndGroup = new ContactAndGroup();
                contactAndGroup.setId(object.getString("id"));
                contactAndGroup.setName(object.getString("name"));
                contactAndGroup.setPicUrl(object.getString("image"));
                contactAndGroup.setType(object.getString("type"));


                contactAndGroupsList.add(contactAndGroup);


            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in getAllContacts() method , "+e.getMessage());
                e.printStackTrace();
            }
        }
        return contactAndGroupsList;
    }

}
