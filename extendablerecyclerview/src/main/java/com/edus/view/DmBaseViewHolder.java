package com.edus.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yqpan on 2016/1/13.
 */
public class DmBaseViewHolder<T> extends RecyclerView.ViewHolder {

    public DmBaseViewHolder(View itemView) {
        super(itemView);
    }

    public void updateData(T t, int position){

    }
}
