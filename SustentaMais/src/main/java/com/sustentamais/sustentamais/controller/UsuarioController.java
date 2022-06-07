package com.sustentamais.sustentamais.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sustentamais.sustentamais.model.UsuarioModel;
import com.sustentamais.sustentamais.repository.UsuarioRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;
	
	@GetMapping
	public ResponseEntity<List<UsuarioModel>> getAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioModel> buscarPorId(@PathVariable long id){
		return repository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<UsuarioModel>> buscarPorNome(@PathVariable String nome){
		return ResponseEntity.ok(repository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@GetMapping("/localidade/{localidade}")
	public ResponseEntity<List<UsuarioModel>> buscarPorlocalidade(@PathVariable String localidade){
		return ResponseEntity.ok(repository.findAllByLocalidadeContainingIgnoreCase(localidade));
	}
	
	@PostMapping
	public ResponseEntity<UsuarioModel> post (@Valid @RequestBody UsuarioModel usuario){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(repository.save(usuario));
	}
	
	@PutMapping
	public ResponseEntity<UsuarioModel> put (@Valid @RequestBody UsuarioModel usuario){
		return ResponseEntity.ok(repository.save(usuario));
	}
	
	@DeleteMapping("/{id}")
	public void delete (@PathVariable long id) {
		repository.deleteById(id);
	}
}
