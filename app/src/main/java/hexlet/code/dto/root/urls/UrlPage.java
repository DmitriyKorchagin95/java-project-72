package hexlet.code.dto.root.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlPage extends BasePage {
    private Url url;
}