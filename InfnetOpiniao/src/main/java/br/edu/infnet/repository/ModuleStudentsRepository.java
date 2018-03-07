package br.edu.infnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.infnet.model.ModuleStudents;

public interface ModuleStudentsRepository extends JpaRepository<ModuleStudents, Long>{
	
	@Query("select ms from ModuleStudents ms where ms.student.id = :oidStudent and ms.oidModule = :oidModule")
	public ModuleStudents findModuleStudent(@Param("oidStudent") Long oidStudent, @Param("oidModule") Long oidModule);

	@Query("select ms from ModuleStudents ms where ms.oidModule = :oidModule")
	public List<ModuleStudents> findModuleStudentPerModule(@Param("oidModule") Long oidModule);
}
