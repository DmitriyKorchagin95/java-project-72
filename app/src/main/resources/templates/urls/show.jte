@import hexlet.code.util.NamedRoutes
@import hexlet.code.util.StringUtils
@import java.text.SimpleDateFormat
@import hexlet.code.dto.root.urls.UrlPage
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h1>Сайт: https://www.example.com</h1>

        <table data-test="url" class="table table-bordered table-hover mt-3">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${new SimpleDateFormat("dd/MM/yyyy HH:mm").format(page.getUrl().getCreatedAt())}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="mt-5">Проверки</h2>
        <form method="post" action="${NamedRoutes.checksPath(page.getUrl().getId())}">
            <input type="submit" class="btn btn-primary" value="Запустить проверку">
        </form>

        <table data-test="checks" class="table table-bordered table-hover mt-3">
            <thead>
            <th>ID</th>
            <th>Код ответа</th>
            <th>title</th>
            <th>h1</th>
            <th>description</th>
            <th>Дата проверки</th>
            </thead>
            <tbody>
            @for(var check : page.getChecks())
                <tr>
                    <td>${check.getId()}</td>
                    <td>${check.getStatusCode()}</td>
                    <td>${StringUtils.truncate(check.getTitle(), 200)}</td>
                    <td>${StringUtils.truncate(check.getH1(), 200)}</td>
                    <td>${StringUtils.truncate(check.getDescription(), 200)}</td>
                    <td>${new SimpleDateFormat("dd/MM/yyyy HH:mm").format(check.getCreatedAt())}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
