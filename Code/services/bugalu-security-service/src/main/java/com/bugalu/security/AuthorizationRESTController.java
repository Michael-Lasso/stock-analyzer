package com.bugalu.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthorizationRESTController {
	@GetMapping("/token")
	public String getToken(@AuthenticationPrincipal TokenUserDetails principal) {
		return principal.getToken();
	}
}
