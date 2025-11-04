package controller;

import model.Student;
import service.StudentService;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Controller layer for the Student Management System.
 * This class handles all user interaction (the console menu)
 * and directs calls to the StudentService.
 */
public class StudentController {

    private StudentService studentService = new StudentService();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Starts the main application loop.
     */
    public void start() {
        while (true) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        addNewStudent();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        viewStudentById();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        deleteStudent();
                        break;
                    case 6:
                        System.out.println("Exiting application. Goodbye! 👋");
                        return; // Exit the start() method, ending the loop
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }

    /**
     * Displays the main menu options to the console.
     */
    private void displayMenu() {
        System.out.println("\n--- 🎓 Student Management System ---");
        System.out.println("1. Add a new Student");
        System.out.println("2. View all Students");
        System.out.println("3. Find Student by ID");
        System.out.println("4. Update Student Information");
        System.out.println("5. Delete Student");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    /**
     * Handles the logic for adding a new student.
     */
    private void addNewStudent() {
        System.out.println("\n--- Add New Student ---");
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student email: ");
        String email = scanner.nextLine();

        Student student = new Student(name, email);
        studentService.registerStudent(student);
    }

    /**
     * Handles the logic for viewing all students.
     */
    private void viewAllStudents() {
        System.out.println("\n--- List of All Students ---");
        List<Student> students = studentService.listAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found in the system.");
        } else {
            for (Student student : students) {
                System.out.println(student); // Relies on the toString() method in Student.java
            }
        }
    }

    /**
     * Handles the logic for finding a single student by their ID.
     */
    private void viewStudentById() {
        System.out.println("\n--- Find Student by ID ---");
        System.out.print("Enter student ID to find: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Student student = studentService.findStudentById(id);
        if (student != null) {
            System.out.println("Student found: " + student);
        } else {
            System.out.println("No student found with ID: " + id);
        }
    }

    /**
     * Handles the logic for updating an existing student.
     */
    private void updateStudent() {
        System.out.println("\n--- Update Student Information ---");
        System.out.print("Enter student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // First, check if the student exists
        Student student = studentService.findStudentById(id);
        if (student == null) {
            System.out.println("No student found with ID: " + id + ". Cannot update.");
            return;
        }

        System.out.println("Found student: " + student);
        System.out.print("Enter new name (or press Enter to keep '" + student.getName() + "'): ");
        String name = scanner.nextLine();
        System.out.print("Enter new email (or press Enter to keep '" + student.getEmail() + "'): ");
        String email = scanner.nextLine();

        // Update the student object
        if (name != null && !name.trim().isEmpty()) {
            student.setName(name);
        }
        if (email != null && !email.trim().isEmpty()) {
            student.setEmail(email);
        }

        // Send the updated object to the service
        studentService.updateStudentInfo(student);
    }

    /**
     * Handles the logic for deleting a student.
     */
    private void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        System.out.print("Enter student ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // First, confirm the student exists
        Student student = studentService.findStudentById(id);
        if (student == null) {
            System.out.println("No student found with ID: " + id + ". Cannot delete.");
            return;
        }

        System.out.println("Found student: " + student);
        System.out.print("Are you sure you want to delete this student? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            studentService.deleteStudentById(id);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}