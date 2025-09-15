package br.com.cotiinformatica.exceptions;

public class EmailJaCadastradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/*
	 * Método para retornar a mensagem de erro
	 */
	@Override
	public String getMessage() {	
		return "O email informado já está cadastrado. Tente outro.";
	}
}
