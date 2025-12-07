<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String adminUsername = (String) session.getAttribute("adminUsername");
  if (adminUsername == null) {
    response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
    return;
  }

  String currentPage = request.getParameter("currentPage");
%>
<div class="sidebar">
  <div class="sidebar-header">
    <i class="fas fa-user-shield fa-3x"></i>
    <h3><%= adminUsername %></h3>
    <p>Διαχειριστής Συστήματος</p>
  </div>

  <a href="${pageContext.request.contextPath}/index.jsp">
    <i class="fas fa-home"></i> Αρχική Σελίδα
  </a>
  <a href="${pageContext.request.contextPath}/administrator/dashboard" <%= "dashboard".equals(currentPage) ? "class=\"active\"" : "" %>>
    <i class="fas fa-tachometer-alt"></i> Πίνακας Ελέγχου
  </a>
  <a href="${pageContext.request.contextPath}/administrator/addDoctorForm" <%= "addDoctor".equals(currentPage) ? "class=\"active\"" : "" %>>
    <i class="fas fa-user-plus"></i> Προσθήκη Γιατρού
  </a>
  <a href="${pageContext.request.contextPath}/administrator/listDoctors" <%= "listDoctors".equals(currentPage) ? "class=\"active\"" : "" %>>
    <i class="fas fa-user-md"></i> Διαχείριση Γιατρών
  </a>
  <a href="${pageContext.request.contextPath}/administrator/addPatientForm" <%= "addPatient".equals(currentPage) ? "class=\"active\"" : "" %>>
    <i class="fas fa-user-plus"></i> Προσθήκη Ασθενούς
  </a>
  <a href="${pageContext.request.contextPath}/administrator/listPatients" <%= "listPatients".equals(currentPage) ? "class=\"active\"" : "" %>>
    <i class="fas fa-users"></i> Διαχείριση Ασθενών
  </a>
  <a href="${pageContext.request.contextPath}/administrator/logout" class="logout-btn">
    <i class="fas fa-sign-out-alt"></i> Αποσύνδεση
  </a>
</div>