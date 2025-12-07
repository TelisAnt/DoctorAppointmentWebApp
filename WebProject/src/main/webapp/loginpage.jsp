<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="el">
<head>
    <meta charset="UTF-8">
    <title>Clinic Login Portal</title>
    <style>
        /* Βασικά στυλ για τη σελίδα */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background-color: #f8f9fa;
            background-image: linear-gradient(to bottom right, #f8f9fa, #e9ecef);
        }

        /* Κουτί περιέκτηρα */
        .container {
            display: flex;
            max-width: 900px;
            width: 90%;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            border-radius: 15px;
            overflow: hidden;
        }

        /* Περιοχή φόρμας */
        .form-box {
            background: white;
            padding: 40px;
            width: 100%;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        /* Περιοχή εικόνας */
        .image-box {
            width: 50%;
            background: url('https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80') center/cover no-repeat;
            position: relative;
        }

        /* Overlay για την εικόνα */
        .image-box::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 123, 255, 0.1);
        }

        /* Στυλ για τον τίτλο */
        .form-box h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #2c3e50;
            font-size: 28px;
        }

        /* Στυλ για τα κουμπιά επιλογής σύνδεσης */
        .login-option {
            display: block;
            width: 80%;
            margin: 15px 0;
            padding: 15px 20px;
            background-color: #007BFF;
            color: white;
            text-align: center;
            border-radius: 6px;
            font-size: 18px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
        }

        /* Εφέ όταν το ποντίκι είναι πάνω από το κουμπί */
        .login-option:hover {
            background-color: #0069d9;
            transform: translateY(-3px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        /* Στυλ για εικονίδια */
        .login-option i {
            margin-right: 10px;
        }

        /* Κουμπί επιστροφής */
        .sidebar-button {
            position: fixed;
            top: 20px;
            left: 20px;
            background-color: #e3e6ea;
            padding: 10px 15px;
            text-decoration: none;
            color: #333;
            font-weight: 500;
            border-radius: 6px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
            z-index: 1000;
            display: flex;
            align-items: center;
        }

        /* Εφέ όταν το ποντίκι είναι πάνω από το κουμπί επιστροφής */
        .sidebar-button:hover {
            background-color: #d0d8e0;
            transform: translateY(-2px);
        }

        /* Στυλ για εικονίδιο στο κουμπί επιστροφής */
        .sidebar-button i {
            margin-right: 8px;
        }

        /* Στυλ για το κείμενο καλωσορίσματος */
        .welcome-text {
            text-align: center;
            margin-bottom: 30px;
            color: #555;
            font-size: 16px;
            line-height: 1.6;
        }

        /* Responsive στυλ για μικρές οθόνες */
        @media (max-width: 768px) {
            .container {
                flex-direction: column;
            }
            .form-box, .image-box {
                width: 100%;
            }
            .image-box {
                height: 200px;
            }
            .login-option {
                width: 90%;
                padding: 12px 15px;
                font-size: 16px;
            }
        }
    </style>
    <!-- Εισαγωγή CSS για εικονίδια από Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
<!-- Κουμπί επιστροφής στην αρχική σελίδα -->
<a href="<%= request.getContextPath() %>/index.jsp" class="sidebar-button">
    <i class="fas fa-arrow-left"></i> Αρχική
</a>

<div class="container">
    <div class="form-box">
        <h2>Σύστημα Διαχείρισης Κλινικής</h2>
        <p class="welcome-text">
            Καλώς ήλθατε στο σύστημα διαχείρισης της κλινικής μας.
            Παρακαλώ επιλέξτε τον τύπο χρήστη για να συνεχίσετε.
        </p>

        <!-- Σύνδεσμοι για σύνδεση ως διαφορετικοί τύποι χρηστών -->
        <a href="${pageContext.request.contextPath}/patientusers/login.jsp" class="login-option">
            <i class="fas fa-user-injured"></i> Ασθενής
        </a>

        <a href="${pageContext.request.contextPath}/doctorusers/login.jsp" class="login-option">
            <i class="fas fa-user-md"></i> Γιατρός
        </a>

        <a href="${pageContext.request.contextPath}/admin/login.jsp" class="login-option">
            <i class="fas fa-user-shield"></i> Διαχειριστής
        </a>
    </div>

    <div class="image-box"></div>
</div>
</body>
</html>