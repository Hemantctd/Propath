package org.ctdworld.propath.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityContactsAndGroups;
import org.ctdworld.propath.activity.ActivityTrainingPlanCreate;
import org.ctdworld.propath.activity.ActivityTrainingPlanEdit;
import org.ctdworld.propath.activity.ActivityTrainingPlanView;
import org.ctdworld.propath.adapter.AdapterTrainingPlan;
import org.ctdworld.propath.contract.ContractTrainingPlan;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterTrainingPlan;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentTrainingPlanList extends Fragment implements ContractTrainingPlan.View
{
    // # constants
    private static final String TAG = FragmentTrainingPlanList.class.getSimpleName();


    // # views
    private FloatingActionButton mFAB;
    private RecyclerView mRecyclerView;
    private DialogLoader mLoader;
    private TextView mTxtErrorMessage; // this is displayed in center if screen
    private Button mBtnDelete;  // to delete items
    private SwipeRefreshLayout mRefreshLayout;


    // # other variables
    private int mTypeReceivedOrCreated = 0;  // # type to check which type of data is to be shown (created or shared)
    private Context mContext;
    private AdapterTrainingPlan mAdapter;
    private ContractTrainingPlan.Presenter mPresenterTrainingPlan;



    public static FragmentTrainingPlanList getInstance(int type)
    {
        FragmentTrainingPlanList fragmentTrainingPlanList = new FragmentTrainingPlanList();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstHelper.Key.TYPE_RECEIVED_OR_CREATED, type);
        fragmentTrainingPlanList.setArguments(bundle);

        return fragmentTrainingPlanList;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mTypeReceivedOrCreated = bundle.getInt(ConstHelper.Key.TYPE_RECEIVED_OR_CREATED);
        }
    }


    public FragmentTrainingPlanList() {} //# default empty constructor



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_training_plan_list, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // initializing...
        mContext = getContext();
        mPresenterTrainingPlan = new PresenterTrainingPlan(mContext, this);
        mRefreshLayout = view.findViewById(R.id.training_plan_received_refresh_layout);
        mRecyclerView = view.findViewById(R.id.training_plan_received_recycler_view);
        mTxtErrorMessage = view.findViewById(R.id.training_plan_received_txt_error_message);
        mFAB = view.findViewById(R.id.fragment_training_plan_float_button);
        mBtnDelete = view.findViewById(R.id.fragment_training_plan_btn_delete);


        if (ConstHelper.Type.RECEIVED == mTypeReceivedOrCreated)
            mFAB.setVisibility(View.GONE);


        attachAdapter();  // setting adapter to show training plan
        requestAllTrainingPlan(); // to get all training plan list


        // # listeners
        mFAB.setOnClickListener(onFloatButtonClicked); // creating training plan
        mRefreshLayout.setOnRefreshListener(onRefreshLayoutPulled);

    }



    // to get all categories from server
    private void requestAllTrainingPlan()
    {
        Log.i(TAG,"requesting all training plan");
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            mRefreshLayout.setRefreshing(true);
            mTxtErrorMessage.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            try
            {
                mAdapter.clearTrainingPlanList();
                mPresenterTrainingPlan.getTrainingPlanList(new PlanData());
            }
            catch (Exception e)
            {
                hideLoader();
                Log.e(TAG,"Error while requesting for training plan list , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            //  DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.no_connection),getString(R.string.connection_message));
            hideLoader();
            mRecyclerView.setVisibility(View.GONE);
            mTxtErrorMessage.setVisibility(View.VISIBLE);
            mTxtErrorMessage.setText(getString(R.string.title_no_connection));
        }
    }



    // this method attaches adapter on recycler view to show all notes
    private void attachAdapter()
    {
        mAdapter = new AdapterTrainingPlan(mContext , onAdapterTrainingPlanListener);

        for (int i=0; i<20; i++)
        {
            TrainingPlan.PlanData trainingPlanData = new TrainingPlan.PlanData();
            if (mTypeReceivedOrCreated == ConstHelper.Type.CREATED)
                trainingPlanData.setTitle("Training PlanData created "+(i+1));
            else if (mTypeReceivedOrCreated == ConstHelper.Type.RECEIVED)
                trainingPlanData.setTitle("Training PlanData received "+(i+1));

            mAdapter.onTrainingPlanReceived(trainingPlanData);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    // this method is called when delete option is selected in options menu in container activity
    public void onDeleteOptionClicked()
    {
        mFAB.setVisibility(View.GONE);
        mBtnDelete.setVisibility(View.VISIBLE);
        mAdapter.enableDeleteMode();
    }



    // deleting training plan
    private void deletePlan(final PlanData trainingPlanData)
    {
        String title = getString(R.string.are_you_sure);
        String message = "Your plan will be deleted.";
        DialogHelper.showCustomDialog(mContext, title, message, "Delete", "close", new DialogHelper.ShowDialogListener() {
            @Override
            public void onOkClicked() {
                if (trainingPlanData != null)
                {
                    showLoader("Deleting...");
                    ArrayList<TrainingPlan.PlanData> arrayList = new ArrayList<>();
                    arrayList.add(trainingPlanData);
                    mPresenterTrainingPlan.deleteTrainingPlan(arrayList);
                }
                else
                    Log.e(TAG,"mTrainingPlan is null while deleting");
            }

            @Override
            public void onCancelClicked() {

            }
        });

    }



    private void editPlan(PlanData planData)
    {
        Intent intent = new Intent(mContext, ActivityTrainingPlanEdit.class);
        intent.putExtra(ConstHelper.Key.TRAINING_PLAN, planData);
        startActivityForResult(intent, ConstHelper.RequestCode.EDIT);
    }


    // receiving selected notes category
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(TAG,"onActivityResult() method called, requestCode = "+requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode && data != null)
        {
           /* // # fetching selected category
            if (requestCode == ConstHelper.RequestCode.SHARE && data.getExtras() != null)
            {
                String[] userArr = new String[]{};
                String[] groupArr = new String[]{};

                ContactAndGroup contactAndGroup = (ContactAndGroup) data.getExtras().getSerializable(ActivityContactsAndGroups.KEY_RESPONSE_CONTACTS_AND_GROUPS);
                if (contactAndGroup != null) {
                    Log.i(TAG, " selected user = " + contactAndGroup.getName());

                    if (ContactAndGroup.TYPE_FRIEND.equalsIgnoreCase(contactAndGroup.getType()))
                        userArr = new String[]{contactAndGroup.getId()};
                    else if (ContactAndGroup.TYPE_GROUP.equalsIgnoreCase(contactAndGroup.getType()))
                        groupArr = new String[]{contactAndGroup.getId()};

                    if (!(userArr.length<1 && groupArr.length<1))
                    {
                         *//* mLoader = DialogLoader.getInstance("Sharing...");
                    mLoader.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);*//*
                        DialogHelper.showLoader(getString(R.string.message_sharing), getChildFragmentManager());
                        mPresenterTrainingPlan.shareTrainingPlan(mSelectedTrainingPlan, groupArr, userArr);
                    }

                } else
                    Log.e(TAG, "contactAndGroup is null");
            }
            else*/ if (requestCode == ConstHelper.RequestCode.CREATE)
            {
                Log.i(TAG,"showing created training plan");
               /* if (mAdapter != null)
                {
                    TrainingPlan.PlanData trainingPlan = (PlanData) data.getSerializableExtra(ConstHelper.Key.TRAINING_PLAN);
                    if (trainingPlan != null && mAdapter != null && mTypeReceivedOrCreated == ConstHelper.Type.CREATED)
                    {
                        Log.i(TAG,"showing created training plan, item size = "+trainingPlan.getPlanItemList().size());
                        mAdapter.onTrainingPlanCreated(trainingPlan);
                        if (mRecyclerView != null)
                            mRecyclerView.scrollToPosition(0);
                    }
                    else
                        Log.e(TAG,"training_plan is null in onActivityResult() method");
                }
                else
                    Log.e(TAG,"mAdapter is null in onActivityResult() method");*/

               requestAllTrainingPlan();

            }

            // updating training plan after coming from view page, may be trained plan was edited or deleted
            else if (requestCode == ConstHelper.RequestCode.VIEW)
            {
                boolean isPlanDeleted = data.getBooleanExtra(ConstHelper.Key.IS_DELETED,false);
                TrainingPlan.PlanData trainingPlanData = (TrainingPlan.PlanData) data.getSerializableExtra(ConstHelper.Key.TRAINING_PLAN);

                if (isPlanDeleted && mAdapter != null)
                    mAdapter.onTrainingPlanDeleted(trainingPlanData);  // # removing plan
                else  // updating adapter..........may be plan was updated
                {
                    if (trainingPlanData != null && mAdapter != null)
                        mAdapter.onTrainingPlanEdited(trainingPlanData);
                    else
                        Log.e(TAG,"trainingPlanData or mAdapter is null, trainingPlanData = "+ trainingPlanData);
                }

            }
            else
                Log.e(TAG,"onActivityResult() method no requestCode matches");
        }
        else
            Log.e(TAG,"onActivityResult() method resultCode is not ok or data is null");
    }



 // # on float button clicked
  private View.OnClickListener onFloatButtonClicked = new View.OnClickListener() {
      @Override
      public void onClick(View view) {

          startActivityForResult(new Intent(mContext, ActivityTrainingPlanCreate.class), ConstHelper.RequestCode.CREATE);
      }
  };


  private SwipeRefreshLayout.OnRefreshListener onRefreshLayoutPulled = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
        requestAllTrainingPlan();
    }};




  private AdapterTrainingPlan.OnAdapterTrainingPlanListener onAdapterTrainingPlanListener = new AdapterTrainingPlan.OnAdapterTrainingPlanListener()
  {
      @Override
      public void onTrainingPlanClicked(PlanData trainingPlanData)
      {
          Intent intent = new Intent(mContext, ActivityTrainingPlanView.class);
          intent.putExtra(ConstHelper.Key.TRAINING_PLAN, trainingPlanData);
          intent.putExtra(ConstHelper.Key.TYPE_RECEIVED_OR_CREATED, mTypeReceivedOrCreated);
          startActivityForResult(intent, ConstHelper.RequestCode.VIEW);
      }

      @Override
      public void onTrainingPlanOptionsMenuClicked(final PlanData trainingPlanData) {
          BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                  .addOption(BottomSheetOption.OPTION_DELETE, "Delete");
          if (trainingPlanData != null && trainingPlanData.getCreatedByUserId() != null && SessionHelper.getUserId(mContext).equals(trainingPlanData.getCreatedByUserId()))
          {
              builder.addOption(BottomSheetOption.OPTION_SHARE,"Share");
              builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit");
              builder.addOption(BottomSheetOption.OPTION_COPY,"Copy");
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
                          editPlan(trainingPlanData);
                          break;

                      case BottomSheetOption.OPTION_COPY:
                     //     editPlan(trainingPlanData);
                          break;


                      case BottomSheetOption.OPTION_DELETE:
                          deletePlan(trainingPlanData);
                          break;
                  }
              }
          });
          options.show(getChildFragmentManager(), "options");
      }


  };


    public void showLoader(String title)
    {
        if (title == null || title.isEmpty())
            title = "Wait..";

        if (mLoader == null)
        {
            mLoader = DialogLoader.getInstance(title);
            mLoader.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        }
        else if (mLoader.isAdded() && mLoader.isVisible())
        {
            mLoader = (DialogLoader) getChildFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            if (mLoader != null)
                mLoader.changeProgressTitle(title);
        }
        else
        {
            mLoader = null;
            mLoader = DialogLoader.getInstance(title);
            mLoader.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        }

    }




    // hiding custom loader and swipeRefreshLayout
    private void hideLoader()
    {
        //DialogHelper.hideLoader(); // hiding d
        if (mLoader != null && mLoader.isAdded())
            mLoader.dismiss();

        if (mRefreshLayout != null && mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }


    // to show loader



    @Override
    public void onTrainingPlanCreated(PlanData trainingPlanData)
    {
        hideLoader();
        /*if (mAdapter != null)
            mAdapter.onTrainingPlanCreated(trainingPlanData);
        if (mRecyclerView != null)
            mRecyclerView.scrollToPosition(0);*/
        if (mAdapter != null)
            mAdapter.onTrainingPlanCreated(trainingPlanData);

    }

    @Override
    public void onTrainingPlanReceived(TrainingPlan trainingPlan)
    {
       // Log.i(TAG,"onTrainingPlanReceived() method called, trainingPlan.isSelfCreated() = "+trainingPlan.isSelfCreated()
       // + " , mTypeReceivedOrCreated = "+mTypeReceivedOrCreated);
        //hideLoader();
        hideLoader();
        String loginUserId = SessionHelper.getInstance(mContext).getUser().getUserId();
        List<TrainingPlan.PlanData> planDataList = trainingPlan.getPlanDataList();

        if (planDataList != null && loginUserId != null)
        {
            for (int i = 0; i< planDataList.size(); i++)
            {
                TrainingPlan.PlanData planData = planDataList.get(i);
                if (mAdapter != null  && loginUserId.equals(planData.getCreatedByUserId()) && mTypeReceivedOrCreated == ConstHelper.Type.CREATED)
                {
                    Log.i(TAG,"showing created training planData");
                    planData.setSelfCreated(true);
                    mAdapter.onTrainingPlanReceived(planData);
                }
                else if (mAdapter != null && !loginUserId.equals(planData.getCreatedByUserId()) && mTypeReceivedOrCreated == ConstHelper.Type.RECEIVED)
                {
                    Log.i(TAG,"showing received training planData");
                    planData.setSelfCreated(false);
                    mAdapter.onTrainingPlanReceived(planData);
                }
                else
                    Log.i(TAG,"not showing any training planData");
            }
        }



    }

    @Override
    public void onTrainingPlanDeleted(TrainingPlan.PlanData trainingPlanData)
    {
        hideLoader();
        if (mAdapter != null)
            mAdapter.onTrainingPlanDeleted(trainingPlanData);
    }

    @Override
    public void onTrainingPlanEdited(PlanData trainingPlanData) {
        //hideLoader();
        hideLoader();
        if (mAdapter != null)
            mAdapter.onTrainingPlanEdited(trainingPlanData);
    }

    @Override
    public void onTrainingPlanShared() {
       hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.message_success), "Training plans shared successfully.");
    }

/*    @Override
    public void onTrainingPlanItemCreated(TrainingPlan.PlanData trainingPlan)
    {
        hideLoader();
    }

    @Override
    public void onTrainingPlanItemReceived(TrainingPlan.PlanData trainingPlan) {
        hideLoader();
    }

    @Override
    public void onTrainingPlanItemEdited(TrainingPlan.PlanData trainingPlan) {
        hideLoader();
    }

    @Override
    public void onTrainingPlanItemDeleted(TrainingPlan.PlanData trainingPlan) {
        hideLoader();
    }*/

    @Override
    public void onFailed(String message) {
        hideLoader();
        if (message != null && !message.isEmpty())
            mTxtErrorMessage.setText(MessageFormat.format("{0}\n{1}", R.string.message_failed, message));

            // DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.message_failed),message);
        else
            mTxtErrorMessage.setText(message);

        // DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.message_failed));

    }

    @Override
    public void onShowMessage(String message) {
        hideLoader();
        if (message != null && !message.isEmpty() && mTxtErrorMessage != null)
            mTxtErrorMessage.setText(message);
            //DialogHelper.showSimpleCustomDialog(mContext,message);
    }


}
