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

import br.com.fiap.mottuGestor.model.Leitor;
import br.com.fiap.mottuGestor.repository.LeitorRepository;
import br.com.fiap.mottuGestor.specification.LeitorSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/leitores")
@Slf4j
public class LeitorController {
     public record LeitorFilter(String nome) {
    }

    @Autowired
    private LeitorRepository repository;

    @GetMapping
    @Cacheable("leitores")
    @Operation(description = "Listar leitores", tags = "leitores", summary = "Lista de leitores")
    public Page<Leitor> index(LeitorFilter filter,
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Buscando leitores com filtro", filter.nome());
        var specification = LeitorSpecification.withFilters(filter);
        return repository.findAll(specification, pageable);
    }

    @PostMapping
    @CacheEvict(value = "leitores", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(responses = {
            @ApiResponse(responseCode = "400", description = "Falha na validação")
    })
    public Leitor create(@RequestBody @Valid Leitor leitor) {
        log.info("Cadastrando leitor " + leitor.getNome());
        return repository.save(leitor);
    }

    @GetMapping("{id_leitor}")
    @Operation(description = "Listar leitor pelo id", tags = "leitores", summary = "Listar de leitor pelo id")
    public Leitor get(@PathVariable Long id_leitor) {
        log.info("Buscando leitor " + id_leitor);
        return getLeitor(id_leitor);
    }

    @DeleteMapping("{id_leitor}")
    @Operation(description = "Deletar leitor pelo id", tags = "leitores", summary = "Deletar leitor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id_leitor) {
        log.info("Apagando leitor " + id_leitor);
        repository.delete(getLeitor(id_leitor));
    }

    @PutMapping("{id_leitor}")
    public Leitor update(@PathVariable Long id_leitor, @RequestBody @Valid Leitor leitor) {
        log.info("Atualizando leitor " + id_leitor + " " + leitor);
        getLeitor(id_leitor);
        leitor.setId_leitor(id_leitor);
        return repository.save(leitor);
    }

    private Leitor getLeitor(Long id_leitor) {
        return repository.findById(id_leitor)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "leitor não encontrado"));
    }
}
