package org.ctdworld.propath.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterYesOrNoSurveyQuestions;


public class ActivityYesOrNoActivity extends AppCompatActivity implements View.OnClickListener{

    Button mBtnPreview;
    Context mContext;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;   // toolbar
    TextView mTxtToolbarTitle;  // for toolbar title


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_or_no);
        init();
        prepareToolbar();
        prepareRecyclerView();
    }

    private void init ()
    {

        mContext =  this;
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mBtnPreview = findViewById(R.id.btn_preview);
        mRecyclerView = findViewById(R.id.recycler_yes_no_questions);
        mBtnPreview.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_preview)
        {
            Log.d("called","called");
        }
    }

    private void prepareRecyclerView()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        AdapterYesOrNoSurveyQuestions adapter  = new AdapterYesOrNoSurveyQuestions(mContext);
        mRecyclerView.setAdapter(adapter);

    }
    private void prepareToolbar()
    {
        setSupportActionBar(mToolbar);
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
