# quickschools2

# Assumptions have been made to simplify solution:

* every entity has Id column
* joins(many*many) are named individually but key pairs have convention tabble1Id-table2Id
* joins are independent from entities, and separately mapped on them
* idea of my 'join graph algorithm' is walking thru joins until all tables participating in report are connected 
  nodes are tables - links between them are joins  
  essentially all schema joins are the graph connecting tables  
  we can walk thru it, starting from any node according to first field(it's table) of the query (if it's not connected - then that's it -  query is impossible),  
as we walk - we reduce graph by already walked paths,  
joins are seem to cummulative as if we join 2 tables, third will be join to accumulated result,  
it makes bit easier to generate SQL, all we need is just one big bundle (no need to keep track of one-o-one paths)

* Please look for comments in the code to see more insights.
