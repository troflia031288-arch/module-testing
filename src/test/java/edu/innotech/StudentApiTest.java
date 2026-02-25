package edu.innotech;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudentApiTest {

    @BeforeEach
    public void prepare() {
        RestAssured.given()
                .baseUri("http://localhost:8080/student")
                .contentType(ContentType.JSON)
                .body("""
                        {
                        "id": 1,
                        "name": "Sasha",
                        "marks": [3,4,5,1]
                        }
                        """)
                .when()
                .post()
                .then()
                .statusCode(201);
    }



    @Test @SneakyThrows
    public void testGetStudentByIdReturnsStudent() {
        int id = 1;
        Student student = RestAssured.given()
                .baseUri("http://localhost:8080/student/" + id)
                .when().get()
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().as(Student.class);

        assertNotNull(student);
        Assertions.assertEquals("Sasha", student.getName());
    }

    @Test
    public void testGetStudentByIdReturns404ForNonExistentStudent() {
        int nonExistentId = 999;
        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + nonExistentId)
                .when().get()
                .then()
                .statusCode(404);
    }

    @Test @SneakyThrows
    public void testPostStudentAddsNewStudent() {
        Student newStudent = new Student(2, "Zhenya", new int[]{5, 4, 3});

        RestAssured.given()
                .baseUri("http://localhost:8080/student")
                .contentType(ContentType.JSON)
                .body(newStudent)
                .when()
                .post()
                .then()
                .statusCode(201);

        Student retrievedStudent = RestAssured.given()
                .baseUri("http://localhost:8080/student/2")
                .when().get()
                .then()
                .statusCode(200)
                .extract().as(Student.class);

        assertNotNull(retrievedStudent);
        Assertions.assertEquals("Zhenya", retrievedStudent.getName());
    }

    @Test @SneakyThrows
    public void testPostStudentUpdatesExistingStudent() {
        Student updatedStudent = new Student(1, "Ivan Ivanov", new int[]{5, 5, 5});

        RestAssured.given()
                .baseUri("http://localhost:8080/student")
                .contentType(ContentType.JSON)
                .body(updatedStudent)
                .when()
                .post()
                .then()
                .statusCode(201); // Ожидаем статус 201

        Student retrievedStudent = RestAssured.given()
                .baseUri("http://localhost:8080/student/1")
                .when().get()
                .then()
                .statusCode(200)
                .extract().as(Student.class);

        assertNotNull(retrievedStudent);
        Assertions.assertEquals("Ivan Ivanov", retrievedStudent.getName());
    }

    @Test @SneakyThrows
    public void testPostStudentReturnsNewIdForNullId() {
        Student studentWithNullId = new Student(0, "Petr Petrov", new int[]{3, 4, 5});

        int id = RestAssured.given()
                .baseUri("http://localhost:8080/student")
                .contentType(ContentType.JSON).body(studentWithNullId)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().path("id");

        Assertions.assertTrue(id > 0);
    }

    @Test @SneakyThrows
    public void testPostStudentReturns400IfNameIsEmpty() {
        Student studentWithEmptyName = new Student(3, "", new int[]{3, 4, 5});

        RestAssured.given()
                .baseUri("http://localhost:8080/student")
                .contentType(ContentType.JSON)
                .body(studentWithEmptyName)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test @SneakyThrows
    public void testDeleteStudentRemovesStudent() {
        int id = 1;

        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + id)
                .when().delete()
                .then()
                .statusCode(200); // Ожидаем статус 200 OK

        // Проверяем, что студент удален
        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + id)
                .when().get()
                .then()
                .statusCode(404);
    }

    @Test @SneakyThrows
    public void testDeleteStudentReturns404ForNonExistentStudent() {
        int nonExistentId = 999;
        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + nonExistentId)
                .when().delete()
                .then()
                .statusCode(404);
    }

    @Test @SneakyThrows
    public void testGetTopStudentReturnsEmptyBodyIfNoStudents() {
        RestAssured.given()
                .baseUri("http://localhost:8080/student/1")
                .when().delete();

        RestAssured.given()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then()
                .statusCode(200)
                .body(equalTo(""));
    }

    @Test @SneakyThrows
    public void testGetTopStudentReturnsEmptyBodyIfNoMarks() {
        // Добавим студента без оценок
        Student studentWithoutMarks = new Student(2, "Zhenya", new int[]{});

        RestAssured.given()
                .baseUri("http://localhost:8080/student")
                .contentType(ContentType.JSON)
                .body(studentWithoutMarks)
                .when()
                .post();

        RestAssured.given()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then()
                .statusCode(200)
                .body(equalTo(""));
    }

    @Test @SneakyThrows
    public void testGetTopStudentReturnsOneStudentWithHighestAverage() {
        // Добавим студентов с оценками
        Student student1 = new Student(3, "Katya", new int[]{5, 5, 4});
        Student student2 = new Student(4, "Dima", new int[]{3, 3, 3});

        RestAssured.given().baseUri("http://localhost:8080/student").contentType(ContentType.JSON).body(student1).when().post();
        RestAssured.given().baseUri("http://localhost:8080/student").contentType(ContentType.JSON).body(student2).when().post();

        RestAssured.given()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then()
                .statusCode(200)
        ;
    }

    @Test @SneakyThrows
    public void testGetTopStudentReturnsMultipleStudentsWithSameAverage() {
        Student student1 = new Student(5, "Miron", new int[]{4, 4, 4});
        Student student2 = new Student(6, "Anton", new int[]{4, 4, 4});RestAssured.given().baseUri("http://localhost:8080/student").contentType(ContentType.JSON).body(student1).when().post();
        RestAssured.given().baseUri("http://localhost:8080/student").contentType(ContentType.JSON).body(student2).when().post();

        RestAssured.given()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then()
                .statusCode(200);
    }
}