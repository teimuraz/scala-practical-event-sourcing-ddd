name := """scala-practical-event-sourcing-ddd"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).aggregate(macros).dependsOn(macros)

lazy val macros = project

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"
libraryDependencies += "com.sendgrid" % "sendgrid-java" % "4.0.1"
libraryDependencies += "com.typesafe.play" % "play-json-joda_2.12" % "2.6.3"
libraryDependencies += "org.joda" % "joda-convert" % "1.7"
libraryDependencies += "net.debasishg" %% "redisclient" % "3.4"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence" % "2.5.8"


libraryDependencies += "org.webjars" % "bootstrap" % "4.1.0"
libraryDependencies += "org.webjars" % "jquery" % "3.2.1"

// val elastic4sVersion = "5.6.0"

//libraryDependencies ++= Seq(
//  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
//  // for the tcp client
////  "com.sksamuel.elastic4s" %% "elastic4s-tcp" % elastic4sVersion,
//
//  // for the http client
//  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
//
//  // if you want to use reactive streams
//  "com.sksamuel.elastic4s" %% "elastic4s-streams" % elastic4sVersion,
//
//  "com.sksamuel.elastic4s" % "elastic4s-circe_2.12" % elastic4sVersion,
//
//  // testing
//  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test",
//  "com.sksamuel.elastic4s" %% "elastic4s-embedded" % elastic4sVersion % "test"
//)

// Elastic search mapping library
// libraryDependencies += "fr.pilato.elasticsearch" %  "elasticsearch-beyonder" % "5.1"

libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"

libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "4.11"

//---- JOOQ -------------------------
libraryDependencies += "org.jooq" % "jooq" % "3.10.5"
libraryDependencies += "org.jooq" % "jooq-codegen-maven" % "3.10.5"
libraryDependencies += "org.jooq" % "jooq-meta" % "3.10.5"

val generateJOOQ = taskKey[Seq[File]]("Generate JooQ classes")

val generateJOOQTask = (sourceManaged, fullClasspath in Compile, runner in Compile, streams) map { (src, cp, r, s) =>
  toError(r.run("org.jooq.util.GenerationTool", cp.files, Array("conf/jooq-code-generator.xml"), s.log))
  ((src / "main") ** "*.scala").get
}

generateJOOQ <<= generateJOOQTask

// ---- JOOQ END --------------------


fork := true // required for "sbt run" to pick up javaOptions


javaOptions in Test += "-Dconfig.file=conf/application.test.conf"


