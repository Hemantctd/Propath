package org.ctdworld.propath.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterViewCareerPlan;

import java.util.ArrayList;

public class ActivityViewCareerPlan extends AppCompatActivity {

    RecyclerView careerRecyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Toolbar mToolbar;
    ImageView mToolbarOptionsMenu;
    TextView mToolbarTitle;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> careerDateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_career_plan);
        setToolbar();
        init();
        setCareerListAdapter();

    }
    private void init() {
        mToolbarOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        arrayList.add("career plan");
        arrayList.add("career plan");
        arrayList.add("career plan");
        arrayList.add("career plan");
        arrayList.add("career plan");
        careerDateList.add("19 june 2018");
        careerDateList.add("19 june 2018");
        careerDateList.add("19 june 2018");
        careerDateList.add("19 june 2018");
        careerDateList.add("19 june 2018");


    }
    private void setToolbar() {
//        setSupportActionBar(mToolbar);
//        mToolbarOptionsMenu.setVisibility(View.VISIBLE);
//        mToolbarTitle.setText("Career PlanData");
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }

    private void setCareerListAdapter() {
        careerRecyclerView = findViewById(R.id.careerRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        adapter = new AdapterViewCareerPlan(this,arrayList,careerDateList);

        careerRecyclerView.setLayoutManager(layoutManager);
        careerRecyclerView.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return true;
    }
}
