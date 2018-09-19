package com.viniciuspugliesi.cursomc.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class CredenciaisDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotEmpty(message="Preenchimento obrigatório.")
	private String email;

	@NotEmpty(message="Preenchimento obrigatório.")
	private String senha;
	
	CredenciaisDTO() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
