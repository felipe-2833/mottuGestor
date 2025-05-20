package br.com.fiap.mottuGestor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.fiap.mottuGestor.model.Moviment;

public interface MovimentRepository extends JpaRepository<Moviment, Long>, JpaSpecificationExecutor<Moviment>{
    
}
