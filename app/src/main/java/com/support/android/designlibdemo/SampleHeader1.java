package com.support.android.designlibdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by cundong on 2015/10/9.
 * <p/>
 * RecyclerView的HeaderView，简单的展示一个TextView
 */
public class SampleHeader1 extends RelativeLayout {

    public SampleHeader1(Context context) {
        super(context);
        init(context);
    }

    public SampleHeader1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SampleHeader1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.sample_header1, this);
    }
}