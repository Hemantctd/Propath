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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractSignUp;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.OtpHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterSignUp;

public class ActivityRegister extends AppCompatActivity implements ContractSignUp.View
{
    private static final String TAG = ActivityRegister.class.getSimpleName();

    Toolbar mToolbar;
    EditText mEditName, mEditEmail, mEditPassword, mEditConfirmPassword;
    ImageView mImgBack;
    Spinner mSpinRole;
    TextView mToolbarTitle;
    Button mBtnRegister, mBtnLogin;
    ProgressBar mProgressbar;
    Context mContext;
    ContractSignUp.Presenter mPresenter;
    OtpHelper mOtpHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setListeners();
        UtilHelper.setUpSpinner(mContext, mSpinRole);  // setting up spinner
    }


    // to initialize
    private void init() {
        mContext = this;
        mOtpHelper = new OtpHelper();
        mPresenter = new PresenterSignUp(mContext);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar);
        mEditName = findViewById(R.id.sign_up_edit_name);
        mEditEmail = findViewById(R.id.sign_up_edit_email);
        mEditPassword = findViewById(R.id.sign_up_edit_password);
        mEditConfirmPassword = findViewById(R.id.sign_up_edit_confirm_password);
        mBtnRegister = findViewById(R.id.sign_up_btn_sign_up);
        mBtnLogin = findViewById(R.id.sign_up_btn_login);
        mProgressbar = findViewById(R.id.sign_up_progressbar);
        mSpinRole = findViewById(R.id.sign_up_spin_role);
    }


    private void setListeners()
    {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityRegister.this,ActivityLogin.class));
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"register button clicked...");
                if(isAnyFieldEmpty())
                    DialogHelper.showSimpleCustomDialog(mContext,"Please Enter All Fields");
                else
                {
                    if (checkValidation())
                    {
                        if (arePasswordsSame())
                        {
                            if (UtilHelper.isConnectedToInternet(mContext))
                            {
                                Log.i(TAG,"registering....");
                                mProgressbar.setVisibility(View.VISIBLE);
                                onSetViewsDisabledOnProgressBarVisible();
                                mPresenter.createAccount(getUserDetails());
                            }
                            else
                                DialogHelper.showSimpleCustomDialog(mContext,"No Internet Connection...");
                        }
                        else
                            DialogHelper.showSimpleCustomDialog(mContext,"Passwords Do Not Match");
                    }
                }

            }

        });

        findViewById(R.id.register_img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


     private boolean arePasswordsSame()
     {
         String password = mEditPassword.getText().toString().trim();
         String confirmPassword = mEditConfirmPassword.getText().toString().trim();

         if (password.equals(confirmPassword))
             return true;
         else
             return false;
     }


    // to check any field is empty or not
    private boolean isAnyFieldEmpty() {

        // checking name
        if (TextUtils.isEmpty(mEditName.getText().toString().trim()))
            return true;

        // checking email
        if (TextUtils.isEmpty(mEditEmail.getText().toString().trim()))
            return true;

        // checking password
        if (TextUtils.isEmpty(mEditPassword.getText().toString().trim()))
            return true;

        // checking confirm password
        if (TextUtils.isEmpty(mEditConfirmPassword.getText().toString().trim()))
            return true;

        // checking role
        String role = getResources().getStringArray(R.array.entries_role)[0];
        if (role.equals(mSpinRole.getSelectedItem().toString()))
            return true;

        // false will be returned by default, it means no field is empty
        return false;
    }

    private User getUserDetails()
    {
        User user = new User();
        mOtpHelper.createOtp();

        user.setName(mEditName.getText().toString().trim());
        user.setEmail(mEditEmail.getText().toString().trim());
        user.setPassword(mEditPassword.getText().toString().trim());
        user.setConfirm_password(mEditConfirmPassword.getText().toString().trim());

        // in string resource file there  are two arrays entries_role and entries_role_value, entries_role_value contains corresponding
        // item as in entries_role. so here we are getting entries_role_value value according to selected item in entries_role
        String roleValue = getResources().getStringArray(R.array.entries_role_value)[mSpinRole.getSelectedItemPosition()];
        user.setRoleId(roleValue);
    //    user.setOtp(mOtpHelper.getOtp());

        return user;
    }


    // to check if email id is valid or not, returns true if valid otherwise false
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
        mProgressbar.setVisibility(View.VISIBLE);
    }



    @Override
    public void onHideProgress() {
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void onSignUpSuccess() {
      /*  Intent intent = new Intent(mContext,ActivityVerifyEmail.class);
        intent.putExtra("otp",mOtpHelper.getOtp());
        intent.putExtra("email",mEditEmail.getText().toString().trim());*/
        Log.i(TAG,"registered successfully, now starting login activity...");
        startActivity(new Intent(mContext,ActivityLogin.class));
    }

    @Override
    public void onSignUpFailed() {
        Log.i(TAG,"onSignUpFailed() method called ");
        DialogHelper.showSimpleCustomDialog(mContext,"Sign Up Failed");
    }

    @Override
    public void onEmailAlreadyRegistered() {
        Log.i(TAG,"Email is already registered now starting login activity...");
        startActivity(new Intent(mContext,ActivityLogin.class));
       /// DialogHelper.showSimpleCustomDialog(mContext,"Email Is Already Registered");
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mEditName.setEnabled(false);
        mEditEmail.setEnabled(false);
        mEditPassword.setEnabled(false);
        mEditConfirmPassword.setEnabled(false);
        mSpinRole.setEnabled(false);
        mBtnRegister.setEnabled(false);
        mBtnLogin.setEnabled(false);
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mEditName.setEnabled(true);
        mEditEmail.setEnabled(true);
        mEditPassword.setEnabled(true);
        mEditConfirmPassword.setEnabled(true);
        mSpinRole.setEnabled(true);
        mBtnRegister.setEnabled(true);
        mBtnLogin.setEnabled(true);
    }

    @Override
    public void onConnectionError() {
        DialogHelper.showSimpleCustomDialog(mContext,"Connection Error...");
    }



}
