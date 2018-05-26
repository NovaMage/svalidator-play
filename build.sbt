import ReleaseTransformations._

name := "svalidator-play"

organization := "com.github.novamage"

version := "0.0.2"

scalaVersion := "2.12.6"

val playVersion = "2.6.13"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % playVersion % Provided,
  "com.github.novamage" %% "svalidator" % "0.9.9"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import")

licenses := Seq("MIT" -> url("https://github.com/NovaMage/SValidator/blob/master/LICENSE.txt"))

homepage := Some(url("https://github.com/NovaMage/svalidator-play"))

scmInfo := Some(ScmInfo(
  browseUrl = url("https://github.com/NovaMage/svalidator-play"),
  connection = "scm:git@github.com:NovaMage/svalidator-play.git")
)

developers := List(
  Developer(
    id = "NovaMage",
    name = "Ángel Felipe Blanco Guzmán",
    email = "angel.softworks@gmail.com",
    url = url("https://github.com/NovaMage")
  )
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

exportJars := true

parallelExecution in Test := false

pomIncludeRepository := { _ => false }

releasePublishArtifactsAction := PgpKeys.publishSigned.value

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)