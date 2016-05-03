name := "Spark Spatial"

version := "1.0"

scalaVersion := "2.10.5"

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.4.0"

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "1.4.0"

libraryDependencies += "org.apache.spark" %% "spark-graphx" % "1.4.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.4.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.4.0"

libraryDependencies += "harsha2010" % "magellan" % "1.0.3-s_2.10" // Magellan

libraryDependencies += "me.simin" % "spatial-spark_2.10" % "1.0" // SpatialSpark

// GeoSpark is placed as a unmanaged jar in *spark-spatial-apis/lib*
// However, it depends on the following geojson library for some functionalities
//libraryDependencies += "org.wololo" % "jts2geojson" % "0.7.0"

// the following is needed in case of publishing a package to "Spark-Packages"
//spDependencies += "harsha2010/magellan:1.0.2-s_2.10" // format is spark-package-name:version
