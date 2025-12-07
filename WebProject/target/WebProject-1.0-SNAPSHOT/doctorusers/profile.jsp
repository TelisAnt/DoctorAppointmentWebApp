<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Προφίλ Γιατρού</title>
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
            justify-content: center;
            text-align: center;
        }

        .profile-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            width: 100%;
        }

        .content h1 {
            color: #007BFF;
            margin-bottom: 30px;
            font-size: 28px;
        }

        .profile-info {
            display: flex;
            flex-direction: column;
            gap: 20px;
            margin-top: 20px;
        }

        .info-item {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }

        .info-item:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: 600;
            color: #555;
            flex: 1;
            text-align: left;
        }

        .info-value {
            color: #222;
            flex: 2;
            text-align: right;
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
            .profile-container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/index.jsp">Αρχική</a>
    <a href="${pageContext.request.contextPath}/doctor/profile" class="active">Προφίλ</a>
    <a href="${pageContext.request.contextPath}/doctor/availability">Διαθεσιμότητα</a>
    <a href="${pageContext.request.contextPath}/doctor/appointments">Ραντεβού</a>
    <a href="${pageContext.request.contextPath}/doctor/logout">Αποσύνδεση</a>
</div>

<div class="content">
    <div class="profile-container">
        <h1>Καλώς ήρθατε, Dr. ${doctor.name}</h1>

        <div class="profile-info">
            <div class="info-item">
                <span class="info-label">Όνομα χρήστη:</span>
                <span class="info-value">${doctor.username}</span>
            </div>
            <div class="info-item">
                <span class="info-label">Email:</span>
                <span class="info-value">${doctor.email}</span>
            </div>
            <div class="info-item">
                <span class="info-label">Ειδικότητα:</span>
                <span class="info-value">${doctor.specialty}</span>
            </div>
        </div>
    </div>
</div>
</body>
</html>