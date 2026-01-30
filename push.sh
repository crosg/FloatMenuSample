#!/bin/bash

# 设置代理
export http_proxy=http://127.0.0.1:18180
export https_proxy=http://127.0.0.1:18180
export ALL_PROXY=socks5://127.0.0.1:11810

# 提示用户输入 Token
echo "=========================================="
echo "请提供 GitHub Personal Access Token"
echo "=========================================="
echo ""
echo "1. 访问: https://github.com/settings/tokens"
echo "2. 点击 'Generate new token' (classic)"
echo "3. 勾选: repo, workflow, write:packages"
echo "4. 生成后复制 Token"
echo ""
read -p "请输入 Token: " TOKEN

# 使用 Token 推送
echo ""
echo "正在推送到 https://github.com/ColdBrando/FloatMenuSample ..."
echo ""

git push https://ColdBrando:$TOKEN@github.com/ColdBrando/FloatMenuSample.git master

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 代码推送成功！"
    echo ""
    echo "现在可以推送 tag 了："
    echo ""
    echo "git tag v2.4.3"
    echo "git push https://ColdBrando:$TOKEN@github.com/ColdBrando/FloatMenuSample.git v2.4.3"
else
    echo ""
    echo "❌ 推送失败，请检查："
    echo "1. Token 是否正确"
    echo "2. 仓库是否存在: https://github.com/ColdBrando/FloatMenuSample"
    echo "3. 代理是否正常工作"
fi
