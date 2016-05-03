# Spark Spatial APIs

This project purpose is to evaluate APIs regarding spatial data processing in
Spark.

## Requirements for running

* Java 7+
* [SBT](http://www.scala-sbt.org/0.13/docs/Manual-Installation.html) installation

## The code
Scala code goes in `src/main/scala`

Java code goes in `src/main/java`

## Compiling
You compile your application (and all codes included) with:
```
sbt compile
```
That command creates the classes (`.class`) but it does not package the
application.

## Packaging
```
sbt package
```
That command creates the classes and also packages them into a `.jar`.
This is required for application submission.
The generated jar will be located under `target/scala-X` where `X` is the scala version being used.

## Cleaning
```
sbt clean
```
That command cleans all `.class` and `.jar` files.

## Spatial libraries

The following represents an overview of the evaluation. We consider three spark
extensions that manipulate spatial data:

* [Magellan](http://spark-packages.org/package/harsha2010/magellan)
* [SpatialSpark](http://spark-packages.org/package/syoummer/SpatialSpark)
* [GeoSpark](https://github.com/DataSystemsLab/GeoSpark)

Last checked overview:

|       -       | Magellan                 | Spatial Spark | GeoSpark     |
|:-------------:|--------------------------|---------------|----------    |
| spark-version | 1.4.0                    | 1.6.2         | 1.5.2        |
| spark-project | sql                      | core          | core         |
| spark-package | yes                      | yes           | no           |
| input formats | shapefile,geojson,osm    | wkt*          | csv,tsv      |
|     scala api | yes                      | yes           | java-compat  |
|      java api | -                        | -             | yes          |
|           doc | 2                        | 1             | 3            |

*[wtk: Well-Known Text](https://en.wikipedia.org/wiki/Well-known_text)

### Magellan

Magellan operates on top of SparkSQL, which is an Spark extension for dealing
with dataframe(sql-like) datasets. Therefore it assumes an schema for the data
(which in our case may not be a problem). The downsides are the compatibility
with recent versions of the framework.

**Geometries:** Point, LineString, Polygon, MultiPoint (dataframes)

**Predicates:** Intersects, Within, Contains

**Operations:** Intersection

### GeoSpark

GeoSpark is implemented on top of Java API for Spark. It seems to have the most
succint and complete implementation of spatial operations. Their API
documentation is available in
[GeoSparkJavaDoc](http://www.public.asu.edu/~jiayu2/geospark/javadoc/index.html)

**Geometries:** PointRDD, RectangleRDD, PolygonRDD, Spatial Index (quad)

**Predicates:** Inside, Overlap, DatasetBoundary, Minimum Bounding Rectangle, Polygon Union

**Operations:** Spatial Range Query, Join Query and KNN Query

### Spatial Spark

It works more like a tool out of the box for joining spatial data. However it is
possible to leverage some implementations. This project has some methods for
custom partitioning with spatial awareness (the way data is partitioned in Spark
      is a great deal). Thus it can be useful for future development references.

The starting point for running their tool is
[SpatialJoinApp](https://github.com/syoummer/SpatialSpark/blob/master/src/main/scala/spatialspark/main/SpatialJoinApp.scala)
and also the
[README.md](https://github.com/syoummer/SpatialSpark/blob/master/README.md)
with examples of dataset joining.

**Geometries:** the ones supported by WKT format (as far as checked)

**Predicates:** within, withind, contains, intersects, overlaps or nearestd

**Operations:** Join (as far as checked)
