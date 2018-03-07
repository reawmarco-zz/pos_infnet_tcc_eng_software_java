package br.edu.infnet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.infnet.model.Answer;
import br.edu.infnet.model.Evaluation;
import br.edu.infnet.model.EvaluationQuestions;
import br.edu.infnet.model.Student;
import br.edu.infnet.repository.AnswerRepository;
import br.edu.infnet.repository.EvaluationQuestionsRepository;
import br.edu.infnet.repository.EvaluationRepository;
import br.edu.infnet.repository.StudentRepository;

@Service
public class AnswerService {
	
	@Autowired
	private EvaluationQuestionsRepository evaluationQuestionsRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	public List<EvaluationQuestions> findEvaluationQuestions(Long oid){
		List<EvaluationQuestions> result = evaluationQuestionsRepository.findEvaluationQuestions(oid);		
		return result;
	}
	
	public void saveAnswers(List<Answer> answers) {
		answerRepository.save(answers);
	}
	
	public List<Answer> findEvaluationAnswered(Long oidStudent, Long oidEvaluation) {
		List<Answer> result = answerRepository.findEvaluationAnswered(oidEvaluation, oidStudent);
		return result;
	}
	
	public Evaluation findEvaluation(Long oid) {
		Evaluation result = evaluationRepository.findOne(oid);
		return result;
	}
	
	public Student findStudent(Long oid) {
		Student student = studentRepository.findOne(oid);
		return student;
	}

}
