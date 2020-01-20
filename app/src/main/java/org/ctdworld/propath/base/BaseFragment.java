package org.ctdworld.propath.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.helper.ConstHelper;

public class BaseFragment extends Fragment
{
    public Context mContext;
    DialogLoader mLoader;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    public boolean isConnectedToInternet()
    {
        /*if (context == null)
            return */
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null;
        }

        return false;
    }


    // to show dialog loader or change title of loader
    public void showLoader(String title)
    {
        try {
            if (mLoader == null)
            {
                mLoader = DialogLoader.getInstance(title);
                mLoader.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            }
            else if (mLoader.isAdded())
                mLoader.changeProgressTitle(title);

          //  mLoader.setCancelable(isCancelable);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // to hide dialog loader
    public void hideLoader()
    {
        try {
            if (mLoader != null && mLoader.isAdded())
                mLoader.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void showToastShort(String message)
    {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String message)
    {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
