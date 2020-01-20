package org.ctdworld.propath.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyYesNoSubmit;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Survey;

public class ActivitySurveyYesNoSubmit extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG =  ActivitySurveyYesNoSubmit.class.getSimpleName();


    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView mRecyclerView;
    Context mContext;
    Survey.SurveyData.YesNo mSurveyYesNo;
    AdapterSurveyYesNoSubmit mAdapter;
    String mSurveyType;
    LinearLayout mCommonDataLayout;
    TextView mTxtSurveyDesc, mTxtSurveyTitle, mTxtSurveyType;
    ImageView mToolbarImageOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_yes_no_submit);
        init();
        setToolbar();
        prepareRecyclerView();
    }

    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRecyclerView = findViewById(R.id.recycler_yes_no_questions);

        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);
        mToolbarImageOptions.setVisibility(View.VISIBLE);
        mToolbarImageOptions.setOnClickListener(this);


        mTxtSurveyTitle = findViewById(R.id.set_survey_title);
        mTxtSurveyType = findViewById(R.id.set_survey_type);
        mTxtSurveyDesc = findViewById(R.id.set_survey_desc);
        mCommonDataLayout = findViewById(R.id.common_data_layout);




    }

    private void prepareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterSurveyYesNoSubmit(this);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.yes_no_preview);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.toolbar_img_options_menu)
        {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            builder.addOption(BottomSheetOption.OPTION_SAVE, "Save");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    switch (option) {
                        case BottomSheetOption.OPTION_SAVE:

                            break;
                    }

                }
            });

            try {
                options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

        }
    }
}
