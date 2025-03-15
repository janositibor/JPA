package repository;

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

class ActorMovieRepositoryTest {
    private EntityManagerFactory factory;
    private ActorRepository actorRepository;
    private MovieRepository movieRepository;
    private ActorMovieRepository actorMovieRepository;
    @BeforeEach
    void init(){
        factory = Persistence.createEntityManagerFactory("MariaDB-pu");
//        factory = Persistence.createEntityManagerFactory("H2-pu");
        actorRepository = new ActorRepository(factory);
        movieRepository=new MovieRepository(factory);
        actorMovieRepository=new ActorMovieRepository(factory);
    }

    @Test
    void saveTest(){
        Movie movie1=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        Movie movie2=new Movie("Papírkutyák", LocalDate.of(2008,10,30));
        Movie movie3=new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989,1,1));
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);

        Actor actor1=new Actor("Scherer Péter",1961);
        Actor actor2=new Actor("Mucsi Zoltán",1957);
        actorRepository.save(actor1);
        actorRepository.save(actor2);

        actor1.addMovies(movie2);
        actor2.setMovies(List.of(movie1,movie2));

        actorRepository.update(actor1);
        actorRepository.update(actor2);

        Actor getActor1=actorMovieRepository.findActorByIdWithMovies(actor1.getId()).get();
        Actor getActor2=actorMovieRepository.findActorByIdWithMovies(actor2.getId()).get();
        Movie getMovie1=actorMovieRepository.findMovieByIdWithActors(movie1.getId()).get();
        Movie getMovie2=actorMovieRepository.findMovieByIdWithActors(movie2.getId()).get();
        Movie getMovie3=actorMovieRepository.findMovieByIdWithActors(movie3.getId()).get();

        List<Actor> actorsForMovie1=getMovie1.getActors();
        List<Actor> actorsForMovie2=getMovie2.getActors();
        List<Actor> actorsForMovie3=getMovie3.getActors();

        assertThat(actorsForMovie1)
                .extracting(Actor::getName,Actor::getYob)
                .containsExactly(Tuple.tuple("Mucsi Zoltán",1957));
        System.out.println(actorsForMovie2);
        assertThat(actorsForMovie2)
                .extracting(Actor::getId,Actor::getName,Actor::getYob)
                .containsExactly(Tuple.tuple(1L,"Scherer Péter",1961),Tuple.tuple(2L,"Mucsi Zoltán",1957));
        assertEquals(0, actorsForMovie3.size());

        List<Movie> moviesForActor1=getActor1.getMovies();
        List<Movie> moviesForActor2=getActor2.getMovies();
        assertThat(moviesForActor1)
                .extracting(Movie::getId,Movie::getTitle,Movie::getReleaseDate)
                .containsExactly(Tuple.tuple(2L,"Papírkutyák", LocalDate.of(2008,10,30)));
        assertThat(moviesForActor2)
                .extracting(Movie::getId,Movie::getTitle,Movie::getReleaseDate)
                .containsExactly(
                        Tuple.tuple(1L,"Roncsfilm", LocalDate.of(1992,9,1)),
                        Tuple.tuple(2L,"Papírkutyák", LocalDate.of(2008,10,30))
                );
    }

}