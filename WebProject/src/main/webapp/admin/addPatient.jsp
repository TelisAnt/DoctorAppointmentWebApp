<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="el">
<head>
    <meta charset="UTF-8">
    <title>Προσθήκη Ασθενούς</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        <%@ include file="../admin/styles.css" %>
        .form-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
        }

        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .btn-submit {
            background-color: #28a745;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .message {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="content">
    <div class="card">
        <div class="card-header">
            <h2><i class="fas fa-user-plus"></i> Προσθήκη Νέου Ασθενούς</h2>
        </div>

        <div class="form-container">
            <c:if test="${not empty successMessage}">
                <div class="message success">${successMessage}</div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="message error">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/administrator/addPatient" method="post">
                <div class="form-group">
                    <label for="amka">AMKA:</label>
                    <input type="text" id="amka" name="amka" required>
                </div>

                <div class="form-group">
                    <label for="name">Όνομα:</label>
                    <input type="text" id="name" name="name" required>
                </div>

                <div class="form-group">
                    <label for="surname">Επώνυμο:</label>
                    <input type="text" id="surname" name="surname" required>
                </div>

                <div class="form-group">
                    <label for="username">Όνομα Χρήστη:</label>
                    <input type="text" id="username" name="username" required>
                </div>

                <div class="form-group">
                    <label for="password">Κωδικός Πρόσβασης:</label>
                    <input type="password" id="password" name="password" required>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                </div>

                <div class="form-group">
                    <label for="phone">Τηλέφωνο:</label>
                    <input type="text" id="phone" name="phone" required>
                </div>

                <div class="form-group">
                    <label for="age">Ηλικία:</label>
                    <input type="number" id="age" name="age" required>
                </div>

                <button type="submit" class="btn-submit">
                    <i class="fas fa-save"></i> Αποθήκευση
                </button>
            </form>
        </div>
    </div>
</div>
</body>
</html>