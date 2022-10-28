package br.com.sp.senai.findjob.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.senai.findjob.model.Erro;
import br.com.sp.senai.findjob.model.Vaga;
import br.com.sp.senai.findjob.repository.EmpresaRepository;
import br.com.sp.senai.findjob.repository.VagaRepository;

@RestController
@RequestMapping("api/empresa/vaga")
public class VagaRestController {

	@Autowired
	private VagaRepository vagaRepository;

	@Autowired
	private EmpresaRepository empresaRepository;
	
	//criar metodo que pega a empresa relacionada aquela vaga
	//criar metodo que lista vaga por AreaProfissional
	//criar metodo  que lista vaga por Empresa
	//criar metodo que solicita aprovacao da vaga pelo adm
	//criar metodo status da vaga se aprovada ou se recusada pelo adm
	
	// metodo para buscar Vagas usuario
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Object> buscaVaga(@PathVariable Long id) {
		try {
			Optional<Vaga> v = vagaRepository.findById(id);
			if (v.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("vaga não encontrada");
			}
			return ResponseEntity.status(200).body(v);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(e);
		}
	}
	
		// metodo para cadastrar vagas 
		@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> cadastraVaga(@RequestBody Vaga vaga, HttpServletRequest request) {
			if (vaga != null) {
				vagaRepository.save(vaga);
			} else {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar dados do Usuario", null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return null;
		}

	}


