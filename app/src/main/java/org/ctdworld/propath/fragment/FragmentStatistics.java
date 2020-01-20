package org.ctdworld.propath.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStatistics extends Fragment {

    Context mContext;
    public FragmentStatistics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        // Inflate the layout for this fragment
        mContext = getContext();

        init(view);
        return view;

    }

    private void init(View view)
    {

    }


}
