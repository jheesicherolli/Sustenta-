package com.sustentamais.sustentamais.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sustentamais.sustentamais.model.UserLogin;
import com.sustentamais.sustentamais.model.UsuarioModel;
import com.sustentamais.sustentamais.repository.UsuarioRepository;
import com.sustentamais.sustentamais.service.UsuarioService;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	/**
	 * Injeta um objeto da Classe TestRestTemplate, responsável por fazer requisições HTTP (semelhante ao Postman)
	 */
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		/**
		 * Apaga todos os registros do banco de dados antes de iniciar os testes
		 */
		
		usuarioRepository.deleteAll();
	}
	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {
		/**
		 * Cria um objeto da Classe Usuario e insere dentro de um Objeto da Classe HttpEntity (Entidade HTTP)
		 */
		HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(new UsuarioModel(0L,"Paulo Antunes","paulo_antunes@email.com.br", "123456789", "https://i.imgur.com/JR7kUFU.jpg", "Paracatu-MG"));
		/**
		 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
		 * enviada pelo Objeto da Classe TestRestTemplate.
		 * 
		 * Na requisição HTTP será enviada a URL do recurso (/usuarios/cadastrar), o verbo (POST), a entidade
		 * HTTP criada acima (corpoRequisicao) e a Classe de retornos da Resposta (Usuario).
		 */
		ResponseEntity<UsuarioModel> corpoResposta = testRestTemplate
				.exchange("/usuario/cadastrar", HttpMethod.POST, corpoRequisicao, UsuarioModel.class);
		/**
		 * Verifica se a requisição retornou o Status Code CREATED (201) 
		 * Se for verdadeira, o teste passa, se não, o teste falha.
		 */
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		
		/**
		 * Verifica se o Atributo Nome do Objeto da Classe Usuario retornado no Corpo da Requisição 
		 * é igual ao Atributo Nome do Objeto da Classe Usuario Retornado no Corpo da Resposta
		 * Se for verdadeiro, o teste passa, senão o teste falha.
		 */
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		/**
		 * Verifica se o Atributo Usuario do Objeto da Classe Usuario retornado no Corpo da Requisição 
		 * é igual ao Atributo Usuario do Objeto da Classe Usuario Retornado no Corpo da Resposta
		 * Se for verdadeiro, o teste passa, senão o teste falha.
		 */
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		/**
		 * Verifica se o Atributo Email do Objeto da Classe Usuario retornado no Corpo da Requisição 
		 * é igual ao Atributo Email do Objeto da Classe Usuario Retornado no Corpo da Resposta
		 * Se for verdadeiro, o teste passa, senão o teste falha.
		 */
	}
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuario")
	public void naoDeveDuplicarUsuario() {
		/**
		 * Persiste um objeto da Classe Usuario no Banco de dados através do Objeto da Classe UsuarioService
		 */
		usuarioService.CadastrarUsuario(new UsuarioModel(0L, "Maria da Silva", "maria_silva@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Sao Paulo - SP"));
		
		/**
		 * Cria o mesmo objeto da Classe Usuario que foi persistido no Banco de dados na linha anterior para
		 * simular uma duplicação de usuário e insere dentro de um Objeto da Classe HttpEntity (Entidade HTTP)
		 */
		HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(new UsuarioModel (0L, "Maria da Silva", "maria_silva@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Sao Paulo - SP"));
		
		/**
		 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
		 * enviada pelo Objeto da Classe TestRestTemplate.
		 * 
		 * Na requisição HTTP será enviada a URL do recurso (/usuarios/cadastrar), o verbo (POST), a entidade
		 * HTTP criada acima (corpoRequisicao) e a Classe de retornos da Resposta (Usuario).
		 */
		
		ResponseEntity<UsuarioModel> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, UsuarioModel.class);
		
		/**
		 * Verifica se a requisição retornou o Status Code BAD_REQUEST (400), que indica que o usuário já existe no
		 * Banco de dados. 
		 * Se for verdadeira, o teste passa, senão o teste falha.
		 */
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	@Test
	@Order(3)
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {
		/**
		 * Persiste um objeto da Classe Usuario no Banco de dados através do Objeto da Classe UsuarioService e
		 * guarda o objeto persistido no Banco de Dadoas no Objeto usuarioCadastrado, que será reutilizado abaixo. 
		 * 
		 * O Objeto usuarioCadastrado será do tipo Optional porquê caso o usuário não seja persistido no Banco 
		 * de dados, o Optional evitará o erro NullPointerException (Objeto Nulo).
		 */
		Optional<UsuarioModel> usuarioCadastrado = usuarioService.CadastrarUsuario(new UsuarioModel(0L, "Carla da Silva","carla_silva@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Recife - BA"));
		
		/**
		 *  Cria um Objeto da Classe Usuário contendo os dados do Objeto usuarioCadastrado, que foi persistido na
		 *  linha anterior, alterando os Atributos Nome e Usuário (Atualização dos Atributos). 
		 *  
		 * Observe que para obter o Id de forma automática, foi utilizado o método getId() do Objeto usuarioCadastrado.
		 */
		UsuarioModel usuarioUpdate = new UsuarioModel(usuarioCadastrado.get().getId(),"Carla Silva","carla_silva@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Recife - BA");
		
		/**
		 * Insere o objeto da Classe Usuario (usuarioUpdate) dentro de um Objeto da Classe HttpEntity (Entidade HTTP)
		 */
		
		HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(usuarioUpdate);
		
		/**
		 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
		 * enviada pelo Objeto da Classe TestRestTemplate.
		 * 
		 * Na requisição HTTP será enviada a URL do recurso (/usuarios/atualizar), o verbo (PUT), a entidade
		 * HTTP criada acima (corpoRequisicao) e a Classe de retornos da Resposta (Usuario).
		 * 
		 * Observe que o Método Atualizar não está liberado de autenticação (Login do usuário), por isso utilizamos o
		 * Método withBasicAuth para autenticar o usuário em memória, criado na BasicSecurityConfig.
		 * 
		 * Usuário: root
		 * Senha: root
		 */
		ResponseEntity<UsuarioModel> corpoResposta= testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, UsuarioModel.class);
		
		/**
		 *  Verifica se a requisição retornou o Status Code OK (200) 
		 * Se for verdadeira, o teste passa, se não, o teste falha.
		 */
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		
		/**
		 * Verifica se o Atributo Nome do Objeto da Classe Usuario retornado no Corpo da Requisição 
		 * é igual ao Atributo Nome do Objeto da Classe Usuario Retornado no Corpo da Resposta
		 * Se for verdadeiro, o teste passa, senão o teste falha.
		 */
		assertEquals(corpoRequisicao.getBody().getUsuario(),corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		/**
		 * Persiste dois objetos diferentes da Classe Usuario no Banco de dados através do Objeto da Classe UsuarioService
		 */
		usuarioService.CadastrarUsuario(new UsuarioModel(0L, "Maria da Silva", "maria_silva@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Sao Paulo - SP"));
		usuarioService.CadastrarUsuario(new UsuarioModel(1L, "Joao Carlos", "joaocarlos@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Rio de Janeiro - RJ"));
		
		/**
		 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
		 * enviada pelo Objeto da Classe TestRestTemplate.
		 * 
		 * Na requisição HTTP será enviada a URL do recurso (/usuarios/all), o verbo (GET), a entidade
		 * HTTP será nula (Requisição GET não envia nada no Corpo da Requisição) e a Classe de retorno da Resposta 
		 * (String), porquê a lista de Usuários será do tipo String.
		 * 
		 * Observe que o Método All não está liberado de autenticação (Login do usuário), por isso utilizamos o
		 * Método withBasicAuth para autenticar o usuário em memória, criado na BasicSecurityConfig.
		 * 
		 * Usuário: root
		 * Senha: root
		 */
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("usuarios/all", HttpMethod.GET, null, String.class);
		/**
		 *  Verifica se a requisição retornou o Status Code OK (200) 
		 * Se for verdadeira, o teste passa, se não, o teste falha.
		 */
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	@Test
	@Order(5)
	@DisplayName("Listar Um Usuário Específico")
	public void deveListarApenasUmUsuario() {
		/**
		 * Persiste um objeto da Classe Usuario no Banco de dados através do Objeto da Classe UsuarioService e
		 * guarda o objeto persistido no Banco de Dadoas no Objeto usuarioCadastrado, que será reutilizado abaixo. 
		 * 
		 * O Objeto usuarioCadastrado será do tipo Optional porquê caso o usuário não seja persistido no Banco 
		 * de dados, o Optional evitará o erro NullPointerException (Objeto Nulo).
		 */
		Optional<UsuarioModel> usuarioBusca = usuarioService.CadastrarUsuario(new UsuarioModel(0L, "Maria da Silva", "maria_silva@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Sao Paulo - SP"));
		
		/**
		 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
		 * enviada pelo Objeto da Classe TestRestTemplate.
		 * 
		 * Na requisição HTTP será enviada a URL do recurso ("/usuarios/" + usuarioBusca.get().getId()), o verbo (GET), a entidade
		 * HTTP será nula (Requisição GET não envia nada no Corpo da Requisição) e a Classe de retorno da Resposta 
		 * (String), porquê a lista de Usuários será do tipo String.
		 * 
		 * Para obtero Id de forma automática, foi utilizado o método getId() do Objeto usuarioBusca.
		 * 
		 * Observe que o Método All não está liberado de autenticação (Login do usuário), por isso utilizamos o
		 * Método withBasicAuth para autenticar o usuário em memória, criado na BasicSecurityConfig.
		 * 
		 * Usuário: root
		 * Senha: root
		 */
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/" + usuarioBusca.get(), HttpMethod.GET, null, String.class);
		/**
		 *  Verifica se a requisição retornou o Status Code OK (200) 
		 * Se for verdadeira, o teste passa, se não, o teste falha.
		 */
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	@Test
	@Order(6)
	@DisplayName("Login do Usuário")
	public void deveAutenticarUsuario() {
		/**
		 * Persiste um objeto da Classe Usuario no Banco de dados através do Método cadastrarUsuario
		 * da Classe UsuarioService
		 */
		usuarioService.CadastrarUsuario(new UsuarioModel(0L,"Maria da Silva", "maria_silva@email.com.br","123456789","https://i.imgur.com/T12NIp9.jpg","Sao Paulo - SP"));
		/**
		 * Cria um Objeto da Classe UsuarioLogin dentro de um Objeto da Classe HttpEntity (Entidade HTTP).
		 * O Objeto desta Classe será preenchido apenas como o usuário e senha do usuário criado acima.
		 */
		HttpEntity<UserLogin> corpoRequisicao = new HttpEntity<UserLogin>(new UserLogin(0L,"Maria da Silva", "maria_silva@email.com.br", "123456789","","", ""));
		/**
		 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
		 * enviada pelo Objeto da Classe TestRestTemplate.
		 * 
		 * Na requisição HTTP será enviada a URL do recurso (/usuarios/logar), o verbo (POST), a entidade
		 * HTTP criada acima (corpoRequisicao) e a Classe de retornos da Resposta (UsuarioLogin).
		 */
		ResponseEntity<UserLogin> corpoResposta = testRestTemplate
				.exchange("/usuarios/logar", HttpMethod.POST, corpoRequisicao, UserLogin.class);
		/**
		 *  Verifica se a requisição retornou o Status Code OK (200) 
		 * Se for verdadeira, o teste passa, se não, o teste falha.
		 */
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}
}
