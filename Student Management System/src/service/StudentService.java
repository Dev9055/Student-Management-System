package service;

import dao.StudentDao;
import model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service layer for student operations.
 * This class contains the business logic for the application.
 */
public class StudentService {

    private StudentDao studentDao;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", 
        Pattern.CASE_INSENSITIVE
    );

    public StudentService() {
        this.studentDao = new StudentDao();
    }

    /**
     * Registers a new student after performing validation.
     * @param student The student object to register.
     * @return null if registration was successful, or an error message string.
     */
    public String registerStudent(Student student) {
        if (student.getId() <= 0) {
            return "Student ID must be a positive number.";
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return "Student name cannot be empty.";
        }
        if (student.getEmail() == null || !EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            return "Invalid email format. Please use name@example.com.";
        }
        
        try {
            studentDao.addStudent(student);
            System.out.println("Service: Student registered successfully!");
            return null; 
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                 return "This ID or Email already exists in the database.";
            }
            return "Database error: " + e.getMessage();
        }
    }

    /**
     * Retrieves a list of all students.
     * @return A List of Student objects.
     */
    public List<Student> listAllStudents() {
        return studentDao.getAllStudents();
    }

    /**
     * Finds a single student by their ID.
     * @param id The ID of the student to find.
     * @return The Student object if found, or null.
     */
    public Student findStudentById(int id) {
        return studentDao.getStudentById(id);
    }

    /**
     * Updates an existing student's information.
     * @param student The student object with updated details.
     * @return null if update was successful, or an error message string.
     */
    public String updateStudentInfo(Student student) {
        if (student.getId() <= 0) {
            return "Invalid student ID for update.";
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return "Student name cannot be empty.";
        }
        if (student.getEmail() == null || !EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            return "Invalid email format.";
        }
        if (studentDao.getStudentById(student.getId()) == null) {
            return "Cannot update: No student found with ID " + student.getId();
        }

        try {
            studentDao.updateStudent(student);
            System.out.println("Service: Student updated successfully!");
            return null; 
        } catch (RuntimeException e) {
            if(e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                 return "This email is already in use by another student.";
            }
            return "Database error: " + e.getMessage();
        }
    }

    /**
     * Deletes a student by their ID.
     * @param id The ID of the student to delete.
     * @return null if deletion was successful, or an error message string.
     */
    public String deleteStudentById(int id) {
        if (id <= 0) {
            return "Invalid student ID.";
        }
        if (studentDao.getStudentById(id) == null) {
            return "Cannot delete: No student found with ID " + id;
        }

        try {
            studentDao.deleteStudent(id);
            System.out.println("Service: Student deleted successfully!");
            return null; 
        } catch (RuntimeException e) {
            return "Database error: " + e.getMessage();
        }
    }

    // =========================================================================
    //  NEW METHOD FOR SEARCH
    // =========================================================================
    /**
     * Searches for students by name.
     * @param name The search term.
     * @return A List of matching Student objects.
     */
    public List<Student> searchStudentsByName(String name) {
        // If the search term is empty, just return all students.
        if (name == null || name.trim().isEmpty()) {
            return listAllStudents();
        }
        return studentDao.searchStudentsByName(name);
    }
}