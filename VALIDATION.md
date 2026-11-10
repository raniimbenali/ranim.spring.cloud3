# Devoir 3 — Brand, Product, resilience, Keycloak, RabbitMQ

`ranim.spring.cloud2` is unchanged. This folder is the copy for sections 11 to 14.

Department in the course is **Brand**. Teacher is **Product**. The new message service is the same idea as the course, but the event is a product notification (`ProductMessageDto`), not a teacher.

| Course | This project |
| --- | --- |
| `GET /api/departments/{code}` | `GET /api/brands/{name}` |
| `GET /api/teachers/{id}` | `GET /api/products/{id}` |
| `POST /api/teachers/create` | `POST /api/products/create` |
| `DeptFallBack` | `BrandFallBack` |
| client `dept-app` | client `brand-app` |
| role on departments | role `ADMIN` on `/api/brands/**` |
| any logged-in user on teachers | any logged-in user on `/api/products/**` |

Seed data: brands MAC (Canada, 1984) and Fenty (USA, 2017). Products 1 and 2 are the MAC lipstick and the Fenty foundation.

## What each section does

**Section 11.** Product calls Brand with OpenFeign. If Brand is down, `BrandFallBack` returns null and the product answer uses country `NOT AVAILABLE` instead of a 500. The gateway has a circuit breaker on both routes. When the breaker opens, the caller receives HTTP 503 and `An error occurred. Please contact the administrator. !`.

**Section 12.** Only the gateway checks the Keycloak token. Brand and Product stay open inside Docker. `/api/brands/**` needs realm role `ADMIN`. `/api/products/**` needs any valid token. `/actuator/**` and `/contactAdmin` stay public so healthchecks and the fallback page still work.

**Section 13.** Keycloak and its Postgres database are in Compose. Eureka, Config Server, Brand, and Product no longer publish ports, so the host must go through `localhost:8888`. Keycloak stays on `8090`. Postgres volume `kc_pg_data` keeps the client and users after a restart.

**Section 14.** `POST /api/products/create` saves the product and publishes `ProductMessageDto` to RabbitMQ exchange/queue `send-communication`. The message service runs `email` then `sms`, logs both, and publishes the product id to `communication-sent`. Product consumes that id and sets `communicationDone` to true.

## Start

Stop devoir 2 first, otherwise container names clash:

```powershell
cd C:\soa\ranim.spring.cloud2
docker compose down
cd C:\soa\ranim.spring.cloud3
mvn clean package -DskipTests
docker compose up --build
```

Published ports: gateway `8888`, Keycloak `8090`, RabbitMQ UI `15672`, message `9010`, MySQL `3306`.

## Keycloak (once)

Open `http://localhost:8090` (admin / admin). Stay in the **master** realm.

1. Clients, Create client. Client ID `brand-app`. Client authentication ON. Standard flow ON. Direct access grants ON. Valid redirect URIs `*`. Save.
2. Client, Credentials tab: copy the secret.
3. Realm roles: `ADMIN` and `USER`.
4. User `ranim` / password `ranim` (temporary OFF), realm role `ADMIN`.
5. User `visitor` / password `visitor` (temporary OFF), realm role `USER`.

The `roles` client scope is assigned by default. The token must contain `realm_access.roles`, because `KeycloakRoleConverter` reads that claim and prefixes `ROLE_`.

Token (Postman, x-www-form-urlencoded):

`POST http://localhost:8090/realms/master/protocol/openid-connect/token`

- `grant_type` = `password`
- `client_id` = `brand-app`
- `client_secret` = the secret from the Credentials tab
- `username` / `password` = `ranim` / `ranim`, or `visitor` / `visitor`

Use the `access_token` as Bearer on the gateway.

## Defense scenarios

1. No token. `GET http://localhost:8888/api/brands/MAC` → **401**.
2. Token of `visitor`. Same URL → **403**. `GET http://localhost:8888/api/products/1` → **200**, brand country `Canada`.
3. Token of `ranim`. `GET http://localhost:8888/api/brands/MAC` → **200**. `GET http://localhost:8888/api/brands/Fenty` → **200**, country `USA`.
4. Feign fallback. `docker stop brands-ms brands-ms2`, then `GET /api/products/1` with the admin token. After a few calls the body is still **200** and `brandCountry` is `NOT AVAILABLE`. `docker start brands-ms brands-ms2` afterwards.
5. Gateway fallback. `docker stop products-ms`, then call `GET /api/products/1` until the breaker opens (about half of 10 calls fail, then it stays open for 10 seconds). Answer: **503** and `An error occurred. Please contact the administrator. !`. Start Product again.
6. Functions over HTTP. `POST http://localhost:9010/email,sms` with header `Content-Type: application/json` and body:

```json
{"productId": 1, "email": "ranim@yahoo.com", "mobileNumber": "20000000"}
```

The response is the product id `1`. Message logs show `Sending email with the details` then `Sending sms with the details`. The course tests `POST /email` before the two functions are composed. In the final file they are composed as `email|sms`, so the HTTP path is `/email,sms`.

7. Full event. With the admin token, `POST http://localhost:8888/api/products/create`:

```json
{"name": "Soft Pinch Blush", "brand": "MAC", "category": "blush", "price": 25, "email": "ranim@yahoo.com", "mobileNumber": "20000000"}
```

Answer: **201** `Product created successfully`. Then `GET /api/products/3` (the new id). `communicationDone` is `true`. RabbitMQ UI `http://localhost:15672` (`guest` / `guest`) shows `send-communication` and `communication-sent`.

8. Ports and persistence. From the host, `localhost:8080` and `localhost:8081` do not answer. `docker compose down` then `docker compose up` keeps the Keycloak client and users (do not add `-v`, that deletes `kc_pg_data`).
