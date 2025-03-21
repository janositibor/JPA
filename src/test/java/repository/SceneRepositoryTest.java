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
import static org.assertj.core.api.Assertions.assertThat;


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
    void dbInit(){
        Movie movie1 = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        Movie movie2 = new Movie("Tarzan", LocalDate.of(1935, 10, 1));
        Movie movie3 = new Movie("Titanic", LocalDate.of(1995, 3, 15));
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);

        Scene scene1 = new Scene("Intro", "Brief overview", 2);
        Scene scene2 = new Scene("Eletkep1", "Legeleszik a disznó a betonon", 3);
        Scene scene3 = new Scene("Intro", "Tarzan sétál a dzsungelben", 2);
        Scene scene4 = new Scene("Jane feltűnik", "Jane utazik a dzsungelben", 4);
        Scene scene5 = new Scene("Fater", "Fater erőszakoskodik", 3);
        Scene scene6 = new Scene("Jane és Jack találkozik", "Bohóckodnak aztán összenéznek", 2);
        Scene scene7 = new Scene("Mulatság", "Cigi és tánc", 2);
        movie1.defineScenes(List.of(scene1, scene2, scene5,scene7));
        movie2.defineScenes(List.of(scene3, scene4));
        movie3.defineScenes(List.of(scene6));
        sceneRepository.save(scene1);
        sceneRepository.save(scene2);
        sceneRepository.save(scene3);
        sceneRepository.save(scene4);
        sceneRepository.save(scene5);
        sceneRepository.save(scene6);
        sceneRepository.save(scene7);
    }

    @Test
    void findByMovieIdTest(){
        dbInit();
        List<Scene> scenes=sceneRepository.findByMovieId(2L);
        assertThat(scenes)
                .hasSize(2)
                .extracting(Scene::getDescription)
                .containsExactly("Tarzan sétál a dzsungelben","Jane utazik a dzsungelben");
    }
    @Test
    void findLongerThanTest(){
        dbInit();
        List<Scene> scenes=sceneRepository.findLongerThan(2);
        assertThat(scenes)
                .hasSize(3)
                .extracting(Scene::getTitle)
                .containsExactly("Eletkep1","Jane feltűnik","Fater");
    }
    @Test
    void findScenesFromMovieWithTheMostScenesTest(){
        dbInit();
        List<Scene> scenes=sceneRepository.findScenesFromMovieWithTheMostScenes();
        assertThat(scenes)
                .hasSize(4)
                .extracting(Scene::getTitle)
                .containsExactly("Intro","Eletkep1","Fater","Mulatság");
    }
    @Test
    void findAverageNumberOfScenesTest(){
        dbInit();
        Double averageNumberOfScenes=sceneRepository.findAverageNumberOfScenes();
        assertEquals(2.333, averageNumberOfScenes,0.0005);
    }
    @Test
    void findMoviesWithScenesMoreThanTest(){
        dbInit();
        List<Movie> movies=sceneRepository.findMoviesWithScenesMoreThan(1);
        assertThat(movies)
                .hasSize(2)
                .extracting(Movie::getTitle)
                .containsExactly("Roncsfilm","Tarzan");
    }
    @Test
    void findMovieWithTheMostScenesTest(){
        dbInit();
        Movie movie=sceneRepository.findMovieWithTheMostScenes();
        assertThat(movie)
                .hasFieldOrPropertyWithValue("title","Roncsfilm");
    }
    @Test
    void findMovieWithSceneTitleTest(){
        dbInit();
        Movie movie=sceneRepository.findMovieWithSceneTitle("Jane és Jack találkozik");
        assertThat(movie)
                .hasFieldOrPropertyWithValue("title","Titanic");
    }

    @Test
    void findByLengthAndMovieTitle(){
        dbInit();
        List<Scene> scenes=sceneRepository.findByLengthAndMovieTitle(4,"Tarzan");
        assertThat(scenes)
                .hasSize(1)
                .extracting(Scene::getTitle)
                .containsExactly("Jane feltűnik");
        List<Scene> scenes2=sceneRepository.findByLengthAndMovieTitle(3,"Roncsfilm");
        assertThat(scenes2)
                .hasSize(2)
                .extracting(Scene::getTitle)
                .containsExactly("Eletkep1","Fater");
    }
}