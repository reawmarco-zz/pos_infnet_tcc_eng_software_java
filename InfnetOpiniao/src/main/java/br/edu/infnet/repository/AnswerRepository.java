package br.edu.infnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.infnet.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
	
	//@Query("SELECT an FROM Answer an, EvaluationQuestions eq WHERE an.oidEvaluationQuestions = eq.oid and eq.oidEvaluation = :oidEvaluation and an.oidStudent = :oidStudent")
	@Query("SELECT an FROM Answer an WHERE an.evaluationQuestion.oidEvaluation = :oidEvaluation and an.student.id = :oidStudent")
	public List<Answer> findEvaluationAnswered(@Param("oidEvaluation") Long oidEvaluation, @Param("oidStudent") Long oidStudent);
	
	@Query("SELECT an FROM Answer an WHERE an.evaluationQuestion.oidEvaluation = :oidEvaluation order by an.student.id")
	public List<Answer> findAllEvaluationAnswered(@Param("oidEvaluation") Long oidEvaluation);

}
