# quickschools2

# Assumptions have been made to simplify solution:

* every entity has Id column
* joins(many*many) are named individually but key pairs have convention tabble1Id-table2Id
* joins are independent from entities, and separately mapped on them
* idea of join graph algorithm is walking thru joins until all tables participating in report are connected 

* Please look for comments in the code to see more insights.
