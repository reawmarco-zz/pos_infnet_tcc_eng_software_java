package br.edu.infnet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.infnet.model.Student;
import br.edu.infnet.repository.StudentRepository;

@Controller
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@RequestMapping("/create")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView("studentMaintenance");
		mv.addObject(new Student());
		return mv;
	}

	@RequestMapping(value = "/update/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Student student = studentRepository.findOne(codigo);
		ModelAndView mv = new ModelAndView("studentMaintenance");
		mv.addObject(student);
		return mv;
	}

	@RequestMapping(value = "/delete/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		Student student = studentRepository.findOne(codigo);
		studentRepository.delete(student);
		attributes.addFlashAttribute("mensagem", "Estudante excluído com sucesso!");
		return "redirect:/students";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(Student student) {
		studentRepository.save(student);
		return "redirect:/students";
	}

	@RequestMapping(value = "/student/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String findStudent(@PathVariable Long id, Model model) {
		model.addAttribute("student", studentRepository.findOne(id));
		return "student";
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView findStudents() {
		ModelAndView modelAndView = new ModelAndView("students");
		modelAndView.addObject("students", studentRepository.findAll());
		modelAndView.addObject(new Student());
		return modelAndView;
	}
}