# Docker deployment guide

`deployment/infrastructure/docker-compose.yml` defines PostgreSQL, Redis, four JPA services, and the API gateway. This checkout contains the shared `common` module. The full stack requires the root `Dockerfile` and the five application modules described below.

## Requirements

- Docker Desktop with Docker Compose v2.
- From a clean checkout, copy `deployment/infrastructure/.env.example` to `deployment/infrastructure/.env`.
- Set a strong `POSTGRES_PASSWORD`, a unique base64 JWT key of at least 32 bytes, and a pgAdmin password (the Compose file requires it even when the tools profile is not selected).

Generate a JWT key with `openssl rand -base64 32`.

## Start the local infrastructure

Run from the repository root to start PostgreSQL and Redis:

```powershell
docker compose --env-file deployment/infrastructure/.env -f deployment/infrastructure/docker-compose.yml up -d --wait postgres redis
docker compose --env-file deployment/infrastructure/.env -f deployment/infrastructure/docker-compose.yml exec -T redis redis-cli ping
```

For Redis alone, use `up -d --wait redis`. A healthy Redis returns `PONG`. Host clients connect to `127.0.0.1:6379` by default. Containers on `pos-network` connect to `redis:6379`. Redis accepts connections from that network without a password and is configured for local development. It stores its append-only files in the `redis-data` volume.

## Start and inspect the stack

Run from the repository root:

```powershell
docker compose --env-file deployment/infrastructure/.env -f deployment/infrastructure/docker-compose.yml up --build
```

The gateway is available at `http://localhost:8080`. For detached mode, add `-d`. Include `--profile tools` to start optional pgAdmin on `http://localhost:5050`.

```powershell
docker compose --env-file deployment/infrastructure/.env -f deployment/infrastructure/docker-compose.yml ps
docker compose --env-file deployment/infrastructure/.env -f deployment/infrastructure/docker-compose.yml logs -f api-gateway
docker compose --env-file deployment/infrastructure/.env -f deployment/infrastructure/docker-compose.yml down
```

`down` removes containers and retains volumes. `down --volumes` deletes the PostgreSQL, Redis, catalog upload, and pgAdmin volumes permanently. For an additional clean DB while retaining the current one, stop the existing stack if ports conflict and set a different `COMPOSE_PROJECT_NAME`; Compose then creates an isolated volume.

## Build structure

The Docker build context is the repository root. The `build` stage installs `common` first because the root POM is not a Maven reactor, then packages user, catalog, order, inventory, and gateway. The runtime stages use Eclipse Temurin 21 JRE and a non-root account. Select an image with a Docker target such as `--target order-service`.

```powershell
docker build --target api-gateway -t pos-system/api-gateway .
```

Compose builds the same target stages and wires database URLs and backend DNS names. The gateway Docker profile uses `user-service`, `catalog-service`, `order-service`, and `inventory-service` hostnames instead of `localhost`.

## Database behavior

The four JPA services run Flyway migrations, then Hibernate validates the schema (`JPA_DDL_AUTO=validate`). Each service has a separate Flyway history table in the shared PostgreSQL database. The gateway initializes its own R2DBC tables through its schema initializer. Fresh database creation is automatic; migrations preserve records and do not clear existing databases. Read [Database and migrations](../README.md#database-and-migrations) before deploying schema changes.

## Environment settings

The full template and descriptions are in `deployment/infrastructure/.env.example`. Important values:

| Setting | Purpose |
| --- | --- |
| `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | Database identity and credentials |
| `JPA_DDL_AUTO` | Defaults to `validate`; do not use Hibernate auto-update for release deployments |
| `JWT_SECRET_BASE64`, `JWT_ISSUER`, `JWT_AUDIENCE` | Signing and validating access tokens |
| `GATEWAY_PORT`, `*_SERVICE_PORT`, `POSTGRES_PORT`, `REDIS_PORT`, `PGADMIN_PORT` | Host port overrides |
| `SPRING_R2DBC_*` | Gateway's connection to PostgreSQL |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | JDBC settings for standalone JPA service runs |

The Compose configuration has health checks for PostgreSQL and each application. The gateway waits for Postgres and all four backend services to report healthy.

## Operational boundaries

Keep backend services inaccessible to untrusted networks; catalog, order, and inventory currently rely on gateway authorization. The Compose file binds their host ports to loopback, but production network rules still need to enforce this. Do not store production credentials in `.env.example` or commit `.env`.
