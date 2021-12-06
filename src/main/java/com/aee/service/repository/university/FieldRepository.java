package com.aee.service.repository.university;

import com.aee.service.models.university.Field;
import com.aee.service.models.university.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field,Long>, JpaSpecificationExecutor<University> {
    Optional<List<Field>> findAllByUniversityId(long id);
}
