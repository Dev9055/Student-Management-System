package service;

import dao.CourseDao;
import model.Course;
import java.util.List;

public class CourseService {
    
    private CourseDao courseDao;

    public CourseService() {
        this.courseDao = new CourseDao();
    }

    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    public String addCourse(Course course) {
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty()) {
            return "Course ID cannot be empty.";
        }
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            return "Course Name cannot be empty.";
        }
        try {
            courseDao.addCourse(course);
            return null; // Success
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return "Course ID already exists.";
            }
            return e.getMessage();
        }
    }

    public String updateCourse(Course course) {
        try {
            courseDao.updateCourse(course);
            return null; // Success
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteCourse(String courseId) {
        try {
            courseDao.deleteCourse(courseId);
            return null; // Success
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}