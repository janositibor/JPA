package repository;

import model.Movie;
import model.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SceneRepositoryTest {
    private EntityManagerFactory factory;
    private SceneRepository sceneRepository;
    private MovieRepository movieRepository;

    @BeforeEach
    void init() {
//        factory = Persistence.createEntityManagerFactory("MariaDB-pu");
        factory = Persistence.createEntityManagerFactory("H2-pu");
        sceneRepository = new SceneRepository(factory);
        movieRepository = new MovieRepository(factory);

    }

    @Test
    void saveTest() {
        Scene scene1 = new Scene("Intro", "Brief overview", 2);
        sceneRepository.save(scene1);

        Scene getScene = sceneRepository.findById(scene1.getId()).get();
        assertEquals("Intro", getScene.getTitle());
        assertEquals(2, getScene.getLength());
    }

    @Test
    void saveWithMovieTest() {
        Movie movie1 = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        movieRepository.save(movie1);

        Scene scene1 = new Scene("Intro", "Brief overview", 2);
        scene1.setMovie(movie1);
        sceneRepository.save(scene1);

        Scene getScene = sceneRepository.findById(scene1.getId()).get();
        assertEquals("Roncsfilm", getScene.getMovie().getTitle());
    }

    @Test
    void saveMultipleScenesForAMovieTest() {
        Movie movie1 = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        movieRepository.save(movie1);

        Scene scene1 = new Scene("Intro", "Brief overview", 2);
        Scene scene2 = new Scene("Eletkep1", "Legeleszik a disznó a betonon", 3);
        movie1.defineScenes(List.of(scene1, scene2));
        sceneRepository.save(scene1);
        sceneRepository.save(scene2);

        Movie getMovie = movieRepository.findMovieWithScenes(movie1.getId()).get();
        assertEquals(2, getMovie.getScenes().size());

        Scene getScene1 = sceneRepository.findById(scene1.getId()).get();
        Scene getScene2 = sceneRepository.findById(scene2.getId()).get();
        assertEquals("Roncsfilm", getScene1.getMovie().getTitle());
        assertTrue(getScene1.getMovie().getId() == getScene2.getMovie().getId());
    }

    @Test
    void saveAndAddScenesForAMovieTest() {
        Movie movie1 = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        movieRepository.save(movie1);

        Scene scene1 = new Scene("Intro", "Brief overview", 2);
        scene1.setMovie(movie1);
        sceneRepository.save(scene1);

        Scene scene2 = new Scene("Eletkep1", "Legeleszik a disznó a betonon", 3);
        Movie getMovie = movieRepository.findMovieWithScenes(movie1.getId()).get();
        movieRepository.save(getMovie);
        getMovie.addScene(scene2);
        sceneRepository.save(scene2);


        Scene getScene1 = sceneRepository.findById(scene1.getId()).get();
        Scene getScene2 = sceneRepository.find(scene2).get();
        assertEquals("Roncsfilm", getScene2.getMovie().getTitle());
        assertTrue(getScene1.getMovie().getId() == getScene2.getMovie().getId());
    }
}