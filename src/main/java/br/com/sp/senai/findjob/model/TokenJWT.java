package br.com.sp.senai.findjob.model;

import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

public class TokenJWT {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Algorithm setToken(Builder withIssuer) {
		// TODO Auto-generated method stub
		return null;
	}

}
