name := "mycoach-rfc2445"

organization := "com.mycoachsport"

version := IO.read(new File("VERSION")).mkString.trim + "-SNAPSHOT"

scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1")

isSnapshot := true

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

credentials += Credentials(
  file(s"${baseDirectory.value.getAbsolutePath}/nexus.credentials")
)

credentials in GlobalScope += Credentials(
  file(s"${baseDirectory.value.getAbsolutePath}/pgp.credentials")
)

pomIncludeRepository := { _ =>
  false
}

pomExtra := (<url>https://github.com/GlobalSport/mycoach-rfc2445</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>https://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:GlobalSport/mycoach-rfc2445.git</url>
      <connection>scm:git:git@github.com:GlobalSport/mycoach-rfc2445.git</connection>
    </scm>
    <developers>
      <developer>
        <id>imclem</id>
        <name>Clément Agarini</name>
        <url>https://github.com/imclem</url>
      </developer>
    </developers>)

pgpPublicRing := file("public.key")
pgpSecretRing := file("private.key")

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.1.1" % Test)
