package br.edu.infnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.infnet.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long>{
	
	@Query("SELECT e FROM Evaluation e WHERE e.status = 'NE' and sysdate() between e.startDate and e.endDate")
	public List<Evaluation> findPendingEvaluation();
	
	@Query("SELECT e FROM Evaluation e WHERE e.status = 'IN' and sysdate() > e.endDate")
	public List<Evaluation> findEndingEvaluation();

}
