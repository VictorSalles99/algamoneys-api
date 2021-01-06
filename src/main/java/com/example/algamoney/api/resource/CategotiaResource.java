package com.example.algamoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

//essa classe é um controller

@RestController
@RequestMapping("/categorias")
public class CategotiaResource {

	//injeção de dependencia
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//criando o metodo de buscar todas categorias
	@GetMapping
	public List<Categoria> buscarTodos() {
		return categoriaRepository.findAll();
	}
}
