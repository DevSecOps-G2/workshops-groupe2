<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inscription - Messagerie Interne</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background: linear-gradient(to right, #0d6efd, #4dabf7);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .card {
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
        }

        .titre-logo {
            color: #0d6efd;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="card p-4" style="width: 450px;">
    <h3 class="text-center titre-logo mb-3">Créer un compte</h3>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <%= request.getAttribute("error") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <form action="register" method="post" id="signupForm">
        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">Prénom</label>
                <input type="text" name="firstName" class="form-control" required value="<%= request.getAttribute("firstName") != null ? request.getAttribute("firstName") : "" %>">
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">Nom</label>
                <input type="text" name="lastName" class="form-control" required value="<%= request.getAttribute("lastName") != null ? request.getAttribute("lastName") : "" %>">
            </div>
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="text" name="email" class="form-control" required value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>">
        </div>

        <div class="mb-3">
            <label class="form-label">Téléphone</label>
            <input type="text" name="phone" class="form-control" value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>">
        </div>

        <div class="mb-3">
            <label class="form-label">Mot de passe</label>
            <input type="password" id="password" name="password" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Confirmer le mot de passe</label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
            <div class="invalid-feedback">
                Les mots de passe ne correspondent pas.
            </div>
        </div>

        <button type="submit" class="btn btn-primary w-100">S'inscrire</button>

        <p class="text-center mt-3">
            Vous avez déjà un compte ? <a href="login">Se connecter</a>
        </p>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById("signupForm").addEventListener("submit", function (event) {
        let password = document.getElementById("password");
        let confirmPassword = document.getElementById("confirmPassword");

        if (password.value !== confirmPassword.value) {
            confirmPassword.classList.add("is-invalid");
            event.preventDefault();
        } else {
            confirmPassword.classList.remove("is-invalid");
        }
    });
</script>

</body>
</html>

