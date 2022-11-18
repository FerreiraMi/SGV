package br.com.sp.senai.findjob.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.sp.senai.findjob.model.Administrador;
import br.com.sp.senai.findjob.model.Empresa;
import br.com.sp.senai.findjob.model.Erro;
import br.com.sp.senai.findjob.model.Sucesso;
import br.com.sp.senai.findjob.model.TokenJWT;
import br.com.sp.senai.findjob.repository.AdministradorRepository;

@RestController
@RequestMapping("api/adm")
public class AdministradorRestController {

	public static final String SECRET = "f1ndJ0b@";
	public static final String EMISSOR = "SistemaGerenciadorVaga";
	private static final int Administrador = 0;

	@Autowired
	private AdministradorRepository administradorRepository;

	// metodo encoder para salvar a criptografia
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	
	// cadastraadm e criptografa a senha dele
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Administrador> cadastraAdministrador(@RequestBody Administrador administrador,
			HttpServletRequest request) {
		if (administrador != null) {
			// criptografa a senha
			String cripto = this.passwordEncoder.encode(administrador.getSenha());

			// pega a senha criptografada
			administrador.setSenha(cripto);

			// ativa o usuario no banco de dados
			administrador.setAtivo(true);

		}
		try {
			administradorRepository.save(administrador);
			return ResponseEntity.status(201).body(administrador);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<Administrador>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Administrador>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * // metodo para fazer o login
	 * 
	 * @RequestMapping(value = "/login", method = RequestMethod.POST, consumes =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<Object>
	 * login(@RequestBody Administrador administrador, HttpServletRequest request) {
	 * 
	 * Boolean validar = validarSenhaAdm(administrador);
	 * 
	 * if (validar = true) { // insere nif e senha dentro da variavel administrador
	 * = administradorRepository.findByIdAndSenha(administrador.getId(),
	 * administrador.getSenha());
	 * 
	 * // verifica se existe administrador cadastrado no banco if (administrador !=
	 * null) { System.out.println(administrador.getNome());
	 * 
	 * Map<String, Object> payload = new HashMap<String, Object>();
	 * 
	 * payload.put("nome", administrador.getNome()); payload.put("email",
	 * administrador.getEmail()); payload.put("senha", administrador.getSenha());
	 * payload.put("nif", administrador.getNif());
	 * 
	 * // coloca assinatura do algoritmo no token Algorithm algoritmo =
	 * Algorithm.HMAC512(SECRET);
	 * 
	 * // instancia a classe token TokenJWT tokenJwt = new TokenJWT();
	 * 
	 * // adiciona no token
	 * tokenJwt.setToken(JWT.create().withPayload(payload).withIssuer(EMISSOR).sign(
	 * algoritmo));
	 * 
	 * System.out.println(tokenJwt);
	 * 
	 * // envia o token return ResponseEntity.ok(tokenJwt); } } return new
	 * ResponseEntity<Object>(HttpStatus.UNAUTHORIZED); }
	 */

	// metodo para validar a senha quando fazer o login
	public Boolean validarSenhaAdm(Administrador administrador) {
		// pegando a senha no banco
		String senha = administradorRepository.findById(administrador.getId()).get().getSenha();
		// validacao da senha, comparando com o banco de dados
		Boolean valido = passwordEncoder.matches(administrador.getSenha(), senha);
		return valido;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Administrador> buscaAdm(Administrador administrador) {
		return administradorRepository.findAll();
	}
	/*
	 * // metodo para desativa ADM pelo id //arrumar metodo para desativar
	 * 
	 * @RequestMapping(value = "/excluirAdm/{id}", method = RequestMethod.PUT)
	 * public boolean excluirAdm(@PathVariable Long id) {
	 * administradorRepository.deleteById(id); return true; }
	 */

	// metodo para atualizar os adm *Funcionando
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> attAdministrador(@PathVariable("id") Long id, @RequestBody Administrador adm) {

		if (adm.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {

			// vendo se o adm é existente
			administradorRepository.findById(id);

			// pegando a senha e transcrevendo
			String crip = this.passwordEncoder.encode(adm.getSenha());

			adm.setSenha(crip);

			administradorRepository.save(adm);
			return new ResponseEntity<Object>(HttpStatus.OK);

		}

	}

	@RequestMapping(value = "/ativarAdm/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativarAdm(@PathVariable("id") Long id, Administrador administrador,
			HttpServletRequest request) {
		administrador = administradorRepository.findById(id).get();
		administrador.setAtivo(true);
		administradorRepository.save(administrador);
		Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
		return new ResponseEntity<Object>(sucesso, HttpStatus.OK);

	}

}
