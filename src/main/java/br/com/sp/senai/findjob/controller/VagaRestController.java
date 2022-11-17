package br.com.sp.senai.findjob.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	// criar metodo que pega a empresa relacionada aquela vaga
	// criar metodo que lista vaga por AreaProfissional *perguntar se é para listar
	// em ordem de areaProfissional ou é para fazer uma busca por areaProfissional

	// criar metodo que solicita aprovacao da vaga pelo adm
	// criar metodo status da vaga se aprovada ou se recusada pelo adm

	// refeito o metodo POST da Vaga (GR) *Alteração na model
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> cadastroVagaPOST(@RequestBody Vaga vaga) {
		if (vaga != null) {
			vagaRepository.save(vaga);
			return ResponseEntity.status(201).body(vaga);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Metodo feito para Trazer todas as Vagas Do Banco (GR)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Vaga> buscaVagaGET(Vaga vaga) {
		return vagaRepository.findAll();
	}

	// Metodo feito para trazer Vagas conforme o ID da Empresa (GR)
	// criar metodo que lista vaga por Empresa
	@RequestMapping(value = "/buscaVaga/{id}", method = RequestMethod.GET)
	public List<Vaga> buscaVagaConformeEmpresa(@PathVariable("id") Long id) {
		return vagaRepository.buscaVagaPorEmpresa(id);
	}

	// metodo para excluir vaga pelo id
	@RequestMapping(value = "/excluir/{id}", method = RequestMethod.PUT)
	public boolean excluirVaga(@PathVariable Long id) {
		vagaRepository.deleteById(id);
		return true;
	}

	//metodo para editar vaga
	@RequestMapping(value = "editavaga/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> editaVaga(@RequestBody Vaga vaga) {
		if (vaga != null) {
			vagaRepository.save(vaga);
			return ResponseEntity.status(201).body(vaga);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/*@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> attVagas(@PathVariable("id") Long id, @RequestBody Vaga vaga){
		if (vaga.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			vagaRepository.save(vaga);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
	}*/
	
}
