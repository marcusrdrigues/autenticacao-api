package br.com.cotiinformatica.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

	/*
	 * Método escrito com JPQL para que o Spring Data possa
	 * consultar 1 usuário no banco de dados baseado no email
	 */
	@Query("""
			SELECT u FROM Usuario u
			WHERE u.email = :pEmail
			""")
	Usuario find(@Param("pEmail") String email);
	
	/*
	 *	Método escrito com JPQL para que o Spring Data possa
	 *	consultar 1 usuário no banco de dados baseado no email e na senha 
	 */
	@Query("""
			SELECT u FROM Usuario u
			WHERE u.email = :pEmail AND u.senha = :pSenha
			""")
	Usuario find(@Param("pEmail") String email, @Param("pSenha") String senha);	
}






