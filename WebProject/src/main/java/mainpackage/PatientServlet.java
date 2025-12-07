package mainpackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Κύριο Servlet για τη διαχείριση των λειτουργιών των ασθενών
@WebServlet(name = "PatientServlet", urlPatterns = {"/patient/*"})
public class PatientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if (action == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Διαχείριση GET αιτημάτων
        switch (action) {
            case "/login" -> handleLogin(request, response);  // Σελίδα σύνδεσης
            case "/profile" -> showPatientProfile(request, response);  // Προφίλ ασθενούς
            case "/appointments" -> showAppointments(request, response);  // Λίστα ραντεβού
            case "/register" -> handleRegister(request, response);  // Σελίδα εγγραφής
            case "/available-appointments" -> showAvailableAppointments(request, response);  // Διαθέσιμα ραντεβού
            case "/logout" -> logout(request, response);  // Αποσύνδεση
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);  // Σφάλμα 404
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if (action == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Διαχείριση POST αιτημάτων
        switch (action) {
            case "/login" -> handleLogin(request, response);  // Επεξεργασία σύνδεσης
            case "/register" -> handleRegister(request, response);  // Επεξεργασία εγγραφής
            case "/book-appointment" -> bookAppointment(request, response);  // Κράτηση ραντεβού
            case "/cancelAppointment" -> cancelAppointment(request, response);  // Ακύρωση ραντεβού
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);  // Σφάλμα 404
        }
    }

    // Σύνδεση ασθενούς
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Έλεγχος διαπιστευτηρίων
        boolean isValid = checkCredentials(username, password);

        if (isValid) {
            // Δημιουργία session και αποθήκευση ονόματος χρήστη
            HttpSession session = request.getSession();
            session.setAttribute("patientUsername", username);
            response.sendRedirect(request.getContextPath() + "/patient/profile");
        } else {
            // Αποτυχία σύνδεσης - ανακατεύθυνση με σφάλμα
            response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp?error=1");
        }
    }

    // Εγγραφή νέου ασθενούς
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Ανάγνωση παραμέτρων από τη φόρμα
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String amka = request.getParameter("amka");
        String age = request.getParameter("age");

        try (Connection conn = ConnectDatabase.getConnection()) {

            // Έλεγχος αν υπάρχει ήδη ο ασθενής με το ίδιο AMKA
            String checkQuery = "SELECT * FROM Patients WHERE amka = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, amka);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("errorMessage", "Ο χρήστης υπάρχει ήδη.");
                        request.getRequestDispatcher("/patientusers/register.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // Έλεγχος υποχρεωτικών πεδίων
            if (phone == null || age == null || phone.isBlank() || age.isBlank()) {
                request.setAttribute("errorMessage", "Συμπληρώστε όλα τα πεδία.");
                request.getRequestDispatcher("/patientusers/register.jsp").forward(request, response);
                return;
            }

            // Κρυπτογράφηση του κωδικού
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Εισαγωγή νέου ασθενούς στη βάση
            String insertQuery = "INSERT INTO Patients (amka, username, password, name, surname, email, phone, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, amka);
                insertStmt.setString(2, username);
                insertStmt.setString(3, hashedPassword);
                insertStmt.setString(4, name);
                insertStmt.setString(5, surname);
                insertStmt.setString(6, email);
                insertStmt.setString(7, phone);
                insertStmt.setInt(8, Integer.parseInt(age));

                insertStmt.executeUpdate();
            }

            // Αυτόματη σύνδεση μετά την εγγραφή
            HttpSession session = request.getSession();
            session.setAttribute("patientUsername", username);
            response.sendRedirect(request.getContextPath() + "/patient/profile");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Σφάλμα κατά την εγγραφή.");
        }
    }

    // Αποσύνδεση ασθενούς
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // Καταστροφή session
        }
        response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");  // Ανακατεύθυνση στη σύνδεση
    }

    // Εμφάνιση προφίλ ασθενούς
    private void showPatientProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("patientUsername");

        // Έλεγχος αν ο ασθενής είναι συνδεδεμένος
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/mydb", "root", "343original!");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Patients WHERE username = ?")) {

            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Ανάκτηση στοιχείων ασθενούς από τη βάση
                    Patient patient = new Patient(
                            username,
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("password"),
                            rs.getInt("phone"),
                            rs.getLong("amka")
                    );
                    // Αποθήκευση στοιχείων στο session και request
                    session.setAttribute("user", patient);
                    request.setAttribute("patient", patient);
                    request.getRequestDispatcher("/patientusers/profile.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
        }
    }

    // Εμφάνιση των ραντεβού του ασθενούς
    private void showAppointments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("patientUsername");

        // Έλεγχος αν ο ασθενής είναι συνδεδεμένος
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
            return;
        }

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/mydb", "root", "343original!");
             PreparedStatement statement = connection.prepareStatement("""
                 SELECT a.id, a.day, a.month, a.year, a.hour, a.reason,
                        d.name AS doctor_name, d.specialty
                 FROM Appointments a
                 JOIN Doctors d ON a.doctor_id = d.id
                 JOIN Patients p ON a.patient_amka = p.amka
                 WHERE p.username = ?
             """)) {

            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    // Δημιουργία αντικειμένου ραντεβού
                    Appointment appointment = new Appointment(
                            rs.getInt("id"),
                            rs.getInt("day"),
                            rs.getInt("month"),
                            rs.getInt("year"),
                            rs.getString("hour"),
                            rs.getString("reason")
                    );

                    // Προσθήκη στοιχείων γιατρού
                    appointment.setDoctorName(rs.getString("doctor_name"));
                    appointment.setSpecialty(rs.getString("specialty"));

                    appointments.add(appointment);
                }
            }

            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("/patientusers/appointments.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
        }
    }

    // Εμφάνιση διαθέσιμων ραντεβού για συγκεκριμένη ειδικότητα
    private void showAvailableAppointments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String specialty = request.getParameter("specialty");

        List<Availability> availableAppointments = new ArrayList<>();

        // Ανάκτηση διαθέσιμων ραντεβού από τη βάση
        String query = """
        SELECT a.id, a.doctor_id, d.name AS doctor_name, d.specialty, a.day, a.month, a.year, a.hour
        FROM Availability a
        JOIN Doctors d ON a.doctor_id = d.id
        WHERE d.specialty = ?
        ORDER BY a.year, a.month, a.day, a.hour
    """;

        try (Connection conn = ConnectDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, specialty);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Availability av = new Availability(
                            rs.getInt("id"),
                            rs.getInt("doctor_id"),
                            rs.getInt("day"),
                            rs.getInt("month"),
                            rs.getInt("year"),
                            rs.getInt("hour"),
                            rs.getString("doctor_name"),
                            rs.getString("specialty")
                    );
                    availableAppointments.add(av);
                }
            }

            request.setAttribute("availabilityList", availableAppointments);
            request.getRequestDispatcher("/patientusers/availableAppointments.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/patient/profile");
        }
    }

    // Κράτηση ραντεβού από ασθενή
    private void bookAppointment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("patientUsername");

        // Έλεγχος αν ο ασθενής είναι συνδεδεμένος
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
            return;
        }

        int availabilityId = Integer.parseInt(request.getParameter("availabilityId"));

        try (Connection conn = ConnectDatabase.getConnection()) {

            // Ανάκτηση AMKA ασθενούς
            long amka = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT amka FROM Patients WHERE username = ?")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        amka = rs.getLong("amka");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
                        return;
                    }
                }
            }

            // Ανάκτηση στοιχείων διαθεσιμότητας
            int doctorId = 0, day = 0, month = 0, year = 0, hour = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Availability WHERE id = ?")) {
                ps.setInt(1, availabilityId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        doctorId = rs.getInt("doctor_id");
                        day = rs.getInt("day");
                        month = rs.getInt("month");
                        year = rs.getInt("year");
                        hour = rs.getInt("hour");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/patient/profile");
                        return;
                    }
                }
            }

            // Δημιουργία νέου ραντεβού
            try (PreparedStatement ps = conn.prepareStatement("""
            INSERT INTO Appointments (patient_amka, doctor_id, day, hour, month, year, reason)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """)) {
                ps.setLong(1, amka);
                ps.setInt(2, doctorId);
                ps.setInt(3, day);
                ps.setInt(4, hour);
                ps.setInt(5, month);
                ps.setInt(6, year);
                ps.setString(7, "");  // Κενός λόγος κράτησης
                ps.executeUpdate();
            }

            // Διαγραφή της διαθεσιμότητας μετά την κράτηση
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Availability WHERE id = ?")) {
                ps.setInt(1, availabilityId);
                ps.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/patient/appointments");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/patient/profile");
        }
    }

    // Ακύρωση ραντεβού από ασθενή
    private void cancelAppointment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String patientUsername = (String) session.getAttribute("patientUsername");
        
        // Έλεγχος αν ο ασθενής είναι συνδεδεμένος
        if (patientUsername == null) {
            response.sendRedirect(request.getContextPath() + "/patientusers/login.jsp");
            return;
        }

        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        try (Connection conn = ConnectDatabase.getConnection()) {
            // Εύρεση ημερομηνίας ραντεβού και επιβεβαίωση ότι ανήκει στον ασθενή
            String selectQuery = """
            SELECT a.day, a.month, a.year
            FROM Appointments a
            JOIN Patients p ON a.patient_amka = p.amka
            WHERE a.id = ? AND p.username = ?
        """;

            LocalDate appointmentDate;

            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, appointmentId);
                stmt.setString(2, patientUsername);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        appointmentDate = LocalDate.of(rs.getInt("year"), rs.getInt("month"), rs.getInt("day"));
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ραντεβού δεν βρέθηκε");
                        return;
                    }
                }
            }

            // Έλεγχος αν το ραντεβού είναι τουλάχιστον 3 μέρες μετά
            if (appointmentDate.isBefore(LocalDate.now().plusDays(3))) {
                request.setAttribute("errorMessage", "Δεν μπορείτε να ακυρώσετε ραντεβού που απέχει λιγότερο από 3 ημέρες.");
                showAppointments(request, response);
                return;
            }

            // Διαγραφή του ραντεβού
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Appointments WHERE id = ?")) {
                deleteStmt.setInt(1, appointmentId);
                deleteStmt.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/patient/appointments?cancelSuccess=1");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/patient/appointments?cancelError=1");
        }
    }

    // Έλεγχος διαπιστευτηρίων ασθενούς
    private boolean checkCredentials(String username, String password) {
        String query = "SELECT password FROM Patients WHERE username = ?";

        try (Connection conn = ConnectDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    // Έλεγχος αν ο κωδικός ταιριάζει με το hash
                    return BCrypt.checkpw(password, storedHash);
                } else {
                    return false;  // Δεν βρέθηκε χρήστης
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Σφάλμα βάσης δεδομένων
        }
    }
}