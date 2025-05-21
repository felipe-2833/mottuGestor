package br.com.fiap.mottuGestor.controller;

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

import br.com.fiap.mottuGestor.model.Patio;
import br.com.fiap.mottuGestor.repository.PatioRepository;
import br.com.fiap.mottuGestor.specification.PatioSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/patios")
@Slf4j
public class PatioController {
    public record patioFilter(String nome, String endereco, Double capacidade) {
    }

    @Autowired
    private PatioRepository repository;

    @GetMapping
    @Cacheable("patios")
    @Operation(description = "Listar patios", tags = "patios", summary = "Lista de patios")
    public Page<Patio> index(patioFilter filter,
            @PageableDefault(size = 5, sort = "nome", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Buscando patios com filtro", filter.nome(), filter.endereco(), filter.capacidade());
        var specification = PatioSpecification.withFilters(filter);
        return repository.findAll(specification, pageable);
    }

    @PostMapping
    @CacheEvict(value = "patios", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(responses = {
            @ApiResponse(responseCode = "400", description = "Falha na validação")
    },description = "Cadastrar patios", tags = "patios", summary = "Cadastrar de patios")
    public Patio create(@RequestBody @Valid Patio patio) {
        log.info("Cadastrando patio " + patio.getNome());
        return repository.save(patio);
    }

    @GetMapping("{id_patio}")
    @Operation(description = "Listar patio pelo id", tags = "patios", summary = "Listar patio pelo id")
    public Patio get(@PathVariable Long id_patio) {
        log.info("Buscando patio " + id_patio);
        return getPatio(id_patio);
    }

    @DeleteMapping("{id_patio}")
    @Operation(description = "Deletar patio pelo id", tags = "patios", summary = "Deletar patio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id_patio) {
        log.info("Apagando patio " + id_patio);
        repository.delete(getPatio(id_patio));
    }

    @PutMapping("{id_patio}")
    @Operation(description = "Update patio pelo id", tags = "patios", summary = "update patio")
    public Patio update(@PathVariable Long id_patio, @RequestBody @Valid Patio patio) {
        log.info("Atualizando patio " + id_patio + " " + patio);
        getPatio(id_patio);
        patio.setId_patio(id_patio);
        return repository.save(patio);
    }

    private Patio getPatio(Long id_patio) {
        return repository.findById(id_patio)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "patio não encontrado"));
    }
}
