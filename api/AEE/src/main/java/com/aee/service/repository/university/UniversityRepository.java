package com.aee.service.repository.university;

import com.aee.service.models.university.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University,Long>, JpaSpecificationExecutor<University> {
    Page<University> findAllByTagName(String tag, Pageable pageable);
    List<University> findAllByNameContaining(String name);
}
