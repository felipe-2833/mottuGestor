package br.com.fiap.mottuGestor.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottuGestor.controller.MotoController.MotoFilter;
import br.com.fiap.mottuGestor.model.Moto;
import jakarta.persistence.criteria.Predicate;

public class MotoSpecification {
    public static Specification<Moto> withFilters(MotoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.placa() != null && !filter.placa().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("placa")), "%" + filter.placa().toLowerCase() + "%"));
            }

            if (filter.modelo() != null && !filter.modelo().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("modelo")), "%" + filter.modelo().toLowerCase() + "%"));
            }
            if (filter.servico() != null && !filter.servico().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("servico")), "%" + filter.servico().toLowerCase() + "%"));
            }
            
            if (filter.startDate() != null && filter.endDate() != null) {
                predicates.add(
                        cb.between(root.get("data_cadastro"), filter.startDate(), filter.endDate()));
            }

            if (filter.startDate() != null && filter.endDate() == null) {
                predicates.add(cb.equal(root.get("data_cadastro"), filter.startDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
