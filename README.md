<div align="center">

# Club Matcher

![java_badge] ![sqlite_badge] ![coverage_badge] ![tests_badge] ![license_badge]

Club Recommendation & Management Software (CRAMS, or Club Matcher) is my A-Level NEA project recommendation/management software designed to assist students and club leaders at schools with efficient club management and student-club matching. A full write-up is available [here](https://github.com/user-attachments/files/17491989/Club.Suggestion.NEA.Rayan.Khan.pdf).

## Table of Contents
### [Features](#Features-1)
### [How it Works](#How-it-Works-1)
### [Installation](#Installation-1)
### [Usage](#Usage-1)
### [Running Tests](#Running-Tests-1)
### [License](#License-1)

---
</div>

## Features

- **Student-Club Matching**: Utilizes the Hungarian algorithm to assign students to the most suitable clubs based on their subjects and interests.
- **Cost Calculation Algorithm**: Uses Dijkstra’s algorithm to compute the "student cost" for each club suggestion, ensuring the closest match between student preferences and available clubs.
- **Secure Login System**: A login system with two types of user roles — students and administrators. Passwords are encrypted for security.
- **Database Management**: Includes tools for administrators to manage students, subjects, clubs, and club leaders, with options for creating, reading, updating, and deleting entries.
- **Attendance Tracking**: Allows club leaders to manage and view attendance, generate reports, and track the top attendees and most attended clubs.
- **Data Backup and Recovery**: Provides full, automatic, importable backups and allows administrators to export changes to CSV files for future use.
- **Flexible Club Management**: Features multiple sub-menus for managing students, departments, subjects, clubs, and attendance logs.

## How it Works

### Algorithms

1. **Club Suggestion Algorithm**: Matches students with clubs by minimizing the "cost" of assigning a student to a particular club based on their subjects.
2. **Cost Calculation**: Dijkstra’s algorithm is used to calculate how well a student's subject choices align with club availability, forming the basis of the club suggestion "cost".

### Data Structures

- **Doubly Linked List (DLL)**: Used to manage the dynamic nature of student and club data efficiently.
- **Binary Search Tree (BST)**:
  - Optimizes search operations for fast retrieval of data.
  - Used to handle collisions in the HashMap implementation to improve lookup performance.
- **HashMap**: Provides fast access to key-value pairs, allowing efficient retrieval of club and student data.
- **Priority Queue**: Applied in the Dijkstra’s algorithm to select the next unvisited vertex with the minimum weight assigned to it.
- **Graph**: Represents the relationship between subjects and is used to compute the cost in club assignments. Constructed using HashMaps
  - Each subject is a key in an outer Hashmap, and its corresponding value is another Hashmap that stores connected subjects and their associated weights
  - This design allows efficient access to node relationships and traversal during club suggestion calculations.

### File Organization

- **Source Code**: Java-based implementation of the data structures, algorithms, and database management.
- **Database Files**: Includes the SQLite database file and supporting scripts to set up and manage the database.
- **CSV Files**: Sample data for preloading clubs and students into the system.
- **JDBC Driver**: For SQLite integration with the Java program.

### Write-Up

- For the detailed write-up, you can look at the [Club Suggestion Write-Up](https://github.com/user-attachments/files/17491989/Club.Suggestion.NEA.Rayan.Khan.pdf)
- This covers analysis, design, implementation and testing

## Installation

1. Clone the repository:
    
    ```bash
    git clone https://github.com/Underdemon/clubMatcher.git
    ```

2. Install [Java](https://www.java.com/en/download/) if not already installed.
3. Install the SQLite JDBC driver.
4. Run the program using the following command:

    ```bash
    java -jar clubMatcher.jar
    ```

## Usage

- **Students**: Log in, view available clubs, and receive personalized recommendations for clubs based on your subjects.
- **Club Leaders**: Manage student attendance, view attendance logs, and access reports on club participation.
- **Administrators**: Access tools to manage users, backup data, and oversee the entire club matching and management system.

## Running Tests

The Club Matcher project includes automated tests to ensure that the algorithms and functionalities work correctly. Follow the steps below to run the tests:

### Prerequisites
Ensure that you have the following installed:
- **Java Development Kit (JDK)**: Version 8 or later.
- **JUnit**: The project uses JUnit for testing.

### Steps to Run Tests

1. **Clone the Repository**:
  If you haven't already cloned the repository, do so by running:

  ```bash
  git clone https://github.com/Underdemon/clubMatcher.git
  ```

2. **Compile the source code**:
  Before running the tests, make sure to compile the source code:

  ```bash
  javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar src/*.java
  ```

3. **Run the test results**:
  After compilation, you can run the tests using JUnit:
  
  ```bash
  java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore [TestClassName]
  ```
  Replace \[TestClassName\] with the actual name of the test class you want to run.

## License

This software is licensed under the following terms:

1. The software may be used for **personal, non-commercial purposes** only.
2. **Redistribution, modification, or distribution of derivative works** is strictly prohibited.
3. This software **may not be used for educational purposes**, including in academic assignments, projects, or research.

These restrictions are in place because the software is part of my A-Level coursework, and to ensure fairness and originality, it should not be used in any academic context.

For any questions, please contact me directly.


---

[license_badge]: https://img.shields.io/badge/license-Custom-brightgreen?style=for-the-badge
[java_badge]: https://img.shields.io/badge/Java-007396?logo=openjdk&logoColor=white&style=for-the-badge
[sqlite_badge]: https://img.shields.io/badge/-SQLite-003B57?logo=sqlite&logoColor=white&style=for-the-badge
[coverage_badge]: https://img.shields.io/badge/coverage-100%25-brightgreen?style=for-the-badge
[tests_badge]: https://img.shields.io/badge/tests-passing-brightgreen?style=for-the-badge
