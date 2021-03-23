package com.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoney.api.model.Categoria;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;

@RestController
@RequestMapping("pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@GetMapping
	public ResponseEntity<?> listar(){
		List<Pessoa> pessoas = pessoaRepository.findAll();
		return !pessoas.isEmpty() ? ResponseEntity.ok(pessoas) : ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> salvar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		 Pessoa pessoaSalva =  pessoaRepository.save(pessoa);
		 
		 URI uri =  ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(pessoa.getCodigo()).toUri();
		 response.setHeader("Location", uri.toASCIIString());
		 
		 return ResponseEntity.created(uri).body(pessoaSalva);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);
		if(pessoaSalva != null) {
			return ResponseEntity.ok(pessoaSalva);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
