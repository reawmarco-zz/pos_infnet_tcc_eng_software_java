package br.edu.infnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.model.Course;


public interface CourseRepository extends JpaRepository<Course, Long>{

}
