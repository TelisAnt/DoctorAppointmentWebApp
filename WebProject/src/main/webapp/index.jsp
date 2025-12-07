<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HealthClinic</title>
    <!-- Εισαγωγή CSS για εικονίδια από Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="style.css">
    <style>
        /* Στυλ για την κατάσταση σύνδεσης χρήστη */
        .auth-status {
            position: absolute;
            top: 20px;
            right: 20px;
            display: flex;
            gap: 10px;
            align-items: center;
            z-index: 1000;
        }

        .auth-status form {
            margin: 0;
        }

        /* Κλάση για απόκρυψη στοιχείων */
        .hidden {
            display: none !important;
        }
    </style>
</head>
<!-- Header με λογότυπο και navigation -->
<body>
<header>
    <div class="container header-container">
        <div class="logo">
            <i class="fas fa-clinic-medical"></i>
            <h1>HealthClinic</h1>
        </div>
        <nav>
            <ul>
                <li><a href="#home">Αρχική</a></li>
                <li><a href="#services">Υπηρεσίες</a></li>
                <li><a href="#contact">Επικοινωνία</a></li>
                <%
                    // Έλεγχος αν ο χρήστης είναι συνδεδεμένος ως ασθενής, γιατρός ή διαχειριστής
                    HttpSession userSession = request.getSession(false);
                    boolean isPatientLoggedIn = userSession != null && userSession.getAttribute("patientUsername") != null;
                    boolean isDoctorLoggedIn = userSession != null && userSession.getAttribute("doctorId") != null;
                    boolean isAdminLoggedIn = userSession != null && userSession.getAttribute("adminUsername") != null;
                %>
                <!-- Κουμπί σύνδεσης (εμφανίζεται μόνο αν δεν είναι συνδεδεμένος κάποιος χρήστης) -->
                <li><a href="loginpage.jsp" class="btn btn-primary <%= (isPatientLoggedIn || isDoctorLoggedIn || isAdminLoggedIn) ? "hidden" : "" %>">Σύνδεση</a></li>
            </ul>
        </nav>

        <!-- Περιοχή κατάστασης σύνδεσης -->
        <div class="auth-status">
            <%
                // Αν είναι συνδεδεμένος ασθενής
                if (isPatientLoggedIn) {
            %>
            <a href="${pageContext.request.contextPath}/patient/profile" class="btn btn-primary">
                <i class="fas fa-user-injured"></i> Προφίλ Ασθενή
            </a>
            <form action="${pageContext.request.contextPath}/patient/logout" method="post" style="display:inline;">
                <button type="submit" class="btn btn-outline">
                    <i class="fas fa-sign-out-alt"></i> Αποσύνδεση
                </button>
            </form>
            <%
                // Αν είναι συνδεδεμένος γιατρός
                } else if (isDoctorLoggedIn) {
            %>
            <a href="${pageContext.request.contextPath}/doctor/profile" class="btn btn-primary">
                <i class="fas fa-user-md"></i> Προφίλ Γιατρού
            </a>
            <form action="${pageContext.request.contextPath}/doctor/logout" method="post" style="display:inline;">
                <button type="submit" class="btn btn-outline">
                    <i class="fas fa-sign-out-alt"></i> Αποσύνδεση
                </button>
            </form>
            <%
                // Αν είναι συνδεδεμένος διαχειριστής
                } else if (isAdminLoggedIn) {
            %>
            <a href="${pageContext.request.contextPath}/administrator/dashboard" class="btn btn-primary">
                <i class="fas fa-user-shield"></i> Προφίλ Διαχειριστή
            </a>
            <form action="${pageContext.request.contextPath}/administrator/logout" method="post" style="display:inline;">
                <button type="submit" class="btn btn-outline">
                    <i class="fas fa-sign-out-alt"></i> Αποσύνδεση
                </button>
            </form>
            <%
                }
            %>
        </div>
    </div>
</header>

