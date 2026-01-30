<div align="center">

# FloatMenu

### 🎯 一个轻量级、易用的 Android 悬浮菜单库

[![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg)](https://android-arsenal.com/api?level=11)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](LICENSE)
[![Release](https://img.shields.io/github/v/release/fanOfDemo/FloatMenuSample)](https://github.com/fanOfDemo/FloatMenuSample/releases)

![FloatMenu Demo](picture/floatmenu2.gif)

</div>

---

## ✨ 特性

| 特性 | 描述 |
|:---:|:---|
| ✨ **轻量级** | 无第三方依赖，体积小巧 |
| 🎨 **高度可定制** | 支持自定义背景、图标、菜单项、颜色等 |
| 📍 **灵活定位** | 支持停靠在屏幕左侧或右侧 |
| 🔔 **红点提醒** | 支持红点数字提醒功能 |
| 🎯 **Unity 支持** | 兼容 Unity3D 游戏引擎 |
| 📱 **Android 11+** | 支持最新 Android 版本 |
| 🔄 **AndroidX** | 完全兼容 AndroidX |
| ♿ **无障碍** | 完整的无障碍功能支持 |

---

## 📦 依赖

在模块的 `build.gradle` 中添加：

```gradle
dependencies {
    implementation 'com.yw.game.floatmenu:FloatMenu:2.4.0'
}
```

或者查看 [JCenter](https://bintray.com/) / [Maven Central](https://mvnrepository.com/) 获取最新版本。

---

## 🚀 快速开始

### 极简用法（3行代码）

```java
// 1. 创建菜单项
List<FloatItem> items = new ArrayList<>();
items.add(new FloatItem("首页", R.drawable.icon_home));
items.add(new FloatItem("客服", R.drawable.icon_service));
items.add(new FloatItem("消息", R.drawable.icon_msg));

// 2. 创建并显示悬浮菜单
FloatMenu.create(this)
    .logo(R.drawable.logo)
    .items(items)
    .show();
```

### 标准用法（带监听）

```java
FloatMenu.create(this)
    .logo(R.drawable.logo)
    .items(items)
    .listener(new FloatMenuView.OnMenuClickListener() {
        @Override
        public void onItemClick(int position, String title) {
            Toast.makeText(this, "点击了: " + title, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void dismiss() {
            // 菜单关闭回调
        }
    })
    .show();
```

### 高级配置

```java
FloatMenu.create(context)
    .logo(R.drawable.logo)
    .items(items)
    .location(FloatMenu.LEFT)          // 停靠在左侧
    .autoShrink(5000)                   // 5秒后自动贴边
    .showRedDot(true)                   // 显示红点数字
    .backgroundColor(0xFF4CAF50)       // 自定义背景颜色
    .drawCircleBg(true)                // 绘制圆形背景
    .listener(listener)
    .show();
```

---

## 📖 详细文档

- **[API 使用指南](API_GUIDE.md)** - 详细的 API 文档和使用示例
- **[更新日志](CHANGELOG.md)** - 版本历史和变更记录
- **[贡献指南](CONTRIBUTING.md)** - 如何参与贡献

---

## 🎯 核心功能详解

### 交互逻辑

```
┌─────────────┐
│  贴边状态   │ ──点击──> ┌─────────────┐
│ (50%隐藏)   │           │  正常状态   │
└─────────────┘           └─────────────┘
                               │
                              点击
                               ↓
                        ┌─────────────┐
                        │  展开状态   │
                        └─────────────┘
```

### 功能说明

#### 1. 拖动限制
悬浮球在拖动时始终保持在屏幕边界内：
- X轴范围：`[0, 屏幕宽度 - logo宽度]`
- Y轴范围：`[状态栏高度, 屏幕高度 - logo高度]`
- 手指中心与logo圆心保持一致

#### 2. 自动贴边
可配置延时后自动贴边至屏幕边缘：
- 默认 3 秒后自动贴边
- 贴边后 logo 的 50% 在屏幕内，50% 超出边界
- 使用平滑动画过渡（300ms 贴边，200ms 恢复）

#### 3. 状态管理
| 状态 | 描述 |
|:---:|:---|
| 正常状态 | logo 完全可见，可点击展开菜单 |
| 贴边状态 | logo 的 50% 超出屏幕，点击先恢复 |
| 展开状态 | 菜单已展开，点击 logo 关闭菜单 |
| 拖动状态 | 正在拖动中，暂停贴边计时 |

---

## 🔧 在 Service 中使用

在 Service 中使用悬浮菜单可以让菜单在应用外持续显示。

### 步骤一：添加权限

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

### 步骤二：创建 Service

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
        }
    }

    private void initFloatMenu() {
        List<FloatItem> items = new ArrayList<>();
        items.add(new FloatItem("首页", R.drawable.icon_home));

        mFloatMenu = FloatMenu.create(getApplicationContext())
            .logo(R.drawable.logo)
            .items(items)
            .location(FloatMenu.LEFT)
            .show();
    }
}
```

### 步骤三：注册 Service

```xml
<service
    android:name=".FloatMenuService"
    android:enabled="true"
    android:exported="false" />
```

### 步骤四：启动 Service

```java
// 启动服务
Intent intent = new Intent(this, FloatMenuService.class);
startService(intent);

// 停止服务
Intent intent = new Intent(this, FloatMenuService.class);
stopService(intent);
```

---

## 📊 API 文档

### FloatMenu.Builder

| 方法 | 必须 | 默认值 | 说明 |
|:---|:---:|:---:|:---|
| `logo(int resId)` | ✅ | - | 设置 logo 图标（资源 ID） |
| `logo(Bitmap bitmap)` | ✅ | - | 设置 logo 图标（Bitmap） |
| `items(List<FloatItem>)` | ✅ | - | 设置菜单项列表 |
| `listener(OnMenuClickListener)` | ⭕ | null | 设置点击监听器 |
| `location(int)` | ⭕ | RIGHT | 停靠位置（LEFT/RIGHT） |
| `autoShrink(int)` | ⭕ | 3000 | 自动贴边延时（0=不贴边） |
| `showRedDot(boolean)` | ⭕ | false | 是否显示红点数字 |
| `drawCircleBg(boolean)` | ⭕ | true | 是否绘制圆形背景 |
| `backgroundColor(int)` | ⭕ | 系统默认 | 背景颜色 |
| `backgroundDrawable(Drawable)` | ⭕ | 系统默认 | 背景 Drawable |
| `show()` | ✅ | - | 显示悬浮菜单 |

### FloatItem 构造方法

```java
// 方法1：只有标题
FloatItem(String title)

// 方法2：标题 + 图标资源ID
FloatItem(String title, int drawableId)

// 方法3：标题 + 图标Bitmap
FloatItem(String title, Bitmap bitmap)

// 方法4：完整参数
FloatItem(String title, int pressColor, int normalColor, Bitmap icon, String dotNum)
```

---

## 🎨 自定义悬浮窗

### 方法一：继承 BaseFloatDialog

```java
public class MyFloatDialog extends BaseFloatDialog {
    @Override
    public View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.my_float_dialog, null);
        return view;
    }
}

