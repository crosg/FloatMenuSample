# FloatMenu API 使用指南

## 概述

`FloatMenu` 是简化的悬浮菜单API，提供链式调用和合理的默认值，让大多数场景只需3-4行代码。

## 新旧API对比

### 旧API（啰嗦版）

```java
FloatLogoMenu floatMenu = new FloatLogoMenu.Builder()
        .withActivity(this)
        .logo(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
        .drawCicleMenuBg(true)
        .backMenuColor(0xffe4e3e1)
        .setBgDrawable(getResources().getDrawable(R.drawable.bg))
        .setFloatItems(itemList)
        .defaultLocation(FloatLogoMenu.RIGHT)
        .drawRedPointNum(false)
        .showWithListener(listener);
```
**代码行数**：~12行
**问题**：不清楚哪些是必须的、哪些是可选的

### 新API（极简版）

```java
FloatMenu.create(this)
        .logo(R.drawable.logo)
        .items(itemList)
        .show();
```
**代码行数**：3行
**优点**：简洁、清晰、默认值合理

## 快速开始

### 1. 极简用法（无监听）

```java
List<FloatItem> items = new ArrayList<>();
items.add(new FloatItem("首页", R.drawable.icon_home));
items.add(new FloatItem("客服", R.drawable.icon_service));
items.add(new FloatItem("消息", R.drawable.icon_msg));

FloatMenu.create(activity)
        .logo(R.drawable.logo)
        .items(items)
        .show();
```

### 2. 标准用法（带监听）

```java
FloatMenu.create(activity)
        .logo(R.drawable.logo)
        .items(items)
        .listener(new FloatMenuView.OnMenuClickListener() {
            @Override
            public void onItemClick(int position, String title) {
                Toast.makeText(activity, "点击了: " + title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void dismiss() {
                // 菜单关闭回调
            }
        })
        .show();
```

### 3. 高级配置

```java
FloatMenu.create(context)
        .logo(R.drawable.logo)
        .items(items)
        .location(FloatMenu.LEFT)          // 停靠在左侧
        .autoShrink(5000)                   // 5秒后自动贴边
        .showRedDot(true)                   // 显示红点数字
        .backgroundColor(0xFF4CAF50)       // 绿色背景
        .backgroundDrawable(drawable)      // 自定义背景
        .drawCircleBg(true)                // 绘制圆形背景
        .listener(listener)
        .show();
```

### 4. Service中使用

```java
public class FloatMenuService extends Service {
    private FloatLogoMenu mFloatMenu;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatMenu == null) {
            List<FloatItem> items = new ArrayList<>();
            items.add(new FloatItem("首页", R.drawable.icon_home));

            // 使用Application Context
            mFloatMenu = FloatMenu.create(getApplicationContext())
                    .logo(R.drawable.logo)
                    .items(items)
                    .location(FloatMenu.LEFT)
                    .show();
        }
        return START_STICKY;
    }
}
```

## API参考

### FloatMenu.create()

**作用**：创建FloatMenu构建器

**重载方法**：
- `create(Activity activity)` - 用于Activity中
- `create(Context context)` - 用于Service或需要Application Context的场景

### Builder方法

| 方法 | 必须 | 默认值 | 说明 |
|------|------|--------|------|
| `logo(int resId)` | ✅ | - | 设置logo图标（资源ID） |
| `logo(Bitmap bitmap)` | ✅ | - | 设置logo图标（Bitmap） |
| `items(List<FloatItem>)` | ✅ | - | 设置菜单项列表 |
| `listener(OnMenuClickListener)` | ⭕ | null | 设置点击监听 |
| `location(int)` | ⭕ | RIGHT | 停靠位置（LEFT/RIGHT） |
| `autoShrink(int)` | ⭕ | 3000ms | 自动贴边延时（0=不贴边） |
| `showRedDot(boolean)` | ⭕ | false | 是否显示红点数字 |
| `drawCircleBg(boolean)` | ⭕ | true | 是否绘制圆形背景 |
| `backgroundColor(int)` | ⭕ | 系统默认 | 背景颜色 |
| `backgroundDrawable(Drawable)` | ⭕ | 系统默认 | 背景Drawable |
| `show()` | ✅ | - | 显示悬浮菜单 |

