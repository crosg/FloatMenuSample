package com.yw.game.floatmenu;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * 菜单项数据模型
 * <p>
 * 表示悬浮菜单中的单个菜单项，包含标题、图标、颜色等信息
 * </p>
 */
public class FloatItem {

    public String title;
    public int titleColor = Color.BLACK;
    public int bgColor = Color.WHITE;
    public Bitmap icon;
    public String dotNum;

    /**
     * 构造函数 - 仅标题
     *
     * @param title 菜单项标题
     */
    public FloatItem(String title) {
        this.title = title;
    }

    /**
     * 构造函数 - 标题 + 颜色 + 图标 + 红点数字
     *
     * @param title         菜单项标题
     * @param titleColor   标题颜色
     * @param bgColor      背景颜色
     * @param icon         图标Bitmap
     * @param dotNum       红点数字（null表示不显示）
     */
    public FloatItem(String title, int titleColor, int bgColor, Bitmap icon, String dotNum) {
        this.title = title;
        this.titleColor = titleColor;
        this.bgColor = bgColor;
        this.icon = icon;
        this.dotNum = dotNum;
    }

    /**
     * 构造函数 - 标题 + 颜色 + 图标
     *
     * @param title         菜单项标题
     * @param titleColor   标题颜色
     * @param bgColor      背景颜色
     * @param icon         图标Bitmap
     */
    public FloatItem(String title, int titleColor, int bgColor, Bitmap icon) {
        this.title = title;
        this.titleColor = titleColor;
        this.bgColor = bgColor;
        this.icon = icon;
    }

    // ==================== Getters ====================

    /**
     * 获取菜单项标题
     *
     * @return 标题文本
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置菜单项标题
     *
     * @param title 标题文本
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取标题颜色
     *
     * @return 标题颜色
     */
    public int getTitleColor() {
        return titleColor;
    }

    /**
     * 设置标题颜色
     *
     * @param titleColor 标题颜色
     */
    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * 获取背景颜色
     *
     * @return 背景颜色
     */
    public int getBgColor() {
        return bgColor;
    }

    /**
     * 设置背景颜色
     *
     * @param bgColor 背景颜色
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * 获取图标
     *
     * @return 图标Bitmap（可能为null）
     */
    public Bitmap getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标Bitmap
     */
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    /**
     * 获取红点数字
     *
     * @return 红点数字（null表示不显示）
     */
    public String getDotNum() {
        return dotNum;
    }

    // ==================== Equals & HashCode ====================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FloatItem other = (FloatItem) obj;
        return title != null && title.equals(other.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FloatItem{" +
                "title='" + title + '\'' +
                ", titleColor=" + titleColor +
                ", bgColor=" + bgColor +
                ", icon=" + icon +
                ", dotNum='" + dotNum + '\'' +
                '}';
    }
}
