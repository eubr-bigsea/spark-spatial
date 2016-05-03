package eu.br.bigsea.benchmark

import org.datasyslab.geospark._
import org.datasyslab.geospark.spatialRDD._

import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.SparkConf

object GeoSparkEval {
  def main(args: Array[String]) {

    val inputFile = args(0)

    // spark configuration
    val conf = new SparkConf().
    setAppName("GeoSpark Evaluation").
    setMaster("local[*]")

    val sc = new JavaSparkContext(conf) // magellan uses java api :/
    val points = new PointRDD (
      sc,
      inputFile,
      1,     // offset of the line for coordinates
      "csv", // file format
      100)   // number of partitions

    // we can access the RDD of points directly
    println (points.getRawPointRDD.count)
    println (points.getRawPointRDD.first)

    // JSON serializer crashes :/
    // points.saveAsGeoJSON ("file:///tmp/onibus.geojson") 

    sc.stop
  }
}
