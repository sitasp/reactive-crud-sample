package com.sage.reactive_crud_sample.controller;

import com.sage.reactive_crud_sample.entity.Tutorial;
import com.sage.reactive_crud_sample.repository.TutorialRepository;
import com.sage.reactive_crud_sample.service.TutorialServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(TutorialController.class)
@ActiveProfiles("test")
@Import({TutorialServiceImpl.class})
class TutorialControllerTest {

    @MockBean
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialController tutorialController;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ConnectionFactoryInitializer initializer;

    @Autowired
    private ApplicationContext context;

    @Test
    void springContextLoads(){
        assertNotNull(context);
    }

    @Test
    void getAllTutorials() {
        List<Tutorial> tutorialList = Arrays.asList(
                new Tutorial(1L, "Spring", "Java Framework", true),
                new Tutorial(2L, "Django", "Python Framework", true),
                new Tutorial(3L, "React", "JS Framework", true),
                new Tutorial(4L, "laravel", "PHP Framework", true)
                );

        Mockito.when(tutorialRepository.findAll()).thenReturn(Flux.fromIterable(tutorialList));

        webTestClient.get().uri("/api/tutorial")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Tutorial.class).isEqualTo(tutorialList);
    }

    @Test
    void getTutorialById() {
        Tutorial tutorial = new Tutorial(1L, "Spring", "Java Framework", true);

        Mockito.when(tutorialRepository.findById(1L)).thenReturn(Mono.just(tutorial));

        webTestClient.get().uri("/api/tutorial/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Tutorial.class).isEqualTo(tutorial);
    }

    @Test
    void createTutorial() {
        Tutorial tutorial = new Tutorial(1L, "Spring", "Java Framework", true);

        Mockito.when(tutorialRepository.save(tutorial)).thenReturn(Mono.just(tutorial));

        webTestClient.post().uri("/api/tutorial/create")
                .body(BodyInserters.fromValue(tutorial))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Tutorial.class).isEqualTo(tutorial);
    }

    @Test
    void createBulkTutorial() {
        List<Tutorial> tutorialList = Arrays.asList(
                new Tutorial(1L, "Spring", "Java Framework", true),
                new Tutorial(2L, "Django", "Python Framework", true),
                new Tutorial(3L, "React", "JS Framework", true),
                new Tutorial(4L, "laravel", "PHP Framework", true)
        );

        Mockito.when(tutorialRepository.save(ArgumentMatchers.any(Tutorial.class)))
                .thenAnswer(invocation -> {
                    Tutorial tutorial = invocation.getArgument(0);
                    return Mono.just(tutorial);
                });

        webTestClient.post().uri("/api/tutorial/bulk/create")
                .body(BodyInserters.fromValue(tutorialList))
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Tutorial.class).isEqualTo(tutorialList);
    }

    @Test
    void updateTutorial() {
        Tutorial tutorial = new Tutorial(1L, "Spring", "Java Framework", true);

        Mockito.when(tutorialRepository.findById(1L))
                .thenReturn(Mono.just(tutorial));

        Mockito.when(tutorialRepository.save(tutorial))
                .thenReturn(Mono.just(tutorial));

        tutorial.setDescription("Java RX");
        webTestClient.put().uri("/api/tutorial/update/1")
                .body(BodyInserters.fromValue(tutorial))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Tutorial.class).isEqualTo(tutorial);
    }

    @Test
    void deleteAllTutorials() {
        List<Tutorial> tutorialList = Arrays.asList(
                new Tutorial(1L, "Spring", "Java Framework", true),
                new Tutorial(2L, "Django", "Python Framework", true),
                new Tutorial(3L, "React", "JS Framework", true),
                new Tutorial(4L, "laravel", "PHP Framework", true)
        );

        Mockito.when(tutorialRepository.deleteAll())
                .thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/tutorial/delete/1")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();
    }

    @Test
    void deleteTutorial() {
        List<Tutorial> tutorialList = Arrays.asList(
                new Tutorial(1L, "Spring", "Java Framework", true),
                new Tutorial(2L, "Django", "Python Framework", true),
                new Tutorial(3L, "React", "JS Framework", true),
                new Tutorial(4L, "laravel", "PHP Framework", true)
        );

        Mockito.when(tutorialRepository.deleteById(ArgumentMatchers.anyLong()))
                .thenAnswer(invocation -> {
                    assertTrue((long)invocation.getArgument(0) >= 1 && (long)invocation.getArgument(0) <= (long) tutorialList.size());

                    Tutorial removedTutorial = tutorialList.remove((int)invocation.getArgument(0)-1);
                    assertEquals(removedTutorial.getId(), (long)invocation.getArgument(0)-1);

                    return Mono.empty();
                });

        webTestClient.delete().uri("/api/tutorial/delete")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();
    }
}