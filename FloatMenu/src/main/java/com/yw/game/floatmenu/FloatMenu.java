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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * FloatMenu - 简化的悬浮菜单API入口
 * <p>
 * 使用示例：
 * <pre>
 * // 极简用法
 * FloatMenu.create(activity)
 *     .logo(R.drawable.logo)
 *     .items(itemList)
 *     .show();
 *
 * // 高级配置
 * FloatMenu.create(context)
 *     .logo(R.drawable.logo)
 *     .items(itemList)
 *     .location(FloatMenu.LEFT)
 *     .autoShrink(5000)
 *     .showRedDot(true)
 *     .backgroundColor(0xFF4CAF50)
 *     .listener(clickListener)
 *     .show();
 * </pre>
 * </p>
 */
public class FloatMenu {

    public static final int LEFT = FloatLogoMenu.LEFT;
    public static final int RIGHT = FloatLogoMenu.RIGHT;

    /**
     * 创建FloatMenu构建器
     *
     * @param activity Activity上下文（推荐用于应用内悬浮）
     * @return Builder实例
     */
    public static Builder create(Activity activity) {
        return new Builder(activity);
    }

    /**
     * 创建FloatMenu构建器
     *
     * @param context 上下文（用于Service或应用外悬浮）
     * @return Builder实例
     */
    public static Builder create(Context context) {
        return new Builder(context);
    }

    /**
     * FloatMenu构建器 - 简化的API
     */
    public static class Builder {
        private FloatLogoMenu.Builder builder;

        private Builder(Activity activity) {
            this.builder = new FloatLogoMenu.Builder().withActivity(activity);
        }

        private Builder(Context context) {
            this.builder = new FloatLogoMenu.Builder().withContext(context);
        }

        /**
         * 设置logo图标（资源ID）
         *
         * @param resId 图标资源ID
         * @return Builder
         */
        public Builder logo(int resId) {
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            builder.logo(android.graphics.BitmapFactory.decodeResource(
                    builder.getContext().getResources(), resId, options));
            return this;
        }

        /**
         * 设置logo图标（Bitmap）
         *
         * @param bitmap 图标Bitmap
         * @return Builder
         */
        public Builder logo(Bitmap bitmap) {
            builder.logo(bitmap);
            return this;
        }

        /**
         * 设置菜单项列表
         *
         * @param items 菜单项列表
         * @return Builder
         */
        public Builder items(List<FloatItem> items) {
            builder.setFloatItems(items);
            return this;
        }

        /**
         * 设置停靠位置（默认：RIGHT）
         *
         * @param location LEFT 或 RIGHT
         * @return Builder
         */
        public Builder location(int location) {
            builder.defaultLocation(location);
            return this;
        }

        /**
         * 设置自动贴边延时时间（默认：3000ms）
         *
         * @param milliseconds 延时时间（毫秒），0表示不自动贴边
         * @return Builder
         */
        public Builder autoShrink(int milliseconds) {
            builder.autoShrinkDelay(milliseconds);
            return this;
        }

        /**
         * 是否显示红点数字（默认：false）
         *
         * @param show true显示，false不显示
         * @return Builder
         */
        public Builder showRedDot(boolean show) {
            builder.drawRedPointNum(show);
            return this;
        }

        /**
         * 是否绘制圆形菜单背景（默认：true）
         *
         * @param draw true绘制，false不绘制
         * @return Builder
         */
        public Builder drawCircleBg(boolean draw) {
            builder.drawCicleMenuBg(draw);
            return this;
        }

        /**
         * 设置背景颜色
         *
         * @param color 背景颜色值
         * @return Builder
         */
        public Builder backgroundColor(int color) {
            builder.backMenuColor(color);
            return this;
        }

        /**
         * 设置背景Drawable
         *
         * @param drawable 背景Drawable
         * @return Builder
         */
        public Builder backgroundDrawable(Drawable drawable) {
            builder.setBgDrawable(drawable);
            return this;
        }

        /**
         * 设置菜单点击监听
         *
         * @param listener 点击监听器
         * @return Builder
         */
        public Builder listener(FloatMenuView.OnMenuClickListener listener) {
            this.listener = listener;
            return this;
        }

        private FloatMenuView.OnMenuClickListener listener;

        /**
         * 显示悬浮菜单（使用默认配置，无监听）
         *
         * @return FloatLogoMenu实例
         */
        public FloatLogoMenu show() {
            if (listener != null) {
                return builder.showWithListener(listener);
            } else {
                return builder.show();
            }
        }
    }
}
