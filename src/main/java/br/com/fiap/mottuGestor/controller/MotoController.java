package br.com.fiap.mottuGestor.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import br.com.fiap.mottuGestor.model.Moto;
import br.com.fiap.mottuGestor.model.User;
import br.com.fiap.mottuGestor.repository.MotoRepository;
import br.com.fiap.mottuGestor.specification.MotoSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/motos")
@Slf4j
public class MotoController {

     public record MotoFilter(String placa, String modelo, String servico, LocalDate startDate, LocalDate endDate) {
    }

    @Autowired
    private MotoRepository repository;

    @GetMapping
    @Cacheable("motos")
    @Operation(description = "Listar motos", tags = "motos", summary = "Lista de motos")
    public Page<Moto> index(MotoFilter filter,
            @PageableDefault(size = 10, sort = "placa", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Buscando motos com filtro", filter.placa(), filter.modelo(), filter.servico());
        var specification = MotoSpecification.withFilters(filter);
        return repository.findAll(specification, pageable);
    }

    @PostMapping
    @CacheEvict(value = "motos", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(responses = {
            @ApiResponse(responseCode = "400", description = "Falha na validação")
    })
    public Moto create(@RequestBody @Valid Moto moto) {
        log.info("Cadastrando moto " + moto.getPlaca());
        return repository.save(moto);
    }

    @GetMapping("{id_moto}")
    @Operation(description = "Listar moto pelo id", tags = "motos", summary = "Listar de moto pelo id")
    public Moto get(@PathVariable Long id_moto) {
        log.info("Buscando moto " + id_moto);
        return getMoto(id_moto);
    }

    @DeleteMapping("{id_moto}")
    @Operation(description = "Deletar moto pelo id", tags = "motos", summary = "Deletar moto")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id_moto) {
        log.info("Apagando moto " + id_moto);
        repository.delete(getMoto(id_moto));
    }

    @PutMapping("{id_moto}")
    public Moto update(@PathVariable Long id_moto, @RequestBody @Valid Moto moto) {
        log.info("Atualizando moto " + id_moto + " " + moto);
        getMoto(id_moto);
        moto.setId_moto(id_moto);
        return repository.save(moto);
    }

    private Moto getMoto(Long id_moto) {
        return repository.findById(id_moto)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Moto não encontrada"));
    }



}
