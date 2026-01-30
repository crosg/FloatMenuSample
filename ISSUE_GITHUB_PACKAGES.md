## 发布到 GitHub Packages 遇到问题

由于 GitHub Packages 配置复杂且需要仓库所有权，建议使用 **JitPack** 替代。

---

## 当前仓库

**新仓库地址**：https://github.com/ColdBrando/FloatMenuSample

**原仓库地址**：https://github.com/crosg/FloatMenuSample (作者已无权限)

---

## 方案一：使用 JitPack（推荐）

### 发布步骤（超简单）

1. **打 tag**
   ```bash
   git tag v2.4.0
   git push origin v2.4.0
   ```

2. **用户使用**
   ```gradle
   // settings.gradle
   repositories {
       maven { url 'https://jitpack.io' }
   }

   // build.gradle
   dependencies {
       implementation 'com.github.crosg:FloatMenuSample:v2.4.0'
   }
   ```

就是这样！JitPack 会自动从 GitHub 构建和发布。

---

## 方案二：继续使用 GitHub Packages

如果你确实想用 GitHub Packages，需要确保：

1. **确认仓库所有权**
   - 访问 https://github.com/crosg/FloatMenuSample/settings
   - 检查你是否是仓库所有者或协作者

2. **Token 权限**
   - Token 需要有 `write:packages` 和 `repo` 权限
   - 需要是 `crosg` 账户生成的 Token

3. **将仓库转移给你**
   如果你想用自己的账户（ColdBrando）：
   - 在 GitHub 上创建新仓库：`ColdBrando/FloatMenu`
   - 推送代码：
     ```bash
     git remote set-url origin https://github.com/ColdBrando/FloatMenu.git
     git push -u origin master --force
     git push origin v2.4.0
     ```

---

## 建议

**使用 JitPack**，因为它：
- 配置零
- 自动化程度高
- 社区广泛使用
- 不担心仓库所有权问题

是否改用 JitPack？
