# Java implementation of SparseHC (Complete-linkage only)
## A memory-efficient online hierarchical clustering algorithm

### References
This implementation is based on the paper by [T.D. Nguyen, B. Schmidt, and C.K. Whoh _"SparseHC: a memory-efficient online
hierarchical clustering algorithm"_](https://www.sciencedirect.com/science/article/pii/S1877050914001781) and on its C++
implementation by [mdimura](https://github.com/mdimura/sparsehc-dm).

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
