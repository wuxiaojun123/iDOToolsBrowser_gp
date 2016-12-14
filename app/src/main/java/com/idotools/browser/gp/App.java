package com.idotools.browser.gp;

import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.FeedBackConstant;
import com.facebook.FacebookSdk;

/**
 * Created by wuxiaojun on 16-10-8.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FeedBackConstant.initFeedBackAnnoy(this,FeedBackConstant.APPKEY_BROWSER);
        //FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

}