### FloatItem构造方法

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

## 参数说明

### location

```java
.location(FloatMenu.LEFT)   // 左侧
.location(FloatMenu.RIGHT)  // 右侧（默认）
```

### autoShrink

```java
.autoShrink(0)      // 不自动贴边
.autoShrink(3000)   // 3秒后自动贴边（默认）
.autoShrink(5000)   // 5秒后自动贴边
```

### showRedDot

```java
.showRedDot(false)  // 不显示红点数字（默认）
.showRedDot(true)   // 显示红点数字（根据FloatItem.dotNum）
```

## 最佳实践

### 1. Activity生命周期管理

```java
public class MainActivity extends Activity {
    private FloatLogoMenu mFloatMenu;

    @Override
    protected void onResume() {
        super.onResume();
        if (mFloatMenu == null) {
            mFloatMenu = FloatMenu.create(this)
                    .logo(R.drawable.logo)
                    .items(items)
                    .show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFloatMenu != null) {
            mFloatMenu.hide();
        }
    }

    @Override
    protected void onDestroy() {
        if (mFloatMenu != null) {
            mFloatMenu.destroyFloat();
            mFloatMenu = null;
        }
        super.onDestroy();
    }
}
```

### 2. Service生命周期管理

```java
public class FloatMenuService extends Service {
    private FloatLogoMenu mFloatMenu;

    @Override
    public void onCreate() {
        super.onCreate();
        // 准备数据
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatMenu == null) {
            mFloatMenu = FloatMenu.create(getApplicationContext())
                    .logo(R.drawable.logo)
                    .items(items)
                    .show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mFloatMenu != null) {
            mFloatMenu.destroyFloat();
            mFloatMenu = null;
        }
        super.onDestroy();
    }
}
```

### 3. 权限处理（Service悬浮窗）

```java
private void startFloatMenuService() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "请先开启悬浮窗权限", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
            return;
        }
    }

    // 启动Service
    Intent intent = new Intent(this, FloatMenuService.class);
    startService(intent);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                startFloatMenuService();
            }
        }
    }
}
```

## 迁移指南

### 从旧API迁移到新API

**旧代码**：
```java
mFloatMenu = new FloatLogoMenu.Builder()
        .withActivity(this)
        .logo(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
        .drawCicleMenuBg(true)
        .backMenuColor(0xffe4e3e1)
        .setBgDrawable(getResources().getDrawable(R.drawable.bg))
        .setFloatItems(items)
        .defaultLocation(FloatLogoMenu.RIGHT)
        .drawRedPointNum(false)
        .showWithListener(listener);
```

**新代码**：
```java
mFloatMenu = FloatMenu.create(this)
        .logo(R.drawable.logo)
        .items(items)
        .listener(listener)
        .show();
```

**简化量**：从12行减少到5行（减少60%）

## 常见问题

### Q: 如何在Activity和Service中都使用悬浮菜单？

A: 使用不同的配置区分：
- Activity：使用`FloatMenu.RIGHT`，灰色背景
- Service：使用`FloatMenu.LEFT`，彩色背景

### Q: 如何防止Activity和Service的悬浮球冲突？

A: 在启动Service前销毁Activity的悬浮球，在停止Service后恢复：

```java
// 启动Service
private void startFloatMenuService() {
    destroyFloat();  // 先销毁Activity的悬浮球
    startService(new Intent(this, FloatMenuService.class));
}

// 停止Service
private void stopFloatMenuService() {
    stopService(new Intent(this, FloatMenuService.class));
    new Handler().postDelayed(() -> {
        mFloatMenu = null;  // 延迟恢复Activity的悬浮球
    }, 500);
}
```

### Q: 新API和旧API有什么区别？

A:
- 新API更简洁（3行 vs 12行）
- 新API有合理的默认值
- 新API方法命名更清晰
- 旧API仍然可用，向后兼容
