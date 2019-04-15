//https://github.com/pbassiner/sbt-multi-project-example/blob/master/build.sbt
name := "udemy-learning-akka"
organization in ThisBuild := "com.galaykovskiy"
scalaVersion in ThisBuild := "2.12.8"
version in ThisBuild := "1.0"

lazy val root = project
  .in(file("."))
  .aggregate(hello, playingWithActors)
  .settings(update / aggregate := false)

lazy val hello = (project in file("Hello Akka"))
  .settings(
    commonSettings,
    libraryDependencies ++= commonDependencies
  )

lazy val playingWithActors = (project in file("playing-with-actors"))
  .settings(
    commonSettings,
    libraryDependencies ++= commonDependencies,
    mainClass in(Compile, run) := Some("com.packt.akka.Creation")
  )

lazy val actorPaths = (project in file("actor-paths"))
  .settings(
    commonSettings,
    libraryDependencies ++= commonDependencies,
    mainClass in(Compile, run) := Some("com.packt.akka.App")
  )

lazy val routing = (project in file("routing"))
  .settings(
    commonSettings,
    libraryDependencies ++= commonDependencies,
    mainClass in(Compile, run) := Some("com.packt.akka.App")
  )

lazy val dependencies =
  new {
    val akkaAktorV = "2.5.21"
    //
    val akkaAktor = "com.typesafe.akka" %% "akka-actor" % akkaAktorV
  }

lazy val commonDependencies = Seq(
  dependencies.akkaAktor,
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    //    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)