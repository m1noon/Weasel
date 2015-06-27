package com.minoon.weasel.sample.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.minoon.weasel.Event;
import com.minoon.weasel.State;
import com.minoon.weasel.Weasel;
import com.minoon.weasel.drag.VerticalDraggableView;
import com.minoon.weasel.sample.R;
import com.minoon.weasel.sample.ui.fragment.SampleFragment;
import com.minoon.weasel.state.HideAtWindowTopState;
import com.minoon.weasel.transformer.Transformer;


public class VerticalDragViewActivity extends AppCompatActivity {

    VerticalDraggableView mDraggableView;
    ImageView mBackImage;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_drag);
        mDraggableView = (VerticalDraggableView) findViewById(R.id.activity_main_draggable);
        SampleFragment fragment = SampleFragment.newInstance();
        mDraggableView.attach(fragment, getFragmentManager());
        // if the draggable view has scrollable contents, you should set trader for tracking child view scroll.
        mDraggableView.setTouchEventTrader(fragment.getTouchEventTrader());

        // set up back recycler view
        mBackImage = (ImageView) findViewById(R.id.activity_main_iv_back_image);
        Glide.with(this)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVpUuW4jLKhi1d3iulS83BP1UPj5e_n3cc6_NLUi2_G-v_46fJ")
                .into(mBackImage);
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerticalDragViewActivity.this, "hello Weasel!!", Toast.LENGTH_SHORT).show();
            }
        });

        // sample that specify the state of beginning and end and scroll 'ratio' of the scroll to the scrollable view  .
        Weasel.chase(mDraggableView)
                .from(new State())
                .to(new HideAtWindowTopState(mBackImage).alpha(0.4f))
                .ratio(0.25f)
                // do not tracking scroll in first 500 px.
                .offset(500)
                // you can implements custom transform.
                .transform(new Transformer() {
                    @Override
                    public void transform(View view, float offset) {
                        view.setScaleX(1 - offset);
                        view.setScaleY(1 - offset);
                    }
                })
                // start chase scroll. your view transform automatically according to your settings.
                .start(mBackImage);

        // sample that specify the scroll range.
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        Weasel.chase(mDraggableView)
                .at(Event.START_SCROLL_UP, new State(), 400)
                .at(Event.START_SCROLL_DOWN, new State().translateY(-300), 400)
//                .at(Event.FLICK_SCROLL_UP, new State(), 400)
//                .at(Event.FLICK_SCROL_DOWN, new State().translateY(-300), 400)
                .start(mToolbar);
    }
}
