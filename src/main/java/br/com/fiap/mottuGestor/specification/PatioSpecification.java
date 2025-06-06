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

            if (filter.capacidade() != null && filter.capacidade() > 0.0 && filter.capacidade() <= 10000.0) {
                predicates.add(cb.between(root.get("capacidade"), 0.0, filter.capacidade()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
