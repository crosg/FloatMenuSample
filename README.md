# FloatMenuSample

[ ![Download](https://api.bintray.com/packages/fanofdemo/maven/FloatMenu/images/download.svg) ](https://bintray.com/fanofdemo/maven/FloatMenu/_latestVersion)[<img src="picture/chinareadlogo.png" width="64" />](http://www.yuewen.com/)


[<img src="picture/chinareadlogo.png" width="64" />游戏产品持续更新](http://xs.qidian.com/Home/Pc/Index/index)

欢迎关注 [https://github.com/crosg ](https://github.com/crosg)	

[crosg/FloatMenuSample](https://github.com/crosg/FloatMenuSample)
transfer from [yiming/FloatMenuSample](https://github.com/fanOfDemo/FloatMenuSample)


 
## GIF
<img src="picture/floatmenu2.gif" width="320" />
	

##  GRADLE:

	compile 'com.yw.game.floatmenu:FloatMenu:2.0.1'


android float menu in app 

## 权限 compatibility & permissions 

 无权限需求，支持 api 11 ++


for use：

	

使用示例
see sample 

[MainActivity](https://github.com/fanOfDemo/FloatMenuSample/blob/master/FloatMenuDemo/src/main/java/com/yw/game/floatmenu/demo/MainActivity.java)


1：

    mFloatMenu = new FloatLogoMenu.Builder()
                    .withActivity(mActivity)
                  //  .withContext(mActivity.getApplication())//这个在7.0（包括7.0）以上以及大部分7.0以下的国产手机上需要用户授权，需要搭配<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
                    .logo(BitmapFactory.decodeResource(getResources(),R.drawable.yw_game_logo))
                    .drawCicleMenuBg(true)
                    .backMenuColor(0xffe4e3e1)
                    .setBgDrawable(this.getResources().getDrawable(R.drawable.yw_game_float_menu_bg))
                    //这个背景色需要和logo的背景色一致
                    .setFloatItems(itemList)
                    .defaultLocation(FloatLogoMenu.RIGHT)
                    .drawRedPointNum(false)
                    .showWithListener(new FloatMenuView.OnMenuClickListener() {
                        @Override
                        public void onItemClick(int position, String title) {
                            Toast.makeText(MainActivity.this, "position " + position + " title:" + title + " is clicked.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void dismiss() {

                        }
                    });


2：

    mFloatMenu = new FloatLogoMenu.Builder()
            		.withActivity(mActivity)
                    .logo(R.drawable.yw_image_float_logo)
                    .backMenuColor(0xffe4e3e1)
                    .drawCicleMenuBg(true)
                    .setFloatItems(itemList)
                    .defaultLocation(FloatLogoMenu.RIGHT)
                    .drawRedPointNum(false)
                    .showWithListener(new FloatMenuView.OnMenuClickListener() {
        @Override
        public void onItemClick(int position, String title) {
            Toast.makeText(MainActivity.this, "position " + position + " title:" + title + " is clicked.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void dismiss() {

        }
    });


3： 因上述代码自定义功能较低，提供下面的方式供自行开发，这里可以直接将 FloatManager (https://github.com/crosg/FloatMenuSample/blob/master/FloatMenu/src/main/java/com/yw/game/floatmenu/customfloat/FloatManager.java) 文件拷贝出来直接使用




         floatManager = new FloatManager(this, new FloatManager.GetViewCallback() {
            @Override
            public View getLeftView(View.OnTouchListener touchListener) {
            //这里可以从xml导入一个用于出现在左边的菜单，,如果要模拟上面的效果，需要add一个logo，并设置touchListener
              //这里可以对xml中导入的子元素设置点击事件
                   return null;
            }

            @Override
            public View getRightView(View.OnTouchListener touchListener) {
            //这里可以从xml导入一个用于出现在右边的菜单,如果要模拟上面的效果，需要add一个logo，并设置touchListener
            //这里可以对xml中导入的子元素设置点击事件
                return null;
            }

            @Override
            public View getLogoView() {
            //这里仍然可以从xml导入布局 ，这个logoView会响应点击拖动事件
                return null;
            }

            @Override
            public void resetLogoViewSize(int hintLocation, View smallView) {
              //可以对smallView执行恢复动画之类的
            }

            @Override
            public void dragingLogoViewOffset(View samllView, boolean isDraging, boolean isResetPosition, float offset) {
                //这里可以根据是否拖动，是否靠边 对logoView执行旋转等动画
            }

            @Override
            public void shrinkLeftLogoView(View logoView) {
                //对logo进行左隐藏动画
            }

            @Override
            public void shrinkRightLogoView(View logoView) {
                //对logo进行右隐藏动画
            }

            @Override
            public void leftViewOpened(View leftView) {
                 //左菜单被打开
            }

            @Override
            public void rightViewOpened(View rightView) {
                 //右菜单被打开
            }

            @Override
            public void leftOrRightViewClosed(View logoView) {
                 //右或左菜单被关闭
            }

            @Override
            public void onDestoryed() {
                 //所有的view都被移除
            }
        });

示例：
         
      FloatManager   floatManager = new FloatManager(this, new FloatManager.GetViewCallback() {
            @Override
            public View getLeftView(View.OnTouchListener touchListener) {
                LinearLayout linearLayout = new LinearLayout(mActivity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);

                TextView textView = new TextView(mActivity);
                textView.setText("左边");

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "左边的菜单被点击了", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageView imageView = new ImageView(mActivity);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                imageView.setImageResource(R.drawable.yw_game_logo);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setOnTouchListener(touchListener);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                linearLayout.setBackgroundResource(R.drawable.yw_game_float_menu_bg);
                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                return linearLayout;
            }

            @Override
            public View getRightView(View.OnTouchListener touchListener) {
                LinearLayout linearLayout = new LinearLayout(mActivity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                TextView textView = new TextView(mActivity);
                textView.setText("右边");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "右边的菜单被点击了", Toast.LENGTH_SHORT).show();
                    }
                });

                ImageView imageView = new ImageView(mActivity);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                imageView.setImageResource(R.drawable.yw_game_logo);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setOnTouchListener(touchListener);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                linearLayout.setBackgroundResource(R.drawable.yw_game_float_menu_bg);
                linearLayout.addView(textView);
                linearLayout.addView(imageView);
                return linearLayout;
            }

            @Override
            public View getLogoView() {

                LinearLayout linearLayout = new LinearLayout(mActivity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);

                ImageView imageView = new ImageView(mActivity);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageResource(R.drawable.yw_game_logo);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dip2px(50), dip2px(50)));
                linearLayout.setBackgroundResource(R.drawable.yw_game_float_menu_bg);

                linearLayout.addView(imageView);
                return linearLayout;
            }

            @Override
            public void resetLogoViewSize(int hintLocation, View logoView) {

                logoView.setTranslationX(0);
                logoView.setScaleX(1);
                logoView.setScaleY(1);
            }

            @Override
            public void dragingLogoViewOffset(View smallView, boolean isDraging, boolean isResetPosition, float offset) {
                if (isDraging && offset > 0) {
                    smallView.setBackgroundDrawable(null);
                    smallView.setScaleX(1 + offset);
                    smallView.setScaleY(1 + offset);
                } else {
                    smallView.setBackgroundResource(R.drawable.yw_game_float_menu_bg);
                    smallView.setTranslationX(0);
                    smallView.setScaleX(1);
                    smallView.setScaleY(1);
                }
            }

            @Override
            public void shrinkLeftLogoView(View smallView) {
                smallView.setTranslationX(-smallView.getWidth() / 3);
            }

            @Override
            public void shrinkRightLogoView(View smallView) {
                smallView.setTranslationX(smallView.getWidth() / 3);
            }

            @Override
            public void leftViewOpened(View leftView) {
                Toast.makeText(mActivity, "左边的菜单被打开了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightViewOpened(View rightView) {
                Toast.makeText(mActivity, "右边的菜单被打开了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void leftOrRightViewClosed(View smallView) {
                Toast.makeText(mActivity, "菜单被关闭了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDestoryed() {

            }
        });
        floatManager.show();
    




## 更新日志
UPDATE LOG:
	
* 1.+ 桌面或应用内悬浮窗 （removed）

		compile 'com.yw.game.floatmenu:FloatMenu:1.0.0'


* 2.0.0 重构版，应用内悬浮窗

		compile 'com.yw.game.floatmenu:FloatMenu:2.0.0'

		 //2.0.0版使用windows.addContentView接口，在unity3D游戏引擎需要开启该选项
            <meta-data android:name="unityplayer.ForwardNativeEventsToDalvik" android:value="true" />


* 2.0.1 可选择支持出现在桌面（需权限），应用内无权限需要。

        移除windows.addContentView接口
        出现在桌面的话需要：
        使用全局上下文  .withContext(mService.getApplication())
        并配合权限 <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />




## License

	
	Copyright (c) 2016, Shanghai YUEWEN Information Technology Co., Ltd. 
	All rights reserved.
	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
	
	* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
	* Neither the name of Shanghai YUEWEN Information Technology Co., Ltd. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
	
	THIS SOFTWARE IS PROVIDED BY SHANGHAI YUEWEN INFORMATION TECHNOLOGY CO., LTD. AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.




