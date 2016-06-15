package com.yw.game.floatmenu.demo;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;

import java.io.ObjectStreamException;

/**
 * Created by wengyiming on 2016/1/6.
 * FloatApi
 */
public class FloatMenuManager implements ServiceConnectionManager.QdServiceConnection {
    private ServiceConnectionManager mServiceConnectionManager;

    private FloatMenuManager() {

    }

    //静态内部类实现单例  优于双重检查锁(DCL)单例
    public static FloatMenuManager getInstance() {
        return FloatMenuHolder.single;
    }

    /**
     * 静态内部类能够解决DCL双重检查锁失效的问题
     */
    private static class FloatMenuHolder {
        private static final FloatMenuManager single = new FloatMenuManager();
    }

    /**
     * 防止反序列获取新的单例
     *
     * @return
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return FloatMenuHolder.single;
    }

    private FloatMenuService mFloatViewService;

    public void startFloatView(Context context) {
        if (mFloatViewService != null) {
            mFloatViewService.showFloat();
            return;
        }
        if (mServiceConnectionManager == null) {
            mServiceConnectionManager = new ServiceConnectionManager(context, FloatMenuService.class, this);
            mServiceConnectionManager.bindToService();
        }
    }

    /**
     */
    public void addFloatMenuItem() {
        if (mFloatViewService != null) {
            mFloatViewService.addCloseMenuItem(0);
        }
    }

    /**
     *
     */
    public void removeMenuItem() {
        if (mFloatViewService != null) {
            mFloatViewService.removeMenuItem();
        }
    }

    /**
     * 显示悬浮图标
     */
    public void showFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.showFloat();
        }
    }

    /**
     * 隐藏悬浮图标
     */
    public void hideFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.hideFloat();
        }
    }

    /**
     * 释放QDSDK数据
     */
    public void destroy() {
        if (mFloatViewService != null) {
            mFloatViewService.hideFloat();
            mFloatViewService.destroyFloat();
        }
        if (mServiceConnectionManager != null) {
            mServiceConnectionManager.unbindFromService();
        }
        mFloatViewService = null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mFloatViewService = ((FloatMenuService.FloatMenuServiceBinder) service).getService();
        if (mFloatViewService != null) {
            mFloatViewService.showFloat();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mFloatViewService = null;
    }
}
