<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Paramètres</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <style>
        body {
            background-color: #f8f9fa;
        }

        .settings-card {
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .section-title {
            color: #0d6efd;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="container mt-5 mb-5">

    <div class="row justify-content-center">
        <div class="col-lg-6">

            <!-- Informations du compte -->
            <div class="card settings-card p-4 mb-4">
                <h5 class="section-title mb-3">
                    <i class="bi bi-person-circle me-2"></i>
                    Informations du compte
                </h5>

                <div class="mb-2">
                    <strong>Prénom :</strong>
                    ${sessionScope.user.firstName}
                </div>

                <div class="mb-2">
                    <strong>Nom :</strong>
                    ${sessionScope.user.lastName}
                </div>

                <div class="mb-2">
                    <strong>Email :</strong>
                    ${sessionScope.user.email}
                </div>

                <div class="mb-2">
                    <strong>Téléphone :</strong>
                    ${sessionScope.user.phone}
                </div>
            </div>

            <!-- Changer mot de passe -->
            <div class="card settings-card p-4">
                <h5 class="section-title mb-3">
                    <i class="bi bi-lock me-2"></i>
                    Changer le mot de passe
                </h5>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                            ${error}
                    </div>
                </c:if>

                <c:if test="${not empty success}">
                    <div class="alert alert-success">
                            ${success}
                    </div>
                </c:if>
                <form action="${pageContext.request.contextPath}/settings" method="post">

                    <div class="mb-3">
                        <label class="form-label">Mot de passe actuel</label>
                        <input type="password" name="currentPassword" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Nouveau mot de passe</label>
                        <input type="password" id="newPassword" name="newPassword" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Confirmer le nouveau mot de passe</label>
                        <input type="password" id="confirmPassword" class="form-control" required>
                        <div class="invalid-feedback">
                            Les mots de passe ne correspondent pas.
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        Mettre à jour le mot de passe
                    </button>

                </form>
            </div>

        </div>
    </div>

</div>

<script>
    document.getElementById("passwordForm").addEventListener("submit", function (event) {

        let newPassword = document.getElementById("newPassword");
        let confirmPassword = document.getElementById("confirmPassword");

        if (newPassword.value !== confirmPassword.value) {
            confirmPassword.classList.add("is-invalid");
            event.preventDefault();
        } else {
            confirmPassword.classList.remove("is-invalid");
        }
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
