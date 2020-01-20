package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityMatchDayEventEdit;
import org.ctdworld.propath.activity.ActivityMatchDayShareEvent;
import org.ctdworld.propath.activity.ActivityMatchDayTeamShare;
import org.ctdworld.propath.activity.ActivitySurvey;
import org.ctdworld.propath.adapter.AdapterMatchDayPastList;
import org.ctdworld.propath.adapter.AdapterMatchReceivedStatistics;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMatchDayPast extends Fragment implements AdapterMatchDayPastList.OnOptionsMenuClickedListener{

    Context mContext;
    RecyclerView mRecyclerView;
    public FragmentMatchDayPast() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment_match_day_past, container, false);
        init(view);
        prepareRecyclerView();
        return view;

    }

    private void init(View view)
    {
        mContext = getContext();
        mRecyclerView = view.findViewById(R.id.recycler_view_received_statistics);

    }

    public void prepareRecyclerView()
    {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchDayPastList adapter = new AdapterMatchDayPastList(getActivity(),this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAdapterOptionsMenuClicked() {
        ArrayList<BottomSheetOption> bottomSheetOptions  = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_SHARE,"Share")
                .addOption(BottomSheetOption.OPTION_EDIT, "Edit")
                .addOption(BottomSheetOption.OPTION_COPY, "Copy")
                .addOption(BottomSheetOption.OPTION_DELETE, "Delete").build();


        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
        {

            @Override
            public void onOptionSelected(int option)
            {
                switch (option)
                {
                    case BottomSheetOption.OPTION_SHARE:
                        startActivity(new Intent(mContext, ActivityMatchDayShareEvent.class));

                        break;



                    case BottomSheetOption.OPTION_EDIT:

                        startActivity(new Intent(mContext, ActivityMatchDayEventEdit.class));

                        break;


                    case BottomSheetOption.OPTION_COPY:
                        DialogEditText dialogEditText = DialogEditText.getInstance("Copy Event", "Enter Event Name", "Save", false);
                        dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                            @Override
                            public void onButtonClicked(String enteredValue) {

                                if (enteredValue.equals(""))
                                {
                                    DialogHelper.showSimpleCustomDialog(mContext,"Plz fill the title");
                                }
                                else
                                {

                                }

                            }
                        });
                        if (getFragmentManager() != null) {
                            dialogEditText.show(getFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                        }
                        break;

                    case BottomSheetOption.OPTION_DELETE:
                        DialogHelper.showCustomDialog(mContext, "Are you sure want to delete this event ?",
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

   /* private AdapterMatchDayPastList.Listener adapterListener = new AdapterMatchDayPastList.Listener() {
        @Override
        public void onCoachSelfReviewOptionClicked(final String reviewId, final String athleteId)
        {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_DELETE, "Delete")
                    .addOption(BottomSheetOption.OPTION_EDIT, "Edit");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {

                    switch (option)
                    {
                        case BottomSheetOption.OPTION_DELETE:
                            DialogHelper.showSimpleCustomDialog(mContext, "Are you sure want to delete this ?", new DialogHelper.ShowSimpleDialogListener() {
                                @Override
                                public void onOkClicked() {
                                }
                            });
                            break;

                        case BottomSheetOption.OPTION_EDIT:

                            break;
                    }

                }
            });
            options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
        }
    };*/
}
