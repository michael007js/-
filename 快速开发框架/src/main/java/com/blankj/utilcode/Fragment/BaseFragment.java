package com.blankj.utilcode.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.autoAdapter.AutoUtils;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment预加载问题的解决方案：
 * 1.可以懒加载的Fragment
 * 2.切换到其他页面时停止加载数据（可选）
 * Created by leilei on 2017/7/27.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * 视图是否已经初初始化
     */
    public boolean isInit = false;
    public boolean isLoad = false;
    public boolean isVisibleToUser = false;
    private View view;
    List<RequestModel> callList = new ArrayList<>();
    List<ImageView> imageViewList = new ArrayList<>();
    public WeakReference<Context> weakReference;
    public WeakReference<BaseFragment> weakReferenceBaseFragment;

    public Context getBaseFragmentActivityContext() {
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public BaseFragment getBaseFragment() {
        if (weakReferenceBaseFragment != null) {
            return weakReferenceBaseFragment.get();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        weakReference = new WeakReference(getActivity());
        weakReferenceBaseFragment = new WeakReference<BaseFragment>(this);
        view = inflater.inflate(setContentView(), container, false);
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
        return view;
    }


    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        isCanLoadData();
        onFragmentVisibleChange(isVisibleToUser);
    }

    /**
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopLoad();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            if (isAdded()){
                lazyLoad();
                isLoad = true;
            }
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }


    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            clear();
        }


    }

    void clear() {
        if (weakReferenceBaseFragment != null) {
            weakReferenceBaseFragment.clear();
        }
        weakReferenceBaseFragment = null;
        if (weakReference != null) {
            weakReference.clear();
        }
        weakReference = null;

        if (callList != null) {
            clearRequestCall();
            callList.clear();
        }
        callList = null;
        FrescoUtils.clearCache();
        clearImageViewList();
        Runtime.getRuntime().gc();
    }


    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract int setContentView();

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected abstract void stopLoad();


    /**************************************************************
     *  自定义的回调方法，子类可根据需求重写
     *************************************************************/

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (!isVisible) {
            clearRequestCall();
        }
    }


    /**
     * 添加网络请求
     *
     * @param requestModel
     */
    public void addRequestCall(RequestModel requestModel) {
        if (callList == null) {
            return;
        }
        callList.add(requestModel);
    }

    /**
     * 清除网络请求
     */
    public void clearRequestCall(String string) {
        if (callList == null) {
            return;
        }
        for (int i = 0; i < callList.size(); i++) {
            if (EmptyUtils.isNotEmpty(callList.get(i))) {
                if (string.equals(callList.get(i).string)) {
                        callList.get(i).call.cancel();
                        callList.get(i).call = null;
                        callList.remove(i);
                    }
            }
        }
    }

    /**
     * 清除网络请求
     */
    public void clearRequestCall() {
        if (callList != null) {
            for (int i = 0; i < callList.size(); i++) {
                if (EmptyUtils.isNotEmpty(callList.get(i))) {
                    callList.get(i).call.cancel();
                    callList.get(i).call = null;
                    callList.remove(i);
                }
            }
        }
    }


    /**
     * 保存图片引用
     *
     * @param imageView
     */
    public void addImageViewList(ImageView imageView) {
        if (imageViewList != null) {
            imageViewList.add(imageView);
        }
    }


    /**
     * 清除图片用缓存
     */
    public void clearImageViewListCache() {
        if (imageViewList != null) {
            for (int i = 0; i < imageViewList.size(); i++) {
                if (imageViewList.get(i) != null) {
                    GlidUtils.clearImg(imageViewList.get(i));
                }
            }
        }
    }

    /**
     * 清除图片引用
     */
    public void clearImageViewList() {
        if (imageViewList != null) {
            for (int i = 0; i < imageViewList.size(); i++) {
                if (imageViewList.get(i) != null) {
                    GlidUtils.clearImg(imageViewList.get(i));
                    imageViewList.set(i, null);
                }
            }
            if (imageViewList != null) {
                imageViewList.clear();
            }
            imageViewList = null;
        }


    }
}
