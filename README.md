# Java implementation of SparseHC
## A memory-efficient online hierarchical clustering algorithm

Currently, this packages contains two implementations of a sparse hierarchical clustering
- `SparseHierarchicalClusterer` is based on the source code found [here](https://github.com/qedsoftware/sparsehc) 
and [here](https://github.com/mdimura/sparsehc-dm)
- `SparseHierarchicalClustererV2` is based on the paper by 
[T.D. Nguyen, B. Schmidt, and C.K. Kwoh _"SparseHC: a memory-efficient online hierarchical clustering algorithm"_](https://www.sciencedirect.com/science/article/pii/S1877050914001781).

I highly recommend using `SparseHierarchicalClustererV2` because it has been better tested,
while `SparseHierarchicalClusterer` is known to have some issues.  

According to the paper, the empirical runtime complexity for complete-linkage is O(n^2), 
and the empirical memory complexity is O(n). Thus, this algorithm can be suitable for 
hierarchical clustering of many objects and when the available memory is scarce. 

### References
This implementation is based on the paper by [T.D. Nguyen, B. Schmidt, and C.K. Kwoh _"SparseHC: a memory-efficient online
hierarchical clustering algorithm"_](https://www.sciencedirect.com/science/article/pii/S1877050914001781) and on its C++
implementation [here](https://github.com/mdimura/sparsehc-dm).

### Remark
So far, only the __complete-linkage__ and __single-linkage__ hierarchical clustering are implemented. Feel free to implement the rest.

### Install
You can use this package in your Maven project by adding the following lines to your __pom.xml__ file:
```xml
<dependency>
    <groupId>org.du-lab.jsparsehc</groupId>
    <artifactId>jsparsehc</artifactId>
    <version>0.3.0</version>
</dependency>
```

### Example
To use the algorithm, you should create an implementation of the interface `Matrix` that provides distance values in the 
ascending order. Then, you can perform clustering as follows:
```java
Matrix matrix = ...;

SparseHierarchicalClustererV2 clusterer = new SparseHierarchicalClustererV2(matrix, new CompleteLinkage());
clusterer.cluster(scoreTolerance);
Map<Integer, Integer> labelMap = clusterer.getLabels();
```
The map `labelMap` is a collection of pairs _(index, label)_, where _index_ is a row (column) index of an object in the
distance matrix, _label_ is a label of the cluster containing that object.

### Command Line
It is possible to run the algorithm from the command line. First, create a "fat" jar file 
(i.e. with all dependencies included) by executing
```shell script
mvn package -PshadeProfile
```
This will create a jar file in folder `target/shade/`. Then, you can run this file as follows:  
```shell
java -jar target/shade/jsparsehc.jar -t THRESHOLD -i INPUTFILE -o OUTPUTFILE
```
Examples of [input file](distance-table.csv) and [output file](labels.csv) are located 
in the root directory of this repository. The input file contains lines with row ID, column ID, and 
distance values of the (sparse) distance matrix. 
E.g. the distance between point 1 and point 2 is equal 2,
distance between point 3 and point 5 is equal 1.41, etc.
The output file contains the clustering labels. I.g., cluster with label 6 contains points 0 and 1,
cluster with label 8 contains points 3, 4, and 5, etc.

### Versions

See [CHANGELOG](CHANGELOG.md)

