package com.yw.game.floatmenu;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

public class MenuItem {
    @DrawableRes
    private int icon;
    private String label;
    @ColorRes
    private int textColor = android.R.color.white;
    private int diameter = 50;

    public boolean showDivider = true;
    private View.OnClickListener onClickListener;

    public MenuItem(int icon, String label, int textColor, View.OnClickListener onClickListener) {
        this.icon = icon;
        this.label = label;
        this.textColor = textColor;
        this.onClickListener = onClickListener;
    }

    public MenuItem(int icon, String label, View.OnClickListener onClickListener, boolean showDivider, int textColor) {
        this.icon = icon;
        this.label = label;
        this.onClickListener = onClickListener;
        this.showDivider = showDivider;
        this.textColor = textColor;
    }

    public MenuItem(int icon, String label, boolean showDivider, int textColor, View.OnClickListener onClickListener) {
        this.icon = icon;
        this.label = label;
        this.showDivider = showDivider;
        this.textColor = textColor;
        this.onClickListener = onClickListener;
    }

    public MenuItem( int icon, String label, int textColor, int diameter, View.OnClickListener onClickListener) {
        this.icon = icon;
        this.label = label;
        this.textColor = textColor;
        this.diameter = diameter;
        this.onClickListener = onClickListener;
    }


    public MenuItem(int bgColor, int icon, String label) {
        this.icon = icon;
        this.label = label;
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
