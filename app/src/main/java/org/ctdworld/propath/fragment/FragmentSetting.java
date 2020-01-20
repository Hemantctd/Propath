package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityResetPassword;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSetting extends PreferenceFragment
{
    private static final String TAG = FragmentSetting.class.getSimpleName();

    Context mContext;
   SwitchPreference mSwitchNotification;
   Preference /*mEditProfile,*/ mChangePassword, mAboutUs /*mFAQ*//*, mFeedback*/;

    public FragmentSetting() {
    }  // Required empty public constructor



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mInitialize();
      /*  mManageLanguagePreference();
        mManageSportPreference();*/

       //mSwitchArchive.setChecked(true);


        setNotificationChecked();
       onChagePasswordClicked();
     //  onEditProfileClicked();
       //onFAQClicked();
    //   handleFeedback();


    }

    // checking if notification is enabled or not in SharedPreference, if it's enabled then it will be checked
    private void setNotificationChecked() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (preferences != null)
        {
            boolean isChecked = preferences.getBoolean(getString(R.string.preference_notification), true);
            if (isChecked)
            {
                mSwitchNotification.setChecked(true);
                Log.i(TAG,"setting notification is enabled");

            }
            else
                Log.i(TAG,"setting notification is disabled");
        }
        else
            Log.e(TAG,"setting notification , preference is null");


    }


    private void mInitialize()
    {
        mContext = getActivity();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

       // mEditProfile = preferenceScreen.findPreference(getString(R.string.preference_edit_profile));
        mChangePassword = preferenceScreen.findPreference(getString(R.string.preferenc_change_password));
        //mFAQ = preferenceScreen.findPreference(getString(R.string.preference_faq));
        mAboutUs = preferenceScreen.findPreference(getString(R.string.preference_about_us));
//        mSwitchArchive = (SwitchPreference) preferenceScreen.findPreference(getString(R.string.preference_archive));
        mSwitchNotification = (SwitchPreference) preferenceScreen.findPreference(getString(R.string.preference_notification));
        //mFeedback = preferenceScreen.findPreference(getString(R.string.preference_feedback));

    }


  /*  private void handleFeedback() {
        mFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(mContext,ActivityFeedback.class));
                return false;
            }
        });
    }*/


    private void onChagePasswordClicked() {
        Log.i(TAG,"handleChagePassword() method called");

        mChangePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG,"change password clicked");
                startActivity(new Intent(getActivity(), ActivityResetPassword.class));
                return true;
            }
        });




       /* int editResource = mChangePassword.getLayoutResource();
        View editProfile = getActivity().getLayoutInflater().inflate(editResource,null);
        TextView textView = editProfile.findViewById(R.id.edit_profile_txt);*/

    }


    private void onEditProfileClicked() {
        Log.i(TAG,"handleChagePassword() method called");

        // # it's working code
       /* mEditProfile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG,"change password clicked");
                startActivity(new Intent(getActivity(), ActivityProfileUpdate.class));
                return true;
            }
        });*/

       /* int editResource = mChangePassword.getLayoutResource();
        View editProfile = getActivity().getLayoutInflater().inflate(editResource,null);
        TextView textView = editProfile.findViewById(R.id.edit_profile_txt);*/

    }



   /* private void onFAQClicked() {
        Log.i(TAG,"handleChagePassword() method called");

        mFAQ.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG,"change password clicked");
                startActivity(new Intent(getActivity(), ActivityFAQ.class));
                return true;
            }
        });*/


       /* int editResource = mChangePassword.getLayoutResource();
        View editProfile = getActivity().getLayoutInflater().inflate(editResource,null);
        TextView textView = editProfile.findViewById(R.id.edit_profile_txt);*/

    }


