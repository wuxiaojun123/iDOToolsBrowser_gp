package com.idotools.browser.gp.manager.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alibaba.sdk.android.FeedBackConstant;
import com.idotools.browser.gp.R;
import com.idotools.browser.gp.activity.AboutActivity;
import com.idotools.browser.gp.activity.BrowserActivity;
import com.idotools.browser.gp.activity.SplashActivity;
import com.idotools.browser.gp.manager.dialog.AlertDialog;
import com.idotools.browser.gp.manager.webview.WebViewManager;
import com.idotools.browser.gp.sqlite.RecordsSqliteManager;
import com.idotools.browser.gp.utils.ActivitySlideAnim;
import com.idotools.browser.gp.utils.ActivityUtils;
import com.idotools.browser.gp.utils.Constant;
import com.idotools.browser.gp.utils.GooglePlayUtils;
import com.idotools.browser.gp.utils.ShareUtils;
import com.idotools.browser.gp.utils.ShortCutUtils;
import com.idotools.browser.gp.view.ImageTextViewGroup;
import com.idotools.utils.MetricsUtils;
import com.idotools.utils.MobileScreenUtils;
import com.idotools.utils.SharedPreferencesHelper;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuxiaojun on 16-10-2.
 */
public class MainPopupWindow implements View.OnClickListener {

    public static final int DURATION_START = 200;
    public static final int DURATION_END = 200;
    private Context mContext;
    private PopupWindow pw;
    private View contentView;
    @BindView(R.id.id_ll_empty)
    LinearLayout id_ll_empty;
    @BindView(R.id.id_ll_content)
    LinearLayout id_ll_content;
    @BindView(R.id.id_share)
    ImageTextViewGroup id_share;
    @BindView(R.id.id_records)
    ImageTextViewGroup id_records;

    @BindView(R.id.id_add_shortcut)
    ImageTextViewGroup id_add_shortcut;
    @BindView(R.id.id_night_mode)
    ImageTextViewGroup id_night_mode;
    @BindView(R.id.id_check_update)
    ImageTextViewGroup id_check_update;
    @BindView(R.id.id_feedback)
    ImageTextViewGroup id_feedback;
    @BindView(R.id.id_about)
    ImageTextViewGroup id_about;
    @BindView(R.id.id_exit)
    ImageTextViewGroup id_exit;

    private int translationY = 0;//平移的高度
    private int bottomHeight = 0;//地步控制布局的高度
    private ObjectAnimator mContentHiddrenAnim;//中间退出动画
    private ObjectAnimator mHeadHiddrenAnim;//底部退出动画
    //开始动画
    private ObjectAnimator mContentStartAnim;
    private ObjectAnimator mHeadStartAnim;
    //是否开始夜间模式的切换动画 0 默认　１从白天到夜间模式切换  2从夜间到白天模式切换
    public int isDayNightModeToogle;
    private RecordsSqliteManager mRecordsSqliteManager;
    private int nightTextColor;
    private int dayTextColor;

    public MainPopupWindow(Context context) {
        this.mContext = context;
        translationY = MetricsUtils.dipToPx(180);
        bottomHeight = MetricsUtils.dipToPx(45.0f);
        nightTextColor = mContext.getResources().getColor(R.color.color_popupwindow_text_night);
        dayTextColor = mContext.getResources().getColor(R.color.color_popupwindow_text);
        init();
        startAnim();
        exitAnim();
    }

