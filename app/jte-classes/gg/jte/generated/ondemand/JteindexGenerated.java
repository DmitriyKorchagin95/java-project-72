package gg.jte.generated.ondemand;
import hexlet.code.dto.BasePage;
import hexlet.code.util.NamedRoutes;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,4,4,7,7,12,12,12,12,12,12,12,12,12,29,29,29,29,29,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <div class=\"row\">\n        <div class=\"col-12 col-md-10 col-lg-8 mx-auto border rounded-3 bg-light p-5\">\n            <h1 class=\"display-3\">Анализатор страниц</h1>\n            <p class=\"lead\">Бесплатно проверяйте сайты на SEO пригодность</p>\n            <form");
				var __jte_html_attribute_0 = NamedRoutes.urlsPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" method=\"post\" class=\"row\">\n                <div class=\"col-8\">\n                    <label for=\"url-name\" class=\"visually-hidden\">Url для проверки</label>\n                    <input\n                            id=\"url-name\"\n                            type=\"text\"\n                            name=\"url\"\n                            class=\"form-control form-control-lg\"\n                            placeholder=\"https://www.example.com\"\n                    >\n                </div>\n                <div class=\"col-2\">\n                    <input type=\"submit\" class=\"btn btn-primary btn-lg ms-3 px-5 text-uppercase mx-3\" value=\"Проверить\">\n                </div>\n            </form>\n        </div>\n    </div>\n");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.getOrDefault("page", null);
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
