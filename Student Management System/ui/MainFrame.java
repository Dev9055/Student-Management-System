package ui;

import model.Course;
import model.Student;
import service.CourseService;
import service.EnrollmentService;
import service.StudentService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter; // Import for FileChooser
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter; // Import for writing files
import java.io.File;           // Import for File
import java.io.FileWriter;     // Import for writing files
import java.io.IOException;    // Import for error handling
import java.util.List;
import java.util.Vector;

/**
 * Main GUI window for the Student Management System.
 * Includes Student, Course, and Enrollment management.
 * Includes Export to CSV functionality.
 */
public class MainFrame extends JFrame {

    // --- Services ---
    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    // --- Main GUI Component ---
    private JTabbedPane tabbedPane;

    // --- Student Tab Components ---
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JTextField studentIdField, studentNameField, studentEmailField, studentSearchField;
    private JButton addStudentButton, updateStudentButton, deleteStudentButton, findStudentButton, refreshStudentButton, studentSearchButton;
    private JButton exportStudentButton; // <-- NEW EXPORT BUTTON
    
    // --- Course Tab Components ---
    private JTable courseTable;
    private DefaultTableModel courseTableModel;
    private JTextField courseIdField, courseNameField, courseInstructorField;
    private JButton addCourseButton, updateCourseButton, deleteCourseButton, refreshCourseButton;
    
    // --- Enrollment Components (on Student Tab) ---
    private JList<Course> enrolledList;
    private DefaultListModel<Course> enrolledListModel;
    private JComboBox<Course> enrollComboBox;
    private JButton enrollButton, dropButton;
    private JPanel enrollmentPanel; 

    public MainFrame() {
        // 1. Initialize all services
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();

        // 2. Set up the main window properties
        setTitle("🎓 Student Management System");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 3. Create the JTabbedPane
        tabbedPane = new JTabbedPane();

        // 4. Create the two main tabs
        JPanel studentPanel = createStudentPanel();
        JPanel coursePanel = createCoursePanel();

        // 5. Add tabs to the pane
        tabbedPane.addTab("Student Management", studentPanel);
        tabbedPane.addTab("Course Management", coursePanel);

        // 6. Add the pane to the main window
        add(tabbedPane);

        // 7. Load initial data
        refreshStudentList();
        refreshCourseList();
    }

    // =========================================================================
    //                            STUDENT TAB
    // =========================================================================

    private JPanel createStudentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top: Student Form ---
        JPanel formPanel = createStudentFormPanel();
        mainPanel.add(formPanel, BorderLayout.NORTH);

        // --- Center: Student Table ---
        JPanel tablePanel = createStudentTablePanel(); // This method now has the export button
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // --- Right: Enrollment Panel ---
        enrollmentPanel = createEnrollmentPanel();
        enrollmentPanel.setVisible(false); // Hide until a student is clicked
        mainPanel.add(enrollmentPanel, BorderLayout.EAST);

        // --- Student Table Click Listener ---
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get data from the table model
                    int id = (int) studentTableModel.getValueAt(selectedRow, 0);
                    String name = (String) studentTableModel.getValueAt(selectedRow, 1);
                    String email = (String) studentTableModel.getValueAt(selectedRow, 2);
                    
                    // Set the text fields
                    studentIdField.setText(String.valueOf(id));
                    studentNameField.setText(name);
                    studentEmailField.setText(email);
                    
