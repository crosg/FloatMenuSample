package com.yw.game.floatmenu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 项目名称：qdgamesdkplugin.demo
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：2016/4/29 16:05
 * 修改人：wengyiming
 * 修改时间：2016/4/29 16:05
 * 修改备注：
 */
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
    private static int mTextSize = 14;

    public MenuItemView(Context context, MenuItem menuItem) {
        super(context);
        this.mMenuItem = menuItem;
        init(context);
    }

    private void init(Context context) {
        Resources resources = getResources();
        mBtn = new ImageView(context);
        LayoutParams btnLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnLp.gravity = Gravity.CENTER;
        btnLp.bottomMargin = Utils.dp2Px(mGapSize, context);
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
