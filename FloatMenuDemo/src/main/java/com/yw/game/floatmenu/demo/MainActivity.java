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

package com.yw.game.floatmenu.demo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yw.game.floatmenu.FloatItem;
import com.yw.game.floatmenu.FloatLogoMenu;
import com.yw.game.floatmenu.FloatMenu;
import com.yw.game.floatmenu.FloatMenuView;
import com.yw.game.floatmenu.customfloat.BaseFloatDialog;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_FLOAT_MENU_PERMISSION = 1001;

    FloatLogoMenu mFloatMenu;
    FloatLogoMenu mFloatMenu1;
    ArrayList<FloatItem> itemList = new ArrayList<>();
    private Activity mActivity;

    String HOME = "首页";
    String FEEDBACK = "客服";
    String MESSAGE = "消息";

    String[] MENU_ITEMS = {HOME, FEEDBACK, MESSAGE};

    private int[] menuIcons = new int[]{R.drawable.yw_menu_account, R.drawable.yw_menu_fb, R.drawable.yw_menu_msg};

    BaseFloatDialog mBaseFloatDialog;
    private Handler refreshDotHandler = new Handler(Looper.getMainLooper());
    private Runnable refreshDotRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mActivity = this;
        for (int i = 0; i < menuIcons.length; i++) {
            itemList.add(new FloatItem(MENU_ITEMS[i], 0x99000000, 0x99000000, BitmapFactory.decodeResource(this.getResources(), menuIcons[i]), String.valueOf(i + 1)));
        }

        // 设置Service控制按钮
        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnStopService = findViewById(R.id.btnStopService);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloatMenuService();
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopFloatMenuService();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mFloatMenu == null) {
            // 使用新的简化API创建悬浮菜单
            mFloatMenu = FloatMenu.create(this)
                    .logo(R.drawable.yw_game_logo)
                    .items(itemList)
                    .listener(new FloatMenuView.OnMenuClickListener() {
                        @Override
                        public void onItemClick(int position, String title) {
                            Toast.makeText(MainActivity.this, "📱 Activity菜单 - position " + position + " title:" + title, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void dismiss() {

                        }
                    })
                    .show();

            // 使用成员变量保存 Runnable，以便取消
            refreshDotRunnable = new Runnable() {
                @Override
                public void run() {
                    refreshDot();
                }
            };
            refreshDotHandler.postDelayed(refreshDotRunnable, 5000);
        }


        if (mBaseFloatDialog != null) return;

        mBaseFloatDialog = new MyFloatDialog(this);
//         mBaseFloatDialog.show();

    }

    private void showWithCallback() {
        mBaseFloatDialog = new BaseFloatDialog.FloatDialogImp(this, new BaseFloatDialog.GetViewCallback() {
            @Override
            public View getLeftView(LayoutInflater inflater, View.OnTouchListener touchListener) {
                LinearLayout linearLayout = new LinearLayout(mActivity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);

                TextView textView = new TextView(mActivity);
                textView.setText("左边");

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "左边的菜单被点击了", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageView imageView = new ImageView(mActivity);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                imageView.setImageResource(R.drawable.yw_game_logo);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setOnTouchListener(touchListener);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                linearLayout.setBackgroundResource(R.drawable.yw_game_float_menu_bg);
                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                return linearLayout;
            }

            @Override
            public View getRightView(LayoutInflater inflater, View.OnTouchListener touchListener) {
                LinearLayout linearLayout = new LinearLayout(mActivity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                TextView textView = new TextView(mActivity);
                textView.setText("右边");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "右边的菜单被点击了", Toast.LENGTH_SHORT).show();
                    }
                });

                ImageView imageView = new ImageView(mActivity);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                imageView.setImageResource(R.drawable.yw_game_logo);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setOnTouchListener(touchListener);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                linearLayout.setBackgroundResource(R.drawable.yw_game_float_menu_bg);
                linearLayout.addView(textView);
                linearLayout.addView(imageView);
                return linearLayout;
            }

            @Override
            public View getLogoView(LayoutInflater inflater) {


                LinearLayout linearLayout = new LinearLayout(mActivity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);

                ImageView imageView = new ImageView(mActivity);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageResource(R.drawable.yw_game_logo);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                linearLayout.setBackgroundResource(R.drawable.yw_game_float_menu_bg);

                linearLayout.addView(imageView);
                return linearLayout;
            }

            @Override
            public void resetLogoViewSize(int hintLocation, View logoView) {
                logoView.clearAnimation();
                logoView.setTranslationX(0);
                logoView.setScaleX(1);
                logoView.setScaleY(1);
            }

            @Override
            public void dragingLogoViewOffset(final View smallView, boolean isDraging, boolean isResetPosition, float offset) {
                if (isDraging && offset > 0) {
                    smallView.setBackgroundDrawable(null);
                    smallView.setScaleX(1 + offset);
                    smallView.setScaleY(1 + offset);
                } else {
                    smallView.setBackgroundResource(R.drawable.yw_game_float_menu_bg);
                    smallView.setTranslationX(0);
                    smallView.setScaleX(1);
                    smallView.setScaleY(1);
                }


                if (isResetPosition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        smallView.setRotation(offset * 360);
                    }
                } else {
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 360 * 4);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int v = (int) animation.getAnimatedValue();
                            smallView.setRotation(v);
                        }
                    });
                    valueAnimator.setDuration(800);
                    valueAnimator.start();
                }
            }

            @Override
            public void shrinkLeftLogoView(View smallView) {
                smallView.setTranslationX(-smallView.getWidth() / 3);
            }

            @Override
            public void shrinkRightLogoView(View smallView) {
                smallView.setTranslationX(smallView.getWidth() / 3);
            }

            @Override
            public void leftViewOpened(View leftView) {
                Toast.makeText(mActivity, "左边的菜单被打开了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightViewOpened(View rightView) {
                Toast.makeText(mActivity, "右边的菜单被打开了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void leftOrRightViewClosed(View smallView) {
                Toast.makeText(mActivity, "菜单被关闭了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDestoryed() {

            }
        });
        mBaseFloatDialog.show();
    }


    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void refreshDot() {
        // 检查 mFloatMenu 是否为 null，避免 Service 启动后的崩溃
        if (mFloatMenu == null) {
            return;
        }

        for (FloatItem menuItem : itemList) {
            if (TextUtils.equals(menuItem.getTitle(), "我的")) {
                menuItem.dotNum = String.valueOf(8);
            }
        }
        mFloatMenu.setFloatItemList(itemList);
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideFloat();
    }

    @Override
    protected void onDestroy() {
        destroyFloat();
        super.onDestroy();

        if (mBaseFloatDialog != null) mBaseFloatDialog.dismiss();

    }


    public void hideFloat() {
        if (mFloatMenu != null) {
            mFloatMenu.hide();
        }
    }

    public void destroyFloat() {
        // 取消 refreshDot 延迟任务
        if (refreshDotRunnable != null) {
            refreshDotHandler.removeCallbacks(refreshDotRunnable);
            refreshDotRunnable = null;
        }

        if (mFloatMenu != null) {
            mFloatMenu.destroyFloat();
        }
        mFloatMenu = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FLOAT_MENU_PERMISSION) {
            // 用户从权限设置页面返回
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // 用户已授予权限，自动启动Service
                    Toast.makeText(this, "权限已授予，正在启动服务...", Toast.LENGTH_SHORT).show();
                    startFloatMenuService();
                } else {
                    // 用户拒绝了权限
                    Toast.makeText(this, "未授予悬浮窗权限，无法启动服务", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 启动悬浮菜单Service
     */
    private void startFloatMenuService() {
        // 检查悬浮窗权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // 没有权限，引导用户去设置页面开启
                Toast.makeText(this, "请先开启悬浮窗权限", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_FLOAT_MENU_PERMISSION);
                return;
            }
        }

        // 启动 Service 前，先销毁 Activity 的悬浮球，避免冲突
        destroyFloat();

        Intent intent = new Intent(this, FloatMenuService.class);
        startService(intent);
        Toast.makeText(this, "悬浮菜单服务已启动\n（Activity悬浮球已销毁）", Toast.LENGTH_SHORT).show();

        // 更新按钮状态
        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnStopService = findViewById(R.id.btnStopService);
        btnStartService.setEnabled(false);
        btnStopService.setEnabled(true);
    }

    /**
     * 停止悬浮菜单Service
     */
    private void stopFloatMenuService() {
        Intent intent = new Intent(this, FloatMenuService.class);
        stopService(intent);
        Toast.makeText(this, "悬浮菜单服务已停止\n（Activity悬浮球将恢复）", Toast.LENGTH_SHORT).show();

        // 更新按钮状态
        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnStopService = findViewById(R.id.btnStopService);
        btnStartService.setEnabled(true);
        btnStopService.setEnabled(false);

        // 延迟恢复 Activity 的悬浮球，避免与服务冲突
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 重新创建 Activity 的悬浮球
                mFloatMenu = null;
                // onResume() 会自动创建悬浮球，但我们需要触发 onResume
                // 或者直接在这里创建
                createActivityFloatMenu();
            }
        }, 500);
    }

    /**
     * 创建 Activity 的悬浮菜单（使用新的简化API）
     */
    private void createActivityFloatMenu() {
        if (mFloatMenu != null) {
            return;  // 已经存在，不需要重复创建
        }

        if (isFinishing()) {
            return;  // Activity正在销毁，不创建悬浮球
        }

        // 使用新的简化API
        mFloatMenu = FloatMenu.create(this)
                .logo(R.drawable.yw_game_logo)
                .items(itemList)
                .listener(new FloatMenuView.OnMenuClickListener() {
                    @Override
                    public void onItemClick(int position, String title) {
                        Toast.makeText(MainActivity.this, "📱 Activity菜单 - position " + position + " title:" + title, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void dismiss() {

                    }
                })
                .show();

        // 重新创建 refreshDot 延迟任务
        refreshDotRunnable = new Runnable() {
            @Override
            public void run() {
                refreshDot();
            }
        };
        refreshDotHandler.postDelayed(refreshDotRunnable, 5000);
    }


}
