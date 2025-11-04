package dao;

import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Student model.
 * This class handles all database operations (CRUD) for students.
 */
public class StudentDao {

    /**
     * C - Create: Adds a new student to the database using a manually provided ID.
     * @param student The Student object to add (must have a valid ID).
     */
    public void addStudent(Student student) {
        String sql = "INSERT INTO students (id, name, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("DAO Error adding student: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e); 
        }
    }

    /**
     * R - Read: Retrieves a single student by their ID.
     * @param id The ID of the student to retrieve.
     * @return The Student object if found, or null if not found.
     */
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        Student student = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving student: " + e.getMessage());
            e.printStackTrace();
        }
        return student;
    }

    /**
     * R - Read: Retrieves a list of all students from the database.
     * @return A List of Student objects.
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving all students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * U - Update: Updates an existing student's information in the database.
     * @param student The Student object with updated information (must have a valid ID).
     */
    public void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setInt(3, student.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e); 
        }
    }

    /**
     * D - Delete: Deletes a student from the database based on their ID.
     * @param id The ID of the student to delete.
     */
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e); 
        }
    }

    // =========================================================================
    //  NEW METHOD FOR SEARCH
    // =========================================================================
    /**
     * R - Read: Searches for students whose name contains the search term.
     * @param name The search term to look for in student names.
     * @return A List of matching Student objects.
     */
    public List<Student> searchStudentsByName(String name) {
        List<Student> students = new ArrayList<>();
        // The SQL 'LIKE' operator with '%' wildcards finds any name that *contains* the search term.
        String sql = "SELECT * FROM students WHERE name LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the parameter with wildcards
            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching students: " + e.getMessage());
            e.printStackTrace();
        }
        return students; // Returns an empty list if no matches are found
    }
}