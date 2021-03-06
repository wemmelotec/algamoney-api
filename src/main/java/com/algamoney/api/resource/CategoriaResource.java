package com.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;
import com.sun.el.stream.Optional;

//Essa é a classe que vai expor tudo que estiver relacionado ao recurso Categoria

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired//para injetar a interface
	private CategoriaRepository categoriaRepository;
	
	@GetMapping//mapear para receber requisições do tipo get em categorias
	//segunda versão do get que trata o recebimento de uma lista vazia
	public ResponseEntity<?> listar(){
		List<Categoria> categorias = categoriaRepository.findAll();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();//o build é necessário para gerar um response entity
	}
	/*
	 * primeira versão do get 
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	*/
	@PostMapping
	//@ResponseStatus(HttpStatus.CREATED)
	//segunda versão do post, já implementando Rest, que indica que após salvar um objeto precisa retornar o location através do header
	//e retorna também o recurso salvo
	public ResponseEntity<Categoria> salvar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		 Categoria categoriaSalva =  categoriaRepository.save(categoria);
		 
		 URI uri =  ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(categoria.getCodigo()).toUri();
		 response.setHeader("Location", uri.toASCIIString());
		 
		 return ResponseEntity.created(uri).body(categoriaSalva);
	}
	/*primeira versão do post
	 * 
	 public void salvar(@RequestBody Categoria categoria) {
	 categoriaRepository.save(categoria);
	}
	*/
	
	@GetMapping("/{codigo}")
	//segunda versão implementando o notFound
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria =   this.categoriaRepository.findById(codigo).orElse(null);
		if(categoria != null) {
			return ResponseEntity.ok(categoria);
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	/*
	 * primeira versão
	 public Categoria buscarPeloCodigo(@PathVariable Long codigo) {
		  return this.categoriaRepository.findById(codigo).orElse(null);
	}
	 */
}
