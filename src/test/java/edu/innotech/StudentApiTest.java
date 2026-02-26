package edu.innotech;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
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
                .statusCode(201);

        Student retrievedStudent = RestAssured.given()
                .baseUri("http://localhost:8080/student/1")
                .when().get()
                .then()
                .statusCode(200)
                .extract().as(Student.class);

        assertNotNull(retrievedStudent);
        Assertions.assertEquals("Ivan Ivanov", retrievedStudent.getName());
    }

    @Test
    public void testPostStudentReturnsNewIdForNullId() {
        int[] marks = {3, 4, 5};

        Student studentWithNullId = new Student(0, "Alina Sidorova", marks);

        Response response = RestAssured.given()
                .basePath("/student")
                .contentType(ContentType.JSON)
                .body(studentWithNullId)
                .when()
                .post();

        Assertions.assertEquals(201, response.getStatusCode());

        Assertions.assertFalse(response.getBody().asString().isEmpty(), "Тело запроса не должно быть пустым");

        Student createdStudent = response.as(Student.class);
        int id = createdStudent.getId();

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
                .statusCode(200);

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
        Student student2 = new Student(6, "Anton", new int[]{4, 4, 4});
        RestAssured.given().baseUri("http://localhost:8080/student").contentType(ContentType.JSON).body(student1).when().post();
        RestAssured.given().baseUri("http://localhost:8080/student").contentType(ContentType.JSON).body(student2).when().post();

        RestAssured.given()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then()
                .statusCode(200);
    }
}