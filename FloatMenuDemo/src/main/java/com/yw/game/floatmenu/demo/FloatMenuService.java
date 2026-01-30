package com.yw.game.floatmenu.demo;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.yw.game.floatmenu.FloatItem;
import com.yw.game.floatmenu.FloatLogoMenu;
import com.yw.game.floatmenu.FloatMenu;
import com.yw.game.floatmenu.FloatMenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * FloatMenu后台服务
 * <p>
 * 本示例展示如何在后台Service中启动和管理悬浮菜单
 * </p>
 * <p>
 * 使用场景：
 * - 需要在应用外显示悬浮菜单
 * - 需要在后台持续运行悬浮菜单
 * - 需要在Service中管理悬浮菜单生命周期
 * </p>
 */
public class FloatMenuService extends Service {

    private static final String TAG = "FloatMenuService";

    private FloatLogoMenu mFloatMenu;
    private ArrayList<FloatItem> itemList;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "FloatMenuService created");
        // 在 onCreate 中只初始化数据，不创建悬浮菜单
        initData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "FloatMenuService started, creating and showing float menu");
        // 在 onStartCommand 中创建并显示悬浮菜单
        if (mFloatMenu == null) {
            createAndShowFloatMenu();
        }
        return START_STICKY; // 确保Service在被系统杀死后重启
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service onBind");
        return null; // 不需要绑定，返回null即可
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "FloatMenuService destroyed");
        // 销毁悬浮菜单
        if (mFloatMenu != null) {
            mFloatMenu.destroyFloat();
            mFloatMenu = null;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 创建菜单项列表
        itemList = new ArrayList<>();
        itemList.add(new FloatItem("首页", 0xFF000000, 0xffe4e3e1,
                BitmapFactory.decodeResource(getResources(), R.drawable.yw_menu_account), "1"));
        itemList.add(new FloatItem("客服", 0xFF000000, 0xffe4e3e1,
                BitmapFactory.decodeResource(getResources(), R.drawable.yw_menu_fb), "2"));
        itemList.add(new FloatItem("消息", 0xFF000000, 0xffe4e3e1,
                BitmapFactory.decodeResource(getResources(), R.drawable.yw_menu_msg), "3"));
    }

    /**
     * 创建并显示悬浮菜单（使用新的简化API）
     */
    private void createAndShowFloatMenu() {
        // 使用新的简化API
        mFloatMenu = FloatMenu.create(getApplicationContext())
                .logo(R.drawable.yw_game_logo)
                .items(itemList)
                .location(FloatMenu.LEFT)              // 左侧，区别于Activity的右侧
                .showRedDot(true)                      // 显示红点数字
                .autoShrink(5000)                      // 5秒后自动贴边
                .backgroundColor(0xffe4e3e1)          // 原始菜单背景颜色
                .drawCircleBg(true)                    // 绘制圆形背景
                .listener(new FloatMenuView.OnMenuClickListener() {
                    @Override
                    public void onItemClick(int position, String title) {
                        // 在子线程中显示Toast，需要切换到主线程
                        new android.os.Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(FloatMenuService.this,
                                    "🔧 Service菜单 - position: " + position + ", title: " + title,
                                    Toast.LENGTH_SHORT).show();
                        });
                        Log.i(TAG, "Service Menu item clicked: " + title);
                    }

                    @Override
                    public void dismiss() {
                        Log.i(TAG, "Service Menu dismissed");
                    }
                })
                .show();

        Log.i(TAG, "FloatMenuService: FloatMenu created and shown successfully");
    }
}
