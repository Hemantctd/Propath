package org.ctdworld.propath.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMatchDayUsersList;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.PermissionHelper;

public class ActivityMatchDayShareEvent extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = ActivityMatchDayShareEvent.class.getSimpleName();

    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView surveyGroupRecyclerView;
    Button surveyDone;
    EditText mEditSearchAthleteNames;
    Context mContext;
    LinearLayoutManager mLayoutManager;
    ImageView mImgSearch;
    SwipeRefreshLayout mRefreshLayout;
    PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_day_share_team);
        init();
        setToolBar();
        setListeners();

        prepareRecyclerView();

        mRefreshLayout.setRefreshing(true);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    // for initialization
    private void init() {
        mContext = this;
        mPermissionHelper = new PermissionHelper(mContext);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        surveyGroupRecyclerView = findViewById(R.id.surveyGroupRecyclerView);
        mRefreshLayout = findViewById(R.id.contact_search_refresh_layout);
        surveyDone = findViewById(R.id.surveyDone);
        mEditSearchAthleteNames = findViewById(R.id.contact_search_edit_search);
        mImgSearch = findViewById(R.id.contact_search_img_search);
        surveyDone.setOnClickListener(this);
    }


    // set adapter
    private void prepareRecyclerView() {
        AdapterMatchDayUsersList adapterSurveyGroup = new AdapterMatchDayUsersList(mContext);
        mLayoutManager = new LinearLayoutManager(mContext);
        surveyGroupRecyclerView.setLayoutManager(mLayoutManager);
        surveyGroupRecyclerView.setAdapter(adapterSurveyGroup);
    }


    // button clickable
    private void setListeners() {
        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    voiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, "Permission required voice feature");
            }
        });

        // to filter list
        mEditSearchAthleteNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    // for converting voice to text
    private void voiceToText() {
        Log.i(TAG, "voiceToText() method called ");
        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
        else {
            FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
            fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                @Override
                public void onReceiveText(String spokenText) {
                    mEditSearchAthleteNames.setText(spokenText);
                    mEditSearchAthleteNames.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearchAthleteNames.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(), "");
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.surveyDone) {
        }


    }


    // set tool bar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey_contacts);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

}
