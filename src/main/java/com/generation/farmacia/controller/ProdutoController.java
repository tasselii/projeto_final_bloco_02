package com.generation.farmacia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {
		
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getAllbyNome(@PathVariable String nome) {
		
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
		
	}
	
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {

	    if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O categoria é obrigatório");
	    }

	    categoriaRepository.findById(produto.getCategoria().getId())
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O categoria não existe"));

	    Produto novaProduto = produtoRepository.save(produto);

	    return ResponseEntity.status(HttpStatus.CREATED).body(novaProduto);
	}

	
	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
	    if (produto.getId() == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID do produto é obrigatório");
	    }

	    if (produto.getCategoria() == null || produto.getCategoria().getId() == null || !categoriaRepository.existsById(produto.getCategoria().getId())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!");
	    }

	    return produtoRepository.findById(produto.getId())
	        .map(p -> ResponseEntity.ok(produtoRepository.save(produto)))
	        .orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		produtoRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		produtoRepository.deleteById(id);
		
	}
}
