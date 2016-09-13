package com.yw.game.floatmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 项目名称：QDGameSDKPlugin
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：2016/8/31 17:33
 * 修改人：wengyiming
 * 修改时间：2016/8/31 17:33
 * 修改备注：
 */
@SuppressLint("ViewConstructor")
public class MenuItemView extends LinearLayout {
    private ImageView menuLogo;
    private TextView menuTitle;
    private View menuItemDivider;
    private MenuItem mMenuItem;


    public MenuItem getMenuItem() {
        return mMenuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        mMenuItem = menuItem;
    }


    public MenuItemView(Context context, MenuItem item) {
        super(context);
        this.mMenuItem = item;
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.layout_yw_menu_item, this);
        menuLogo = (ImageView) view.findViewById(R.id.menuLogoImg);
        menuTitle = (TextView) view.findViewById(R.id.menuItemTxt);
        menuItemDivider = view.findViewById(R.id.menuItemDivider);
        setBackgroundColor(Color.TRANSPARENT);
        setLayoutParams(Utils.createWrapParams());
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);


//        OvalShape ovalShape = new OvalShape();
//        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
//        shapeDrawable.getPaint().setColor(mMenuItem.getBgColor());
//        this.setBackgroundDrawable(shapeDrawable);
        menuLogo.setImageResource(mMenuItem.getIcon());
        menuLogo.setClickable(false);

        menuTitle.setText(mMenuItem.getLabel());
        menuTitle.setTextColor(context.getResources().getColor(mMenuItem.getTextColor()));

        if (mMenuItem.showDivider) {
            menuItemDivider.setVisibility(VISIBLE);
        } else {
            menuItemDivider.setVisibility(INVISIBLE);
        }


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewGroup parent = (ViewGroup) getParent();
                parent.startAnimation(Utils.getExitScaleAlphaAnimation(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                }));
            }
        });
        setOnClickListener(mMenuItem.getOnClickListener());
        view.bringToFront();
    }

    public void setImageView(@DrawableRes int drawableRes) {
        menuLogo.setImageResource(drawableRes);
    }


    public void setDividerGone(boolean dividerGone){
        menuItemDivider.setVisibility(dividerGone?GONE:VISIBLE);
    }
}
