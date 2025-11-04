package dao;

import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    /**
     * Gets all courses a single student is enrolled in.
     */
    public List<Course> getCoursesForStudent(int studentId) {
        List<Course> courses = new ArrayList<>();
        // SQL JOIN to get course details from the enrollment table
        String sql = "SELECT c.course_id, c.course_name, c.instructor " +
                     "FROM courses c " +
                     "JOIN enrollment e ON c.course_id = e.course_id " +
                     "WHERE e.student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("instructor")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DAO Error: " + e.getMessage(), e);
        }
        return courses;
    }

    /**
     * Enrolls a student in a course.
     */
    public void enrollStudent(int studentId, String courseId) {
        String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DAO Error: " + e.getMessage(), e);
        }
    }

    /**
     * Drops a student from a course.
     */
    public void dropStudent(int studentId, String courseId) {
        String sql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DAO Error: " + e.getMessage(), e);
        }
    }
}