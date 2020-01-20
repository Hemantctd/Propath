package org.ctdworld.propath.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMatchDayFuture1;
import org.ctdworld.propath.adapter.AdapterMatchSetUp;

import java.util.ArrayList;

public class ActivityMatchDayFuture1 extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = ActivityCareer.class.getSimpleName();
    Toolbar mToolbar;
    TextView mToolbarTitle;
    Context mContext;
    RecyclerView mRecyclerView;
    Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_day_future1);
        init();
        prepareRecyclerView();
        setToolbar();  // setting toolbar
    }

    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mNextBtn = findViewById(R.id.next_btn);
        mRecyclerView = findViewById(R.id.recycler_choose_support);
        mNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            /*case R.id.next_btn :
                startActivity(new Intent(mContext, ActivityMatchAnalysisSetUpCreate.class));
                break;*/
        }
    }

    private void prepareRecyclerView()
    {
        ArrayList<String> arrayListText = new ArrayList<>();
        arrayListText.add("New Sport");
        arrayListText.add("Netball");
        arrayListText.add("Rugby");
        arrayListText.add("Boxing");
        arrayListText.add("Rugby League");
        arrayListText.add("Touch Rugby");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchDayFuture1 adapter = new AdapterMatchDayFuture1(mContext,arrayListText);
        mRecyclerView.setAdapter(adapter);
    }

    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Match Set Up");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
