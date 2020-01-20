package org.ctdworld.propath.helper;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;


public class PermissionHelper implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = PermissionHelper.class.getSimpleName();
    private Context context;
    private Activity activity;
    private Listener listener;
    private static final int PERMISSION_REQUEST_CODE = 100;

    public PermissionHelper(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.listener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }


    interface Listener {
        void onPermissionCancelled();

        void onPermissionGranted();

        void onPermissionDoNotAskAgain();
    }


    public void requestPermission(final String permission, String messageToExplainPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "permission request for = " + permission);
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                //  listener.onPermissionGranted();
                Log.i(TAG, "permission is granted");
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    //  Log.i(TAG,"you should show message ");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Permission Required");
                    builder.setMessage(messageToExplainPermission);
                    builder.setPositiveButton("Give Permission", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();


                } else {
                    Log.i(TAG, "requesting permission............");
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
                }
            }
        }
    }

    public boolean isPermissionGranted(final String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            // Log.i(TAG,"permission granted");
            return true;
        } else {
            //  Log.i(TAG,"permission granted");
            return false;
        }
    }

}
