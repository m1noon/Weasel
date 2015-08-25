package com.minoon.weasel.sample.ui.adapter;

import com.minoon.weasel.sample.ui.fragment.SampleFragment;

/**
 * Created by a13587 on 15/08/24.
 */
public class SampleViewPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {
    private static final String TAG = SampleViewPagerAdapter.class.getSimpleName();

    public SampleViewPagerAdapter(android.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.app.Fragment getItem(int i) {
        return SampleFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Page1";
            case 1:
                return "Page2";
            case 2:
                return "Page3";
            default:
                return "UNKNOWN";
        }
    }
}
