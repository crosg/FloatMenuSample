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

package com.yw.game.floatmenu.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

public class FloatMenuInServiceActivity extends AppCompatActivity {
    private FloatMenuService mFloatMenuService;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mFloatMenuService = ((FloatMenuService.FloatViewServiceBinder) iBinder).getService();
            if (mFloatMenuService != null) {
                mFloatMenuService.showFloat();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mFloatMenuService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_menu_in_service);
        startFloatMenu(this);

    }

    public boolean startFloatMenu(Context context) {
        boolean startFloatMenuSuccessed;
        try {
            Intent intent = new Intent(context, FloatMenuService.class);
            context.startService(intent);
            context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            startFloatMenuSuccessed = true;
        } catch (Exception ignored) {
            startFloatMenuSuccessed = false;
        }
        return startFloatMenuSuccessed;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mFloatMenuService != null)
            mFloatMenuService.showFloat();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFloatMenuService.hideFloat();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFloatMenuService.destroyFloat();
        unbindService(mServiceConnection);
    }
}
