package com.google.push.test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.idotools.utils.LogUtils;

/**
 * Created by wuxiaojun on 16-12-14.
 */

public class BrowserWebView extends WebView {


    public BrowserWebView(Context context) {
        super(context);
    }

    public BrowserWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrowserWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int lastY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                LogUtils.e("lastY=" + lastY + "----" + y);

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(ev);
    }


}
