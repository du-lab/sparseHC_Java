# Java implementation of SparseHC (Complete-linkage only)
## A memory-efficient online hierarchical clustering algorithm

According to the paper, the empirical runtime complexity for complete-linkage is O(n^2) and the empirical memory complexity is O(n). Thus, this algorithm can be suitable for hierachical clustering of a large number of objects. 

### References
This implementation is based on the paper by [T.D. Nguyen, B. Schmidt, and C.K. Whoh _"SparseHC: a memory-efficient online
hierarchical clustering algorithm"_](https://www.sciencedirect.com/science/article/pii/S1877050914001781) and on its C++
implementation [here](https://github.com/mdimura/sparsehc-dm).

### Remark
So far, only the __complete-linkage__ hierarchical clustering is implemented. Feel free to implement the rest.

### Install
You can use this package in your Maven project by adding the following lines to your __pom.xml__ file:
```xml
<dependency>
    <groupId>org.du-lab.jsparsehc</groupId>
    <artifactId>jsparsehc</artifactId>
    <version>0.0.3</version>
</dependency>
```
