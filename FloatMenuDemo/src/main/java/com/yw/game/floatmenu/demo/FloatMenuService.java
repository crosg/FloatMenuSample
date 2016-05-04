/*
 * 【ENG】
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
 * 【中文】
 * Copyright (c) 2016 著作权由上海阅文信息技术有限公司所有。著作权人保留一切权利。
 *
 * 这份授权条款，在使用者符合以下三条件的情形下，授予使用者使用及再散播本软件包装原始码及二进位可执行形式的权利，无论此包装是否经改作皆然：
 *
 *  对于本软件源代码的再散播，必须保留上述的版权宣告、此三条件表列，以及下述的免责声明。
 *  对于本套件二进位可执行形式的再散播，必须连带以文件以及／或者其他附于散播包装中的媒介方式，重制上述之版权宣告、此三条件表列，以及下述的免责声明。
 *  未获事前取得书面许可，不得使用柏克莱加州大学或本软件贡献者之名称，来为本软件之衍生物做任何表示支持、认可或推广、促销之行为。
 *
 * 免责声明：本软件是由上海阅文信息技术有限公司及本软件之贡献者以现状提供，本软件包装不负任何明示或默示之担保责任，包括但不限于就适售性以及特定目的的适用性为默示性担保。加州大学董事会及本软件之贡献者，无论任何条件、无论成因或任何责任主义、无论此责任为因合约关系、无过失责任主义或因非违约之侵权（包括过失或其他原因等）而起，对于任何因使用本软件包装所产生的任何直接性、间接性、偶发性、特殊性、惩罚性或任何结果的损害（包括但不限于替代商品或劳务之购用、使用损失、资料损失、利益损失、业务中断等等），不负任何责任，即在该种使用已获事前告知可能会造成此类损害的情形下亦然。
 *
 */

package com.yw.game.floatmenu.demo;

import com.yw.game.floatmenu.FloatMenu;
import com.yw.game.floatmenu.MenuItemView;
import com.yw.game.floatmenu.OnMenuActionListener;
import com.yw.game.sclib.Sc;
import com.yw.game.sclib.ScCreateResultCallback;

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

/**
 * Created by wengyiming on 2015/12/20.
 */
public class FloatMenuService extends Service implements View.OnClickListener, OnMenuActionListener {
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
        return new FloatViewServiceBinder();
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
                .addMenuItem(android.R.color.transparent, R.drawable.yw_menu_account, Const.MENU_ITEMS[0], android.R.color.black, this)
                .addMenuItem(android.R.color.transparent, R.drawable.yw_menu_favour, Const.MENU_ITEMS[1], android.R.color.black, this)
                .addMenuItem(android.R.color.transparent, R.drawable.yw_menu_fb, Const.MENU_ITEMS[2], android.R.color.black, this)
                .addMenuItem(android.R.color.transparent, R.drawable.yw_menu_msg, Const.MENU_ITEMS[3], android.R.color.black, this)
                .addMenuItem(android.R.color.transparent, R.drawable.yw_menu_close, Const.MENU_ITEMS[4], android.R.color.black, this)
                .menuBackground(R.drawable.yw_menu_bg)
                .onMenuActionListner(this)
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
                    break;
                case Const.FEEDBACK:

                    mFloatMenu.removeMenuItenView(Const.MENU_ITEMS.length - 1);
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

    /**
     * On menu open.
     */
    @Override
    public void onMenuOpen() {

    }

    /**
     * On menu close.
     */
    @Override
    public void onMenuClose() {

    }

    /**
     * The type Float view service binder.
     */
    public class FloatViewServiceBinder extends Binder {
        /**
         * Gets service.
         *
         * @return the service
         */
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
