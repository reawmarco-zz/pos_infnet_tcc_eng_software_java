package br.edu.infnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Long> {

}
