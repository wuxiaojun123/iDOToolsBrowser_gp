package com.idotools.browser.gp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.idotools.browser.gp.R;
import com.idotools.browser.gp.activity.BrowserActivity;
import com.idotools.browser.gp.adapter.HistoryRecyclerAdapter;
import com.idotools.browser.gp.bean.CartoonDetailsBean;
import com.idotools.browser.gp.manager.dialog.RadioButtonAlertDialog;
import com.idotools.browser.gp.minterface.OnItemDeleteClickListener;
import com.idotools.browser.gp.sqlite.SqliteManager;
import com.idotools.browser.gp.utils.ActivitySlideAnim;
import com.idotools.browser.gp.view.SideSlipRecyclerView;
import com.idotools.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 16-11-30.
 */

public class HistoryFragment extends Fragment implements View.OnClickListener, OnItemDeleteClickListener {

    @BindView(R.id.id_recycler_view)
    SideSlipRecyclerView recyclerView;
    private List<CartoonDetailsBean> list;
    private HistoryRecyclerAdapter mAdapter;
    private SqliteManager mSqliteManager;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history, null);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 从历史记录中获取数据
     */
    private void initData() {
        SqliteManager manager = new SqliteManager(mContext);
        list = manager.selectAll();
        if (list != null && !list.isEmpty()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new HistoryRecyclerAdapter(mContext, list);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setRecyclerOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
    }

    private boolean checked = true;

    public void cleanCache() {
        if (list == null || list.isEmpty()) {
            ToastUtils.show(mContext, getString(R.string.string_no_history));
            return;
        }
        new RadioButtonAlertDialog(getActivity()).builder()
                .setMsg(R.string.string_confirm_clean_all_cache)
                .setPositiveButton(R.string.string_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除所有历史记录
                        if (checked) {
                            if (list != null && !list.isEmpty()) {
                                if (mSqliteManager == null) {
                                    mSqliteManager = new SqliteManager(mContext);
                                }
                                mSqliteManager.deleteAll();
                                list.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        ToastUtils.show(mContext, mContext.getResources().getString(R.string.string_clean_success));
                    }
                })
                .setNegativeButton(R.string.string_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setRadioButton(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checked = isChecked;
                    }
                })
                .show();
    }

    @Override
    public void onItemClickListener(int position) {
        //打开url
        CartoonDetailsBean bean = list.get(position);
        if (bean != null) {
            Intent mIntent = new Intent(getActivity(), BrowserActivity.class);
            mIntent.putExtra("url", bean.url);
            startActivity(mIntent);

            ActivitySlideAnim.slideOutAnim(getActivity());
        }
    }

    /**
     * //删除数据库中的这条数据
     *
     * @param position
     */
    @Override
    public void deleteListener(int position) {
        if (list != null && (list.size() - 1) >= position) {
            CartoonDetailsBean bean = list.get(position);
            if (bean != null) {
                if (mSqliteManager == null) {
                    mSqliteManager = new SqliteManager(mContext);
                }
                mSqliteManager.delete(bean.url);
                list.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mSqliteManager != null) {
            mSqliteManager.closeData();
        }
        super.onDestroyView();
    }

}
