package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMatchSetUp;

import java.util.ArrayList;

public class ActivityMatchAnalysisSetUp extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ActivityCareer.class.getSimpleName();
    Toolbar mToolbar;
    TextView mToolbarTitle;
    Context mContext;
    RecyclerView mRecyclerView;
    Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_set_up);
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
            case R.id.next_btn :
                startActivity(new Intent(mContext, ActivityMatchAnalysisSetUpCreate.class));
                break;
        }
    }

    private void prepareRecyclerView()
    {
        ArrayList<String> arrayListText = new ArrayList<>();
        arrayListText.add("Boxing");
        arrayListText.add("Netball");
        arrayListText.add("Rugby");
        arrayListText.add("Rugby League");
        arrayListText.add("Tag Football");
        arrayListText.add("Touch Rugby");

        ArrayList<Integer> arrayListImages = new ArrayList<>();
        arrayListImages.add(R.drawable.ic_boxing_gloves);
        arrayListImages.add(R.drawable.ic_ball2);
        arrayListImages.add(R.drawable.ic_ball);
        arrayListImages.add(R.drawable.ic_ball);
        arrayListImages.add(R.drawable.ic_ball);
        arrayListImages.add(R.drawable.ic_ball);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchSetUp adapterMatchSetUp = new AdapterMatchSetUp(mContext,arrayListText,arrayListImages);
        mRecyclerView.setAdapter(adapterMatchSetUp);
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
