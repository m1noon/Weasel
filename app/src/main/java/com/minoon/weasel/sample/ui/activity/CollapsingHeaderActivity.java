package com.minoon.weasel.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.minoon.weasel.layout.CollapsingHeaderView;
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
    CollapsingHeaderView mCollapsingHeaderView;
    @Bind(R.id.activity_collapsing_header_tb_toolbar)
    Toolbar mToolbar;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollapsingHeaderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_collapsing_header_layout);
        ButterKnife.bind(this);

        mCollapsingHeaderView.attachHeaderView(HeaderFragment.newInstance(), getFragmentManager());
        mCollapsingHeaderView.attachDragView(SampleFragment.newInstance(), getFragmentManager());
        mCollapsingHeaderView.setDragListener(new CollapsingHeaderView.DragListener() {
            boolean isHide = false;

            @Override
            public void onDragged(CollapsingHeaderView view, int y, int dy, float progress) {
                Log.d(TAG, String.format("onDragged. y='%s', dy='%s', progress='%s'", y, dy, progress));

                if (progress == 1) {
                    Log.d(TAG, "progress == 1");
                    if (dy > 0) {
                        ((TransitionDrawable)mToolbar.getBackground()).startTransition(300);
                        isHide = true;
                    }
                } else if (isHide) {
                    isHide = false;
                    ((TransitionDrawable)mToolbar.getBackground()).reverseTransition(300);
                }

            }
        });
        setSupportActionBar(mToolbar);

        TransitionDrawable drawable = new TransitionDrawable(new Drawable[] {
                new ColorDrawable(Color.TRANSPARENT),
                new ColorDrawable(Color.WHITE)
        });
        mToolbar.setBackground(drawable);
    }
}
