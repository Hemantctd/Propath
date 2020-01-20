package org.ctdworld.propath.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.BottomSheetOption;

import java.util.ArrayList;


public class FragmentBottomSheetOptions extends BottomSheetDialogFragment implements  AdapterBottomSheetOptions.OnOptionsSelectedListener
{
   private static final String TAG = FragmentBottomSheetOptions.class.getSimpleName();
   private ArrayList<BottomSheetOption> mListOptions;
   private OnOptionSelectedListener mListener;

    public FragmentBottomSheetOptions() {
        // Required empty public constructor
    }

    public interface OnOptionSelectedListener{void onOptionSelected(int option);}

    public static FragmentBottomSheetOptions getInstance(ArrayList<BottomSheetOption> optionList)
    {
       // Log.i(TAG,"bottom sheet options size, in getInstance  = "+optionList.size());

        FragmentBottomSheetOptions fragmentBottomSheetOptions = new FragmentBottomSheetOptions();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ConstHelper.Key.OPTIONS_LIST, optionList);
        fragmentBottomSheetOptions.setArguments(bundle);
        return fragmentBottomSheetOptions;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mListOptions = bundle.getParcelableArrayList(ConstHelper.Key.OPTIONS_LIST);
           // Log.i(TAG,"bottom sheet options size = "+mListOptions.size());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_options, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
       // Log.i(TAG,"bottom sheet options size, in onViewCreated  = "+mListOptions.size());

        AdapterBottomSheetOptions adapterBottomSheetOptions  = new AdapterBottomSheetOptions(getContext(),mListOptions, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.fragment_bottom_sheet_options_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterBottomSheetOptions);
    }

    @Override
    public void onOptionSelected(int option)
    {
        if(mListener != null)
        {
            mListener.onOptionSelected(option);
            dismiss();
        }
        else
            Log.e(TAG,"mListener is null in onOptionSelected");
    }



    public void setOnBottomSheetOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener)
    {
        this.mListener = onOptionSelectedListener;
    }

}
