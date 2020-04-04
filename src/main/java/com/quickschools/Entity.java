package com.quickschools;

import java.util.HashSet;
import java.util.Set;

import static com.quickschools.Utils.*;

public class Entity {
    private String name;
    //let's assume primary key is always present and is called 'id'
    //private PrimaryKey primaryKey;
    private Set<Field> fields;

    public Entity(String name, String[] fieldNameList)
    {
        this.name=name;
        //primaryKey = new PrimaryKey("id",this); //always id for simplicity
        fields = new HashSet<Field>();
        int i = 0;
        for(String fieldName: fieldNameList) //let's say field type doesn't matter and it's out of scope of this project
        {
            Field field = new Field(fieldName, this, name+up(fieldName));
            //for simplicity assumption made that future report builder will manipulate fieldNames in the format tableName+fieldName e.g
            //if table is student and field is name - report reference will be unique name studentName
            fields.add(field);
        }

    }

    public String getName() {
        return name;
    }

    /*public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }*/

    public Set<Field> getFields() {
        return fields;
    }
}
