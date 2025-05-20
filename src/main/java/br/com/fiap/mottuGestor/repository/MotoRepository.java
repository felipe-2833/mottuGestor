package br.com.fiap.mottuGestor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottuGestor.model.Moto;

public interface MotoRepository extends JpaRepository<Moto, Long>{
    
}
