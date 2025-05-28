package com.generation.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.farmacia.Model.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	List<Categoria> findAllByDescricaoContainingIgnoreCase(String descricao);
}