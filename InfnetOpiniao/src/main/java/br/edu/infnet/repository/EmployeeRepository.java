package br.edu.infnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.infnet.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	@Query("SELECT em FROM Employee em WHERE em.username = :username")
	public Employee findEmployeeByUsername(@Param("username") String username);
}
