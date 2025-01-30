# SURLY Database
SURLY is a Java-based database management system that processes SQL statements from a provided input file (SURLY-input.txt). It parses the SQL commands to create and manage a simple in-memory database, demonstrating fundamental database operations and command processing.

Features:
Parses SQL statements from an input file to construct and manage an in-memory database.
Supports basic SQL commands such as CREATE, INSERT, DELETE, and SELECT.
Demonstrates command processing and error handling for educational purposes.
Usage:
Clone the repository: ```bash git clone https://github.com/Brian-St/SURLY-Database.git

Compile the Java source files: ```bash javac -d bin src/*.java

Run the application with the input file: ```bash java -cp bin SurlyDatabase path/to/SURLY-input.txt

This file is ran through eclispe with no additional software installed. For proper sound, make sure sounds folder is located within the working directory of the application.
