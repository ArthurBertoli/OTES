package com.financeiro.api.transacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.financeiro.api.shared.ResourceNotFoundException;
import com.financeiro.api.transacao.dto.ResumoTransacoesResponse;
import com.financeiro.api.transacao.dto.TransacaoRequest;
import com.financeiro.api.transacao.dto.TransacaoResponse;

@Service
public class TransacaoService {
	private final TransacaoRepository repository;

	public TransacaoService(TransacaoRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public TransacaoResponse criar(TransacaoRequest request) {
		Transacao transacao = new Transacao();
		transacao.setDescricao(request.getDescricao().trim());
		transacao.setValor(request.getValor().setScale(2, RoundingMode.HALF_UP));
		transacao.setTipo(request.getTipo());
		transacao.setCategoria(request.getCategoria().trim());
		transacao.setDataCadastro(request.getDataCadastro());
		return toResponse(repository.save(transacao));
	}

	@Transactional(readOnly = true)
	public ResumoTransacoesResponse listar(TipoTransacao tipo, String categoria, Integer mes, Integer ano) {
		List<TransacaoResponse> transacoes = repository.findAll(buildSpecification(tipo, categoria, mes, ano)).stream()
				.map(TransacaoService::toResponse)
				.toList();

		BigDecimal totalEntradas = transacoes.stream()
				.filter(transacao -> transacao.getTipo() == TipoTransacao.ENTRADA)
				.map(TransacaoResponse::getValor)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalSaidas = transacoes.stream()
				.filter(transacao -> transacao.getTipo() == TipoTransacao.SAIDA)
				.map(TransacaoResponse::getValor)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal saldoTotal = totalEntradas.subtract(totalSaidas);
		return new ResumoTransacoesResponse(saldoTotal, totalEntradas, totalSaidas, transacoes);
	}

	@Transactional
	public void excluir(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Transacao nao encontrada para o id " + id);
		}
		repository.deleteById(id);
	}

	private Specification<Transacao> buildSpecification(TipoTransacao tipo, String categoria, Integer mes, Integer ano) {
		if (mes != null && (mes < 1 || mes > 12)) {
			throw new IllegalArgumentException("mes deve estar entre 1 e 12");
		}
		if (ano != null && ano < 1) {
			throw new IllegalArgumentException("ano deve ser positivo");
		}
		return TransacaoSpecifications.tipoEquals(tipo)
				.and(TransacaoSpecifications.categoriaContains(categoria))
				.and(TransacaoSpecifications.monthEquals(mes))
				.and(TransacaoSpecifications.yearEquals(ano));
	}

	private static TransacaoResponse toResponse(Transacao transacao) {
		return new TransacaoResponse(
				transacao.getId(),
				transacao.getDescricao(),
				transacao.getValor(),
				transacao.getTipo(),
				transacao.getCategoria(),
				transacao.getDataCadastro());
	}
}