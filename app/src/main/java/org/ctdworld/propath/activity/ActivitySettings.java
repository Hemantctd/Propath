package org.ctdworld.propath.activity;

import android.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentSetting;


public class ActivitySettings extends AppCompatActivity
{
    private static final String TAG = ActivitySettings.class.getSimpleName();
  //  ContractMain.View mMainView;
    Toolbar toolbar;
    TextView mToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_settings);
            mSetAcctionBar();
            mToolbarTitle = findViewById(R.id.toolbar_txt_title);
            mToolbarTitle.setText(R.string.settings);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container_fragment_setting,new FragmentSetting(),"fragment").commit();

        }
        catch (Exception e)
        {
            Log.e(TAG,"error in onCreate() method, "+e.getMessage());
            e.printStackTrace();
        }
    }



    private void mSetAcctionBar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.settings);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
