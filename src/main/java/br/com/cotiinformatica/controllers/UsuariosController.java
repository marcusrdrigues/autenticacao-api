package br.com.cotiinformatica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;
import br.com.cotiinformatica.services.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuariosController {

	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("criar")
	public ResponseEntity<?> criar(@RequestBody @Valid CriarUsuarioRequestDto request) {
		var response = usuarioService.criarUsuario(request);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("autenticar")
	public ResponseEntity<?> autenticar(@RequestBody @Valid AutenticarUsuarioRequestDto request) {
		var response = usuarioService.autenticarUsuario(request);
		return ResponseEntity.ok().body(response);
	}
}
