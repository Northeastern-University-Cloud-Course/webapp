# README

# webapp

## Introduction

This document outlines the prerequisites required for building and deploying a Spring Boot application,version 3.2.2, using Maven and Java JDK 21. Additionally, this application requires MySQL 8 for database operations.

## Prerequisites

Before we begin the local setup of the Spring Boot application, we have to ensure we have the following prerequisites installed and configured on to your system:

### 1. Java Development Kit (JDK) 21

- **Description**: Java JDK is a development environment for building applications, applets, and components using the Java programming language.
- **Installation**:
    - **MacOS**: Download and install the JDK from the [Oracle website](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).
    - **Verify Installation**: Open a terminal or command prompt and run `java -version` to ensure it's correctly installed. We see a version string that includes "21".

### 2. Maven

- **Description**: Maven is a build automation tool used primarily for Java projects.
- **Installation**:
    - **macOS**: Install Maven via a package manager. On macOS with Homebrew, run `brew install maven`.
    - **Verify Installation**: In a terminal or command prompt, run `mvn -v` to verify the installation. We see the Maven version along with Java version.

### 3. MySQL 8

- **Description**: MySQL is an open-source relational database management system.
- **Installation**:
    - **All Platforms**: Download MySQL 8 from the [official MySQL downloads page](https://dev.mysql.com/downloads/mysql/). Follow the installation guide.
    - During installation, set up a root password and note it down, it will be required for configuring our Spring Boot application's `application.properties` file.
    - **Verify Installation**: Access the MySQL command-line tool by running `mysql -u root -p` in terminal. Enter the root password when prompted. If installed correctly, we can enter the MySQL command-line interface.

### 4. Spring Boot 3.2.2

- While not a direct installation requirement, the project will be configured to use Spring Boot version 3.2.2 through its Maven dependencies. Ensure the `pom.xml` file specifies this version appropriately.

## Building and Running the Application

After ensuring all the prerequisites are installed and configured, we can proceed to build and run our Spring Boot application. Here is a basic overview:

1. **Clone the repository** or download the source code to your local machine.
2. **Navigate to the project directory** in a terminal or command prompt.
3. **Configure the `application.properties`** file in `src/main/resources` with your MySQL connection details, including the database URL, username, and password.
4. **Build the project** using Maven by running `mvn clean install`. This will compile your application and run any tests.
5. **Run the application** using `java -jar target/yourapp.jar`, replacing `yourapp.jar` with your actual jar file name, or use Maven with `mvn spring-boot:run`.
