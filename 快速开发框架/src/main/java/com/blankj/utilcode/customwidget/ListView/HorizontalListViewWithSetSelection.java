/**
 *
 */
package com.blankj.utilcode.customwidget.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.blankj.utilcode.util.LogUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author hxh
 *         <p>
 *         2016-7-22上午10:04:42
 */
@SuppressLint("WrongCall")
public class HorizontalListViewWithSetSelection extends AdapterView<ListAdapter> {

    public boolean mAlwaysOverrideTouch = true;

    protected ListAdapter mAdapter;

    private int mLeftViewIndex = -1;

    private int mRightViewIndex = 0;

    public int mCurrentX;

    protected int mNextX;

    private int mMaxX = Integer.MAX_VALUE;

    private int mDisplayOffset = 0;

    protected Scroller mScroller;

    private GestureDetector mGesture;

    private Queue<View> mRemovedViewQueue = new LinkedList<View>();

    private OnItemSelectedListener mOnItemSelected;

    private OnItemClickListener mOnItemClicked;

    private OnItemLongClickListener mOnItemLongClicked;

    private boolean mDataChanged = false;

    private OnScrollCallBack mOnScrollCallBack;

    public void setOnScrollCallBack(OnScrollCallBack OnScrollCallBack) {
        this.mOnScrollCallBack = OnScrollCallBack;
    }

