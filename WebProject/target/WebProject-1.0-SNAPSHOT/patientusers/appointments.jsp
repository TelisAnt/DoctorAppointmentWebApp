<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ιστορικό Ραντεβού</title>
    <style>
        /* (Ίδιο CSS όπως πριν) */
        body {
            margin: 0;
            padding: 0;
            font-family: "Segoe UI", sans-serif;
            background-color: #f7f9fa;
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            background-color: #e3e6ea;
            padding: 25px;
            width: 220px;
            box-shadow: 2px 0 5px rgba(0,0,0,0.05);
        }
        .sidebar a {
            display: block;
            margin: 10px 0;
            padding: 10px 15px;
            text-decoration: none;
            color: #333;
            font-weight: 500;
            border-radius: 6px;
            transition: background-color 0.2s ease, color 0.2s ease;
        }
        .sidebar a:hover {
            background-color: #d0d8e0;
        }
        .sidebar a.active {
            background-color: #007BFF;
            color: #fff;
            font-weight: bold;
        }
        .content {
            flex-grow: 1;
            padding: 40px;
        }
        .content h1 {
            color: #444;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .btn {
            padding: 6px 10px;
            background-color: #dc3545;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.9em;
        }
        .btn:hover {
            background-color: #c82333;
        }
        .message {
            padding: 10px 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
    </style>
</head>
<body>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/index.jsp">Αρχική</a>
    <a href="${pageContext.request.contextPath}/patient/profile">Προφίλ</a>
    <a href="${pageContext.request.contextPath}/patient/appointments" class="active">Ιστορικό Ραντεβού</a>
    <a href="${pageContext.request.contextPath}/patient/available-appointments">Διαθέσιμα Ραντεβού</a>
    <a href="${pageContext.request.contextPath}/patient/logout">Αποσύνδεση</a>
</div>

<div class="content">
    <h1>Ιστορικό Ραντεβού</h1>

    <!-- Μήνυμα λάθους -->
    <c:if test="${not empty errorMessage}">
        <div class="message error">${errorMessage}</div>
    </c:if>

    <!-- Μήνυμα επιτυχίας ακύρωσης -->
    <c:if test="${param.cancelSuccess == '1'}">
        <div class="message success">Το ραντεβού ακυρώθηκε επιτυχώς.</div>
    </c:if>

    <c:if test="${not empty appointments}">
        <table>
            <tr>
                <th>Γιατρός</th>
                <th>Ειδικότητα</th>
                <th>Ημερομηνία</th>
                <th>Ώρα</th>
                <th>Ενέργεια</th>
            </tr>
            <c:forEach var="appointment" items="${appointments}">
                <tr>
                    <td>${appointment.doctorName}</td>
                    <td>${appointment.specialty}</td>
                    <td>${appointment.day}/${appointment.month}/${appointment.year}</td>
                    <td>${appointment.hour}:00</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/patient/cancelAppointment" onsubmit="return confirm('Είστε σίγουροι ότι θέλετε να ακυρώσετε αυτό το ραντεβού;');">
                            <input type="hidden" name="appointmentId" value="${appointment.appointmentId}"/>
                            <button type="submit" class="btn">Ακύρωση</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${empty appointments}">
        <p>Δεν έχετε καμία κράτηση ραντεβού.</p>
    </c:if>
</div>
</body>
</html>
