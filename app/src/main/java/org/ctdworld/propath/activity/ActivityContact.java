package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterViewPagerContact;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentContact;
import org.ctdworld.propath.fragment.FragmentGroupChatInjuryManagement;
import org.ctdworld.propath.fragment.FragmentSearch;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.model.BottomSheetOption;

public class ActivityContact extends BaseActivity implements FragmentSearch.SearchListener
{
    private final String TAG = ActivityContact.class.getSimpleName();
    public static final int REQUEST_CODE_ACTIVITY_CHAT = 100 ; //# to get data back from ActivityChat

    // positions used in view pager adapter
    public static final int FRAGMENT_CONTACT = 0;
    public static final int FRAGMENT_GROUP = 1;


    Context mContext;
    Toolbar mToolbar;
    ImageView mImgToolbarOptionsMenu;
    TextView mToolbarTitle;
    PermissionHelper mPermissionHelper;
    ViewPager mViewPager;
    AdapterViewPagerContact mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        init();
        setToolbar();
        setUpViewPager();
        setUpOptionsMenu();

    }



    // # initializing variables
    private void init() {
        mContext = this;
        mViewPager = findViewById(R.id.activity_contact_view_pager);
        mAdapter = new AdapterViewPagerContact(getSupportFragmentManager());
        mPermissionHelper = new PermissionHelper(mContext);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgToolbarOptionsMenu = mToolbar.findViewById(R.id.toolbar_img_options_menu);
    }


    // setting up options menu
    private void setUpOptionsMenu() {
        // setting listener on mImgToolbarOptionsMenu to show option menu dialog
        mImgToolbarOptionsMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_CREATE_GROUP, "Create Group")
                        .addOption(BottomSheetOption.OPTION_ADD_CONTACT, "Add Contact");


                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
                {
                    @Override
                    public void onOptionSelected(int option)
                    {
                        switch (option)
                        {
                            case BottomSheetOption.OPTION_CREATE_GROUP :

                                if (getGroupFragmentInstance() != null)
                                    getGroupFragmentInstance().createGroup(BottomSheetOption.OPTION_CREATE_GROUP);
                                    break;

                            case BottomSheetOption.OPTION_ADD_CONTACT :
                                 startActivity(new Intent(mContext, ActivityContactAdd.class));  // to create new group
                                 break;
                        }
                    }
                });
                options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

            }
        });
    }



    // setting up ViewPager and TabLayout
    private void setUpViewPager()
    {
        TabLayout tabLayout = findViewById(R.id.activity_contact_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);
    }


    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(getString(R.string.contacts));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    // returns instance of FragmentGroupChatInjuryManagement
    private FragmentGroupChatInjuryManagement getGroupFragmentInstance()
    {
        Fragment fragment = mAdapter.getRegisteredFragment(FRAGMENT_GROUP);
        if (fragment instanceof FragmentGroupChatInjuryManagement)
            return (FragmentGroupChatInjuryManagement) fragment;
        else
            return null;
    }


    // returns instance of FragmentContact
    private FragmentContact getContactFragmentInstance()
    {
        Fragment fragment = mAdapter.getRegisteredFragment(FRAGMENT_GROUP);
        if (fragment instanceof FragmentContact)
            return (FragmentContact) fragment;
        else
            return null;
    }


    @Override
    public void onSearchToFilter(String searchedText)
    {
        if (mViewPager.getCurrentItem() == FRAGMENT_CONTACT)
        {
            FragmentContact fragmentContact = (FragmentContact) mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
            if (fragmentContact != null)
                fragmentContact.filterList(searchedText);
        }
        else if (mViewPager.getCurrentItem() == FRAGMENT_GROUP)
        {
            FragmentGroupChatInjuryManagement fragment = (FragmentGroupChatInjuryManagement) mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
            if (fragment != null)
                fragment.filterList(searchedText);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null)
        {
            // checking if anything has been changed in any group or not if there is any change then updating group list in FragmentGroupChatInjuryManagement
            if (REQUEST_CODE_ACTIVITY_CHAT == requestCode && data.getExtras() != null) {

                boolean isAnyGroupChanged = data.getExtras().getBoolean(ActivityChat.KEY_IS_SOMETHING_CHANGED);
                if (isAnyGroupChanged) {
                    if (getGroupFragmentInstance() != null)
                        getGroupFragmentInstance().updateGroupList();
                }
            }
        }
        else
            Log.e(TAG, "cancelled");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

}
