package com.sustentamais.sustentamais.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sustentamais.sustentamais.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
	public List<UsuarioModel> findAllByNomeContainingIgnoreCase(@Param("nome")String nome);
	
	public List<UsuarioModel> findAllByLocalidadeContainingIgnoreCase(@Param("localidade")String localidade);
	
	public Optional<UsuarioModel>findByUsuario(@Param("usuario")String usuario);
}
