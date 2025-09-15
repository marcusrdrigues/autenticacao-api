package br.com.cotiinformatica.helpers;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtHelper {

	/*
	 * Método para que possamos gerar um TOKEN JWT
	 * com as credenciais do usuário autenticado
	 */
	public String generateToken(String user, Long expiration, String secretKey) {
		
		// Data atual (momento da geração do token)
        Date dataAtual = new Date();
        
        // Data de expiração (atual + tempo em milissegundos)
        Date dataExpiracao = new Date(dataAtual.getTime() + expiration);

        // Geração do token
        return Jwts.builder()
                .setSubject(user)               // Usuário dono do token
                .setIssuedAt(dataAtual)         // Data de emissão
                .setExpiration(dataExpiracao)   // Data de expiração
                .signWith(SignatureAlgorithm.HS256, secretKey) // Algoritmo e chave
                .compact();
	}
}
