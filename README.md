 
# Simple Spark App (SBT)

This is a simple [SBT](http://www.scala-sbt.org/) application with Spark dependencies included.
SBT supports scala and java code.

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

## Dependencies

The dependencies are listed in `build.sbt`. So, when you compile or package the
application the SBT will download these dependecies automatically to your local
environment.

## Sample Applications

The following applications are included in the repository:
- `SampleApp.scala`: the most trivial template on Earth.
- `PageRank.scala`: a naive implementation of PageRank.

** Under construction: this repository will be updated as necessary or requested.

