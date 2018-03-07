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
@Table(name="module_students")
public class ModuleStudents {
	
	@Id
	@Column(name = "oid_module_students")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long oid;
	
	@Column(name = "oid_module")
	private Long oidModule;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "oid_student", referencedColumnName = "id", nullable = false)
	private Student student;
	
	public ModuleStudents() {
		
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public Long getOidModule() {
		return oidModule;
	}

	public void setOidModule(Long oidModule) {
		this.oidModule = oidModule;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
}
