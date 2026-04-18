package hexlet.code.dto.root.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Check;
import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlPage extends BasePage {
    private Url url;
    private List<Check> checks;
}
