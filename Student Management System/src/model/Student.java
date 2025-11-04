package model;

/**
 * This class represents the Student model (also known as a POJO or Entity).
 * It is used to hold data about a single student.
 */
public class Student {

    // --- Fields ---
    
    private int id;
    private String name;
    private String email;
    // You could add more fields here, like:
    // private String phone;
    // private String major;

    
    // --- Constructors ---

    /**
     * Default constructor.
     * Useful for creating an empty Student object that you can fill using setters.
     */
    public Student() {
    }

    /**
     * Overloaded constructor to create a new student, typically used
     * BEFORE saving to the database (since the ID is not yet known).
     * @param name The student's full name.
     * @param email The student's email address.
     */
    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Overloaded constructor to create a student object, typically used
     * AFTER retrieving from the database (since the ID is known).
     * @param id The student's unique ID.
     * @param name The student's full name.
     * @param email The student's email address.
     */
    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    
    // --- Getters and Setters ---
    // These methods are used to access and modify the private fields (encapsulation).

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    // --- toString() Method ---

    /**
     * Provides a human-readable string representation of the Student object.
     * This is very useful for debugging and for displaying the student in the console.
     * @return A string describing the student.
     */
    @Override
    public String toString() {
        return "Student [ID=" + id + ", Name=" + name + ", Email=" + email + "]";
    }
}