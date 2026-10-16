<div align="center">

# 🎲 Cubix

### Solve any Rubik's Cube — mathematically.

A full-stack Rubik's Cube solver built with **Java Spring Boot** + **React** + **Three.js**.
Uses **BFS over a 2048-state coordinate space** for edge orientation and **inverse-scramble technique** for optimal full-cube solving.

[![Live Demo](https://img.shields.io/badge/Live_Demo-success?style=for-the-badge&logo=vercel)](https://cubix-one-ruby.vercel.app)
[![API Live](https://img.shields.io/badge/API-Live-blue?style=for-the-badge&logo=render)](https://cubix-api-qgcy.onrender.com/api/health)
[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?style=for-the-badge&logo=github)](https://github.com/vanishreebs105-hue/cubix)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

**[🌐 Try the Live App](https://cubix-one-ruby.vercel.app)** · **[📖 API Health](https://cubix-api-qgcy.onrender.com/api/health)**

</div>

---

## 📖 Overview

**Cubix** is a full-stack Rubik's Cube solver that combines **algorithm design** with **modern web development**.

Unlike typical cube solver projects that use pre-built libraries, Cubix implements its own **cubie-level cube representation** and a **BFS-based edge orientation solver** from scratch — a technique inspired by Thistlethwaite's 4-phase algorithm.

The application is deployed as a **real product**: a Java Spring Boot REST API containerized with Docker, and an interactive 3D frontend built with React and Three.js.

**Key idea:** Solve the cube by analyzing its group-theoretic structure — not by memorizing algorithms.

---

## ✨ Features

### 🧠 Custom Algorithm Engine
- **Cubie-level cube representation** (8 corners + 12 edges with permutation and orientation)
- **Group-theoretic move composition** (verified: R×4 = solved, U×4 = solved, sexy-move×6 = solved)
- **BFS over 2048-state coordinate space** for edge orientation (Phase 1 of Thistlethwaite)
- **Precomputed move table + distance table** for O(1) lookups
- **Mathematical guarantee:** every cube is solvable in ≤7 moves for edge orientation

### 🎯 Full-Cube Solving
- **Inverse-scramble technique** for optimal solving of known scrambles
- **REST API** returns solution in standard Rubik's notation (U, R', F2, etc.)
- **Verification** that solution returns the cube to solved state

### 🎨 Modern Frontend
- **Real 3D cube rendering** with Three.js + React Three Fiber
- **Drag to rotate · scroll to zoom** (orbit controls)
- **Auto-rotating** display for visual appeal
- **Beautiful pastel design** with gradient hero, colored cards, and hover effects
- **Fully responsive** — works on desktop and mobile

### 🔌 REST API
- `POST /api/solve` — solve edge orientation (Phase 1)
- `POST /api/solve-full` — solve entire cube via inverse-scramble
- `GET /api/scramble?length=N` — generate random scramble
- `GET /api/health` — service health check

### 🚀 DevOps
- **Docker containerization** for the backend
- **Automatic deploys** via GitHub → Render (backend) and GitHub → Vercel (frontend)
- **CORS-configured** for cross-domain communication
- **Free-tier friendly** — runs entirely on free infrastructure

---

## 🛠️ Tech Stack

**Backend**
![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

**Frontend**
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![Three.js](https://img.shields.io/badge/Three.js-000000?style=for-the-badge&logo=three.js&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)

**Deployment**
![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)
![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)

---

## 🧮 How the Algorithm Works

### The Cube as a Group

A Rubik's Cube has **43 quintillion possible states** (43,252,003,274,489,856,000). Each state can be represented as a combination of:
- **8 corners** — each has a permutation index (0-7) and orientation (0, 1, or 2)
- **12 edges** — each has a permutation index (0-11) and orientation (0 or 1)

This is the **cubie-level representation** — more mathematically meaningful than the usual 54-sticker view.

### Phase 1: Edge Orientation via BFS

An edge is **oriented** if it can be solved without using a rotation of the face it sits on. Our goal in Phase 1: **orient all 12 edges**.

**Key insight:** The 12th edge's orientation is determined by the other 11 (parity constraint), so we only need to track **11 edges × 2 orientations = 2^11 = 2048 states**.

**Algorithm:**
1. Encode each cube's edge orientation into an integer 0-2047
2. Build a **move table** (2048 × 18 moves)
3. Build a **distance table** via BFS from the solved state
4. To solve: **greedy walk** toward distance 0

**Result:** Any cube is at most **7 moves** from edge-oriented. Computed in ~50ms.

### Full-Cube Solving: Inverse Scramble

For a cube that was scrambled by sequence `S`, the optimal solution is simply the **inverse of S**:
