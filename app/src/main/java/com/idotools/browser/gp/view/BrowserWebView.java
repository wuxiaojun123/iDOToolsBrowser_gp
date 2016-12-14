package com.idotools.browser.gp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.idotools.utils.LogUtils;

/**
 * Created by wuxiaojun on 16-10-8.
 */
public class BrowserWebView extends WebView {

    public BrowserWebView(Context context) {
        this(context, null);
    }

    public BrowserWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrowserWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
