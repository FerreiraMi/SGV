package br.com.sp.senai.findjob.repository;

import org.springframework.data.repository.PagingAndSortingRepository;


import br.com.sp.senai.findjob.model.SolicitacaoVaga;

public interface SolicitacaoRepository extends PagingAndSortingRepository<SolicitacaoVaga, Long> {
		

}
