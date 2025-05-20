package br.com.fiap.mottuGestor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottuGestor.model.Leitor;

public interface LeitorRepository extends JpaRepository<Leitor, Long>{
    
}
