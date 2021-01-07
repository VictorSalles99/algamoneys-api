package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

//essa classe é um controller
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	// injeção de dependencia
	@Autowired
	private CategoriaRepository categoriaRepository;

	// criando o metodo de buscar todas categorias
	@GetMapping
	public List<Categoria> buscarTodos() {
		return categoriaRepository.findAll();
	}

	// Metodo criado para poder manupilar o status de retorno
	//	@GetMapping()
	//	public ResponseEntity<?> buscarTodosTrocandoStatus() {
	//		List<Categoria> categoria = categoriaRepository.findAll();
	//		return !categoria.isEmpty() ? ResponseEntity.ok(categoria) : ResponseEntity.noContent().build();
	//	}

	@PostMapping
	//	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<Categoria> criarCategoria(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);

		// utilizar ServletUriComponentsBuilder que é um metodo usado para tratativas
		// com uri atual
		// como o nome ja diz fromCurrentRequestUri - a partir da uri da requisição
		// atual
		// adicionar o codigo ("path(/{codigo})") na uri
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(categoriaSalva.getCodigo()).toUri();

		// setar a chave location no headers
		response.setHeader("Location", uri.toASCIIString());

		// caso o retorno (se houver) estiver informando o status (created) podemos
		// remover a anotação responseStaus (linha 47)
		return ResponseEntity.created(uri).body(categoriaSalva);
	}

	// verificação de conteudo e manipulação de status http (ispresent)
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarCategoriaPeloCodigo(@PathVariable Long codigo) {
		Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		return categoria.isEmpty() ? ResponseEntity.ok(categoria.orElse(null)) : ResponseEntity.notFound().build();
	}
	
	//segunda forma de fazer a verificação de retorno (map)
	//	@GetMapping("/{codigo}")
	//	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
	//	  return this.categoriaRepository.findById(codigo)
	//	      .map(categoria -> ResponseEntity.ok(categoria))
	//	      .orElse(ResponseEntity.notFound().build());
	//	}
}
