@import gg.jte.Content
@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param Content content
@param BasePage page = null

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Анализатор страниц</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="d-flex flex-column min-vh-100">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3">
        <div class="container-fluid">
            <a class="navbar-brand" href="${NamedRoutes.mainPath()}">Анализатор страниц</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${NamedRoutes.mainPath()}">Главная</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${NamedRoutes.urlsPath()}">Сайты</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="flex-grow-1">
        @if(page != null && page.getFlash() != null)
            <div class="alert alert-${page.getFlashType()} alert-dismissible fade show" role="alert">
                ${page.getFlash()}
            </div>
        @endif

        ${content}
    </div>
</body>

<footer class="footer border-top py-3 mt-5 bg-light">

    <div class="container-xl">
        <div class="text-center">
            created by
            <a href="https://ru.hexlet.io" target="_blank">Hexlet</a>
        </div>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"></script>
</html>