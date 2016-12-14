package com.idotools.browser.gp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.idotools.browser.gp.utils.ActivitySlideAnim;
import com.idotools.browser.gp.utils.ActivityUtils;

/**
 * Created by wuxiaojun on 16-10-2.
 */
public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        ActivityUtils.addActivitys(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivitySlideAnim.slideOutAnim(this);
    }

    @Override
    protected void onDestroy() {
        ActivityUtils.removeActivity(this);
        super.onDestroy();
    }
}
