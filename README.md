# COMP3050 — Team Server

## Available Endpoints

### GET /test

Handled by `MyHandler`

- Response Type: `application/json`
- Example Response:

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

Handled by `HelloHandler`

- Response Type: `application/json`
- Example Response:

```json
{
  "message": "Hello from COMP3050!"
}
```

---

## How to Run

```bash
javac Test.java HelloHandler.java MyHandler.java
java Test
```

Visit: http://localhost:8000/test

---

## Team Members and Roles

### Week 2 — Git, GitHub & CI

| Name           | Role         | Responsibilities                                                                                                      |
| -------------- | ------------ | --------------------------------------------------------------------------------------------------------------------- |
| Hanseong Park  | Team Manager | Created and managed the GitHub repository, reviewed and merged pull requests, set up CI workflow using GitHub Actions |
| Abdul Karim    | Member A     | Added a new `/hello` endpoint (HelloHandler.java)                                                                     |
| Jaehyeok Park  | Member B     | Updated and fixed the HTML client (simple_script2.html)                                                               |
| Arindam Biswas | Member C     | Updated README documentation                                                                                          |

### Week 3 — Docker & Containerisation

| Name           | Role         | Responsibilities                                                          |
| -------------- | ------------ | ------------------------------------------------------------------------- |
| Hanseong Park  | Team Manager | Managed the repo, reviewed and merged all PRs                             |
| Abdul Karim    | Member A     | Wrote the Dockerfile to containerise the Java server                      |
| Shoa           | Member B     | Created Docker Compose setup with Nginx (docker-compose.yml + nginx.conf) |
| Arindam Biswas | Member C     | Built and pushed the image to Docker Hub                                  |
| Jaehyeok Park  | Member D     | Added Docker build step to the CI workflow                                |

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
