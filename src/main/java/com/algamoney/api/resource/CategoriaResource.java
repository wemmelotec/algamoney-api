package com.algamoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;

//Essa é a classe que vai expor tudo que estiver relacionado ao recurso Categoria

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired//para injetar a interface
	private CategoriaRepository categoriaRepository;
	
	@GetMapping//mapear para receber requisições do tipo get em categorias
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}

}
