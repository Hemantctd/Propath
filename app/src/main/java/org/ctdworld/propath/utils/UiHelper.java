package org.ctdworld.propath.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.afollestad.materialdialogs.MaterialDialog;

import org.ctdworld.propath.enums.ImagePickerEnum;
import org.ctdworld.propath.listeners.IImagePickerLister;


public class UiHelper {

    public static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    public static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;


    public void showImagePickerDialog(@NonNull Context callingClassContext, final IImagePickerLister imagePickerLister) {
        new MaterialDialog.Builder(callingClassContext)
               // .items(R.array.imagePicker)
                .canceledOnTouchOutside(true)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (position == 0)
                            imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_GALLERY);
                        else if (position == 1)
                            imagePickerLister.onOptionSelected(ImagePickerEnum.FROM_CAMERA);
                        dialog.dismiss();
                    }
                }).show();
    }

    public void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkSelfPermissions(@NonNull Activity activity) {
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_STORAGE_REQUEST_CODE);
            return false;
        } else if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},ONLY_CAMERA_REQUEST_CODE);
            return false;
        } else if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},ONLY_STORAGE_REQUEST_CODE);
            return false;
        }
        return true;
    }
}
