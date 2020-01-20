package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.OtpHelper;
import org.ctdworld.propath.helper.SendMailHelper;

public class ActivityVerifyEmail extends AppCompatActivity
{
    private static final String TAG = ActivityVerifyEmail.class.getSimpleName();
    String mOtp, mEmail;
    EditText mEditOtp;
    TextView mTxtResendOtp;
    Button mBtnVerify;
    Context mContext;
    OtpHelper mOtpHelper; // if user clicks on resend otp then new otp will be created
    ProgressBar mProgressBar;
    boolean mEmailVerificationStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        init();

        // getting otp sent from registration activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
           mOtp = bundle.getString("otp");
           mEmail = bundle.getString("email");

            Log.i(TAG,"otp = "+mOtp);
            Log.i(TAG,"email  = "+mEmail);

        }

        setListeners();

    }

    private void init()
    {
        mContext = this;
        mOtpHelper = new OtpHelper();
        mProgressBar = findViewById(R.id.verify_email_progressbar);
        mEditOtp = findViewById(R.id.verify_email_edit_otp);
        mTxtResendOtp = findViewById(R.id.verify_email_txt_resend_otp);
        mBtnVerify = findViewById(R.id.verify_email_btn_verify);

    }


    private void setListeners()
    {

        mBtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"verify button clicked ");
                if (mEditOtp.getText().toString().trim().isEmpty())
                    DialogHelper.showSimpleCustomDialog(mContext,"Enter OTP");
                else
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    verifyOtp();
                }

            }
        });

        mTxtResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG,"mTxtResendOtp clicked ");
                mProgressBar.setVisibility(View.VISIBLE);

                mOtpHelper.createOtp();
                mOtp = mOtpHelper.getOtp();
                SendMailHelper sendMailHelper = new SendMailHelper(mContext, mEmail, "Athlete OTP", "Your OTP is " + mOtp, new SendMailHelper.Listener() {
                    @Override
                    public void onSuccess() {
                        DialogHelper.showSimpleCustomDialog(mContext,"OTP sent to your email");
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailed() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

                sendMailHelper.execute();
            }
        });
    }


    private void verifyOtp()
    {
        Log.i(TAG,"verifying otp............");
        String sEditOtp = mEditOtp.getText().toString().trim();
        if (sEditOtp.equals(mOtp))
        {
            mEmailVerificationStatus = true;
            Intent intent = new Intent(mContext,ActivityLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else
            DialogHelper.showSimpleCustomDialog(mContext,"Enter Valid OTP");

        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // if email has not been verified, then deleting user from server
        /*if(!mEmailVerificationStatus)
        {
            final String SERVER_MESSAGE = "message";
            final String SERVER_MESSAGE_SUCCESS = "success";

            final Map<String,String> map = new HashMap<>();
            map.put("email",mEmail);



            RemoteServer remoteServer = new RemoteServer(mContext);
            remoteServer.sendData(RemoteServer.URL_DELETE_USER, map, new RemoteServer.VolleyStringListener() {
                @Override
                public void onVolleySuccessResponse(String message)
                {
                    JSONObject jsonObject ;
                    String serverMessage="";

                    try {
                        jsonObject = new JSONObject(message);
                        serverMessage = jsonObject.getString(SERVER_MESSAGE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (serverMessage.equals(SERVER_MESSAGE_SUCCESS))
                        Log.i(TAG,"user deleted from database because user didn't verify email..");
                    else
                        Log.i(TAG,"user not deleted from database and user didn't verify email..");

                }

                @Override
                public void onVolleyErrorResponse(VolleyError error) {
                    Log.e(TAG," error while deleting user from database because user didn't verify email..");

                }
            });
        }*/
    }
}
