package br.com.sp.senai.findjob.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.br.CNPJ;

import lombok.Data;

@Data
@Entity
@Table(name = "empresa")
public class Empresa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nome", length = 100)
	private String nome;
	@Email
	@Column(name = "email", length = 50)
	private String email;
	@Column(name = "telefone", length = 20)
	private String telefone;
	@Column(name = "cnpj", length = 20)
	@CNPJ
	private String cnpj;

	@OneToMany
	@JoinTable(name = "vagas")
	private List<Vaga> vagas;

	@Column(name = "senha", columnDefinition = "TEXT")
	private String senha;
	@Column(name = "cep", length = 50)
	private String cep;
	@Column(name = "endereco", length = 100)
	private String endereco;
	@Column(name = "complemento", length = 15)
	private String complemento;
	@Column(name = "bairro", length = 50)
	private String bairro;
	@Column(name = "cidade", length = 50)
	private String cidade;
	@Column(name = "uf", length = 5)
	private String uf;
	private Boolean ativo;

}
