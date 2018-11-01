# arm-java
[![Build Status](https://travis-ci.org/cpearce/arm-java.svg?branch=master)](https://travis-ci.org/cpearce/arm-java)

An implementation of association rule data mining in Java.
Frequent itemset generation via FPGrowth, and rule generation via appgenrules.

For an overview of assocation rule mining,
see Chapter 5 of Introduction to Data Mining, Kumar et al:
[Association Analysis: Basic Concepts and Algorithms](https://www-users.cs.umn.edu/~kumar001/dmbook/ch5_association_analysis.pdf).

Build with [Maven](https://maven.apache.org/index.html):
```
  mvn package
```
Run with:
```
    java -cp target/arm-1.0-SNAPSHOT.jar nz.org.pearce.arm.ARM
```

For example:
```
    java -cp target/arm-1.0-SNAPSHOT.jar nz.org.pearce.arm.ARM \
        --input datasets/kosarak.csv \
        --output rules \
        --min-support 0.05 \
        --min-confidence 0.05 \
        --min-lift 1.5
```

To run tsts:
```
    mvn test
```