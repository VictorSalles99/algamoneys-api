package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoEvents;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@GetMapping
	public List<Lancamento> buscarTodosLancamentos(){
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarLancamentoPorCodigo(@PathVariable Long codigo){
		Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(codigo);
		return !lancamentoEncontrado.isEmpty() ? ResponseEntity.ok(lancamentoEncontrado.orElse(null)) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> cadastrarLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		publisher.publishEvent(new RecursoEvents(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
}
