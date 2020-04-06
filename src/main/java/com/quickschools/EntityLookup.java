package com.quickschools;

import java.util.*;
import java.util.stream.Collectors;

import static com.quickschools.Utils.*;

public class EntityLookup {
    //Assumption are made for simplicity:
    //every table automatically has Id column invisible in reports
    //all other fields in table can be present in report

    private static EntityLookup schema = null;

    Set<Entity> entities;
    Set<Join> joins;
    Map<String, Field> reportFieldsIndex = new HashMap<String, Field>();

    private EntityLookup(Entity[] entities, Join[] joins){
        this.entities = new HashSet<>(Arrays.asList(entities));
        this.joins = new HashSet<>(Arrays.asList(joins));

        //Let's call it 'report index'
        for(Entity table: entities)
        {
            for(Field field: table.getFields()){
                reportFieldsIndex.put(field.getReportRef(),field);
            }
        }
    }

    public static EntityLookup createSchema(Entity[] entities, Join[] joins)
    {
        if(schema==null) {
            schema = new EntityLookup(entities, joins);
            return schema;
        }
        throw new IllegalStateException("schema has been created already!");
    }

    public static EntityLookup getInstance(Entity[] entities, Join[] joins)
    {
       if(schema == null) throw new IllegalStateException("schema has not been created!");
       return schema;
    }

    public String buildReportQuery(String[] reportFieldNamesParam)
    {
        //Set<String> reportRefFieldNames= new HashSet<>(Arrays.asList(reportFieldNamesParam));
        List<String> reportRefFieldNames= new ArrayList<>(Arrays.asList(reportFieldNamesParam));

        StringBuilder fieldsSelector = new StringBuilder("select ");
        StringBuilder tableSelector  = new StringBuilder(" from ");
        String reportQeury = new String();

        if(reportRefFieldNames.isEmpty()) throw new IllegalArgumentException("No fields specified");

        List<String> tableList = new ArrayList<>();
        for(String fieldName: reportRefFieldNames) {
            Field field = reportFieldsIndex.get(fieldName);
            if(field == null) throw new IllegalArgumentException(fieldName+" doesn't exists");
            String tableName = field.getTable().getName();
            tableList.add(tableName);
            fieldsSelector.append(field.getTable().getName() + "." + field.getName() + " as " + field.getReportRef() + ", ");
        }
        String startTable = tableList.get(0);
        Set<String> tables = new HashSet<>(tableList);

        if(joins.size()<tables.size()-1) throw new IllegalStateException("Schema doesn't have enough joins to connect report's entities");

        Set<Join> joinsCounter = copy(joins);
        Set<String> tableCounter = new HashSet<>();

        Set<String> currentTables = new HashSet<>();
        currentTables.add(startTable);

        tableSelector.append(startTable);
        do{
            Set<String> nextTables = new HashSet<>();
            for(String currentTable: currentTables) {
                List<Join> currentTableLeftJoins = joinsCounter.stream().filter(join -> join.getLeft() == currentTable).collect(Collectors.toList());
                List<Join> currentTableRightJoins = joinsCounter.stream().filter(join -> join.getRight() == currentTable).collect(Collectors.toList());

                for(Join join: currentTableLeftJoins)
                {
                    tableSelector.append(
                            " join "+join.getName() +  " on "+join.getLeft()+ ".id="+join.getName()+"."+join.getLeft()+"Id"+
                            " join "+join.getRight()+" on "+join.getRight()+".id="+join.getName()+"."+join.getRight()+"Id");
                    nextTables.add(join.getRight());
                }

                for(Join join: currentTableRightJoins)
                {
                    tableSelector.append(
                            " join "+join.getName()+  " on "+join.getRight()+ ".id="+join.getName()+"."+join.getRight()+"Id"+
                            " join "+join.getLeft()+" on "+join.getLeft()+".id="+join.getName()+"."+join.getLeft()+"Id");
                    nextTables.add(join.getLeft());
                }
                joinsCounter.removeAll(currentTableLeftJoins);
                joinsCounter.removeAll(currentTableRightJoins);
            }
            tableCounter.addAll(currentTables);
            tableCounter.addAll(nextTables);
            currentTables = nextTables;
        }
        while(!currentTables.isEmpty() && !joinsCounter.isEmpty() && (tableCounter.size() != tables.size()));
        if(tableCounter.size() != tables.size()) throw new IllegalStateException("Schema's joins don't cover this report");

        reportQeury = fieldsSelector.toString();
        //remove redundant comma
        return removeLastChar(reportQeury)+tableSelector.toString()+";";
    }

}
