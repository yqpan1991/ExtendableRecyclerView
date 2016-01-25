package com.support.android.designlibdemo;

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

import com.dewmobile.kuaiya.view.recyclerview.DmRecyclerViewWrapper;
import com.dewmobile.kuaiya.view.recyclerview.decoration.DividerItemDecoration;
import com.support.android.designlibdemo.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class CheeseList4Fragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private DmRecyclerViewWrapper mDrvwContent;
    private TestAdapter mAdapter;
    private boolean isFirstTime = true;
    private boolean isSecondTime = false;
    private View mHeadView1;

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
        mDrvwContent.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mDrvwContent.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mDrvwContent.setOnLoadMoreListener(new DmRecyclerViewWrapper.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> data = mAdapter.getDataList();
                        int lastCount = data.size();
                        for (int i = 0; i <= 3; i++) {
                            data.add(data.size() + 1 + "");
                        }
//                        mAdapter.notifyDataSetChanged();
                        mAdapter.notifyAdapterItemRangeInserted(lastCount,0);
                    }
                }, 2000);
            }
        });

        mDrvwContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(isFirstTime){
                    isFirstTime = false;
                    isSecondTime = true;
                    mAdapter.removeHeaderView(mHeadView1, true);
                }else if(isSecondTime){
                    isSecondTime = false;
                    mAdapter.addHeaderView(mHeadView1, true);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mDrvwContent.setRefreshing(false);
                        List<String> data = mAdapter.getDataList();
                        int index = data.size();
                        for (int i = 0; i <= 3; i++) {
                            data.add(0, data.size() + 1 + "");
                        }
                        mAdapter.notifyAdapterItemRangeInserted(0,3);
//                        mAdapter.notifyDataSetChanged();
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
        mAdapter = new TestAdapter(this.getActivity());
        mAdapter.addHeaderView(new SampleHeader(this.getActivity()));
        mHeadView1 = new SampleHeader1(this.getActivity());
        mAdapter.addHeaderView(mHeadView1);
        mAdapter.addHeaderView(new SampleHeader2(this.getActivity()));
        List<String> dataList = new ArrayList<>();
        for(int i = 0;i<30;i++){
            dataList.add(i+"");
        }
        mAdapter.setDataList(dataList);
    }

}
