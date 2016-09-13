/*
 * Copyright (c) 2016, Shanghai YUEWEN Information Technology Co., Ltd.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of Shanghai YUEWEN Information Technology Co., Ltd. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY SHANGHAI YUEWEN INFORMATION TECHNOLOGY CO., LTD. AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.yw.game.floatmenu;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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

public class FloatMenu extends FrameLayout implements OnTouchListener {
    private final static String TAG = "FloatMenu";
    private final int HANDLER_TYPE_HIDE_LOGO = 100;
    private final int HANDLER_TYPE_CANCEL_ANIM = 101;

    //WindowMananger的params，控制这个值可以将自定义的view设置到窗口管理器中
    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;//当前view的窗口管理器
    private Context mContext;//一个全局上下文，demo使用的service的上下文


    private ImageView mFloatLogoImv;//悬浮球的logo
    private ImageView mFloatLoaderImv;//围绕悬浮球的动画背景图，可用于做旋转或其它动画，看具体的设计
    private LinearLayout mFloatMenuLine;//悬浮菜单的载体，横向线性布局
    private FrameLayout mFloatLogoFra;//悬浮球的logo和动画背景图的载体

    private ArrayList<MenuItem> mMenuItems; //菜单对象的集合
    private ArrayList<MenuItemView> mMenuItemViews; //菜单中的Item对应的view集合

    private boolean mIsRight;//当前悬浮球是否悬停在右边
    private boolean isInitingLoader;//当前悬浮球的动画是否首次加载
    private boolean isActionLoading;//当前悬浮球动画是否是点击事件触发的动画

    private float mTouchStartX;//记录首次按下的位置x
    private float mTouchStartY;//记录首次按下的位置y

    private int mScreenWidth;//屏幕宽度
    private int mScreenHeight;//屏幕高度
    private boolean mDraging;//是否拖动中
    private boolean mShowLoader = true;//是否显示加载动画

    private Timer mTimer;//一段时间没有操作 定时隐藏菜单，缩小悬浮球 的定时器，定时器可以设置一个对应的定时任务
    private TimerTask mTimerTask;//配合定时器的定时任务，本质是一个runnable,包装了定时相关业务方法

    /**
     * 用于接收隐藏缩小悬浮球、悬浮菜单，取消悬浮球的加载动画
     */
    final Handler mTimerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
                if (isInitingLoader) {
                    isInitingLoader = false;
                    mShowLoader = false;
                    LayoutParams params = (LayoutParams) mFloatLogoFra.getLayoutParams();
                    int padding75 = (params.width) / 3;
                    if (mIsRight) {
                        if (params.rightMargin <= 0) {
                            mFloatLogoFra.setPadding(16, 16, 0, 16);
                            params.setMargins(0, 0, -padding75, 0);
                            mFloatLogoFra.setLayoutParams(params);

                        }
                    } else {
                        if (params.leftMargin >= 0) {
                            params.setMargins(-padding75, 0, 0, 0);
                            mFloatLogoFra.setLayoutParams(params);
                            mFloatLogoFra.setPadding(0, 16, 16, 16);
                        }
                    }
                    mWmParams.alpha = 0.7f;
                    mWindowManager.updateViewLayout(FloatMenu.this, mWmParams);
                    refreshFloatMenu(mIsRight);
                    mFloatMenuLine.setVisibility(View.GONE);
                }
            } else if (msg.what == HANDLER_TYPE_CANCEL_ANIM) {
                resetLogoSize();
                mFloatLoaderImv.clearAnimation();
                mFloatLoaderImv.setVisibility(View.GONE);
                mShowLoader = false;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 悬浮球缩小后下次恢复原始大小
     */
    private void resetLogoSize() {
        LayoutParams floatLogoLayoutParams = Utils.createLayoutParams(Utils.dp2Px(50, mContext), Utils.dp2Px(50, mContext));
        mFloatLogoFra.setLayoutParams(floatLogoLayoutParams);
        mFloatLogoFra.setPadding(0, 0, 0, 0);
    }

    /**
     * 悬浮球构造器
     */
    public static class Builder {
        private Context mContext;
        private ArrayList<MenuItem> menuItems = new ArrayList<>();

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder menuItems(ArrayList<MenuItem> menuItems) {
            this.menuItems = menuItems;
            return this;
        }

        public FloatMenu build() {
            return new FloatMenu(this);
        }
    }

    /**
     * 悬浮球构造方法，用于创建悬浮菜单实例
     */
    public FloatMenu(Builder builder) {
        super(builder.mContext);
        this.mMenuItems = builder.menuItems;
        this.mContext = builder.mContext;
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

    public ArrayList<MenuItem> getMenuItems() {
        return mMenuItems;
    }

    /**
     * @param mContext mContext
     * @return return
     * 创建悬浮球，先从xml导入悬浮球的资源样板
     * 根据每个菜单item内容创建对应的MenuItemView
     */
    private View createView(final Context mContext) {
        View rootView = View.inflate(mContext, R.layout.layout_yw_float_menu, null);//将xml布局导入当前View
        FrameLayout rootFloatView = (FrameLayout) rootView.findViewById(R.id.ywGameMenu);
        mFloatMenuLine = (LinearLayout) rootView.findViewById(R.id.ywGameMenuLine);
        mFloatLogoFra = (FrameLayout) rootView.findViewById(R.id.ywGameFra);
        mFloatLogoImv = (ImageView) rootView.findViewById(R.id.ywGameLogo);
        mFloatLoaderImv = (ImageView) rootView.findViewById(R.id.ywGameLoader);

        mMenuItemViews = generateMenuItemViews();//创建悬浮菜单Item
        addMenuItemViews();//将创建的menuItemView加入线性布局


        //使悬浮球和加速球可以超出父容器之外
        mFloatLogoFra.setClipChildren(false);
        mFloatLogoFra.setClipToPadding(false);
        rootFloatView.setClipChildren(false);
        rootFloatView.setClipToPadding(false);


        mFloatMenuLine.setClipChildren(false);
        mFloatMenuLine.setClipToPadding(false);

        //设置当前悬浮球的touchListener事件，此listerner设置后，ontouchevent方法失效
        rootView.setOnTouchListener(this);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDraging) {
                    if (mFloatMenuLine.getVisibility() == View.VISIBLE) {
                        mFloatMenuLine.setVisibility(View.GONE);
                    } else {
                        mFloatMenuLine.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        //悬浮菜单的父容器设置的测量模式为大小完全不能确定
        rootView.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        return rootView;
    }

    //首次弹出菜单时，item会从透明到不透明，从小到大的动画效果
    private void addMenuItemViews() {
        for (final MenuItemView menuItemView : mMenuItemViews) {
            menuItemView.setVisibility(VISIBLE);
            mFloatMenuLine.addView(menuItemView);
        }
    }

    private ArrayList<MenuItemView> generateMenuItemViews() {
        int menuSize = mMenuItems.size();
        Log.e(TAG, "menuSize:" + menuSize);
        ArrayList<MenuItemView> menuItemViews = new ArrayList<>();
        for (int i = 0; i < mMenuItems.size(); i++) {
            MenuItem item = mMenuItems.get(i);
            item.showDivider = i != mMenuItems.size() - 1;
            menuItemViews.add(generateMenuItemView(item));
        }
        return menuItemViews;
    }

    private MenuItemView generateMenuItemView(MenuItem item) {
        MenuItemView menuItemView = new MenuItemView(mContext, item);
        setMenuItemOnClickListener(menuItemView, item.getOnClickListener());
        return menuItemView;
    }


    //中间先代理一次menuitem的点击事件，增加每次点击前先隐藏悬浮的菜单
    private void setMenuItemOnClickListener(final MenuItemView menuItemView, final View.OnClickListener onClickListener) {
        menuItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick");
                if (mFloatMenuLine.getVisibility() == VISIBLE) {
                    mFloatMenuLine.setVisibility(GONE);
                }
                onClickListener.onClick(menuItemView);
            }
        });
    }

    private void init(Context mContext) {
        this.mContext = mContext;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);//获取系统的窗口服务
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;//根据当前屏幕信息拿到屏幕的宽高
        mScreenHeight = dm.heightPixels;

        this.mWmParams = new WindowManager.LayoutParams();//获取窗口参数

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;//等于API19或API19以下需要指定窗口参数type值为TYPE_TOAST才可以作为悬浮控件显示出来
        } else {
            mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//API19以上侧只需指定为TYPE_PHONE即可
        }
        mWmParams.format = PixelFormat.RGBA_8888;//当前窗口的像素格式为RGBA_8888,即为最高质量


        //NOT_FOCUSABLE可以是悬浮控件可以响应事件，LAYOUT_IN_SCREEN可以指定悬浮球指定在屏幕内，部分虚拟按键的手机，虚拟按键隐藏时，虚拟按键的位置则属于屏幕内，此时悬浮球会出现在原虚拟按键的位置
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        //默认指定位置在屏幕的左上方，可以根据需要自己修改
        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

        //默认指定的横坐标为屏幕最左边
        mWmParams.x = 0;

        //默认指定的纵坐标为屏幕高度的10分之一，这里只是大概约束，因为上的flags参数限制，悬浮球不会出现在屏幕外
        mWmParams.y = mScreenHeight / 10;

        //宽度指定为内容自适应
        mWmParams.width = LayoutParams.WRAP_CONTENT;
        mWmParams.height = LayoutParams.WRAP_CONTENT;
//        addView();
        //导入xml的子布局
        addView(createView(mContext));

        //将当前view设置为前置窗口
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
                resetLogoSize();

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

    /**
     * 部分中兴手机 直接从窗口管理器中移除会产生崩溃，此处try catch结束悬浮球
     */
    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
        }
    }

    //部分手机，隐藏时当前view的成员变量可能会为空
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
        resetLogoSize();
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
        resetLogoSize();
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
     *
     *              左边时：悬浮球logo的大小是50dp，所有当左右切换时，需要将菜单中的第一个item对应悬浮的位置右移50dp，其它则在第一个基础上再移4dp的间距
     *              右侧同理
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
