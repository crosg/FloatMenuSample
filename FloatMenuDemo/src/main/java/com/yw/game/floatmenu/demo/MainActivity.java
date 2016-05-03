package com.yw.game.floatmenu.demo;

import com.yw.game.floatmenu.FloatMenu;
import com.yw.game.floatmenu.MenuItemView;
import com.yw.game.floatmenu.OnMenuActionListener;
import com.yw.game.sclib.Sc;
import com.yw.game.sclib.ScCreateResultCallback;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMenuActionListener {


    private FloatMenu mFloatMenu;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildFloatMenu();

    }

    private void buildFloatMenu() {

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
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mFloatMenu == null) {
            buildFloatMenu();
            mFloatMenu.show();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mFloatMenu != null) {
            mFloatMenu.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFloatMenu != null) {
            mFloatMenu.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if (v instanceof MenuItemView) {
            MenuItemView menuItemView = (MenuItemView) v;
            String menuItemLabel = menuItemView.getMenuItem().getLabel();
            Toast.makeText(MainActivity.this, menuItemLabel, Toast.LENGTH_SHORT).show();
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
                                }
                            });
                        }
                    }).start();

                    break;
                case Const.FAVOUR:
                    new Sc.Builder(this, MainActivity.class).
                            setName(getResources().getString(R.string.app_name)).
                            setAllowRepeat(false).
                            setIcon(R.mipmap.ic_launcher).
                            setCallBack(new ScCreateResultCallback() {
                                @Override
                                public void createSuccessed(String createdOrUpdate, Object tag) {
                                    Toast.makeText(MainActivity.this, createdOrUpdate, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void createError(String errorMsg, Object tag) {
                                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();

                                }
                            }).build().createSc();
                    break;
                case Const.FEEDBACK:
                    break;
                case Const.CLOSE:
                    if (mFloatMenu != null)
                        mFloatMenu.destroy();
                    break;
            }
        }
    }

    @Override
    public void onMenuOpen() {
        Toast.makeText(MainActivity.this, "onMenuOpen", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuClose() {
        Toast.makeText(MainActivity.this, "onMenuClose", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.service:
                Intent mIntent = new Intent(this, FloatMenuInServiceActivity.class);
                startActivity(mIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
