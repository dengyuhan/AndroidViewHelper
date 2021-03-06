package com.dyhdyh.helper.recyclerview.loadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.alibaba.android.vlayout.DelegateAdapter;

import java.util.Arrays;

/**
 * @author dengyuhan
 *         created 2018/7/3 16:47
 */
public class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

    public static final int DEFAULT_AUTOLOAD_COUNT = 1;

    private boolean mLoadMoreCompleted = true;
    private boolean mLoadMoreEnable = true;

    private int mEarlyCountForAutoLoad = DEFAULT_AUTOLOAD_COUNT;
    private OnLoadMoreListener mLoadMoreListener;

    private OnFindVisibleItemCallback mFindCallback;

    public void setEarlyCountForAutoLoad(int earlyCountForAutoLoad) {
        this.mEarlyCountForAutoLoad = earlyCountForAutoLoad;
    }

    public void setLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        /*if (mLoadMoreEnable){
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                callScrollLoadMore(recyclerView);
            }
        }*/
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mLoadMoreEnable) {
            callScrollLoadMore(recyclerView);
        }
    }

    private void callScrollLoadMore(RecyclerView recyclerView) {
        //hasMore: status of current page, means if there's more data, you have to maintain this status
        if (mLoadMoreListener != null && mLoadMoreCompleted) {
            int last = 0, total = 0;
            final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            //如果callback是空 就通过LayoutManager获取最后显示的索引
            if (mFindCallback == null) {
                last = findLastVisibleItemPosition(lm);
            } else {
                //不是空就通过callback获取最后显示的索引
                last = mFindCallback.findLastVisibleItemPosition(lm);
                //如果是-1 就用默认方式再查一遍
                if (last == -1) {
                    last = findLastVisibleItemPosition(lm);
                }
            }

            total = getItemCount(recyclerView.getAdapter());
            if (!onIntercept(recyclerView, last, total)) {
                //earlyCountForAutoLoad: help to trigger load more listener earlier
                //Log.d("-------------->", first + " " + last + " " + total);
                mLoadMoreCompleted = false;
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    protected int findLastVisibleItemPosition(RecyclerView.LayoutManager lm) {
        if (lm instanceof LinearLayoutManager) {
            //first = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
            return ((LinearLayoutManager) lm).findLastVisibleItemPosition();
        } else if (lm instanceof StaggeredGridLayoutManager) {
            //final int[] firstPositions = ((StaggeredGridLayoutManager) lm).findFirstCompletelyVisibleItemPositions(null);
            //Arrays.sort(firstPositions);
            //first = firstPositions[0];
            final int[] lastPositions = ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(null);
            Arrays.sort(lastPositions);
            return lastPositions[lastPositions.length - 1];
        } else {
            return 0;
        }
    }

    protected int getItemCount(RecyclerView.Adapter adapter) {
        if (adapter instanceof LoadMoreAdapter) {
            return ((LoadMoreAdapter) adapter).getInnerAdapter().getItemCount();
        } else if (adapter instanceof DelegateAdapter) {
            return adapter.getItemCount() - 1;
        } else {
            return adapter.getItemCount();
        }
    }

    protected boolean onIntercept(RecyclerView recyclerView, int last, int total) {
        return !(last > 0 && last >= total - mEarlyCountForAutoLoad);
    }

    public void setLoadMoreCompleted() {
        this.mLoadMoreCompleted = true;
    }

    public void setLoadMoreEnable(boolean enable) {
        this.mLoadMoreEnable = enable;
    }

    public void setOnFindVisibleItemCallback(OnFindVisibleItemCallback callback) {
        this.mFindCallback = callback;
    }
}
