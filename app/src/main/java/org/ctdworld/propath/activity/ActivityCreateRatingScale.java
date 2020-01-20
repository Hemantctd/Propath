package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.ctdworld.propath.R;

public class ActivityCreateRatingScale extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    Button mNextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rating_scale);
        init();
    }


    private void init()
    {
        mContext = this;
        mNextBtn = findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.next_btn:
                startActivity(new Intent(mContext, ActivityRatingScaleQuestions.class));
                break;
        }
    }
}
