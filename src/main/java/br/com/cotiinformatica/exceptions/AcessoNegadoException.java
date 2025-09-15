package br.com.cotiinformatica.exceptions;

public class AcessoNegadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/*
	 * Método para retornar a mensagem de erro
	 */
	@Override
	public String getMessage() {	
		return "Acesso negado. Usuário inválido.";
	}
}
