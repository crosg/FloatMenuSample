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
import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.DrawableRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuItemView extends LinearLayout implements OnMenuActionListener {
    private static final String TAG = "MIV";
    private ImageView mBtn;
    private TextView mLabel;

    public MenuItem getMenuItem() {
        return mMenuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        mMenuItem = menuItem;
    }

    private MenuItem mMenuItem;
    private static int mGapSize = 4;
    private static int mTextSize = 12;

    public MenuItemView(Context context, MenuItem menuItem) {
        super(context);
        this.mMenuItem = menuItem;
        init(context);
    }

    public void setImageView(@DrawableRes int drawableRes){
        mBtn.setImageResource(drawableRes);
    }

    private void init(Context context) {
        Resources resources = getResources();
        mBtn = new ImageView(context);
        LayoutParams btnLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnLp.gravity = Gravity.CENTER;
        btnLp.leftMargin = Utils.dp2Px(mGapSize, context);
        btnLp.rightMargin = Utils.dp2Px(mGapSize, context);
        btnLp.bottomMargin = Utils.dp2Px(1,context);
        mBtn.setLayoutParams(btnLp);
        mBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(resources.getColor(mMenuItem.getBgColor()));
        mBtn.setBackgroundDrawable(shapeDrawable);
        mBtn.setImageResource(mMenuItem.getIcon());
        mBtn.setClickable(false);
        addView(mBtn);
        mLabel = new TextView(context);
        LayoutParams labelLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        labelLp.gravity = Gravity.CENTER;
        mLabel.setLayoutParams(labelLp);
        mLabel.setText(mMenuItem.getLabel());
        mLabel.setTextColor(resources.getColor(mMenuItem.getTextColor()));
        mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        addView(mLabel);

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                applyPressAnimation();
                ViewGroup parent = (ViewGroup) getParent();
                parent.setClipChildren(false);
                parent.setClipToPadding(false);
                setClipChildren(false);
                setClipToPadding(false);
            }
        });


    }


    @Override
    public void onMenuOpen() {

    }

    @Override
    public void onMenuClose() {

    }
}
