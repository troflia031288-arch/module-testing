package office;

import main.java.office.Service;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    private Service service;

    @BeforeEach
    public void setUp() {
        service = new Service();
        service.createDB();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            con.createStatement().executeUpdate("DROP ALL OBJECTS");
        }
    }

    @Test
    public void testDeleteDepartment() {
        service.deleteDepartment(2); // Удаляем IT отдел
        assertEquals(2, service.countEmployeesInDepartment(1));
        assertEquals(0, service.countEmployeesInDepartment(2));
    }

    @Test
    public void testUpdateEmployeeDepartment() {
        int updatedRows = service.updateEmployeeDepartment("Ann", 3);
        assertEquals(1, updatedRows);
        assertEquals(0, service.countEmployeesInDepartment(1));
        assertEquals(1, service.countEmployeesInDepartment(3));
    }

    @Test
    public void testCapitalizeEmployeeNames() {
        int correctedNamesCount = service.capitalizeEmployeeNames();
        assertEquals(1, correctedNamesCount);
        assertEquals("Todd", service.getEmployeeNameById(5));
    }

    @Test
    public void testCountEmployeesInDepartment() {
        int countInIT = service.countEmployeesInDepartment(2);
        assertEquals(2, countInIT);
    }
}