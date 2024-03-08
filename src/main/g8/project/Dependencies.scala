import sbt.*

object Dependencies {
  object Version {
    val cats = "$cats_version$"
    val catsEffect = "$catsEffect_version$"
    val iron = "$iron_version$"
    val kitten = "$kitten_version$"
    val monocle = "$monocle_version$"
    val skunk = "$skunk_version$"
    val weaver = "$weaver_version$"
    val circe = "$circe_version$"

  }
  object Library {
    val cats = "org.typelevel" %% "cats-core" % Version.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Version.catsEffect
    val circe = "io.circe" %% "circe-core" % Version.circe
    val iron = "io.github.iltotore" %% "iron" % Version.iron
    val ironCat = "io.github.iltotore" %% "iron-cats" % Version.iron
    val ironCirce = "io.github.iltotore" %% "iron-circe" % Version.iron
    val ironScalaC = "io.github.iltotore" %% "iron-scalacheck" % Version.iron
    val ironSkunk = "io.github.iltotore" %% "iron-skunk" % Version.iron
    val kitten = "org.typelevel" %% "kittens" % Version.kitten
    val monocle = "dev.optics" %% "monocle-core" % Version.monocle
    val skunkCirce = "org.tpolecat" %% "skunk-circe" % Version.skunk
    val skunkCore = "org.tpolecat" %% "skunk-core" % Version.skunk

    // Testing library
    val weaverCats = "com.disneystreaming" %% "weaver-cats" % Version.weaver
    val weaverDiscipline =
      "com.disneystreaming" %% "weaver-discipline" % Version.weaver
    val weaverScalaCheck =
      "com.disneystreaming" %% "weaver-scalacheck" % Version.weaver
  }
}
