package com.minoon.weasel.sample.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.adapter.SampleFragmentAdapter;
import com.minoon.weasel.trader.LinearLayoutRecyclerViewTrader;
import com.minoon.weasel.trader.TouchEventTrader;

/**
 *
 */
public class SampleFragment extends Fragment {

    RecyclerView mRecyclerView;

    LinearLayoutRecyclerViewTrader mTrader;

    public static SampleFragment newInstance() {
        SampleFragment fragment = new SampleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SampleFragment() {
        // Required empty public constructor
        mTrader = new LinearLayoutRecyclerViewTrader(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_sample_rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new SampleFragmentAdapter());
        mTrader.setRecyclerView(mRecyclerView);
    }

    public TouchEventTrader getTouchEventTrader() {
        return mTrader;
    }
}
