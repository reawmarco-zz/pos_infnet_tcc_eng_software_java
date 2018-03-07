package br.edu.infnet.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.infnet.exception.AdvancedEncryptionStandardException;
import br.edu.infnet.model.Answer;
import br.edu.infnet.model.Evaluation;
import br.edu.infnet.model.EvaluationQuestions;
import br.edu.infnet.model.Student;
import br.edu.infnet.service.AnswerService;
import br.edu.infnet.util.AdvancedEncryptionStandard;
import br.edu.infnet.util.AnswerStatusEnum;
import br.edu.infnet.util.ExternalFileUtil;
import br.edu.infnet.util.Likert;
import br.edu.infnet.util.VariablesUtil;

@Controller
@RequestMapping("/answer")
public class AnswerController {
	
	private List<EvaluationQuestions> questions = new ArrayList<EvaluationQuestions>();
	private Long oidStudent = new Long(0);
	private Long oidEvaluation = new Long(0);
	private String hash = new String();
	
	@Autowired
	private AnswerService answerService;
	
	@RequestMapping(method = RequestMethod.GET, params = {"_hash"})
	public ModelAndView get(@RequestParam(value = "_hash") String hash) throws AdvancedEncryptionStandardException {
		ModelAndView mv = new ModelAndView("answer");
		String values = AdvancedEncryptionStandard.decrypt(hash, VariablesUtil.KEY);
		if(values.contains(";")) {
			String[] params = values.split(";");
			this.oidEvaluation = Long.valueOf(params[0]);
			this.oidStudent = Long.valueOf(params[1]);
			this.hash = hash;
			
			if(checkAlreadyAnswered(this.oidStudent, this.oidEvaluation)) {
				mv.addObject("status", AnswerStatusEnum.ANSWERED.getDesc());
				return mv;
			}
			
			if(checkExpiredEvaluation(this.oidEvaluation)) {
				mv.addObject("status", AnswerStatusEnum.EXPIRED.getDesc());
				return mv;
			}
			
			List<EvaluationQuestions> questions = this.answerService.findEvaluationQuestions(Long.valueOf(this.oidEvaluation));
			
			List<EvaluationQuestions> questionsExtras = questions.stream().filter(questionType -> "EXTRA".equals(questionType.getQuestion().getQuestionCategory())).collect(Collectors.toList());
			List<EvaluationQuestions> questionsProfessor = questions.stream().filter(questionType -> "PROFESSOR".equals(questionType.getQuestion().getQuestionCategory())).collect(Collectors.toList());
			List<EvaluationQuestions> questionsEstrutura = questions.stream().filter(questionType -> "ESTRUTURA".equals(questionType.getQuestion().getQuestionCategory())).collect(Collectors.toList());
			
			this.questions = new ArrayList<EvaluationQuestions>(questions);
			
			mv.addObject("status", AnswerStatusEnum.OK.getDesc());
			mv.addObject("questions", questions);	
			mv.addObject("questionsExtras", questionsExtras);	
			mv.addObject("questionsProfessor", questionsProfessor);	
			mv.addObject("questionsEstrutura", questionsEstrutura);	
			return mv;
		}
		mv.addObject("status", AnswerStatusEnum.ERROR.getDesc());
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView post(HttpServletRequest request, HttpServletResponse response) {
		List<Answer> answers = new ArrayList<Answer>();
		Student student = this.answerService.findStudent(this.oidStudent);
		StringBuilder csv = new StringBuilder(ExternalFileUtil.buildAnswerCSVLine("Questão", "Resposta"));
		ModelAndView mv = new ModelAndView("answer");
		for(EvaluationQuestions eq : this.questions) {
			String answer = request.getParameter(eq.getQuestion().getOid().toString());	
			boolean isLikert = eq.getQuestion().getQuestionType().equals("LIKERT");
			if(answer == null && isLikert) {
				mv.addObject("status", AnswerStatusEnum.ERROR.getDesc());
				return mv;
			}
			Answer entityAnswer = new Answer();
			entityAnswer.setAnswer(answer);
			//entityAnswer.setOidStudent(Long.valueOf(this.oidStudent));
			//entityAnswer.setOidEvaluationQuestions(eq.getOid());
			entityAnswer.setStudent(student);
			entityAnswer.setEvaluationQuestion(eq);
			csv.append(ExternalFileUtil.buildAnswerCSVLine(eq.getQuestion().getQuestionText(), isLikert ? Likert.getValue(answer).getDescription() : answer));
			answers.add(entityAnswer);
		}
		this.answerService.saveAnswers(answers);
		if(VariablesUtil.GENERATE_CSV_PER_STUDENT) {
			ExternalFileUtil.createCSVFile(csv, ExternalFileUtil.buildFileName(student.getName()));
		}
		mv.addObject("status", AnswerStatusEnum.SUCCESS.getDesc());
		return mv;
	}
	
	private boolean checkAlreadyAnswered(Long oidStudent, Long oidEvaluation) {
		List<Answer> result = this.answerService.findEvaluationAnswered(oidStudent, oidEvaluation);
		return result.size() == 0 ? false : true;
	}
	
	private boolean checkExpiredEvaluation(Long oidEvaluation) {
		Evaluation evaluation = this.answerService.findEvaluation(oidEvaluation);
		if(evaluation.getEndDate().compareTo(new Date()) < 0) {
			return true;
		}
		return false;
	}
}
