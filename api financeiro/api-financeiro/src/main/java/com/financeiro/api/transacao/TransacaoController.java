package com.financeiro.api.transacao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financeiro.api.transacao.dto.ResumoTransacoesResponse;
import com.financeiro.api.transacao.dto.TransacaoRequest;
import com.financeiro.api.transacao.dto.TransacaoResponse;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/transacoes")
public class TransacaoController {
	private final TransacaoService service;

	public TransacaoController(TransacaoService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<TransacaoResponse> criar(@Valid @RequestBody TransacaoRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
	}

	@GetMapping
	public ResumoTransacoesResponse listar(
			@RequestParam(required = false) TipoTransacao tipo,
			@RequestParam(required = false) String categoria,
			@RequestParam(required = false) Integer mes,
			@RequestParam(required = false) Integer ano) {
		return service.listar(tipo, categoria, mes, ano);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
}