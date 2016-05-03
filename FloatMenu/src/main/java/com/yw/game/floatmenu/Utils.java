package com.yw.game.floatmenu;


import android.content.Context;
import android.support.annotation.DimenRes;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 项目名称：qdgamesdkplugin.demo
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：2016/4/29 16:05
 * 修改人：wengyiming
 * 修改时间：2016/4/29 16:05
 * 修改备注：
 */
public class Utils {

    private static final String TAG = "Utils";

    public static int getDimension(Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    public static int px2dp(float pxVal, Context mContext) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pxVal, mContext.getResources().getDisplayMetrics()));
    }

    public static int dp2Px(float dp, Context mContext) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                mContext.getResources().getDisplayMetrics());
    }

    public static final FrameLayout.LayoutParams createLayoutParams(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }

    public static final FrameLayout.LayoutParams createMatchParams() {
        return createLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static final FrameLayout.LayoutParams createWrapParams() {
        return createLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static final FrameLayout.LayoutParams createWrapMatchParams() {
        return createLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static final FrameLayout.LayoutParams createMatchWrapParams() {
        return createLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

//    public static void setInsets(Activity context, View view) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
//        SystemBarTintManager tintManager = new SystemBarTintManager(context);
//        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//        view.setPadding(0, config.getPixelInsetTop(false), config.getPixelInsetRight(), config.getPixelInsetBottom());
//    }
//
//    public static int getInsetsTop(Activity context, View view) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return 0;
//        SystemBarTintManager tintManager = new SystemBarTintManager(context);
//        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//        return config.getPixelInsetTop(false);
//    }
//
//    public static int getInsetsBottom(Activity context, View view) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return 0;
//        SystemBarTintManager tintManager = new SystemBarTintManager(context);
//        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//        return config.getPixelInsetBottom();
//    }
}
