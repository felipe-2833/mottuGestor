package br.com.fiap.mottuGestor.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottuGestor.controller.PatioController;
import br.com.fiap.mottuGestor.model.Patio;
import jakarta.persistence.criteria.Predicate;

public class PatioSpecification {
    public static Specification<Patio> withFilters(PatioController.patioFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.nome() != null && !filter.nome().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            if (filter.endereco() != null && !filter.endereco().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("endereco")), "%" + filter.endereco().toLowerCase() + "%"));
            }

            if (filter.capacidade() != 0 && filter.capacidade() > 0 && filter.capacidade() <= 10000) {
                predicates.add(cb.equal(root.get("capacidade"), filter.capacidade()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
