package org.ctdworld.propath.activity;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFAQ;
import org.ctdworld.propath.model.FAQ;

import java.util.ArrayList;
import java.util.List;

public class ActivityFAQ extends AppCompatActivity
{
    Context mContext;

    Toolbar mToolbar;
    TextView mToolbarTitle;

    RecyclerView mRecyclerView;
    AdapterFAQ mAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);

        setToolbar();
        setAdapter();

    }

    private void setAdapter()
    {
        mRecyclerView = findViewById(R.id.activity_faq_recycler_faq);

        List<FAQ> faqList =  new ArrayList<>();

        for (int i= 0; i<15 ; i++)
        {
            faqList.add(new FAQ("FAQ "+(i+1), "Description for faq "+(i+1)));
        }

        layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterFAQ(mContext,faqList );
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("FAQ");
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
