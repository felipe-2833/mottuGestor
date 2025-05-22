package br.com.fiap.mottuGestor.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import br.com.fiap.mottuGestor.model.User;
import br.com.fiap.mottuGestor.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    @Cacheable("users")
    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha na validação dos filtros ou parâmetros"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
},description = "Listar users", tags = "users", summary = "Lista de users")
    public Page<User> index(
        @ParameterObject @PageableDefault(sort = "nome", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Buscando users");
        return repository.findAll(pageable);
    }

    @PostMapping
    @CacheEvict(value = "users", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(responses = {
            @ApiResponse(responseCode = "400", description = "Falha na validação")
    }, description = "Cadastrar user", tags = "users", summary = "Cadastrar user")
    public User create(@RequestBody @Valid User user) {
        log.info("Cadastrando user " + user.getNome());
        return repository.save(user);
    }

    @GetMapping("{id_user}")
    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
},description = "Listar user pelo id", tags = "users", summary = "Listar user pelo id")
    public User get(@PathVariable Long id_user) {
        log.info("Buscando user " + id_user);
        return getUser(id_user);
    }

    @DeleteMapping("{id_user}")
    @Operation(responses = {
        @ApiResponse(responseCode = "204", description = "Registro removido com sucesso"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
},description = "Deletar user pelo id", tags = "users", summary = "Deletar user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id_user) {
        log.info("Apagando user " + id_user);
        repository.delete(getUser(id_user));
    }

    @PutMapping("{id_user}")
    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Falha na validação dos dados"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
},description = "Update user pelo id", tags = "users", summary = "Update user pelo id")
    public User update(@PathVariable Long id_user, @RequestBody @Valid User user) {
        log.info("Atualizando user " + id_user + " " + user);
        getUser(id_user);
        user.setId_user(id_user);
        return repository.save(user);
    }

    private User getUser(Long id_user) {
        return repository.findById(id_user)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "user não encontrado"));
    }
}
