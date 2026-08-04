# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repository is

This is a **Giter8 template**, not an application. The Scala/TypeScript sources under `src/main/g8/`
are *template text* — they are never compiled by this repository's build. The root build exists only
to package and test the template.

Consequence: editing a file under `src/main/g8/` gives you no compiler feedback. The only way to
check that generated code compiles is to run the template test (see below), which materializes a
real project and builds it.

## Commands

### This repository (the template)

- `sbt "Test/g8Test"` — renders the template into `target/sbt-test/<name>/scripted/` using
  `default.properties` values and runs `sbt test` inside the generated project, via scripted. This
  is the full end-to-end check and the only real compile verification. `sbt test` is aliased to it.
  A healthy run logs `Running 1 / 1 scripted tests` and takes tens of seconds; a "success" in about
  a second means it generated nothing and the result is meaningless.
- `sbt "Test/g8"` — renders the template without building it. Useful to inspect substitution output
  or to check that every `maven()` property resolves.
- The key is only defined in the `Test` scope. Note that scripted buffers the generated project's
  log (`scriptedBufferLog := true`), so its output only appears on failure.
- Requires network access: `default.properties` resolves nearly every version dynamically from
  Maven, so a rendered project pulls whatever is current.

### Generated projects (what `src/main/g8/build.sbt` provides)

Run from the generated project root:

- `sbt run` / `sbt reStart` — aliased to `main/run` and `main/reStart`; the http4s server binds
  `0.0.0.0:8080`.
- `sbt test` — Weaver + ScalaCheck suites (test framework is `weaver.framework.CatsEffect`).
- `sbt "main/testOnly *ExampleSuite"` — single suite; append `-- -o exact.test.name` for a single test.
- `sbt ~fastLinkJS` — Scala.js watch build for the frontend.
- `cd 06-frontend && npm install && npm run dev` — Vite dev server on `:5173`, proxying `/api` to
  `:8080` (`vite.config.ts`). The `@scala-js/vite-plugin-scalajs` plugin reads the sbt build one
  level up and resolves the `scalajs:main.js` import in `main.ts`, so Vite drives linking in dev.
- `sbt scalafmtAll` — formatting; both this repo and generated projects use `scalafmt` with
  `dialect = scala3`.

## Template mechanics

Two distinct placeholder syntaxes are in play, and mixing them up silently produces broken output:

- **In file and directory names**: `$package__lower,package,packaged$` and `$name__lower,word$`
  (double underscore separates the variable from its formatters). The `packaged` formatter turns a
  package name into nested directories, so `src/main/scala/$package__lower,package,packaged$/$name__lower,word$/`
  expands into the real package path.
- **Inside file contents**: `$package;format="lower,package"$` and `$name;format="lower,word"$`.

Every variable used must be declared in `src/main/g8/default.properties`. That file also carries
the dependency versions — add a new library by adding a `*_version` entry there, referencing it
from `project/Dependencies.scala` as `$foo_version$`, and wiring it into `build.sbt`.

### Version resolution

`maven(org,artifact,stable)` resolves the newest release with no version qualifier. Two rules,
both easy to get wrong:

- **The artifact must be the published Maven artifactId, suffix included.** giter8 does no
  `%%`/`%%%` inference, so it is `cats-core_3`, `laminar_sjs1_3` (Laminar is JS-only), and
  `name_2.12_1.0` for sbt plugins. For `%%%` dependencies use the JVM artifact; the version is
  shared with the `_sjs1_3` build.
- **An unresolvable property aborts the whole generation**, it is not skipped — `Maven.lookup`
  folds over `Either`, so one bad name fails everything.

There are no version ranges, so "latest stable" cannot express "newest 1.x" or "newest 3.3.x".
`sbt_version` and `scala_version` are therefore pinned by hand and commented as such: dynamic
resolution would yield sbt 2.x and drag Scala off the 3.3 LTS line. Keep them pinned. Before
bumping `scala_version`, check that the libraries still publish against a compatible TASTy version.

giter8 changed backends in 0.18.0 — earlier releases query the legacy `search.maven.org` Solr API
with `rows=10` and see only the ten most recent versions, so a library on a prerelease streak
resolves to nothing. Do not downgrade `sbt-giter8` below 0.18.0.

