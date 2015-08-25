package com.minoon.weasel.sample.ui.fragment;

import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.adapter.SampleViewPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ViewPagerTabFragment extends Fragment {

    @Bind(R.id.f_view_pager_tab_vp_viewpager)
    ViewPager mViewPager;
    @Bind(R.id.f_view_pager_tab_psts_tab)
    PagerSlidingTabStrip mTabs;

    FragmentPagerAdapter mAdapter;


    public static ViewPagerTabFragment newInstance() {
        ViewPagerTabFragment fragment = new ViewPagerTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_view_pager_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mAdapter = new SampleViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabs.setViewPager(mViewPager);
    }

    public View getTab() {
        return mTabs;
    }
}
