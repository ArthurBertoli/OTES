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
				: builder.like(builder.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%");
	}

	public static Specification<Transacao> monthEquals(Integer mes) {
		return (root, query, builder) -> mes == null ? builder.conjunction() : builder.equal(builder.function("strftime", String.class, builder.literal("%m"), root.get("dataCadastro")), String.format("%02d", mes));
	}

	public static Specification<Transacao> yearEquals(Integer ano) {
		return (root, query, builder) -> ano == null ? builder.conjunction() : builder.equal(builder.function("strftime", String.class, builder.literal("%Y"), root.get("dataCadastro")), String.valueOf(ano));
	}

	public static Specification<Transacao> dateBetween(LocalDate startInclusive, LocalDate endInclusive) {
		return (root, query, builder) -> builder.between(root.get("dataCadastro"), startInclusive, endInclusive);
	}
}