    private void init() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_popupwindow, null);
        ButterKnife.bind(this, contentView);
        pw = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, MobileScreenUtils.getScreenHeight(mContext) - bottomHeight - MobileScreenUtils.getStatusHeight(mContext));
        pw.setOutsideTouchable(true);//设置点击其他区域也会消失
        boolean modeNight = SharedPreferencesHelper.getInstance(mContext).getBoolean(SharedPreferencesHelper.SP_KEY_MODE_NIGHT, false);
        toogleNightMode(modeNight);
        initWebviewManager();
        judgeRecords();
    }

    /**
     * 判断当前页面是否为首页中的页面
     * 是
     * 判断是否已收藏当前页面
     * 不是
     * 则设置当前收藏按钮不能点击
     */
    private void judgeRecords() {
        BrowserActivity mActivity = (BrowserActivity) mContext;
        if (mActivity.mRecordsBean != null) {
            urlIsexitSqlite(mActivity.mRecordsBean.url);
        } else {
            //获取当前url
            urlIsexitSqlite(mWebViewManager.getCurrentUrl());
            id_records.setClickable(false);
        }
    }

    /**
     * 初始化webviewmanger
     */
    private void initWebviewManager() {
        if (mWebViewManager == null) {
            mWebViewManager = ((BrowserActivity) mContext).getWebViewManager();
        }
    }

    /***
     * 判断url 是否存在收藏数据库中
     *
     * @param url
     */
    private void urlIsexitSqlite(String url) {
        if (mRecordsSqliteManager == null) {
            mRecordsSqliteManager = new RecordsSqliteManager(mContext);
        }
        boolean result = mRecordsSqliteManager.selectByUrl(url);
        if (result) {
            id_records.setClickable(false);
            id_records.setImageResource(R.mipmap.img_already_records);
            id_records.setTextResource(R.string.string_alread_records);
        }
    }


    public void showPopupWindow(View rootView) {
        if (pw != null) {
            enterStartAnim();
            pw.showAsDropDown(rootView, 0, 0);
        }
    }

    /**
     * 判断是否正在显示
     *
     * @return
     */
    public boolean isShow() {
        if (pw != null) {
            return pw.isShowing();
        }
        return false;
    }

    private WebViewManager mWebViewManager;

    @OnClick({R.id.id_ll_empty, R.id.id_share, R.id.id_records,
            R.id.id_add_shortcut, R.id.id_night_mode, R.id.id_check_update,
            R.id.id_feedback, R.id.id_about, R.id.id_exit})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_ll_empty:
                exitStartAnim();
                break;
            case R.id.id_share:
                //分享
                if (TextUtils.isEmpty(mWebViewManager.getCurrentUrl())) {
                    ShareUtils.shareText((BrowserActivity) mContext, mWebViewManager.getCurrentTitle() + " " + Constant.PATH);
                } else {
                    ShareUtils.shareText((BrowserActivity) mContext, mWebViewManager.getCurrentTitle() + " " + mWebViewManager.getCurrentUrl());
                }

                break;
            case R.id.id_records:
                //收藏,把当前数据插入到数据库中
                exitStartAnim();
                recordsPage();

                break;
            case R.id.id_add_shortcut:
                //添加桌面快捷方式
                addShortcut(mWebViewManager.getCurrentTitle(), mWebViewManager.getCurrentUrl());

                break;
            case R.id.id_night_mode:
                //夜间模式
                exitStartAnim();
                isDayNightModeToogle = 1;

                break;
            case R.id.id_check_update:
                //更新直接跳往google play
                GooglePlayUtils.openGooglePlay(mContext.getApplicationContext(), mContext.getPackageName());

                break;
            case R.id.id_feedback:
                //意见反馈
                FeedBackConstant.openFeedBackActivity((BrowserActivity) mContext);

                break;
            case R.id.id_about:
                exitStartAnim();
                mContext.startActivity(new Intent(mContext, AboutActivity.class));
                ActivitySlideAnim.slideInAnim((BrowserActivity) mContext);

                break;
            case R.id.id_exit:
                //退出
                exitApp();

                break;
        }
    }

    /***
     * 收藏当前页面
     */
    private void recordsPage() {
        BrowserActivity mActivity = (BrowserActivity) mContext;
        if (mActivity.mRecordsBean != null) {
            mRecordsSqliteManager.insert(mActivity.mRecordsBean);
            id_records.setImageResource(R.mipmap.img_already_records);
            id_records.setTextResource(R.string.string_alread_records);
            id_records.setClickable(false);
            ToastUtils.show(mContext, R.string.string_alread_records);
        }
    }

    /***
     * 添加桌面快捷方式
     * 获取当前页面的网址
     */
    private void addShortcut(final String title, final String url) {
        new AlertDialog((BrowserActivity) mContext).builder()
                .setMsg(R.string.string_confirm_add_shortcut)
                .setPositiveButton(R.string.string_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确定添加桌面快捷方式
                        Intent mIntent = new Intent();
                        mIntent.setClass(mContext, SplashActivity.class);
                        mIntent.putExtra("url", url);
                        ShortCutUtils.addShortCut(mContext, title, R.mipmap.icon, mIntent);
                    }
                }).setNegativeButton(R.string.string_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    /***
     * 夜间模式切换
     *
     * @param isNightMode
     */
    public void toogleNightMode(boolean isNightMode) {
        if (isNightMode) {
            //夜间模式
            id_night_mode.setImageResource(R.mipmap.img_day_normal);
            id_night_mode.setTextResource(R.string.string_night_day_mode);

            id_night_mode.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);
            id_share.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);
            id_records.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);
            id_add_shortcut.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);
            id_check_update.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);
            id_feedback.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);
            id_about.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);
            id_exit.setBackgroundResource(R.drawable.selector_bg_popupwindow_night);

            setTextNightMode();
        } else {
            //白天模式
            id_night_mode.setImageResource(R.mipmap.img_night_normal);
            id_night_mode.setTextResource(R.string.string_night_mode);

            id_night_mode.setBackgroundResource(R.drawable.selector_bg_popupwindow);
            id_share.setBackgroundResource(R.drawable.selector_bg_popupwindow);
            id_records.setBackgroundResource(R.drawable.selector_bg_popupwindow);
            id_add_shortcut.setBackgroundResource(R.drawable.selector_bg_popupwindow);
            id_check_update.setBackgroundResource(R.drawable.selector_bg_popupwindow);
            id_feedback.setBackgroundResource(R.drawable.selector_bg_popupwindow);
            id_about.setBackgroundResource(R.drawable.selector_bg_popupwindow);
            id_exit.setBackgroundResource(R.drawable.selector_bg_popupwindow);

            setTextDayMode();
        }
    }

    private void setTextDayMode() {
        id_night_mode.setTextColor(dayTextColor);
        id_share.setTextColor(dayTextColor);
        id_records.setTextColor(dayTextColor);
        id_add_shortcut.setTextColor(dayTextColor);
        id_check_update.setTextColor(dayTextColor);
        id_feedback.setTextColor(dayTextColor);
        id_about.setTextColor(dayTextColor);
        id_exit.setTextColor(dayTextColor);
    }

    private void setTextNightMode() {
        id_night_mode.setTextColor(nightTextColor);
        id_share.setTextColor(nightTextColor);
        id_records.setTextColor(nightTextColor);
        id_add_shortcut.setTextColor(nightTextColor);
        id_check_update.setTextColor(nightTextColor);
        id_feedback.setTextColor(nightTextColor);
        id_about.setTextColor(nightTextColor);
        id_exit.setTextColor(nightTextColor);
    }

    /***
     * 退出
     */
    private void exitApp() {
        new AlertDialog((BrowserActivity) mContext).builder()
                .setMsg(R.string.string_confirm_exit_app)
                .setPositiveButton(R.string.string_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitStartAnim();
                        ActivityUtils.finishAll();
                    }
                }).setNegativeButton(R.string.string_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    /**
     * 开始退出动画
     */
    public void exitStartAnim() {
        mContentHiddrenAnim.start();
        mHeadHiddrenAnim.start();
    }

    /***
     * 开始进入动画
     */
    private void enterStartAnim() {
        mContentStartAnim.start();
        mHeadStartAnim.start();
    }

    /***
     * popupwindow进入动画
     */
    public void startAnim() {
        if (mContentStartAnim == null && mHeadStartAnim == null) {
            mContentStartAnim = ObjectAnimator.ofFloat(id_ll_content, "translationY",
                    translationY, 0);
            mHeadStartAnim = ObjectAnimator.ofFloat(id_ll_empty, "alpha", 0f, 1f);
            mContentStartAnim.setDuration(DURATION_START);
            mHeadStartAnim.setDuration(500);
        }
    }

    /***
     * popupwindow退出动画
     */
    public void exitAnim() {
        if (mContentHiddrenAnim == null && mHeadHiddrenAnim == null) {
            mContentHiddrenAnim = ObjectAnimator.ofFloat(id_ll_content,
                    "translationY", 0, translationY);
            mHeadHiddrenAnim = ObjectAnimator.ofFloat(id_ll_empty, "alpha", 1f, 0f);
            mHeadHiddrenAnim.setDuration(DURATION_END);
            mContentHiddrenAnim.setDuration(DURATION_END);
            mContentHiddrenAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 等待动画执行完毕关闭popupwindow
                    pw.dismiss();
                    if (isDayNightModeToogle > 0) {
                        ((BrowserActivity) mContext).startDayNightModeToogleAnim();
                    }
                }
            });
        }
    }


}
