package com.dewmobile.kuaiya.view.recyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dewmobile.dmrecyclerview.R;


/**
 * 1. implements RecyclerView basic function
 * 2. can load more and refresh
 * Created by yqpan on 2016/1/13.
 */
public class DmRecyclerViewWrapper extends FrameLayout {

    private SwipeRefreshLayout mSrlRefresh;
    private RecyclerView mRvRecycler;
    private LayoutInflater mInflater;

    private int mScrollbarStyle;

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    protected int[] mDefaultSwipeToDismissColors = null;

    private static final int SCROLLBARS_NONE = 0;
    private static final int SCROLLBARS_VERTICAL = 1;
    private static final int SCROLLBARS_HORIZONTAL = 2;

    private boolean mEnableLoadMore;
    private boolean mIsFirstSetLoadMore = true;
    private OnLoadMoreListener mOnLoadMoreListener;
    private RecyclerView.OnScrollListener mOnScrollListener;
    protected LAYOUT_MANAGER_TYPE layoutManagerType;

    private int lastVisibleItemPosition;
    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int mPreviousTotal = 0;
    private int mFirstVisibleItem;
    private boolean mIsLoadingMore = false;
    private RecyclerView.AdapterDataObserver mAdapterDataObserver;

    public DmRecyclerViewWrapper(Context context) {
        this(context, null);
    }

