package org.ctdworld.propath.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ctdworld.propath.R;

public class ActivityMatchDayFuture4 extends AppCompatActivity {

    Toolbar mToolbar;
    TextView mToolbarTitle;
    Context mContext;

    Button mNext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_day_future4);
        init();
        setToolbar();  // setting toolbar

    }



    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mNext = findViewById(R.id.btn_next);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,ActivityMatchDayFuture5.class));
            }
        });

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
