package com.aee.service.repository.university;

import com.aee.service.models.university.University;
import org.springframework.data.jpa.domain.Specification;

public class UniversitySpecification {
    public static Specification<University> inZone(String zone) {
        return (university, cq, cb) -> cb.equal(university.get("zone"), zone);
    }
    public static Specification<University> hasName(String name) {
        return (university, cq, cb) -> cb.like(university.get("name"), name);
    }
}
