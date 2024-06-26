package com.sage.reactive_crud_sample.controller;

import com.sage.reactive_crud_sample.entity.Tutorial;
import com.sage.reactive_crud_sample.service.TutorialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/tutorial")
public class TutorialController {

    @Autowired
    private TutorialServiceImpl tutorialServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Tutorial> getAllTutorials(@RequestParam(required = false, name = "keyword") String keyword){
        if(Objects.isNull(keyword)){
            return tutorialServiceImpl.findAll();
        }
        return tutorialServiceImpl.findByKeywordIgnoreCase(keyword);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Tutorial> getTutorial(@PathVariable(name = "id") long id){
        return tutorialServiceImpl.findById(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Tutorial> createTutorial(@RequestBody Tutorial tutorial){
        return tutorialServiceImpl.create(tutorial);
    }

    @PostMapping("/bulk/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Tutorial> createBulkTutorial(@RequestBody List<Tutorial> tutorialList){
        return tutorialServiceImpl.bulkCreate(tutorialList);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Tutorial> updateTutorial(@PathVariable(name = "id") long id, @RequestBody Tutorial tutorial){
        return tutorialServiceImpl.update(id, tutorial);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllTutorials(){
        return tutorialServiceImpl.deleteAll();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTutorial(@PathVariable(name = "id") long id) {
        return tutorialServiceImpl.deleteById(id);
    }
}
