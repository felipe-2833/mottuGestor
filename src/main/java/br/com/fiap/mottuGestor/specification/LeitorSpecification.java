package br.com.fiap.mottuGestor.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottuGestor.controller.LeitorController;
import br.com.fiap.mottuGestor.model.Leitor;
import jakarta.persistence.criteria.Predicate;

public class LeitorSpecification {
    public static Specification<Leitor> withFilters(LeitorController.LeitorFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.nome() != null && !filter.nome().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
