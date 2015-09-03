package com.minoon.weasel.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.minoon.weasel.layout.CollapsingHeaderLayout;
import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.adapter.SampleFragmentAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by a13587 on 15/08/22.
 */
public class CollapsingHeaderLayoutActivity extends AppCompatActivity {
    private static final String TAG = CollapsingHeaderLayoutActivity.class.getSimpleName();

    @Bind(R.id.a_collapsing_header_layout_chl_container)
    CollapsingHeaderLayout mCollapsingHeaderView;
    @Bind(R.id.a_collapsing_header_layout_tb_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.a_collapsing_header_layout_rv_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.a_collapsing_header_layout_iv_header_image)
    ImageView mImageView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollapsingHeaderLayoutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_collapsing_header_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        TransitionDrawable drawable = new TransitionDrawable(new Drawable[] {
                new ColorDrawable(Color.TRANSPARENT),
                new ColorDrawable(Color.WHITE)
        });
        mToolbar.setBackground(drawable);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SampleFragmentAdapter());

        Glide.with(this)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVpUuW4jLKhi1d3iulS83BP1UPj5e_n3cc6_NLUi2_G-v_46fJ")
                .into(mImageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollapsingHeaderLayoutActivity.this, "hello Weasel!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
