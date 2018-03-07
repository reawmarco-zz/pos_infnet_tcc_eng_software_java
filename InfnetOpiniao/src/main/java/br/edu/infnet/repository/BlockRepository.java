package br.edu.infnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.model.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {

}
