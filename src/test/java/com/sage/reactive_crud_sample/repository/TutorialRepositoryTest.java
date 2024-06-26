package com.sage.reactive_crud_sample.repository;

import com.sage.reactive_crud_sample.entity.Tutorial;
import com.sage.reactive_crud_sample.service.ITutorialService;
import com.sage.reactive_crud_sample.service.TutorialServiceImpl;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataR2dbcTest
class TutorialRepositoryTest {

    @Autowired
    private TutorialRepository tutorialRepository;

    @BeforeEach
    void setup(){
        tutorialRepository.deleteAll().subscribe();
    }

    @Test
    void givenNewTutorial_whenSaved_thenSuccess(){
        Tutorial newTutorial = new Tutorial(null, "Spring", "Course for Spring", true);
        Mono<Tutorial> insertedTutorial = tutorialRepository.save(newTutorial);

        StepVerifier.create(insertedTutorial)
                .assertNext(savedTutorial -> {
                    assertThat(savedTutorial.getId()).isNotNull();
                    assertThat(savedTutorial.getTitle()).isEqualTo(newTutorial.getTitle());
                    assertThat(savedTutorial.getDescription()).isEqualTo(newTutorial.getDescription());
                    assertThat(savedTutorial.isPublished()).isEqualTo(newTutorial.isPublished());
                });
    }


    @Test
    void givenCreatedTutorial_whenUpdated_thenSuccess(){
        Tutorial newTutorial = new Tutorial(null, "Spring", "Course for Spring", true);
        Mono<Tutorial> insertedTutorial = tutorialRepository.save(newTutorial);

        newTutorial.setDescription("Course for Spring #2");
        insertedTutorial = tutorialRepository.save(newTutorial);

        StepVerifier.create(insertedTutorial)
                .assertNext(savedTutorial -> {
                    assertThat(savedTutorial.getId()).isNotNull();
                    assertThat(savedTutorial.getTitle()).isEqualTo(newTutorial.getTitle());
                    assertThat(savedTutorial.getDescription()).isEqualTo(newTutorial.getDescription());
                    assertThat(savedTutorial.isPublished()).isEqualTo(newTutorial.isPublished());
                })
                .verifyComplete();
    }


    @Test
    void givenTutorialId_whenFind_thenSuccess(){
        Tutorial newTutorial = new Tutorial(null, "Spring", "Course for Spring", true);
        Tutorial insertedTutorial = tutorialRepository.save(newTutorial).block();
        Mono<Tutorial> foundTutorial = tutorialRepository.findById(insertedTutorial.getId());

        StepVerifier.create(foundTutorial)
                .assertNext(savedTutorial -> {
                    assertThat(savedTutorial.getId()).isNotNull();
                    assertThat(savedTutorial).isEqualTo(insertedTutorial);
                })
                .verifyComplete();

    }


    @Test
    void givenKeyword_whenFind_thenSuccess(){
        List<Tutorial> tutorialList = Arrays.asList(
                new Tutorial(null, "Spring #1", "Java Backend Spring MVC Course", true),
                new Tutorial(null, "Spring #2", "Java Backend Spring WebFlux Course", true),
                new Tutorial(null, "Spring #3", "Microservice Course", true)
        );

        Flux.fromIterable(tutorialList)
                .flatMap(tutorialRepository::save)
                .then()
                .block();

        Flux<Tutorial> javaKeywordTutorials = tutorialRepository.findByKeywordIgnoreCase("java");

        StepVerifier.create(javaKeywordTutorials)
                .expectNextMatches(
                        tutorial -> tutorial.getTitle().contains("Spring #1")
                )
                .expectNextMatches(
                        tutorial -> tutorial.getTitle().contains("Spring #2")
                )
                .verifyComplete();
    }
}