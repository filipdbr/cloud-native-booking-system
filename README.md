# KubeReserve — Cloud-Native Seat Reservation System

> **Status: Work In Progress** — Phase 1 complete, Phase 2 (Terraform/Azure) in progress.

A sandbox project for getting hands-on experience with production-grade DevOps tooling: Docker, Kubernetes on AKS, GitHub Actions CI/CD, Ansible, and Terraform on Azure. The domain is a seat reservation system built as a microservices architecture.

---

## Architecture

```
Internet
   │
   ▼
AKS LoadBalancer (public IP)
   │
   ▼
nginx-ingress controller
   ├── /api/*  ──► reservation-service:8080   (Spring Boot)
   └── /*      ──► frontend:80                (Nginx)
                        │
                        ├── redis:6379         (internal only)
                        └── postgres:5432      (internal only)
```

### Services

| Service | Tech | Role |
|---|---|---|
| `frontend` | Nginx + HTML | Serves static UI, reverse proxies `/api/*` to backend |
| `reservation-service` | Java 21 + Spring Boot | Core reservation REST API |
| `postgres` | PostgreSQL 16 | Persistent storage for confirmed reservations |
| `redis` | Redis 7 | Short-term seat locks to prevent double-bookings |
| `analytics-service` | Java 21 (planned) | Background worker for occupancy metrics |

---

## Repository Structure

```
cloud-native-booking-system/
├── .github/
│   └── workflows/
│       ├── frontend.yml              # CI: lint, build, test frontend container
│       ├── reservation-service.yml   # CI: test, build JAR, build container
│       └── integration.yml           # CI: full stack docker-compose test
├── src/
│   ├── frontend/
│   │   ├── Dockerfile
│   │   ├── nginx.conf
│   │   ├── conf.d/
│   │   │   └── default.conf          # server block, upstream, reverse proxy rules
│   │   └── html/
│   │       ├── index.html
│   │       └── admin.html
│   ├── reservation-service/
│   │   ├── Dockerfile                # multi-stage: Maven build → JRE Alpine runtime
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/reservation/
│   │       └── test/java/reservation/
│   └── analytics-service/            # planned
├── k8s/                              # planned — Kubernetes manifests
├── terraform/                        # planned — Azure infrastructure
├── ansible/                          # planned — deployment playbooks
├── docker-compose.yml
├── .env.example
└── README.md
```

---

## Running Locally

**Prerequisites:** Docker, Docker Compose

```bash
git clone https://github.com/filipdbr/cloud-native-booking-system.git
cd cloud-native-booking-system

# create your local env file
cp .env.example .env
# edit .env and fill in the values

# build and start all services
docker compose up --build
```

| Service | URL |
|---|---|
| Frontend | http://localhost:80 |
| Reservation API | http://localhost:8080 |
| Actuator health | http://localhost:8080/actuator/health |

```bash
# stop and remove containers
docker compose down

# stop and remove containers + database volume
docker compose down -v
```

---

## CI/CD Pipelines

Three GitHub Actions workflows run automatically on push:

| Workflow | Trigger | What it does |
|---|---|---|
| `frontend.yml` | changes in `src/frontend/**` | Hadolint, build image, test HTTP responses |
| `reservation-service.yml` | changes in `src/reservation-service/**` | JUnit tests, build JAR, build image, health check |
| `integration.yml` | changes in `src/**` or `docker-compose.yml` | Full stack `docker compose up`, integration tests |

---

## Docker — Key Practices

**Multi-stage builds** — `reservation-service` compiles with a full Maven+JDK image, then ships only the JAR in a lightweight JRE Alpine image (~200MB vs ~500MB).

**Layer cache optimization** — `pom.xml` is copied and dependencies resolved before source code, so `mvn dependency:go-offline` is only re-run when dependencies change.

**Non-root users** — both containers run as unprivileged users (`nginx` and `appuser`) to follow the principle of least privilege.

---

## Roadmap

- [x] Phase 1 — Dockerization & local stack
    - [x] Multi-stage Dockerfiles for frontend and reservation-service
    - [x] Nginx reverse proxy config with upstream routing
    - [x] docker-compose with PostgreSQL, Redis, and all services
    - [x] GitHub Actions CI for all three workflows
- [ ] Phase 2 — Infrastructure as Code (Terraform)
    - [ ] Azure Resource Group, VNet, ACR
    - [ ] Single-node AKS cluster
- [ ] Phase 3 — CD with Ansible
    - [ ] Push images to ACR on merge to main
    - [ ] Ansible playbook for AKS deployment
- [ ] Phase 4 — Kubernetes manifests
    - [ ] Deployments, Services, Ingress
    - [ ] PersistentVolumeClaims for PostgreSQL
    - [ ] Rollback and zero-downtime update testing

---

## Tech Stack

`Java 21` `Spring Boot` `Maven` `Nginx` `PostgreSQL` `Redis` `Docker` `Docker Compose` `GitHub Actions` `Terraform` `Ansible` `Azure AKS` `Azure ACR`

---

## License

MIT
