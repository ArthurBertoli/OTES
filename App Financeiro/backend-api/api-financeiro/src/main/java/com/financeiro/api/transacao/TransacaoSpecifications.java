package com.financeiro.api.transacao;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

public final class TransacaoSpecifications {
	private TransacaoSpecifications() {
	}

	public static Specification<Transacao> tipoEquals(TipoTransacao tipo) {
		return (root, query, builder) -> tipo == null ? builder.conjunction() : builder.equal(root.get("tipo"), tipo);
	}

	public static Specification<Transacao> categoriaContains(String categoria) {
		return (root, query, builder) -> categoria == null || categoria.isBlank()
				? builder.conjunction()
				: builder.like(builder.lower(root.get("categoria")), "%" + categoria.trim().toLowerCase() + "%");
	}

	/**
	 * Filtra por intervalo de datas. O SQLite guarda a data como numero (milissegundos), entao
	 * funcoes de texto como strftime nao funcionam na coluna; comparar intervalos funciona,
	 * porque o parametro e convertido da mesma forma que o valor gravado.
	 */
	public static Specification<Transacao> dateBetween(LocalDate startInclusive, LocalDate endInclusive) {
		return (root, query, builder) -> startInclusive == null || endInclusive == null
				? builder.conjunction()
				: builder.between(root.get("dataCadastro"), startInclusive, endInclusive);
	}
}
