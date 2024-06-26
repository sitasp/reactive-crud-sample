package com.sage.reactive_crud_sample.repository;

import com.sage.reactive_crud_sample.entity.Tutorial;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TutorialRepository extends R2dbcRepository<Tutorial, Long> {
    Flux<Tutorial> findByTitleContaining(String title);
    Flux<Tutorial> findByPublished(boolean isPublished);
}
