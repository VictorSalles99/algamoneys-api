package com.example.algamoney.api.resource;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoEvents;
import com.example.algamoney.api.exceptionhandler.ResponseExceptionHandler.Erro;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.service.LancamentoService;
import com.example.algamoney.api.service.exception.PessoaInativaException;
import com.example.algamoney.api.service.exception.PessoaInexistenteException;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	LancamentoRepository lancamentoRepository;

	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter) {
		return lancamentoRepository.filtrar(lancamentoFilter);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarLancamentoPorCodigo(@PathVariable Long codigo) {
		Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(codigo);
		return !lancamentoEncontrado.isEmpty() ? ResponseEntity.ok(lancamentoEncontrado.orElse(null))
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> cadastrarLancamento(@Valid @RequestBody Lancamento lancamento,
			HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoEvents(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@ExceptionHandler({ PessoaInativaException.class })
	public ResponseEntity<?> handlePessoaInexistenteOuInativaException(PessoaInativaException ex) {
		
		String msgUser = messageSource.getMessage("pessoa.inativa", null, LocaleContextHolder.getLocale());
		String msgDev = ex.toString();
		List<Erro> erro = Arrays.asList(new Erro(msgUser, msgDev));
		return ResponseEntity.badRequest().body(erro);
	}
	
	@ExceptionHandler({ PessoaInexistenteException.class })
	public ResponseEntity<?> handlePessoaInexistenteException(PessoaInexistenteException ex) {
		String msgUser = messageSource.getMessage("pessoa.inexistente", null, LocaleContextHolder.getLocale());
		String msgDev = ex.toString();
		List<Erro> erro = Arrays.asList(new Erro(msgUser, msgDev));
		return ResponseEntity.badRequest().body(erro);
	}
}
