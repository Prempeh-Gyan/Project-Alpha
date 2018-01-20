package org.isag_ghana.alpha.controller.publik;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class LoginController {
	
	@RequestMapping("/login")
	public String login() {
		log.info("Login page requested, rendering...");
		return "login";
	}
	
	@RequestMapping("/badUser")
	public String badUser() {
		return "badUser";
	}
	
	@RequestMapping("/page_403")
	public String error403() {
		return "page_403";
	}
	
	 @RequestMapping(value="/page_404")
	 public String error404(){
	  return "page_404";
	 }
	  
	 @RequestMapping(value="/page_500")
	 public String error500(){
	  return "page_500";
	 }
}
