# Contributing to FloatMenu

感谢你对 FloatMenu 项目的关注！我们欢迎任何形式的贡献。

## 📋 目录

- [行为准则](#行为准则)
- [如何贡献](#如何贡献)
- [开发流程](#开发流程)
- [代码规范](#代码规范)
- [提交信息规范](#提交信息规范)
- [问题反馈](#问题反馈)

---

## 行为准则

- 尊重所有贡献者
- 欢迎不同观点和建设性反馈
- 专注于对项目最有利的事情

---

## 如何贡献

### 报告 Bug

1. 在 [Issues](https://github.com/fanOfDemo/FloatMenuSample/issues) 中搜索现有问题
2. 如果没有找到，创建新的 Issue，包含：
   - 清晰的标题
   - 详细的问题描述
   - 复现步骤
   - 预期行为 vs 实际行为
   - 设备信息（Android 版本、设备型号）
   - 日志截图（如有）

### 提出新功能

1. 在 [Issues](https://github.com/fanOfDemo/FloatMenuSample/issues) 中创建功能请求
2. 说明功能的使用场景和预期效果
3. 如有可能，提供实现思路

### 提交代码

#### 1. Fork 项目

点击项目页面右上角的 "Fork" 按钮

#### 2. 克隆你的 Fork

```bash
git clone https://github.com/your-username/FloatMenuSample.git
cd FloatMenuSample
```

#### 3. 创建分支

```bash
git checkout -b feature/your-feature-name
# 或
git checkout -b fix/your-bug-fix
```

分支命名规范：
- `feature/xxx` - 新功能
- `fix/xxx` - Bug 修复
- `docs/xxx` - 文档更新
- `refactor/xxx` - 代码重构
- `style/xxx` - 代码风格调整
- `test/xxx` - 测试相关
- `chore/xxx` - 构建/工具相关

#### 4. 进行开发

#### 5. 提交代码

```bash
git add .
git commit -m "type: description"
```

#### 6. 推送到你的 Fork

```bash
git push origin feature/your-feature-name
```

#### 7. 创建 Pull Request

1. 访问你 Fork 的项目页面
2. 点击 "Pull Request" 按钮
3. 填写 PR 描述：
   - 关联相关 Issue
   - 说明你的改动内容
   - 附上截图（如适用）

---

## 开发流程

### 环境要求

- JDK 17+
- Android SDK 36
- Gradle 8.5+
- Android Studio

### 构建项目

```bash
# 克隆项目
git clone https://github.com/fanOfDemo/FloatMenuSample.git
cd FloatMenuSample

# 构建库
./gradlew :FloatMenu:assembleRelease

# 构建示例应用
./gradlew :FloatMenuDemo:assembleDebug

# 运行测试
./gradlew test

# 运行 lint 检查
./gradlew lint
```

### 项目结构

```
FloatMenu/
├── FloatMenu/                    # 库模块
│   └── src/main/java/...         # 源代码
│       ├── FloatMenu.java        # 简化的 API
│       ├── FloatLogoMenu.java    # 主要实现
│       ├── FloatMenuView.java    # 菜单视图
│       ├── FloatItem.java        # 菜单项
│       ├── DotImageView.java     # Logo 视图
│       └── customfloat/          # 自定义悬浮窗
├── FloatMenuDemo/                # 示例应用
│   └── src/main/java/...         # 示例代码
├── .github/                      # GitHub 配置
│   └── workflows/                # CI/CD 工作流
├── README.md                     # 项目说明
├── API_GUIDE.md                  # API 文档
├── CHANGELOG.md                  # 变更日志
└── CONTRIBUTING.md               # 贡献指南（本文件）
```

### 开发注意事项

1. **保持向后兼容** - 新功能不应破坏现有 API
2. **添加文档** - 更新相关的 README 和 API 文档
3. **编写测试** - 为新功能添加单元测试
4. **代码审查** - 确保代码通过 lint 检查

---

## 代码规范

### Java 代码风格

遵循 [Android Code Style](https://developer.android.com/kotlin/style-guide)：

```java
// 类名使用 PascalCase
public class FloatMenu { }

// 方法名使用 camelCase
public void createMenu() { }

// 常量使用全大写下划线分隔
private static final int MAX_COUNT = 100;

// 成员变量使用 m 前缀（可选）
private FloatLogoMenu mFloatMenu;
```

### 注释规范

```java
/**
 * 创建并显示悬浮菜单
 *
 * @param activity 当前 Activity
 * @param items 菜单项列表
 * @return FloatMenu 实例
 */
public static FloatMenu create(Activity activity, List<FloatItem> items) {
    // 实现
}
```

### Lint 检查

提交前确保代码通过 lint 检查：

```bash
./gradlew lint
```

常见 lint 警告处理：
- **ClickableViewAccessibility** - 添加 `performClick()` 调用或 `@SuppressLint` 注解
- **RtlHardcoded** - 使用 START/END 或添加 `@SuppressWarnings` 说明
- **PrivateApi** - 仅在必要时使用，添加 `@SuppressWarnings` 注解

---

## 提交信息规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 格式：

```
<type>: <description>

[optional body]

[optional footer]
```

### Type 类型

- `feat` - 新功能
- `fix` - Bug 修复
- `docs` - 文档更新
- `style` - 代码格式调整（不影响功能）
- `refactor` - 代码重构
- `perf` - 性能优化
- `test` - 测试相关
- `chore` - 构建/工具相关

### 示例

```bash
# 新功能
git commit -m "feat: add support for custom menu icons"

# Bug 修复
git commit -m "fix: resolve menu positioning issue on right side"

# 文档更新
git commit -m "docs: update API documentation for new methods"

# 重构
git commit -m "refactor: simplify positioning logic with absolute coordinates"
```

### 多行提交信息

```bash
git commit -m "feat: add dynamic red dot configuration

- Add setLogoDrawNum() method
- Add setLogoDrawBg() method
- Update documentation

Closes #123"
```

---

## 问题反馈

### Issue 模板

报告 Bug 时请使用以下模板：

```markdown
**问题描述**
清晰简洁地描述问题

**复现步骤**
1. 步骤一
2. 步骤二
3. ...

**预期行为**
描述你期望发生的情况

**实际行为**
描述实际发生的情况

**环境信息**
- FloatMenu 版本:
- Android 版本:
- 设备型号:
- Gradle 版本:

**日志**
```
粘贴相关日志
```

**截图**
如有必要，请添加截图
```

---

## 审查流程

1. 所有 PR 需要通过至少一位维护者的审查
2. 确保 CI 检查通过
3. 解决所有审查意见
4. 获得批准后，维护者将合并代码

---

## 获取帮助

如果你有任何问题：

- 📧 邮件：coderbrando@gmail.com
- 📝 [GitHub Issues](https://github.com/fanOfDemo/FloatMenuSample/issues)
- 📖 查看 [API 文档](API_GUIDE.md)

---

## 许可证

提交代码即表示你同意你的贡献将根据项目的 [BSD 3-Clause License](LICENSE) 进行许可。

---

## 致谢

感谢所有贡献者！你的贡献让 FloatMenu 变得更好。

<div align="center">

**再次感谢你的贡献！** 🎉

</div>
