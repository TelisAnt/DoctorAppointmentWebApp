<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Clinic Registration</title>
    <style>
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

        .container {
            display: flex;
            max-width: 900px;
            width: 90%;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            border-radius: 15px;
            overflow: hidden;
        }

        .form-box {
            background: white;
            padding: 40px;
            width: 50%;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .image-box {
            width: 50%;
            background: url('https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80') center/cover no-repeat;
            position: relative;
        }

        .image-box::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 123, 255, 0.1);
        }

        .form-box h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #2c3e50;
            font-size: 28px;
        }

        input {
            display: block;
            margin: 10px 0;
            padding: 12px;
            width: 100%;
            box-sizing: border-box;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            transition: border 0.3s ease;
        }

        input:focus {
            border-color: #007BFF;
            outline: none;
        }

        button {
            background-color: #007BFF;
            border: none;
            padding: 12px 20px;
            width: 100%;
            color: white;
            cursor: pointer;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 500;
            margin-top: 15px;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0069d9;
        }

        .links {
            margin-top: 20px;
            text-align: center;
        }

        a {
            color: #3498db;
            text-decoration: none;
            font-size: 14px;
            display: inline-block;
            margin: 5px 0;
        }

        a:hover {
            text-decoration: underline;
        }

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

        .sidebar-button:hover {
            background-color: #d0d8e0;
            transform: translateY(-2px);
        }

        .sidebar-button i {
            margin-right: 8px;
        }

        .error-message {
            color: #e74c3c;
            text-align: center;
            margin-bottom: 15px;
            font-size: 14px;
        }

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
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
<a href="<%= request.getContextPath() %>/index.jsp" class="sidebar-button">
    <i class="fas fa-arrow-left"></i> Αρχική
</a>

<div class="container">
    <div class="form-box">
        <h2>Registration</h2>

        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <p class="error-message"><%= errorMessage %></p>
        <%
            }
        %>

        <form method="post" action="${pageContext.request.contextPath}/patient/register">
            <input type="text" name="username" placeholder="Username" required />
            <input type="password" name="password" placeholder="Password" required />
            <input type="text" name="name" placeholder="First Name" required />
            <input type="text" name="surname" placeholder="Last Name" required />
            <input type="email" name="email" placeholder="Email" required />
            <input type="text" name="phone" placeholder="Phone Number" required />
            <input type="text" name="amka" placeholder="AMKA" required />
            <input type="text" name="age" placeholder="Age" required />

            <button type="submit">
                <i class="fas fa-user-plus"></i> Register
            </button>
        </form>

        <div class="links">
            <a href="<%= request.getContextPath() %>/patientusers/login.jsp">
                <i class="fas fa-sign-in-alt"></i> Έχετε ήδη λογαριασμό; Συνδεθείτε!
            </a>
        </div>
    </div>

    <div class="image-box"></div>
</div>
</body>
</html>