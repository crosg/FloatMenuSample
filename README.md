# FloatMenu

一个轻量级、易用的 Android 悬浮菜单库。

![GIF](picture/floatmenu2.gif)

## 特性

- ✨ 轻量级，无第三方依赖
- 🎨 支持自定义背景、图标、菜单项
- 📍 支持停靠在屏幕左侧或右侧
- 🔔 支持红点提醒（带数字）
- 🎯 支持 Unity3D 游戏引擎
- 📱 支持 Android 11+
- 🔄 支持 AndroidX

## 功能说明和交互细节

**FloatMenu** 是一个智能悬浮球菜单，具备以下交互特性：

### 核心功能
- **拖动限制**：悬浮球在拖动时始终保持在屏幕边界内
  - X轴范围：`[0, 屏幕宽度 - logo宽度]`
  - Y轴范围：`[状态栏高度, 屏幕高度 - logo高度]`
  - 手指中心与logo圆心保持一致

- **拖动动画**：拖动时只有logo中间的图标会旋转，背景圆圈和红点保持静止

- **自动贴边**：可配置延时后自动贴边至屏幕边缘（默认3秒）
  - 贴边后logo的50%在屏幕内，50%超出屏幕边界
  - 使用平滑动画过渡（300ms贴边，200ms恢复）
  - 通过 `autoShrinkDelay(int)` 方法配置延时时间

### 点击交互逻辑
1. **贴边状态** → 点击 → 恢复到100%可见（不打开菜单）
2. **正常状态** → 点击 → 展开菜单
3. **菜单展开** → 点击logo → 关闭菜单

### 状态管理
- **正常状态**：logo完全可见，可点击展开菜单
- **贴边状态**：logo的50%超出屏幕，点击先恢复
- **展开状态**：菜单已展开，点击logo关闭菜单
- **拖动状态**：正在拖动中，暂停贴边计时

## 依赖

在模块的 `build.gradle` 中添加：

```gradle
dependencies {
    implementation 'com.yw.game.floatmenu:FloatMenu:x.y.z'
}
```

