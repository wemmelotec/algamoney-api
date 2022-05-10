package com.algamoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lancamento")
public class Lancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotNull
	private String descricao;

	@NotNull
	@Column(name = "data_vencimento")
	// @JsonFormat(pattern = "git yyyy/MM/dd")//essa notação força a entidade a
	// retorna a data neste formato
	// @DateTimeFormat(pattern = "yyyy/MM/dd")//essa notação força a entidade a
	// retorna a data neste formato
	private LocalDate dataVencimento;

	@Column(name = "data_pagamento")
	// @JsonFormat(pattern = "yyyy/MM/dd")//essa notação força a entidade a retorna
	// a data neste formato
	private LocalDate dataPagamento;

	@NotNull
	private BigDecimal valor;

	private String observacao;

	@NotNull
	@Enumerated(EnumType.STRING) // Esse tipo será um ENUM logo eu tenho que criar uma classe para ele
	private TipoLancamento tipo;

	/*
	 * Agora serão os relacionamentos, o lançamento tem uma categoria (*ToOne) e o
	 * lançamento tem uma pessoa (*ToOne) Mas uma categoria pode estar em vários
	 * lançamentos (ManyToOne) Mas uma pessoa pode estar em vários lançamentos
	 * (ManyToOne)
	 */
	// notação para o relacionamento

	@ManyToOne
	@JoinColumn(name = "codigo_categoria") // aponta a coluna que faz esse relacionamento no banco
	@NotNull
	private Categoria codigoCategoria;
	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	@NotNull
	private Pessoa codigoPessoa;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public Categoria getCodigoCategoria() {
		return codigoCategoria;
	}

	public void setCodigoCategoria(Categoria codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}

	public Pessoa getCodigoPessoa() {
		return codigoPessoa;
	}

	public void setCodigoPessoa(Pessoa codigoPessoa) {
		this.codigoPessoa = codigoPessoa;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamento other = (Lancamento) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}
}
