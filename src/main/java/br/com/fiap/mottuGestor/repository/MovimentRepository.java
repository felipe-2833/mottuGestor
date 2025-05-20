package br.com.fiap.mottuGestor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottuGestor.model.Moviment;

public interface MovimentRepository extends JpaRepository<Moviment, Long>{
    
}
