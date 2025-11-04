package service;

import dao.EnrollmentDao;
import model.Course;
import java.util.List;

public class EnrollmentService {

    private EnrollmentDao enrollmentDao;

    public EnrollmentService() {
        this.enrollmentDao = new EnrollmentDao();
    }

    public List<Course> getCoursesForStudent(int studentId) {
        return enrollmentDao.getCoursesForStudent(studentId);
    }

    public String enrollStudent(int studentId, String courseId) {
        if (studentId <= 0 || courseId == null || courseId.trim().isEmpty()) {
            return "Invalid student or course ID.";
        }
        try {
            enrollmentDao.enrollStudent(studentId, courseId);
            return null; // Success
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return "Student is already enrolled in this course.";
            }
            return e.getMessage();
        }
    }

    public String dropStudent(int studentId, String courseId) {
        try {
            enrollmentDao.dropStudent(studentId, courseId);
            return null; // Success
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}