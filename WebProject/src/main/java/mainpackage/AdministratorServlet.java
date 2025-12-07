package mainpackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Κύριο Servlet για τη διαχείριση του διαχειριστή
@WebServlet(name = "AdministratorServlet", urlPatterns = {"/administrator/*"})
public class AdministratorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        // Αν δεν υπάρχει ενέργεια, εμφάνισε τον πίνακα ελέγχου
        if (action == null || action.equals("/")) {
            showDashboard(request, response);
            return;
        }

        // Διαχείριση διαφορετικών GET αιτημάτων
        switch (action) {
            case "/dashboard" -> showDashboard(request, response);  // Πίνακας ελέγχου
            case "/addDoctorForm" -> showAddDoctorForm(request, response);  // Φόρμα προσθήκης γιατρού
            case "/addPatientForm" -> showAddPatientForm(request, response);  // Φόρμα προσθήκης ασθενούς
            case "/listDoctors" -> listDoctors(request, response);  // Λίστα γιατρών
            case "/listPatients" -> listPatients(request, response);  // Λίστα ασθενών
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

        // Διαχείριση διαφορετικών POST αιτημάτων
        switch (action) {
            case "/login" -> adminLogin(request, response);  // Σύνδεση διαχειριστή
            case "/addDoctor" -> addDoctor(request, response);  // Προσθήκη γιατρού
            case "/addPatient" -> addPatient(request, response);  // Προσθήκη ασθενούς
            case "/deleteDoctor" -> deleteDoctor(request, response);  // Διαγραφή γιατρού
            case "/deletePatient" -> deletePatient(request, response);  // Διαγραφή ασθενούς
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);  // Σφάλμα 404
        }
    }

    // Μέθοδος για τη σύνδεση του διαχειριστή
    private void adminLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Έλεγχος εγκυρότητας διαπιστευτηρίων
        boolean isValid = checkCredentials(username, password);

        if (isValid) {
            // Δημιουργία session και αποθήκευση του ονόματος χρήστη
            HttpSession session = request.getSession();
            session.setAttribute("adminUsername", username);
            response.sendRedirect(request.getContextPath() + "/administrator/dashboard");
            System.out.println("Admin logged in: " + username);
        } else {
            // Αποτυχία σύνδεσης - ανακατεύθυνση με σφάλμα
            response.sendRedirect(request.getContextPath() + "/admin/login.jsp?error=1");
        }
    }

    // Έλεγχος των διαπιστευτηρίων στη βάση δεδομένων
    private boolean checkCredentials(String username, String password) {
        String query = "SELECT password FROM Admins WHERE username = ?";

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

    // Αποσύνδεση διαχειριστή
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // Καταστροφή session
        }
        response.sendRedirect(request.getContextPath() + "/admin/login.jsp");  // Ανακατεύθυνση στη σελίδα σύνδεσης
    }

    // Εμφάνιση του πίνακα ελέγχου
    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
    }

    // Εμφάνιση φόρμας προσθήκης γιατρού
    private void showAddDoctorForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAdminAuth(request, response);  // Έλεγχος εξουσιοδότησης
        request.getRequestDispatcher("/admin/addDoctor.jsp").forward(request, response);
    }

    // Εμφάνιση φόρμας προσθήκης ασθενούς
    private void showAddPatientForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAdminAuth(request, response);  // Έλεγχος εξουσιοδότησης
        request.getRequestDispatcher("/admin/addPatient.jsp").forward(request, response);
    }

    // Προσθήκη νέου γιατρού στη βάση δεδομένων
    private void addDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAdminAuth(request, response);  // Έλεγχος εξουσιοδότησης

        // Ανάγνωση παραμέτρων από τη φόρμα
        String username = request.getParameter("username");
        String password = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt());  // Κρυπτογράφηση κωδικού
        String name = request.getParameter("name");
        String specialty = request.getParameter("specialty");
        String email = request.getParameter("email");

        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "INSERT INTO Doctors (username, password, name, specialty, email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, name);
                stmt.setString(4, specialty);
                stmt.setString(5, email);
                stmt.executeUpdate();

                // Μήνυμα επιτυχίας
                request.setAttribute("successMessage", "Ο γιατρός προστέθηκε επιτυχώς!");
                showAddDoctorForm(request, response);  // Επιστροφή στη φόρμα
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Μήνυμα σφάλματος
            request.setAttribute("errorMessage", "Σφάλμα κατά την εισαγωγή γιατρού: " + e.getMessage());
            showAddDoctorForm(request, response);  // Επιστροφή στη φόρμα
        }
    }

    // Προσθήκη νέου ασθενούς στη βάση δεδομένων
    private void addPatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAdminAuth(request, response);  // Έλεγχος εξουσιοδότησης

        // Ανάγνωση παραμέτρων από τη φόρμα
        String amka = request.getParameter("amka");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String username = request.getParameter("username");
        String password = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt());  // Κρυπτογράφηση κωδικού
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        int age = Integer.parseInt(request.getParameter("age"));

        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "INSERT INTO Patients (amka, name, surname, username, password, email, phone, age) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, amka);
                stmt.setString(2, name);
                stmt.setString(3, surname);
                stmt.setString(4, username);
                stmt.setString(5, password);
                stmt.setString(6, email);
                stmt.setString(7, phone);
                stmt.setInt(8, age);
                stmt.executeUpdate();

                // Μήνυμα επιτυχίας
                request.setAttribute("successMessage", "Ο ασθενής προστέθηκε επιτυχώς!");
                showAddPatientForm(request, response);  // Επιστροφή στη φόρμα
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Μήνυμα σφάλματος
            request.setAttribute("errorMessage", "Σφάλμα κατά την εισαγωγή ασθενούς: " + e.getMessage());
            showAddPatientForm(request, response);  // Επιστροφή στη φόρμα
        }
    }

    // Διαγραφή γιατρού από τη βάση δεδομένων
    private void deleteDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAdminAuth(request, response);  // Έλεγχος εξουσιοδότησης

        int doctorId = Integer.parseInt(request.getParameter("doctorId"));

        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "DELETE FROM Doctors WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, doctorId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Μήνυμα επιτυχίας
                    request.setAttribute("successMessage", "Ο γιατρός διαγράφηκε επιτυχώς!");
                } else {
                    // Μήνυμα αν ο γιατρός δεν βρέθηκε
                    request.setAttribute("errorMessage", "Δεν βρέθηκε γιατρός με αυτό το ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Μήνυμα σφάλματος
            request.setAttribute("errorMessage", "Σφάλμα κατά τη διαγραφή γιατρού: " + e.getMessage());
        }

        listDoctors(request, response);  // Επιστροφή στη λίστα γιατρών
    }

    // Διαγραφή ασθενούς από τη βάση δεδομένων
    private void deletePatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAdminAuth(request, response);  // Έλεγχος εξουσιοδότησης

        String amka = request.getParameter("amka");

        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "DELETE FROM Patients WHERE amka = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, amka);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Μήνυμα επιτυχίας
                    request.setAttribute("successMessage", "Ο ασθενής διαγράφηκε επιτυχώς!");
                } else {
                    // Μήνυμα αν ο ασθενής δεν βρέθηκε
                    request.setAttribute("errorMessage", "Δεν βρέθηκε ασθενής με αυτό το AMKA");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Μήνυμα σφάλματος
            request.setAttribute("errorMessage", "Σφάλμα κατά τη διαγραφή ασθενούς: " + e.getMessage());
        }

        listPatients(request, response);  // Επιστροφή στη λίστα ασθενών
    }

    // Λίστα όλων των γιατρών
    private void listDoctors(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Doctor> doctors = new ArrayList<>();

        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "SELECT id, username, name, specialty, email FROM Doctors ORDER BY name";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Doctor doctor = new Doctor(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("specialty"),
                            rs.getString("email"),
                            ""
                    );
                    doctors.add(doctor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("doctors", doctors);
        request.getRequestDispatcher("/admin/listDoctors.jsp").forward(request, response);
    }

    // Λίστα όλων των ασθενών
    private void listPatients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAdminAuth(request, response);  // Έλεγχος εξουσιοδότησης

        List<Patient> patients = new ArrayList<>();

        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "SELECT amka, username, password, name, surname, email, phone, age FROM Patients ORDER BY surname, name";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Patient patient = new Patient(
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("password"),
                            rs.getLong("phone"),
                            rs.getLong("amka")
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Σφάλμα κατά τη φόρτωση ασθενών: " + e.getMessage());
        }

        request.setAttribute("patients", patients);
        request.getRequestDispatcher("/admin/listPatients.jsp").forward(request, response);
    }

    // Έλεγχος αν ο χρήστης είναι εξουσιοδοτημένος διαχειριστής
    private void checkAdminAuth(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
            return;
        }
    }
}