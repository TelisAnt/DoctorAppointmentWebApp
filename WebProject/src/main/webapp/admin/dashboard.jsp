<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Έλεγχος αν ο διαχειριστής είναι συνδεδεμένος
    String adminUsername = (String) session.getAttribute("adminUsername");
    if (adminUsername == null) {
        // Αν δεν είναι συνδεδεμένος, ανακατεύθυνση στη σελίδα σύνδεσης
        response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="el">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <!-- Εισαγωγή CSS για εικονίδια από Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        /* Ορισμός CSS μεταβλητών για τα χρώματα */
        :root {
            --sidebar-bg: #2c3e50;          /* Χρώμα φόντου πλευρικής μπάρας */
            --sidebar-hover: #34495e;       /* Χρώμα όταν το ποντίκι είναι πάνω */
            --sidebar-active: #007BFF;      /* Χρώμα ενεργού στοιχείου */
            --content-bg: #f7f9fa;         /* Χρώμα φόντου κύριας περιοχής */
            --text-dark: #333;             /* Σκούρο χρώμα κειμένου */
            --text-light: #ecf0f1;          /* Ανοιχτό χρώμα κειμένου */
            --card-bg: #ffffff;            /* Χρώμα φόντου καρτών */
            --shadow: 0 2px 10px rgba(0,0,0,0.1);  /* Σκιά */
        }

        /* Βασικά στυλ για τη σελίδα */
        body {
            margin: 0;
            padding: 0;
            font-family: "Segoe UI", sans-serif;
            background-color: var(--content-bg);
            display: flex;
            min-height: 100vh;
            color: var(--text-dark);
        }

        /* Στυλ για την πλευρική μπάρα */
        .sidebar {
            background-color: var(--sidebar-bg);
            padding: 20px;
            width: 250px;
            box-shadow: var(--shadow);
            position: fixed;
            height: 100vh;
            color: var(--text-light);
        }

        /* Στυλ για την κεφαλίδα της πλευρικής μπάρας */
        .sidebar-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }

        .sidebar-header h3 {
            margin: 10px 0 5px;
            color: white;
        }

        .sidebar-header p {
            margin: 0;
            font-size: 13px;
            color: #bdc3c7;
        }

        /* Στυλ για τους συνδέσμους της πλευρικής μπάρας */
        .sidebar a {
            display: flex;
            align-items: center;
            margin: 12px 0;
            padding: 12px 15px;
            text-decoration: none;
            color: var(--text-light);
            font-weight: 500;
            border-radius: 6px;
            transition: all 0.2s ease;
        }

        /* Στυλ για τα εικονίδια των συνδέσμων */
        .sidebar a i {
            margin-right: 10px;
            width: 20px;
            text-align: center;
        }

        /* Στυλ όταν το ποντίκι είναι πάνω από έναν σύνδεσμο */
        .sidebar a:hover {
            background-color: var(--sidebar-hover);
            color: white;
        }

        /* Στυλ για τον ενεργό σύνδεσμο */
        .sidebar a.active {
            background-color: var(--sidebar-active);
            color: white;
        }

        /* Στυλ για την κύρια περιοχή περιεχομένου */
        .content {
            flex-grow: 1;
            margin-left: 250px;
            padding: 40px;
        }

        /* Στυλ για το μήνυμα καλωσορίσματος */
        .welcome-message {
            text-align: center;
            margin-top: 100px;
        }

        .welcome-message h1 {
            color: var(--text-dark);
            font-size: 36px;
            margin-bottom: 20px;
        }

        .welcome-message p {
            color: #7f8c8d;
            font-size: 18px;
        }

        /* Στυλ για το κουμπί αποσύνδεσης */
        .logout-btn {
            background-color: #e74c3c;
            color: white;
        }

        .logout-btn:hover {
            background-color: #c0392b;
        }

        /* Responsive στυλ για μικρές οθόνες */
        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                height: auto;
                position: relative;
                padding: 20px;
            }

            .content {
                margin-left: 0;
                padding: 25px;
            }
        }
    </style>
</head>
<body>

<!-- Πλευρική μπάρα πλοήγησης -->
<div class="sidebar">
    <div class="sidebar-header">
        <i class="fas fa-user-shield fa-3x"></i>
        <h3><%= adminUsername %></h3>
        <p>Διαχειριστής Συστήματος</p>
    </div>

    <!-- Σύνδεσμοι πλοήγησης -->
    <a href="${pageContext.request.contextPath}/index.jsp">
        <i class="fas fa-home"></i> Αρχική Σελίδα
    </a>
    <a href="${pageContext.request.contextPath}/administrator/dashboard" class="active">
        <i class="fas fa-tachometer-alt"></i> Πίνακας Ελέγχου
    </a>
    <a href="${pageContext.request.contextPath}/administrator/addDoctorForm">
        <i class="fas fa-user-plus"></i> Προσθήκη Γιατρού
    </a>
    <a href="${pageContext.request.contextPath}/administrator/listDoctors">
        <i class="fas fa-user-md"></i> Διαχείριση Γιατρών
    </a>
    <a href="${pageContext.request.contextPath}/administrator/addPatientForm">
        <i class="fas fa-user-plus"></i> Προσθήκη Ασθενούς
    </a>
    <a href="${pageContext.request.contextPath}/administrator/listPatients">
        <i class="fas fa-users"></i> Διαχείριση Ασθενών
    </a>
    <a href="${pageContext.request.contextPath}/administrator/logout" class="logout-btn">
        <i class="fas fa-sign-out-alt"></i> Αποσύνδεση
    </a>
</div>

<!-- Κύρια περιοχή περιεχομένου -->
<div class="content">
    <div class="welcome-message">
        <h1>Καλώς ήρθες, Διαχειριστή!</h1>
        <p>Χρησιμοποίησε το μενού στα αριστερά για να περιηγηθείς στο σύστημα</p>
    </div>
</div>
</body>
</html>