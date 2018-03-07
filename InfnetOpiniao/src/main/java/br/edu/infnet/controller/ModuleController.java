package br.edu.infnet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.edu.infnet.model.Module;
import br.edu.infnet.model.ModuleStudents;
import br.edu.infnet.model.Student;
import br.edu.infnet.repository.BlockRepository;
import br.edu.infnet.repository.ModuleRepository;
import br.edu.infnet.repository.ModuleStudentsRepository;
import br.edu.infnet.repository.StudentRepository;

@Controller
@RequestMapping("/modules")
public class ModuleController {
	
	private Map<Long, Student> selectedStudentsMap = new HashMap<Long, Student>();
	private Map<Long, Student> allStudentsMap = new HashMap<Long, Student>();
	private Module selectedModule = new Module();

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private BlockRepository blockRepository;

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ModuleStudentsRepository moduleStudentsRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView findModules() {
		ModelAndView modelAndView = new ModelAndView("modules");
		modelAndView.addObject("modules", moduleRepository.findAll());
		modelAndView.addObject(new Module());
		return modelAndView;
	}

	@RequestMapping("/create")
	public ModelAndView inserir() {
		this.selectedModule = new Module();
		ModelAndView mv = startModuleMaintenance();
		List<Student> allStudents = studentRepository.findAll();
		this.allStudentsMap = buildMapStudents(allStudents);
		mv.addObject("allStudents", this.allStudentsMap.values());
		return mv;
	}

	@RequestMapping(value = "/update/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		this.selectedStudentsMap = buildSelectedStudentsMap(codigo);
		Module module = moduleRepository.findOne(codigo);
		this.selectedModule = module;
		ModelAndView mv = startModuleMaintenance();
		List<Student> allStudents = studentRepository.findAll();
		this.allStudentsMap = buildMapStudents(allStudents);
		mv.addObject("allStudents", this.allStudentsMap.values());		
		return mv;
	}

	@RequestMapping(value = "/delete/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		Module module = moduleRepository.findOne(codigo);
		moduleRepository.delete(module);
		attributes.addFlashAttribute("mensagem", "Módulo excluido com sucesso!");
		return "redirect:/modules";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView save(@Validated Module module, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return new ModelAndView("moduleMaintenance");
		}
		try {
			Module newModule = moduleRepository.save(module);
			attributes.addFlashAttribute("mensagem", "Módulo salvo com sucesso!");
			return editar(newModule.getOid());
		} catch (IllegalArgumentException e) {
			errors.rejectValue("Erro", null, e.getMessage());
			return new ModelAndView("moduleMaintenance");
		}
	}

	@RequestMapping(value = "/module/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String findModule(@PathVariable Long id, Model model) {
		model.addAttribute("module", moduleRepository.findOne(id));
		return "module";
	}
	
	@RequestMapping(value = "/student/adicionar/{id}" , method = RequestMethod.POST)
	public ModelAndView adicionarStudent(@PathVariable Long id){
		if(!id.equals(0L)){
			Student student = studentRepository.findOne(id);
			this.selectedStudentsMap.put(student.getId(), student);
			this.allStudentsMap.remove(student.getId());
			
			ModuleStudents moduleStudent = new ModuleStudents();
			moduleStudent.setOidModule(this.selectedModule.getOid());
			moduleStudent.setStudent(student);
			moduleStudentsRepository.save(moduleStudent);
		}
		ModelAndView mv = startModuleMaintenance();
		mv.addObject("allStudents", this.allStudentsMap.values());	
		return mv;		
	}
	
	@RequestMapping(value = "/student/remover/{id}" , method = RequestMethod.GET)
	public ModelAndView removerStudent(@PathVariable Long id){
		Student student = studentRepository.findOne(id);
		this.selectedStudentsMap.remove(student.getId());
		
		ModuleStudents moduleStudent = moduleStudentsRepository.findModuleStudent(id, this.selectedModule.getOid());
		moduleStudentsRepository.delete(moduleStudent);
		
		ModelAndView mv = startModuleMaintenance();
		this.allStudentsMap.put(student.getId(), student);
		mv.addObject("allStudents", this.allStudentsMap.values());	
		return mv;		
	}
	
	private Map<Long, Student> buildMapStudents(List<Student> allStudents) {
		Map<Long, Student> auxAllStudentsMap = new HashMap<Long, Student>();
		for(Student student : allStudents) {
			if(this.selectedStudentsMap.get(student.getId()) == null){
				auxAllStudentsMap.put(student.getId(), student);
			}
		}
		return auxAllStudentsMap;
	}
	
	private Map<Long, Student> buildSelectedStudentsMap(Long oidModule){
		List<ModuleStudents> listModuleStudents = moduleStudentsRepository.findModuleStudentPerModule(oidModule);
		Map<Long, Student> auxAllStudentsMap = new HashMap<Long, Student>();
		for(ModuleStudents mStudent : listModuleStudents) {
			auxAllStudentsMap.put(mStudent.getStudent().getId(), mStudent.getStudent());
		}
		return auxAllStudentsMap;
	}
	
	private ModelAndView startModuleMaintenance() {
		ModelAndView mv = new ModelAndView("moduleMaintenance");
		List<Block> allBlocks = blockRepository.findAll();
		mv.addObject("allBlocks", allBlocks);
		mv.addObject("selectedStudents", this.selectedStudentsMap.values());
		mv.addObject("student", new Student());
		mv.addObject("module", this.selectedModule);
		return mv;
	}
}