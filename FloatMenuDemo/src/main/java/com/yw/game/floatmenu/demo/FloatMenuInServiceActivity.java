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
