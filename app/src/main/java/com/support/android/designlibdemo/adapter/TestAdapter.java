package com.support.android.designlibdemo.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edus.view.DmBaseAdapter;
import com.edus.view.DmBaseViewHolder;
import com.support.android.designlibdemo.R;

/**
 { * Created by yqpan on 2016/1/13.
 */
public class TestAdapter extends DmBaseAdapter<String> {

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    public DmBaseViewHolder<String> onCreateAdapterViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(mInflater.inflate(R.layout.test_item,parent,false));
    }

    @Override
    public void onBindAdapterViewHolder(DmBaseViewHolder<String> holder, int position) {
        String adapterDataItem = getAdapterDataItem(position);
        holder.updateData(adapterDataItem, position);
    }

    public static class CustomViewHolder extends DmBaseViewHolder<String>{
        TextView tvDemo;
        public CustomViewHolder(View itemView) {
            super(itemView);
            tvDemo = (TextView) itemView.findViewById(R.id.tv_demo);
        }

        @Override
        public void updateData(String s, int position) {
            super.updateData(s, position);
            tvDemo.setText(s);
        }
    }
}
