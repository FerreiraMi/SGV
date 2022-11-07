package br.com.sp.senai.findjob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.senai.findjob.model.DadosPessoais;
import br.com.sp.senai.findjob.model.Erro;
import br.com.sp.senai.findjob.repository.DadosPessoaisRepository;
import br.com.sp.senai.findjob.repository.UsuarioRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/usuario/dados")
public class DadosPessoaisRestController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private DadosPessoaisRepository dadosPessoaisRepository;

	// formacao profissional
	
	// metdo para criar Dados Pessoais do Usuario (GR) *model alterada
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> cadastroDadosPessoais(@RequestBody DadosPessoais dadosPessoais){
		if (dadosPessoais != null) {
			dadosPessoaisRepository.save(dadosPessoais);
			return ResponseEntity.status(201).body(dadosPessoais);
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar dados do Usuario",null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// metodo para listar dados inseridos no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<DadosPessoais> listaDadosPessoais(DadosPessoais dadosPessoais) {
		return dadosPessoaisRepository.findAll();
	}
	

	/*// metodo para cadastrar dados do usuario
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cadastrarDados(@RequestBody DadosPessoais dadosPessoais, HttpServletRequest request) {

		Optional<Usuario> u = usuarioRepository.findById(dadosPessoais.getUsuario().getId());

		if (!u.isEmpty() && dadosPessoais != null) {
			u.get().setDadosPessoais(dadosPessoais);
			
			try {
				usuarioRepository.save(u.get());
			} catch (Exception e) {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar dados do Usuario",
						null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		if (dadosPessoais != null) {
			dadosPessoaisRepository.save(dadosPessoais);
		}
		
		return null;
	}*/

}
