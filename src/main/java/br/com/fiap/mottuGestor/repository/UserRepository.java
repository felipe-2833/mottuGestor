package br.com.fiap.mottuGestor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.mottuGestor.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
