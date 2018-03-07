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
@Table(name="evaluation_questions")
public class EvaluationQuestions {
	
	@Id
	@Column(name = "oid_evaluation_questions")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long oid;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "oid_question", referencedColumnName = "oid_question", nullable = false)
	private Question question;
	
	@Column(name = "oid_evaluation")
	private Long oidEvaluation;
	
	public EvaluationQuestions() {
		
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Long getOidEvaluation() {
		return oidEvaluation;
	}

	public void setOidEvaluation(Long oidEvaluation) {
		this.oidEvaluation = oidEvaluation;
	}
	
}