                    // Show and update the enrollment panel
                    refreshEnrolledList(id);
                    enrollmentPanel.setVisible(true);
                } else {
                    // If no row is selected, hide enrollment panel
                    enrollmentPanel.setVisible(false);
                }
            }
        });
        
        return mainPanel;
    }

    private JPanel createStudentFormPanel() {
        JPanel mainFormPanel = new JPanel(new BorderLayout(10, 10));

        // --- Input Fields Panel ---
        JPanel inputFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentIdField = new JTextField(10);
        studentNameField = new JTextField(20);
        studentEmailField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        inputFieldsPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        inputFieldsPanel.add(studentIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.EAST;
        inputFieldsPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        inputFieldsPanel.add(studentNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.EAST;
        inputFieldsPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        inputFieldsPanel.add(studentEmailField, gbc);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        addStudentButton = new JButton("Add Student");
        updateStudentButton = new JButton("Update Student");
        deleteStudentButton = new JButton("Delete Student");
        findStudentButton = new JButton("Find by ID");
        refreshStudentButton = new JButton("Refresh (Show All)");
        
        buttonPanel.add(addStudentButton);
        buttonPanel.add(updateStudentButton);
        buttonPanel.add(deleteStudentButton);
        buttonPanel.add(findStudentButton);
        buttonPanel.add(refreshStudentButton);
        buttonPanel.add(new JLabel("")); // Blank cell

        mainFormPanel.add(inputFieldsPanel, BorderLayout.CENTER);
        mainFormPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Add Action Listeners ---
        addStudentButton.addActionListener(e -> addStudent());
        updateStudentButton.addActionListener(e -> updateStudent());
        deleteStudentButton.addActionListener(e -> deleteStudent());
        findStudentButton.addActionListener(e -> findStudentById());
        refreshStudentButton.addActionListener(e -> refreshStudentList());
        
        return mainFormPanel;
    }

    // =========================================================================
    //  UPDATED METHOD (Added Export Button)
    // =========================================================================
    private JPanel createStudentTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        
        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentSearchField = new JTextField(25);
        studentSearchButton = new JButton("Search by Name");
        studentSearchButton.addActionListener(e -> searchStudents());
        
        // --- NEW EXPORT BUTTON ---
        exportStudentButton = new JButton("Export to CSV");
        exportStudentButton.addActionListener(e -> exportStudentsToCsv());
        // -------------------------
        
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(studentSearchField);
        searchPanel.add(studentSearchButton);
        searchPanel.add(exportStudentButton); // <-- ADDED BUTTON TO PANEL

        // --- JTable Setup ---
        String[] columnNames = {"ID", "Name", "Email"};
        studentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(studentTableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        studentTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    // =========================================================================
    //                            ENROLLMENT PANEL
    // =========================================================================
    
    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Enrollment"));
        panel.setPreferredSize(new Dimension(300, 0)); // Set preferred width

        // --- Top: List of enrolled courses ---
        enrolledListModel = new DefaultListModel<>();
        enrolledList = new JList<>(enrolledListModel);
        JScrollPane listScrollPane = new JScrollPane(enrolledList);
        panel.add(listScrollPane, BorderLayout.CENTER);
        
        // --- Bottom: Controls to add/drop ---
        JPanel controlPanel = new JPanel(new BorderLayout(5, 5));
        
        // ComboBox to select a course to enroll in
        enrollComboBox = new JComboBox<>();
        controlPanel.add(enrollComboBox, BorderLayout.NORTH);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        enrollButton = new JButton("Enroll");
        dropButton = new JButton("Drop");
        buttonPanel.add(enrollButton);
        buttonPanel.add(dropButton);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(controlPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        enrollButton.addActionListener(e -> enrollStudent());
        dropButton.addActionListener(e -> dropCourse());

        return panel;
    }

    // =========================================================================
    //                            COURSE TAB
    // =========================================================================
    
    private JPanel createCoursePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top: Course Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        courseIdField = new JTextField(10);
        courseNameField = new JTextField(20);
        courseInstructorField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Course ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(courseIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(courseNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Instructor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(courseInstructorField, gbc);
        
        // --- Course Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addCourseButton = new JButton("Add Course");
        updateCourseButton = new JButton("Update Course");
        deleteCourseButton = new JButton("Delete Course");
        refreshCourseButton = new JButton("Refresh Courses");
        
        buttonPanel.add(addCourseButton);
        buttonPanel.add(updateCourseButton);
        buttonPanel.add(deleteCourseButton);
        buttonPanel.add(refreshCourseButton);
        
        // Add course form and buttons to the top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- Center: Course Table ---
        String[] columnNames = {"Course ID", "Course Name", "Instructor"};
        courseTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        courseTable = new JTable(courseTableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // --- Course Table Click Listener ---
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = courseTable.getSelectedRow();
                if (selectedRow != -1) {
                    courseIdField.setText((String) courseTableModel.getValueAt(selectedRow, 0));
                    courseNameField.setText((String) courseTableModel.getValueAt(selectedRow, 1));
                    courseInstructorField.setText((String) courseTableModel.getValueAt(selectedRow, 2));
                }
            }
        });

        // --- Course Button Action Listeners ---
        addCourseButton.addActionListener(e -> addCourse());
        updateCourseButton.addActionListener(e -> updateCourse());
        deleteCourseButton.addActionListener(e -> deleteCourse());
        refreshCourseButton.addActionListener(e -> refreshCourseList());

        return mainPanel;
    }
    
    // =========================================================================
    //                            LOGIC METHODS (Students)
    // =========================================================================

    private void displayStudentResults(List<Student> students) {
        studentTableModel.setRowCount(0); // Clear table
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getEmail()
            });
        }
    }

    private void refreshStudentList() {
        displayStudentResults(studentService.listAllStudents());
        studentSearchField.setText("");
        clearStudentFields();
    }
    
    private void searchStudents() {
        displayStudentResults(studentService.searchStudentsByName(studentSearchField.getText()));
    }

    private void addStudent() {
        try {
            int id = Integer.parseInt(studentIdField.getText());
            Student student = new Student(id, studentNameField.getText(), studentEmailField.getText());
            String error = studentService.registerStudent(student);
            if (error == null) {
                JOptionPane.showMessageDialog(this, "Student added!");
                refreshStudentList();
            } else {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStudent() {
        try {
            int id = Integer.parseInt(studentIdField.getText());
            Student student = new Student(id, studentNameField.getText(), studentEmailField.getText());
            String error = studentService.updateStudentInfo(student);
            if (error == null) {
                JOptionPane.showMessageDialog(this, "Student updated!");
                refreshStudentList();
            } else {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please select a student or enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        try {
            int id = Integer.parseInt(studentIdField.getText());
            int choice = JOptionPane.showConfirmDialog(this, "Delete student ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                String error = studentService.deleteStudentById(id);
                if (error == null) {
                    JOptionPane.showMessageDialog(this, "Student deleted!");
                    refreshStudentList();
                } else {
                    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please select a student or enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findStudentById() {
        try {
            int id = Integer.parseInt(studentIdField.getText());
            Student student = studentService.findStudentById(id);
            if (student != null) {
                studentNameField.setText(student.getName());
                studentEmailField.setText(student.getEmail());
                for (int i = 0; i < studentTableModel.getRowCount(); i++) {
                    if (studentTableModel.getValueAt(i, 0).equals(id)) {
                        studentTable.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No student found with ID: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearStudentFields() {
        studentIdField.setText("");
        studentNameField.setText("");
        studentEmailField.setText("");
        studentTable.clearSelection();
        enrollmentPanel.setVisible(false);
    }
    
    // =========================================================================
    //                            LOGIC METHODS (Courses)
    // =========================================================================

    private void refreshCourseList() {
        courseTableModel.setRowCount(0); // Clear table
        
        Vector<Course> courseVector = new Vector<>();
        
        List<Course> courses = courseService.getAllCourses();
        for (Course course : courses) {
            courseTableModel.addRow(new Object[]{
                course.getCourseId(),
                course.getCourseName(),
                course.getInstructor()
            });
            courseVector.add(course);
        }
        enrollComboBox.setModel(new DefaultComboBoxModel<>(courseVector));
        enrollComboBox.setRenderer(new CourseListRenderer());
        
        clearCourseFields();
    }
    
    private void addCourse() {
        Course course = new Course(
            courseIdField.getText(),
            courseNameField.getText(),
            courseInstructorField.getText()
        );
        String error = courseService.addCourse(course);
        if (error == null) {
            JOptionPane.showMessageDialog(this, "Course added!");
            refreshCourseList();
        } else {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCourse() {
        Course course = new Course(
            courseIdField.getText(),
            courseNameField.getText(),
            courseInstructorField.getText()
        );
        String error = courseService.updateCourse(course);
        if (error == null) {
            JOptionPane.showMessageDialog(this, "Course updated!");
            refreshCourseList();
        } else {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCourse() {
        String id = courseIdField.getText();
        int choice = JOptionPane.showConfirmDialog(this, "Delete course " + id + "?\n(This will also un-enroll all students)", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String error = courseService.deleteCourse(id);
            if (error == null) {
                JOptionPane.showMessageDialog(this, "Course deleted!");
                refreshCourseList();
            } else {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearCourseFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        courseInstructorField.setText("");
        courseTable.clearSelection();
    }
    
    // =========================================================================
    //                            LOGIC METHODS (Enrollment)
    // =========================================================================
    
    private void refreshEnrolledList(int studentId) {
        enrolledListModel.clear();
        List<Course> courses = enrollmentService.getCoursesForStudent(studentId);
        for (Course course : courses) {
            enrolledListModel.addElement(course);
        }
        enrolledList.setCellRenderer(new CourseListRenderer());
    }
    
    private void enrollStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int studentId = (int) studentTableModel.getValueAt(selectedRow, 0);
        
        Course selectedCourse = (Course) enrollComboBox.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to enroll in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String error = enrollmentService.enrollStudent(studentId, selectedCourse.getCourseId());
        if (error == null) {
            JOptionPane.showMessageDialog(this, "Enrolled successfully!");
            refreshEnrolledList(studentId); // Refresh just the list
        } else {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void dropCourse() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int studentId = (int) studentTableModel.getValueAt(selectedRow, 0);
        
        Course selectedCourse = enrolledList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course from the 'Enrolled' list to drop.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String error = enrollmentService.dropStudent(studentId, selectedCourse.getCourseId());
        if (error == null) {
            JOptionPane.showMessageDialog(this, "Dropped successfully!");
            refreshEnrolledList(studentId); // Refresh just the list
        } else {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    //                            NEW EXPORT LOGIC
    // =========================================================================

    /**
     * Opens a file chooser and exports all students to a CSV file.
     */
    private void exportStudentsToCsv() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Student List as CSV");
        // Set a default filename
        fileChooser.setSelectedFile(new File("students.csv"));
        // Filter for .csv files
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Ensure the file has a .csv extension
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            // Get all students (not just the filtered ones, for simplicity)
            List<Student> allStudents = studentService.listAllStudents();

            // Use try-with-resources to auto-close the writer
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                // Write the header row
                writer.write("ID,Name,Email\n");
                
                // Write each student row
                for (Student student : allStudents) {
                    String line = String.format("%d,%s,%s\n",
                        student.getId(),
                        escapeCsv(student.getName()),
                        escapeCsv(student.getEmail())
                    );
                    writer.write(line);
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Successfully exported " + allStudents.size() + " students to\n" + fileToSave.getName(),
                    "Export Successful", 
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error writing file: " + ex.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * A helper method to make data "CSV-safe."
     * If a string contains a comma, it wraps it in double quotes.
     * It also doubles any existing double quotes.
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        // Replace all " with ""
        String escaped = value.replace("\"", "\"\"");
        // If it contains a comma, space, or the quote, wrap the whole thing in quotes
        if (escaped.contains(",") || escaped.contains(" ") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }


    // =========================================================================
    //                            CUSTOM RENDERER
    // =========================================================================
    
    /**
     * Custom renderer to display Course objects nicely in JList and JComboBox.
     */
    private class CourseListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Call the super method to get the default styling
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Course) {
                // Use the custom display format
                setText(((Course) value).getDisplayFormat());
            }
            return this;
        }
    }
}