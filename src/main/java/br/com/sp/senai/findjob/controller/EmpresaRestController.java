package br.com.sp.senai.findjob.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import br.com.sp.senai.findjob.model.Empresa;
import br.com.sp.senai.findjob.model.Erro;
import br.com.sp.senai.findjob.model.Sucesso;
import br.com.sp.senai.findjob.model.TokenJWT;

import br.com.sp.senai.findjob.repository.EmpresaRepository;

@CrossOrigin
@RestController
@RequestMapping("api/empresa")
public class EmpresaRestController {

	@Autowired
	private EmpresaRepository empresaRepository;

	public static final String SECRET = "f1ndJ0b@";
	public static final String EMISSOR = "SistemaGerenciadorVaga";

	// metodo encoder para salvar a criptografia
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// criar metodo que solicita aprovacao da empresa pelo adm
	// criar metodo status da empresa se aprovada ou se recusada pelo adm
	// criar metodo que envia por email a solicitacao de cadastro da empresa
	// criar metodo para alterar a senha
	// criar metodo para enviar email "usuario cadastrado com sucesso";
	
	
	// refeito metodo POST da Empresa (GR) *Alteração na model
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> cadastroEmpresaPOST(@RequestBody Empresa empresa){
		if(empresa != null) {
			empresaRepository.save(empresa);
			return ResponseEntity.status(201).body(empresa);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// metodo para listar todos as empresas inseridos no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Empresa> listaEmpresa(Empresa empresa) {
		return empresaRepository.findAll();
		
	}
	
	// refazendo metodo que tras a empresa pelo ID (GR)*add uma Query no Repository
	@RequestMapping(value = "/empresaID/{id}", method = RequestMethod.GET)
	public Iterable<Empresa> listaPorID(@PathVariable("id") Long id){
		return empresaRepository.buscaPorIdEmpresa(id);
	}
	/*
	 * //metodo para pegar empresa especifica pelo id
	 * 
	 * @RequestMapping(value = "/empresa/{id}", method = RequestMethod.GET) public
	 * ResponseEntity<Object> buscaUsuarioEspecifico(@PathVariable Long id) { try {
	 * Optional<Empresa> e = empresaRepository.findById(id); if (e.isEmpty()) {
	 * return
	 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body("empresa não localizada");
	 * } return ResponseEntity.status(200).body(e); } catch (Exception e) {
	 * e.printStackTrace(); return ResponseEntity.status(500).body(e); } }
	 */
	
	/*
	 * //metodo para cadastrar empresa
	 * 
	 * @RequestMapping(value = "", method = RequestMethod.PUT, consumes =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<Empresa>
	 * cadastraEmpresa(@Valid @RequestBody Empresa empresa) { try {
	 * empresaRepository.save(empresa); return
	 * ResponseEntity.status(201).body(empresa); } catch
	 * (DataIntegrityViolationException e) { e.printStackTrace(); return new
	 * ResponseEntity<Empresa>(HttpStatus.INTERNAL_SERVER_ERROR); } catch (Exception
	 * e) { e.printStackTrace(); return new
	 * ResponseEntity<Empresa>(HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */


	// metodo para atualizar os dados da Empresa *Funcionando
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarEmpresa(@PathVariable("id") Long id, @RequestBody Empresa empresa,
			HttpServletRequest request) {

		if (empresa.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {

			// busca a empresa no banco de dados
			empresaRepository.findById(id);

			// criptografa a senha
			String cripto = this.passwordEncoder.encode(empresa.getSenha());

			// pega a senha criptografada
			empresa.setSenha(cripto);

			empresaRepository.save(empresa);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
	}

	// metodo para validar a senha quando fazer o login
	public Boolean validarSenhaEmpresa(Empresa empresa) {
		// pegando a senha no banco
		String senha = empresaRepository.findById(empresa.getId()).get().getSenha();
		// validacao da senha, comparando com o banco de dados
		Boolean valido = passwordEncoder.matches(empresa.getSenha(), senha);
		return valido;
	}

	// metodo para realizar login
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> login(@RequestBody Empresa empresa, HttpServletRequest request) {

		validarSenhaEmpresa(empresa);

		if (true) {
			// se a senha for valida insere dentro da variavel
			empresa = empresaRepository.findByIdAndSenha(empresa.getId(), empresa.getSenha());

			// verifica se existe empresa cadastrado
			if (empresa != null) {
				System.out.println(empresa.getCnpj());

				// cria uma variavel payload e insere os dados da empresa
				Map<String, Object> payload = new HashMap<String, Object>();

				payload.put("nome", empresa.getNome());
				payload.put("email", empresa.getEmail());
				payload.put("senha", empresa.getSenha());
				payload.put("ativo", empresa.getAtivo());

				// coloca assinatura do algoritmo no token
				Algorithm algoritimo = Algorithm.HMAC512(SECRET);

				// instancia a classe token
				TokenJWT tokenJwt = new TokenJWT();

				tokenJwt.setToken(JWT.create().withPayload(payload).withIssuer(EMISSOR).sign(algoritimo));

				System.out.println(tokenJwt);

				// envia o token
				return ResponseEntity.ok(tokenJwt);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
	}

	/*
	 * // metodo para tornar o estado Ativo da empresa como false
	 * 
	 * @RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT) public
	 * ResponseEntity<Object> desativaEmpresa(@PathVariable("id") Long id,
	 * HttpServletRequest request) { Optional<Empresa> desativar =
	 * empresaRepository.findById(id);
	 * 
	 * if (desativar.get().getId() == id) { desativar.get().setAtivo(false);
	 * empresaRepository.save(desativar.get()); System.out.println("passou aqui");
	 * return new ResponseEntity<Object>(HttpStatus.OK);
	 * 
	 * } else { Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
	 * "Não foi possivel desativar empresa", null);
	 * System.out.println("xiiiiiiiiiiiiiiiiiiiiiiii"); return new
	 * ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
	 */
			
	@RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativar(@PathVariable("id") Long id, Empresa empresa,HttpServletRequest request) {		
		empresa = empresaRepository.findById(id).get();
		empresa.setAtivo(false);
		empresaRepository.save(empresa);
		Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
		return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		

		
	}

	
}
