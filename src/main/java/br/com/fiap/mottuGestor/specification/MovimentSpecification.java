package br.com.fiap.mottuGestor.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.mottuGestor.controller.MovimentController;
import br.com.fiap.mottuGestor.model.Moviment;
import jakarta.persistence.criteria.Predicate;

public class MovimentSpecification {
    public static Specification<Moviment> withFilters(MovimentController.MovimentFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filter.startDate() != null && filter.endDate() != null) {
                predicates.add(
                        cb.between(root.get("date"), filter.startDate(), filter.endDate()));
            }

            if (filter.startDate() != null && filter.endDate() == null) {
                predicates.add(cb.equal(root.get("date"), filter.startDate()));
            }

            if (filter.movimentType() != null) {
                predicates.add(cb.equal(root.get("movimentType"), filter.movimentType()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
