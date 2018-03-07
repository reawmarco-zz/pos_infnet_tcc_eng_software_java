package br.edu.infnet.controller;

import java.util.List;

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

import br.edu.infnet.model.Block;
import br.edu.infnet.model.Course;
import br.edu.infnet.repository.BlockRepository;
import br.edu.infnet.repository.CourseRepository;

@Controller
@RequestMapping("/blocks")
public class BlockController {

	@Autowired
	private BlockRepository blockRepository;

	@Autowired
	private CourseRepository courseRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView findBlock() {
		ModelAndView modelAndView = new ModelAndView("blocks");
		modelAndView.addObject("blocks", blockRepository.findAll());
		modelAndView.addObject(new Block());
		return modelAndView;
	}

	@RequestMapping("/create")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView("blockMaintenance");
		mv.addObject(new Block());
		List<Course> allCourses = courseRepository.findAll();
		mv.addObject("allCourses", allCourses);
		return mv;
	}

	@RequestMapping(value = "/update/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Block block = blockRepository.findOne(codigo);
		ModelAndView mv = new ModelAndView("blockMaintenance");
		mv.addObject(block);
		List<Course> allCourses = courseRepository.findAll();
		mv.addObject("allCourses", allCourses);
		return mv;
	}

	@RequestMapping(value = "/delete/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		Block block = blockRepository.findOne(codigo);
		blockRepository.delete(block);
		attributes.addFlashAttribute("mensagem", "Bloco excluído com sucesso!");
		return "redirect:/blocks";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@Validated Block block, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return "blockMaintenance";
		}
		try {
			blockRepository.save(block);
			attributes.addFlashAttribute("mensagem", "Bloco salvo com sucesso!");
			return "redirect:/blocks";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("Erro", null, e.getMessage());
			return "blockMaintenance";
		}
	}

	@RequestMapping(value = "/module/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String findModule(@PathVariable Long id, Model model) {
		model.addAttribute("module", blockRepository.findOne(id));
		return "block";
	}
}