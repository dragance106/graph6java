# graph6java
<img source="https://zenodo.org/badge/DOI/10.5281/zenodo.1244001.svg">

An important part of a chemical graph theorist's research work is concerned with making observations and developing intuition about a particular research problem through extensive numerical testing. Research questions in chemical graph theory are often restricted to specific graph classes in which one is either looking for extremal values and extremal graphs of graph invariants, or graphs satisfying certain constraints, or inequalities between different invariants. Many graphs from such classes can nowadays be easily generated or 
readily downloaded from the web in nauty's graph6 format.

What is provided here is a Java framework for answering the above research questions among sets of graphs given in graph6 format, which represents unification of testing programs that we had used over the years. The framework consists of templates that can be easily customised so that the researcher's initial work should reduce just to rephrasing a question in hand within a specific template. This way one should be able to quickly prepare numerical calculations to be performed over large sets of graphs and shift focus to more creative research work instead. 

The use of templates is described in detail and illustrated on several conjectures from chemical graph theory in an associated preprint "Researcher-friendly Java framework for testing conjectures in chemical graph theory", which is currently being written.
