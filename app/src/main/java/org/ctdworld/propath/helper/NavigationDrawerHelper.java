package org.ctdworld.propath.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityContact;
import org.ctdworld.propath.activity.ActivityFeedback;
import org.ctdworld.propath.activity.ActivityGetAllSharedSurvey;
import org.ctdworld.propath.activity.ActivityNoteContainer;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.activity.ActivitySettings;
import org.ctdworld.propath.activity.ActivitySurvey;
import org.ctdworld.propath.prefrence.SessionHelper;

public class NavigationDrawerHelper {

    private Fragment fragmentToShow = null;
    private String tag = null;
    private boolean addToBackStack = false;

    private Intent intent = null;

    public void setIntentToStartActivity(Context context, int menuItemId) {
        switch (menuItemId) {

            case R.id.menu_profile:

                intent = new Intent(context, ActivityProfileView.class);
                // putting profile type self or other to show profile accordingly
                //  intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_SELF);
                // putting userId to get profile
                intent.putExtra(ConstHelper.Key.ID, SessionHelper.getInstance(context).getUser().getUserId());


                // below code is for deciding which page to open , profile view or profile update
               /* if (PreferenceNotification.getInstance(context).getProfileUpdateStatus())
                {
                    intent = new Intent(context, ActivityProfileView.class);
                    // putting profile type self or other to show profile accordingly
                    intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_SELF);
                    // putting userId to get profile
                    intent.putExtra(ConstHelper.Key.ID,SessionHelper.getInstance(context).getUser().getUserId());
                }
                else
                    intent = new Intent(context,ActivityProfileUpdate.class);*/
                break;

           /* case R.id.menu_search:
                intent = new Intent(context, ActivitySearch.class);
                break;*/

            case R.id.menu_survey:

                int role = Integer.parseInt(SessionHelper.getInstance(context).getUser().getRoleId());
                int roleManager = Integer.parseInt(RoleHelper.MANAGER_ROLE_ID);
                int roleWelfare = Integer.parseInt(RoleHelper.PLAYER_WELFARE_ROLE_ID);
                int roleClub = Integer.parseInt(RoleHelper.CLUB_MANAGEMENT_ROLE_ID);
                int roleTeacher = Integer.parseInt(RoleHelper.TEACHER_ROLE_ID);
                //int roleAthlete = Integer.parseInt(RoleHelper.ATHLETE_ROLE_ID);


                if (role == roleManager || role == roleWelfare || role == roleClub || role == roleTeacher) {
                    intent = new Intent(context, ActivitySurvey.class);
                } else {

                    intent = new Intent(context, ActivityGetAllSharedSurvey.class);

//                    final ActivityMain activityMain = (ActivityMain) context;
//                    if (activityMain != null) {
//                        activityMain.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                DialogHelper.showSimpleCustomDialog(activityMain, " There is no any surveys");
//                            }
//                        });
//                    }
//                    else
//                        Toast.makeText(activityMain, "null", Toast.LENGTH_SHORT).show();
                }
                //intent = new Intent(context, ActivitySurvey.class);
                break;

            case R.id.menu_setting:
                intent = new Intent(context, ActivitySettings.class);
                break;

            /*case R.id.menu_add_contacts:
                intent = new Intent(context, ActivityContactAdd.class);  // to add people to contact
                break;*/

            case R.id.menu_contacts:
                intent = new Intent(context, ActivityContact.class);
                break;

            case R.id.menu_notes:
                intent = new Intent(context, ActivityNoteContainer.class);
                break;

            case R.id.menu_feedback:
                intent = new Intent(context, ActivityFeedback.class);
                break;
        }

    }

    public Intent getIntentToStartActivity() {
        return intent;
    }


    public void setFragmentToShow(int menuItemId, boolean addToBackStack) {
        switch (menuItemId) {
           /* case R.id.menu_dash:
                this.fragmentToShow = new FragmentHome();
                this.addToBackStack = addToBackStack;
                this.tag = ConstHelper.Tag.Fragment.HOME;
                break;

            case R.id.menu_activity:
                this.fragmentToShow = new FragmentActivityContainer();
                this.addToBackStack = addToBackStack;
                this.tag = ConstHelper.Tag.Fragment.ACTIVITY;
                break;

            case R.id.menu_events:
                this.fragmentToShow = new FragmentEvents();
                this.addToBackStack = addToBackStack;
                this.tag = ConstHelper.Tag.Fragment.EVENTS;
                break;

            case R.id.menu_profile:
                this.fragmentToShow = new FragmentProfile();
                this.addToBackStack = addToBackStack;
                this.tag = ConstHelper.Tag.Fragment.PROFILE;
                break;*/

        }

    }

    public String getTag() {
        return tag;
    }

    public Fragment getFragmentToShow() {
        return this.fragmentToShow;
    }

    public boolean getAddToBackStack() {
        return this.addToBackStack;
    }


}
