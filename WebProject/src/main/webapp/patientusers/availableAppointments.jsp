<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Διαθέσιμα Ραντεβού</title>
    <style>
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
            padding: 8px 12px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .btn:hover {
            background-color: #0069d9;
        }

        select, button {
            padding: 8px 12px;
            margin: 5px 0;
            border-radius: 4px;
            border: 1px solid #ddd;
        }
    </style>
</head>
<body>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/index.jsp">Αρχική</a>
    <a href="${pageContext.request.contextPath}/patient/profile">Προφίλ</a>
    <a href="${pageContext.request.contextPath}/patient/appointments">Ιστορικό Ραντεβού</a>
    <a href="${pageContext.request.contextPath}/patient/available-appointments" class="active">Διαθέσιμα Ραντεβού</a>
    <a href="${pageContext.request.contextPath}/patient/logout">Αποσύνδεση</a>
</div>

<div class="content">
    <h1>Διαθέσιμα Ραντεβού</h1>

    <form method="get" action="${pageContext.request.contextPath}/patient/available-appointments">
        <label for="specialty">Επιλέξτε ειδικότητα:</label>
        <select name="specialty" id="specialty" required>
            <option value="" disabled selected>-- Επιλέξτε ειδικότητα --</option>
            <option value="Cardiology">Καρδιολογία</option>
            <option value="Dermatology">Δερματολογία</option>
        </select>
        <button type="submit" class="btn">Αναζήτηση</button>
    </form>

    <c:if test="${not empty availabilityList}">
        <table>
            <tr>
                <th>Γιατρός</th>
                <th>Ειδικότητα</th>
                <th>Ημερομηνία</th>
                <th>Ώρα</th>
                <th>Ενέργεια</th>
            </tr>
            <c:forEach var="slot" items="${availabilityList}">
                <tr>
                    <td>${slot.doctorName}</td>
                    <td>${slot.specialty}</td>
                    <td>${slot.day}/${slot.month}/${slot.year}</td>
                    <td>${slot.hour}:00</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/patient/book-appointment">
                            <input type="hidden" name="availabilityId" value="${slot.id}">
                            <button type="submit" class="btn">Κλείσε</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${empty availabilityList}">
        <p>Δεν υπάρχουν διαθέσιμα ραντεβού για αυτή την ειδικότητα.</p>
    </c:if>
</div>
</body>
</html>