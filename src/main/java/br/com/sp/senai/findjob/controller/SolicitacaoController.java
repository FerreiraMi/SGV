package br.com.sp.senai.findjob.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sp.senai.findjob.model.SolicitacaoVaga;
import br.com.sp.senai.findjob.model.Vaga;
import br.com.sp.senai.findjob.repository.AdministradorRepository;
import br.com.sp.senai.findjob.repository.SolicitacaoRepository;
import br.com.sp.senai.findjob.repository.VagaRepository;

@RestController
@RequestMapping("api/solicitacao")
public class SolicitacaoController {
	@Autowired
	SolicitacaoRepository solicitrepository;
	@Autowired
	VagaRepository vagaRepository;
	@Autowired
	AdministradorRepository admRepository;
	
	//metodo para pegar vagas e listar
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Vaga> listaVaga(Vaga vaga){
		return vagaRepository.findAll();
	}
		
	/*
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> aprovaVaga (@PathVariable ("id") Long id){
		SolicitacaoVaga vagaAp = solicitrepository.findById(id).get();
		    
	    }
			
		
		
		return vagas;
	}
	}*/
		

}


