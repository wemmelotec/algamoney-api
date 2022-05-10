package com.algamoney.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.algamoney.api.model.Lancamento;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.LancamentoRepository;
import com.algamoney.api.repository.PessoaRepository;
import com.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

import java.util.Optional;
@Service
public class LancamentoService {
	@Autowired
	private LancamentoRepository lancamentoRepository;
	@Autowired
	private PessoaRepository pessoaRepository;

	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		return lancamentoRepository.findById(codigo).map(lancamento -> ResponseEntity.ok(lancamento))
				.orElse(ResponseEntity.notFound().build());
	}
	
	public Lancamento salvar(Lancamento lancamento) {
        //Essa linha vai buscar a pessoa que veio no lancamento
        Optional<Pessoa> pessoa = this.pessoaRepository.findById(lancamento.getCodigoPessoa().getCodigo());
        //Esse if faz a verificação se o atributo ativo em pessoa esta true
        if(!pessoa.isPresent() || pessoa.get().isAtivo() ){
            throw new PessoaInexistenteOuInativaException(); //essa linha lança a exceção que vou tratar em LancamentoResource
        }
        //Se tudo estiver ok, essa linha salva o objeto e retorna para o método na classe LancamentoResource
        return this.lancamentoRepository.save(lancamento);
    }
}
