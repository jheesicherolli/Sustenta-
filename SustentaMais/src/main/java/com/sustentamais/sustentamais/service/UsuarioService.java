package com.sustentamais.sustentamais.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sustentamais.sustentamais.model.UserLogin;
import com.sustentamais.sustentamais.model.UsuarioModel;
import com.sustentamais.sustentamais.repository.UsuarioRepository;

@Service 
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);

	}
	
	public Optional<UsuarioModel> atualizarUsuario(UsuarioModel usuario) {
		
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {
			
		
			Optional<UsuarioModel> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			
	
			if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			usuario.setSenha(criptografarSenha(usuario.getSenha()));

		
		 
			return Optional.ofNullable(usuarioRepository.save(usuario));
			
		}
		return Optional.empty();
	}
	
	public Optional<UsuarioModel> CadastrarUsuario(UsuarioModel usuario) {
		
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();
	
		usuario.setSenha(criptografarSenha(usuario.getSenha()));

	
		return Optional.of(usuarioRepository.save(usuario));
		
	}
	
	public Optional<UserLogin> Logar(Optional<UserLogin> user){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<UsuarioModel> usuario = usuarioRepository.findByUsuario(user.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
				
				String auth = user.get().getUsuario() + ":" + user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				
				user.get().setToken(authHeader);
				user.get().setNome(usuario.get().getNome());
				user.get().setSenha(usuario.get().getSenha());
				
				return user;
						
			}
		}
		  return null;
	}

}
