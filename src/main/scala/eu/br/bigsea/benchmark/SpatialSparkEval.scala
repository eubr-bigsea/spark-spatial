package eu.br.bigsea.benchmark

import spatialspark._

object SpatialSparkEval {
  /** it works more like a tool, so here is an example of running command
   *  (extracted from the site):
   *  bin/spark-submit --master spark://spark_cluster:7077 --class spatialspark.main.SpatialJoinApp \
   *    spatial-spark-assembly-1.1.0-SNAPSHOT.jar --left A --geom_left 0 --right B --geom_right 0 --broadcast true --output output \
   *    --partition 1024 --predicate within 
   */
}
