[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/1k61TO-3)
# Learning Outcomes
- Author unit tests using JUnit 5
- Understand CI as a quality gate
- Observe how pipelines catch regressions
- Practice fix-validate-ship workflow

# Overview
You will implement a small Java program, write a test suite, and validate the system through an automated GitHub Actions pipeline.
Your main job is to ship a green build!

## Part 0 — Clone and Run
Accept the GitHub Classroom assignment: 
Clone the repository.
Run the project:
`mvn -B test`

## Part 1 — Create and Trigger a CI/CD Pipeline
Inside your repository, create the folder structure:
```
.github/
   workflows/
    ci.yaml
```

Add the following baseline pipeline:
```yaml
name: Java CI

on:
  push:
    branches: ["main"]
  pull_request:
    branches: ["main"]

jobs:
  build-test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up Java 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Build
        run: mvn -B package

      - name: Test
        run: mvn -B test

```
Commit and push your file (`ci.yaml`).
This should trigger a pipeline run in GitHub Actions:
GitHub → Your Repo → Actions

Ensure that the pipeline runs and results in a green checkmark!

## Part 2 — Implement PlaylistRecommender
Open `src/main/java/edu/pitt/se/PlaylistRecommender.java` and implement the three methods:
- `classifyEnergy(List<Integer> bpms)`
   - Return "HIGH" if avg BPM ≥ 140
   - "MEDIUM" if 100–139
   - "LOW" if < 100
   - Reject null or empty lists
- `isValidTrackTitle(String title)`
   - Checks for alphabetic characters + spaces, 1–30 chars
   - Reject null or special characters
- `normalizeVolume(int volumeDb)`
   - Clamp volume into range 0–100 (e.g., 120 -> 100, -10 -> 0)

## Part 3 — Write Tests in PlaylistRecommenderTest.java
Open `src/test/java/edu/pitt/se/PlaylistRecommenderTest.java` and implement at least one test case per method.

## Part 4 — Inspect CI/CD pipeline
Complete the pipeline and ensure that test cases are executed during every build.

Your goal:
All tests must pass (=green checkmark = shippable build).
