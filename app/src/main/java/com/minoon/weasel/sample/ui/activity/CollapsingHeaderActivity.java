package com.minoon.weasel.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.minoon.weasel.layout.CollapsingHeaderLayout;
import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.fragment.HeaderFragment;
import com.minoon.weasel.sample.ui.fragment.SampleFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by a13587 on 15/08/22.
 */
public class CollapsingHeaderActivity extends AppCompatActivity {
    private static final String TAG = CollapsingHeaderActivity.class.getSimpleName();

    @Bind(R.id.activity_collapsing_header_chl_container)
    CollapsingHeaderLayout mCollapsingHeaderLayout;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollapsingHeaderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_header_layout);
        ButterKnife.bind(this);

        mCollapsingHeaderLayout.attachHeaderView(HeaderFragment.newInstance(), getFragmentManager());
        mCollapsingHeaderLayout.attachDragView(SampleFragment.newInstance(), getFragmentManager());
        mCollapsingHeaderLayout.setDragListener(new CollapsingHeaderLayout.DragListener() {
            @Override
            public void onDragged(View dragView, View headerView, int y, int dy) {
                Log.d(TAG, String.format("onDragged. y='%s', dy='%s'" , y, dy));
            }
        });
    }
}
