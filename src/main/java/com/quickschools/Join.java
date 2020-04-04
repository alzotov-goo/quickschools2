package com.quickschools;


import java.util.Objects;

//Let's assume Joins are stndardized on  a way
//keyNames are simply formatted as [tableName]Id
public class Join {
    String name;
    String leftTableId;
    String rightTableId;

    public Join(String name, String left, String right)
    {
        this.name=name;
        this.leftTableId=left;
        this.rightTableId=right;
    }

    public String getName() {
        return name;
    }

    public String getLeft() {
        return leftTableId;
    }

    public String getRight() {
        return rightTableId;
    }

    //a,b = b,a essentially for joinTables
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Join join = (Join) o;
        return  (Objects.equals(leftTableId, join.leftTableId) && Objects.equals(rightTableId, join.rightTableId)) ||
                (Objects.equals(leftTableId, join.rightTableId) && Objects.equals(rightTableId, join.leftTableId)        );
    }

    @Override
    public int hashCode() {
        return (Objects.hash(leftTableId, rightTableId)+Objects.hash(rightTableId, leftTableId));
    }
}
