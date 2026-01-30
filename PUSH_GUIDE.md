# 切换到新仓库推送指南

## 当前状态

✅ 远程仓库已切换到：`https://github.com/ColdBrando/FloatMenuSample`

## 需要完成的步骤

### 步骤 1：验证远程仓库

```bash
git remote -v
# 应该显示：
# origin  https://github.com/ColdBrando/FloatMenu.git (fetch)
# origin  https://github.com/ColdBrando/FloatMenu.git (push)
```

### 步骤 2：配置代理

```bash
export http_proxy=http://127.0.0.1:18180
export https_proxy=http://127.0.0.1:18180
export ALL_PROXY=socks5://127.0.0.1:11810
```

### 步骤 3：生成新的 Personal Access Token

1. 访问：https://github.com/settings/tokens
2. 点击 "Generate new token" (classic)
3. 勾选：
   - ✅ `repo` (Full control of private repositories)
   - ✅ `workflow` (Update GitHub Action workflows)
   - ✅ `write:packages` (Upload packages to GitHub Packages)
4. 生成后复制 Token

### 步骤 4：推送代码

**方式一：使用 Token（推荐）**

```bash
# 使用 Token 推送
git push https://ColdBrando:<你的Token>@github.com/ColdBrando/FloatMenuSample.git master
```

**方式二：使用 Git 凭据管理器（更方便）**

1. 安装 GitHub CLI：
   ```bash
   brew install gh
   ```

2. 登录：
   ```bash
   gh auth login
   ```

3. 推送：
   ```bash
   git push -u origin master
   ```

### 步骤 5：推送 Tag

```bash
git tag v2.4.0
git push origin v2.4.0
```

---

## 推送成功后的配置

### 更新 README.md 中的仓库链接

需要将所有 `fanOfDemo` 或 `crosg` 改为 `ColdBrando`

### 更新 local.properties

如果需要，可以删除旧的 GitHub Packages 配置（已清理）

---

## 验证推送成功

访问：https://github.com/ColdBrando/FloatMenuSample
