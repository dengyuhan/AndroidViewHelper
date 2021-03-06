package com.dyhdyh.helper.recyclerview.loadmore.valyout;

import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.dyhdyh.helper.recyclerview.loadmore.LoadMoreAdapter;
import com.dyhdyh.helper.recyclerview.loadmore.LoadMoreDelegateAdapter;
import com.dyhdyh.helper.recyclerview.loadmore.LoadMoreHelper;
import com.dyhdyh.helper.recyclerview.loadmore.LoadMoreView;

/**
 * @author dengyuhan
 *         created 2018/7/18 15:37
 */
public class VLayoutLoadMoreHelper extends LoadMoreHelper {
    private LoadMoreDelegateAdapter.Delegate mDelegateAdapter;

    public VLayoutLoadMoreHelper(RecyclerView recyclerView) {
        super(recyclerView);
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof DelegateAdapter) {
            LoadMoreVLayoutAdapter loadMoreAdapter = new LoadMoreVLayoutAdapter(mLoadMoreView);
            mDelegateAdapter = loadMoreAdapter;
            ((DelegateAdapter) adapter).addAdapter(loadMoreAdapter);
        } else {
            mDelegateAdapter = new LoadMoreAdapter(adapter, mLoadMoreView);
        }

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setLoadMoreState(int state) {
        if (mDelegateAdapter != null) {
            mDelegateAdapter.setLoadMoreState(state);
        }
    }

    @Override
    public int getLoadMoreState() {
        if (mDelegateAdapter != null) {
            return mDelegateAdapter.getLoadMoreState();
        }
        return LoadMoreView.GONE;
    }
}
