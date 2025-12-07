<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="el">
<head>
  <meta charset="UTF-8">
  <title>Λίστα Γιατρών</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <style>
    <%@ include file="../admin/styles.css" %>
    .table-container {
      overflow-x: auto;
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }

    th, td {
      padding: 12px 15px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }

    th {
      background-color: #f8f9fa;
      font-weight: 600;
    }

    tr:hover {
      background-color: #f8f9fa;
    }

    .action-btn {
      padding: 5px 10px;
      margin: 0 3px;
      border-radius: 4px;
      font-size: 13px;
      text-decoration: none;
    }

    .btn-danger {
      background-color: #e74c3c;
      color: white;
    }

    .btn-danger:hover {
      background-color: #c0392b;
    }
  </style>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="content">
  <div class="card">
    <div class="card-header">
      <h2><i class="fas fa-user-md"></i> Διαχείριση Γιατρών</h2>
    </div>

    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th>Username</th>
          <th>Όνομα</th>
          <th>Ειδικότητα</th>
          <th>Email</th>
          <th>Ενέργειες</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="doctor" items="${doctors}">
          <tr>
            <td>${doctor.username}</td>
            <td>${doctor.name}</td>
            <td>${doctor.specialty}</td>
            <td>${doctor.email}</td>
            <td>
              <form action="${pageContext.request.contextPath}/administrator/deleteDoctor" method="post" style="display: inline;">
                <input type="hidden" name="doctorId" value="${doctor.id}">
                <button type="submit" class="action-btn btn-danger">
                  <i class="fas fa-trash-alt"></i> Διαγραφή
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>
</body>
</html>