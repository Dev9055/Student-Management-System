package model;

public class Course {
    private String courseId;
    private String courseName;
    private String instructor;

    // Constructors
    public Course() {}

    public Course(String courseId, String courseName, String instructor) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructor = instructor;
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return courseId + ": " + courseName + " (" + instructor + ")";
    }

    // This is useful for displaying in a JComboBox
    public String getDisplayFormat() {
        return getCourseId() + " - " + getCourseName();
    }
}