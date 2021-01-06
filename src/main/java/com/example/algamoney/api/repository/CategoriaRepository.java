package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Categoria;

// interface criada extendendo jpa repository
// metodos padrões já criados
// sempre importando a dependecia dele no pom.xml

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	
}
