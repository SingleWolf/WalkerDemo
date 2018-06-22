package com.walker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.walker.R;
import com.walker.WalkerApplication;
import com.walker.adapter.SummyAdapter;
import com.walker.base.BaseFragment;
import com.walker.constant.SQLiteConfig;
import com.walker.data.sqlite.WalkerDB;
import com.walker.delegate.OnRecyclerItemClickListener;
import com.walker.entity.Summary;

import java.util.ArrayList;
import java.util.List;

/**
 * summary :简介fragment
 * time    :2016/7/5 11:07
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class SummyFragment extends BaseFragment {
    /**
     * 所有
     */
    public static final int TYPE_ALL = 0;
    /**
     * UI
     */
    public static final int TYPE_UI = 1;
    /**
     * 逻辑
     */
    public static final int TYPE_LOGIC = 2;
    /**
     * 测试
     */
    public static final int TYPE_TEST = 3;
    /**
     * 其他
     */
    public static final int TYPE_OTHER = 4;
    /**
     * 刷新数据
     */
    private static final int CODE_REFRESH_DATA = 0x101;
    /**
     * 简介类型
     */
    private int mSummyType;
    /**
     * 示例数据
     */
    private List<Summary> mSummary;
    /**
     * 数据库实例
     */
    private WalkerDB mDB;
    /**
     * 刷新组件
     */
    private SwipeRefreshLayout mSwipeRefresh;
    /**
     * 适配器
     */
    private SummyAdapter mAdapter;

    /**
     * 获取实例
     *
     * @param type 类型
     * @return SummyFragment
     */
    public static SummyFragment newInstance(int type) {
        SummyFragment fragment = new SummyFragment();
        Bundle data = new Bundle();
        data.putInt("type", type);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDB = new WalkerDB(WalkerApplication.getContext());
        mSummyType = getArguments().getInt("type", 0);
        mSummary = new ArrayList<>();
        mSummary.addAll(getData());
        mAdapter = new SummyAdapter(mSummary);
    }


    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        RecyclerView recyclerView = (RecyclerView) baseView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Summary entity = mSummary.get(vh.getLayoutPosition());
                try {
                    if (TextUtils.equals(SQLiteConfig.CLASS_ACT, entity.getCLASS_TYPE())) {
                        Intent intent = new Intent(getHoldActivity(), Class.forName(entity.getCLASS_NAME()));
                        startActivity(intent);
                    } else if (TextUtils.equals(SQLiteConfig.CLASS_FRAG, entity.getCLASS_TYPE())) {
                        if (findFragByTag(entity.getCLASS_NAME()) != null) {
                            addFragment((BaseFragment) findFragByTag(entity.getCLASS_NAME()), entity.getCLASS_NAME());
                        } else {
                            addFragment((BaseFragment) Class.forName(entity.getCLASS_NAME()).newInstance(), entity.getCLASS_NAME());
                        }
                    }
                } catch (ClassNotFoundException e) {
                    Log.e("ClassNotFound", e.toString());
                    Snackbar.make(vh.itemView, "ClassNotFound", Snackbar.LENGTH_SHORT).show();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });

        mSwipeRefresh = (SwipeRefreshLayout) baseView.findViewById(R.id.swipeLayout);
        mSwipeRefresh.setColorSchemeResources(R.color.red, R.color.green, R.color.yellow, R.color.blue);
        mSwipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefresh.setProgressBackgroundColor(R.color.lightblue);
        //mSwipeRefresh.setPadding(20, 20, 20, 20);
        //mSwipeRefresh.setProgressViewOffset(true, 100, 200);
        //mSwipeRefresh.setDistanceToTriggerSync(50);
        mSwipeRefresh.setProgressViewEndTarget(true, 100);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exeHanlder.sendEmptyMessageDelayed(CODE_REFRESH_DATA, 2000);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_summy;
    }


    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    /**
     * 获取数据
     *
     * @return 数据集合
     */
    private ArrayList<Summary> getData() {
        ArrayList<Summary> summary;
        if (mDB == null) {
            mDB = new WalkerDB(WalkerApplication.getContext());
        }
        switch (mSummyType) {
            case TYPE_ALL:
                summary = mDB.getSummary();
                break;
            case TYPE_UI:
                summary = mDB.getSummaryByType(SQLiteConfig.SUMMARY_UI);
                break;
            case TYPE_LOGIC:
                summary = mDB.getSummaryByType(SQLiteConfig.SUMMARY_LOGIC);
                break;
            case TYPE_TEST:
                summary = mDB.getSummaryByType(SQLiteConfig.SUMMARY_TEST);
                break;
            case TYPE_OTHER:
                summary = mDB.getSummaryByType(SQLiteConfig.SUMMARY_OTHER);
                break;
            default:
                summary = new ArrayList<>();
                break;
        }
        return summary;
    }

    /**
     * 异步处理
     */
    private Handler exeHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_REFRESH_DATA:
                    mSummary.clear();
                    mSummary.addAll(getData());
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefresh.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 根据标签获取fragment
     *
     * @param tag 标签
     * @return Object
     */
    private Object findFragByTag(String tag) {
        return getHoldActivity().getSupportFragmentManager().findFragmentByTag(tag);
    }
}
