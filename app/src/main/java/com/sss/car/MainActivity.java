package com.sss.car;//package com.sss.car;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import lovcreate.com.testapplication.helper.PullToLoading;
//import lovcreate.com.testapplication.helper.RecyclerViewHelper;
//
//public class MainActivity extends AppCompatActivity{
//    private SwipeRefreshLayout layout_swipe_refresh;
//    private RecyclerView recyclerview;
//    private ArrayList<String> data;
//    private RecyclerViewHelper helper;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initData();
//        initView();
//    }
//
//    private void initData() {
//        data = new ArrayList<String>();
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//        data.add("第一条数据");
//    }
//    //初始化控件
//    private void initView() {
//        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
//        layout_swipe_refresh = (SwipeRefreshLayout)findViewById(R.id.layout_swipe_refresh);
//        //设置recyclerView
//        recyclerview.setLayoutManager(new LinearLayoutManager(this));
//        //自己写的adapter
//        Adapter adapter = new Adapter(data);
//        //把自己写的adapter嵌套到helper中
//        helper = new RecyclerViewHelper(adapter);
//                //给RecyclerView添加头
//                TextView tv = new TextView(this);
//                tv.setText("这是一个头");
//                helper.addHeader(tv);
//                //给RecyclerView添加脚
////                TextView tv2 = new TextView(this);
////                tv.setText("这是一个脚");
////                helper.addFooter(tv2);
//        //最后把helper设置到recyclerView上
//        recyclerview.setAdapter(helper);
//        //给recyclerView添加下拉刷新(刷新的颜色可自定义)
//        layout_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //更改数据
//                data.add(0,"下拉刷新新增的数据");
//                //这里的1是添加的头的个数，因为调用系统的刷新动画需要传一个位置参数
//                helper.notifyItemInserted(1); //刷新动画
//                //关闭下拉刷新动画
//                layout_swipe_refresh.setRefreshing(false);
//            }
//        });
//        //给recyclerView添加上拉加载(加载动画可自定义，更改base_pull_to_loading.xml文件即可)
//        recyclerview.addOnScrollListener(new PullToLoading() {
//            @Override
//            public void loading(RecyclerView recyclerView) {
//                //滑动到底部调用(模拟上拉加载)
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(200);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //更改数据
//                                    data.add("这个是上拉加载的数据");
//                                    //需关闭上拉加载动画（可以不用关闭）
//                                    helper.closePullToLoading();
//                                }
//                            });
//                        } catch (InterruptedException e) {
//
//                        }
//                    }
//                }).start();
//            }
//        });
//    }
//}
