package com.minoon.weasel.sample.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.ScrollChaser;
import com.minoon.weasel.sample.ui.adapter.SampleFragmentAdapter;
import com.minoon.weasel.trader.LinearLayoutRecyclerViewTrader;
import com.minoon.weasel.trader.TouchEventTrader;

/**
 *
 */
public class SampleFragment extends Fragment {
    private static final String TAG = SampleFragment.class.getSimpleName();

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
        return inflater.inflate(R.layout.f_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_sample_rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new SampleFragmentAdapter());
        mTrader.setRecyclerView(mRecyclerView);

        Log.d(TAG, "onViewCreated.");
        Activity a = getActivity();
        if (a instanceof ScrollChaser) {
            ((ScrollChaser)a).chaseStart(mRecyclerView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Activity a = getActivity();
        if (a instanceof ScrollChaser) {
            ((ScrollChaser)a).chaseEnd(mRecyclerView);
        }
    }

    public TouchEventTrader getTouchEventTrader() {
        return mTrader;
    }
}
