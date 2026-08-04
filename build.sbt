// Giter8Plugin is auto-enabled (trigger = allRequirements) and already applies
// ScriptedPlugin.projectSettings, plus its own overrides of scriptedDependencies
// (which renders the template) and sbtTestDirectory. Calling
// enablePlugins(ScriptedPlugin) here would apply ScriptedPlugin's settings after
// those overrides and undo both, leaving g8Test to publish and run nothing.
// The keys are still imported explicitly so they can be configured below.
import sbt.ScriptedPlugin.autoImport.{sbtTestDirectory, scriptedLaunchOpts}

name := "My Template Project"

// This build is for this Giter8 template.
// To test the template run `g8` or `Test/g8Test` from the sbt session.
// See http://www.foundweekends.org/giter8/testing.html#Using+the+Giter8Plugin for more details.
addCommandAlias("test", "Test/g8Test")

sbtTestDirectory := target.value / "sbt-test"

scriptedLaunchOpts ++= Seq(
  "-Xms1024m",
  "-Xmx2048m",
  "-XX:ReservedCodeCacheSize=256m",
  "-Xss4m",
  "-Dfile.encoding=UTF-8"
)

lazy val root = (project in file("."))
  .settings(
    Test / test := {
      val _ = (Test / g8Test).toTask("").value
    },
    resolvers += Resolver.url("typesafe", url("https://repo.typesafe.com/typesafe/ivy-releases/"))(
      Resolver.ivyStylePatterns
    )
  )
