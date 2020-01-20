package org.ctdworld.propath.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;

public class DialogHelper
{
    private static final String TAG = DialogHelper.class.getSimpleName();
    private interface MyDialogListener{}
    public interface ShowSimpleDialogListener extends MyDialogListener{ void onOkClicked();}
    public interface ShowDialogListener extends MyDialogListener{ void onOkClicked(); void onCancelClicked();}

    // loader
  //  private static DialogLoader mLoader;


    // inflates dialog layout and  sets layout(view) to AlertDialog.Builder object.
    private static void showDialog(Context context, boolean showCancelButton, String title, String message, String btnOkText, String btnCancelText, final MyDialogListener listener)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.dialog,null);

        TextView txtTitle = view.findViewById(R.id.show_message_txt_title);
        TextView txtMessage = view.findViewById(R.id.show_message_txt_message);
        TextView btnOk = view.findViewById(R.id.show_message_btn_ok);
        TextView btnCancel = view.findViewById(R.id.show_message_btn_cancel);


        txtTitle.setText(title);

        if(message != null)
        {
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(message);
        }

        if (btnOkText != null)
            btnOk.setText(btnOkText);  // setting text at the place of cancel

        if (btnCancelText != null)
            btnCancel.setText(btnCancelText);   // setting text at the place of ok


        // setting custom dialog layout on AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        // creating dialog
        final AlertDialog dialog  = builder.create();
        if (dialog != null)
        {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }


        Log.i(TAG,"listener = "+listener);

        // enabling cancel button and setting ShowDialogListener for both buttons ok and cancel
        if (showCancelButton)
        {
            btnCancel.setVisibility(View.VISIBLE);
            final ShowDialogListener listener1 = (ShowDialogListener) listener;
            btnOk.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                if (listener1 != null)
                    listener1.onOkClicked();
                if (dialog != null)
                    dialog.dismiss();
                }
            });


            btnCancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                if (listener1 != null)
                    listener1.onCancelClicked();
                if (dialog != null)
                    dialog.dismiss();
                }
            });

        }
        else  // setting ShowSimpleDialogListener for only ok button in dialog
        {
            btnCancel.setVisibility(View.GONE);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowSimpleDialogListener listener1 = (ShowSimpleDialogListener) listener;
                    if (listener1 != null)
                        listener1.onOkClicked();
                  /*  else
                        Log.i(TAG,"listener1 is null");*/
                    if (dialog != null)
                        dialog.dismiss();
                }
            });
        }





        //  showCustomDialog(context,view);



    }






    public static void showSimpleCustomDialog(Context context, String title)
    {
        showDialog(context,false,title,null, null, null, null);
    }


    public static void showSimpleCustomDialog(Context context, String title, final ShowSimpleDialogListener listener)
    {
      showDialog(context,false,title, null, null, null, listener);
    }



    public static void showSimpleCustomDialog(Context context, String title, String message)
    {
        showDialog(context,false,title,message, null, null, null);
    }



    public static void showSimpleCustomDialog(Context context, String title, String message, final ShowSimpleDialogListener listener)
    {
        showDialog(context,false,title,message, null, null, listener);
    }






    public static void showCustomDialog(Context context, String title)
    {
        showDialog(context,true,title,null, null, null, null);
    }


    public static void showCustomDialog(Context context, String title, final ShowDialogListener listener)
    {
        showDialog(context,true,title, null, null, null, listener);
    }



    public static void showCustomDialog(Context context, String title, String message)
    {
        showDialog(context,true,title,message, null, null, null);
    }



    public static void showCustomDialog(Context context, String title, String message, final ShowDialogListener listener)
    {
        showDialog(context,true,title,message, null, null, listener);
    }









    // below method extra parameters to change text of ok and cancel button

    // in showSimpleCustomDialog() method i am receiving String passNull to make sure cancel button is not being shown
    public static void showSimpleCustomDialog(Context context, String title, String btnOkText, String passNull)
    {
        showDialog(context,false,title,null, btnOkText, null, null);
    }


    public static void showSimpleCustomDialog(Context context, String title, String btnOkText, String passNull, final ShowSimpleDialogListener listener)
    {
        showDialog(context,false,title, null, btnOkText, null, listener);
    }



    public static void showSimpleCustomDialog(Context context, String title, String message, String btnOkText, String passNull)
    {
        showDialog(context,false,title,message, btnOkText, null, null);
    }



    public static void showSimpleCustomDialog(Context context, String title, String message, String btnOkText, String passNull, final ShowSimpleDialogListener listener)
    {
        showDialog(context,false,title,message, btnOkText, null, listener);
    }






    public static void showCustomDialog(Context context, String title, String btnOkText, String btnCancelText)
    {
        showDialog(context,true,title,null, btnOkText, btnCancelText, null);
    }


    public static void showCustomDialog(Context context, String title, String btnOkText, String btnCancelText, final ShowDialogListener listener)
    {
        showDialog(context,true,title, null, btnOkText, btnCancelText, listener);
    }



    public static void showCustomDialog(Context context, String title, String btnOkText, String btnCancelText, String message)
    {
        showDialog(context,true,title,message, btnOkText, btnCancelText, null);
    }



    public static void showCustomDialog(Context context, String title, String message, String btnOkText, String btnCancelText, final ShowDialogListener listener)
    {
        showDialog(context,true,title,message, btnOkText, btnCancelText, listener);
    }



   /* public static void showLoader(String title, FragmentManager fragmentManager)
    {
        if (title == null || title.isEmpty())
            title = "Wait..";

        if (mLoader == null)
        {
            mLoader = DialogLoader.getInstance(title);
            mLoader.show(fragmentManager, ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        }
        else if (mLoader.isAdded() && mLoader.isVisible())
        {
            mLoader = (DialogLoader) fragmentManager.findFragmentByTag(ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            if (mLoader != null)
                mLoader.changeProgressTitle(title);
        }
        else
        {
            mLoader = null;
            mLoader = DialogLoader.getInstance(title);
            mLoader.show(fragmentManager, ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
        }

    }

    public static void hideLoader()
    {
        if (mLoader != null && mLoader.isAdded())
            mLoader.dismiss();
    }

    public static void hideLoaderRefreshLayout(SwipeRefreshLayout refreshLayout)
    {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }
*/

}
