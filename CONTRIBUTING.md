# Contributing to FloatMenu

欢迎为 FloatMenu 做出贡献！

## 如何贡献

### 报告 Bug

1. 在 GitHub Issues 中搜索是否已存在相同问题
2. 如果没有，创建新的 Issue
3. 包含以下信息：
   - Android 版本
   - 设备型号
   - 复现步骤
   - 预期行为 vs 实际行为
   - 相关代码片段或日志

### 提交 Pull Request

1. **Fork** 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. **Commit** 你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. **Push** 到分支 (`git push origin feature/AmazingFeature`)
5. **Pull Request** 到主分支

### 开发设置

```bash
# 克隆仓库
git clone https://github.com/your-username/FloatMenuSample.git

# 进入项目目录
cd FloatMenuSample

# 使用 Gradle 构建
./gradlew build

# 运行示例应用
./gradlew installDebug
```

## 代码规范

### Java 代码风格

- 使用 4 空格缩进
- 类和接口名使用 `PascalCase`
- 方法和变量名使用 `camelCase`
- 常量使用 `UPPER_CASE_WITH_UNDERSCORES`
- 每个文件顶部添加版权声明

### 注释

- 公共 API 必须添加 Javadoc 注释
- 复杂逻辑添加解释性注释
- 避免使用过时的 API，如必须使用，添加 `@Deprecated` 注解

### 提交信息

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type:**
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式调整（不影响功能）
- `refactor`: 重构
- `perf`: 性能改进
- `test`: 测试
- `chore`: 构建/工具链更新

**示例：**
```
feat(menu): add drag gesture support for floating menu

feat(auto-shrink): add configurable auto-shrink to screen edge

fix(dialog): resolve duplicate openMenu method crash

docs(readme): update API usage examples

refactor(core): remove unused code and improve state management
```

## 测试

- 在运行示例应用前，确保所有单元测试通过
- 测试不同的 Android 版本和设备
- 特别注意权限相关的场景

## 版本发布流程

1. 更新 `CHANGELOG.md`
2. 更新 `build.gradle` 中的版本号
3. 创建 Git tag:
   ```bash
   git tag -a v3.0.0 -m "Release version 3.0.0"
   git push origin v3.0.0
   ```
4. 在 GitHub 上创建 Release

## License

贡献的代码将使用与项目相同的 BSD 3-Clause License。
