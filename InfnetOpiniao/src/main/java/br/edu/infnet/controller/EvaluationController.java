package br.edu.infnet.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.infnet.model.Evaluation;
import br.edu.infnet.model.EvaluationQuestions;
import br.edu.infnet.model.Module;
import br.edu.infnet.model.Question;
import br.edu.infnet.repository.EvaluationQuestionsRepository;
import br.edu.infnet.repository.EvaluationRepository;
import br.edu.infnet.repository.ModuleRepository;
import br.edu.infnet.repository.QuestionRepository;
import br.edu.infnet.util.StatusEnum;
import br.edu.infnet.util.VariablesUtil;

@Controller
@RequestMapping("/evaluations")
public class EvaluationController {
	
	private Evaluation selectedEvaluation = new Evaluation();
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private ModuleRepository moduleRepository;
	
	@Autowired
	private EvaluationQuestionsRepository evaluationQuestionRepository;


	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView findEvaluations() {
		ModelAndView modelAndView = new ModelAndView("evaluations");
		modelAndView.addObject("evaluations", evaluationRepository.findAll());
		modelAndView.addObject("statusMap", buildMapStatusEvaluation());
		modelAndView.addObject(new Evaluation());
		return modelAndView;
	}

	@RequestMapping("/create")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView("evaluationMaintenance");
		mv.addObject(new Evaluation());	
		List<Module> allModules = moduleRepository.findAll();
		mv.addObject("allModules", allModules);
		mv.addObject("status", "noQuestions");
		return mv;
	}
	
	@RequestMapping(value="/update/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo ) {
		Evaluation evaluation = evaluationRepository.findOne(codigo);
		this.selectedEvaluation = evaluation;
		ModelAndView mv = new ModelAndView("evaluationMaintenance");
		mv.addObject(evaluation);	
		List<Question> questions = questionRepository.findAll();
		List<EvaluationQuestions> equestions = evaluationQuestionRepository.findEvaluationQuestions(codigo);
		List<Question> rmQuestion = new ArrayList<Question>();
		List<Module> allModules = moduleRepository.findAll();
		
		for (EvaluationQuestions eq : equestions) {
			rmQuestion.add(eq.getQuestion());
		} 
		questions.removeAll(rmQuestion);
		
		mv.addObject("equestions",equestions);
		mv.addObject("questions", questions);	
		mv.addObject("status", "withQuestions");
		mv.addObject("allModules", allModules);
		return mv;
	}
	
	@RequestMapping(value="/delete/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		Evaluation evaluation = evaluationRepository.findOne(codigo);
		evaluationRepository.delete(evaluation);
		attributes.addFlashAttribute("mensagem", "avaliação excluido com sucesso!");		
		return "redirect:/evaluations";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView save(Evaluation evaluation) {
		saveEvaluation(evaluation);
		return editar(evaluation.getOid());
	}
	
	@RequestMapping(value = "/question/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String findQuestion(@PathVariable Long id, Model model) {
		model.addAttribute("evaluation", evaluationRepository.findOne(id));
		return "evaluation";
	}
	
	@RequestMapping(value = "/{codigo}/adicionar/{id}" , method = RequestMethod.GET)
	public String adicionarQuestao(@PathVariable Long codigo, @PathVariable Long id){
		System.out.println("cod: " + codigo + " id: " + id);
		Question question = questionRepository.findOne(id);
		EvaluationQuestions eq = new EvaluationQuestions();
		eq.setOidEvaluation(codigo);
		eq.setQuestion(question);
		evaluationQuestionRepository.save(eq);
		
		return "redirect:/evaluations/update/"+codigo;
	}
	
	@RequestMapping(value = "/{codigo}/remover/{id}" , method = RequestMethod.GET)
	public String removerQuestao(@PathVariable Long codigo, @PathVariable Long id){
		System.out.println("cod: " + codigo + " id: " + id);
		List<EvaluationQuestions> eqs = evaluationQuestionRepository.findEvaluationQuestionsFull(codigo, id);
		for (EvaluationQuestions evaluationQuestions : eqs) {
			evaluationQuestionRepository.delete(evaluationQuestions);
		}
		
		return "redirect:/evaluations/update/"+codigo;
	}
	
	@RequestMapping(value = "/endConstruction" , method = RequestMethod.POST)
	public String finalizarConstrucao(){
		this.selectedEvaluation.setStatus(StatusEnum.NEW.getDesc());
		saveEvaluation(this.selectedEvaluation);
		return "redirect:/evaluations";
	}
	
	@RequestMapping(value = "/file/{name:.+}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public FileSystemResource getFile(@PathVariable("name") String fileName) {
		FileSystemResource rsr = new FileSystemResource(VariablesUtil.PATH + fileName);
	    return rsr; 
	}
	
	private void saveEvaluation(Evaluation evaluation) {
		if(evaluation.getCreationDate() == null){
			evaluation.setCreationDate(new Date());	
		}
		if(evaluation.getStatus().trim().equals("")){
			evaluation.setStatus(null);
		}
		evaluationRepository.save(evaluation);
		this.selectedEvaluation = evaluation;
	}
	
	private Map<String, String> buildMapStatusEvaluation(){
		Map<String, String> map = new HashMap<String, String>();
		for(StatusEnum status : StatusEnum.values()) {
			map.put(status.getDesc(), status.getView());
		}
		return map;
	}
	
}
