package com.google.push.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.*;

/**
 * Created by wuxiaojun on 16-11-28.
 */
public class BannerExample extends Activity {

    /*private NativeExpressAdView mNativeExpressAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create a native express ad. The ad size and ad unit ID must be set before calling
        // loadAd.
        mNativeExpressAdView = new NativeExpressAdView(this);
        mNativeExpressAdView.setAdSize(new AdSize(400, 100));
        mNativeExpressAdView.setAdUnitId("myAdUnitId");

        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        // Optionally populate the ad request builder.
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        // Add the NativeExpressAdView to the view hierarchy.
        layout.addView(mNativeExpressAdView);

        // Start loading the ad.
        mNativeExpressAdView.loadAd(adRequestBuilder.build());

        setContentView(layout);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resume the NativeExpressAdView.
        mNativeExpressAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the NativeExpressAdView.
        mNativeExpressAdView.pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the NativeExpressAdView.
        mNativeExpressAdView.destroy();

        super.onDestroy();
    }*/
}
