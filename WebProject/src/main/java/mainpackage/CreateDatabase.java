package mainpackage;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class CreateDatabase {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        String url = "jdbc:mysql://localhost:3307";
        String user = "root";
        String password = "343original!";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS mydb");
            System.out.println("Database 'mydb' created or already exists.");

            stmt.executeUpdate("USE mydb");

            // Create tables
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Patients (
                    amka BIGINT PRIMARY KEY,
                    username VARCHAR(100),
                    password VARCHAR(255),
                    name VARCHAR(100),
                    surname VARCHAR(100),
                    email VARCHAR(100),
                    phone INT,
                    age INT
                );
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Doctors (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE,
                    password VARCHAR(255),
                    name VARCHAR(100),
                    specialty VARCHAR(100),
                    email VARCHAR(100)
                );
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Admins (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50),
                    password VARCHAR(255)
                );
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Appointments (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    patient_amka BIGINT,
                    doctor_id INT,
                    day INT,
                    hour INT,
                    month INT,
                    year INT,
                    reason VARCHAR(255),
                    FOREIGN KEY (patient_amka) REFERENCES Patients(amka),
                    FOREIGN KEY (doctor_id) REFERENCES Doctors(id)
                );
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Availability (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    doctor_id INT,
                    day INT,
                    month INT,
                    year INT,
                    hour INT,
                    FOREIGN KEY (doctor_id) REFERENCES Doctors(id)
                );
            """);

            System.out.println("Tables created successfully!");

            insertDoctorIfNotExists(conn, "doctor1", "doctor123", "Dr. John Smith", "Cardiology", "john.smith@example.com");
            insertDoctorIfNotExists(conn, "doctor2", "doctor456", "Dr. Emily Davis", "Dermatology", "emily.davis@example.com");

            insertAdminIfNotExists(conn, "admin1", "admin123");
            insertAdminIfNotExists(conn, "superadmin", "admin456");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertAdminIfNotExists(Connection conn, String username, String rawPassword) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Admins WHERE username = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, username);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

                    String insertSql = "INSERT INTO Admins (username, password) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, hashedPassword);
                        insertStmt.executeUpdate();
                        System.out.println("Admin '" + username + "' created successfully.");
                    }
                } else {
                    System.out.println("Admin '" + username + "' already exists. Skipping insertion.");
                }
            }
        }
    }

    private static void insertDoctorIfNotExists(Connection conn, String username, String rawPassword,
                                                String name, String specialty, String email) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Doctors WHERE username = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
                    String insertSql = "INSERT INTO Doctors (username, password, name, specialty, email) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, hashedPassword);
                        insertStmt.setString(3, name);
                        insertStmt.setString(4, specialty);
                        insertStmt.setString(5, email);
                        insertStmt.executeUpdate();
                        System.out.println("Doctor '" + username + "' inserted.");
                    }
                } else {
                    System.out.println("Doctor '" + username + "' already exists. Skipping insert.");
                }
            }
        }
    }
}