    public HorizontalListViewWithSetSelection(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private synchronized void initView() {
        mLeftViewIndex = -1;
        mRightViewIndex = 0;
        mDisplayOffset = 0;
        mCurrentX = 0;
        mNextX = 0;
        mMaxX = Integer.MAX_VALUE;
        mScroller = new Scroller(getContext());
        mGesture = new GestureDetector(getContext(), mOnGesture);


    }

    @Override
    public void setOnItemSelectedListener(
            OnItemSelectedListener listener) {
        mOnItemSelected = listener;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClicked = listener;
    }

    @Override
    public void setOnItemLongClickListener(
            OnItemLongClickListener listener) {
        mOnItemLongClicked = listener;
    }

    private DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            synchronized (HorizontalListViewWithSetSelection.this) {
                mDataChanged = true;
            }
            invalidate();
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            reset();
            invalidate();
            requestLayout();
        }

    };

    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public View getSelectedView() {
        // TODO: implement
        return null;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataObserver);
        reset();
    }

    private synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    @Override
    public void setSelection(int position) {
        if (position >= 0) {
            if (null != mAdapter) {
                View item = mAdapter.getView(0, null, null);
                int width = getSize(item, 0);
                mNextX = width * position;
                invalidate();
            }
        }
    }

    public void setSelection(int position, boolean fromUser) {
        if (position >= 0) {
            if (null != mAdapter) {
                View item = mAdapter.getView(0, null, null);
                int width = getSize(item, 0);
                mNextX = width * position;
                if (fromUser) {
                    onLayout(true, 0, 0, right, bottom);
                } else {
                    invalidate();
                }
            }
        }
    }

    /**
     * 获取自定义组件尺�?
     *
     * @param view
     * @param flag 0:width 1:height
     * @return
     */
    public static int getSize(View view, int flag) {
        int w = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        if (0 == flag) {
            return view.getMeasuredWidth();
        } else {
            return view.getMeasuredHeight();
        }
    }

    private void addAndMeasureChild(final View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT);
        }

        addViewInLayout(child, viewPos, params, true);
        child.measure(
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
    }

    int right, bottom;

    @SuppressLint("DrawAllocation")
    @Override
    protected synchronized void onLayout(boolean changed, int left, int top,
                                         int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.right = right;
        this.bottom = bottom;

        if (mAdapter == null) {
            return;
        }

        if (mDataChanged) {
            int oldCurrentX = mCurrentX;
            initView();
            removeAllViewsInLayout();
            mNextX = oldCurrentX;
            mDataChanged = false;
        }

        if (mScroller.computeScrollOffset()) {
            int scrollx = mScroller.getCurrX();
            mNextX = scrollx;
        }

        if (mNextX <= 0) {
            mNextX = 0;
            mScroller.forceFinished(true);
        }
        if (mNextX >= mMaxX) {
            mNextX = mMaxX;
            mScroller.forceFinished(true);
        }

        int dx = mCurrentX - mNextX;

        removeNonVisibleItems(dx);
        fillList(dx);
        positionItems(dx);

        mCurrentX = mNextX;
        if (!mScroller.isFinished()) {
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();

                }
            });
        }

    }


    private void fillList(final int dx) {
        int edge = 0;
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }
        fillListRight(edge, dx);

        edge = 0;
        child = getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }
        fillListLeft(edge, dx);

    }

    private void fillListRight(int rightEdge, final int dx) {
        while (rightEdge + dx < getWidth()
                && mRightViewIndex < mAdapter.getCount()) {

            View child = mAdapter.getView(mRightViewIndex,
                    mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();

            if (mRightViewIndex == mAdapter.getCount() - 1) {
                mMaxX = mCurrentX + rightEdge - getWidth();
            }

            if (mMaxX < 0) {
                mMaxX = 0;
            }
            mRightViewIndex++;
        }

    }

    private void fillListLeft(int leftEdge, final int dx) {
        while (leftEdge + dx > 0 && mLeftViewIndex >= 0) {
            View child = mAdapter.getView(mLeftViewIndex,
                    mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            mLeftViewIndex--;
            mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(final int dx) {
        View child = getChildAt(0);
        while (child != null && child.getRight() + dx <= 0) {
            mDisplayOffset += child.getMeasuredWidth();
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mLeftViewIndex++;
            child = getChildAt(0);

        }

        child = getChildAt(getChildCount() - 1);
        while (child != null && child.getLeft() + dx >= getWidth()) {
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mRightViewIndex--;
            child = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(final int dx) {
        if (getChildCount() > 0) {
            mDisplayOffset += dx;
            int left = mDisplayOffset;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth,
                        child.getMeasuredHeight());
                left += childWidth + child.getPaddingRight();
            }
        }
    }


    public synchronized void scrollTo(int x) {
        mScroller.startScroll(mNextX, 0, x - mNextX, 0);
        requestLayout();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return mGesture.onTouchEvent(ev);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        handled |= mGesture.onTouchEvent(ev);
        return handled;
    }

    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                              float velocityY) {
        synchronized (HorizontalListViewWithSetSelection.this) {
            mScroller.fling(mNextX, 0, (int) -velocityX, 0, 0, mMaxX, 0, 0);
        }
        requestLayout();

        return true;
    }

    protected boolean onDown(MotionEvent e) {
        mScroller.forceFinished(true);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOnScrollCallBack != null) {
            mOnScrollCallBack.onScroll(mCurrentX);
        }
    }

    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return HorizontalListViewWithSetSelection.this.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return HorizontalListViewWithSetSelection.this
                    .onFling(e1, e2, velocityX, velocityY);
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            synchronized (HorizontalListViewWithSetSelection.this) {
                mNextX += (int) distanceX;
            }
            requestLayout();


            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (isEventWithinView(e, child)) {
                    if (mOnItemClicked != null) {
                        mOnItemClicked.onItemClick(HorizontalListViewWithSetSelection.this,
                                child, mLeftViewIndex + 1 + i,
                                mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontalListViewWithSetSelection.this,
                                child, mLeftViewIndex + 1 + i,
                                mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (isEventWithinView(e, child)) {
                    if (mOnItemLongClicked != null) {
                        mOnItemLongClicked.onItemLongClick(
                                HorizontalListViewWithSetSelection.this, child, mLeftViewIndex
                                        + 1 + i,
                                mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
        }

        private boolean isEventWithinView(MotionEvent e, View child) {
            Rect viewRect = new Rect();
            int[] childPosition = new int[2];
            child.getLocationOnScreen(childPosition);
            int left = childPosition[0];
            int right = left + child.getWidth();
            int top = childPosition[1];
            int bottom = top + child.getHeight();
            viewRect.set(left, top, right, bottom);
            return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
        }
    };

    public interface OnScrollCallBack {
        void onScroll(int x);
    }
}
