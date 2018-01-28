package com.support.android.designlibdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edus.view.DmBaseAdapter;
import com.edus.view.DmBaseViewHolder;
import com.edus.view.DmRecyclerViewWrapper;
import com.edus.view.decoration.DividerItemDecoration;
import com.edus.view.sticky.StickyRecyclerHeadersAdapter;
import com.edus.view.sticky.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqpan on 2016/1/12.
 */
public class StickHeadFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private DmRecyclerViewWrapper mDrvwContent;
    private StickAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cheese_2_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrvwContent = (DmRecyclerViewWrapper) view.findViewById(R.id.rv_content);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        initAdapterData();
        mDrvwContent.setAdapter(mAdapter);
        mDrvwContent.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        mDrvwContent.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mDrvwContent.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mDrvwContent.setOnLoadMoreListener(new DmRecyclerViewWrapper.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> data = mAdapter.getDataList();
                        for (int i = 0; i <= 3; i++) {
                            data.add(data.size() + 1 + "");
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

        mDrvwContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrvwContent.setRefreshing(false);
                        List<String> data = mAdapter.getDataList();
                        for (int i = 0; i <= 3; i++) {
                            data.add(0, data.size() + 1 + "");
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
        mDrvwContent.enableLoadMore(true);
        mDrvwContent.enableRefresh(true);
        mDrvwContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG,"onScrollStateChanged:"+newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled,dx:" + dx+",dy:"+dy);
            }
        });
    }

    private void initAdapterData() {
        mAdapter = new StickAdapter(this.getActivity());
        List<String> dataList = new ArrayList<>();
        for(int i = 0;i<30;i++){
            dataList.add(i+"");
        }
        mAdapter.setDataList(dataList);
    }

    public static class StickAdapter extends DmBaseAdapter<String> implements StickyRecyclerHeadersAdapter<DmBaseViewHolder>{

        public StickAdapter(Context context) {
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


        @Override
        public long getHeaderId(int position) {
            String adapterDataItem = getAdapterDataItem(position);
            if(adapterDataItem != null){
                return getAdapterDataItem(position).charAt(0);
            }else{
                return -1;
            }
        }

        @Override
        public DmBaseViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return new CustomViewHolder(mInflater.inflate(R.layout.test_head_item,parent,false));
        }

        @Override
        public void onBindHeaderViewHolder(DmBaseViewHolder holder, int position) {
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

}
