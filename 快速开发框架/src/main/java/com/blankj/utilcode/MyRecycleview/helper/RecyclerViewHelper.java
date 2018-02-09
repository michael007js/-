package com.blankj.utilcode.MyRecycleview.helper;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * 作者：yuanYe创建于2016/10/28
 * QQ：962851730
 * 用于给RecyclerView添加header和footer
 */

public class RecyclerViewHelper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    private RecyclerView.Adapter adapter;

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
//    private static final int BASE_PULL_TO_LOADING = 300000;

//    private int pullToLoading = 0;


    public RecyclerViewHelper(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    private boolean isHeader(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooter(int position) {
        return position >= (getItemCount() - getFooterCount());
    }

    /* //如果是上拉加载
     private boolean (int position) {
         //如果是最后一条
         return (position == getItemCount() - 1) && pullToLoading == 1;
     }*/
    int headWidth;

    /**
     * 自定义头部宽度(在addHeader之前调用)
     *
     * @param headWidth
     */
    public void setHeadWidth(int headWidth) {
        this.headWidth = headWidth;
    }

    public void addHeader(View header) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, header);
        header.setLayoutParams(new LinearLayout.LayoutParams(headWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    int footerWidth;

    /**
     * 自定义脚部宽度(在addFooter之前调用)
     *
     * @param footerWidth
     */
    public void setFooterWidth(int footerWidth) {
        this.footerWidth = footerWidth;
    }

    public void addFooter(View footer) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, footer);
        footer.setLayoutParams(new LinearLayout.LayoutParams(footerWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

//    //显示刷新
//    public void showPullToLoading() {
//        pullToLoading = 1;
//        notifyItemInserted(getHeaderCount() + getFooterCount() + getRealItemCount() + pullToLoading);
//    }
//
//    //关闭刷新
//    public void closePullToLoading() {
//        pullToLoading = 0;
//        notifyItemRemoved(getHeaderCount() + getFooterCount() + getRealItemCount() + pullToLoading);
//    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return mHeaderViews.keyAt(position); //查看指定位置的键
        } else if (isFooter(position)) {
            return mFooterViews.keyAt(position - getHeaderCount() - getRealItemCount());
        } else /*if (isPullToLoading(position)) {
            return BASE_PULL_TO_LOADING;
        }*/
            return adapter.getItemViewType(position - getHeaderCount());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) { //说明是头布局
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;
        } else if (mFooterViews.get(viewType) != null) { //说明是脚布局
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFooterViews.get(viewType));
            return holder;
        }/* else if (viewType == BASE_PULL_TO_LOADING) {
            //下拉刷新样式加载(暂时不用,下方影藏GONE)
            ViewHolder holder;
            holder = ViewHolder.createViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.base_pull_to_loading, parent, false));
            return holder;
        }*/
        return adapter.onCreateViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isFooter(position)) {
            return;
        }
        if (isHeader(position)) {
            return;
        }
       /* if (isPullToLoading(position)) {
            if (pullToLoading == 0) {
                ((ViewHolder) holder).getmConvertView().setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).getmConvertView().setVisibility(View.GONE);
            }
            return;
        }*/
        adapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        //返回数据的总数
        return getHeaderCount() + getFooterCount() + getRealItemCount() /*+ pullToLoading*/;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        adapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    /**
     * 得到头和脚的总个数
     */
    private int getHeaderCount() {
        return mHeaderViews.size();
    }

    private int getFooterCount() {
        return mFooterViews.size();
    }

    private int getRealItemCount() {
        return adapter.getItemCount();
    }


}
