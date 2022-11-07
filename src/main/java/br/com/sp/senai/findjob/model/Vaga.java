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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@Entity
@Table(name = "vagas")
public class Vaga {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "titulo_vaga", length = 50)
	private String tituloVaga;
	
	@Email
	@Column(name = "email_contato", length = 100)
	private String emailContato;
	
	@Column(name = "whatsapp", length = 50)
	private String whatsapp;
	
	@Column(name = "exigencias", length = 50)
	private String exigencias;
	
	@Column(name = "periodo", length = 50)
	private String periodo;
	
	private Boolean ativo;
	
	@Column(name="area_profissional")
	private AreaProfissional areaProfissional;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	private Empresa empresa;

}