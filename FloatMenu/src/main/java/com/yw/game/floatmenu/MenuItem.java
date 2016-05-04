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

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

public class MenuItem {
    @ColorRes
    private int bgColor;
    @DrawableRes
    private int icon;
    private String label;
    @ColorRes
    private int textColor = android.R.color.white;
    private int diameter = 50;
    private View.OnClickListener onClickListener;

    public MenuItem(int bgColor, int icon, String label, int textColor, View.OnClickListener onClickListener) {
        this.bgColor = bgColor;
        this.icon = icon;
        this.label = label;
        this.textColor = textColor;
        this.onClickListener = onClickListener;
    }

    public MenuItem(int bgColor, int icon, String label, int textColor, int diameter, View.OnClickListener onClickListener) {
        this.bgColor = bgColor;
        this.icon = icon;
        this.label = label;
        this.textColor = textColor;
        this.diameter = diameter;
        this.onClickListener = onClickListener;
    }

    public MenuItem(int bgColor) {
        this.bgColor = bgColor;
    }

    public MenuItem(int bgColor, int icon, String label) {
        this.bgColor = bgColor;
        this.icon = icon;
        this.label = label;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    @DrawableRes
    public int getIcon() {
        return  icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
