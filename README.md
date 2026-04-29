# COMP3050 — Team Server Project

## Project Overview

This is the team project for **COMP3050 – Software System Development and Operations** at Macquarie University, 2026.

### What We're Building

A **Java HTTP game server** for a 2D tile-based virtual world. The server communicates with a web-based client via a **REST API (API v3)**. The client and tile images are provided by the teaching staff — our team designs, builds, and deploys the server.

### API Endpoints

| Endpoint        | Method | Description                                            |
| --------------- | ------ | ------------------------------------------------------ |
| `/login`        | POST   | Verify name + encrypted password, return session token |
| `/logout`       | GET    | Invalidate session token                               |
| `/move?dy=&dx=` | GET    | Move character N/S/E/W                                 |
| `/info?y=&x=`   | GET    | Return map tile data around player's location          |
| `/take`         | GET    | Pick up item at player's location                      |
| `/place`        | GET    | Drop item from inventory                               |
| `/use?dy=&dx=`  | GET    | Interact with adjacent map element (e.g. open a door)  |

### Tech Stack

| Tool                     | Purpose                       |
| ------------------------ | ----------------------------- |
| Java                     | Server language               |
| Maven                    | Build & dependency management |
| Docker                   | Containerisation              |
| GitHub Actions           | CI/CD pipeline                |
| AWS EC2 (ap-southeast-2) | Cloud deployment              |
| Terraform                | Infrastructure as Code        |
| Trivy, Semgrep           | Security scanning             |

### Weekly Progress

| Week | Topic             | What We Built                                        |
| ---- | ----------------- | ---------------------------------------------------- |
| 1    | Java HTTP server  | Basic `/test` and `/hello` endpoints                 |
| 2    | Git + GitHub + CI | Team workflow, GitHub Actions pipeline               |
| 3    | Docker            | Containerised the server                             |
| 4    | JUnit testing     | Automated tests with Maven                           |
| 5    | Kubernetes        | Container orchestration                              |
| 6    | DevSecOps         | Trivy and Semgrep security scanning in CI            |
| 7    | AWS EC2           | Cloud deployment to ap-southeast-2 (Sydney)          |
| 8    | Terraform         | Infrastructure as Code, automated EC2 deployment     |

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

## API Specification (v1)

Based on `COMP3050_Team_project_2026_API_v1.pdf`.

### Core Endpoints

| Endpoint            | Method | Description                                         |
| ------------------- | ------ | --------------------------------------------------- |
| `/move?dy=DY&dx=DX` | GET    | Move the player character N/S/E/W                   |
| `/info?y=Y&x=X`     | GET    | Return 11x11 map tile data around player's location |

---

### `/move` Details

Move the player one space in a cardinal direction.

| Direction     | dy  | dx  |
| ------------- | --- | --- |
| North (W / ↑) | -1  | 0   |
| South (S / ↓) | +1  | 0   |
| West (A / ←)  | 0   | -1  |
| East (D / →)  | 0   | +1  |

**Valid requests:**

```
/move?dy=-1&dx=0   → Move North
/move?dy=-1        → Move North (dx defaults to 0)
/move?dx=+1        → Move East  (dy defaults to 0)
/move?dy=0&dx=0    → No movement (valid)
/move              → No movement (valid)
```

**Invalid requests (return 204):**

```
/move?dy=+1&dx=+1  → Diagonal — invalid
/move?dy=-2&dx=0   → More than one space — invalid
```

**Responses:**

- `200` + JSON if move succeeded:

```json
{ "y": 0, "x": 6 }
```

- `204` (empty body) if blocked (wall/water/map edge) or invalid

---

### `/info` Details

Returns an 11x11 grid of tile data centred on the player.

**Request:**

```
/info?y=7&x=5
```

**Response `200`:**

