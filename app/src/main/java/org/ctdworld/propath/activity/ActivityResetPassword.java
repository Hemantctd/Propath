package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractResetPassword;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterResetPassword;

public class ActivityResetPassword extends AppCompatActivity implements ContractResetPassword.View
{
    private final String TAG = ActivityResetPassword.class.getCanonicalName();

    Toolbar mToolbar;
    ImageView mImgToolbarOptionsMenu;
    TextView mToolbarTitle;
    EditText mEditOldPassword, mEditPassword, mEditConfirmPassword;
    Button mBtnReset;
    ProgressBar mProgressbar;
    Context mContext;
    ContractResetPassword.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
        setToolbar();
        setListeners();
    }


    // to initialize
    private void init() {
        mContext = this;

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);

        mPresenter = new PresenterResetPassword(mContext);
        mEditOldPassword = findViewById(R.id.reset_pass_edit_old_pass);
        mEditPassword = findViewById(R.id.reset_pass_edit_password);
        mEditConfirmPassword = findViewById(R.id.reset_pass_edit_confirm_password);
        mBtnReset = findViewById(R.id.reset_pass_btn_reset);
        mProgressbar = findViewById(R.id.reset_pass_progressbar);
    }


    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Reset Password");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }

    private void setListeners()
    {
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"reset password button clicked...");
                if(isAnyFieldEmpty())
                    DialogHelper.showSimpleCustomDialog(mContext,"Please Enter All Fields");
                else
                {
                    if (arePasswordsSame())
                    {
                        if (UtilHelper.isConnectedToInternet(mContext))
                        {
                            Log.i(TAG,"resetting password....");
                            onSetViewsDisabledOnProgressBarVisible();
                            mProgressbar.setVisibility(View.VISIBLE);
                            mPresenter.resetPassword(getUserDetails());
                        }
                        else
                            DialogHelper.showSimpleCustomDialog(mContext,"No Internet Connection...");
                    }
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,"Passwords Do Not Match");
                }

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

        // checking password
        if (TextUtils.isEmpty(mEditOldPassword.getText().toString().trim()))
            return true;

        // checking password
        if (TextUtils.isEmpty(mEditPassword.getText().toString().trim()))
            return true;

        // checking confirm password
        if (TextUtils.isEmpty(mEditConfirmPassword.getText().toString().trim()))
            return true;

        // false will be returned by default, it means no field is empty
        return false;
    }

    private User getUserDetails()
    {
        User user = new User();

        user.setOldPassword(mEditOldPassword.getText().toString().trim());
        user.setPassword(mEditPassword.getText().toString().trim());
        user.setConfirm_password(mEditConfirmPassword.getText().toString().trim());

        return user;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

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
    public void onSuccess() {
        DialogHelper.showSimpleCustomDialog(mContext, "Congratulations...", "Your password has been reset successfully", new DialogHelper.ShowSimpleDialogListener() {
            @Override
            public void onOkClicked() {
                startActivity(new Intent(mContext,ActivityLogin.class));
                finish();
            }
        });
    }

    @Override
    public void onFailed() {
        DialogHelper.showSimpleCustomDialog(mContext,"Reset Password Failed");
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mEditOldPassword.setEnabled(false);
        mEditPassword.setEnabled(false);
        mEditConfirmPassword.setEnabled(false);
        mBtnReset.setEnabled(false);
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mEditOldPassword.setEnabled(true);
        mEditPassword.setEnabled(true);
        mEditConfirmPassword.setEnabled(true);
        mBtnReset.setEnabled(true);
    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }
}
