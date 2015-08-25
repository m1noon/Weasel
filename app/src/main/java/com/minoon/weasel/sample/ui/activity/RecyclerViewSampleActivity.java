package com.minoon.weasel.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.minoon.weasel.Event;
import com.minoon.weasel.State;
import com.minoon.weasel.Weasel;
import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.adapter.SampleFragmentAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by a13587 on 15/07/10.
 */
public class RecyclerViewSampleActivity extends AppCompatActivity {
    private static final String TAG = RecyclerViewSampleActivity.class.getSimpleName();

    @Bind(R.id.activity_recycler_view_sample_rv_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.tool_bar)
    Toolbar mToolbar;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecyclerViewSampleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_recycler_view_sample);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SampleFragmentAdapter());

        // sample that specify the scroll range.
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        Weasel.chase(mRecyclerView)
                .at(Event.START_SCROLL_BACK, new State(), 400)
                .at(Event.START_SCROLL_FORWARD, new State().translateY(-300), 400)
                .start(mToolbar);
    }
}
