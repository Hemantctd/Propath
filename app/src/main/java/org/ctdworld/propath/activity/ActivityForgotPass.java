package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import org.ctdworld.propath.R;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityForgotPass extends AppCompatActivity
{
    private static final String TAG = ActivityForgotPass.class.getSimpleName();

    Context mContext;
    Button mBtnSendEmail;
    EditText mEditEmail;
    ProgressBar mProgressBar;


    private final String KEY_FORGOT_PASS_DEFAULT_VALUE= "forgotpassword";
    private final String FORGOT_PASS_DEFAULT_VALUE= "1";
    private final String KEY_EMAIL= "email";

    private final String RES_KEY_SUCCESS = "success";
    private final String RES_KEY_MESSAGE = "message";
    private final String RES_VALUE_SUCCESS = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Log.i(TAG,"main thread = "+Thread.currentThread());
        init();
        setListeners();
    }


    private void setListeners() {
        mBtnSendEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!mEditEmail.getText().toString().trim().isEmpty())
                {
                    if (Patterns.EMAIL_ADDRESS.matcher(mEditEmail.getText().toString().trim()).matches())
                    {
                        Log.i(TAG,"setListeners , thread = "+Thread.currentThread());
                        mProgressBar.setVisibility(View.VISIBLE);
                        sendEmail();
                    }
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,"Enter Valid Email Id");
                }
                else
                    DialogHelper.showSimpleCustomDialog(mContext,"Enter Email Id");
            }
        });
    }

    private void init() {
                mContext = ActivityForgotPass.this;
                mBtnSendEmail = findViewById(R.id.forgot_pass_btn_send);
                mEditEmail = findViewById(R.id.forgot_pass_edit_email);
                mProgressBar = findViewById(R.id.forgot_pass_progress_bar);
    }

    private void sendEmail()
    {
        RemoteServer remoteServer = new RemoteServer(mContext);
        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                try {
                    JSONObject jsonObject = new JSONObject(message.trim());
                    if (RES_VALUE_SUCCESS.equals(jsonObject.getString(RES_KEY_SUCCESS)))
                    {
                        DialogHelper.showSimpleCustomDialog(mContext, "Congratulations...", jsonObject.getString(RES_KEY_MESSAGE), new DialogHelper.ShowSimpleDialogListener() {
                            @Override
                            public void onOkClicked() {
                                startActivity(new Intent(mContext,ActivityLogin.class));
                            }
                        });
                    }
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,"Failed Try Again...");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    DialogHelper.showSimpleCustomDialog(mContext,"Failed Try Again...");
                }
                finally {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                DialogHelper.showSimpleCustomDialog(mContext,"Connection Error...");
            }
        });

    }

    private Map<String,String> getParams()
    {
        Map<String,String> params = new HashMap<>();
        params.put(KEY_FORGOT_PASS_DEFAULT_VALUE,FORGOT_PASS_DEFAULT_VALUE);
        params.put(KEY_EMAIL,mEditEmail.getText().toString().trim());

        return params;
    }

}
