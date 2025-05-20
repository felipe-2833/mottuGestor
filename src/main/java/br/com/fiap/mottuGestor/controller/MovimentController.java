package br.com.fiap.mottuGestor.controller;

import java.time.LocalDate;

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

import br.com.fiap.mottuGestor.model.Moviment;
import br.com.fiap.mottuGestor.model.MovimentType;
import br.com.fiap.mottuGestor.repository.MovimentRepository;
import br.com.fiap.mottuGestor.specification.MovimentSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/moviments")
@Slf4j
public class MovimentController {
     public record MovimentFilter(LocalDate startDate, LocalDate endDate, MovimentType movimentType) {
    }

    @Autowired
    private MovimentRepository repository;

    @GetMapping
    @Cacheable("moviments")
    @Operation(description = "Listar moviments", tags = "moviments", summary = "Lista de moviments")
    public Page<Moviment> index(MovimentFilter filter,
            @PageableDefault(size = 5, sort = "movimentType", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Buscando moviments com filtro", filter.movimentType());
        var specification = MovimentSpecification.withFilters(filter);
        return repository.findAll(specification, pageable);
    }

    @PostMapping
    @CacheEvict(value = "moviments", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(responses = {
            @ApiResponse(responseCode = "400", description = "Falha na validação")
    })
    public Moviment create(@RequestBody @Valid Moviment moviment) {
        log.info("Cadastrando moviment " + moviment.getId_moviment());
        return repository.save(moviment);
    }

    @GetMapping("{id_moviment}")
    @Operation(description = "Listar moviment pelo id", tags = "moviments", summary = "Listar de moviment pelo id")
    public Moviment get(@PathVariable Long id_moviment) {
        log.info("Buscando moviment " + id_moviment);
        return getMoviment(id_moviment);
    }

    @DeleteMapping("{id_moviment}")
    @Operation(description = "Deletar moviment pelo id", tags = "moviments", summary = "Deletar moviment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id_moviment) {
        log.info("Apagando moviment " + id_moviment);
        repository.delete(getMoviment(id_moviment));
    }

    @PutMapping("{id_moviment}")
    public Moviment update(@PathVariable Long id_moviment, @RequestBody @Valid Moviment moviment) {
        log.info("Atualizando moviment " + id_moviment + " " + moviment);
        getMoviment(id_moviment);
        moviment.setId_moviment(id_moviment);
        return repository.save(moviment);
    }

    private Moviment getMoviment(Long id_moviment) {
        return repository.findById(id_moviment)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "moviment não encontrada"));
    }

}
