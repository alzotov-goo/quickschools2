package com.quickschools;

import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportGenrationTest {

    @AfterEach
    void tearDown(){
        try {
            Field instance = EntityLookup.class.getDeclaredField("schema");
            instance.setAccessible(true);
            instance.set(null,null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test of simple 2 tables 1 join Schema")
    public void twoTablesJoinTest(){
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

        assertEquals(sql,"select student.name as studentName, grade.name as gradeName from student join studentToGrade on student.id=studentToGrade.studentId join grade on grade.id=studentToGrade.gradeId;");
    }

    @Test
    @DisplayName("Test of simple 3 tables 2 join Schema")
    public void threeTablesJoinTest(){
        String[] studentFields = {"name","gender"};
        Entity students = new Entity("student", studentFields);

        String[] gradeFields = {"name"};
        Entity grades = new Entity("grade", gradeFields);

        String[] countryFields = {"name"};
        Entity countries = new Entity("country", gradeFields);

        Entity[] entities = {students, grades, countries};

        Join studentsToGrades = new Join("studentToGrade","student","grade");
        Join countriesToGrades = new Join("studentToCountry","student","country");
        Join[] joins = {studentsToGrades,countriesToGrades};

        EntityLookup schema = EntityLookup.createSchema(entities,joins);

        String[] reportFields = {"studentName","studentGender","gradeName","countryName"};
        String sql = schema.buildReportQuery(reportFields);

        System.out.println(sql);

        assertEquals(sql,"select student.name as studentName, student.gender as studentGender, grade.name as gradeName, country.name as countryName from student join studentToCountry on student.id=studentToCountry.studentId join country on country.id=studentToCountry.countryId join studentToGrade on student.id=studentToGrade.studentId join grade on grade.id=studentToGrade.gradeId;");
    }

    @Test
    @DisplayName("Test of simple 3 tables 1 join Schema")
    void threeTablesBadJoinTest(){
        String[] studentFields = {"name","gender"};
        Entity students = new Entity("student", studentFields);

        String[] gradeFields = {"name"};
        Entity grades = new Entity("grade", gradeFields);

        String[] countryFields = {"name"};
        Entity countries = new Entity("country", gradeFields);

        Entity[] entities = {students, grades, countries};

        Join studentsToGrades = new Join("studentToGrade","student","grade");

        Join[] joins = {studentsToGrades}; //,countriesToGrades};

        EntityLookup schema = EntityLookup.createSchema(entities,joins);

        String[] reportFields = {"studentName","gradeName","countryName"};

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> {
                    String sql = schema.buildReportQuery(reportFields);
                    System.out.println(sql);
                });
    }

    //5-nodes graph is where several not conneted paths is already possible!
    @Test
    @DisplayName("Test of simple 5 tables 4 join Schema but graph is not connected")
    void fiveTablesBadJoinTest(){
        String[] studentFields = {"name","gender"};
        Entity students = new Entity("student", studentFields);

        String[] universityFields = {"name"};
        Entity universities = new Entity("university", universityFields);

        String[] countryFields = {"name"};
        Entity countries = new Entity("country", countryFields);

        String[] gradeFields = {"name"};
        Entity grades = new Entity("grade", gradeFields);

        String[] gradeDescFields = {"desc"};
        Entity gradesDescs = new Entity("gradeDesc", gradeDescFields);

        Entity[] entities = {students, grades, universities, countries, gradesDescs};

        //join can be individually assigned to any two tables, which could create redundancy in global connection graph, but let's stick with this strategy
        //multiple joins would be built for a report, based on scanning report fields and joins
        Join studentToUniversity = new Join("studentToUniversity","student","university");
        Join studentToCountry = new Join("studentToCountry","student","country");
        Join countryToUniversity = new Join("countryToUniversity","country","university");
        Join gradeToGradeDesc = new Join("gradeToGradeDesc","grade","gradeDesc");
        Join[] joins = {studentToUniversity,studentToCountry,countryToUniversity,gradeToGradeDesc};

        EntityLookup schema = EntityLookup.createSchema(entities,joins);

        String[] reportFields = {"studentName","universityName","countryName","gradeName","gradeDescDesc"};

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> {
                    String sql = schema.buildReportQuery(reportFields);
                    System.out.println(sql);
                });

    }

    @Test
    @DisplayName("Test of simple 5 tables 4 join Schema but graph is not connected, different startiing point")
    void fiveTablesBadJoinTest1(){
        String[] studentFields = {"name","gender"};
        Entity students = new Entity("student", studentFields);

        String[] universityFields = {"name"};
        Entity universities = new Entity("university", universityFields);

        String[] countryFields = {"name"};
        Entity countries = new Entity("country", countryFields);

        String[] gradeFields = {"name"};
        Entity grades = new Entity("grade", gradeFields);

        String[] gradeDescFields = {"text"};
        Entity gradesDescs = new Entity("gradeDesc", gradeDescFields);

        Entity[] entities = {students, grades, countries, universities, gradesDescs};

        //join can be individually assigned to any two tables, which could create redundancy in global connection graph, but let's stick with this strategy
        //multiple joins would be built for a report, based on scanning report fields and joins
        Join studentToUniversity = new Join("studentToUniversity","student","university");
        Join studentToCountry = new Join("studentToCountry","student","country");
        Join countryToUniversity = new Join("countryToUniversity","country","university");
        Join gradeToGradeDesc = new Join("gradeToGradeDesc","grade","gradeDesc");
        Join[] joins = {gradeToGradeDesc,studentToUniversity,studentToCountry,countryToUniversity};

        EntityLookup schema = EntityLookup.createSchema(entities,joins);

        String[] reportFields = {"gradeName","gradeDescText","studentName","universityName","countryName"};

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> {
                    String sql = schema.buildReportQuery(reportFields);
                    System.out.println(sql);
                });
    }

    @Test
    @DisplayName("Test of simple 5 tables 4 join Schema but graph is not connected, different startiing point")
    void threeFiledsFromFiveTablesJoinTest(){
        String[] studentFields = {"name","gender"};
        Entity students = new Entity("student", studentFields);

        String[] universityFields = {"name"};
        Entity universities = new Entity("university", universityFields);

        String[] countryFields = {"name"};
        Entity countries = new Entity("country", countryFields);

        String[] gradeFields = {"name"};
        Entity grades = new Entity("grade", gradeFields);

        String[] gradeDescFields = {"text"};
        Entity gradesDescs = new Entity("gradeDesc", gradeDescFields);

        Entity[] entities = {students, grades, countries, universities, gradesDescs};

        //join can be individually assigned to any two tables, which could create redundancy in global connection graph, but let's stick with this strategy
        //multiple joins would be built for a report, based on scanning report fields and joins
        Join studentToUniversity = new Join("studentToUniversity","student","university");
        Join studentToCountry = new Join("studentToCountry","student","country");
        Join countryToUniversity = new Join("countryToUniversity","country","university");
        Join gradeToGradeDesc = new Join("gradeToGradeDesc","grade","gradeDesc");
        Join[] joins = {gradeToGradeDesc,studentToUniversity,studentToCountry,countryToUniversity};

        EntityLookup schema = EntityLookup.createSchema(entities,joins);

        String[] reportFields = {"studentName","universityName","countryName"};

        String sql = schema.buildReportQuery(reportFields);
        System.out.println(sql);
        assertEquals(sql,"select student.name as studentName, university.name as universityName, country.name as countryName from student join studentToCountry on student.id=studentToCountry.studentId join country on country.id=studentToCountry.countryId join studentToUniversity on student.id=studentToUniversity.studentId join university on university.id=studentToUniversity.universityId;");
    }
}
