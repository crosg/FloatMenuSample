# FloatMenuSample
a float menu in game

android桌面悬浮球

##兼容性与权限：
 api 9 - api 23

api level <19 需要权限:

	 <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

api level >=19 无需权限

##使用

引用库

	compile 'com.yw.game.floatmenu:FloatMenu:0.0.1'

调用：

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

###see sample [FloatMenuService](https://github.com/fanOfDemo/FloatMenuSample/blob/master/FloatMenuDemo%2Fsrc%2Fmain%2Fjava%2Fcom%2Fyw%2Fgame%2Ffloatmenu%2Fdemo%2FFloatMenuService.java)

##效果

<img src="pickture/20160503125603.png" width="320" />


##参考

 [http://www.jianshu.com/p/167fd5f47d5c](http://www.jianshu.com/p/167fd5f47d5c) 

 [http://www.jianshu.com/p/634cd056b90c](http://www.jianshu.com/p/634cd056b90c) 

 [http://www.liaohuqiu.net/cn/posts/android-windows-manager/](http://www.liaohuqiu.net/cn/posts/android-windows-manager/) 

