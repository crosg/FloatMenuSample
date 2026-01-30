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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 悬浮菜单（支持自动贴边功能）
 */
public class FloatLogoMenu {
    private static final String TAG = "FloatLogoMenu";

    /**
     * 记录 logo 停放的位置
     */
    private static final String LOCATION_X = "hintLocation";
    private static final String LOCATION_Y = "locationY";

    /**
     * 悬浮球 坐落位置
     */
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    /**
     * 来自 activity 的 WindowManager
     */
    private WindowManager wManager;

    /**
     * 为 wManager 设置 LayoutParams
     */
    private WindowManager.LayoutParams wmParams;

    /**
     * 带透明度动画、旋转、放大的悬浮球
     */
    private DotImageView mFloatLogo;

    /**
     * 用于定时隐藏 logo
     */
    private CountDownTimer mHideTimer;

    /**
     * 记录是否拖动中
     */
    private boolean isDrag = false;

    /**
     * 来自 activity 的 mActivity
     */
    private Context mActivity;

    /**
     * 菜单背景颜色
     */
    private int mBackMenuColor = 0xffe4e3e1;

    /**
     * 是否绘制红点数字
     */
    private boolean mDrawRedPointNum = false;

    /**
     * 是否绘制圆形菜单项
     */
    private boolean mCircleMenuBg = false;

    /**
     * R.drawable.yw_game_logo
     */
    private Bitmap mLogoRes;

    /**
     * 用于显示在 mActivity 上的 Activity
     */
    private FloatMenuView.OnMenuClickListener mOnMenuClickListener;

    /**
     * 菜单背景
     */
    private Drawable mBackground;

    /**
     * 菜单项列表
     */
    private List<FloatItem> mFloatItems = new ArrayList<>();

    /**
     * 左侧布局
     */
    private LinearLayout rootViewLeft;

    /**
     * 右侧布局
     */
    private LinearLayout rootViewRight;

    /**
     * 默认停靠位置
     */
    private int mDefaultLocation = RIGHT;

    /**
     * 悬浮窗停靠位置
     */
    private int mHintLocation = mDefaultLocation;

    /**
     * 屏幕宽度
     */
    private int mScreenWidth;

    /**
     * 状态栏高度
     */
    private int mStatusBarHeight;

    /**
     * 触摸点在视图中的坐标
     */
    private float mXInView;
    private float mYinView;

    /**
     * 手指按下时的屏幕坐标
     */
    private float mXDownInScreen;
    private float mYDownInScreen;

    /**
     * 当前的屏幕坐标
     */
    private float mXInScreen;
    private float mYInScreen;

    /**
     * 菜单是否展开
     */
    private boolean isExpanded = false;

    /**
     * 自动贴边延时时间（毫秒），默认3秒
     */
    private int mAutoShrinkDelay = 3000;

    /**
     * 贴边状态
     */
    private boolean isShrunk = false;

    /**
     * 贴边动画
     */
    private ValueAnimator shrinkAnimator;

    /**
     * 贴边任务的Handler
     */
    private final Handler shrinkHandler = new Handler(Looper.getMainLooper());

