package br.edu.infnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	

}
