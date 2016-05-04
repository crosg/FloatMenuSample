/*
 *
 *  Copyright (c) 2015,2016, yuewen and/or its affiliates. All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *  This code is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License version 3 only, as
 *  published by the Free Software Foundation.  yuewen designates this
 *  particular file as subject to the "Classpath" exception as provided
 *  by yuewen in the LICENSE file that accompanied this code.
 *
 *  This code is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  version 3 for more details (a copy is included in the LICENSE file that
 *  * accompanied this code).
 *
 *  You should have received a copy of the GNU General Public License version
 *  * 3 along with this work; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *  Please contact yuewen, then mailto opensource@yuewen.com
 *  if you need additional information or have any questions.
 * /
 */

package com.yw.game.floatmenu;


import android.content.Context;
import android.support.annotation.DimenRes;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
