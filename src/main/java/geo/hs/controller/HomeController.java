package geo.hs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String main(){

		return "WFS";
	}
}