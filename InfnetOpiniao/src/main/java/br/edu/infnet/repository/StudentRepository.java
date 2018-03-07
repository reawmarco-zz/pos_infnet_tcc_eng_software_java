package br.edu.infnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.infnet.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("select s from Student s, ModuleStudents ms, Evaluation e where ms.student.id = s.id and e.oidModule = ms.oidModule and e.oid = :oidEvaluation")
	public List<Student> findStudentPerModule(@Param("oidEvaluation") Long oid);

}
