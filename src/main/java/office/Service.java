package main.java.office;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Service {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:.\\Office");
    }

    public void createDB() {
        try (Connection con = getConnection()) {
            Statement stm = con.createStatement();
            stm.executeUpdate("DROP TABLE IF EXISTS Department");
            stm.executeUpdate("CREATE TABLE Department(ID INT PRIMARY KEY, NAME VARCHAR(255))");
            stm.executeUpdate("INSERT INTO Department VALUES(1,'Accounting')");
            stm.executeUpdate("INSERT INTO Department VALUES(2,'IT')");
            stm.executeUpdate("INSERT INTO Department VALUES(3,'HR')");

            stm.executeUpdate("DROP TABLE IF EXISTS Employee");
            stm.executeUpdate("CREATE TABLE Employee(ID INT PRIMARY KEY, NAME VARCHAR(255), DepartmentID INT)");
            stm.executeUpdate("INSERT INTO Employee VALUES(1,'Pete',1)");
            stm.executeUpdate("INSERT INTO Employee VALUES(2,'Ann',1)");
            stm.executeUpdate("INSERT INTO Employee VALUES(3,'Liz',2)");
            stm.executeUpdate("INSERT INTO Employee VALUES(4,'Tom',2)");
            stm.executeUpdate("INSERT INTO Employee VALUES(5,'todd',3)");

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void deleteDepartment(int departmentId) {
        try (Connection con = getConnection()) {
            String deleteEmployeesQuery = "DELETE FROM Employee WHERE DepartmentID = ?";
            PreparedStatement deleteEmployeesStmt = con.prepareStatement(deleteEmployeesQuery);
            deleteEmployeesStmt.setInt(1, departmentId);
            deleteEmployeesStmt.executeUpdate();

            String deleteDepartmentQuery = "DELETE FROM Department WHERE ID = ?";
            PreparedStatement deleteDepartmentStmt = con.prepareStatement(deleteDepartmentQuery);
            deleteDepartmentStmt.setInt(1, departmentId);
            deleteDepartmentStmt.executeUpdate();

            System.out.println("Отдел с ID " + departmentId + " и все его сотрудники были удалены.");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int updateEmployeeDepartment(String employeeName, int newDepartmentId) {
        try (Connection con = getConnection()) {
            String selectQuery = "SELECT ID FROM Employee WHERE NAME = ?";
            PreparedStatement selectStmt = con.prepareStatement(selectQuery);
            selectStmt.setString(1, employeeName);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int employeeId = rs.getInt("ID");
                String updateQuery = "UPDATE Employee SET DepartmentID = ? WHERE ID = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, newDepartmentId);
                updateStmt.setInt(2, employeeId);
                return updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public int capitalizeEmployeeNames() {
        int count = 0;
        try (Connection con = getConnection()) {String selectQuery = "SELECT ID, NAME FROM Employee";
            PreparedStatement selectStmt = con.prepareStatement(selectQuery);
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                if (Character.isLowerCase(name.charAt(0))) {
                    String capitalizedName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    String updateQuery = "UPDATE Employee SET NAME = ? WHERE ID = ?";
                    PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                    updateStmt.setString(1, capitalizedName);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                    count++;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return count;
    }

    public int countEmployeesInDepartment(int departmentId) {
        int count = 0;
        try (Connection con = getConnection()) {
            String countQuery = "SELECT COUNT(*) AS count FROM Employee WHERE DepartmentID = ?";
            PreparedStatement countStmt = con.prepareStatement(countQuery);
            countStmt.setInt(1, departmentId);
            ResultSet rs = countStmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return count;
    }

    public String getEmployeeNameById(int i) {
    }
}