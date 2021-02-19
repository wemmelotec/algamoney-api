package com.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algamoney.api.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	/* A interface JpaRepository me oferece vários métodos para manipular meus objetos no banco.
	 * Quem vai implementar essa interface é o SpringBoot DataJpa.
	 * Eu consigo realizar todas as operações de CRUD (create, retrieve(consulta), update, delete) no meu banco.
	 * */

}
