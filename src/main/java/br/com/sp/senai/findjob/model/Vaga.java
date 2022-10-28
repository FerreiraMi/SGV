package br.com.sp.senai.findjob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import lombok.Data;

@Data
@Entity
@Table(name = "vagas")
public class Vaga {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "titulo_vaga", length = 50, nullable = true)
	private String tituloVaga;
	@Email
	@Column(name = "email_contato", length = 100, nullable = true)
	private String emailContato;
	@Column(name = "whatsapp", length = 50, nullable = false)
	private String whatsapp;
	@Column(name = "exigencias", length = 50, nullable = true)
	private String exigencias;
	@Column(name = "periodo", length = 50, nullable = false)
	private String periodo;
	
	private Boolean ativo;
	@Column(name="area_profissional", nullable = true)
	private AreaProfissional areaProfissional;
	
	@ManyToOne
	@JoinColumn(name="empresa_id")
	private Empresa empresa;

}
