package com.quickschools;

public class QuickSchools2App {
    public static void main(String[] args) {
        String[] studentFields = {"name","gender"};
        Entity students = new Entity("student", studentFields);

        String[] gradeFields = {"name"};
        Entity grades = new Entity("grade", gradeFields);

        Entity[] entities = {students, grades};

        //join can be individually assigned to any two tables, which could create redundancy in global connection graph, but let's stick with this strategy
        //multiple joins would be built for a report, based on scanning report fields and joins index(registery)
        Join studentsToGrades = new Join("studentToGrade","student","grade");
        Join[] joins = {studentsToGrades};

        EntityLookup schema = EntityLookup.createSchema(entities,joins);

        String[] reportFields = {"studentName","gradeName"};
        String sql = schema.buildReportQuery(reportFields);

        System.out.println(sql);
    }
}
