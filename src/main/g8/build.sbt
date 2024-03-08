import Util.*
import Dependencies.*

ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "$scala_version$"

//Clean Architecture Multibuild

lazy val `$name;format="norm"$` =
  project
    .in(file("."))
    .aggregate(domain, core, delivery, persistence, main)
    .settings(
      name := "$name$"
    )

//Domain contains our industry specific logic. It be will be application agnostic logic.
//Most of the information there will moslikely be data structure. For example in the banking
//industry, one will have concept such as Account; Saving Account etc... that will
//be modeled in this folder.

lazy val domain =
  project
    .in(file("01-domain"))

//Core contains our application logic. In our previous example, creating and account
//or more generally CRUD.

lazy val core =
  project
    .in(file("02-core"))
    .dependsOn(domain % Cctt)
    .settings(
      libraryDependencies ++= Seq(
        Library.cats,
        Library.catsEffect,
        Library.iron
      )
    )

//Make sure to translate request model (web request; CLI) into something our core can understand.

lazy val delivery =
  project
    .in(file("03-delivery"))
    .dependsOn(core % Cctt)

//How our data are stored

lazy val persistence =
  project
    .in(file("04-persistence"))
    .dependsOn(core % Cctt)

//Entry point of the application.

lazy val main =
  project
    .in(file("05-main"))
    .dependsOn(delivery % Cctt)
    .dependsOn(core % Cctt)
    .settings(testDependencies)

//Some aliases
addCommandAlias("run", "main/run")
addCommandAlias("reStart", "main/reStart")

//Testing library
lazy val testDependencies = Seq(
  testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
  libraryDependencies ++= Seq(
    Library.weaverCats,
    Library.weaverDiscipline,
    Library.weaverScalaCheck
  )
)
