package br.edu.infnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.infnet.model.EvaluationQuestions;

public interface EvaluationQuestionsRepository extends JpaRepository<EvaluationQuestions, Long>{

	@Query("SELECT eq FROM EvaluationQuestions eq WHERE eq.oidEvaluation = :oidEvaluation")
	public List<EvaluationQuestions> findEvaluationQuestions(@Param("oidEvaluation") Long oid);
	
	@Query("SELECT eq FROM EvaluationQuestions eq WHERE eq.oidEvaluation = :oidEvaluation and eq.question.oid = :oidQuestion ")
	public List<EvaluationQuestions> findEvaluationQuestionsFull(@Param("oidEvaluation") Long oid, @Param("oidQuestion") Long oidq);
	
}
