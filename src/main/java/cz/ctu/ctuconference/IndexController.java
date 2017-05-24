package cz.ctu.ctuconference;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String defaultAction() {
		return "index";
	}

	@RequestMapping(value = { "{path:(?!app|api).*$}", "{path:(?!app|api).*$}/**" }, method = RequestMethod.GET)
	public String subPageAction(@PathVariable String path) {
			return "index";
	}

}
