package org.ctdworld.propath.activity;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.prefrence.SessionHelper;


public class ActivitySplash extends AppCompatActivity {

    private static final String TAG = ActivitySplash.class.getSimpleName();
    private static final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Log.i(TAG, "firebase token = "+SessionHelper.getInstance(this).getFirebaseRegistrationToken());

        // creating role list RoleHelper is singleton class to create and get All roles list
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoleHelper.getInstance(ActivitySplash.this).createRoleFromServer();
            }
        }).start();



        animate();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (SessionHelper.getInstance(ActivitySplash.this).isUserLoggedIn())
                    startActivity(new Intent(ActivitySplash.this,ActivityMain.class));
                else
                    startActivity(new Intent(ActivitySplash.this, ActivityLoginRegister.class));

                finish();
            }
        },SPLASH_TIME);

    }

    private void animate()
    {


        Animation animLogo = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
       // Animation animText = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        View logo = findViewById(R.id.img_logo);
      //  TextView textView = findViewById(R.id.activity_splash_txt_logo);

        logo.setAnimation(animLogo);
       // textView.setAnimation(animText);

    }

}
