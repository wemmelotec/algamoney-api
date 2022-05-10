package com.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa atualizar(Long codigo, Pessoa pessoa){
    	Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);
		if(pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		//agora preciso copiar as propriedades do objeto pessoa que eu recebi para pessoaSalva
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");//vou copiar de pessoa para pessoaSalva ignorando o codigo
		//agora salvo a pessoa no banco
		pessoaRepository.save(pessoaSalva);
		//e depois retorno a pessoaSalva
		return pessoaRepository.save(pessoaSalva);
        }

    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
        //primeiro, buscar a pessoa
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
        //segundo, agora que eu tenho a pessoa eu dou um set na propriedade ativo
        pessoaSalva.setAtivo(ativo);
        //terceito, eu passo a pessoaSalva para o banco de dados
        pessoaRepository.save(pessoaSalva);
    }


    /*
    Para não duplicar o código, vou refatorar o método buscar pessoa pelo  código utilizado nos dois métodos acima
    É bom deixar os métodos privados da classe no fim
     */

    public Pessoa buscarPessoaPeloCodigo(Long codigo) {
        return this.pessoaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}