// 使用
MyFloatDialog dialog = new MyFloatDialog();
dialog.show(this);
```

### 方法二：实现 GetViewCallback

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

---

## 🔐 权限说明

| 使用场景 | 权限要求 |
|:---|:---|
| 应用内悬浮 | 无需权限 |
| 桌面悬浮 | `SYSTEM_ALERT_WINDOW` |

添加悬浮窗权限：

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

---

## 📱 系统要求

| 项目 | 要求 |
|:---|:---|
| 最低 SDK | API 11 (Android 3.0) |
| 编译 SDK | API 36 |
| Java 版本 | Java 17 |
| Gradle 版本 | 8.5+ |

---

## 📸 示例应用

查看 [FloatMenuDemo](FloatMenuDemo) 模块了解完整用法。

Demo 应用展示了：
- ✅ Activity 中使用悬浮菜单
- ✅ Service 中使用悬浮菜单
- ✅ 自定义配置
- ✅ 生命周期管理

---

## 📝 更新日志

### v2.4.0 (2024-01-30)

**新增功能**
- 简化的 FloatMenu API，提供链式调用
- 新增 FloatMenuService 支持 Service 中使用悬浮菜单
- 新增动态配置方法 `setLogoDrawNum()` 和 `setLogoDrawBg()`

**Bug 修复**
- 修复右侧菜单展开时跳到左侧的问题
- 修复菜单展开后 logo 背景消失的问题
- 修复 lint 警告和无障碍支持

**优化**
- 移除 logo 拖动时的旋转动画
- 统一背景颜色
- 代码重构，简化定位逻辑

查看 [完整更新日志](CHANGELOG.md)

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

详情请参阅 [贡献指南](CONTRIBUTING.md)

---

## 📄 许可证

```
BSD 3-Clause License

Copyright (c) 2016, Shanghai YUEWEN Information Technology Co., Ltd.
All rights reserved.
```

详细内容见项目根目录的 [LICENSE](LICENSE) 文件。

---

## 📞 联系方式

- 作者：fanofdemo
- 邮箱：18720625976@163.com

---

<div align="center">

**如果这个项目对你有帮助，请给个 ⭐️ Star 支持一下！**

</div>
