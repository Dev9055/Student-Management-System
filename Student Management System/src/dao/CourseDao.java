package dao;

import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    public void addCourse(Course course) {
        String sql = "INSERT INTO courses (course_id, course_name, instructor) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getInstructor());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DAO Error: " + e.getMessage(), e);
        }
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course course = new Course(
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getString("instructor")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DAO Error: " + e.getMessage(), e);
        }
        return courses;
    }
    
    public void updateCourse(Course course) {
        String sql = "UPDATE courses SET course_name = ?, instructor = ? WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getInstructor());
            pstmt.setString(3, course.getCourseId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DAO Error: " + e.getMessage(), e);
        }
    }

    public void deleteCourse(String courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DAO Error: " + e.getMessage(), e);
        }
    }
}