    /**
     * 贴边任务：3秒无操作后自动贴边
     */
    private final Runnable shrinkRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "shrinkRunnable: 触发贴边任务, isExpanded=" + isExpanded + ", isDrag=" + isDrag);
            if (!isExpanded && !isDrag) {
                shrinkToEdge();
            } else {
                Log.d(TAG, "shrinkRunnable: 贴边被跳过，isExpanded=" + isExpanded + ", isDrag=" + isDrag);
            }
        }
    };

    public FloatLogoMenu(Builder builder) {
        mBackMenuColor = builder.mBackMenuColor;
        mDrawRedPointNum = builder.mDrawRedPointNum;
        mCircleMenuBg = builder.mCircleMenuBg;
        mLogoRes = builder.mLogoRes;
        mActivity = builder.mActivity;
        mOnMenuClickListener = builder.mOnMenuClickListener;
        mFloatItems = builder.mFloatItems;
        mBackground = builder.mDrawable;
        mAutoShrinkDelay = builder.mAutoShrinkDelay;
        mDefaultLocation = builder.mDefaultLocation;  // 从builder读取默认位置
        mHintLocation = mDefaultLocation;  // 初始化当前位置

        if (mActivity == null) {
            throw new IllegalArgumentException("Activity is null");
        }

        if (mLogoRes == null) {
            throw new IllegalArgumentException("No logo found, please setLogo/showWithLogo");
        }

        if (mFloatItems.isEmpty()) {
            throw new IllegalArgumentException("At least one menu item!");
        }

        initFloatWindow();
        initTimer();
        initFloat();
    }

    /**
     * 设置菜单项列表
     */
    public void setFloatItemList(List<FloatItem> floatItems) {
        this.mFloatItems = floatItems;
        calculateDotNum();
    }

    /**
     * 初始化悬浮球 window
     */
    private void initFloatWindow() {
        wmParams = new WindowManager.LayoutParams();
        if (mActivity instanceof Activity) {
            Activity activity = (Activity) mActivity;
            wManager = activity.getWindowManager();
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        } else {
            wManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }
        mScreenWidth = wManager.getDefaultDisplay().getWidth();
        int screenHeight = wManager.getDefaultDisplay().getHeight();

        wmParams.format = PixelFormat.RGBA_8888;
        // 添加 FLAG_LAYOUT_NO_LIMITS 允许view超出屏幕边界
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        wmParams.alpha = 1;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // 简化方案：始终使用Gravity.LEFT，所有x坐标都相对于左边缘
        @SuppressWarnings("RtlHardcoded")
        int gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.gravity = gravity;

        // 根据默认停靠位置设置初始x坐标（绝对坐标）
        int y = ((screenHeight - mStatusBarHeight) / 2) / 3;
        int logoWidth = dp2Px(50, mActivity);
        if (mDefaultLocation == RIGHT) {
            wmParams.x = mScreenWidth - logoWidth;  // 距离左边缘（屏幕宽度 - logo宽度）
            Log.d(TAG, "initFloatWindow: mDefaultLocation=RIGHT, x=" + wmParams.x + "（距左边缘）");
        } else {
            wmParams.x = 0;  // 距离左边缘0px（紧贴左边缘）
            Log.d(TAG, "initFloatWindow: mDefaultLocation=LEFT, x=" + wmParams.x + "（距左边缘）");
        }
        wmParams.y = y;

        Log.d(TAG, "initFloatWindow: mDefaultLocation=" + mDefaultLocation + ", mScreenWidth=" + mScreenWidth + ", 初始X=" + wmParams.x + ", 初始Y=" + wmParams.y);

        saveSetting(LOCATION_X, mDefaultLocation);
        saveSetting(LOCATION_Y, wmParams.y);
    }

    /**
     * 初始化悬浮球
     */
    private void initFloat() {
        generateLeftLineLayout();
        generateRightLineLayout();
        mFloatLogo = new DotImageView(mActivity, mLogoRes);
        mFloatLogo.setLayoutParams(new WindowManager.LayoutParams(dp2Px(50, mActivity), dp2Px(50, mActivity)));
        mFloatLogo.setDrawNum(mDrawRedPointNum);
        mFloatLogo.setBgColor(mBackMenuColor);
        mFloatLogo.setDrawDarkBg(true);
        calculateDotNum();
        floatBtnEvent();
        try {
            wManager.addView(mFloatLogo, wmParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 初始化完成后启动贴边任务
        startShrinkTask();
    }

    /**
     * 初始化定时器
     */
    private void initTimer() {
        mHideTimer = new CountDownTimer(2000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mOnMenuClickListener == null || isExpanded) {
                    mHideTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                // Timer finished
            }
        };
    }

    /**
     * 这个事件用于处理移动、自定义点击或者其它事情，return true可以保证 onclick 事件失效
     */
    @android.annotation.SuppressLint("ClickableViewAccessibility")
    private final OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    floatEventDown(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    floatEventMove(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    floatEventUp();
                    // 对于无障碍访问的支持
                    v.performClick();
                    break;
            }
            return true;
        }
    };

    /**
     * 菜单点击、关闭监听
     */
    private void setMenuClickListener(FloatMenuView mFloatMenuView) {
        mFloatMenuView.setOnMenuClickListener(new FloatMenuView.OnMenuClickListener() {
            @Override
            public void onItemClick(int position, String title) {
                mOnMenuClickListener.onItemClick(position, title);
            }

            @Override
            public void dismiss() {
                mFloatLogo.setDrawDarkBg(true);
                mOnMenuClickListener.dismiss();
                mHideTimer.start();
                // 菜单关闭后启动贴边任务
                startShrinkTask();
            }
        });
    }

    /**
     * 悬浮窗touch事件的 down 事件
     */
    private void floatEventDown(MotionEvent event) {
        isDrag = false;
        mHideTimer.cancel();

        // 取消贴边任务并恢复位置
        cancelShrinkTask();
        restoreFromShrink();

        if (mFloatLogo.getStatus() != DotImageView.NORMAL) {
            mFloatLogo.setStatus(DotImageView.NORMAL);
        }

        mXInView = event.getX();
        mYinView = event.getY();
        mXDownInScreen = event.getRawX();
        mYDownInScreen = event.getRawY();
        mXInScreen = event.getRawX();
        mYInScreen = event.getRawY();
    }

    /**
     * 悬浮窗touch事件的 move 事件
     */
    private void floatEventMove(MotionEvent event) {
        mXInScreen = event.getRawX();
        mYInScreen = event.getRawY();

        // 连续移动的距离超过3则更新一次视图位置
        if (Math.abs(mXInScreen - mXDownInScreen) > mFloatLogo.getWidth() / 4
                || Math.abs(mYInScreen - mYDownInScreen) > mFloatLogo.getHeight() / 4) {
            isDrag = true;
            int logoWidth = mFloatLogo.getWidth();
            int logoHeight = mFloatLogo.getHeight();
            int screenHeight = wManager.getDefaultDisplay().getHeight();

            // 手指中心和logo圆心保持一致
            wmParams.x = (int) (mXInScreen - logoWidth / 2);
            wmParams.y = (int) (mYInScreen - logoHeight / 2);

            // X坐标边界限制：不允许x为负数，也不允许x+logo宽度大于屏幕宽度
            if (wmParams.x < 0) {
                wmParams.x = 0;
            } else if (wmParams.x + logoWidth > mScreenWidth) {
                wmParams.x = mScreenWidth - logoWidth;
            }

            // Y坐标边界限制：不允许y小于状态栏高度，也不允许y+logo高度大于屏幕高度
            if (wmParams.y < mStatusBarHeight) {
                wmParams.y = mStatusBarHeight;
            } else if (wmParams.y + logoHeight > screenHeight) {
                wmParams.y = screenHeight - logoHeight;
            }

            updateViewPosition();

            mFloatLogo.setDrag(isDrag, false);
        } else {
            isDrag = false;
            mFloatLogo.setDrag(false, true);
        }
    }

    /**
     * 悬浮窗touch事件的 up 事件
     */
    private void floatEventUp() {
        Log.d(TAG, "floatEventUp: 手指抬起, isDrag=" + isDrag + ", isShrunk=" + isShrunk + ", isExpanded=" + isExpanded);
        Log.d(TAG, "floatEventUp: 当前logo位置X=" + wmParams.x + ", mXInScreen=" + mXInScreen + ", mScreenWidth/2=" + (mScreenWidth / 2));

        // 重置 isDrag 状态，允许贴边和打开菜单
        isDrag = false;

        // 改进的临界点判断：基于logo中心位置而不是触摸点
        int logoCenter = wmParams.x + mFloatLogo.getWidth() / 2;
        if (logoCenter < mScreenWidth / 2) {
            mHintLocation = LEFT;
            Log.d(TAG, "floatEventUp: logo中心在左侧，mHintLocation=LEFT, logoCenter=" + logoCenter);
        } else {
            mHintLocation = RIGHT;
            Log.d(TAG, "floatEventUp: logo中心在右侧，mHintLocation=RIGHT, logoCenter=" + logoCenter);
        }

        // 不再在拖动结束时更新gravity，保持Gravity.LEFT拖动
        // 只在展开菜单时才根据mHintLocation设置正确的gravity

        // 判断是否是点击事件（移动距离很小）
        boolean isClick = Math.abs(mXInScreen - mXDownInScreen) <= 3
                && Math.abs(mYInScreen - mYDownInScreen) <= 3;

        Log.d(TAG, "floatEventUp: isClick=" + isClick + ", 移动距离X=" + Math.abs(mXInScreen - mXDownInScreen) + ", 移动距离Y=" + Math.abs(mYInScreen - mYDownInScreen));

        if (isClick) {
            // 点击事件：根据状态决定行为
            if (isShrunk) {
                // 贴边状态下点击：先恢复到100%可见
                Log.d(TAG, "floatEventUp: 贴边状态点击，恢复到100%可见，不打开菜单");
                cancelShrinkTask();
                restoreFromShrink();
                // restoreFromShrink完成时会自动启动贴边任务（3秒后）
            } else {
                // 正常状态点击：打开菜单
                Log.d(TAG, "floatEventUp: 正常状态点击，打开菜单");
                openMenu();
                // 如果菜单没有展开，启动贴边任务
                if (!isExpanded) {
                    startShrinkTask();
                }
            }
        } else {
            // 拖拽结束：启动贴边任务
            startShrinkTask();
        }
    }

    /**
     * 打开菜单（始终使用Gravity.LEFT + 绝对坐标）
     */
    private void openMenu() {
        if (isDrag) return;

        // 取消贴边任务并恢复位置
        cancelShrinkTask();
        restoreFromShrink();

        if (!isExpanded) {
            mFloatLogo.setDrawDarkBg(false);
            try {
                wManager.removeViewImmediate(mFloatLogo);

                // 计算展开菜单的x坐标（始终使用Gravity.LEFT）
                int logoWidth = mFloatLogo.getWidth();
                int menuWidth = dp2Px(50, mActivity) * mFloatItems.size();
                int totalWidth = logoWidth + menuWidth;

                if (mHintLocation == RIGHT) {
                    // 右侧展开：让整个布局的右边缘贴着屏幕右边缘
                    wmParams.x = mScreenWidth - totalWidth;
                    Log.d(TAG, "openMenu: 右侧展开，x=" + wmParams.x + "（距左边缘），总宽度=" + totalWidth);
                } else {
                    // 左侧展开：从左边缘开始
                    wmParams.x = 0;
                    Log.d(TAG, "openMenu: 左侧展开，x=" + wmParams.x + "（距左边缘）");
                }

                // 添加视图（始终使用Gravity.LEFT）
                if (mHintLocation == RIGHT) {
                    wManager.addView(rootViewRight, wmParams);
                } else {
                    wManager.addView(rootViewLeft, wmParams);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            isExpanded = true;
            mHideTimer.cancel();
        } else {
            mFloatLogo.setDrawDarkBg(true);
            try {
                wManager.removeViewImmediate(mHintLocation == LEFT ? rootViewLeft : rootViewRight);

                // 关闭菜单：恢复logo到边缘位置
                int logoWidth = mFloatLogo.getWidth();
                if (mHintLocation == RIGHT) {
                    wmParams.x = mScreenWidth - logoWidth;
                    Log.d(TAG, "openMenu: 关闭右侧菜单，x=" + wmParams.x);
                } else {
                    wmParams.x = 0;
                    Log.d(TAG, "openMenu: 关闭左侧菜单，x=" + wmParams.x);
                }

                wManager.addView(mFloatLogo, wmParams);
            } catch (Exception e) {
                e.printStackTrace();
            }

            isExpanded = false;
            mHideTimer.start();
        }
    }

    /**
     * 更新悬浮窗在屏幕中的位置
     */
    private void updateViewPosition() {
        isDrag = true;
        try {
            if (!isExpanded) {
                if (wmParams.y - mFloatLogo.getHeight() / 2 <= 0) {
                    wmParams.y = mStatusBarHeight;
                    isDrag = true;
                }
                wManager.updateViewLayout(mFloatLogo, wmParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 悬浮窗的点击事件和touch事件的切换
     */
    @android.annotation.SuppressLint("ClickableViewAccessibility")
    private void floatBtnEvent() {
        mFloatLogo.setOnTouchListener(touchListener);
    }

    /**
     * 生成左侧布局
     */
    private void generateLeftLineLayout() {
        DotImageView floatLogo = new DotImageView(mActivity, mLogoRes);
        floatLogo.setLayoutParams(new WindowManager.LayoutParams(dp2Px(50, mActivity), dp2Px(50, mActivity)));
        floatLogo.setDrawNum(mDrawRedPointNum);
        floatLogo.setDrawDarkBg(mCircleMenuBg);

        // 为logo添加点击监听器，点击后关闭菜单
        floatLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    openMenu();  // 再次调用openMenu会关闭菜单
                }
            }
        });

        rootViewLeft = new LinearLayout(mActivity);
        rootViewLeft.setOrientation(LinearLayout.HORIZONTAL);
        rootViewLeft.setGravity(Gravity.CENTER);
        rootViewLeft.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2Px(50, mActivity)));

        rootViewLeft.setBackgroundDrawable(mBackground);

        FloatMenuView mFloatMenuView = new FloatMenuView.Builder(mActivity)
                .setFloatItems(mFloatItems)
                .setBackgroundColor(Color.TRANSPARENT)
                .setCicleBg(mCircleMenuBg)
                .setStatus(FloatMenuView.STATUS_LEFT)
                .setMenuBackgroundColor(Color.TRANSPARENT)
                .drawNum(mDrawRedPointNum)
                .create();
        setMenuClickListener(mFloatMenuView);

        rootViewLeft.addView(floatLogo);
        rootViewLeft.addView(mFloatMenuView);
    }

    /**
     * 生成右侧布局
     */
    private void generateRightLineLayout() {
        DotImageView floatLogo = new DotImageView(mActivity, mLogoRes);
        floatLogo.setLayoutParams(new WindowManager.LayoutParams(dp2Px(50, mActivity), dp2Px(50, mActivity)));
        floatLogo.setDrawNum(mDrawRedPointNum);
        floatLogo.setDrawDarkBg(mCircleMenuBg);

        // 为logo添加点击监听器，点击后关闭菜单
        floatLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    openMenu();  // 再次调用openMenu会关闭菜单
                }
            }
        });

        rootViewRight = new LinearLayout(mActivity);
        rootViewRight.setOrientation(LinearLayout.HORIZONTAL);
        rootViewRight.setGravity(Gravity.CENTER);
        rootViewRight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2Px(50, mActivity)));

        rootViewRight.setBackgroundDrawable(mBackground);

        FloatMenuView mFloatMenuView = new FloatMenuView.Builder(mActivity)
                .setFloatItems(mFloatItems)
                .setBackgroundColor(Color.TRANSPARENT)
                .setCicleBg(mCircleMenuBg)
                .setStatus(FloatMenuView.STATUS_RIGHT)
                .setMenuBackgroundColor(Color.TRANSPARENT)
                .drawNum(mDrawRedPointNum)
                .create();
        setMenuClickListener(mFloatMenuView);

        // 右侧布局：菜单在左，logo在右（配合Gravity.RIGHT使用）
        // 布局效果：[菜单1] [菜单2] [菜单3] [logo] → logo贴着右边缘
        rootViewRight.addView(mFloatMenuView);
        rootViewRight.addView(floatLogo);
    }

    /**
     * 计算总红点数
     */
    private void calculateDotNum() {
        int dotNum = 0;
        for (FloatItem floatItem : mFloatItems) {
            if (!TextUtils.isEmpty(floatItem.getDotNum())) {
                int num = Integer.parseInt(floatItem.getDotNum());
                dotNum = dotNum + num;
            }
        }
        if (mDrawRedPointNum) {
            mFloatLogo.setDotNum(dotNum, null);
        }
    }

    /**
     * 保存设置
     */
    private void saveSetting(String key, int value) {
        try {
            SharedPreferences.Editor sharedata = mActivity.getSharedPreferences("floatLogo", 0).edit();
            sharedata.putInt(key, value);
            sharedata.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dp 转 px
     */
    private int dp2Px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 启动贴边任务
     */
    private void startShrinkTask() {
        Log.d(TAG, "startShrinkTask: 启动贴边任务，" + mAutoShrinkDelay + "ms后贴边");
        shrinkHandler.removeCallbacks(shrinkRunnable);
        shrinkHandler.postDelayed(shrinkRunnable, mAutoShrinkDelay);
    }

    /**
     * 取消贴边任务
     */
    private void cancelShrinkTask() {
        Log.d(TAG, "cancelShrinkTask: 取消贴边任务");
        shrinkHandler.removeCallbacks(shrinkRunnable);
        if (shrinkAnimator != null && shrinkAnimator.isRunning()) {
            shrinkAnimator.cancel();
        }
    }

    /**
     * 贴边到屏幕边缘
     */
    private void shrinkToEdge() {
        Log.d(TAG, "shrinkToEdge: 开始贴边, mFloatLogo=" + mFloatLogo + ", isExpanded=" + isExpanded + ", isDrag=" + isDrag);

        if (mFloatLogo == null || isExpanded || isDrag) {
            Log.d(TAG, "shrinkToEdge: 贴边取消，原因：mFloatLogo=" + (mFloatLogo != null) + ", isExpanded=" + isExpanded + ", isDrag=" + isDrag);
            return;
        }

        // 强制测量并获取logo的宽度
        mFloatLogo.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int logoWidth = mFloatLogo.getMeasuredWidth();
        Log.d(TAG, "shrinkToEdge: getMeasuredWidth()=" + logoWidth + ", getWidth()=" + mFloatLogo.getWidth());

        if (logoWidth <= 0) {
            logoWidth = dp2Px(50, mActivity);
            Log.d(TAG, "shrinkToEdge: 使用默认宽度 dp2Px(50)=" + logoWidth);
        }

        // 计算目标位置：让logo的一半超出屏幕，一半在屏幕内
        final int targetX;
        int visiblePart = logoWidth / 2;  // 50%可见
        int hiddenPart = logoWidth - visiblePart;  // 50%隐藏

        if (mHintLocation == LEFT) {
            // 贴到左边：logo的一半超出屏幕左侧
            targetX = -hiddenPart;
            Log.d(TAG, "shrinkToEdge: 左贴边, logoWidth=" + logoWidth + ", 可见部分=" + visiblePart + ", 隐藏部分=" + hiddenPart + ", targetX=" + targetX);
        } else {
            // 贴到右边：logo的一半超出屏幕右侧
            targetX = mScreenWidth - visiblePart;
            Log.d(TAG, "shrinkToEdge: 右贴边, logoWidth=" + logoWidth + ", 可见部分=" + visiblePart + ", 隐藏部分=" + hiddenPart + ", targetX=" + targetX);
        }

        Log.d(TAG, "shrinkToEdge: mScreenWidth=" + mScreenWidth + ", 当前X=" + wmParams.x + ", 目标X=" + targetX + ", mHintLocation=" + mHintLocation);

        // 动画：移动到边缘，logo保持完整大小，大部分在屏幕外
        shrinkAnimator = ValueAnimator.ofInt(wmParams.x, targetX);
        shrinkAnimator.setDuration(300);
        shrinkAnimator.setInterpolator(new DecelerateInterpolator());
        shrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                wmParams.x = animatedValue;
                try {
                    wManager.updateViewLayout(mFloatLogo, wmParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        shrinkAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "shrinkToEdge: 贴边完成，最终位置X=" + wmParams.x + ", isShrunk=true");
                isShrunk = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        shrinkAnimator.start();
    }

    /**
     * 从贴边状态恢复（同步版本，始终使用Gravity.LEFT）
     */
    private void restoreFromShrink() {
        if (!isShrunk || mFloatLogo == null) {
            Log.d(TAG, "restoreFromShrink: 跳过恢复，isShrunk=" + isShrunk);
            return;
        }

        int logoWidth = mFloatLogo.getWidth();
        if (logoWidth <= 0) {
            logoWidth = dp2Px(50, mActivity);
        }

        // 根据mHintLocation恢复到正确的绝对坐标位置
        if (mHintLocation == RIGHT) {
            wmParams.x = mScreenWidth - logoWidth;  // 右侧：距离左边缘（屏幕宽度 - logo宽度）
            Log.d(TAG, "restoreFromShrink: 恢复到右侧，x=" + wmParams.x + "（距左边缘）");
        } else {
            wmParams.x = 0;  // 左侧：距离左边缘0px
            Log.d(TAG, "restoreFromShrink: 恢复到左侧，x=" + wmParams.x + "（距左边缘）");
        }

        // 更新布局（始终使用Gravity.LEFT）
        try {
            wManager.updateViewLayout(mFloatLogo, wmParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 重置贴边状态
        isShrunk = false;
        Log.d(TAG, "restoreFromShrink: 恢复完成，isShrunk设置为false，启动贴边任务");

        // 恢复完成后启动贴边任务，3秒后自动贴边
        startShrinkTask();
    }

    /**
     * 打开菜单
     */
    public void show() {
        try {
            if (wManager != null && wmParams != null && mFloatLogo != null) {
                wManager.addView(mFloatLogo, wmParams);
            }
            if (mHideTimer != null) {
                mHideTimer.start();
            } else {
                initTimer();
                mHideTimer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭菜单
     */
    public void hide() {
        destroyFloat();
    }

    /**
     * 移除所有悬浮窗
     */
    public void destroyFloat() {
        saveSetting(LOCATION_X, mHintLocation);
        saveSetting(LOCATION_Y, wmParams.y);
        mFloatLogo.clearAnimation();
        try {
            mHideTimer.cancel();
            // 取消贴边任务并清理Handler
            cancelShrinkTask();
            shrinkHandler.removeCallbacksAndMessages(null);

            if (isExpanded) {
                wManager.removeViewImmediate(mHintLocation == LEFT ? rootViewLeft : rootViewRight);
            } else {
                wManager.removeViewImmediate(mFloatLogo);
            }
            isExpanded = false;
            isDrag = false;
            isShrunk = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态设置logo是否显示红点数字
     * @param drawNum true-显示红点数字，false-不显示
     */
    public void setLogoDrawNum(boolean drawNum) {
        mDrawRedPointNum = drawNum;
        if (mFloatLogo != null) {
            mFloatLogo.setDrawNum(drawNum);
        }
        // 注意：菜单中的红点需要通过 setFloatItemList() 方法更新
    }

    /**
     * 动态设置logo是否绘制圆形背景
     * @param drawBg true-绘制圆形背景，false-不绘制
     */
    public void setLogoDrawBg(boolean drawBg) {
        mCircleMenuBg = drawBg;
        if (mFloatLogo != null) {
            mFloatLogo.setDrawDarkBg(drawBg);
        }
    }

    /**
     * 构建器
     */
    public static final class Builder {
        private int mBackMenuColor;
        private boolean mDrawRedPointNum;
        private boolean mCircleMenuBg;
        private Bitmap mLogoRes;
        private int mDefaultLocation;
        private List<FloatItem> mFloatItems = new ArrayList<>();
        private Context mActivity;
        private FloatMenuView.OnMenuClickListener mOnMenuClickListener;
        private Drawable mDrawable;
        private int mAutoShrinkDelay = 3000;  // 默认3秒

        public Builder() {
        }

        public Builder setBgDrawable(Drawable drawable) {
            mDrawable = drawable;
            return this;
        }

        public Builder setFloatItems(List<FloatItem> mFloatItems) {
            this.mFloatItems = mFloatItems;
            return this;
        }

        public Builder addFloatItem(FloatItem floatItem) {
            this.mFloatItems.add(floatItem);
            return this;
        }

        public Builder backMenuColor(int val) {
            mBackMenuColor = val;
            return this;
        }

        public Builder drawRedPointNum(boolean val) {
            mDrawRedPointNum = val;
            return this;
        }

        public Builder drawCicleMenuBg(boolean val) {
            mCircleMenuBg = val;
            return this;
        }

        public Builder logo(Bitmap val) {
            mLogoRes = val;
            return this;
        }

        public Builder withActivity(Activity val) {
            mActivity = val;
            return this;
        }

        public Builder withContext(Context val) {
            mActivity = val;
            return this;
        }

        public Builder setOnMenuItemClickListener(FloatMenuView.OnMenuClickListener val) {
            mOnMenuClickListener = val;
            return this;
        }

        public Builder defaultLocation(int val) {
            mDefaultLocation = val;
            return this;
        }

        public Builder autoShrinkDelay(int milliseconds) {
            this.mAutoShrinkDelay = milliseconds;
            return this;
        }

        /**
         * 获取Context（供内部API使用）
         */
        Context getContext() {
            return mActivity;
        }

        public FloatLogoMenu showWithListener(FloatMenuView.OnMenuClickListener val) {
            mOnMenuClickListener = val;
            return new FloatLogoMenu(this);
        }

        public FloatLogoMenu showWithLogo(Bitmap val) {
            mLogoRes = val;
            return new FloatLogoMenu(this);
        }

        public FloatLogoMenu show() {
            return new FloatLogoMenu(this);
        }
    }
}
