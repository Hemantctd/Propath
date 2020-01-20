package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractSignIn;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterSignIn;


public class ActivityLogin extends AppCompatActivity implements ContractSignIn.View
{
    private static final String TAG = ActivityLogin.class.getCanonicalName();
    Toolbar toolbar;
    EditText mEditEmail, mEditPassword;
    Button mBtnLogin;
    TextView mTxtForgotPassword;
    Context mContext;
    ContractSignIn.Presenter mPresenter;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setListeners();
    }

    private void init()
    {
        mContext = this;
        mPresenter = new PresenterSignIn(mContext);
        mProgressBar = findViewById(R.id.login_progressbar);
        mEditEmail = findViewById(R.id.login_edit_email);
        mEditPassword = findViewById(R.id.login_edit_password);
        mBtnLogin = findViewById(R.id.login_btn_login);
        mTxtForgotPassword = findViewById(R.id.login_txt_forgot_password);
    }

    private void setListeners() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Login button clicked");
                if(isAnyFieldEmpty())
                    DialogHelper.showSimpleCustomDialog(mContext,"Please Enter All Fields");
                else
                {
                    if (checkValidation())
                    {
                        if (UtilHelper.isConnectedToInternet(mContext))
                        {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mPresenter.signIn(getUserDetails());
                            onSetViewsDisabledOnProgressBarVisible();
                        }
                        else
                            DialogHelper.showSimpleCustomDialog(mContext,"No Internet Connection...");

                    }
                }
            }
        });

        mTxtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogin.this,ActivityForgotPass.class));
            }

        });
    }

    // to check any field is empty or not
    private boolean isAnyFieldEmpty() {

        // checking email
        if (TextUtils.isEmpty(mEditEmail.getText().toString().trim()))
            return true;

        // checking password
        if (TextUtils.isEmpty(mEditPassword.getText().toString().trim()))
            return true;

        // false will be returned by default, it means no field is empty
        return false;
    }

    // returns user details mobile number and otp
    private User getUserDetails()
    {
        User user = new User();
        user.setEmail(mEditEmail.getText().toString().trim());
        user.setPassword(mEditPassword.getText().toString().trim());

        return user;
    }


    // to check if  email id is valid or not
    private boolean checkValidation()
    {
        if (!Patterns.EMAIL_ADDRESS.matcher(mEditEmail.getText().toString().trim()).matches())
        {
            DialogHelper.showSimpleCustomDialog(mContext,"Enter Valid Email...");
            return false;
        }
            return true;
    }





    @Override
    public void onShowProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onEmailOrPasswordWrong() {
        DialogHelper.showSimpleCustomDialog(mContext,"Email Or Password Is Wrong");
    }


    @Override
    public void onSuccess()
    {
        Log.i(TAG,"onSuccess ");
        User user = SessionHelper.getInstance(mContext).getUser();
        Log.i(TAG,"id = "+user.getUserId());
        Log.i(TAG,"name = "+user.getName());
        Log.i(TAG,"email = "+user.getEmail());
        Log.i(TAG,"Auth token = "+user.getAuthToken());
        Log.i(TAG,"status = "+user.getStatus());
        Log.i(TAG,"activity = "+user.getActivity());
        Log.i(TAG,"role = "+user.getRoleId());

        startActivity(new Intent(mContext,ActivityMain.class));
        finishAffinity();
    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mEditEmail.setEnabled(false);
        mEditPassword.setEnabled(false);
        mBtnLogin.setEnabled(false);
        mTxtForgotPassword.setEnabled(false);
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mEditEmail.setEnabled(true);
        mEditPassword.setEnabled(true);
        mBtnLogin.setEnabled(true);
        mTxtForgotPassword.setEnabled(true);
    }

    @Override
    public void onConnectionError() {
        DialogHelper.showSimpleCustomDialog(mContext,"Connection Error...");
    }

}
