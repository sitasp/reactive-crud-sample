package com.sage.reactive_crud_sample.service;

import com.sage.reactive_crud_sample.entity.Tutorial;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ITutorialService {
    Flux<Tutorial> findAll();

    Mono<Tutorial> findById(long id);

    Mono<Tutorial> save(Tutorial tutorial);

    Flux<Tutorial> findByTitleContaining(String title);

    Mono<Tutorial> update(long id, Tutorial tutorial);

    Mono<Void> deleteById(long id);

    Mono<Void> deleteAll();

    Flux<Tutorial> findByPublished(boolean isPublished);

    Mono<Tutorial> create(Tutorial tutorial);
}
