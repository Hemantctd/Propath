package org.ctdworld.propath.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityFeedback extends BaseActivity
{
    Toolbar mToolbar;
    ImageView mImgToolbarOptionsMenu;
    TextView mToolbarTitle;
    EditText name, email, feedback;
    Button send;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
        setToolbar();
        setListeners();
    }



    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgToolbarOptionsMenu = mToolbar.findViewById(R.id.toolbar_img_options_menu);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        feedback = findViewById(R.id.feedback);
        send = findViewById(R.id.send);

        name.setFocusable(false);
        email.setFocusable(false);
        name.setText(SessionHelper.getInstance(ActivityFeedback.this).getUser().getName());
        email.setText(SessionHelper.getInstance(ActivityFeedback.this).getUser().getEmail());
    }

    private void setListeners() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilHelper.hideKeyboard(ActivityFeedback.this);
                showLoader(getString(R.string.message_sending));
                final String userFeedback = feedback.getText().toString().trim();

                Map<String, String> params = new HashMap<>();
                params.put("add_feedback", "1");
                params.put("name", SessionHelper.getInstance(ActivityFeedback.this).getUser().getName());
                params.put("email", SessionHelper.getInstance(ActivityFeedback.this).getUser().getEmail());
                params.put("message", userFeedback);
                params.put("user_id",SessionHelper.getInstance(ActivityFeedback.this).getUser().getUserId());

                RemoteServer remoteServer = new RemoteServer(mContext);

                remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
                    @Override
                    public void onVolleySuccessResponse(String message) {
                        hideLoader();
                        System.out.println("response : " + message);
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            if (jsonObject.get("res").toString().equals("1"))
                            {
                                DialogHelper.showSimpleCustomDialog(ActivityFeedback.this, "Feedback Submitted Successfully", new DialogHelper.ShowSimpleDialogListener() {
                                    @Override
                                    public void onOkClicked() {

                                        feedback.setText("");
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVolleyErrorResponse(VolleyError error) {
                        hideLoader();
                        System.out.println("error : " + error.toString());
                    }
                });
            }

        });
    }

    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.feedback);
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
        if (item != null && item.getItemId() == android.R.id.home)
            super.onBackPressed();

        return super.onOptionsItemSelected(item);

    }
}
