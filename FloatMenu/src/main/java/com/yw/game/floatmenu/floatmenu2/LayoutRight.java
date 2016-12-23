package com.yw.game.floatmenu.floatmenu2;

import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yw.game.floatmenu.R;
import com.yw.game.floatmenu.Utils;

/**
 * 项目名称：FloatMenuSample
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：2016/12/23 10:14
 * 修改人：wengyiming
 * 修改时间：2016/12/23 10:14
 * 修改备注：
 */

public class LayoutRight extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private LinearLayout menu;
    private FrameLayout frameLayout;

    private int screenW;
    private float xDownInScreen;
    private float yDownInScreen;
    private boolean mDraging;

    private boolean isExpand;


    public LayoutRight(Context context) {
        super(context);
        screenW = Utils.getScreenWidth(getContext());
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        View.inflate(context, R.layout.layout_yw_float_menu_new_right, this);
        menu = (LinearLayout) findViewById(R.id.ywGameMenuLine);
        frameLayout = (FrameLayout) findViewById(R.id.ywGameFra);
        frameLayout.setOnClickListener(this);
        frameLayout.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDownInScreen = event.getX();
                yDownInScreen = event.getY();
                mDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                if (Math.abs(xDownInScreen - mMoveStartX) > 3
                        && Math.abs(yDownInScreen - mMoveStartY) > 3) {
                    mDraging = true;
                    mParams.x = (int) (x - xDownInScreen);
                    mParams.y = (int) (y - yDownInScreen);
                    windowManager.updateViewLayout(this, mParams);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                refresh();
                break;
        }
        return false;
    }

    private void refresh() {
        int location = 1;
        if (mParams.x >= Utils.getScreenWidth(getContext()) / 2) {
            mParams.x = Utils.getScreenWidth(getContext());
            location = 1;//右边
        } else if (mParams.x < Utils.getScreenWidth(getContext()) / 2) {
            mParams.x = 0;
            location = 2;//左边
        }

        if (location == 2) {
            FloatWindowsManager.removeRight();
            FloatWindowsManager.createLeftLayout(getContext());
        } else {
            FloatWindowsManager.removeLeft();
            windowManager.updateViewLayout(this, mParams);
        }
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }


    @Override
    public void onClick(View v) {
        if (mDraging) return;
        int id = v.getId();
        if (id == R.id.ywGameFra) {
            if (!isExpand) {
                menu.setVisibility(VISIBLE);
                int count = menu.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = menu.getChildAt((i));
                    doExpandAnimation(view, (count - 1 - i));
                }
            } else {
                int count = menu.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = menu.getChildAt((count - 1 - i));
                    doStrinkAnimation(view, (count - 1 - i));
                }
            }
        }
    }


    private void doStrinkAnimation(final View view, final int i) {
        AnimationSet animation = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, view.getWidth() * i, 0, 0);
        animation.addAnimation(translateAnimation);
        animation.setFillAfter(true);
        animation.setDuration(100);
        animation.setStartOffset(100 * i);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(INVISIBLE);
                isExpand = false;
                if (i == menu.getChildCount() - 1) {
                    menu.setVisibility(GONE);
                    frameLayout.setOnTouchListener(LayoutRight.this);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    private void doExpandAnimation(final View view, int i) {

        TranslateAnimation animation = new TranslateAnimation(screenW, 0, 0, 0);
        animation.setFillAfter(false);
        animation.setDuration(100);
        animation.setStartOffset(100 * i);
        animation.setInterpolator(new FastOutLinearInInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(VISIBLE);
                isExpand = true;
                frameLayout.setOnTouchListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }


}
