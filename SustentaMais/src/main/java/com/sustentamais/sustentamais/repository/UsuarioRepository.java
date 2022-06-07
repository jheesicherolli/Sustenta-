package com.sustentamais.sustentamais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sustentamais.sustentamais.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
	public List<UsuarioModel> findAllByNomeContainingIgnoreCase(String nome);
	
	public List<UsuarioModel> findAllByLocalidadeContainingIgnoreCase(String localidade);
}
