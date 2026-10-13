
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "introtosclafx",
    libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
      val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux")   => "linux"
        case n if n.startsWith("Mac")     => "mac"
        case n if n.startsWith("Windows") => "win"
        case _                            => throw new Exception("Unknown platform!")
      }
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "21.0.4" classifier osName)
    },
    libraryDependencies ++= Seq("org.scalafx" %% "scalafx" % "21.0.0-R32",
      "org.apache.derby" % "derby" % "10.14.2.0",
      "org.scalikejdbc" %% "scalikejdbc"       % "4.3.5",
      "com.h2database"  %  "h2"                % "2.2.224",
      "ch.qos.logback"  %  "logback-classic"   % "1.5.6")
  )
//enable for sbt-assembly
//assembly / assemblyMergeStrategy := {
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard // Discard all META-INF files
//  case PathList("reference.conf")    => MergeStrategy.concat  // Concatenate config files
//  case PathList(ps @ _*) if ps.last.endsWith(".class") => MergeStrategy.first // Take the first class file
//  case _ => MergeStrategy.first // Apply first strategy to any other file
//}
