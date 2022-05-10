package com.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Lancamento;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.LancamentoRepository;
import com.algamoney.api.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	@Autowired
	private LancamentoRepository lancamentoRepository;
	@Autowired
	private LancamentoService lancamentoService;
	@Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private MessageSource messageSource;

	@GetMapping
	// este método vai receber o filtro e vamos implementar a pesquisa na query
	// para acrescentar a páginação eu tive que receber mais um parâmetro o Pageable
	//public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
	//	return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	//}
	public ResponseEntity<?> listar(){
		List<Lancamento> lancamentos = lancamentoRepository.findAll();
		return !lancamentos.isEmpty() ? ResponseEntity.ok(lancamentos) : ResponseEntity.noContent().build();
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		return lancamentoService.buscarPeloCodigo(codigo);
	}
	
	@PostMapping
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
        //essa linha vai impedir que o usuário coloque uma pessoa inativa em lancamento (3 versão da método)
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
        return  ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }
}