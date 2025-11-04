# 🎓 Java Student Management System

Hey there! This is a desktop Student Management System I built from scratch using Java Swing. It's a pretty comprehensive app that connects to a MySQL database to handle all the core functions of a school or college registrar.

I started this project to get a deep, hands-on understanding of a few key Java concepts. I wanted to move beyond simple console apps and build something real. This project was my way to learn:

* How to build a functional, multi-window **GUI with Java Swing**.
* How to properly structure a Java application using a **3-tier (DAO/Service/UI) architecture**.
* How to connect to a **MySQL database with JDBC** and perform full CRUD operations.
* How to make different parts of an application (like students and courses) **interact with each other**.

---

## 📸 Screenshot

*(I highly recommend you take a screenshot of your app running and put it here! It makes a huge difference. Just replace the line below.)*

``

---

## ✨ What's Inside? (Features)

This isn't just a simple address book. It's a connected system with several key features:

* **👨‍🎓 Student Management:**
    * Full **CRUD** (Create, Read, Update, Delete) for all students.
    * Student IDs are set manually (e.g., `500124743`).
    * Clean `JTable` view of all students.

* **📚 Course Management:**
    * Full **CRUD** for all courses (e.g., "CS101", "Intro to Java").
    * `JTable` view of all available courses.

* **🔗 Interactive Enrollment:**
    * This is the best part! When you click a student, an **Enrollment Panel** pops up on the right.
    * It shows you every course that student is **currently enrolled in**.
    * You can easily **enroll** the student in any new course from a dropdown menu.
    * You can also **drop** them from a course they're already in.

* **🔍 Live Search & Export:**
    * A **search bar** to instantly filter the student list by name.
    * An **"Export to CSV"** button that saves the entire student list to a `.csv` file you can open in Excel.

---

## 🛠 What I Used (Tech Stack)

* **Language:** Java (JDK)
* **GUI:** Java Swing
* **Database:** MySQL
* **Connector:** JDBC (MySQL Connector/J)
* **Architecture:** 3-Tier (Model-DAO-Service-UI)

---

## 🚀 How to Get it Running

Want to try it out yourself? It's pretty straightforward.

1.  **Set up the Database:**
    * First, you'll need a MySQL server running.
    * Open MySQL Workbench (or your favorite SQL tool) and run the `schema.sql` file. This will create the `studentdb` database and all three tables (`students`, `courses`, and `enrollment`).

2.  **Update the Password:**
    * Open the project and find this file: `src/dao/DatabaseConnection.java`.
    * Go to line 19 and change the `PASSWORD` variable from `"your_password"` to your **actual MySQL password**.

3.  **Add the Driver:**
    * You'll need the MySQL JDBC driver. Download the `.jar` file (called "MySQL Connector/J") from the official MySQL website.
    * Drop that `.jar` file into the `lib/` folder in this project.
    * **Important:** Make sure your IDE (like VS Code, Eclipse, or IntelliJ) knows to use this .jar. You might have to right-click it and "Add to Build Path" or "Add to Classpath".

4.  **Run it!**
    * That's it! Compile the project and run the `src/Main.java` file. The app should launch.

---

## 💡 Future Ideas

This project was a ton of fun, but there's always more to do. Here are a few things I'm thinking about adding next:

* **Grade Management:** The next logical step. Add a `grade` column to the `enrollment` table.
* **A Full Login System:** Create a separate login window to secure the app.
* **Dashboard Tab:** A new tab with stats like "Total Students," "Most Popular Course," etc.

Feel free to clone, fork, or suggest features!
