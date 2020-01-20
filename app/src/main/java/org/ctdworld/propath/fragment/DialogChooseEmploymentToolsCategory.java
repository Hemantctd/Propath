package org.ctdworld.propath.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.ctdworld.propath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogChooseEmploymentToolsCategory extends DialogFragment/* implements View.OnClickListener*/
{


    private static final String TAG = DialogChooseEmploymentToolsCategory.class.getSimpleName();

    View mView; // main layout
  /*  Button mBtnEnter;*/


/*

    private static final String KEY_VALUE_TYPE = "create type";
    private static final String KEY_BUTTON_TEXT = "button text";
    public static final int TYPE_CREATE_CATEGORY = 1;
    public static final int TYPE_CREATE_NOTE = 2;

    private int VALUE_TYPE = 0; // it will contain edittext type, eg. category, note
    private String VALUE_BUTTON_TEXT = "Enter";  // it will store button text

*/



    // these strings have been initialized in onCreate() method
    private String mStrButtonTitle1, mStrButtonTitle2;
    private OnCategorySelectedListener mListener;

 /*   // to get instance and set arguments for button title
    public static DialogChooseEmploymentToolsCategory getInstance(int createType, String buttonText)
    {
        DialogChooseEmploymentToolsCategory filesNotesCreate = new DialogChooseEmploymentToolsCategory();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_VALUE_TYPE,createType);
        bundle.putString(KEY_BUTTON_TEXT,buttonText);

        filesNotesCreate.setArguments(bundle);

        return filesNotesCreate;
    }
*/
    public DialogChooseEmploymentToolsCategory() { }  // constructor



    public interface OnCategorySelectedListener   // listener
    {
        void onCategorySelected(int selectedCategory);
    }



   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            Bundle bundle = getArguments();
            try
            {
                VALUE_TYPE = bundle.getInt(KEY_VALUE_TYPE);
                VALUE_BUTTON_TEXT = bundle.getString(KEY_BUTTON_TEXT);
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error in onCreate() method , "+e.getMessage());
                e.printStackTrace();
            }
        }
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.i(TAG,"DialogChooseEmploymentToolsCategory fragment appeared ");
        View view = inflater.inflate(R.layout.dialog_choose_employment_tools_category, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);


        mView = view;



        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_in_from_left);
        view.setAnimation(anim);


        // setting onClickListener on views
        view.findViewById(R.id.dialog_choose_employment_tools_category_txt_application_letter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onCategorySelected(FragmentCareerEmploymentTools.CATEGORY_APPLICATION_LETTER);

                dismissDialog();
            }
        });

        view.findViewById(R.id.dialog_choose_employment_tools_category_txt_curriculum_vitae).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onCategorySelected(FragmentCareerEmploymentTools.CATEGORY_CURRICULUM_VITAE);

                dismissDialog();
            }
        });



        view.findViewById(R.id.dialog_choose_employment_tools_category_txt_interview_checklist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onCategorySelected(FragmentCareerEmploymentTools.CATEGORY_INTERVIEW_CHECKLIST);

                dismissDialog();
            }
        });


        view.findViewById(R.id.dialog_choose_employment_tools_category_txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onCategorySelected(FragmentCareerEmploymentTools.CATEGORY_NOTHING);
                dismissDialog();
            }
        });

        return view;
    }


    // setting text on views according to type
   /* private void setTextOnFieldsByType()
    {
        if (VALUE_TYPE == TYPE_CREATE_CATEGORY)
        {
            mTxtTitle.setText("Create New Category");
            mEditText.setHint("Enter Category Name");
            mBtnEnter.setText(VALUE_BUTTON_TEXT);
        }


        if (VALUE_TYPE == TYPE_CREATE_NOTE)
        {
            mTxtTitle.setText("Create New Note");
            mEditText.setHint("Enter Note Name");
            mBtnEnter.setText(VALUE_BUTTON_TEXT);
        }


    }*/





    public void setCategorySelectedListener(OnCategorySelectedListener listener)
    {
        this.mListener = listener;
    }


    // to dismiss dialog, setting animation on main view to dismiss dialog with animation
    private void dismissDialog()
    {

        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_out_dialog);
        // using startAnimation method, setAnimation() method is not working here
        mView.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Log.i(TAG,"animation started");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              //  Log.i(TAG,"animation ended");

                // using postDelayed() method to make sure dialog is dismissed after it stops, if we don't use postDelayed() method then there is error
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Dialog dialog = getDialog();
                        if (dialog != null)
                            dialog.dismiss();
                    }
                },10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }

}
