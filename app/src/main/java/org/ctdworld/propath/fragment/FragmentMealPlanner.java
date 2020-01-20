package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityContactsAndGroups;
import org.ctdworld.propath.activity.ActivityMealPlannerCreate;
import org.ctdworld.propath.activity.ActivityTrainingPlanEdit;
import org.ctdworld.propath.adapter.AdapterMealPlanner;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.MealPlan;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMealPlanner extends Fragment {

    private static final String TAG = FragmentMealPlanner.class.getSimpleName();


    private Context mContext;
    FloatingActionButton mFAB;
    private RecyclerView mRecyclerView;
    AdapterMealPlanner mAdapter;


    // # loader
     private DialogLoader mLoader;

    //private TextView mTxtErrorMessage; // this is displayed in center if screen
    SwipeRefreshLayout mRefreshLayout;
    //  private Button mBtnDelete;  // to delete items


    public FragmentMealPlanner() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // initializing...
        mContext = getContext();
        mRefreshLayout = view.findViewById(R.id.meal_planner_received_refresh_layout);
        mRecyclerView = view.findViewById(R.id.meal_planner_received_recycler_view);
        //   mTxtErrorMessage = view.findViewById(R.id.meal_planner_received_txt_error_message);
        mFAB = view.findViewById(R.id.fragment_meal_planner_float_button);
        //  mBtnDelete = view.findViewById(R.id.fragment_meal_planner_btn_delete);

        attachAdapter();  // setting adapter to show training plan
        mFAB.setOnClickListener(onFloatButtonClicked); // creating meal plan
        //    mBtnDelete.setOnClickListener(onDeleteButtonClicked); // deleting meal plan
        mRefreshLayout.setOnRefreshListener(onRefreshLayoutPulled);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_planner, container, false);
    }


    private void attachAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new AdapterMealPlanner(mContext, onAdapterTrainingPlanListener);

        for (int i = 0; i < 20; i++) {
            MealPlan.Plan mealPlan = new MealPlan.Plan();
            mealPlan.setId((i + 1) + "");
            mealPlan.setTitle("Meal Plan created " + (i + 1));

            mAdapter.onMealPlanReceived(mealPlan);
        }

        mRecyclerView.setAdapter(mAdapter);
    }

    private AdapterMealPlanner.OnAdapterMealPlanListener onAdapterTrainingPlanListener = new AdapterMealPlanner.OnAdapterMealPlanListener() {
        @Override
        public void onMealPlanClicked(MealPlan.Plan mealPlan) {
        }

        @Override
        public void onMealPlanOptionsMenuClicked(final MealPlan.Plan mealPlan) {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_DELETE, "Delete Plan");
//            if (mealPlan != null && mealPlan.getCreatedByUserId() != null && SessionHelper.getUserId(mContext).equals(mealPlan.getCreatedByUserId())) {
            builder.addOption(BottomSheetOption.OPTION_SHARE, "Share Plan");
            builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit Plan");
//            }

            ArrayList<BottomSheetOption> bottomSheetOptions = builder.build();

            Log.i(TAG, "bottom sheet options size = " + bottomSheetOptions.size());

            // List<BottomSheetOption> bottomSheetOptions = builder.build();
            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    Log.i(TAG, "bottom sheet options selected, option = " + option);
                    switch (option) {
                        case BottomSheetOption.OPTION_SHARE:
                            startActivityForResult(new Intent(mContext, ActivityContactsAndGroups.class), ConstHelper.RequestCode.SHARE);

                            break;

                        case BottomSheetOption.OPTION_EDIT:
                            Intent intent = new Intent(mContext, ActivityTrainingPlanEdit.class);
                            intent.putExtra(ConstHelper.Key.TRAINING_PLAN, mealPlan);
                            startActivityForResult(intent, ConstHelper.RequestCode.EDIT);
                            break;

                        case BottomSheetOption.OPTION_DELETE:
                            String title = "Are you sure";
                            String message = "This plan will be deleted.";
                            DialogHelper.showCustomDialog(mContext, title, message, "Delete", "close", new DialogHelper.ShowDialogListener() {
                                @Override
                                public void onOkClicked() {
                                    if (mealPlan != null) {
                                        showLoader("Deleting...");
                                        ArrayList<MealPlan.Plan> arrayList = new ArrayList<>();
                                        arrayList.add(mealPlan);
                                        mAdapter.onMealPlanDeleted(mealPlan);
                                        hideLoader();
                                    } else
                                        Log.e(TAG, "mTrainingPlan is null while deleting");
                                }

                                @Override
                                public void onCancelClicked() {

                                }
                            });

                            break;
                    }
                }
            });
            options.show(getChildFragmentManager(), "options");
        }
    };

    private void hideLoader() {
        //DialogHelper.hideLoader(); // hiding d
        if (mLoader != null)
            mLoader.dismiss();

        if (mRefreshLayout != null && mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }


    public void showLoader(String title) {
        if (title == null || title.isEmpty())
            title = "Wait..";

        if (mLoader == null) {
            mLoader = DialogLoader.getInstance(title);
            mLoader.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        } else if (mLoader.isAdded() && mLoader.isVisible()) {
            mLoader = (DialogLoader) getChildFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            if (mLoader != null)
                mLoader.changeProgressTitle(title);
        } else {
            mLoader = null;
            mLoader = DialogLoader.getInstance(title);
            mLoader.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        }

    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshLayoutPulled = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
        }
    };


    // # on float button clicked
    private View.OnClickListener onFloatButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(mContext, ActivityMealPlannerCreate.class));
        }
    };


}
