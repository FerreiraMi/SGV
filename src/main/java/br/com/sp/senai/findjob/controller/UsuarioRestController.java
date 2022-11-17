package br.com.sp.senai.findjob.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.sp.senai.findjob.model.Erro;
import br.com.sp.senai.findjob.model.TokenJWT;
import br.com.sp.senai.findjob.model.Usuario;
import br.com.sp.senai.findjob.repository.UsuarioRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public static final String SECRET = "f1ndJ0b@";
	public static final String EMISSOR = "SistemaGerenciadorVagas";

	// metodo encoder para salvar a criptografia
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	//criar metodo para alterar a senha
	//criar metodo para enviar email "usuario cadastrado com sucesso";
	
	// metodo está funcionando
	// metodo para criar usuario
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarUsuario(@RequestBody Usuario usuario, HttpServletRequest request) {
		if (usuario != null) {
			// criptografa a senha
			String cripto = this.passwordEncoder.encode(usuario.getSenha());

			// pega a senha criptografada
			usuario.setSenha(cripto);

			// ativa o usuario no banco de dados
			usuario.setAtivo(true);
			usuarioRepository.save(usuario);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar Usuario", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// metodo funcionando
	// metodo para pegar usuario especifico pelo id
	@RequestMapping(value = "/especifico/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> buscaUsuarioEspecifico(@PathVariable Long id) {
		try {
			Optional<Usuario> u = usuarioRepository.findById(id);
			if (u.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuario nao localizado");
			}
			return ResponseEntity.status(200).body(u);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(e);
		}
	}

	//metodo funcionando
	// metodo para listar todos os usuarios inseridos no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Usuario> listaUsuario(Usuario usuario) {
		return usuarioRepository.findAll();

	}

	//metodo esta funcionando
	// metodo para atualizar os dados do usuario
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuario,
			HttpServletRequest request) {

		if (usuario.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			// busca o usuario no banco de dados
			usuarioRepository.findById(id);

			// criptografa a senha
			String cripto = this.passwordEncoder.encode(usuario.getSenha());

			// pega a senha criptografada
			usuario.setSenha(cripto);

			usuarioRepository.save(usuario);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
	}

	// metodo para validar a senha quando fazer o login
	public Boolean validarSenha(Usuario usuario) {
		// pegando a senha no banco
		String senha = usuarioRepository.findById(usuario.getId()).get().getSenha();
		// validacao da senha, comparando com o banco de dados
		Boolean valido = passwordEncoder.matches(usuario.getSenha(), senha);
		return valido;
	}

	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenJWT> login(@RequestBody Usuario usuario, HttpServletRequest request){
		
		Boolean valido = validarSenha(usuario);
		
		// buscar o usuário no banco de dados
		usuario = usuarioRepository.findByCpfAndSenha(usuario.getCpf(), usuario.getSenha());
		// verifica se o usuário não é nulo
		if(usuario != null) {
			// variável para inserir dados no payload
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("id_user", usuario.getId());
			payload.put("cpf", usuario.getCpf());
			payload.put("nome", usuario.getNome());
			payload.put("email", usuario.getEmail());
			payload.put("senha", usuario.getSenha());
			payload.put("ativo", usuario.getAtivo());
			
			// variável para a data de expiração
			Calendar expiracao = Calendar.getInstance();
			
			// adiciona
			expiracao.add(Calendar.HOUR, 1);
			
			// algoritmo para assinar o toke
			Algorithm algoritmo = Algorithm.HMAC256(SECRET);
			
			// cria o objeto para receber token
			TokenJWT tokenJwt = new TokenJWT();
			
			// gera o token
			tokenJwt.setToken(JWT.create()
					.withPayload(payload)
					.withIssuer(EMISSOR)
					.withExpiresAt(expiracao.getTime())
					.sign(algoritmo));
			return ResponseEntity.ok(tokenJwt);
		}else {
			return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	/*}

	// metodo para excluir usuario pelo id
	@RequestMapping(value = "/excluir/{id}", method = RequestMethod.PUT)
	public boolean excluirUsuario(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
		return true;
	}*/
	//metodo para tornar o estado Ativo do usuario como false
	  
	 @RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT) 
	 public ResponseEntity<Object> desativaUsuario(@PathVariable("id") Long id,
	 HttpServletRequest request) { Optional<Usuario> desativar =
	 usuarioRepository.findById(id);
	  
	  if (desativar.get().getId() == id) { desativar.get().setAtivo(false);
	  usuarioRepository.save(desativar.get()); return new
	  ResponseEntity<Object>(HttpStatus.OK); } else { Erro erro = new
	  Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel desativar usuario",
	  null); return new ResponseEntity<Object>(erro,
	  HttpStatus.INTERNAL_SERVER_ERROR); }
	  
	  }
	 

}
