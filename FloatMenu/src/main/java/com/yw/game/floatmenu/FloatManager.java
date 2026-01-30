/*
 * Copyright (c) 2026, Shanghai YUEWEN Information Technology Co., Ltd.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of Shanghai YUEWEN Information Technology Co., Ltd. nor the names of its contributors may be used to avoid the use of this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY SHANGHAI YUEWEN INFORMATION TECHNOLOGY CO., LTD. AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.yw.game.floatmenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.yw.game.floatmenu.customfloat.BaseFloatDialog;

/**
 * 悬浮窗全局管理器
 * <p>
 * 提供单例模式的悬浮窗管理，支持位置保存/加载和全局访问
 * </p>
 */
public class FloatManager {
    private static final String TAG = "FloatManager";
    private static final String PREF_NAME = "float_manager";
    private static final String KEY_FLOAT_X = "float_x";
    private static final String KEY_FLOAT_Y = "float_y";
    private static final String KEY_FLOAT_HINT = "float_hint";

    private static volatile FloatManager instance = null;
    private static final Object instanceLock = new Object();

    private BaseFloatDialog currentFloatDialog = null;
    private final Context context;
    private final SharedPreferences prefs;

    /**
     * 私有构造函数
     *
     * @param context ApplicationContext
     */
    private FloatManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 初始化全局实例
     * <p>
     * 建议在 Application.onCreate() 中调用
     * </p>
     *
     * @param context ApplicationContext
     */
    public static void init(Context context) {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new FloatManager(context);
                    Log.i(TAG, "FloatManager initialized");
                }
            }
        }
    }

    /**
     * 获取全局实例
     *
     * @return FloatManager 实例
     * @throws IllegalStateException 如果未先调用 init()
     */
    public static FloatManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                "FloatManager must be initialized first. Call init() in Application.onCreate()");
        }
        return instance;
    }

    /**
     * 检查是否已初始化
     *
     * @return true 表示已初始化，false 表示未初始化
     */
    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * 设置当前悬浮窗实例
     *
     * @param dialog 悬浮窗实例
     */
    public static synchronized void setCurrentFloatDialog(BaseFloatDialog dialog) {
        instance.currentFloatDialog = dialog;
    }

    /**
     * 获取当前悬浮窗实例
     *
     * @return 当前悬浮窗实例，可能为null
     */
    public static synchronized BaseFloatDialog getCurrentFloatDialog() {
        return instance.currentFloatDialog;
    }

    /**
     * 保存悬浮窗位置
     *
     * @param x X坐标
     * @param y Y坐标
     */
    public void saveFloatPosition(float x, float y) {
        try {
            prefs.edit()
                    .putFloat(KEY_FLOAT_X, x)
                    .putFloat(KEY_FLOAT_Y, y)
                    .apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save float position: x=" + x + ", y=" + y, e);
        }
    }

    /**
     * 加载悬浮窗位置
     *
     * @return 如果没有保存的位置返回null，否则返回 [x, y]
     */
    public Float[] loadFloatPosition() {
        try {
            float x = prefs.getFloat(KEY_FLOAT_X, Float.MIN_VALUE);
            float y = prefs.getFloat(KEY_FLOAT_Y, Float.MIN_VALUE);

            if (x == Float.MIN_VALUE || y == Float.MIN_VALUE) {
                return null;
            }

            return new Float[]{x, y};
        } catch (Exception e) {
            Log.e(TAG, "Failed to load float position", e);
            return null;
        }
    }

    /**
     * 保存悬浮窗停靠位置
     *
     * @param hint 停靠位置（LEFT=0, RIGHT=1）
     */
    public void saveFloatHint(int hint) {
        try {
            prefs.edit()
                    .putInt(KEY_FLOAT_HINT, hint)
                    .apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save float hint: " + hint, e);
        }
    }

    /**
     * 加载悬浮窗停靠位置
     *
     * @return 默认返回 RIGHT（1）
     */
    public int loadFloatHint() {
        try {
            return prefs.getInt(KEY_FLOAT_HINT, BaseFloatDialog.RIGHT);
        } catch (Exception e) {
            Log.e(TAG, "Failed to load float hint", e);
            return BaseFloatDialog.RIGHT;
        }
    }

    /**
     * 显示悬浮窗（公共API入口）
     *
     * @param context Context
     * @deprecated 推荐使用具体的 FloatDialog 子类，此方法保留为公共API入口
     */
    @Deprecated
    public static void showFloat(Context context) {
        try {
            getInstance().showFloatInternal(context);
        } catch (Exception e) {
            Log.e(TAG, "Failed to show float dialog", e);
        }
    }

    /**
     * 关闭悬浮窗（公共API入口）
     *
     * @param context Context
     */
    public static void dismissFloat(Context context) {
        try {
            getInstance().dismissFloatInternal();
        } catch (Exception e) {
            Log.e(TAG, "Failed to dismiss float dialog", e);
        }
    }

    /**
     * 打开菜单（公共API入口）
     *
     * @param context Context
     */
    public static void openMenu(Context context) {
        try {
            getInstance().openMenuInternal();
        } catch (Exception e) {
            Log.e(TAG, "Failed to open menu", e);
        }
    }

    /**
     * 内部实现 - 显示悬浮窗
     *
     * @param context Context
     */
    private void showFloatInternal(Context context) {
        if (currentFloatDialog == null) {
            // 注意：此方法需要根据实际使用场景实现
            // 这里保留空实现，由外部通过 BaseFloatDialog.show() 调用
            Log.w(TAG, "showFloatInternal called but no dialog instance is set");
        }
    }

    /**
     * 内部实现 - 关闭悬浮窗
     */
    private void dismissFloatInternal() {
        if (currentFloatDialog != null) {
            try {
                currentFloatDialog.dismiss();
                currentFloatDialog = null;
                Log.i(TAG, "Float dialog dismissed");
            } catch (Exception e) {
                Log.e(TAG, "Failed to dismiss float dialog", e);
            }
        }
    }

    /**
     * 内部实现 - 打开菜单
     */
    private void openMenuInternal() {
        if (currentFloatDialog != null) {
            try {
                currentFloatDialog.openMenu();
                Log.i(TAG, "Menu opened");
            } catch (Exception e) {
                Log.e(TAG, "Failed to open menu", e);
            }
        }
    }

    /**
     * 关闭所有悬浮窗
     */
    public void closeAllFloats() {
        dismissFloatInternal();
    }
}
