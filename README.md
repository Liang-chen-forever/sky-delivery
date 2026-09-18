# Sky Delivery

Sky Delivery 是一个外卖配送平台个人实践项目，包含 Spring Boot 后端、由 Vue 构建并通过 Nginx 部署的管理端前端，以及微信小程序客户端。

## 项目结构

- `sky-take-out/`：Maven 多模块后端项目，包含 `sky-common`、`sky-pojo` 和 `sky-server`
- `frontend/`：管理端前端构建产物及本地 Nginx 配置
- `mp-weixin/`：微信小程序源码及生成的客户端资源
- `docs/消息队列延迟关单改造落地清单.md`：基于消息队列实现订单延迟关单的落地记录

## 环境要求

- JDK 8，并在 Maven 或 IDE 中正确配置
- Maven 3.8+
- MySQL 8.x 和 Redis
- 微信开发者工具
- 如果需要在本地运行管理端前端，需要 Windows 版 Nginx

## 后端启动

1. 创建 `sky_take_out` 数据库，并导入本地环境使用的数据库结构和初始化数据。
2. 将 `sky-take-out/sky-server/src/main/resources/application-dev.example.yml` 复制为 `application-dev.yml`。
3. 填写数据库、Redis、对象存储、微信和百度地图配置。复制后的配置文件仅保留在本地，已被 Git 忽略。
4. 进入 `sky-take-out/` 目录启动后端：

```bash
mvn spring-boot:run -pl sky-server -am
```

后端默认监听 `http://localhost:8080`。

## 管理端前端

管理端前端位于 `frontend/nginx-1.20.2/html/sky`。请先查看 `frontend/nginx-1.20.2/conf/nginx.conf`，然后从不包含中文字符的路径启动 Nginx，并通过配置的本地 HTTP 端口访问。Nginx 会将 API 请求转发到后端服务。

## 微信小程序

使用微信开发者工具打开 `mp-weixin/`。如有需要，请在生成的客户端配置中设置本地后端地址。仓库不包含 `project.private.config.json`，因为该文件是与开发机器相关的私有覆盖配置。

## 安全边界

本仓库已按源码审查和项目展示场景整理。生产凭据、私钥、证书、请求日志、本地数据库、IDE 配置、依赖缓存、构建产物和 Nginx 可执行文件均未提交。详细说明请参阅 [SECURITY.md](SECURITY.md)。

## 项目状态

这是一个个人实践项目。完整运行需要本地基础设施和第三方服务凭据；仓库未配置生产环境的微信支付和微信小程序参数。
