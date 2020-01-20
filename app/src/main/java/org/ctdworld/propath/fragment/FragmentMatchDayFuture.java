package org.ctdworld.propath.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityMatchDayEvent;
import org.ctdworld.propath.activity.ActivityMatchDayFuture6;
import org.ctdworld.propath.activity.ActivityMatchDayTeamShare;
import org.ctdworld.propath.adapter.AdapterMatchDayPastList;
import org.ctdworld.propath.adapter.AdapterMatchDayReceivedList;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMatchDayFuture extends Fragment implements AdapterMatchDayReceivedList.OnOptionsMenuClickedListener{

    Context mContext;
    RecyclerView mRecyclerView;
    FloatingActionButton mFloatingBtn;

    public FragmentMatchDayFuture() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment_match_day_future, container, false);
        init(view);
        prepareRecyclerView();
        return view;
    }

    private void init(View view)
    {
        mContext = getContext();
        mFloatingBtn = view.findViewById(R.id.float_icon);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_match_day_layout);
                Button btnTeam = dialog.findViewById(R.id.btn_team);
                Button btnEvent =  dialog.findViewById(R.id.btn_event);
                btnTeam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mContext, ActivityMatchDayFuture6.class));
                    }
                });

                btnEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mContext, ActivityMatchDayEvent.class));
                    }
                });
                dialog.show();

            }
        });
    }

    public void prepareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchDayReceivedList adapter = new AdapterMatchDayReceivedList(getActivity(),this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAdapterOptionsMenuClicked() {
        ArrayList<BottomSheetOption> bottomSheetOptions = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_SHARE, "Share")
                .addOption(BottomSheetOption.OPTION_EDIT, "Edit")
                .addOption(BottomSheetOption.OPTION_COPY, "Copy")
                .addOption(BottomSheetOption.OPTION_DELETE, "Delete").build();


        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {

            @Override
            public void onOptionSelected(int option) {
                switch (option) {
                    case BottomSheetOption.OPTION_SHARE:
                            startActivity(new Intent(mContext, ActivityMatchDayTeamShare.class));

                        break;


                    case BottomSheetOption.OPTION_EDIT:


                        break;


                    case BottomSheetOption.OPTION_COPY:
                        DialogEditText dialogEditText = DialogEditText.getInstance("Copy Team", "Enter Team Name", "Save", false);
                        dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                            @Override
                            public void onButtonClicked(String enteredValue) {

                                if (enteredValue.equals("")) {
                                    DialogHelper.showSimpleCustomDialog(mContext, "Plz fill the name");
                                } else {

                                }

                            }
                        });
                        if (getFragmentManager() != null) {
                            dialogEditText.show(getFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                        }
                        break;

                    case BottomSheetOption.OPTION_DELETE:
                        DialogHelper.showCustomDialog(mContext, "Are you sure want to delete this team ?",
                                new DialogHelper.ShowDialogListener() {
                                    @Override
                                    public void onOkClicked() {


                                    }

                                    @Override
                                    public void onCancelClicked() {

                                    }
                                });

                        break;
                }
            }
        });
        if (getFragmentManager() != null) {
            options.show(getFragmentManager(), "options");
        }
    }
}