**最新版本：** [![Release](https://img.shields.io/github/release/fanOfDemo/FloatMenuSample.svg)](https://github.com/fanOfDemo/FloatMenuSample/releases)

## 快速开始

### 1. 基本使用

```java
// 创建菜单项列表
List<FloatItem> itemList = new ArrayList<>();
itemList.add(new FloatItem("菜单1", R.drawable.icon1));
itemList.add(new FloatItem("菜单2", R.drawable.icon2));
itemList.add(new FloatItem("菜单3", R.drawable.icon3));

// 创建并显示菜单（使用默认3秒自动贴边）
FloatLogoMenu floatMenu = new FloatLogoMenu.Builder()
    .withActivity(this)
    .logo(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
    .drawCicleMenuBg(true)
    .backMenuColor(0xffe4e3e1)
    .setBgDrawable(getResources().getDrawable(R.drawable.menu_bg))
    .setFloatItems(itemList)
    .defaultLocation(FloatLogoMenu.RIGHT)
    .drawRedPointNum(false)
    .showWithListener(new FloatMenuView.OnMenuClickListener() {
        @Override
        public void onItemClick(int position, String title) {
            Toast.makeText(this, position + ": " + title, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void dismiss() {
            // 菜单关闭回调
        }
    });

// 自定义自动贴边延时（例如5秒）
FloatLogoMenu floatMenu = new FloatLogoMenu.Builder()
    .withActivity(this)
    .logo(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
    .setFloatItems(itemList)
    .autoShrinkDelay(5000)  // 5秒后自动贴边
    .showWithListener(listener);
```

### 2. 在 Service 中使用（推荐用于后台悬浮）

在 Service 中使用悬浮菜单可以让菜单在应用外持续显示。

**Demo 应用示例对比**：

| 特性 | Activity 悬浮球 | Service 悬浮球 |
|------|----------------|----------------|
| 显示时机 | 应用打开时自动显示 | 需要手动启动服务 |
| 停靠位置 | 右侧（RIGHT） | 左侧（LEFT） |
| 背景颜色 | 灰色（0xffe4e3e1） | 绿色（0xFF4CAF50） |
| 红点数字 | 不显示 | 显示 |
| Toast标识 | 📱 Activity菜单 | 🔧 Service菜单 |
| 应用范围 | 仅应用内 | 可在应用外显示 |
| 持续运行 | Activity销毁时消失 | Service持续运行 |

这样设计是为了在 Demo 中同时展示两种使用方式，方便对比学习。

#### 步骤一：添加权限

在 `AndroidManifest.xml` 中添加：

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

#### 步骤二：创建 Service

```java
public class FloatMenuService extends Service {
    private FloatLogoMenu mFloatMenu;

    @Override
    public void onCreate() {
        super.onCreate();
        initFloatMenu();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatMenu != null) {
            mFloatMenu.show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatMenu != null) {
            mFloatMenu.destroyFloat();
            mFloatMenu = null;
        }
    }

    private void initFloatMenu() {
        ArrayList<FloatItem> itemList = new ArrayList<>();
        itemList.add(new FloatItem("首页", R.drawable.icon1));
        itemList.add(new FloatItem("客服", R.drawable.icon2));
        itemList.add(new FloatItem("消息", R.drawable.icon3));

        mFloatMenu = new FloatLogoMenu.Builder()
                .withContext(getApplicationContext())  // 必须使用Application上下文
                .logo(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setFloatItems(itemList)
                .defaultLocation(FloatLogoMenu.RIGHT)
                .autoShrinkDelay(5000)  // 5秒后自动贴边
                .showWithListener(new FloatMenuView.OnMenuClickListener() {
                    @Override
                    public void onItemClick(int position, String title) {
                        // 处理菜单点击
                    }

                    @Override
                    public void dismiss() {
                        // 菜单关闭回调
                    }
                });
    }
}
```

#### 步骤三：注册 Service

在 `AndroidManifest.xml` 中注册：

```xml
<service
    android:name=".FloatMenuService"
    android:enabled="true"
    android:exported="false" />
```

#### 步骤四：启动 Service

```java
// 启动服务
Intent intent = new Intent(this, FloatMenuService.class);
startService(intent);

// 停止服务
Intent intent = new Intent(this, FloatMenuService.class);
stopService(intent);
```

### 3. 带权限的桌面悬浮

如需在桌面显示悬浮窗，需要添加权限：

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

并使用 Application 上下文：

```java
FloatLogoMenu floatMenu = new FloatLogoMenu.Builder()
    .withContext(getApplication())  // 使用全局上下文
    .logo(R.drawable.logo)
    .setFloatItems(itemList)
    .show();
```

### 4. 自定义悬浮窗

#### 方法一：继承 BaseFloatDialog

```java
public class MyFloatDialog extends BaseFloatDialog {
    @Override
    public View onCreateView(LayoutInflater inflater) {
        // 自定义布局
        View view = inflater.inflate(R.layout.my_float_dialog, null);
        return view;
    }
}

// 使用
MyFloatDialog dialog = new MyFloatDialog();
dialog.show(this);
```

#### 方法二：实现 GetViewCallback

```java
BaseFloatDialog dialog = new BaseFloatDialog(this, new BaseFloatDialog.GetViewCallback() {
    @Override
    public View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.my_float_dialog, null);
        // 初始化视图
        return view;
    }
});
dialog.show();
```

## API 文档

### FloatLogoMenu.Builder

| 方法 | 说明 |
|------|------|
| `withActivity(Activity)` | 设置 Activity |
| `withContext(Context)` | 设置 Context（用于桌面悬浮） |
| `logo(int/drawable)` | 设置 Logo 图标 |
| `drawCicleMenuBg(boolean)` | 是否绘制圆形菜单背景 |
| `backMenuColor(int)` | 菜单背景色 |
| `setBgDrawable(Drawable)` | 设置背景 Drawable |
| `setFloatItems(List<FloatItem>)` | 设置菜单项列表 |
| `defaultLocation(int)` | 默认停靠位置（LEFT/RIGHT） |
| `autoShrinkDelay(int)` | 自动贴边延时时间（毫秒），默认3000（3秒） |
| `drawRedPointNum(boolean)` | 是否绘制红点数字 |
| `showWithListener(OnMenuClickListener)` | 显示并设置点击监听 |

### FloatItem

```java
public class FloatItem {
    public FloatItem(String title);                    // 仅标题
    public FloatItem(String title, int drawableId);     // 标题 + 图标
    public FloatItem(String title, Bitmap bitmap);       // 标题 + Bitmap
}
```

## 权限

无权限需求（应用内悬浮）。

如需桌面悬浮，添加：

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

## 最小 SDK 版本

- Android 11 (API 11)
- compileSdk 36
- Java 17

## 示例应用

查看 [FloatMenuDemo](FloatMenuDemo) 模块了解完整用法。

## 更新日志

查看 [CHANGELOG.md](CHANGELOG.md)

## 贡献

欢迎提交 Issue 和 Pull Request！详见 [CONTRIBUTING.md](CONTRIBUTING.md)

## License

```
BSD 3-Clause License

Copyright (c) 2016, Shanghai YUEWEN Information Technology Co., Ltd.
All rights reserved.
```

详细内容见项目根目录的 LICENSE 文件。
