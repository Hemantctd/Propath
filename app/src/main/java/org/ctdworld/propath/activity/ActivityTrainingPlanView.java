package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterTrainingPlanItemsView;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractTrainingPlan;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.ContactAndGroup;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterTrainingPlan;

import java.util.ArrayList;



/* # this activity contains list of training plan items.*/
public class ActivityTrainingPlanView extends BaseActivity implements ContractTrainingPlan.View, AdapterTrainingPlanItemsView.OnItemClickedListener
{
    // constants
    private static final String TAG = ActivityTrainingPlanView.class.getSimpleName();
    private static final int REQUEST_CODE_VIEW_ITEMS_IN_SLIDER = 200;


    // # views
    RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private TextView mToolbarTxtTitle, mTxtTitle, mTxtDescription;
    private ImageView mImgToolbarOptionsMenu;


    // # other variables
    private TrainingPlan.PlanData mTrainingPlanData;
    private Context mContext;
    private AdapterTrainingPlanItemsView mAdapter;
    private ContractTrainingPlan.Presenter mPresenterTrainingPlan;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan_view);

        mContext = this;
        mPresenterTrainingPlan = new PresenterTrainingPlan(mContext,this);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mTxtTitle = findViewById(R.id.activity_training_plan_txt_title);
        mTxtDescription = findViewById(R.id.activity_training_plan_txt_description);
        mImgToolbarOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        mRecyclerView = findViewById(R.id.activity_training_plan_created_recycler);

        if (getIntent() != null && getIntent().getExtras() != null)
            mTrainingPlanData = (TrainingPlan.PlanData) getIntent().getExtras().getSerializable(ConstHelper.Key.TRAINING_PLAN);
        

        setToolbar();
        attachAdapter();
        setViews(mTrainingPlanData);  // setting data


        showCreatedDataDetailsFragment();  // showing creator details and created or updated details

        mImgToolbarOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_DELETE, "Delete");
                if (mTrainingPlanData != null && mTrainingPlanData.getCreatedByUserId() != null && SessionHelper.getUserId(mContext).equals(mTrainingPlanData.getCreatedByUserId()))
                {
                    builder.addOption(BottomSheetOption.OPTION_SHARE,"Share");
                    builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit");
                }

                ArrayList<BottomSheetOption> bottomSheetOptions = builder.build();

                Log.i(TAG,"bottom sheet options size = "+bottomSheetOptions.size());

                // List<BottomSheetOption> bottomSheetOptions = builder.build();
                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
                {
                    @Override
                    public void onOptionSelected(int option)
                    {
                        Log.i(TAG,"bottom sheet options selected, option = "+option);
                        switch (option)
                        {
                            case BottomSheetOption.OPTION_SHARE:
                                startActivityForResult(new Intent(mContext, ActivityContactsAndGroups.class) , ConstHelper.RequestCode.SHARE);

                                break;

                            case BottomSheetOption.OPTION_EDIT:
                                Intent intent = new Intent(mContext, ActivityTrainingPlanEdit.class);
                                intent.putExtra(ConstHelper.Key.TRAINING_PLAN, mTrainingPlanData);
                                startActivityForResult(intent, ConstHelper.RequestCode.EDIT);
                                break;

                            case BottomSheetOption.OPTION_DELETE:
                                String title = getString(R.string.are_you_sure);
                                String message = "Your plan will be deleted.";
                                DialogHelper.showCustomDialog(mContext, title, message, "Delete", "close", new DialogHelper.ShowDialogListener() {
                                    @Override
                                    public void onOkClicked() {
                                        if (mTrainingPlanData != null)
                                        {
                                            showLoader("Deleting...");
                                            ArrayList<TrainingPlan.PlanData> arrayList = new ArrayList<>();
                                            arrayList.add(mTrainingPlanData);
                                            mPresenterTrainingPlan.deleteTrainingPlan(arrayList);
                                        }
                                        else
                                            Log.e(TAG,"mTrainingPlanData is null while deleting");
                                    }

                                    @Override
                                    public void onCancelClicked() {

                                    }
                                });

                                break;
                        }
                    }
                });
                options.show(getSupportFragmentManager(), "options");
            }
        });


    }

    // showing creator details and created or updated details
    private void showCreatedDataDetailsFragment() {
        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id._fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }



    // setting toolbar data
    private void setToolbar() {

        setSupportActionBar(mToolbar);
        mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        mToolbarTxtTitle.setText(getString(R.string.training_plan));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            return;
        
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }




    private void attachAdapter()
    {
        mAdapter = new AdapterTrainingPlanItemsView(mContext, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            super.onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.IS_DELETED, false);
        intent.putExtra(ConstHelper.Key.TRAINING_PLAN, mTrainingPlanData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
        {
            // checking if training plan was edited
            if (requestCode == ConstHelper.RequestCode.EDIT)
            {
                TrainingPlan.PlanData trainingPlanData = (TrainingPlan.PlanData) data.getSerializableExtra(ConstHelper.Key.TRAINING_PLAN);
                if (trainingPlanData != null)
                {
                    mTrainingPlanData = trainingPlanData;  // initializing new training plan
                    setViews(trainingPlanData);
                }
            }

            // # sharing training plan
            else if (requestCode == ConstHelper.RequestCode.SHARE && data.getExtras() != null)
            {
                String[] userArr = new String[]{};
                String[] groupArr = new String[]{};

                ContactAndGroup contactAndGroup = (ContactAndGroup) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                if (contactAndGroup != null) {
                    Log.i(TAG, " selected user = " + contactAndGroup.getName());

                    if (ContactAndGroup.TYPE_FRIEND.equalsIgnoreCase(contactAndGroup.getType()))
                        userArr = new String[]{contactAndGroup.getId()};
                    else if (ContactAndGroup.TYPE_GROUP.equalsIgnoreCase(contactAndGroup.getType()))
                        groupArr = new String[]{contactAndGroup.getId()};

                    if (!(userArr.length<1 && groupArr.length<1))
                    {
                        showLoader(getString(R.string.message_sharing));
                        if (mTrainingPlanData != null)
                            mPresenterTrainingPlan.shareTrainingPlan(mTrainingPlanData, groupArr, userArr);
                        else
                            Log.e(TAG,"mTrainingPlanData is null in onActivityResult() method while sharing training plan");
                    }

                } else
                    Log.e(TAG, "contactAndGroup is null");
            }

            // checking if item was deleted or edited in item view page where we slide items
            if (requestCode == REQUEST_CODE_VIEW_ITEMS_IN_SLIDER)
            {
                Log.i(TAG,"Came back from plan item view page (slider)");

               TrainingPlan.PlanData trainingPlanData = (TrainingPlan.PlanData) data.getSerializableExtra(ConstHelper.Key.TRAINING_PLAN_SINGLE_PLAN);
               setViews(trainingPlanData);
            }
        }
    }



    // setting data
    private void setViews(PlanData trainingPlanData)
    {
        Log.i(TAG,"setting result , planId = "+ trainingPlanData.getId());
        mTxtTitle.setText(trainingPlanData.getTitle());
        mTxtDescription.setText(trainingPlanData.getDescription());

        if (mAdapter != null && trainingPlanData.getPlanItemList() != null)
        {
            Log.i(TAG,"item size = "+ trainingPlanData.getPlanItemList().size());
            mAdapter.addTrainingPlan(trainingPlanData);
        }
        else
            Log.e(TAG,"mAdapter or itemList is null in setData() method");
    }



    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails()
    {
        if (mTrainingPlanData == null)
            return null;
        
        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        
        createdDataDetails.setName(mTrainingPlanData.getCreatedByName());
        createdDataDetails.setRoleId(mTrainingPlanData.getCreatedByRoleId());
        createdDataDetails.setUserPicUrl(mTrainingPlanData.getCreatedByProfilePic());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(mTrainingPlanData.getCreatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(DateTimeHelper.getDateTime(mTrainingPlanData.getCreatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME));

        return createdDataDetails;
    }



    @Override
    public void onTrainingPlanCreated(TrainingPlan.PlanData trainingPlanData)
    {
        hideLoader();
    }

    @Override
    public void onTrainingPlanReceived(TrainingPlan trainingPlan)
    {
      hideLoader();
    }

    @Override
    public void onTrainingPlanDeleted(TrainingPlan.PlanData trainingPlanData)
    {
        hideLoader();
        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.TRAINING_PLAN, mTrainingPlanData);
        intent.putExtra(ConstHelper.Key.IS_DELETED, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTrainingPlanEdited(TrainingPlan.PlanData trainingPlanData) {
        hideLoader();
    }

    @Override
    public void onTrainingPlanShared() {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.message_success), "Training plans shared successfully.");
    }


    @Override
    public void onFailed(String message) {
        hideLoader();
        if (message != null && !message.isEmpty())
            DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.message_failed),message);
        else
            DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.message_failed));

    }

    @Override
    public void onShowMessage(String message) {
        hideLoader();
    }


    @Override
    public void onItemClickedListener(TrainingPlan.PlanData planData, int adapterPosition)
    {
        if (planData == null)
            return;

        Intent intent = new Intent(mContext, ActivityTrainingPlanItemViewSlider.class);
        intent.putExtra(ActivityTrainingPlanItemViewSlider.EXTRA_SELECTED_ITEM_POSITION, adapterPosition);
        intent.putExtra(ActivityTrainingPlanItemViewSlider.EXTRA_TRAINING_PLAN, planData);
        intent.putExtra(ActivityTrainingPlanItemViewSlider.EXTRA_TRAINING_PLAN_ID, mTrainingPlanData.getId());
        startActivityForResult(intent, REQUEST_CODE_VIEW_ITEMS_IN_SLIDER);
    }
}
