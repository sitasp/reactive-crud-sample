package com.sage.reactive_crud_sample.repository;

import com.sage.reactive_crud_sample.entity.Tutorial;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TutorialRepository extends R2dbcRepository<Tutorial, Long> {
    @Query("SELECT * FROM tutorial t WHERE lower(t.title) LIKE CONCAT('%', lower(:keyword), '%') OR lower(t.description) LIKE CONCAT('%', lower(:keyword), '%')")
    Flux<Tutorial> findByKeywordIgnoreCase(String keyword);

    Flux<Tutorial> findByPublished(boolean isPublished);
}
