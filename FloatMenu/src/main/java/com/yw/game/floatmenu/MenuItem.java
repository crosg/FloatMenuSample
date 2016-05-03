package com.yw.game.floatmenu;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * 项目名称：qdgamesdkplugin.demo
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：2016/4/29 15:18
 * 修改人：wengyiming
 * 修改时间：2016/4/29 15:18
 * 修改备注：
 */
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

    public MenuItem(int bgColor, int icon, String label, int textColor,View.OnClickListener onClickListener) {
        this.bgColor = bgColor;
        this.icon = icon;
        this.label = label;
        this.textColor = textColor;
        this.onClickListener = onClickListener;
    }

    public MenuItem(int bgColor, int icon, String label, int textColor, int diameter,View.OnClickListener onClickListener) {
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
