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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CriarUsuarioTest {

	@Autowired
	private MockMvc mockMvc; //Executar testes na API REST
	
	@Autowired
	private ObjectMapper objectMapper; //Serializar dados em JSON
	
	//Atributo para armazenar o email do 
	//usuário cadastrado no primeiro teste
	private static String emailUsuario;
	
	@Test
	@DisplayName("Deve criar um usuário com sucesso.")
	@Order(1)
	public void criarUsuarioComSucesso() {
		try {
			
			//Instanciando a biblioteca Java Faker
			var faker = new Faker(Locale.of("pt-BR"));
			
			//Arrange (criar os dados da requisição do teste)
			var request = new CriarUsuarioRequestDto();
			request.setNome(faker.name().firstName() + " " + faker.name().lastName());
			request.setEmail(faker.internet().emailAddress());
			request.setSenha("@Teste2025");
			
			//Act (executar o endpoint da API)
			var result = mockMvc.perform(
							post("/api/v1/usuarios/criar") //endpoint da api
							.contentType("application/json") //formato de dados
							.content(objectMapper.writeValueAsString(request)) //dados
						).andReturn();
							
			//Assert (verificar o resultado obtido)
			assertEquals(200, result.getResponse().getStatus());
			
			//armazenar o email que foi cadastrado
			emailUsuario = request.getEmail();
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
			
			//Arrange (criar os dados da requisição do teste)
			var request = new CriarUsuarioRequestDto();
			request.setNome(null); //vazio
			request.setEmail(null); //vazio
			request.setSenha(null); //vazio
			
			//Act (executar o endpoint da API)
			var result = mockMvc.perform(
							post("/api/v1/usuarios/criar") //endpoint da api
							.contentType("application/json") //formato de dados
							.content(objectMapper.writeValueAsString(request)) //dados
						).andReturn();
							
			//Assert (verificar o resultado obtido)
			assertEquals(400, result.getResponse().getStatus());
						
			assertTrue(result.getResponse().getContentAsString().contains("O nome do usuário é obrigatório."));
			assertTrue(result.getResponse().getContentAsString().contains("O email do usuário é obrigatório."));
			assertTrue(result.getResponse().getContentAsString().contains("A senha do usuário é obrigatório."));
		}
		catch(Exception e) {
			fail("Teste falhou: " + e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Deve validar senha forte.")
	@Order(3)
	public void validarSenhaForte() {
		try {
			
			var faker = new Faker(Locale.of("pt-BR"));
			
			//Arrange (criar os dados da requisição do teste)
			var request = new CriarUsuarioRequestDto();
			request.setNome(faker.name().firstName() + " " + faker.name().lastName());
			request.setEmail(faker.internet().emailAddress());
			request.setSenha("cotiinformatica"); //SENHA FRACA
			
			//Act (executar o endpoint da API)
			var result = mockMvc.perform(
							post("/api/v1/usuarios/criar") //endpoint da api
							.contentType("application/json") //formato de dados
							.content(objectMapper.writeValueAsString(request)) //dados
						).andReturn();
							
			//Assert (verificar o resultado obtido)
			assertEquals(400, result.getResponse().getStatus());
						
			assertTrue(result.getResponse().getContentAsString().contains("A senha deve ter pelo menos uma letra maiúscula, uma letra minúscula, um número e um símbolo especial e pelo menos 8 caracteres."));
		}
		catch(Exception e) {
			fail("Teste falhou: " + e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Não deve permitir o cadastro de emails iguais para o usuário.")
	@Order(4)
	public void verificarEmailJaCadastrado() {
		try {
			
			//Instanciando a biblioteca Java Faker
			var faker = new Faker(Locale.of("pt-BR"));
			
			//Arrange (criar os dados da requisição do teste)
			var request = new CriarUsuarioRequestDto();
			request.setNome(faker.name().firstName() + " " + faker.name().lastName());
			request.setEmail(emailUsuario); //email que foi cadastrado no primeiro teste!
			request.setSenha("@Teste2025");
			
			//Act (executar o endpoint da API)
			var result = mockMvc.perform(
							post("/api/v1/usuarios/criar") //endpoint da api
							.contentType("application/json") //formato de dados
							.content(objectMapper.writeValueAsString(request)) //dados
						).andReturn();
							
			//Assert (verificar o resultado obtido)
			assertEquals(409, result.getResponse().getStatus());
			
			assertTrue(result.getResponse().getContentAsString().contains("O email informado já está cadastrado. Tente outro."));
		}
		catch(Exception e) {
			fail("Teste falhou: " + e.getMessage());
		}
	}
}