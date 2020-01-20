package org.ctdworld.propath.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.ctdworld.propath.R;

public class ActivityLoginRegister extends AppCompatActivity
{

    Button mBtnRegister, mBtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        init();
        setListeners();
    }

    private void setListeners() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLoginRegister.this,ActivityLogin.class));
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLoginRegister.this,ActivityRegister.class));
            }
        });
    }

    private void init() {
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);
    }
}
