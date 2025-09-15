package br.com.cotiinformatica.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.cotiinformatica.exceptions.EmailJaCadastradoException;

@ControllerAdvice
public class EmailJaCadastradoExceptionHandler {

	@ExceptionHandler(EmailJaCadastradoException.class)
	public ResponseEntity<Object> handleEmailJaCadastrado(EmailJaCadastradoException ex) {

		Map<String, Object> body = new HashMap<>();

		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.CONFLICT.value());
		body.put("errors", ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
	}
}
