<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Διαθεσιμότητα Γιατρού</title>
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
            max-width: 800px;
            width: 100%;
        }

        h1, h2 {
            color: #007BFF;
            margin-bottom: 30px;
            text-align: center;
        }

        form {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            margin-bottom: 30px;
            justify-content: center;
        }

        form input {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            width: 80px;
        }

        form input[type="submit"] {
            background-color: #007BFF;
            color: white;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            width: auto;
        }

        form input[type="submit"]:hover {
            background-color: #0069d9;
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
        }
    </style>
</head>
<body>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/index.jsp">Αρχική</a>
    <a href="${pageContext.request.contextPath}/doctor/profile">Προφίλ</a>
    <a href="${pageContext.request.contextPath}/doctor/availability" class="active">Διαθεσιμότητα</a>
    <a href="${pageContext.request.contextPath}/doctor/appointments">Ραντεβού</a>
    <a href="${pageContext.request.contextPath}/doctor/logout">Αποσύνδεση</a>
</div>

<div class="content">
    <div class="container">
        <h1>Διαχείριση Διαθεσιμότητας</h1>

        <h2>Καταχώρηση Νέας Διαθεσιμότητας</h2>
        <form action="${pageContext.request.contextPath}/doctor/availability" method="post">
            <input type="number" name="day" min="1" max="31" placeholder="Ημέρα" required>
            <input type="number" name="month" min="1" max="12" placeholder="Μήνας" required>
            <input type="number" name="year" min="2024" placeholder="Έτος" required>
            <input type="number" name="hour" min="0" max="23" placeholder="Ώρα" required>
            <input type="submit" value="Προσθήκη">
        </form>

        <h2>Οι διαθεσιμότητές σας</h2>
        ${availabilityHtml}
    </div>
</div>
</body>
</html>