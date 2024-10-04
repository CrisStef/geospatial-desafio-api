package com.geospatial.desafio.domain.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.geospatial.desafio.domain.model.Person;

@Repository
public interface PersonRepository extends CustomJpaRepository<Person, Long> {
	List<Person> findAll(Sort sort);

	@Query("SELECT COALESCE(MAX(p.id), 0) FROM Person p")
	Long findMaxId();

	boolean existsById(Long id);
}