package com.google.push.test;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.push.test.view.BrowserWebView;
import com.idotools.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "mainActivity";

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.webview)
    BrowserWebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient());
//        webView.loadUrl("https://www.baidu.com/");
        webView.loadUrl("http://wp.cgameclub.com/2016/12/12/dogs-need-to-exercise-and-keep-fit/");
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        LogUtils.e("开始刷新");
    }

}
