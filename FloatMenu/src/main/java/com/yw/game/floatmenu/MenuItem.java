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
