package com.yw.game.floatmenu.floatmenu2;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.yw.game.floatmenu.Utils;

/**
 * 项目名称：FloatMenuSample
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：2016/12/23 10:10
 * 修改人：wengyiming
 * 修改时间：2016/12/23 10:10
 * 修改备注：
 */

public class FloatWindowsManager {
    private static final String TAG = FloatWindowsManager.class.getSimpleName();
    private static WindowManager windowManager;
    private static WindowManager.LayoutParams mWmParams;

    private static LayoutLeft layoutLeft;
    private static LayoutRight layoutRight;


    public static void Log(String log) {
        Log.e(TAG, log);
    }


    public static void createLeftLayout(Context context) {
        Log("createLeftLayout");
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获取系统的窗口服务
        if (layoutLeft == null) {
            layoutLeft = new LayoutLeft(context);
            if (mWmParams == null) {
                mWmParams = new WindowManager.LayoutParams();//获取窗口参数
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
                mWmParams.y = Utils.getScreenHeight(context) / 10;

                //宽度指定为内容自适应
                mWmParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
                mWmParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            }

        }
        layoutLeft.setParams(mWmParams);
        windowManager.addView(layoutLeft, mWmParams);

    }

    public static void createRightLayout(Context context) {
        Log("createRightLayout");
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获取系统的窗口服务
        if (layoutRight == null) {
            layoutRight = new LayoutRight(context);
            if (mWmParams == null) {
                mWmParams = new WindowManager.LayoutParams();//获取窗口参数
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
                mWmParams.x = Utils.getScreenWidth(context);

                //默认指定的纵坐标为屏幕高度的10分之一，这里只是大概约束，因为上的flags参数限制，悬浮球不会出现在屏幕外
                mWmParams.y = Utils.getScreenHeight(context) / 10;

                //宽度指定为内容自适应
                mWmParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
                mWmParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            }

        }
        layoutRight.setParams(mWmParams);
        windowManager.addView(layoutRight, mWmParams);
    }


    public static void removeLeft() {
        Log("removeLeft");
        if (layoutLeft != null) {
            windowManager.removeView(layoutLeft);
        }
        layoutLeft = null;
    }

    public static void removeRight() {
        Log("removeRight");
        if (layoutRight != null) {
            windowManager.removeView(layoutRight);
        }
        layoutRight = null;
    }

}
