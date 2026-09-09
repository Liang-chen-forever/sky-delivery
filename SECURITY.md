# 安全策略

请勿向本仓库提交密码、访问密钥、API Token、私钥、证书、本地配置文件或用户数据。

如果不慎暴露凭据，请立即在对应服务商控制台中吊销或轮换凭据，然后从工作区和 Git 历史中清除相关内容，再重新发布仓库。安全事件请私下联系仓库所有者，不要直接创建公开 Issue。

`sky-take-out/sky-server/src/main/resources/application-dev.example.yml` 只包含配置占位符。实际使用的 `application-dev.yml` 和微信小程序私有项目配置已被 Git 忽略。