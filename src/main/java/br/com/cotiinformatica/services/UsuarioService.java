package br.com.cotiinformatica.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.AutenticarUsuarioResponseDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioResponseDto;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.exceptions.AcessoNegadoException;
import br.com.cotiinformatica.exceptions.EmailJaCadastradoException;
import br.com.cotiinformatica.helpers.CryptoHelper;
import br.com.cotiinformatica.helpers.JwtHelper;
import br.com.cotiinformatica.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/*
	 * Serviço para criação de usuário
	 */
	public CriarUsuarioResponseDto criarUsuario(CriarUsuarioRequestDto request) {		
		
		//Verificação de segurança para não permitir email duplicado
		//perguntando se já existe um usuário no banco de dados com o email informado
		if(usuarioRepository.find(request.getEmail()) != null)			
			throw new EmailJaCadastradoException(); 
				
		//criando um objeto da classe Usuario
		var usuario = new Usuario();
		
		//Criando um objeto da classe de criptografia
		var crypto = new CryptoHelper();
		
		//Preenchendo os dados do usuário
		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());
		usuario.setSenha(crypto.getSha256(request.getSenha()));
		usuario.setDataHoraCriacao(LocalDateTime.now());
		
		//gravar o usuário no banco de dados
		usuarioRepository.save(usuario);
		
		//Retornar os dados da resposta
		var response = new CriarUsuarioResponseDto();
		response.setId(usuario.getId());
		response.setNome(usuario.getNome());
		response.setEmail(usuario.getEmail());
		response.setDataHoraCriacao(usuario.getDataHoraCriacao());
		
		return response;
	}
	
	/*
	 * Serviço para autenticação de usuário
	 */
	public AutenticarUsuarioResponseDto autenticarUsuario(AutenticarUsuarioRequestDto request) {

		var crypto = new CryptoHelper();
		
		//Consultar o usuário no banco de dados através do email e da senha
		var usuario = usuarioRepository.find(request.getEmail(), crypto.getSha256(request.getSenha()));
		
		//Verificar se o usuário não foi encontrado
		if(usuario == null)
			throw new AcessoNegadoException();
		
		//Definindo o tempo de expiração
		var expiracao = 3600000L;
		
		//Gerando o TOKEN do usuário
		var jwtHelper = new JwtHelper();
		var token = jwtHelper.generateToken(request.getEmail(), expiracao, "autenticacaoapi-cotiinformatica");
		
		//Retornar os dados do usuário
		var response = new AutenticarUsuarioResponseDto();
		response.setId(usuario.getId());
		response.setNome(usuario.getNome());
		response.setEmail(usuario.getEmail());
		response.setAccessToken(token);
		response.setDataHoraAcesso(LocalDateTime.now());
		response.setDataHoraExpiracao(LocalDateTime.now().plusSeconds(expiracao / 1000));
		
		return response;
	}
}