    public DmRecyclerViewWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DmRecyclerViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initViews();
    }

    private void initAttr(AttributeSet attrs) {
        if (attrs != null) {
            //1. mScrollbarStyle get it from xml
        }
    }

    private void initViews() {
        mInflater = LayoutInflater.from(getContext());
        View rootView = mInflater.inflate(R.layout.dm_recyclerview_wrapper_layout, this, true);
        mSrlRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);
        mRvRecycler = (RecyclerView) rootView.findViewById(R.id.rv_recycler);
        setScrollBars();
        enableLoadMore(mEnableLoadMore);
        enableRefresh(false);
    }

    private void setScrollBars() {
        mInflater = LayoutInflater.from(getContext());
        switch (mScrollbarStyle) {
            case SCROLLBARS_VERTICAL:
                mSrlRefresh.removeView(mRvRecycler);
                View verticalView = mInflater.inflate(R.layout.dm_recyclerview_vertical, mSrlRefresh, true);
                mRvRecycler = (RecyclerView) verticalView.findViewById(R.id.rv_recycler);
                break;
            case SCROLLBARS_HORIZONTAL:
                mSrlRefresh.removeView(mRvRecycler);
                View horizontalView = mInflater.inflate(R.layout.dm_recyclerview_horizontal, mSrlRefresh, true);
                mRvRecycler = (RecyclerView) horizontalView.findViewById(R.id.rv_recycler);
                break;
            default:
                break;
        }
    }


    //---------------------recyclerview wrapper----------------

    public void setAdapter(DmBaseAdapter adapter) {
        if(getAdapter() != null && mAdapterDataObserver != null){
            getAdapter().unregisterAdapterDataObserver(mAdapterDataObserver);
        }
        mRvRecycler.setAdapter(adapter);
        mAdapterDataObserver = newAdapterDataObserver();
        adapter.registerAdapterDataObserver(mAdapterDataObserver);
        mIsFirstSetLoadMore = true;
        enableLoadMore(mEnableLoadMore);
    }

    public DmBaseAdapter getAdapter(){
        return (DmBaseAdapter) mRvRecycler.getAdapter();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRvRecycler.setLayoutManager(layoutManager);
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRvRecycler.setHasFixedSize(hasFixedSize);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        mRvRecycler.addItemDecoration(itemDecoration);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator){
        mRvRecycler.setItemAnimator(itemAnimator);
    }

    //-------------load more-------
    public void enableLoadMore(boolean enable) {
        if (mEnableLoadMore != enable || mIsFirstSetLoadMore) {
            mEnableLoadMore = enable;
            mRvRecycler.removeOnScrollListener(mOnScrollListener);
            if(enable){
                mOnScrollListener = newOnScrollListener();
                mRvRecycler.addOnScrollListener(mOnScrollListener);
            }
            if(mRvRecycler.getAdapter() != null && mRvRecycler.getAdapter() instanceof DmBaseAdapter){
                DmBaseAdapter adapter = (DmBaseAdapter) (mRvRecycler.getAdapter());
                if(enable && adapter.getCustomLoadingView() == null){
                    adapter.setCustomLoadingView(mInflater.inflate(R.layout.dm_recyclerview_bottom_progressbar,this,false));
                }
                adapter.enableLoadingMore(enable);
            }
            mIsFirstSetLoadMore = false;
        }
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener){
        mRvRecycler.addOnScrollListener(listener);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener listener){
        mRvRecycler.removeOnScrollListener(listener);
    }

    private RecyclerView.OnScrollListener newOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            private int[] lastPositions;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManagerType == null) {
                    if (layoutManager instanceof GridLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
                    } else if (layoutManager instanceof LinearLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                    } else {
                        throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                    }
                }

                switch (layoutManagerType) {
                    case LINEAR:
                        mVisibleItemCount = layoutManager.getChildCount();
                        mTotalItemCount = layoutManager.getItemCount();
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        mFirstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        break;
                    case GRID:
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        mFirstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        break;
                    case STAGGERED_GRID:
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        if (lastPositions == null)
                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        lastVisibleItemPosition = findMax(lastPositions);

                        staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions);
                        mFirstVisibleItem = findMin(lastPositions);
                        break;
                }

                if (mIsLoadingMore) {
                    //todo: there are some bugs needs to be adjusted for admob adapter
                    if (mTotalItemCount > mPreviousTotal) {
                        mIsLoadingMore = false;
                        mPreviousTotal = mTotalItemCount;
                    }
                }

                if (!mIsLoadingMore && (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem) {
                    //todo: there are some bugs needs to be adjusted for admob adapter
                    mOnLoadMoreListener.loadMore(mRvRecycler.getAdapter().getItemCount(), lastVisibleItemPosition);
                    mIsLoadingMore = true;
                    mPreviousTotal = mTotalItemCount;
                }
            }
        };
    }

    private RecyclerView.AdapterDataObserver newAdapterDataObserver(){
        return new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateData();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                updateData();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                updateData();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateData();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                updateData();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                updateData();
            }

            private void updateData(){
                mIsLoadingMore = false;
            }
        };
    }

    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private int findMin(int[] lastPositions) {
        int min = Integer.MAX_VALUE;
        for (int value : lastPositions) {
            if (value != RecyclerView.NO_POSITION && value < min)
                min = value;
        }
        return min;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void loadMore(int itemsCount, final int maxLastVisiblePosition);
    }

    //------------refresh--------
    public void setDefaultSwipeToDismissColors(int... colors) {
        if (colors != null && colors.length > 0) {
            mSrlRefresh.setColorSchemeColors(mDefaultSwipeToDismissColors);
        } else if (mDefaultSwipeToDismissColors != null && mDefaultSwipeToDismissColors.length > 0) {
            mSrlRefresh.setColorSchemeColors(mDefaultSwipeToDismissColors);
        } else {
            //<color name="holo_blue_bright">#ff00ddff</color>
            //<color name="holo_green_light">#ff99cc00</color>
            //<color name="holo_orange_light">#ffffbb33</color>
            //<color name="holo_red_light">#ffff4444</color>
            mSrlRefresh.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

        }
    }


    public void enableRefresh(boolean enable) {
        if (enable != mSrlRefresh.isEnabled()) {
            mSrlRefresh.setEnabled(enable);
            if (!enable) mSrlRefresh.setRefreshing(false);
        }
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListener = listener;
        setDefaultSwipeToDismissColors(mDefaultSwipeToDismissColors);

        mSrlRefresh.setOnRefreshListener(listener);

    }

    public void setRefreshing(boolean refreshing) {
        if(mSrlRefresh != null){
            mSrlRefresh.setRefreshing(refreshing);
        }
    }

    public static enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }


}
