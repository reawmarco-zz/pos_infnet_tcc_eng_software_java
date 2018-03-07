package br.edu.infnet.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.infnet.exception.AdvancedEncryptionStandardException;
import br.edu.infnet.model.Answer;
import br.edu.infnet.model.Evaluation;
import br.edu.infnet.model.EvaluationQuestions;
import br.edu.infnet.model.Question;
import br.edu.infnet.model.Student;
import br.edu.infnet.repository.AnswerRepository;
import br.edu.infnet.repository.EvaluationQuestionsRepository;
import br.edu.infnet.repository.EvaluationRepository;
import br.edu.infnet.repository.StudentRepository;

@Component
public class ScheduledTasks {
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private EvaluationQuestionsRepository evaluationQuestionsRepository;
	
	@Autowired
	private ServletContext servletContext;
	
	
	@Autowired
    @Qualifier("javasampleapproachMailSender")
	public MailHelper mailHelper;
	
	private List<Likert> listLikert = Arrays.asList(Likert.values());

    @Scheduled(cron = "0 * * * * ?")
    public void searchEvaluation() throws AdvancedEncryptionStandardException {
    	if(VariablesUtil.LOAD_SCHEDULE) {
	        List<Evaluation> evaluations = evaluationRepository.findPendingEvaluation();
	        for(Evaluation evaluation : evaluations) {
	        	List<Student> students = studentRepository.findStudentPerModule(evaluation.getOid());
	        	for(Student student : students) {
	        		StringBuilder sbBody = new StringBuilder();
	        		String link = VariablesUtil.LINK + buildEncriptedKey(evaluation.getOid(), student.getId());
	        		sbBody.append(evaluation.getInviteText()).append("\n\n").append(link);
	        		System.out.println(student.getEmail() + " - " + link);
	        		mailHelper.sendMail(VariablesUtil.EMAIL, student.getEmail(), VariablesUtil.EMAIL_TITLE, sbBody.toString());
	        	}
	        	evaluation.setStatus(StatusEnum.INITIALIZED.getDesc());
	        	evaluationRepository.save(evaluation);
	        }
    	}
    }
    
    @Scheduled(cron = "0 * * * * ?")
    public void finishEvaluation() {
    	if(VariablesUtil.LOAD_SCHEDULE) {
    		List<Evaluation> evaluations = evaluationRepository.findEndingEvaluation();
	        for(Evaluation evaluation : evaluations) {
	        	Map<Question, Map<String, BigDecimal>> count = startCountMap(evaluation.getOid());
	        	List<Answer> answers = answerRepository.findAllEvaluationAnswered(evaluation.getOid());
	        	StringBuilder sbObs = startObservacao();
	        	for(Answer answer : answers) {
	        		Question question = answer.getEvaluationQuestion().getQuestion();
	        		if(question.getQuestionType().equals("LIKERT")) {
		        		BigDecimal add = count.get(question).get(answer.getAnswer()).add(BigDecimal.ONE);
		        		count.get(question).put(answer.getAnswer(), add);		        		
		        		count.put(question, new TreeMap<String, BigDecimal>(count.get(question)));
		        		
	        		}else{
	        			sbObs.append(answer.getAnswer()).append("\n");
	        		}
	        	}
	        	if(VariablesUtil.GENERATE_CSV_RESUME) {
		        	StringBuilder sbTotais = buildTotais(startTotais(), count);
		        	sbTotais.append(sbObs);
	        		ExternalFileUtil.createCSVFile(sbTotais, VariablesUtil.FILE_NAME + evaluation.getOid());
	        	}
	        	evaluation.setStatus(StatusEnum.FINISHED.getDesc());
	        	evaluationRepository.save(evaluation);
	        }
    	}
    }
    
    private String buildEncriptedKey(Long oidEvaluation, Long oidStudent) throws AdvancedEncryptionStandardException {
    	StringBuilder sbData= new StringBuilder();
    	sbData.append(oidEvaluation).append(VariablesUtil.SEPARATOR).append(oidStudent);
    	String encriptedKey = AdvancedEncryptionStandard.encrypt(sbData.toString(), VariablesUtil.KEY);
    	return encriptedKey;
    }
    
    private Map<Question, Map<String, BigDecimal>> startCountMap(Long oidEvaluation){ 
    	Map<Question, Map<String, BigDecimal>> count = new HashMap<Question, Map<String, BigDecimal>>();
    	Map<String, BigDecimal> subMap = new TreeMap<String, BigDecimal>();
    	for(Likert likert : listLikert){
    		subMap.put(likert.getCode(), BigDecimal.ZERO);
    	}
    	List<EvaluationQuestions> eqs = evaluationQuestionsRepository.findEvaluationQuestions(oidEvaluation);
    	for(EvaluationQuestions eq : eqs) {
    		if(eq.getQuestion().getQuestionType().equals("LIKERT")) {
	    		Map<String, BigDecimal> subMapEq = new HashMap<String, BigDecimal>(subMap);
	    		count.put(eq.getQuestion(), subMapEq);
    		}
    	}
    	return count;
    }
    
    private StringBuilder startObservacao(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("Observações").append("\n\n");
    	return sb;
    }
    
    private StringBuilder startTotais(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("Totais").append("\n\n")
    		.append("Questão/Likert").append(VariablesUtil.SEPARATOR);
    	Collections.sort(listLikert, new Comparator<Likert>() {
            @Override
            public int compare(Likert o1, Likert o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    	for(Likert likert : listLikert){
    		sb.append(likert.getDescription()).append(VariablesUtil.SEPARATOR);
    	}   	
    	return sb.append("\n");
    }
    
    private StringBuilder buildTotais(StringBuilder sb, Map<Question, Map<String, BigDecimal>> count) {
    	for(Question question : count.keySet()) {
    		sb.append(question.getQuestionText()).append(VariablesUtil.SEPARATOR);
    		for(BigDecimal qts : count.get(question).values()) {
    			sb.append(qts.toString()).append(VariablesUtil.SEPARATOR);
    		}
    		sb.append("\n");
    	}
    	return sb.append("\n");
    }
}