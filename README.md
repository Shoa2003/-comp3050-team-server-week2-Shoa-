# COMP3050 — Team Server Project

## Project Overview

This is the team project for COMP3050 (Software System Development and Operations)
at Macquarie University, 2026.

We are building a **Java server** for a 2D tile-based virtual world game.
The server tracks the player's position on a map and responds to movement
and information requests from a web-based client.

The client (web browser) is provided — our team builds and deploys the server.

---

## Assessment Details

| Component               | Marks        | Type       |
| ----------------------- | ------------ | ---------- |
| Implemented APIs        | 15 marks     | Team       |
| Code Quality + Testing  | 10 marks     | Team       |
| DevOps Process          | 5 marks      | Team       |
| Individual Presentation | 20 marks     | Individual |
| **Total**               | **50 marks** |            |

**Due Date:** 5 June 2026, 23:55 (Week 13)

**Submission:** Git repository (GitHub) + face-to-face presentation

---

## What We Need to Build

### Core APIs

| Endpoint            | Method | Description                                       |
| ------------------- | ------ | ------------------------------------------------- |
| `/move?dy=DY&dx=DX` | GET    | Move the player character N/S/E/W                 |
| `/info?y=Y&x=X`     | GET    | Return map tile data around the player's location |

### `/move` Details

- Moves the player one space in a cardinal direction (N/S/E/W)
- Returns `200` with new `{y, x}` location if successful
- Returns `204` if blocked (wall, water, map edge) or invalid (diagonal, >1 space)

```json
{ "y": 0, "x": 6 }
```

### `/info` Details

- Returns an 11x11 grid of tile data around the player
- Returns `200` with tile info if successful
- Returns `204` if the requested location doesn't match the player's location

```json
{
  "y": 7,
  "x": 5,
  "top": 2,
  "left": 0,
  "bottom": 12,
  "right": 10,
  "info": [
    ["g", "g", "W", "W", "g"],
    ["_", "_", "_", "g", "g"]
  ]
}
```

### Map Tile Types

| Symbol | Tile          | Blocks Movement? |
| ------ | ------------- | ---------------- |
| `B`    | Brick wall    | ✅               |
| `S`    | Stone wall    | ✅               |
| `W`    | Water         | ✅               |
| `D`    | Door (closed) | ✅               |
| `g`    | Grass         | ❌               |
| `_`    | Dirt          | ❌               |
| `d`    | Door (open)   | ❌               |
| `w`    | Wooden boards | ❌               |
| `t`    | Tree          | ❌               |
| `s`    | Sand          | ❌               |

---

## Tech Stack

- **Language:** Java 18+
- **HTTP Server:** Java built-in HttpServer
- **Containerisation:** Docker
- **Reverse Proxy:** Nginx
- **CI/CD:** GitHub Actions
- **Cloud Deployment:** AWS (Terraform)
- **Testing:** JUnit 5 + Maven

---

## Project Task Assignment

| Task                                      | Assigned To     | Status  |
| ----------------------------------------- | --------------- | ------- |
| Map file setup and map-loading code       | Arindam         | 🔲 Todo |
| Implement `/move` endpoint                | Jaehyeok        | 🔲 Todo |
| Implement `/info` endpoint                | Abdul           | 🔲 Todo |
| Set up Docker, CI, Testing infrastructure | Shoa + Hanseong | 🔲 Todo |

---

## Development Priorities

```
Step 1: Implement /move and /info endpoints  ← most important
Step 2: Map file loading
Step 3: Docker + AWS deployment
Step 4: CI/CD automation
Step 5: Write tests
```

---

## How to Run

```bash
# Compile
javac Test.java HelloHandler.java MyHandler.java

# Run
java Test

# Visit
http://localhost:8000/test
```

### With Docker

```bash
docker build -t comp3050-server .
docker run -d -p 8000:8000 comp3050-server
```

### With Docker Compose

```bash
docker compose up -d
```

---

## Available Endpoints (Current)

### GET /test

```json
{
  "name": "Japan",
  "gold": 27,
  "silver": 14,
  "bronze": 17,
  "total": 58
}
```

### GET /hello

```json
{
  "message": "Hello from COMP3050!"
}
```

---

## Team Members and Roles

### Week 2 — Git, GitHub & CI

| Name           | Role         | Responsibilities                                                                                                      |
| -------------- | ------------ | --------------------------------------------------------------------------------------------------------------------- |
| Hanseong Park  | Team Manager | Created and managed the GitHub repository, reviewed and merged pull requests, set up CI workflow using GitHub Actions |
| Abdul Karim    | Member A     | Added a new `/hello` endpoint (HelloHandler.java)                                                                     |
| Jaehyeok Park  | Member B     | Updated and fixed the HTML client                                                                                     |
| Arindam Biswas | Member C     | Updated README documentation                                                                                          |

### Week 3 — Docker & Containerisation

| Name           | Role         | Responsibilities                                     |
| -------------- | ------------ | ---------------------------------------------------- |
| Hanseong Park  | Team Manager | Managed the repo, reviewed and merged all PRs        |
| Abdul Karim    | Member A     | Wrote the Dockerfile to containerise the Java server |
| Shoa           | Member B     | Created Docker Compose setup with Nginx              |
| Arindam Biswas | Member C     | Built and pushed the image to Docker Hub             |
| Jaehyeok Park  | Member D     | Added Docker build step to the CI workflow           |

### Week 4 — Testing & TDD

| Name          | Role         | Responsibilities                                        |
| ------------- | ------------ | ------------------------------------------------------- |
| Hanseong Park | Team Manager | Managed the repo, reviewed and merged all PRs           |
| All members   | TBD          | Set up JUnit testing, write unit tests for project code |

---

## How to Contribute

We follow a Fork and Pull Request workflow.

1. Fork the repository
2. Clone your fork
3. Create a new branch

```bash
git checkout -b your-branch-name
```

4. Make your changes
5. Commit and push

```bash
git add .
git commit -m "Description of change"
git push origin your-branch-name
```

6. Open a Pull Request → Team Manager will review and merge
