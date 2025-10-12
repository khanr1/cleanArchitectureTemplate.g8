# Clean Architecture Template

A [Giter8](https://www.foundweekends.org/giter8/) template that bootstraps a Scala 3 clean architecture stack featuring http4s on the backend and Laminar + Vite on the frontend. It gives you a multi-module layout (domain, core, delivery, persistence, main, frontend) wired together and ready for iterative development.

## Prerequisites

- Java 17 or newer (used by sbt and the backend)
- [sbt](https://www.scala-sbt.org/) 1.9.0 or newer
- [Node.js](https://nodejs.org/) 18.x or newer (npm ships with Node and is required for Vite)

## Generate a project

```bash
sbt new khanr1/template.g8
```

Answer the prompts (`name`, `organization`, `package`) and then move into the folder that was created:

```bash
cd <project-name>
```

## Install frontend dependencies (first run only)

The frontend lives under `06-frontend`. Install its toolchain once:

```bash
cd 06-frontend
npm install
cd ..
```

## Start the development stack

Use three terminals so that each long-running task can keep watching for changes:

- **Terminal A (Scala.js hot compilation)**  
  From the project root run:
  ```bash
  sbt ~fastLinkJS
  ```
  This watches the Scala.js sources and copies the generated bundle into the backend resources so the UI always serves the latest code.

- **Terminal B (HTTP server)**  
  From the project root run:
  ```bash
  sbt reStart
  ```
  The http4s server comes up on `http://localhost:8080`; a quick smoke check is `curl http://localhost:8080/api/hello`.

- **Terminal C (Frontend dev server)**  
  Move into the frontend folder and launch Vite:
  ```bash
  cd 06-frontend
  npm run dev
  ```
  Vite serves the UI on `http://localhost:5173` and proxies API calls to the backend (see `vite.config.ts` if you need to tweak ports).

Whenever you edit backend code, the sbt sessions recompile and restart automatically. Frontend changes trigger instant browser refreshes via Vite.

## Testing and useful sbt commands

- `sbt test` – runs the shared/unit test suites (Weaver + ScalaCheck).
- `sbt frontend/test` – placeholder for frontend JVM tests (add your own suites under `06-frontend/src/test`).
- `sbt run` – runs the backend once (no hot-reload).

