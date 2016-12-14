package com.idotools.browser.gp.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.idotools.browser.gp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 16-11-29.
 */

public class DmzjViewHolderTypeAdmob extends RecyclerView.ViewHolder {

    /*@BindView(R.id.adView)
    public NativeExpressAdView adView;*/
    @BindView(R.id.id_fl_content)
    public FrameLayout id_fl_content;

    public DmzjViewHolderTypeAdmob(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
