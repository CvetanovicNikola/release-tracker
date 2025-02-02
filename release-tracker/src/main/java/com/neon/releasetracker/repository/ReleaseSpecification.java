package com.neon.releasetracker.repository;

import com.neon.releasetracker.entity.Release;
import com.neon.releasetracker.enums.Status;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ReleaseSpecification {
    private static final String STATUS = "status";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String RELEASE_DATE = "releaseDate";
    public static Specification<Release> filterByParam(Status status,
                                                       String name,
                                                       String description,
                                                       LocalDate releaseDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(status != null)
                predicates.add(criteriaBuilder.equal(root.get(STATUS), status));

            if(name != null)
                predicates.add(criteriaBuilder.equal(root.get(NAME), "%" + name + "%"));

            if(description != null)
                predicates.add(criteriaBuilder.equal(root.get(DESCRIPTION), "%" + description + "%"));

            if(releaseDate != null)
                predicates.add(criteriaBuilder.equal(root.get(RELEASE_DATE), releaseDate));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
