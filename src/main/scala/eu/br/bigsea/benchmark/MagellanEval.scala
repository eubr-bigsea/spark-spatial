package eu.br.bigsea.benchmark

import org.apache.spark.Logging
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.sql.{SQLContext, Row}

import magellan.{Point, Polygon}
import org.apache.spark.sql.magellan.dsl.expressions._
import org.apache.spark.sql.types._

import java.util.Date

/**
 * This is an example of Magellan API usage. The application has no practical
 * purpose at all, thus no semantics intended.
 * However it shows common patterns for loading our data and applying operations
 * and predicates on it.
 */

// "codveiculo","latitude","longitude","data","codlinha"
case class Track(codVehicle: String, coord: Point, date: String, codLine: String)
case class SubArea(polygon: Polygon)

object MagellanEval extends Logging {
  def main(args: Array[String]) {

    // assumes format:
    // "codveiculo","latitude","longitude","data","codlinha"
    val inputFile = args(0)
   
    // spark, spark-sql common  configurations
    val conf = new SparkConf().
      setMaster ("local[*]").
      setAppName ("Magellan Evaluation")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext (sc)
    import sqlContext.implicits._

    // for our purposes and input data, we must build dataframes manually
    // furthermore we use scala case classes to represent the data
    val busTracks = sc.
      textFile (inputFile).
      flatMap { line =>
        try {
          val fields = line.replaceAll("\"", "").split (",")
          val track = Track (fields(0),
            new Point(fields(2).toDouble, fields(1).toDouble), // long, lat
            fields(3),
            fields(4))

          Iterator (track)

        } catch {
          case _: Exception =>
            logInfo (s"Could not parse line: ${line}")
            Iterator.empty
        }
      }.toDF() // magellan works with spark dataframes

    // west, east, south, north
    val origin = (Double.MaxValue, Double.MinValue, Double.MaxValue, Double.MinValue)

    // function that keep the most wide area from two sets of bounds
    def widerArea(
        a1: (Double,Double,Double,Double),
        a2: (Double,Double,Double,Double)): (Double,Double,Double,Double) = {
      (a1._1 min a2._1,
        a1._2 max a2._2,
        a1._3 min a2._3,
        a1._4 max a2._4
        )
    }

    // busTracks is a dataframe, do selection on coord column
    val coords = busTracks.select ("coord")
    // expand the wider area in the dataset (bounds that cover all points)
    val wholeArea = coords.rdd.
      aggregate (origin) (
        { case (area, Row(p : Point)) => widerArea (area, (p.x, p.x, p.y, p.y)) },
        (area1, area2) => widerArea (area1, area2)
      )

    println (s"number of coords = ${coords.count}")
    println (s"whole area = ${wholeArea}")

    // longitudinal range
    val deltaX = (wholeArea._2 - wholeArea._1).abs
    // latitudinal range
    val deltaY = (wholeArea._4 - wholeArea._3).abs

    // size of one dimension of the grid
    val numSubAreasAxis = 10

    // create polygons representing the grid
    val subAreasAxis = sc.parallelize (0 until numSubAreasAxis)
    val subAreas = subAreasAxis.cartesian (subAreasAxis).
      map { case (x,y) =>
        val (w, s) = (wholeArea._1 + x*deltaX, wholeArea._3 + y*deltaY)
        val (e, n) = (w + deltaX, s + deltaY)
        // create polygon
        val ring = Array(
          new Point(w,s), new Point(w,n), new Point(e,n), new Point(e,s),
          new Point(w,s)
          )
        SubArea(new Polygon(Array(0), ring))
      }.toDF

    println (s"number of subareas = ${subAreas.count}")

    // intersect the points in dataset with polygons in grid
    val intersects = subAreas.
      join (coords).
      where($"polygon" intersects $"coord").
      groupBy ("polygon").
      count.
      cache

    println (s"number of intersects = ${intersects.count}")
    println (s"first intersect = ${intersects.first}")

    intersects.show

    sc.stop()
  }
}
