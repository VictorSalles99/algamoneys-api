package com.example.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.exception.PessoaInativaException;
import com.example.algamoney.api.service.exception.PessoaInexistenteException;

@Service
public class LancamentoService {

	@Autowired
	LancamentoRepository lancamentoRepository;

	@Autowired
	PessoaRepository pessoaRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		if (pessoa.isEmpty()) {
			throw new PessoaInexistenteException();
		}
		if(pessoa.get().isInativo()) {
			throw new PessoaInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}
}
