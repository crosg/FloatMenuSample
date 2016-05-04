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
 * 浮动窗口服务
 */
public class FloatMenuService extends Service implements View.OnClickListener, OnMenuActionListener {
    private FloatMenu mFloatMenu;
    private final static String TAG = FloatMenuService.class.getSimpleName();

    private Handler mHandler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return new FloatViewServiceBinder();
    }


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


    public void showFloat() {
        if (mFloatMenu != null)
            mFloatMenu.show();
    }

    public void hideFloat() {
        if (mFloatMenu != null) {
            mFloatMenu.hide();
        }
    }

    public void destroyFloat() {
        hideFloat();
        if (mFloatMenu != null) {
            mFloatMenu.destroy();
        }
        mFloatMenu = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyFloat();
    }

    @Override
    public void onMenuOpen() {

    }

    @Override
    public void onMenuClose() {

    }

    public class FloatViewServiceBinder extends Binder {
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
