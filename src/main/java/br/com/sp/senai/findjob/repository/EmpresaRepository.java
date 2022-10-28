package br.com.sp.senai.findjob.repository;

import javax.validation.constraints.NotNull;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.sp.senai.findjob.model.Empresa;

public interface EmpresaRepository extends PagingAndSortingRepository<Empresa, Long> {

	// buscar empresa no banco por Id e senha.
	public Empresa findByIdAndSenha(Long id, @NotNull String senha);

}
