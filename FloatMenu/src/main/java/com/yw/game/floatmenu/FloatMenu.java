package com.yw.game.floatmenu;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by wengyiming on 2015/12/20.
 * 浮动窗口服务
 */
public class FloatMenu extends FrameLayout implements OnTouchListener {
    private final static String TAG = "FloatView";
    private final int HANDLER_TYPE_HIDE_LOGO = 100;
    private final int HANDLER_TYPE_CANCEL_ANIM = 101;


    private
    @DrawableRes
    int floatLogo = -1;
    private
    @DrawableRes
    int floatLoader = -1;

    private
    @DrawableRes
    int menuBgId = -1;

    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;
    private Context mContext;


    private ImageView mFloatLogoImv;
    private ImageView mFloatLoaderImv;
    private LinearLayout mFloatMenuLine;
    private FrameLayout mFloatLogoFra;
    private ArrayList<MenuItem> mMenuItems;


    private ArrayList<MenuItemView> mMenuItemViews;
    private ArrayList<OnMenuActionListener> mActionListeners;

    private boolean mIsRight;
    private boolean isInitingLoader;
    private boolean isActionLoading;
    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mDraging;
    private boolean mShowLoader = true;

    private Timer mTimer;
    private TimerTask mTimerTask;

    final Handler mTimerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
                if (isInitingLoader) {
                    isInitingLoader = false;
                    mShowLoader = false;

                    LayoutParams params = (LayoutParams) mFloatLogoImv.getLayoutParams();
                    int padding30 = 30;
                    if (mIsRight) {
                        if (params.rightMargin <= 0) {
                            params.setMargins(0, 0, -padding30, 0);
                            mFloatLogoImv.setPadding(16, 16, 0, 16);
                        }
                    } else {
                        if (params.leftMargin >= 0) {
                            params.setMargins(-padding30, 0, 0, 0);
                            mFloatLogoImv.setPadding(0, 16, 16, 16);
                        }
                    }
                    mWmParams.alpha = 0.7f;
                    mWindowManager.updateViewLayout(FloatMenu.this, mWmParams);
                    refreshFloatMenu(mIsRight);
                    mFloatMenuLine.setVisibility(View.GONE);
                }
            } else if (msg.what == HANDLER_TYPE_CANCEL_ANIM) {
                mFloatLoaderImv.clearAnimation();
                mFloatLoaderImv.setVisibility(View.GONE);
                mShowLoader = false;
            }
            super.handleMessage(msg);
        }
    };


    public static class Builder {

        private Context mContext;
        private ArrayList<MenuItem> menuItems = new ArrayList<>();
        private
        @DrawableRes
        int floatLogoRes;
        private
        @DrawableRes
        int floatLoaderRes;
        private
        @DrawableRes
        int menuBgId;


        private ArrayList<OnMenuActionListener> actionListeners = new ArrayList<>();

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder addMenuItem(@ColorRes int bgColor, int icon, String label,
                                   @ColorRes int textColor, OnClickListener onClickListener) {
            menuItems.add(new MenuItem(bgColor, icon, label, textColor, onClickListener));
            return this;
        }

        public Builder menuItems(ArrayList<MenuItem> menuItems) {
            menuItems.clear();
            this.menuItems = menuItems;
            return this;
        }


        public Builder menuBackground(@DrawableRes int menuBgId) {
            this.menuBgId = menuBgId;
            return this;
        }


        public Builder addMenuItem(@ColorRes int bgColor, int icon, String label,
                                   @ColorRes int textColor, int diameter, OnClickListener onClickListener) {
            menuItems.add(new MenuItem(bgColor, icon, label, textColor, diameter, onClickListener));
            return this;
        }

        public Builder floatLogo(@DrawableRes int FloatLogo) {
            this.floatLogoRes = FloatLogo;
            return this;
        }

        public Builder floatLoader(@DrawableRes int floatLoader) {
            this.floatLoaderRes = floatLoader;
            return this;
        }


        public Builder onMenuActionListner(OnMenuActionListener listener) {
            actionListeners.add(listener);
            return this;
        }

        public FloatMenu build() {
            return new FloatMenu(this);
        }
    }

    public FloatMenu(Builder builder) {
        super(builder.mContext);
        this.floatLogo = builder.floatLogoRes;
        this.floatLoader = builder.floatLoaderRes;
        this.mMenuItems = builder.menuItems;
        this.mActionListeners = builder.actionListeners;
        this.mContext = builder.mContext;
        this.menuBgId = builder.menuBgId;
        Log.e(TAG, (mContext == null) + "");
        init(this.mContext);
    }

    public FloatMenu(Context context) {
        this(context, null, 0);
    }

    public FloatMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param mContext mContext
     * @return return
     */
    private View createView(final Context mContext) {
        FrameLayout rootFloatView = new FrameLayout(mContext);
        LayoutParams params = Utils.createLayoutParams(LayoutParams.MATCH_PARENT, Utils.dp2Px(50, mContext));
        params.gravity = Gravity.CENTER;
        rootFloatView.setLayoutParams(params);
        mFloatMenuLine = new LinearLayout(mContext);
        LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, Utils.dp2Px(50, mContext));
        mFloatMenuLine.setLayoutParams(lineLp);
        mFloatMenuLine.setOrientation(LinearLayout.HORIZONTAL);
        mFloatMenuLine.setBackgroundResource((menuBgId == -1) ? R.drawable.yw_menu_bg : menuBgId);
        mMenuItemViews = generateMenuItemViews();
        for (MenuItemView menuItemView : mMenuItemViews) {
            mFloatMenuLine.addView(menuItemView);
            addOnMenuActionListener(menuItemView);
        }
        rootFloatView.addView(mFloatMenuLine);

        mFloatLogoFra = new FrameLayout(mContext);
        LayoutParams floatLogoLayoutParams = Utils.createLayoutParams(Utils.dp2Px(50, mContext), Utils.dp2Px(50, mContext));
        mFloatLogoFra.setLayoutParams(floatLogoLayoutParams);
        floatLogoLayoutParams.gravity = Gravity.CENTER;
        mFloatLogoImv = new ImageView(mContext);


        LayoutParams imgLp = getImageViewLayoutParams();
        mFloatLogoImv.setLayoutParams(imgLp);
        mFloatLogoImv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mFloatLogoImv.setImageResource((floatLogo == -1) ? R.drawable.yw_image_float_logo : floatLogo);


        LayoutParams layoutParams = getImageViewLayoutParams();
        mFloatLoaderImv = new ImageView(mContext);
        mFloatLoaderImv.setLayoutParams(layoutParams);
        mFloatLoaderImv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mFloatLoaderImv.setImageResource((floatLoader == -1) ? R.drawable.yw_anim_background : floatLoader);
        mFloatLoaderImv.setVisibility(INVISIBLE);


        mFloatLogoFra.setClipChildren(false);
        mFloatLogoFra.setClipToPadding(false);

        rootFloatView.setClipChildren(false);
        rootFloatView.setClipToPadding(false);


        mFloatLogoFra.addView(mFloatLogoImv);
        mFloatLogoFra.addView(mFloatLoaderImv);
        rootFloatView.addView(mFloatLogoFra);


        rootFloatView.setOnTouchListener(this);
        rootFloatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDraging) {
                    if (mFloatMenuLine.getVisibility() == View.VISIBLE) {
                        mFloatMenuLine.setVisibility(View.GONE);
                        for (OnMenuActionListener listener : mActionListeners) {
                            listener.onMenuClose();
                        }
                    } else {
                        mFloatMenuLine.setVisibility(View.VISIBLE);
                        for (OnMenuActionListener listener : mActionListeners) {
                            listener.onMenuOpen();
                        }
                    }
                }
            }
        });
        rootFloatView.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        return rootFloatView;
    }

    @NonNull
    private LayoutParams getImageViewLayoutParams() {
        LayoutParams imgLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imgLp.gravity = Gravity.LEFT;
        return imgLp;
    }

    private void addOnMenuActionListener(OnMenuActionListener listener) {
        this.mActionListeners.add(listener);
    }

    private ArrayList<MenuItemView> generateMenuItemViews() {
        ArrayList<MenuItemView> menuItemViews = new ArrayList<>(mMenuItems.size());
        for (MenuItem item : mMenuItems) {
            MenuItemView menuItemView = new MenuItemView(mContext, item);
            LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lineLp.gravity = Gravity.CENTER;
            menuItemView.setLayoutParams(lineLp);
            menuItemView.setBackgroundColor(Color.TRANSPARENT);
            menuItemView.setOnClickListener(item.getOnClickListener());
            menuItemViews.add(menuItemView);
        }
        return menuItemViews;
    }

    private void init(Context mContext) {
        this.mContext = mContext;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        this.mWmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mWmParams.format = PixelFormat.RGBA_8888;
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mWmParams.x = 0;
        mWmParams.y = mScreenHeight / 10;
        mWmParams.width = LayoutParams.WRAP_CONTENT;
        mWmParams.height = LayoutParams.WRAP_CONTENT;
        addView(createView(mContext));
        bringToFront();
        mWindowManager.addView(this, mWmParams);
        mTimer = new Timer();
        mShowLoader = true;
        refreshFloatMenu(mIsRight);
        mFloatMenuLine.setVisibility(View.GONE);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getVisibility() == View.GONE) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        int oldX = mWmParams.x;
        int oldY = mWmParams.y;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
        }
        mWindowManager.updateViewLayout(this, mWmParams);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        removeTimerTask();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                isInitingLoader = true;
                LayoutParams imgLp = getImageViewLayoutParams();
                imgLp.setMargins(0, 0, 0, 0);
                mFloatLogoImv.setLayoutParams(imgLp);
                mFloatLoaderImv.setPadding(0, 0, 0, 0);
                mFloatLoaderImv.setLayoutParams(imgLp);

                mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);
                mDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    mDraging = true;
                    mWmParams.x = (int) (x - mTouchStartX);
                    mWmParams.y = (int) (y - mTouchStartY);
                    mWindowManager.updateViewLayout(this, mWmParams);
                    mFloatMenuLine.setVisibility(View.GONE);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (mWmParams.x >= mScreenWidth / 2) {
                    mWmParams.x = mScreenWidth;
                    mIsRight = true;
                } else if (mWmParams.x < mScreenWidth / 2) {
                    mIsRight = false;
                    mWmParams.x = 0;
                }
                refreshFloatMenu(mIsRight);
                timerForHide();
                mWindowManager.updateViewLayout(this, mWmParams);
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return false;
    }

    private void removeTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
        }
    }

    public void hide() {
        try {
            setVisibility(View.GONE);
            Message message = mTimerHandler.obtainMessage();
            message.what = HANDLER_TYPE_HIDE_LOGO;
            mTimerHandler.sendMessage(message);
            removeTimerTask();
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        }
    }

    public void show() {
        setVisibility(View.VISIBLE);
        isInitingLoader = true;

        LayoutParams imgLp = getImageViewLayoutParams();
        imgLp.setMargins(0, 0, 0, 0);
        mFloatLogoImv.setPadding(0, 0, 0, 0);
        mFloatLogoImv.setLayoutParams(imgLp);
        mFloatLoaderImv.setPadding(0, 0, 0, 0);
        mFloatLoaderImv.setLayoutParams(imgLp);

        mWmParams.alpha = 1f;
        mWindowManager.updateViewLayout(this, mWmParams);


        if (mShowLoader) {
            mShowLoader = false;
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f));
            animationSet.addAnimation(new AlphaAnimation(0.5f, 1.0f));
            animationSet.setDuration(500);
            animationSet.setInterpolator(new AccelerateInterpolator());
            animationSet.setFillAfter(false);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mFloatLoaderImv.setVisibility(VISIBLE);
                    Animation rotaAnimation = new RotateAnimation(0f, +360f, Animation.RELATIVE_TO_PARENT,
                            0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
                    rotaAnimation.setInterpolator(new LinearInterpolator());
                    rotaAnimation.setRepeatCount(Animation.INFINITE);
                    rotaAnimation.setDuration(800);
                    rotaAnimation.setRepeatMode(Animation.RESTART);
                    mFloatLoaderImv.startAnimation(rotaAnimation);
                    cancleAnim();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFloatLogoImv.startAnimation(animationSet);
            timerForHide();
        }
    }

    public void startLoaderAnim() {
        isActionLoading = true;
        removeTimerTask();
        Animation rotaAnimation = new RotateAnimation(0f, +360f, Animation.RELATIVE_TO_PARENT,
                0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        rotaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotaAnimation.setRepeatCount(Animation.INFINITE);
        rotaAnimation.setDuration(800);
        rotaAnimation.setRepeatMode(Animation.RESTART);
        mFloatLoaderImv.startAnimation(rotaAnimation);

    }

    public void changeLogo(int logoDrawableRes, int msgDrawableRes, int menuItemPosition) {
        mFloatLogoImv.setImageResource(logoDrawableRes);
        mMenuItemViews.get(menuItemPosition).setImageView(msgDrawableRes);
    }


    public void removeMenuItenView(int position) {
        MenuItemView menuItemView = null;
        int count = mMenuItemViews.size();
        for (int i = 0; i < count; i++) {
            if (position == i) {
                menuItemView = mMenuItemViews.get(i);
            }
        }
        if (menuItemView != null) {
            mFloatMenuLine.removeView(menuItemView);
            mFloatMenuLine.requestLayout();
            refreshFloatMenu(mIsRight);
        }
    }

    public void stopLoaderAnim() {
        isActionLoading = false;
        mFloatLoaderImv.clearAnimation();
        mFloatLoaderImv.setVisibility(View.GONE);
        timerForHide();
    }

    public void cancleAnim() {
        mShowLoader = false;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.sendEmptyMessage(HANDLER_TYPE_CANCEL_ANIM);
            }
        }, 3000);
    }


    /**
     * 刷新float view menu
     *
     * @param right right
     */
    @SuppressLint("RtlHardcoded")
    private void refreshFloatMenu(boolean right) {
        int padding = Utils.dp2Px(4, mContext);
        int padding52 = Utils.dp2Px(50, mContext);
        int count = mMenuItemViews.size();
        if (right) {
            LayoutParams paramsFloatImage = (LayoutParams) mFloatLogoImv.getLayoutParams();
            paramsFloatImage.gravity = Gravity.RIGHT;
            mFloatLogoImv.setLayoutParams(paramsFloatImage);


            LayoutParams mFloatLoaderImvLayoutParams = (LayoutParams) mFloatLoaderImv.getLayoutParams();
            mFloatLoaderImvLayoutParams.gravity = Gravity.RIGHT;
            mFloatLoaderImv.setLayoutParams(mFloatLoaderImvLayoutParams);


            LayoutParams paramsFlFloat = (LayoutParams) mFloatLogoFra.getLayoutParams();
            paramsFlFloat.gravity = Gravity.RIGHT;
            mFloatLogoFra.setLayoutParams(paramsFlFloat);

            for (int i = 0; i < count; i++) {
                MenuItemView menuItemView = mMenuItemViews.get(i);
                if (i == count - 1) {
                    LinearLayout.LayoutParams paramsMenuClose = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenuClose.rightMargin = padding52;
                    paramsMenuClose.leftMargin = padding;
                    menuItemView.setLayoutParams(paramsMenuClose);
                } else {
                    LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenuAccount.rightMargin = padding;
                    paramsMenuAccount.leftMargin = padding;
                    menuItemView.setLayoutParams(paramsMenuAccount);
                }
            }
        } else {
            LayoutParams params = (LayoutParams) mFloatLogoImv.getLayoutParams();
            params.gravity = Gravity.LEFT;
            mFloatLogoImv.setLayoutParams(params);


            LayoutParams mFloatLoaderImvLayoutParams = (LayoutParams) mFloatLoaderImv.getLayoutParams();
            mFloatLoaderImvLayoutParams.gravity = Gravity.RIGHT;
            mFloatLoaderImv.setLayoutParams(mFloatLoaderImvLayoutParams);


            LayoutParams paramsFlFloat = (LayoutParams) mFloatLogoFra.getLayoutParams();
            paramsFlFloat.gravity = Gravity.LEFT;
            mFloatLogoFra.setLayoutParams(paramsFlFloat);

            for (int i = 0; i < count; i++) {
                MenuItemView menuItemView = mMenuItemViews.get(i);
                if (i == 0) {
                    LinearLayout.LayoutParams paramsMenuClose = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenuClose.rightMargin = padding;
                    paramsMenuClose.leftMargin = padding52;
                    menuItemView.setLayoutParams(paramsMenuClose);
                } else {
                    LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) menuItemView.getLayoutParams();
                    paramsMenuAccount.rightMargin = padding;
                    paramsMenuAccount.leftMargin = padding;
                    menuItemView.setLayoutParams(paramsMenuAccount);
                }
            }
        }
    }

    /**
     */
    private void timerForHide() {
        if (isActionLoading) {
            Log.e("", "加载动画正在执行,不能启动隐藏悬浮的定时器");
            return;
        }
        isInitingLoader = true;
        //结束任务
        if (mTimerTask != null) {
            try {
                mTimerTask.cancel();
                mTimerTask = null;
            } catch (Exception ignored) {
            }
        }
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = mTimerHandler.obtainMessage();
                message.what = HANDLER_TYPE_HIDE_LOGO;
                mTimerHandler.sendMessage(message);
            }
        };
        if (isInitingLoader) {
            if (mTimer != null) {
                mTimer.schedule(mTimerTask, 6000, 3000);
            }
        }
    }

    public void destroy() {
        hide();
        removeFloatView();
        removeTimerTask();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        try {
            mTimerHandler.removeMessages(1);
        } catch (Exception ignored) {
        }
    }


}
