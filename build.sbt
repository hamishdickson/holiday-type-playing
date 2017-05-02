name := "holiday-type-playing"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.9.0",
  "com.chuusai" %% "shapeless" % "2.3.2"
)

scalacOptions ++= Seq(
  "-Ypartial-unification"
)
