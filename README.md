# FloatMenuSample
[ ![Download](https://api.bintray.com/packages/fanofdemo/maven/FloatMenu/images/download.svg) ](https://bintray.com/fanofdemo/maven/FloatMenu/_latestVersion)




##项目已转移到 阅文集团 
[该项目在阅文集团游戏产品中持续更新，欢迎关注： https://github.com/crosg ](https://github.com/crosg)	

[crosg/FloatMenuSample](https://github.com/crosg/FloatMenuSample)


##update log:
	
	0.0.1 init lib
	0.0.2 fix a initLoading Logo Animation bug， add Logo startLoadingAnimation
	0.0.3 add change Logo and MenuItem Drawable
	0.0.4 fix logo size when startLoadingAnimation
	0.0.5 add method:
				addMenuIten(position:int,menuItem);
				removeMenuItem(positon:int);
			set MenuItem Animation:
				show with animation(alpha/scale);

## to use

Gradle:

	compile 'com.yw.game.floatmenu:FloatMenu:0.0.5'


Download [aar](https://dl.bintray.com/fanofdemo/maven/com/yw/game/floatmenu/FloatMenu/0.0.5/FloatMenu-0.0.5.aar)	

Download [jar](https://bintray.com/fanofdemo/maven/download_file?file_path=com%2Fyw%2Fgame%2Ffloatmenu%2FFloatMenu%2F0.0.5%2FFloatMenu-0.0.5-sources.jar)


android float menu in app or launcher

##compatibility & permissions
 api 9 - api 23

api level <19 need permissions:

	 <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

api level >=19 no permissions



for use：

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

	//移除一个菜单
   	mFloatMenu.removeMenuItem(0);


	//添加一个菜单
	public void addCloseMenuItem(int position) {
        if (mFloatMenu == null)
            return;
        mFloatMenu.addMenuItem(position, android.R.color.transparent, R.drawable.yw_menu_close, Const.MENU_ITEMS[4], android.R.color.black, this);
    }

	 private void showRed() {
        if (!hasNewMsg) {
            mFloatMenu.changeLogo(R.drawable.yw_image_float_logo, R.drawable.yw_menu_msg, 3);
        } else {
            mFloatMenu.changeLogo(R.drawable.yw_image_float_logo_red, R.drawable.yw_menu_msg_red, 3);
        }
    }


###see sample [FloatMenuService](https://github.com/fanOfDemo/FloatMenuSample/blob/master/FloatMenuDemo%2Fsrc%2Fmain%2Fjava%2Fcom%2Fyw%2Fgame%2Ffloatmenu%2Fdemo%2FFloatMenuService.java)

##demo

<img src="pickture/20160503125603.png" width="320" />

<img src="pickture/201605031543.gif" width="320" />
<img src="pickture/201605041543.gif" width="320" />

##reference 

 [http://www.jianshu.com/p/167fd5f47d5c](http://www.jianshu.com/p/167fd5f47d5c) 

 [http://www.jianshu.com/p/634cd056b90c](http://www.jianshu.com/p/634cd056b90c) 

 [http://www.liaohuqiu.net/cn/posts/android-windows-manager/](http://www.liaohuqiu.net/cn/posts/android-windows-manager/) 




##License

	
	Copyright (c) 2016, Shanghai YUEWEN Information Technology Co., Ltd. 
	All rights reserved.
	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
	
	* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
	* Neither the name of Shanghai YUEWEN Information Technology Co., Ltd. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
	
	THIS SOFTWARE IS PROVIDED BY SHANGHAI YUEWEN INFORMATION TECHNOLOGY CO., LTD. AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.




