import play.sbt.PlayImport.{cacheApi, guice}

name := """product"""

version := "1.0"
lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
scalaVersion := "2.12.8"
sources in(Compile, doc) := Seq.empty
publishArtifact in(Compile, packageDoc) := false
resolvers += "Local Maven Repository" at "file:///" + Path.userHome.absolutePath + "/.ivy2/cache"
resolvers += "ali-maven" at "http://maven.aliyun.com/nexus/content/groups/public"
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "maven-central" at "http://repo1.maven.org/maven2/"
resolvers += "maven-releases" at "http://teamwork.kmtongji.com/nexus/content/groups/public"
resolvers += "jitpack" at "http://jitpack.io"
resolvers += "jboss-public-repository-group" at "http://repository.jboss.org/nexus/content/groups/public/"

libraryDependencies ++= Seq(
  cacheApi,
  javaJdbc,
  guice,
  filters,
  "mysql" % "mysql-connector-java" % "8.0.17",
  "commons-io" % "commons-io" % "2.5",
  "commons-validator" % "commons-validator" % "1.5.1",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  "com.github.bingoohuang" % "patchca" % "0.0.1",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.55",
  "com.squareup.okhttp3" % "okhttp" % "3.14.2",
  "com.squareup.okio" % "okio" % "1.14.0",
  "com.qiniu" % "happy-dns-java" % "0.1.5",
  "com.google.code.gson" % "gson" % "2.6.2",
  "com.aliyun" % "aliyun-java-sdk-core" % "3.0.9",
  "com.aliyun" % "aliyun-java-sdk-sms" % "3.0.0-rc1",
  "com.github.martinwithaar" % "encryptor4j" % "0.1",
  "com.google.zxing" % "core" % "3.3.2",
  "com.aliyun.oss" % "aliyun-sdk-oss" % "3.4.2",
  "com.github.karelcemus" %% "play-redis" % "2.6.1",
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",
  "com.typesafe.akka" %% "akka-remote" % "2.5.23",
  "com.github.romix.akka" %% "akka-kryo-serialization" % "0.5.2",
  javaWs
)
libraryDependencies += filters
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

javacOptions ++= Seq("-encoding", "UTF-8")
