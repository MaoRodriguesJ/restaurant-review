package dev.restaurantreview.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	private static final String INDEX_HTML = "/index.html";

	@GetMapping(value = "/")
	public String index() {
		return INDEX_HTML;
	}

	@GetMapping(value = "/{dummy:(?!api|static|vendor|images|assets)[a-zA-Z0-9-]+$}/**")
	public String login() {
		return INDEX_HTML;
	}

}
