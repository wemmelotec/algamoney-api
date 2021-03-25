package com.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;
import com.sun.el.stream.Optional;


//Essa é a classe que vai expor tudo que estiver relacionado ao recurso Categoria

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired//para injetar a interface
	private CategoriaRepository categoriaRepository;
	
	@Autowired//responsável por disparar o evento de recurso criado a partir desse objeto
	private ApplicationEventPublisher publisher;
	
	@GetMapping//mapear para receber requisições do tipo get em categorias
	public ResponseEntity<?> listar(){
		List<Categoria> categorias = categoriaRepository.findAll();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();//o build é necessário para gerar um response entity
	}
	
	
	@PostMapping
	public ResponseEntity<Categoria> salvar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		 Categoria categoriaSalva =  categoriaRepository.save(categoria);
		 
		 publisher.publishEvent(new RecursoCriadoEvent(this, response,categoriaSalva.getCodigo()));
		 return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria =   this.categoriaRepository.findById(codigo).orElse(null);
		if(categoria != null) {
			return ResponseEntity.ok(categoria);
		}else{
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		categoriaRepository.deleteById(codigo);
	}
	
}
