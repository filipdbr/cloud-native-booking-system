# Cloud-Native Seat Reservation System: Architecture & Roadmap

> **Status: Work In Progress (WIP)**  
> This project is currently under active development. The architecture, manifests, and deployment pipelines are being built out incrementally.

This document serves as a guide for building a multi-module Seat Reservation System. The goal of this sandbox project is to move past the basics and get hands-on experience with Docker optimization, Kubernetes orchestration, Ansible automation, advanced CI/CD pipelines, and Infrastructure as Code using Terraform on Azure.

---

## 1. System Architecture & Modules

The application is built using a microservices pattern to mimic what a production-grade cloud environment actually looks like.

*   Frontend (UI): A lightweight web interface packaged with Nginx (or a straightforward Java Thymeleaf setup) that renders the seating map and captures user selections.
*   Logic Layer (Reservation API): A Java microservice (built with Spring Boot or Quarkus) that handles the core reservation business logic, validation rules, and handles the REST endpoints.
*   Fast Cache Layer (Redis): An in-memory data store used to hold short-term locks on seats (e.g., holding a seat for 5 minutes) to prevent double-bookings and race conditions.
*   Persistent Database (PostgreSQL): The source of truth that stores finalized, completed, and confirmed reservation records.
*   Analytics Layer (Reporting Service): An independent Java background worker that pulls data from the PostgreSQL database to generate occupancy metrics for an admin dashboard.

### Traffic Flow Summary

```
Internet
   │
   ▼
AKS LoadBalancer (public IP)
   │
   ▼
nginx-ingress controller
   ├── /api/*  ──► reservation-api-svc:8080  (Spring Boot)
   └── /*      ──► frontend-svc:80           (Nginx / Thymeleaf)
                        │
                        └── internally calls reservation-api-svc
                                │
                                ├── redis-svc:6379       (internal only)
                                └── postgres-svc:5432    (internal only)
```

---

## 2. Tech Stack & What I'm Learning Here

### Docker
*   The Goal: Write efficient, production-ready container images completely from scratch.
*   What I'm practicing: Using multi-stage builds (compiling with Maven first, then shipping with a lightweight JRE Alpine runtime), optimizing .dockerignore files, leveraging layer caching, and configuring containers to run safely as a non-root user.

### Kubernetes (AKS)
*   The Goal: Learn real-world orchestration, state management, and cluster networking.
*   What I'm practicing: Writing Deployments for the stateless Java components, using PersistentVolumeClaims (PVC) to keep PostgreSQL data safe, injecting configuration via ConfigMaps and Secrets, and routing traffic with Services and an Ingress controller.

### CI/CD (GitHub Actions) & Ansible
*   The Goal: Build an automated software delivery pipeline that handles deployment gracefully.
*   What I'm practicing: Creating multi-stage GitHub Actions workflows. Instead of writing messy inline Bash scripts in the pipeline YAML, I am using Ansible inside the runner to talk to Azure, grab the K8s context, and roll out the manifests cleanly.

### Terraform & Azure
*   The Goal: Spin up managed cloud infrastructure with zero manual clicks.
*   What I'm practicing: Writing declarative code to provision an Azure Resource Group, Virtual Network (VNet), Azure Container Registry (ACR), and a minimalist, budget-friendly Azure Kubernetes Service (AKS) cluster.

---

## 3. Step-by-Step Implementation Guide

### Phase 1: Local Setup & Custom Dockerization
*   Create a single Monorepo on GitHub with clean subdirectories for the services (/frontend, /reservation-service, /analytics-service).
*   Write bare-bones Java code—just enough to get the basic endpoints functioning without getting bogged down in complex business logic.
*   Delete any boilerplates or auto-generated Dockerfiles. Write optimized, multi-stage Dockerfile manifests manually for each app.
*   Spin up the whole environment locally using docker-compose.yml to make sure all the containers talk to each other properly.

### Phase 2: Infrastructure as Code (Terraform)
*   Write the Terraform manifests to spin up the Azure resources.
*   Set up Azure Container Registry (ACR) so there is a secure, private place to push the Docker images.
*   Provision a single-node AKS cluster to keep Azure costs low while maintaining full Kubernetes capabilities.
*   Configure the network rules to ensure public traffic can only hit what it’s supposed to.

### Phase 3: Advanced CI/CD with Ansible Integration
*   Build out the GitHub Actions workflow file (.github/workflows/deploy.yml).
*   The CI Part: Lint the Dockerfiles using Hadolint, compile the Java apps, run `docker image build`, tag the images using the Git commit SHA, and push them up to ACR.
*   The CD Part: Trigger an Ansible playbook inside the runner. The playbook will authenticate with Azure, run `az aks get-credentials`, and use the native `kubernetes.core.k8s` module to deploy everything to the cluster.

### Phase 4: Kubernetes Deployments & Breaking Things
*   Write the declarative Kubernetes YAML specs for the deployments.
*   Attach persistent storage (Azure Disk or Azure File) to the PostgreSQL container via PVCs so data survives database restarts.
*   Test resilience: intentionally push a broken container image to the repo to verify that the pipeline can catch errors, handle rollbacks automatically (`kubectl rollout undo`), and maintain zero-downtime rolling updates.