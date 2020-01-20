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
import org.ctdworld.propath.adapter.AdapterMatchReceivedStatistics;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMatchVideoAnalysis extends Fragment {

    Context mContext;
    RecyclerView mRecyclerView;

    public FragmentMatchVideoAnalysis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_match_video_analysis, container, false);
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

        ArrayList<String> arrayList =  new ArrayList<>();
        arrayList.add("Team List");
        arrayList.add("Team Statistics");
        arrayList.add("Statistics Sheet");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchReceivedStatistics adapterMatchReceivedStatistics = new AdapterMatchReceivedStatistics(mContext, arrayList);
        mRecyclerView.setAdapter(adapterMatchReceivedStatistics);
    }

}
