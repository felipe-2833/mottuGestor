package br.com.fiap.mottuGestor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.fiap.mottuGestor.model.Patio;

public interface PatioRepository extends JpaRepository<Patio, Long>, JpaSpecificationExecutor<Patio>{
    
}
