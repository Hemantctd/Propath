package org.ctdworld.propath.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMatchPastStatistics;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMatchPastStatistics extends Fragment {

        Context mContext;
        RecyclerView mRecyclerView;

    public FragmentMatchPastStatistics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_match_past_statistics, container, false);
        init(view);
        prepareRecyclerView();
        return view;
    }

    private void init(View view)
    {
        mRecyclerView = view.findViewById(R.id.recycler_view_match_past_statistics);
    }

    public void prepareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchPastStatistics adapterMatchPastStatistics = new AdapterMatchPastStatistics(getContext());
        mRecyclerView.setAdapter(adapterMatchPastStatistics);
    }

}
