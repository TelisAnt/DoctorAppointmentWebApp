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

// Κύριο Servlet για τη διαχείριση των λειτουργιών των ιατρών
@WebServlet(name = "DoctorServlet", urlPatterns = {"/doctor/*"})
public class DoctorServlet extends HttpServlet {

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
            case "/login" -> doctorLogin(request, response);  // Σελίδα σύνδεσης
            case "/profile" -> showDoctorProfile(request, response);  // Προφίλ ιατρού
            case "/logout" -> logout(request, response);  // Αποσύνδεση
            case "/availability" -> showAvailabilityForm(request, response);  // Φόρμα διαθεσιμότητας
            case "/availabilityList" -> listAvailability(request, response);  // Λίστα διαθεσιμοτήτων
            case "/appointments" -> showAppointments(request, response);  // Λίστα ραντεβού
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
            case "/login" -> doctorLogin(request, response);  // Επεξεργασία σύνδεσης
            case "/logout" -> logout(request, response);  // Αποσύνδεση
            case "/availability" -> addAvailability(request, response);  // Προσθήκη διαθεσιμότητας
            case "/deleteAvailability" -> deleteAvailability(request, response);  // Διαγραφή διαθεσιμότητας
            case "/cancelAppointment" -> cancelAppointment(request, response);  // Ακύρωση ραντεβού
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);  // Σφάλμα 404
        }
    }

    // Σύνδεση ιατρού
    private void doctorLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "SELECT id, password FROM doctors WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        int doctorId = rs.getInt("id");

                        // Έλεγχος κωδικού με BCrypt
                        if (BCrypt.checkpw(password, hashedPassword)) {
                            HttpSession session = request.getSession();
                            session.setAttribute("doctorId", doctorId);
                            session.setAttribute("doctorUsername", username);

                            // Επιτυχής σύνδεση - ανακατεύθυνση στο προφίλ
                            response.sendRedirect(request.getContextPath() + "/doctor/profile");
                            return;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Αποτυχία σύνδεσης - επιστροφή με σφάλμα
        request.setAttribute("loginError", true);
        request.getRequestDispatcher("/doctorusers/login.jsp?error=1").forward(request, response);
    }

    // Εμφάνιση προφίλ ιατρού
    private void showDoctorProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("doctorUsername");

        // Έλεγχος αν ο ιατρός είναι συνδεδεμένος
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
            return;
        }

        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM doctors WHERE username = ?")) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Ανάκτηση στοιχείων ιατρού από τη βάση
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String specialty = rs.getString("specialty");
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    Doctor doctor = new Doctor(id, username, name, specialty, email, password);

                    // Αποθήκευση στοιχείων στο session και request
                    session.setAttribute("user", doctor);
                    request.setAttribute("doctor", doctor);

                    // Εμφάνιση της σελίδας προφίλ
                    request.getRequestDispatcher("/doctorusers/profile.jsp").forward(request, response);
                } else {
                    // Αν δεν βρεθεί ο ιατρός - ανακατεύθυνση στη σύνδεση
                    response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
        }
    }

    // Αποσύνδεση ιατρού
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // Καταστροφή session
        }
        response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");  // Ανακατεύθυνση στη σύνδεση
    }

    // Εμφάνιση φόρμας διαθεσιμότητας
    private void showAvailabilityForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("doctorId");
        
        // Έλεγχος εξουσιοδότησης
        if (doctorId == null) {
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
            return;
        }

        List<Availability> slots = new ArrayList<>();
        StringBuilder availabilityHtml = new StringBuilder();

        try (Connection conn = ConnectDatabase.getConnection()) {
            // Ανάκτηση όλων των διαθεσιμοτήτων του ιατρού
            String sql = "SELECT id, day, month, year, hour FROM Availability WHERE doctor_id = ? ORDER BY year, month, day, hour";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, doctorId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Availability slot = new Availability(
                                rs.getInt("id"),
                                doctorId,
                                rs.getInt("day"),
                                rs.getInt("month"),
                                rs.getInt("year"),
                                rs.getInt("hour")
                        );
                        slots.add(slot);
                    }
                }
            }

            // Δημιουργία HTML λίστας με τις διαθεσιμότητες
            if (slots.isEmpty()) {
                availabilityHtml.append("<p>Δεν έχετε ακόμα καταχωρήσει διαθεσιμότητες.</p>");
            } else {
                availabilityHtml.append("<ul>");
                for (Availability slot : slots) {
                    availabilityHtml.append("<li>")
                            .append(slot.getDay()).append("/").append(slot.getMonth()).append("/").append(slot.getYear())
                            .append(" - ").append(slot.getHour()).append(":00")
                            .append("<form action=\"").append(request.getContextPath()).append("/doctor/deleteAvailability\" method=\"post\" style=\"display:inline;\">")
                            .append("<input type=\"hidden\" name=\"slotId\" value=\"").append(slot.getId()).append("\">")
                            .append("<input type=\"submit\" value=\"Διαγραφή\">")
                            .append("</form>")
                            .append("</li>");
                }
                availabilityHtml.append("</ul>");
            }

            request.setAttribute("availabilityHtml", availabilityHtml.toString());
            request.getRequestDispatcher("/doctorusers/availability.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/doctor/availability?error=1");
        }
    }

    // Προσθήκη νέας διαθεσιμότητας
    private void addAvailability(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("doctorId");
        
        // Έλεγχος εξουσιοδότησης
        if (doctorId == null) {
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
            return;
        }

        try (Connection conn = ConnectDatabase.getConnection()) {
            // Ανάγνωση παραμέτρων από τη φόρμα
            int day = Integer.parseInt(request.getParameter("day"));
            int month = Integer.parseInt(request.getParameter("month"));
            int year = Integer.parseInt(request.getParameter("year"));
            int hour = Integer.parseInt(request.getParameter("hour"));

            // Εισαγωγή νέας διαθεσιμότητας στη βάση
            String insertQuery = "INSERT INTO Availability (doctor_id, day, month, year, hour) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setInt(1, doctorId);
                stmt.setInt(2, day);
                stmt.setInt(3, month);
                stmt.setInt(4, year);
                stmt.setInt(5, hour);
                stmt.executeUpdate();
            }
            response.sendRedirect(request.getContextPath() + "/doctor/availability");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/doctor/availability?error=1");
        }
    }

    // Λίστα με τις διαθεσιμότητες του ιατρού
    private void listAvailability(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("doctorId");
        
        // Έλεγχος εξουσιοδότησης
        if (doctorId == null) {
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
            return;
        }

        List<Availability> slots = new ArrayList<>();

        try (Connection conn = ConnectDatabase.getConnection()) {
            // Ανάκτηση διαθεσιμοτήτων από τη βάση
            String sql = "SELECT id, day, month, year, hour FROM Availability WHERE doctor_id = ? ORDER BY year, month, day, hour";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, doctorId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Availability slot = new Availability(
                                rs.getInt("id"),
                                doctorId,
                                rs.getInt("day"),
                                rs.getInt("month"),
                                rs.getInt("year"),
                                rs.getInt("hour")
                        );
                        slots.add(slot);
                    }
                }
            }

            request.setAttribute("availabilitySlots", slots);
            request.getRequestDispatcher("/doctorusers/availability.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/doctor/availability?error=1");
        }
    }

    // Διαγραφή διαθεσιμότητας
    private void deleteAvailability(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("doctorId");
        
        // Έλεγχος εξουσιοδότησης
        if (doctorId == null) {
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
            return;
        }

        int slotId = Integer.parseInt(request.getParameter("slotId"));

        try (Connection conn = ConnectDatabase.getConnection()) {
            // Διαγραφή της διαθεσιμότητας από τη βάση
            String sql = "DELETE FROM Availability WHERE id = ? AND doctor_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, slotId);
                stmt.setInt(2, doctorId);
                stmt.executeUpdate();
            }
            response.sendRedirect(request.getContextPath() + "/doctor/availability");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/doctor/availabilityList?deleteError=1");
        }
    }

    // Εμφάνιση των ραντεβού του ιατρού
    private void showAppointments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("doctorId");
        
        // Έλεγχος εξουσιοδότησης
        if (doctorId == null) {
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
            return;
        }

        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = ConnectDatabase.getConnection()) {
            // Ανάκτηση ραντεβού από τη βάση με στοιχεία ασθενούς
            String query = """
                SELECT a.id, a.day, a.month, a.year, a.hour, p.name AS patient_name, p.surname AS patient_surname, a.reason
                FROM Appointments a
                JOIN Patients p ON a.patient_amka = p.amka
                WHERE a.doctor_id = ?
                ORDER BY a.year, a.month, a.day, a.hour
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, doctorId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Appointment app = new Appointment(
                                rs.getInt("id"),
                                rs.getInt("hour"),
                                rs.getInt("month"),
                                rs.getInt("year"),
                                rs.getString("patient_name") + " " + rs.getString("patient_surname"),
                                rs.getString("reason"),
                                rs.getInt("day")
                        );
                        appointments.add(app);
                    }
                }
            }

            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("/doctorusers/appointments.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
        }
    }

    // Ακύρωση ραντεβού από τον ιατρό
    private void cancelAppointment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("doctorId");
        
        // Έλεγχος εξουσιοδότησης
        if (doctorId == null) {
            response.sendRedirect(request.getContextPath() + "/doctorusers/login.jsp");
            return;
        }

        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        try (Connection conn = ConnectDatabase.getConnection()) {
            // Εύρεση ημερομηνίας ραντεβού
            String selectQuery = "SELECT day, month, year FROM Appointments WHERE id = ? AND doctor_id = ?";
            LocalDate appointmentDate;

            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, appointmentId);
                stmt.setInt(2, doctorId);
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

            response.sendRedirect(request.getContextPath() + "/doctor/appointments?cancelSuccess=1");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/doctor/appointments?cancelError=1");
        }
    }
}