Because g8 processes all files under `src/main/g8/`, a literal `$` in template source has to be
escaped (`\$`) — this matters for Scala string interpolation and shell snippets.

## Architecture of the generated project

Six sbt modules, numbered to make the dependency direction visible. Dependencies point *inward*
toward the domain; nothing in `01-domain` or `02-core` knows about http4s, Vite, or storage.

```
01-domain  (crossProject JS+JVM, CrossType.Pure) — pure data types, no effects
   ├─ 02-core (JVM)      — service traits + repository algebras (the ports)
   │    ├─ 03-delivery   — http4s controllers, routing, Ember server
   │    └─ 04-persistence— repository implementations (adapters)
   │         └─ 05-main  — composition root + entry point
   └─ 06-frontend (JS)   — Laminar UI, depends on domain.js only
```

- Inter-module dependencies use `Cctt` (`"compile->compile;test->test"`, defined in
  `project/MyUtil.scala`) so test fixtures are shared down the chain.
- `01-domain` is the *only* cross-compiled module; it is the shared vocabulary between backend and
  frontend. Anything the Laminar UI needs to see must live there. Its JS variant disables tests
  (`test := {}`).
- Only `05-main` knows every module. It is the sole place where concrete implementations meet
  abstractions.

### Wiring pattern (tagless final)

Everything is abstract in `F[_]` with a `Async`/`Sync`/`Monad` constraint, constructed via `make`
smart constructors on companion objects rather than classes with constructors. Traits are sealed or
private-constructor with anonymous-subclass instantiation (`new Services[F](…) {}`), which keeps
construction funneled through `make`.

The composition chain in `05-main` is the thing to read first when adding a feature:

`Main` (`IOApp.Simple`, provides `Logger[IO]`) → `Program.make` → `AppResource.makeInMemory`
(allocates `Ref`s / connection pools as a `Resource`) → `Services.make` (bundles all service
instances) → controllers → `HttpApi.make` (merges `HttpRoutes` with `<+>`, mounts under `/api`,
adds request/response logging) → `HttpServer.make` (Ember) → `.useForever`.

Adding an endpoint therefore touches, in order: a domain type in `01-domain`, a service trait +
repository algebra in `02-core`, an adapter in `04-persistence`, a `Controller` in `03-delivery`,
and finally registration in `Services`/`Program`.

### Frontend

`06-frontend/src/main/scala/.../App.scala` uses a `@main` method with
`renderOnDomContentLoaded`; `scalaJSUseMainModuleInitializer := true` and the linker emits ES
modules with `SmallModulesFor(List("$name$"))` for fast Vite reloads. API calls go through
Airstream's `FetchStream` (`api/MessageAPI.scala`) against the Vite-proxied `/api` prefix.

## Known rough edges in the template

- `src/main/g8/build.sbt` defines `fastOptCompileCopy`/`fullOptCompileCopy` with
  `jsPath = "04-delivery/src/main/resources"`, but the delivery module is `03-delivery` and
  persistence is `04-persistence`. Those tasks copy into a directory that does not exist. The Vite
  plugin path is what actually works in dev.
- `06-frontend/index.html` still carries a `<title>CryoCompose</title>` and CDN Bootstrap links
  from an earlier project.
- `README.markdown` tells users to run `sbt new khanr1/template.g8`; the repository is
  `khanr1/cleanArchitectureTemplate.g8`.
- `sbt-revolver` stays hardcoded in the template's `project/plugins.sbt` because it is not published
  to Maven Central under a `_2.12_1.0` artifactId, so `maven()` cannot see it.
- The generated project depends on `log4cats-slf4j` only transitively, through
  `http4s-ember-server`. It compiles, but `logback-classic` is test-scoped there, so a generated app
  has no SLF4J binding at runtime and the startup banner logs nowhere until one is added.
- Do not add `enablePlugins(ScriptedPlugin)` to the root `build.sbt`. `Giter8Plugin` is auto-enabled
  and applies `ScriptedPlugin.projectSettings` plus its own overrides of `scriptedDependencies` and
  `sbtTestDirectory`; enabling it explicitly reapplies the defaults afterwards and silently reverts
  both, so `g8Test` publishes to Ivy, generates nothing, and still exits green.
