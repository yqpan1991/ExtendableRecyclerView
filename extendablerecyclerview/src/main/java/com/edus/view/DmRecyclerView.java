package com.edus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by yqpan on 2015/12/17.
 */
public class DmRecyclerView extends RecyclerView {

    private final String TAG = DmRecyclerView.this.getClass().getSimpleName();

    public DmRecyclerView(Context context) {
        this(context, null);
    }

    public DmRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DmRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        Log.e(TAG, "CustomRecyclerView init");
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
//        Log.e(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.e(TAG, "onLayout");
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
//        Log.e(TAG, "onDraw");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        Log.e(TAG,"onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        Log.e(TAG, "onDetachedFromWindow");
    }
}
