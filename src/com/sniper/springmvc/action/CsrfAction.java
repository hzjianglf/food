package com.sniper.springmvc.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/csrf")
public class CsrfAction {

	@RequestMapping("/")
	public void csrf(HttpServletRequest request, PrintWriter writer) {
		HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
		CsrfToken csrfToken = csrfTokenRepository.generateToken(request);

		writer.write(csrfToken.getParameterName() + "=" + csrfToken.getToken());
	}
}
