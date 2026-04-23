@import hexlet.code.util.NamedRoutes
@import java.time.format.DateTimeFormatter
@import hexlet.code.dto.root.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h1>Сайты</h1>

        <table data-test="urls" class="table table-bordered table-hover mt-3">
            <thead>
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @for(var url : page.getUrls())
                !{var latestCheck = page.getLatestChecksByUrlId().get(url.getId());}
                <tr>
                    <td>${url.getId()}</td>
                    <td>
                        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                    </td>

                    @if (latestCheck != null)
                        <td>${latestCheck.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</td>
                        <td>${latestCheck.getStatusCode()}</td>
                    @else
                        <td></td>
                        <td></td>
                    @endif
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
