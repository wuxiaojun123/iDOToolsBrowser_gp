package com.idotools.browser.gp.manager.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;

import com.idotools.browser.gp.App;
import com.idotools.browser.gp.R;
import com.idotools.browser.gp.activity.BrowserActivity;
import com.idotools.browser.gp.manager.dialog.AlertDialog;
import com.idotools.browser.gp.minterface.OnPageStartedListener;
import com.idotools.browser.gp.minterface.OnReceivedErrorListener;
import com.idotools.browser.gp.view.BrowserWebView;
import com.idotools.utils.ToastUtils;


/**
 * Created by wuxiaojun on 16-10-8.
 */
public class WebViewManager {

    private static final int API = Build.VERSION.SDK_INT;
    private BrowserWebView mWebView;
    private Context mContext;

    public WebViewManager(Activity mActivity) {
        this.mContext = mActivity;
        mWebView = new BrowserWebView(mActivity);
        initWebView(mActivity);
    }

    private void initWebView(Activity mActivity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.setId(View.generateViewId());
        }
        mWebView.setDrawingCacheBackgroundColor(Color.WHITE);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setFocusable(true);
        mWebView.setDrawingCacheEnabled(false);
        mWebView.setWillNotCacheDrawing(true);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //noinspection deprecation
            mWebView.setAnimationCacheEnabled(false);
            //noinspection deprecation
            mWebView.setAlwaysDrawnWithCacheEnabled(false);
        }
        mWebView.setBackgroundColor(Color.WHITE);

        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setSaveEnabled(true);
        mWebView.setNetworkAvailable(true);
        BrowserWebChromeClient mWebViewChromeClient = new BrowserWebChromeClient((WebviewInteface) mActivity);
        mWebView.setWebChromeClient(mWebViewChromeClient);
        BrowserWebClient mBrowserWebClient = new BrowserWebClient(mActivity);
        mBrowserWebClient.setmOnPageStartedListener((OnPageStartedListener) mActivity);
        mBrowserWebClient.setmOnReceivedErrorListener((OnReceivedErrorListener) mActivity);
        mWebView.setWebViewClient(mBrowserWebClient);
        mWebView.addJavascriptInterface(new BrowserJsInterface(mActivity.getApplicationContext()), "BrowserJsInterface");
        mWebView.setDownloadListener(new CustomDownloadListener());
        //mGestureDetector = new GestureDetector(activity, new CustomGestureListener());
        //mWebView.setOnTouchListener(new TouchListener());
        initializeSettings();
    }

    private class CustomDownloadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeSettings() {
        if (mWebView == null) {
            return;
        }
        final WebSettings settings = mWebView.getSettings();
        if (API < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //noinspection deprecation
            settings.setAppCacheMaxSize(Long.MAX_VALUE);
        }
        if (API < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection deprecation
            settings.setEnableSmoothTransition(true);
        }
        if (API > Build.VERSION_CODES.JELLY_BEAN) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (API >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        } else if (API >= Build.VERSION_CODES.LOLLIPOP) {
            // We're in Incognito mode, reject
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        }
        //设置缓存
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getFilePath("appCache"));
        if (API < Build.VERSION_CODES.KITKAT) {
            //noinspection deprecation
            settings.setDatabasePath(getFilePath("databaseCache"));
        }
//        settings.setUserAgentString("Android");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        if (API >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
    }

    /***
     * 缓存路径
     *
     * @param fileName
     * @return
     */
    private String getFilePath(String fileName) {
        return App.get(mContext).getDir(fileName, Context.MODE_PRIVATE).getPath();
    }

    /***
     * 获取当前页面的标题
     *
     * @return
     */
    public String getCurrentTitle() {
        if (mWebView != null) {
            return mWebView.getTitle();
        }
        return null;
    }

    /***
     * 获取当前页面的url
     *
     * @return
     */
    public String getCurrentUrl() {
        if (mWebView != null)
            return mWebView.getUrl();
        return null;
    }

    /***
     * 清除缓存
     *
     * @return
     */
    public void cleanCache() {
        new AlertDialog((BrowserActivity) mContext).builder()
                .setMsg(R.string.string_confirm_clean_all_cache)
                .setPositiveButton(R.string.string_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mWebView != null) {
                            mWebView.clearCache(true);
                            ToastUtils.show(mContext, mContext.getResources().getString(R.string.string_clean_success));
                        }
                    }
                }).setNegativeButton(R.string.string_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    private void checkWebviewIsNull() {
        if (mWebView == null) {
            return;
        }
    }

    public BrowserWebView getWebView() {
        return mWebView;
    }

}
