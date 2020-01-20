package org.ctdworld.propath.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.fragment.FragmentSearch;
import org.ctdworld.propath.helper.ConstHelper;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    Context mContext;
    DialogLoader mLoader;
    Dialog progressDialog;

   /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }*/

    public void showToastShort(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


    public void showToastLong(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }


    // to show dialog loader or change title of loader
    public void showLoader(String title) {
        try {
            if (mLoader != null)
                mLoader.dismiss();

            mLoader = null;
            mLoader = DialogLoader.getInstance(title);
            if (mLoader != null && !mLoader.isAdded())
                mLoader.show(getSupportFragmentManager(), "");

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // Log.e(TAG, "Exception while showing loader");
            e.printStackTrace();
        }
    }


    public void showLoaderOrChangeTitle(String title) {
      /*  try {
            if (mLoader == null) {
                mLoader = DialogLoader.getInstance(title);
                mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            } else if (mLoader.isAdded())
                mLoader.changeProgressTitle(title);
        }catch (Exception e)
        {

        }*/

        try {
            if (mLoader != null)
                mLoader.dismiss();

            mLoader = null;
            mLoader = DialogLoader.getInstance(title);
            if (mLoader != null && !mLoader.isAdded())
                mLoader.show(getSupportFragmentManager(), "");

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // Log.e(TAG, "Exception while showing loader");
            e.printStackTrace();
        }
    }


    // to hide dialog loader
    public void hideLoader() {
        try {
            if (mLoader != null && mLoader.isAdded() && mLoader.isVisible())
                mLoader.dismiss();
        } catch (Exception ignored) {
        }
    }


    public boolean isConnectedToInternet(Context context) {
        /*if (context == null)
            return */
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null;
        }

        return false;
    }


    // returns the reference of FragmentSearch
    protected FragmentSearch getSearchFragment() {
        try {
            return (FragmentSearch) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.SEARCH);
        } catch (Exception ignored) {
        }

        return null;
    }

    public void showLoaderInActivity() {
        try {

            if (progressDialog == null) {
                progressDialog = new Dialog(BaseActivity.this);
                progressDialog.setContentView(R.layout.dialog_progress);
                ((TextView) progressDialog.findViewById(R.id.dialog_progress_txt_message)).setText(getResources().getString(R.string.please_wait));
            }
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissLoaderInActivity() {
        try {
            if (progressDialog != null)
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
