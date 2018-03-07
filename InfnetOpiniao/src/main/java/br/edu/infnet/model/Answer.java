package br.edu.infnet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="answer")
public class Answer {
	
	@Id
	@Column(name = "oid_answer")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long oid;
	
//	@Column(name = "oid_evaluation_questions")
//	private Long oidEvaluationQuestions;
//	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "oid_evaluation_questions", referencedColumnName = "oid_evaluation_questions", nullable = false)
	private EvaluationQuestions evaluationQuestion;
	
//	@Column(name = "oid_student")
//	private Long oidStudent;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "oid_student", referencedColumnName = "id", nullable = false)
	private Student student;

	@Column(name = "answer")
	private String answer;

	public Answer() {
		super();
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

//	public Long getOidEvaluationQuestions() {
//		return oidEvaluationQuestions;
//	}
//
//	public void setOidEvaluationQuestions(Long oidEvaluationQuestions) {
//		this.oidEvaluationQuestions = oidEvaluationQuestions;
//	}

//	public Long getOidStudent() {
//		return oidStudent;
//	}
//
//	public void setOidStudent(Long oidStudent) {
//		this.oidStudent = oidStudent;
//	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public EvaluationQuestions getEvaluationQuestion() {
		return evaluationQuestion;
	}

	public void setEvaluationQuestion(EvaluationQuestions evaluationQuestion) {
		this.evaluationQuestion = evaluationQuestion;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
}
