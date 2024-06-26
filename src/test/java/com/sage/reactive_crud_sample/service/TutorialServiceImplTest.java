package com.sage.reactive_crud_sample.service;

import com.sage.reactive_crud_sample.entity.Tutorial;
import com.sage.reactive_crud_sample.repository.TutorialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TutorialServiceImplTest {

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialServiceImpl tutorialService;

    private Tutorial tutorial;

    private List<Tutorial> tutorialList;

    @BeforeEach
    void setup(){
        tutorial = new Tutorial(1L, "Spring", "Java Backend", true);

        tutorialList = Arrays.asList(
                new Tutorial(2L, "Django", "Python Backend", true),
                new Tutorial(3L, "Spring", "Java Backend", true),
                new Tutorial(4L, "Netty", "Java Server", true)
        );
    }

    @Test
    public void givenTutorials_whenBulkInserted_thenSuccess(){

        Mockito.when(tutorialRepository.save(ArgumentMatchers.any(Tutorial.class)))
                .thenAnswer(invocation -> {
                    Tutorial tutorial = invocation.getArgument(0);
                    return Mono.just(tutorial); // Return a Mono emitting the saved tutorial
                });

        Flux<Tutorial> tutorialFlux = tutorialService.bulkCreate(tutorialList);
        StepVerifier.create(tutorialFlux)
                .expectNextMatches(item -> Objects.equals(item, tutorialList.get(0)))
                .expectNextMatches(item -> Objects.equals(item, tutorialList.get(1)))
                .expectNextMatches(item -> Objects.equals(item, tutorialList.get(2)))
                .expectComplete()
                .verify();
    }


    @Test
    public void givenTutorial_whenUpdated_thenSuccess(){
        Mockito.when(tutorialRepository.findById(1L))
                .thenReturn(Mono.just(tutorial));

        Mockito.when(tutorialRepository.save(tutorial))
                        .thenReturn(Mono.just(tutorial));

        tutorial.setDescription("JavaRX");
        Mono<Tutorial> tutorialMono = tutorialService.update(1L, tutorial);

        StepVerifier.create(tutorialMono)
                .expectNextMatches(tutorial1 -> tutorial1.getDescription().equals("JavaRX"))
                .verifyComplete();
    }
}