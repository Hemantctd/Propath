package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterTrainingPlanItemViewSlider;
import org.ctdworld.propath.contract.ContractTrainingPlan;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.fragment.DialogTrainingPlanCreateEditItem;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;
import org.ctdworld.propath.presenter.PresenterTrainingPlan;

import java.util.ArrayList;

public class ActivityTrainingPlanItemViewSlider extends AppCompatActivity implements ContractTrainingPlan.PlanItemView
{
    private final String TAG = ActivityTrainingPlanItemViewSlider.class.getSimpleName();

    public static final String EXTRA_TRAINING_PLAN = "item list";
    public static final String EXTRA_SELECTED_ITEM_POSITION = "selected position";
    public static final String EXTRA_TRAINING_PLAN_ID = "plan_id";




    private Context mContext;
    ContractTrainingPlan.Presenter mPresenter;
    private Toolbar mToolbar;
    private ImageView mImgToolbarOptionsMenu;
    private TextView mToolbarTitle;

    ViewPager mViewPager;
    AdapterTrainingPlanItemViewSlider mAdapter;

    private ArrayList<TrainingPlan.PlanData.PlanItem> mListPlanItems;
    private TrainingPlan.PlanData mTrainingPlanPlanData;
    private int mSelectedItemPosition; // position of item which has been selected in item list
   // private String mTrainingPlanId = null;

    DialogLoader mLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan_item_view_slider);

        mContext = this;
        mPresenter = new PresenterTrainingPlan(mContext, this);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgToolbarOptionsMenu = mToolbar.findViewById(R.id.toolbar_img_options_menu);
        mViewPager = findViewById(R.id.activity_training_plan_item_view_viewpager);

        if (getIntent() != null && getIntent().getExtras() != null)
        {
            mSelectedItemPosition = getIntent().getExtras().getInt(EXTRA_SELECTED_ITEM_POSITION);
            mTrainingPlanPlanData = (TrainingPlan.PlanData) getIntent().getExtras().getSerializable(EXTRA_TRAINING_PLAN);
            if (mTrainingPlanPlanData != null)
                mListPlanItems = mTrainingPlanPlanData.getPlanItemList();

        }

        setToolbar();

        // # getting list of items which are not a link. items may contain (title and image or video) or link only
       // ArrayList<PlanData.PlanItem> filteredItems = getItemList(mListPlanItems);
        mAdapter = new AdapterTrainingPlanItemViewSlider(mContext, mListPlanItems);
        mViewPager.setAdapter(mAdapter);  // setting adapter to viewPager

        if (mSelectedItemPosition >= 0)
            mViewPager.setCurrentItem(mSelectedItemPosition);

        mViewPager.addOnPageChangeListener(onPageSlide);
        mImgToolbarOptionsMenu.setOnClickListener(onOptionsMenuIconClicked);
    }


    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(getString(R.string.training_plan_item));

        if (getSupportActionBar() == null)
            return;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    // deleting plan item
    private void deletePlanItem()
    {
        final PlanData.PlanItem planItem = mListPlanItems.get(mSelectedItemPosition);
        String title = getString(R.string.are_you_sure);
        String message = "Your plan item will be deleted.";

        DialogHelper.showCustomDialog(mContext, title, message, "Delete", "Cancel", new DialogHelper.ShowDialogListener() {
            @Override
            public void onOkClicked() {
                mPresenter.deleteTrainingPlanItem(mTrainingPlanPlanData.getId(), planItem);
            }

            @Override
            public void onCancelClicked() {

            }
        });
    }


    // editing plan item
    private void editPlanItem()
    {
        final PlanData.PlanItem planItem = mListPlanItems.get(mSelectedItemPosition);
        DialogTrainingPlanCreateEditItem editItem = DialogTrainingPlanCreateEditItem.getInstance(planItem, DialogTrainingPlanCreateEditItem.EXTRA_EDIT);
        editItem.setOnButtonClickListener(new DialogTrainingPlanCreateEditItem.OnButtonClickListener() {
            @Override
            public void onAddButtonClicked(TrainingPlan.PlanData.PlanItem planItem) {
                Log.i(TAG,"plan Id = "+ mTrainingPlanPlanData.getId()+" , edited item title = "+planItem.getTitle()+" , edited item media = "+planItem.getMediaUrl()+" , edited item media type = "+planItem.getMediaType());
                showLoader();
                mPresenter.editTrainingPlanItem(mTrainingPlanPlanData.getId(), planItem);
            }
        });
        editItem.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_CREATE_TRAINING_PLAN_ITEM);
    }



    private ViewPager.OnPageChangeListener onPageSlide = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            mSelectedItemPosition = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


    View.OnClickListener onOptionsMenuIconClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_EDIT,"Edit Item")
                    .addOption(BottomSheetOption.OPTION_DELETE,"Delete Item");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option)
                {
                    if (mListPlanItems == null)
                        return;

                    switch (option)
                    {
                        case BottomSheetOption.OPTION_EDIT:
                            editPlanItem();
                            break;

                        case BottomSheetOption.OPTION_DELETE:
                            deletePlanItem();
                            break;
                    }
                }
            });

            options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        try {

            mTrainingPlanPlanData.setPlanItemList(mAdapter.getPlanItemList());  // setting latest plan items list (items may be edited)
            Intent intent = new Intent();
            intent.putExtra(ConstHelper.Key.TRAINING_PLAN_SINGLE_PLAN, mTrainingPlanPlanData);
            setResult(RESULT_OK, intent);
            finish();
        }
        catch (Exception e)
        {
            super.onBackPressed();
        }
    }

    private void showLoader()
    {
        if (mLoader == null || !mLoader.isAdded())
            mLoader = DialogLoader.getInstance("Saving...");

        mLoader.show(getSupportFragmentManager(),  ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
    }

    private void hideLoader()
    {
        if (mLoader != null)
            mLoader.dismiss();
    }


    @Override
    public void onTrainingPlanItemDeleted(final TrainingPlan.PlanData.PlanItem planItem)
    {
        Log.i(TAG,"onTrainingPlanItemDeleted() method called, planItem id = "+planItem.getId());

        hideLoader();
        mAdapter.onItemDeleted(planItem);
    }

    @Override
    public void onTrainingPlanItemEdited(PlanData.PlanItem planItem)
    {
        Log.i(TAG,"onTrainingPlanItemEdited() method called");
        hideLoader();
        if (mAdapter != null)
            mAdapter.updateItem(planItem);
    }

    @Override
    public void onFailedPlanItem(String message) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext, message);
    }

    @Override
    public void onShowMessagePlanItem(String message) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext, message);
    }


}
