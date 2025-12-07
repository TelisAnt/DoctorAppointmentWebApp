<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mainpackage.Appointment" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ραντεβού Γιατρού</title>
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
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 1000px;
            width: 100%;
        }

        h1 {
            color: #007BFF;
            margin-bottom: 30px;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            text-align: center;
            border-bottom: 1px solid #eee;
        }

        th {
            background-color: #007BFF;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .no-data {
            color: #777;
            text-align: center;
            margin-top: 20px;
        }

        .cancel-button form {
            display: inline;
        }

        .cancel-button button {
            background-color: #dc3545;
            border: none;
            color: white;
            padding: 8px 15px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.2s;
        }

        .cancel-button button:hover {
            background-color: #a71d2a;
        }

        @media (max-width: 768px) {
            body {
                flex-direction: column;
            }
            .sidebar {
                width: 100%;
                padding: 15px;
            }
            .content {
                padding: 20px;
            }
            .container {
                padding: 20px;
            }
            table {
                font-size: 14px;
            }
            th, td {
                padding: 8px;
            }
        }
    </style>
</head>
<body>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/index.jsp">Αρχική</a>
    <a href="${pageContext.request.contextPath}/doctor/profile">Προφίλ</a>
    <a href="${pageContext.request.contextPath}/doctor/availability">Διαθεσιμότητα</a>
    <a href="${pageContext.request.contextPath}/doctor/appointments" class="active">Ραντεβού</a>
    <a href="${pageContext.request.contextPath}/doctor/logout">Αποσύνδεση</a>
</div>

<div class="content">
    <div class="container">
        <h1>Τα Ραντεβού Σας</h1>

        <%
            List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
            if (appointments != null && !appointments.isEmpty()) {
        %>
        <table>
            <tr>
                <th>ID</th>
                <th>Ημερομηνία</th>
                <th>Ώρα</th>
                <th>Ασθενής</th>
                <th>Λόγος</th>
                <th>Ενέργειες</th>
            </tr>
            <%
                for (Appointment appointment : appointments) {
            %>
            <tr>
                <td><%= appointment.getAppointmentId() %></td>
                <td><%= appointment.getDay() %>/<%= appointment.getMonth() %>/<%= appointment.getYear() %></td>
                <td><%= appointment.getHour() %>:00</td>
                <td><%= appointment.getPatientName() %></td>
                <td><%= appointment.getReason() %></td>
                <td class="cancel-button">
                    <form action="<%= request.getContextPath() %>/doctor/cancelAppointment" method="post"
                          onsubmit="return confirm('Είσαι σίγουρος ότι θέλεις να ακυρώσεις το ραντεβού;');">
                        <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>"/>
                        <button type="submit">Ακύρωση</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <%
        } else {
        %>
        <p class="no-data">Δεν υπάρχουν ραντεβού.</p>
        <%
            }
        %>
    </div>
</div>
</body>
</html>