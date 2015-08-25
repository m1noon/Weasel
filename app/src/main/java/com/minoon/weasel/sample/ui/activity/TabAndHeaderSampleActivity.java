package com.minoon.weasel.sample.ui.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.minoon.weasel.Event;
import com.minoon.weasel.State;
import com.minoon.weasel.Weasel;
import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.ScrollChaser;
import com.minoon.weasel.sample.ui.fragment.ViewPagerTabFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A sample
 *
 */
public class TabAndHeaderSampleActivity extends AppCompatActivity implements ScrollChaser {

    private static final String TAG = TabAndHeaderSampleActivity.class.getSimpleName();

    @Bind(R.id.a_tab_and_header_sample_tb_toolbar)
    Toolbar mToolbar;

    Weasel mHeaderWeasel;
    Weasel mScrollViewWeasel;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TabAndHeaderSampleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_tab_and_header_sample);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.a_tab_and_header_sample_fl_container, ViewPagerTabFragment.newInstance())
                    .commit();
        }

        setSupportActionBar(mToolbar);
    }

    /** {@link ScrollChaser} */

    @Override
    public void chaseStart(RecyclerView recyclerView) {
        Log.d(TAG, "chase. recyclerView=" + recyclerView + ", weasel.size=" + mHeaderWeasel);
        final int headerHeight = getResources().getDimensionPixelSize(R.dimen.actionbar_height);
        // caution!
        recyclerView.setTranslationY(headerHeight + mToolbar.getTranslationY());

        if (mHeaderWeasel != null) {
            mHeaderWeasel.addChaseView(recyclerView);
            mScrollViewWeasel.addChaseView(recyclerView);
            mScrollViewWeasel.addChaserView(recyclerView);
            return;
        }

        final int duration = 300;

        // set up header weasel
        mHeaderWeasel = Weasel.chase(recyclerView)
                .at(Event.START_SCROLL_UP, new State(), duration)
                .at(Event.START_SCROLL_DOWN, new State().translateY(-headerHeight), duration)
                .start(mToolbar);
        Fragment f = getFragmentManager().findFragmentById(R.id.a_tab_and_header_sample_fl_container);
        if (f instanceof ViewPagerTabFragment) {
            View tab = ((ViewPagerTabFragment)f).getTab();
            if (tab != null) {
                mHeaderWeasel.addChaserView(tab);
            }
        }

        // set up scroll view weasel
        mScrollViewWeasel = Weasel.chase(recyclerView)
                .at(Event.START_SCROLL_UP, new State().translateY(headerHeight), duration)
                .at(Event.START_SCROLL_DOWN, new State(), duration)
                .start(recyclerView);
    }

    @Override
    public void chaseEnd(RecyclerView recyclerView) {
        if (mHeaderWeasel != null) {
            mHeaderWeasel.removeChaserView(recyclerView);
        }
        if (mScrollViewWeasel != null) {
            mScrollViewWeasel.removeChaserView(recyclerView);
        }
    }
}
