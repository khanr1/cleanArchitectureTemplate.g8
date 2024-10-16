import sbt.*

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport.*

object Dependencies {
  object Version {
    val cats = "$cats_version$"
    val catsEffect = "$catsEffect_version$"
    val iron = "$iron_version$"
    val kitten = "$kitten_version$"
    val monocle = "$monocle_version$"
    val skunk = "$skunk_version$"
    val circe = "$circe_version$"
    val squants = "$squants_version$"
    val http4s = "$http4s_version$"
    val log4cats = "$log4cat_version$"
    val laminar = "$laminar_version$"
    val chimney = "$chimney_version$"

    // test
    val scalacheck = "$scalacheck_version$"
    val weaver = "$weaver_version$"

  }

  object Library {

    val cats = Def.setting("org.typelevel" %%% "cats-core" % Version.cats)
    val catsEffect =
      Def.setting("org.typelevel" %%% "cats-effect" % Version.catsEffect)

    val circe = Def.setting("io.circe" %%% "circe-core" % Version.circe)
    val circeGeneric =
      Def.setting("io.circe" %%% "circe-generic" % Version.circe)
    val circeParser = Def.setting("io.circe" %%% "circe-parser" % Version.circe)

    val htt4sCirce =
      Def.setting("org.http4s" %%% "http4s-circe" % Version.http4s)
    val http4sClient =
      Def.setting("org.http4s" %%% "http4s-client" % Version.http4s)
    val htt4sDsl = Def.setting("org.http4s" %%% "http4s-dsl" % Version.http4s)
    val htt4sEmberServer =
      Def.setting("org.http4s" %%% "http4s-ember-server" % Version.http4s)
    val htt4sEmberClient =
      Def.setting("org.http4s" %% "http4s-ember-client" % Version.http4s)

    val chimney = Def.setting("io.scalaland" %% "chimney" % Version.chimney)

    val iron = Def.setting("io.github.iltotore" %%% "iron" % Version.iron)
    val ironCat =
      Def.setting("io.github.iltotore" %%% "iron-cats" % Version.iron)
    val ironCirce =
      Def.setting("io.github.iltotore" %%% "iron-circe" % Version.iron)
    val ironScalaC =
      Def.setting("io.github.iltotore" %%% "iron-scalacheck" % Version.iron)
    val ironSkunk =
      Def.setting("io.github.iltotore" %% "iron-skunk" % Version.iron)
    val kitten = Def.setting("org.typelevel" %% "kittens" % Version.kitten)
    val log4cats = "org.typelevel" %% "log4cats-core" % Version.log4cats
    val log4catslf4j = "org.typelevel" %% "log4cats-slf4j" % Version.log4cats
    val monocle = Def.setting("dev.optics" %% "monocle-core" % Version.monocle)
    val skunkCirce = "org.tpolecat" %% "skunk-circe" % Version.skunk
    val skunkCore = "org.tpolecat" %% "skunk-core" % Version.skunk
    val squants = Def.setting("org.typelevel" %%% "squants" % Version.squants)

    // Testing library
    val weaverCats = "com.disneystreaming" %% "weaver-cats" % Version.weaver
    val weaverDiscipline =
      "com.disneystreaming" %% "weaver-discipline" % Version.weaver
    val weaverScalaCheck =
      "com.disneystreaming" %% "weaver-scalacheck" % Version.weaver

    // UI Library
    val laminar = Def.setting("com.raquo" %%% "laminar" % Version.laminar)

  }
}
