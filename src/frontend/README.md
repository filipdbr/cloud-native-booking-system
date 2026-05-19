# Frontend Module

> **Status: Work In Progress (WIP)**  
> This directory contains the user interface components and the routing layer for the application.

---

## Overview

The frontend layer is designed as a lightweight, static web server built on top of Nginx. It serves two distinct static HTML views and acts as an intelligent Reverse Proxy (API Gateway) that handles incoming client requests and routes them to internal microservices based on URI paths.

---

## Directory Structure

*   `nginx.conf`: The core configuration file defining the web server behavior, security rules, and upstream proxy routing.
*   `index.html`: The client-facing dashboard where users interact with the seat reservation mapping system.
*   `admin.html`: An isolated, secure administration view used to load system and business metrics.
*   `Dockerfile`: The instructions used to compile the production-ready container image, optimized to run as a non-root system user.

---

## Detailed Component Breakdown

### 1. User Interface Views

The frontend utilizes standard vanilla JavaScript `fetch` API methods to communicate asynchronously with the backend layers, ensuring that no complex client-side framework overhead is introduced:

*   **Client View (`index.html`)**: Triggers asynchronous requests to `/api/reservation/seats`. Nginx forwards this call directly to the internal Java reservation engine to display live layout statuses.
*   **Admin Dashboard (`admin.html`)**: Triggers asynchronous requests to `/api/analytics/metrics`. This completely bypasses the reservation flow and pulls data from the analytics worker, ensuring resource isolation.

### 2. Traffic Flow & Reverse Proxy Logic

Rather than exposing multiple backend ports to the public internet, all communication goes through a single point of entry on port 80.

*   **Static Asset Delivery**: Requests hitting `/` or `/admin` pull down the respective HTML structures directly from the local container path `/usr/share/nginx/html`.
*   **Dynamic API Routing**: Whenever the browser requests paths starting with `/api/`, Nginx automatically strips the routing alias and proxies the raw execution thread directly into the virtual application network using container service name discovery.