/* PageRank.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.Logging

import org.apache.spark.HashPartitioner

object PageRank extends Logging {
  def main (args: Array[String]) {
    // parâmetros de entrada
    val fileName = args(0)
    val numIterations = args(1).toInt

    // SparkContext
    val conf = new SparkConf().setAppName("PageRank")
    val sc = new SparkContext(conf)


    val lines = sc.textFile (fileName)
    val links = lines.
      map ( line => line.split("\\s+").map(str => str.toInt) ).
      map ( ids => (ids(0), ids.drop(1)) ). // (página, lista de adjacência)
      partitionBy (new HashPartitioner(lines.partitions.size)).
      cache() // os links serão reutilizados a cada iteração

    var ranks = links.mapValues (_ => 1.0) // (página, 1.0)
     
    for (i <- 1 to numIterations) {
       ranks = links.join(ranks).
         flatMap { case (url, (adjList, rank)) =>
           adjList.map (dest => (dest, rank / adjList.size))
         }.
         reduceByKey(_ + _)
    }
    
    logInfo ("Ranking sample (4) = " + ranks.take(4).mkString(","))

    sc.stop()
  }
}
