package org.ctdworld.propath.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.model.BottomSheetOption;

import java.util.ArrayList;

public class ActivityMatchDayEventEdit extends AppCompatActivity {
    private final String TAG = ActivityMatchDayEvent.class.getSimpleName();

    Context mContext;

    ImageView toolBarOptions;
    Toolbar mToolbar;
    TextView mToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_day_event_edit);
        init();
        setToolBar();

    }

    // for initialization
    private void init() {
        mContext = this;

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        toolBarOptions = findViewById(R.id.toolbar_img_options_menu);
        toolBarOptions.setVisibility(View.VISIBLE);


        toolBarOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<BottomSheetOption> bottomSheetOptions  = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_EDIT,"Edit").build();


                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
                {

                    @Override
                    public void onOptionSelected(int option)
                    {
                        if (option == BottomSheetOption.OPTION_EDIT) {


                        }
                    }
                });
                if (getFragmentManager() != null) {
                    options.show(getSupportFragmentManager(), "options");
                }
            }
        });
    }



    // set tool bar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Match Day");
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
