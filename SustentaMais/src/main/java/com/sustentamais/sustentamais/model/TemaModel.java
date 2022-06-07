package com.sustentamais.sustentamais.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tb_tema")
public class TemaModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String tema;
	
	@NotNull
	private String categoria;
	
	@OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("tema")
	private List<PostagemModel> postagem;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public List<PostagemModel> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<PostagemModel> postagem) {
		this.postagem = postagem;
	}
	
}
