package service;

import model.Actor;
import model.Movie;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private EntityManagerFactory factory;
    Service service;

    @BeforeEach
    void init() {
        factory = Persistence.createEntityManagerFactory("MariaDB-pu");
        service = new Service(factory);
    }

    @Test
    void saveActorTest() {
        service.saveActor("Scherer Péter", 1961);
        service.saveActor("Mucsi Zoltán", 1957);
    }

    @Test
    void findActorTest() {
        service.saveActor("Scherer Péter", 1961);
        service.saveActor("Mucsi Zoltán", 1957);
        Actor getActor = service.findActor(new Actor("Mucsi Zoltán", 1957));
        assertThat(getActor)
                .hasFieldOrPropertyWithValue("name", "Mucsi Zoltán")
                .hasFieldOrPropertyWithValue("yob", 1957);
    }

    @Test
    void notFoundActorTest() {
        service.saveActor("Scherer Péter", 1961);
        service.saveActor("Mucsi Zoltán", 1957);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> service.findActor(new Actor("Schererr Péter", 1961)));
        assertEquals("No actor found: Actor{id=0, name='Schererr Péter', yob=1961}", iae.getMessage());

    }

    @Test
    void findAllActorTest() {
        service.saveActor("Scherer Péter", 1961);
        service.saveActor("Mucsi Zoltán", 1957);
        List<Actor> actors = service.findAllActor();
        assertThat(actors)
                .extracting(Actor::getName, Actor::getYob)
                .containsExactly(new Tuple("Scherer Péter", 1961), new Tuple("Mucsi Zoltán", 1957));
    }

    @Test
    void notContainsMovieTest() {
        assertEquals(false, service.containsMovie(new Movie("Roncsfilm", LocalDate.of(1992, 9, 1))));
    }

    @Test
    void saveMovieWithActorsTest() {
        Actor actor1 = new Actor("Scherer Péter", 1961);
        Actor actor2 = new Actor("Mucsi Zoltán", 1957);
        service.saveMovieWithActors("Papírkutyák", LocalDate.of(2008, 10, 30), List.of(actor1, actor2));

        Movie movie = service.findMovie(new Movie("Papírkutyák", LocalDate.of(2008, 10, 30)));

        List<Actor> actors = movie.getActors();
        assertThat(actors)
                .extracting(Actor::getName, Actor::getYob)
                .containsExactly(new Tuple("Scherer Péter", 1961), new Tuple("Mucsi Zoltán", 1957));
    }

    @Test
    void actorsMoviesTest() {
        Actor actor1 = new Actor("Scherer Péter", 1961);
        Actor actor2 = new Actor("Mucsi Zoltán", 1957);
        service.saveMovieWithActors("Papírkutyák", LocalDate.of(2008, 10, 30), List.of(actor1, actor2));
        service.saveMovieWithActors("Roncsfilm", LocalDate.of(1992, 9, 1), List.of(actor2));

        Actor actorWithMovies1 = service.findActor(actor1);
        Actor actorWithMovies2 = service.findActor(actor2);

        List<Movie> movies1 = actorWithMovies1.getMovies();
        assertThat(movies1)
                .extracting(Movie::getTitle, Movie::getReleaseDate)
                .containsExactly(new Tuple("Papírkutyák", LocalDate.of(2008, 10, 30)));
        List<Movie> movies2 = actorWithMovies2.getMovies();
        assertThat(movies2)
                .extracting(Movie::getTitle, Movie::getReleaseDate)
                .containsExactly(new Tuple("Papírkutyák", LocalDate.of(2008, 10, 30))
                        , new Tuple("Roncsfilm", LocalDate.of(1992, 9, 1)));
    }


}