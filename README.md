# ranim.spring.cloud2 (part 2: part 1 + course PDFs 07 to 09)

This project is **a copy of `ranim.spring.cloud` (PDFs 01 to 06) plus**:

| PDF | What was added |
|---|---|
| 07 MySQL as a Docker container | `mysqldb` container, MySQL driver, `brand-microservice` uses MySQL (database `test`) |
| 08 Dockerize the microservices | a `Dockerfile` in every module, `docker-compose.yml` (eurekaserver, configserver, brands-ms, brands-ms2, products-ms, gatewayserver), URLs through `DISCOVERY_SERVICE_URL` / `CONFIG_SERVICE_URL` |
| 09 Spring Cloud Bus + RabbitMQ, Config Monitor | `rabbitmq` container, `spring-cloud-starter-bus-amqp`, `/author` api in Product, `spring-cloud-config-monitor` in the Config Server |
| 10 | not received yet |

Names: **Department → Brand**, **Teacher → Product**, `nadhem` → `ranim`. Ports: Brand 8080, Product 8081, Eureka 8761, Config Server 9999, Gateway 8888, MySQL 3306, RabbitMQ 5672 / 15672 (management UI).

## Run with Docker (PDF 08)

1. Build the jars (once, and after each code change), from the project root:
   ```
   mvn clean package -DskipTests
   ```
   (or in IntelliJ: Maven tool window → `ranim.spring.cloud2` → Lifecycle → `clean`, then `package` with "Skip tests").
2. Start everything:
   ```
   docker compose up --build
   ```
   The start order is enforced with healthchecks (MySQL, RabbitMQ, Eureka → Config Server → Brand → Product, Gateway). Wait about 3 minutes, check with `docker compose ps`.
3. Stop: `Ctrl+C` then `docker compose down`.

Containers: `mysqldb`, `rabbitmq`, `eurekaserver`, `configserver`, `brands-ms` (8080), `brands-ms2` (8084 → second instance of Brand), `products-ms` (8081), `gatewayserver`.

## Run from IntelliJ
Start the infrastructure only: `docker compose up -d mysqldb rabbitmq`, then run the applications in this order: `ServiceRegistryApplication`, `ConfigserverApplication`, `BrandMicroserviceApplication`, `ProductMicroserviceApplication`, `GatewayserverApplication` (all URLs default to `localhost`).

## Tests

**PDF 07 – MySQL**
- http://localhost:8080/api/brands/MAC
- `docker exec -it mysqldb mysql -uroot -proot -e "select * from test.brand;"`
- Without the compose file (as in the PDF): `docker network create my-net` then
  `docker run --network my-net -p 3306:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=test mysql:8.4`

**PDF 08 – Docker**
- http://localhost:8761 → BRAND (2 instances: `brands-ms` and `brands-ms2`), PRODUCT, GATEWAYSERVER
- http://localhost:8888/api/brands/MAC and http://localhost:8888/api/products/1
- Failover: `docker stop brands-ms` then http://localhost:8888/api/products/1 still works (served by `brands-ms2`, http://localhost:8084/api/brands/MAC). `docker start brands-ms` to bring it back.

**PDF 09 – Spring Cloud Bus**
1. RabbitMQ UI: http://localhost:15672 (guest / guest)
2. http://localhost:8080/api/brands/author and http://localhost:8081/api/products/author
3. Edit `author.name` in `config-repo/brand-prod.yml` (Brand runs with the profile `prod`) and in `config-repo/product.yml` (Product has no profile), and save.
4. One call refreshes **all** the services connected to RabbitMQ (both Brand instances and Product):
   ```
   curl.exe -X POST http://localhost:8080/actuator/busrefresh
   ```
5. Check the two `/author` urls again (and http://localhost:8084/api/brands/author).

**PDF 09 – Config Monitor**
- The Config Server exposes `/monitor`. With GitHub and a webhook (hookdeck, as in the PDF), a `git push` calls it automatically: start the Config Server with `SPRING_PROFILES_ACTIVE=git` and your repository URL (`configserver/.../application.yml`).
- Without GitHub you can call it by hand (same effect as the webhook): edit `config-repo/brand-prod.yml`, then
  ```
  curl.exe -X POST -d "path=brand" http://localhost:9999/monitor
  ```
  and check http://localhost:8080/api/brands/author: it refreshes without any call to `/actuator/busrefresh`.

## Small differences from the PDFs
- Only Brand uses MySQL (PDF 07 only changes the Department service). Product keeps H2. MySQL image `mysql:8.4`.
- Brand inserts its 2 brands only if the table is empty (MySQL keeps the data between restarts).
- `brands-ms2` uses the same MySQL database, so it starts after `brands-ms`; its healthcheck uses port 8080 (the port inside the container).
- Healthchecks have `retries` and `start_period` so slow machines do not mark a service unhealthy during startup.
- The Config Server container reads `config-repo/` from a mounted folder (no GitHub repository needed). `bus-amqp` is also added to the Config Server, which `config-monitor` needs to broadcast refresh events.
- `eureka.instance.prefer-ip-address: true` in Brand and Product so the containers reach each other by IP.
- Each `Dockerfile` installs `curl` (used by the healthchecks).