```json
{
  "y": 7,
  "x": 5,
  "top": 2,
  "left": 0,
  "bottom": 12,
  "right": 10,
  "info": [
    ["S", "w", "w", "w", "w", "S", "g", "g", "g", "W", "W"],
    ["S", "w", "w", "w", "w", "S", "g", "g", "W", "W", "g"],
    ["S", "S", "S", "w", "S", "S", "g", "g", "W", "g", "g"],
    ["g", "g", ",", "_", "g", "g", "g", "W", "W", "W", "g"],
    [".", "g", "g", "_", "g", "g", "g", "W", "g", "W", "."],
    ["_", "_", "_", "_", "g", "g", "W", "W", "g", "W", "W"],
    ["_", "g", "t", "t", "g", "W", "W", "W", "g", "g", "W"],
    ["_", "g", "g", "t", "g", "W", "W", "g", "g", "g", "W"],
    ["_", "g", "g", "g", "g", "g", "g", "g", "g", "g", "W"],
    ["_", "g", "g", "g", "g", "g", "t", "t", "g", "W", "W"],
    ["_", "g", "g", "g", "g", "g", "t", "g", "g", "W", ","]
  ]
}
```

**Response `204`** (empty body) if `y` and `x` don't match the player's current location.

---

### Map Tile Types

| Symbol | Tile          | Blocks Movement? |
| ------ | ------------- | ---------------- |
| `B`    | Brick wall    | ✅ Yes           |
| `S`    | Stone wall    | ✅ Yes           |
| `W`    | Water         | ✅ Yes           |
| `D`    | Door (closed) | ✅ Yes           |
| `g`    | Grass         | ❌ No            |
| `_`    | Dirt          | ❌ No            |
| `d`    | Door (open)   | ❌ No            |
| `w`    | Wooden boards | ❌ No            |
| `t`    | Tree          | ❌ No            |
| `s`    | Sand          | ❌ No            |
| `f`    | Flagstones    | ❌ No            |
| `p`    | Pebbles       | ❌ No            |
| `b`    | Bridge        | ❌ No            |
| `.`    | One rock      | ❌ No            |
| `,`    | Two rocks     | ❌ No            |
| `:`    | Three rocks   | ❌ No            |
| `;`    | Six rocks     | ❌ No            |

---

## Tech Stack

| Tool            | Purpose                |
| --------------- | ---------------------- |
| Java 18+        | Server language        |
| Java HttpServer | Built-in HTTP server   |
| Maven           | Build & dependency management |
| JUnit 5         | Unit testing           |
| Docker          | Containerisation       |
| Nginx           | Reverse proxy          |
| GitHub Actions  | CI/CD pipeline         |
| AWS + Terraform | Cloud deployment (IaC) |

---

## Project Task Assignment

| Task                                            | Assigned To | Status   |
| ----------------------------------------------- | ----------- | -------- |
| Map file (`map.txt`) + `GameMap.java`           | Arindam     | ✅ Done  |
| Implement `/move` endpoint (`MoveHandler.java`) | Jaehyeok    | ✅ Done  |
| Implement `/info` endpoint (`InfoHandler.java`) | Hanseong    | ✅ Done  |
| Maven setup + JUnit 5 tests                     | Hanseong    | ✅ Done  |
| CI/CD update + Docker + README                  | Hanseong    | ✅ Done  |
| AWS deployment (Terraform)                      | TBD         | 🔲 Next  |

---

## Development Priorities

```
Step 1: Implement /move and /info endpoints   ✅ Done
Step 2: Map file loading (GameMap.java)       ✅ Done
Step 3: Maven setup + JUnit tests             ✅ Done
Step 4: Docker + CI/CD update                 ✅ Done
Step 5: AWS deployment (Terraform)            ← Next
```

---

## How to Run

### Local (Maven)

```bash
# Run tests
JAVA_HOME="/c/Program Files/Eclipse Adoptium/jdk-21.0.8.9-hotspot" ./mvnw test

# Compile only
JAVA_HOME="/c/Program Files/Eclipse Adoptium/jdk-21.0.8.9-hotspot" ./mvnw compile
```

### With Docker

```bash
docker build -t comp3050-server .
docker run -d -p 8000:8000 comp3050-server
```

### With Docker Compose (includes Nginx)

```bash
docker compose up -d
```

---

## Current Endpoints

