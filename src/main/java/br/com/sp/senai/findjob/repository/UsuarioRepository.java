package br.com.sp.senai.findjob.repository;

import javax.validation.constraints.NotNull;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.sp.senai.findjob.model.Usuario;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

	// buscar usuario no banco por Id e senha.
	public Usuario findByIdAndSenha(Long id, @NotNull String senha);

	public Usuario findByCpfAndSenha(String cpf, String senha);
}
