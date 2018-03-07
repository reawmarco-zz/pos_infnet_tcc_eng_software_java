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

import br.edu.infnet.model.Course;
import br.edu.infnet.repository.CourseRepository;

@Controller
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseRepository courseRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView findCourse() {
		ModelAndView modelAndView = new ModelAndView("courses");
		modelAndView.addObject("courses", courseRepository.findAll());
		modelAndView.addObject(new Course());
		return modelAndView;
	}
	
	@RequestMapping("/create")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView("courseMaintenance");
		mv.addObject(new Course());
		return mv;
	}

	@RequestMapping(value = "/update/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Course course = courseRepository.findOne(codigo);
		ModelAndView mv = new ModelAndView("courseMaintenance");
		mv.addObject(course);
		return mv;
	}

	@RequestMapping(value = "/delete/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		Course course = courseRepository.findOne(codigo);
		courseRepository.delete(course);
		attributes.addFlashAttribute("mensagem", "Curso excluído com sucesso!");
		return "redirect:/courses";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@Validated Course course, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return "courseMaintenance";
		}
		try {
			courseRepository.save(course);
			attributes.addFlashAttribute("mensagem", "Curso salvo com sucesso!");
			return "redirect:/courses";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("Erro", null, e.getMessage());
			return "courseMaintenance";
		}
	}

	@RequestMapping(value = "/block/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String findModule(@PathVariable Long id, Model model) {
		model.addAttribute("block", courseRepository.findOne(id));
		return "block";
	}
}