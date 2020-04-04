package com.quickschools;

public class Field {
    private String reportRef; //unique field name to refernce from report builder
    private String name;
    private Entity table;

    public Field(String name, Entity table, String reportReference) {
        this.reportRef = reportReference;
        this.name = name;
        this.table = table;
    }

    public String getReportRef() {
        return reportRef;
    }

    public String getName() {
        return name;
    }

    public Entity getTable() {
        return table;
    }
}
