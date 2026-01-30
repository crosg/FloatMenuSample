# GitHub Packages 发布指南

本文档说明如何将 FloatMenu 发布到 GitHub Packages。

---

## 前置准备

### 1. 创建 GitHub Personal Access Token

1. 访问：https://github.com/settings/tokens
2. 点击 "Generate new token" (classic)
3. 设置 token 名称（如：FloatMenu Publish）
4. 勾选以下权限：
   - ✅ `repo` - Full control of private repositories
   - ✅ `write:packages` - Upload packages to GitHub Packages
   - ✅ `read:packages` - Download packages from GitHub Packages
5. 点击 "Generate token"
6. **重要：** 复制并保存 token，只会显示一次！

### 2. 配置 local.properties

在项目根目录的 `local.properties` 文件中添加（该文件不应提交到 Git）：

```properties
# GitHub Packages 发布凭据
gpr.user=你的GitHub用户名
gpr.key=刚才创建的Personal Access Token
```

或者在命令行中设置环境变量（推荐，更安全）：

```bash
# macOS/Linux
export GITHUB_USER=你的GitHub用户名
export GITHUB_TOKEN=你的Personal_Access_Token

# Windows PowerShell
$env:GITHUB_USER="你的GitHub用户名"
$env:GITHUB_TOKEN="你的Personal_Access_Token"
```

---

## 发布流程

### 步骤 1：确认版本号

检查 `FloatMenu/build.gradle` 中的版本号：

```gradle
version = "2.4.0"
```

### 步骤 2：构建 Release

```bash
./gradlew :FloatMenu:assembleRelease
```

### 步骤 3：发布到 GitHub Packages

```bash
./gradlew :FloatMenu:publishReleasePublicationToGitHubPackagesRepository
```

或者发布所有产物（包括 sources）：

```bash
./gradlew :FloatMenu:publish
```

### 步骤 4：验证发布

1. 访问：https://github.com/fanOfDemo/FloatMenuSample/packages
2. 你应该能看到 `FloatMenu` 包
3. 点击包查看版本和详情

---

## 使用方式

### 消费者如何使用

在其他项目的 `settings.gradle` 中添加：

```gradle
dependencyResolutionManagement {
    repositories {
        maven {
            url = "https://maven.pkg.github.com/fanOfDemo/FloatMenuSample"
            credentials {
                // 方式一：使用用户名和 Token（不推荐，会暴露凭据）
                username = "你的GitHub用户名"
                password = "你的GitHub Token"

                // 方式二：使用环境变量（推荐）
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

在 `build.gradle` 中添加依赖：

```gradle
dependencies {
    implementation 'com.github.fanofdemo:FloatMenu:2.4.0'
}
```

### 公开包访问

如果你希望包能被公开访问（不需要认证），需要在 GitHub 上设置包为公开：

1. 访问：https://github.com/fanOfDemo/FloatMenuSample/packages
2. 点击 `FloatMenu` 包
3. 点击 "Package settings"
4. 滚动到 "Danger Zone"
5. 点击 "Change visibility"
6. 选择 "Public"

---

## 常见问题

### Q1: 发布失败，提示 401 Unauthorized

**原因：** Token 无效或权限不足

**解决：**
1. 检查 `local.properties` 中的凭据是否正确
2. 确认 Token 有 `write:packages` 权限
3. 尝试重新生成 Token

### Q2: 发布失败，提示 404 Not Found

**原因：** 仓库名称或 URL 配置错误

**解决：**
检查 `build.gradle` 中的 URL 是否正确：
```gradle
url = uri("https://maven.pkg.github.com/fanOfDemo/FloatMenuSample")
```

### Q3: 用户无法下载包

**原因：** 包未设置为公开或凭据问题

**解决：**
1. 将包设置为公开（见上文"公开包访问"）
2. 或确保用户有正确的 GitHub Token

### Q4: 想删除已发布的版本

**解决：**
1. 访问：https://github.com/fanOfDemo/FloatMenuSample/packages
2. 点击 `FloatMenu` 包
3. 找到要删除的版本
4. 点击版本右侧的 "..." 菜单
5. 选择 "Delete version"

---

## 版本管理

### 发布新版本

1. 更新 `FloatMenu/build.gradle` 中的版本号
2. 更新 `CHANGELOG.md`
3. 提交代码：`git commit -m "chore: bump version to x.x.x"`
4. 推送：`git push`
5. 创建 tag：`git tag v2.4.1`
6. 推送 tag：`git push origin v2.4.1`
7. 发布：`./gradlew :FloatMenu:publish`

### 快速发布脚本

创建 `publish.sh` 脚本：

```bash
#!/bin/bash

# 设置版本号
VERSION="2.4.0"

# 更新版本号（可选）
sed -i '' "s/version = \".*\"/version = \"$VERSION\"/" FloatMenu/build.gradle

# 提交版本更新
git add FloatMenu/build.gradle
git commit -m "chore: bump version to $VERSION"

# 创建并推送 tag
git tag v$VERSION
git push origin master
git push origin v$VERSION

# 发布到 GitHub Packages
./gradlew :FloatMenu:publishReleasePublicationToGitHubPackagesRepository

echo "✅ Release $VERSION published successfully!"
```

使用：

```bash
chmod +x publish.sh
./publish.sh
```

---

## CI/CD 集成

### GitHub Actions 自动发布

创建 `.github/workflows/release.yml`：

```yaml
name: Release to GitHub Packages

on:
  push:
    tags:
      - 'v*.*.*'

jobs:
  release:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Publish to GitHub Packages
        run: ./gradlew :FloatMenu:publish
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

这样每次推送 tag 时会自动发布到 GitHub Packages。

---

## 相关链接

- [GitHub Packages 官方文档](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
- [Gradle Maven Publish 插件文档](https://docs.gradle.org/current/userguide/publishing_maven.html)
- [项目主页](https://github.com/fanOfDemo/FloatMenuSample)

---

## 许可证

BSD 3-Clause License
