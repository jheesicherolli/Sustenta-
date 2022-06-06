package com.sustentamais.sustentamais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sustentamais.sustentamais.model.TemaModel;


@Repository
public interface TemaRepository extends JpaRepository<TemaModel, Long> {
	public List<TemaModel> findAllByTemaContainingIgnoreCase(String tema);
	public List<TemaModel> findAllByCategoriaContainingIgnoreCase(String categoria);
	
}

