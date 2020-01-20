package org.ctdworld.propath.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityProfileRepAchievementAddEdit;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.adapter.AdapterRepAchievement;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractRepAchievement;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.RepAchievement;
import org.ctdworld.propath.presenter.PresenterRepAchievement;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfileViewRepAchievement extends BaseFragment implements ContractRepAchievement.View, AdapterRepAchievement.OnOptionSelected
{
    private final String TAG = FragmentProfileViewRepAchievement.class.getSimpleName();

    private static final int REQUEST_CODE_EDIT_REP_ACHIEVEMENT = 100;
    private ActivityProfileView mActivityProfileView;
    private Context mContext;
    private RecyclerView.Adapter mAdapterRepAchievement; // mAdapterRepAchievementImages;
    private RecyclerView mRecyclerViewRepAchievements ; // mRecyclerViewRepAchievementsImages ;
    private FloatingActionButton mFloatAddRepAchievement;

    private ContractRepAchievement.Presenter mPresenter;


    private static final String KEY_USER_ID = "user id";  // key to send user id from calling activity
    private String USER_ID = ""; //  string to contain user id, to get profile of this userId

    // returns instance and sets profile type to argument
    public static FragmentProfileViewRepAchievement getInstance(/*String profileType,*/ String userId)
    {
        FragmentProfileViewRepAchievement fragmentProfileViewRepAchievement = new FragmentProfileViewRepAchievement();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_USER_ID,userId); // putting userId to get rep achievement
        fragmentProfileViewRepAchievement.setArguments(bundle);

        return fragmentProfileViewRepAchievement;
    }


    // initializing PROFILE_TYPE from arguments
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            USER_ID = bundle.getString(KEY_USER_ID);
    }



    // constructor
    public FragmentProfileViewRepAchievement() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_view_rep_achievement, container, false);

        init(view);
        setRepAchievementAdapter();
        requestRepList();

        //hiding option menu icon, we are showing options menu icon for each individual achievement in adapter
        mActivityProfileView.disableOptionsMenu();
        // changing toobar title
        mActivityProfileView.showToolbarTitle("Representative Achievement");

        mFloatAddRepAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepAchievement repAchievement = new RepAchievement();
                Intent intentAchievement = new Intent(new Intent(mContext, ActivityProfileRepAchievementAddEdit.class));
                intentAchievement.putExtra(ActivityProfileRepAchievementAddEdit.REP_Type,"add");
                intentAchievement.putExtra(ActivityProfileRepAchievementAddEdit.REP_ID,"");
                intentAchievement.putExtra(ActivityProfileRepAchievementAddEdit.REP_DATA,repAchievement);

                startActivity(intentAchievement);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //requestRepList();
    }

    private void init(View view)
    {
        mContext = getContext();
        mPresenter = new PresenterRepAchievement(mContext,this);
        mRecyclerViewRepAchievements = view.findViewById(R.id.profile_view_recycler_rep_achievement);
        mFloatAddRepAchievement = view.findViewById(R.id.add_rep_achievement);
     //   mRecyclerViewRepAchievementsImages = view.findViewById(R.id.profile_view_recycler_rep_achievement_images);

    }


    private void requestRepList()
    {
        showLoader(getString(R.string.message_loading));
        mPresenter.requestRepAchievement(USER_ID);
    }


    // adding adapter for representative achievement
    private void setRepAchievementAdapter()
    {
        try {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            mAdapterRepAchievement = new AdapterRepAchievement(mContext, USER_ID, getChildFragmentManager(), this);
            mRecyclerViewRepAchievements.setLayoutManager(layoutManager);
            mRecyclerViewRepAchievements.setAdapter(mAdapterRepAchievement);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CODE_EDIT_REP_ACHIEVEMENT && data.getExtras() != null)
            {
                boolean isEdited = data.getExtras().getBoolean(ActivityProfileRepAchievementAddEdit.KEY_RESPONSE_IS_ADDED_OR_EDITED);
                if (isEdited)
                    requestRepList();
                else
                    Log.e(TAG,"rep achievement is not edited");
            }
        }
    }




    // # this method is called when an option is selected in options menu in adapter
    @Override
    public void onRepOptionSelected(final int positionInAdapter, final RepAchievement repAchievement) {

            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_EDIT, "Edit Achievement")
                    .addOption(BottomSheetOption.OPTION_DELETE, "Delete Achievement");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
            {
                @Override
                public void onOptionSelected(int option)
                {
                    switch (option)
                    {
                        case BottomSheetOption.OPTION_EDIT:

                            if (repAchievement.getRepID() != null)
                            {
                                Intent intent = new Intent(new Intent(mContext, ActivityProfileRepAchievementAddEdit.class));
                                intent.putExtra(ActivityProfileRepAchievementAddEdit.REP_Type,ActivityProfileRepAchievementAddEdit.TYPE_EDIT);
                                intent.putExtra(ActivityProfileRepAchievementAddEdit.REP_DATA, repAchievement);
                                intent.putExtra(ActivityProfileRepAchievementAddEdit.REP_ID, repAchievement.getRepID());
                                startActivityForResult(intent, REQUEST_CODE_EDIT_REP_ACHIEVEMENT);
                            }

                            break;


                        case BottomSheetOption.OPTION_DELETE:

                            String title = getString(R.string.are_you_sure);
                            String message = "Your achievement will be deleted";
                            DialogHelper.showCustomDialog(mContext, title, message, new DialogHelper.ShowDialogListener() {
                                @Override
                                public void onOkClicked() {
                                    if (repAchievement.getRepID() != null)
                                    {
                                        showLoader(getString(R.string.deleting));
                                        mPresenter.deleteRepAchievement(positionInAdapter, repAchievement.getRepID());
                                    }
                                }

                                @Override
                                public void onCancelClicked() {

                                }
                            });

                            break;
                    }

                }
            });
            options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityProfileView = (ActivityProfileView) context;
    }



    @Override
    public void onReceivedRepList(List<RepAchievement> listRepAchievement)
    {
        hideLoader();
        // notifying adapter for representative achievement images
        List<RepAchievement> listFilteredRepAchievement = new ArrayList<>();
        for (int i=0; i<listRepAchievement.size(); i++) // looping through all rep achievement
        {
            RepAchievement repAchievement = listRepAchievement.get(i);
            //  checking if team and location is empty or not, if these are empty then that rep achievement is not being shown.
            if (!repAchievement.getRepTeam().isEmpty() && !repAchievement.getRepLocation().isEmpty() )
            {
                Log.i(TAG,"rep achievement details are not added at position = "+i);
                listFilteredRepAchievement.add(repAchievement);
                Log.i(TAG,"rep achievement details are not added at position so removing this item = "+i);
            }
        }
        AdapterRepAchievement adapterRepAchievement = (AdapterRepAchievement) mAdapterRepAchievement;
        adapterRepAchievement.addListRepAchievement(listFilteredRepAchievement);
        mAdapterRepAchievement.notifyDataSetChanged();


    }

    @Override
    public void onDeleted(int position) {
        hideLoader();
        AdapterRepAchievement adapterRepAchievement = (AdapterRepAchievement) mAdapterRepAchievement;
                adapterRepAchievement.onItemDeleted(position);
    }

    @Override
    public void onAddRepSuccessfully() {
        hideLoader();
    }

    @Override
    public void onEditSuccessfully()
    {
        hideLoader();
    }

    @Override
    public void onShowProgress()
    {
        //mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        hideLoader();
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible()
    {
        hideLoader();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        hideLoader();
    }

    @Override
    public void onShowMessage(String message) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }



}
