gp-mkp
======

Experiment using Genetic Programming applied to the 0-1 Multidimensional Knapsack Problem with [ECJ21 platform](http://cs.gmu.edu/~eclab/projects/ecj/ecj21.tar.gz).

Theses: [Automatic generation of algorithms using Genetic Programming for the 0-1 MKP](https://www.dropbox.com/s/j8514kdncnvcerk/Memoria%20-%20FINAL.pdf?dl=0)

Dependencies
------------

* JRE Library 1.6
* JDK 6
* ECJ v21

Optionals
---------

* Graphviz 2.38
* Eclipse 3.7.2
* Ubuntu 12.04 LTS

Running
-------

* Evolve

`java ec.Evolve -file src/model/params/mkp.params`
* Evaluation

`java ec.Evolve -file src/model/params/mkp.params -p pop.subpop.0.size=20 -p pop.subpop.0.extra-behavior=truncate -p pop.file=$data/evaluation/BestIndividual.in -p generations=1`

Results Files
---
* **BestIndividual.dot** Dot file that content the best individual to be displayed as syntax tree with Graphviz tool
* **BestIndividual.in** Plain text file that content the best individual in Lisp format
* **Statistics.out** Results of the best individual in each generation of the evolution process
* **MKPResults.out** Results of the evolve for each invidividual during the process formatted as:
  1. Generation
  2. Individual ID
  3. Instance name
  4. Number of items (N)
  5. Number of dimensions (M)
  6. Tightness Ratio
  7. Optimal known?
  8. Execution time
  9. Profit
  10. Optimal or Best
  11. Relative Error (optimal)
  12. Tree Depth
  13. Tree Size
  14. Max number of nodes permitted
  15. Relative Error (number of nodes)
  16. Fitness
  17. Hits


Others
------

* To change the directory of reading files (Instances) you must edit the path at "src/model/MKProblem.java:58", which is by default "/data/evolution"
* Evolve parameter "pop.subpop.0.size" must be the exact quantity of individuales included in the file "data/evaluation/BestIndividual.in"
* To modify other parameters you must edit some of the files in "src/model/params/*"

Author
-------

Caio Bezares Machado, IT Engineer

Universidad de Santiago de Chile, 2013
