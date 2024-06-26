package com.sage.reactive_crud_sample.service;

import com.sage.reactive_crud_sample.entity.Tutorial;
import com.sage.reactive_crud_sample.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class TutorialServiceImpl implements ITutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Override
    public Flux<Tutorial> findAll(){
        return tutorialRepository.findAll();
    }

    @Override
    public Mono<Tutorial> findById(long id){
        return tutorialRepository.findById(id);
    }

    @Override
    public Mono<Tutorial> save(Tutorial tutorial){
        return tutorialRepository.save(tutorial);
    }

    @Override
    public Flux<Tutorial> findByKeywordIgnoreCase(String keyword){
        return tutorialRepository.findByKeywordIgnoreCase(keyword);
    }

    @Override
    public Mono<Tutorial> update(long id, Tutorial tutorial){
        return tutorialRepository.findById(id)
                .map(Optional::of)
                .flatMap(optionalTutorial -> {
                    if(optionalTutorial.isPresent()){
                        tutorial.setId(id);
                        return tutorialRepository.save(tutorial);
                    }

                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> deleteById(long id){
        return tutorialRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAll(){
        return tutorialRepository.deleteAll();
    }

    @Override
    public Flux<Tutorial> findByPublished(boolean isPublished){
        return tutorialRepository.findByPublished(isPublished);
    }

    @Override
    public Mono<Tutorial> create(Tutorial tutorial) {
        return save(tutorial);
    }

    @Override
    public Flux<Tutorial> bulkCreate(List<Tutorial> tutorialList) {
        return Flux.fromIterable(tutorialList)
                .flatMap(tutorialRepository::save);
    }
}
