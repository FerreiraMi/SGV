package br.com.sp.senai.findjob.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.sp.senai.findjob.model.Administrador;

public interface AdministradorRepository extends PagingAndSortingRepository<Administrador, Long> {

	Administrador findByIdAndSenha(Long id, String senha);

	Administrador findByNifAndSenha(String nif, String senha);

	Administrador findByEmail(String email);

	Administrador findByCpf(String cpf);

	Administrador findByCpfAndSenha(String cpf, String senha);

	//Quey para buscar por id de Administrador Especificado
	@Query("SELECT a FROM Administrador a WHERE a.id = :id")
	public List<Administrador> buscaPorId(@Param("id") Long id);

}