| Endpoint     | Status      | Response                             |
| ------------ | ----------- | ------------------------------------ |
| `GET /test`  | ✅ Working  | `{"name":"Japan", ...}`              |
| `GET /hello` | ✅ Working  | `{"message":"Hello from COMP3050!"}` |
| `GET /move`  | ✅ Working  | `{"y": Y, "x": X}` or 204           |
| `GET /info`  | ✅ Working  | 11x11 tile grid JSON or 204          |

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

| Name           | Role         | Responsibilities                                                 |
| -------------- | ------------ | ---------------------------------------------------------------- |
| Hanseong Park  | Team Manager | Managed the repo, reviewed and merged all PRs                    |
| Abdul Karim    | Member A     | Wrote JUnit tests for coordinate boundary handling               |
| Jaehyeok Park  | Member B     | Wrote JUnit tests for move validation logic                      |
| Arindam Biswas | Member C     | Wrote JUnit tests for map loading and tile type checks           |
| Shoa           | Member D     | Set up Maven project structure and JUnit 5 dependency in pom.xml |

### Week 5 — Core API Implementation

| Name           | Role         | Responsibilities                                                                                                  |
| -------------- | ------------ | ----------------------------------------------------------------------------------------------------------------- |
| Hanseong Park  | Team Manager | Updated CI pipeline, reviewed and merged PRs, coordinated task assignments for `/move` and `/info` implementation |
| Abdul Karim    | Member A     | Started implementation of `/info` endpoint (InfoHandler.java)                                                     |
| Jaehyeok Park  | Member B     | Started implementation of `/move` endpoint (MoveHandler.java)                                                     |
| Arindam Biswas | Member C     | Started map file creation (map.txt) and GameMap.java loader                                                       |
| Shoa           | Member D     | Maven project setup and initial JUnit test structure                                                              |

### Week 6 — Integration & CI/CD Fix

| Name          | Role         | Responsibilities                                                                                          |
| ------------- | ------------ | --------------------------------------------------------------------------------------------------------- |
| Hanseong Park | Team Manager | Implemented `/info` endpoint (InfoHandler.java), Maven structure, JUnit tests, CI/CD update, coordinated all PRs |

---

## Week 8 – Infrastructure as Code (Terraform)

### Overview

Infrastructure as Code (IaC) means defining cloud resources in configuration files rather than configuring them manually through a web console. We use **Terraform v1.14.9** to provision and manage our AWS environment, making deployments reproducible, version-controlled, and automatable — eliminating the need to click through the AWS console each time.

### What Was Automated

| Resource       | Configuration                                              |
| -------------- | ---------------------------------------------------------- |
| EC2 Instance   | t3.micro, Amazon Linux 2023, ap-southeast-2                |
| Security Group | Ports 22 (SSH), 80 (HTTP), 8000 (game server)             |
| Software setup | Docker + game server container installed via `user_data`   |

### How to Deploy

**Initialise Terraform (first time only):**

```bash
terraform init
```

**Preview changes before applying:**

```bash
terraform plan -var="key_pair_name=YOUR_KEY"
```

**Provision the infrastructure:**

```bash
terraform apply -var="key_pair_name=YOUR_KEY"
```

**Tear down all resources after testing:**

```bash
terraform destroy -var="key_pair_name=YOUR_KEY"
```

### Important Notes

- **Never commit `terraform.tfstate`** — it contains sensitive resource metadata
- **Never commit `.tfvars` files or AWS credentials** — treat them like passwords
- **Always run `terraform destroy` after testing** — idle EC2 instances consume AWS credits

### Team Task Breakdown

| Member   | Challenge   | Task                                                        |
| -------- | ----------- | ----------------------------------------------------------- |
| Hans     | 3.1 + 3.4   | Terraform tutorial setup + Git repository configuration     |
| Abdul    | 3.2         | Replace nginx with game server Docker image in Terraform    |
| Arindam  | 3.2 support | Confirm Docker image, test game server endpoint on EC2      |
| Jaehyeok | 3.3         | Add `instance_type` and `ssh_location` input variables      |

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
git commit -m "feat: description of change"
git push origin your-branch-name
```

6. Open a Pull Request → Team Manager (Hanseong) will review and merge

### Commit Message Convention

```
feat: add new feature
fix: bug fix
docs: documentation update
test: add or update tests
ci: CI/CD changes
refactor: code refactoring
```
