package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	PessoaRepository pessoaRepository;

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaEncontrada = pessoaRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(pessoa, pessoaEncontrada, "codigo");
		return pessoaRepository.save(pessoaEncontrada);
	}
}
