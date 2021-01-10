package com.example.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//o controlleradvice é um observador que verifica toda a aplicação gerenciando exceptions
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Erro> erros = criarListaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ EmptyResultDataAccessException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {

		final String msgUser = messageSource.getMessage("recurso.not-found", null, LocaleContextHolder.getLocale());
		final String msgDev = ex.toString();

		List<Erro> erro = Arrays.asList(new Erro(msgUser, msgDev));
		return handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,  WebRequest request){
		
		String msgUser = messageSource.getMessage("data.invalid", null, LocaleContextHolder.getLocale());
		String msgDev = ex.getRootCause().toString();// Optional.ofNullable(ex.getCause()).orElse(ex).toString();
		
		List<Erro> erro = Arrays.asList(new Erro(msgUser, msgDev));
		return handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Erro> criarListaDeErros(BindingResult res) {
		List<Erro> erros = new ArrayList<>();

		for (FieldError flde : res.getFieldErrors()) {
			String retornoDeErroUsuario = messageSource.getMessage(flde, LocaleContextHolder.getLocale());
			String retornoDeErroDev = flde.toString();
			erros.add(new Erro(retornoDeErroUsuario, retornoDeErroDev));
		}

		return erros;
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		// pego o valor da mensagem de erro informada no arquivo de definições de
		// mensagens para tratativas
		String retornoDeErroUsuario = messageSource.getMessage("mensagem.invalida", null,
				LocaleContextHolder.getLocale());

		// pego a causa da exeção (ex.getCause())
		String retornoDeErroDev = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		return handleExceptionInternal(ex, new Erro(retornoDeErroUsuario, retornoDeErroDev), headers,
				HttpStatus.BAD_REQUEST, request);
	}

	// Essa nova classe foi gerada para retornar as mensagens do user e do dev
	// juntamente
	// sem necessidade de concatenar deixando o codigo melhor extruturado

	// cria a classe PUBLICA
	public static class Erro {
		// definice as strings em questão
		String retornoDeErroUsuario;
		String retornoDeErroDev;

		// cria o metodo PUBLICO
		public Erro(String retornoDeErroUsuario, String retornoDeErroDev) {
			// Retorno os dados solicitados (no caso os valores das mensagens)
			super();
			this.retornoDeErroUsuario = retornoDeErroUsuario;
			this.retornoDeErroDev = retornoDeErroDev;
		}

		// GET
		public String getRetornoDeErroUsuario() {
			return retornoDeErroUsuario;
		}

		// GET
		public String getRetornoDeErroDev() {
			return retornoDeErroDev;
		}
	}
}
