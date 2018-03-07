package br.edu.infnet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.infnet.model.Question;
import br.edu.infnet.repository.QuestionRepository;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView findQuestions() {
		ModelAndView modelAndView = new ModelAndView("questions");
		modelAndView.addObject("questions", questionRepository.findAll());
		modelAndView.addObject(new Question());
		return modelAndView;
	}
	
	@RequestMapping("/create")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView("questionMaintenance");
		mv.addObject(new Question());	
		return mv;
	}
	
	@RequestMapping(value="/update/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo ) {
		Question question = questionRepository.findOne(codigo);
		ModelAndView mv = new ModelAndView("questionMaintenance");
		mv.addObject(question);	
		return mv;
	}
	
	@RequestMapping(value="/delete/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		Question question = questionRepository.findOne(codigo);
		questionRepository.delete(question);
		attributes.addFlashAttribute("mensagem", "question excluido com sucesso!");		
		return "redirect:/questions";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String save(@Validated Question question, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return "questionMaintenance";
		}	
		try {
			questionRepository.save(question);
			attributes.addFlashAttribute("mensagem", "Questão salva com sucesso!");
			return "redirect:/questions";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("Erro", null, e.getMessage());
			return "questionMaintenance";
		}	
	}
	
	@RequestMapping(value = "/question/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String findQuestion(@PathVariable Long id, Model model) {
		model.addAttribute("question", questionRepository.findOne(id));
		return "question";
	}
}
