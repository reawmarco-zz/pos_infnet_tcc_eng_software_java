package br.edu.infnet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.edu.infnet.util.MailHelper;

@Controller
public class MailController {
	
	@Autowired
    @Qualifier("javasampleapproachMailSender")
	public MailHelper mailHelper;
	
	@RequestMapping(value = "/sendMail", method = RequestMethod.GET)
	public @ResponseBody ModelAndView sendEmail() {
		String from = "freitasmoura@gmail.com";
		String to = "nandows@gmail.com";
		String subject = "Primeiro E-mail Aplicação!";
		String body = "Infnet Porto Alegre!";
		
		mailHelper.sendMail(from, to, subject, body);
			
		ModelAndView mv = new ModelAndView("home");
		mv.addObject("mensagem", "Email Enviado Com Sucesso");
			
		return mv; 
	}		
}