<!-- Κύριο banner -->
<section class="hero" id="home">
    <div class="container">
        <div class="hero-content">
            <h2>Υγειονομική Περίθαλψη</h2>
            <p>Στη HealthClinic, παρέχουμε ιατρικές υπηρεσίες υψηλής ποιότητας με μία προσέγγιση που επικεντρώνεται στον ασθενή.</p>
            <div class="hero-buttons">
                <a href="loginpage.jsp" class="btn btn-outline">Λογαριασμός</a>
            </div>
        </div>
    </div>
</section>

<!-- Ενότητα υπηρεσιών -->
<section class="section" id="services">
    <div class="container">
        <h2 class="section-title">Οι Ιατρικές μας Υπηρεσίες</h2>
        <p class="section-subtitle">Ολοκληρωμένη φροντίδα για όλες τις ανάγκες υγείας σας</p>

        <div class="services-grid">
            <!-- Κάρτα υπηρεσίας 1 -->
            <div class="service-card">
                <div class="service-img">
                    <img src="https://images.unsplash.com/photo-1576091160550-2173dba999ef?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Γενική Ιατρική">
                </div>
                <div class="service-content">
                    <i class="fas fa-stethoscope"></i>
                    <h3>Γενική Ιατρική</h3>
                    <p>Ολοκληρωμένη πρωτοβάθμια φροντίδα για ασθενείς όλων των ηλικιών ηλικίες.</p>
                </div>
            </div>

            <!-- Κάρτα υπηρεσίας 2 -->
            <div class="service-card">
                <div class="service-img">
                    <img src="https://images.unsplash.com/photo-1581595219315-a187dd40c322?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Παιδιατρική">
                </div>
                <div class="service-content">
                    <i class="fas fa-baby"></i>
                    <h3>Παιδιατρική</h3>
                    <p>Εξειδικευμένη φροντίδα για βρέφη, παιδιά και εφήβους.</p>
                </div>
            </div>

            <!-- Κάρτα υπηρεσίας 3 -->
            <div class="service-card">
                <div class="service-img">
                    <img src="https://images.unsplash.com/photo-1551601651-bc60f254d532?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Καρδιολογία">
                </div>
                <div class="service-content">
                    <i class="fas fa-heartbeat"></i>
                    <h3>Καρδιολογία</h3>
                    <p>Προηγμένες διαγνωστικές και θεραπευτικές υπηρεσίες.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Ενότητα επικοινωνίας -->
<section class="section" id="contact">
    <div class="container">
        <h2 class="section-title">Επικοινωνήστε μαζί μας</h2>

        <div class="contact-container">
            <div class="contact-info">
                <h3>Οι πληροφορίες μας</h3>
                <div class="contact-item">
                    <i class="fas fa-map-marker-alt"></i>
                    <div>
                        <h4>Τοποθεσία</h4>
                        <p>Καραολή & Δημητρίου 80, Πειραιάς</p>
                    </div>
                </div>
                <div class="contact-item">
                    <i class="fas fa-phone-alt"></i>
                    <div>
                        <h4>Τηλέφωνο</h4>
                        <p> +30 6945612356</p>
                    </div>
                </div>
            </div>

            <!-- Φόρμα επικοινωνίας -->
            <div class="contact-form">
                <h3>Στείλτε μας Μήνυμα</h3>
                <form>
                    <div class="form-group">
                        <input type="text" placeholder="Ονομα" required>
                    </div>
                    <div class="form-group">
                        <input type="email" placeholder="E-mail" required>
                    </div>
                    <div class="form-group">
                        <textarea placeholder="Το μήνυμα σας" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Στείλτε Μήνυμα</button>
                </form>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<footer>
    <div class="container">
        <div class="footer-container">
            <div class="footer-col">
                <div class="logo">
                    <i class="fas fa-clinic-medical"></i>
                    <h4>HealthClinic</h4>
                </div>
            </div>

            <!-- Σύνδεσμοι footer -->
            <div class="footer-col">
                <h4> Links</h4>
                <ul>
                    <li><a href="#home">Αρχική</a></li>
                    <li><a href="patientusers/login.jsp">Login Ασθενή</a></li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            <p>&copy; 2025 HealthClinic. All Rights Reserved.</p>
        </div>
    </div>
</footer>
</body>
</html>