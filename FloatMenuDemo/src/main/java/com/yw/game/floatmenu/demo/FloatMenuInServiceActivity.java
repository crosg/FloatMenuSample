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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class FloatMenuInServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBothNavigationBarAndStatusBar();
        setContentView(R.layout.activity_float_menu_in_service);
        findViewById(R.id.hideStatuBarNaviBar).setOnClickListener(this);

        boolean canDrawOverlays = checkCanDrawOverlays(this);
        if (canDrawOverlays) {
            FloatMenuManager.getInstance().startFloatView(this.getApplicationContext());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT <= 23) {
                return;
            }
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "悬浮权限被拒", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "权限授予成功", Toast.LENGTH_SHORT).show();
                FloatMenuManager.getInstance().startFloatView(this.getApplicationContext());
            }
        }
    }


    /**
     * 这个权限主要是为了配合window的phone类型，引导用户前往应用设置打开悬浮窗权限（出现在其他应用上）
     *
     * @param activity 这里必须是activity，否则引导的设置界面就不是当前应用的设置界面了，而是当前手机所有需要悬浮窗权限的app列表
     * @return true/false false表示需要申请这个权限
     */
    public static boolean checkCanDrawOverlays(Activity activity) {
        //理论上是23就需要申请这个权限，但测试返现，23仍然可以使用toast类型，避开用户权限引导。但7.1则会出现崩溃
        if (Build.VERSION.SDK_INT > 23) {
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    /**
     * // Hide both the navigation bar and the status bar.
     * // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
     * // a general rule, you should design your app to hide the status bar whenever you
     * // hide the navigation bar.
     */
    private void hideBothNavigationBarAndStatusBar() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBothNavigationBarAndStatusBar();
        FloatMenuManager.getInstance().showFloatingView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FloatMenuManager.getInstance().hideFloatingView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatMenuManager.getInstance().destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hideStatuBarNaviBar:
                hideBothNavigationBarAndStatusBar();
                break;
        }
    }
}
