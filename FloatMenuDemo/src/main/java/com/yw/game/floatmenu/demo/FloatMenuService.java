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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import com.yw.game.floatmenu.FloatMenu;
import com.yw.game.floatmenu.MenuItemView;
import com.yw.game.sclib.Sc;
import com.yw.game.sclib.ScCreateResultCallback;

/**
 * Created by wengyiming on 2015/12/20.
 */
public class FloatMenuService extends Service implements View.OnClickListener {
    private FloatMenu mFloatMenu;
    private final static String TAG = FloatMenuService.class.getSimpleName();

    private Handler mHandler = new Handler();

    /**
     * On bind binder.
     *
     * @param intent the intent
     * @return the binder
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new FloatMenuServiceBinder();
    }


    /**
     * On create.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mFloatMenu = new FloatMenu.Builder(this)
                .floatLoader(R.drawable.yw_anim_background)
                .floatLogo(R.drawable.yw_image_float_logo)
                .addMenuItem( R.drawable.yw_menu_account, Const.MENU_ITEMS[0], android.R.color.black, this)
                .addMenuItem(R.drawable.yw_menu_favour, Const.MENU_ITEMS[1], android.R.color.black, this)
                .addMenuItem( R.drawable.yw_menu_fb, Const.MENU_ITEMS[2], android.R.color.black, this)
                .addMenuItem(R.drawable.yw_menu_msg, Const.MENU_ITEMS[3], android.R.color.black, this)
                .addMenuItem(R.drawable.yw_menu_close, Const.MENU_ITEMS[4], android.R.color.black, this)
                .menuBackground(R.drawable.yw_menu_bg)
                .build();


        mFloatMenu.show();
    }

    /**
     * On click.
     *
     * @param v the v
     */
    @Override
    public void onClick(View v) {
        if (v instanceof MenuItemView) {
            MenuItemView menuItemView = (MenuItemView) v;
            String menuItemLabel = menuItemView.getMenuItem().getLabel();
            Toast.makeText(this, menuItemLabel, Toast.LENGTH_SHORT).show();
            switch (menuItemLabel) {
                case Const.HOME:
                    // TODO WHAT U WANT 此处模拟联网操作
                    mFloatMenu.startLoaderAnim();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mFloatMenu.stopLoaderAnim();
                                    goHomeIndex(FloatMenuService.this);
                                }
                            });
                        }
                    }).start();

                    break;
                case Const.FAVOUR:
                    createSc();
                    addCloseMenuItem(2);
                    break;
                case Const.FEEDBACK:
                    mFloatMenu.removeMenuItem(0);
                    break;
                case Const.MESSAGE:
                    if (hasNewMsg) {
                        hasNewMsg = false;
                    } else {
                        hasNewMsg = true;
                    }
                    showRed();

                    break;
                case Const.CLOSE:
                    hideFloat();
                    break;
            }
        }
    }

    private boolean hasNewMsg = false;

    private void showRed() {
        if (!hasNewMsg) {
            mFloatMenu.changeLogo(R.drawable.yw_image_float_logo, R.drawable.yw_menu_msg, 3);
        } else {
            mFloatMenu.changeLogo(R.drawable.yw_image_float_logo_red, R.drawable.yw_menu_msg_red, 3);
        }
    }

    public void addCloseMenuItem(int position) {
        if (mFloatMenu == null)
            return;
        mFloatMenu.addMenuItem(position, R.drawable.yw_menu_close, Const.MENU_ITEMS[4], android.R.color.black, this);
    }

    public void removeMenuItem() {
        if (mFloatMenu == null)
            return;
        int size = mFloatMenu.getMenuItems().size();
        if (size >= 6) {
            mFloatMenu.removeMenuItem(5);
        }
    }


    private void createSc() {
        //在service中的使用场景
        PackageManager pm = this.getPackageManager();
        ApplicationInfo appInfo = FloatMenuService.this.getApplicationInfo();
        Drawable drawable = appInfo.loadIcon(pm);//当前app的logo
        String name = appInfo.loadLabel(pm).toString();//当前app的名称
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);//当前app的入口程序
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        new Sc.Builder(this, intent).
                setName(name).
                setAllowRepeat(true).
                setIcon(drawable).
                setCallBack(new ScCreateResultCallback() {
                    @Override
                    public void createSuccessed(String createdOrUpdate, Object tag) {
                        Toast.makeText(FloatMenuService.this, createdOrUpdate, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void createError(String errorMsg, Object tag) {
                        Toast.makeText(FloatMenuService.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }).build().createSc();
    }


    /**
     * Show float.
     */
    public void showFloat() {
        if (mFloatMenu != null)
            mFloatMenu.show();
    }

    /**
     * Hide float.
     */
    public void hideFloat() {
        if (mFloatMenu != null) {
            mFloatMenu.hide();
        }
    }

    /**
     * Destroy float.
     */
    public void destroyFloat() {
        hideFloat();
        if (mFloatMenu != null) {
            mFloatMenu.destroy();
        }
        mFloatMenu = null;
    }

    /**
     * On destroy.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyFloat();
    }


    public class FloatMenuServiceBinder extends Binder {
        public FloatMenuService getService() {
            return FloatMenuService.this;
        }
    }

    private void goHomeIndex(Context context) {
        Uri uri = Uri.parse(Const.GAME_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
