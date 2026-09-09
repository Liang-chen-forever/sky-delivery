# Sky Delivery

Sky Delivery is an internship project for a food ordering and delivery platform. It contains a Spring Boot backend, a Vue-built administration frontend served by Nginx, and a WeChat mini-program client.

## Repository Layout

- `sky-take-out/`: Maven multi-module backend (`sky-common`, `sky-pojo`, `sky-server`)
- `frontend/`: packaged administration frontend and local Nginx configuration
- `mp-weixin/`: WeChat mini-program source and generated client assets
- `苍穹外卖-消息队列延迟关单-落地清单.md`: implementation notes for delayed order closing with a message queue

## Prerequisites

- JDK 8 or a compatible JDK configured for the project's Maven compiler settings
- Maven 3.8+
- MySQL 8.x and Redis
- WeChat Developer Tools for the mini-program client
- Nginx on Windows if serving the packaged administration frontend locally

## Backend Setup

1. Create the `sky_take_out` database and import the schema/data script used by your local environment.
2. Copy `sky-take-out/sky-server/src/main/resources/application-dev.example.yml` to `application-dev.yml`.
3. Fill in local database, Redis, object storage, WeChat, and Baidu Map values. Keep the copied file local; it is intentionally ignored by Git.
4. Start the backend from `sky-take-out/`:

```bash
mvn spring-boot:run -pl sky-server -am
```

The backend listens on `http://localhost:8080` by default.

## Administration Frontend

The packaged frontend is under `frontend/nginx-1.20.2/html/sky`. Review `frontend/nginx-1.20.2/conf/nginx.conf`, start Nginx from a path without Chinese characters, and use the configured local HTTP port. Nginx forwards API requests to the backend.

## WeChat Mini-program

Open `mp-weixin/` in WeChat Developer Tools and configure the local backend URL in the generated client configuration when needed. The checked-in `project.private.config.json` is intentionally excluded because it is a machine-specific override.

## Security Boundary

This repository is prepared for source-code review and portfolio use. Production credentials, private keys, certificates, request logs, local databases, IDE metadata, dependency caches, build output, and the Nginx executable are excluded. See [SECURITY.md](SECURITY.md) for reporting guidance.

## Status

This is an internship project. Local infrastructure and third-party credentials are required for a complete end-to-end run; payment and production WeChat integrations are not configured in this repository.
