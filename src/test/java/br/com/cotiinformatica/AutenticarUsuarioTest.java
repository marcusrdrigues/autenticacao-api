package br.com.cotiinformatica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.dtos.AutenticarUsuarioRequestDto;
import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;

@SpringBootTest(classes = AutenticacaoApiApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutenticarUsuarioTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Deve autenticar um usuário com sucesso.")
	@Order(1)
	public void autenticarUsuarioComSucesso() {
		try {
			
			//Instanciando a biblioteca Java Faker
			var faker = new Faker(Locale.of("pt-BR"));
			
			//Arrange (criar os dados da requisição do teste)
			var requestCriar = new CriarUsuarioRequestDto();
			requestCriar.setNome(faker.name().firstName() + " " + faker.name().lastName());
			requestCriar.setEmail(faker.internet().emailAddress());
			requestCriar.setSenha("@Teste2025");
			
			var requestAutenticar = new AutenticarUsuarioRequestDto();
			requestAutenticar.setEmail(requestCriar.getEmail());
			requestAutenticar.setSenha(requestCriar.getSenha());
			
			//Act (executar o endpoint da API)
			var resultCriar = mockMvc.perform(
					post("/api/v1/usuarios/criar") //cadastrando o usuário
						.contentType("application/json") //formato de dados
						.content(objectMapper.writeValueAsString(requestCriar))
					).andReturn();
			
			var resultAutenticar = mockMvc.perform(
					post("/api/v1/usuarios/autenticar") //autenticando o usuário
						.contentType("application/json") //formato de dados
						.content(objectMapper.writeValueAsString(requestAutenticar))
					).andReturn();
							
			//Assert (verificar o resultado obtido)
			assertEquals(200, resultCriar.getResponse().getStatus());
			assertEquals(200, resultAutenticar.getResponse().getStatus());
		}
		catch(Exception e) {
			fail("Teste falhou: " + e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Deve validar todos os campos como obrigatórios.")
	@Order(2)
	public void validarCamposObrigatorios() {
		try {
					
			//Arrange
			var request = new AutenticarUsuarioRequestDto();
			request.setEmail(null);
			request.setSenha(null);
					
			//Act
			var result = mockMvc.perform(
					post("/api/v1/usuarios/autenticar") //autenticando o usuário
						.contentType("application/json") //formato de dados
						.content(objectMapper.writeValueAsString(request))
					).andReturn();
							
			//Assert
			assertEquals(400, result.getResponse().getStatus());
			assertTrue(result.getResponse().getContentAsString().contains("O email de acesso é obrigatório."));
			assertTrue(result.getResponse().getContentAsString().contains("A senha de acesso é obrigatório."));
		}
		catch(Exception e) {
			fail("Teste falhou: " + e.getMessage());
		}		
	}
	
	@Test
	@DisplayName("Deve retornar acesso negado para usuário inválido.")
	@Order(3)
	public void acessoNegado() {
		try {
			
			var faker = new Faker(Locale.of("pt-BR"));
			
			//Arrange
			var request = new AutenticarUsuarioRequestDto();
			request.setEmail(faker.internet().emailAddress());
			request.setSenha("@Senha123");
					
			//Act
			var result = mockMvc.perform(
					post("/api/v1/usuarios/autenticar") //autenticando o usuário
						.contentType("application/json") //formato de dados
						.content(objectMapper.writeValueAsString(request))
					).andReturn();
							
			//Assert
			assertEquals(401, result.getResponse().getStatus());
			assertTrue(result.getResponse().getContentAsString().contains("Acesso negado. Usuário inválido."));
		}
		catch(Exception e) {
			fail("Teste falhou: " + e.getMessage());
		}		
	}
}
