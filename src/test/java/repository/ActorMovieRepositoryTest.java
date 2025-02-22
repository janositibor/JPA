package repository;

import model.Actor;
import model.Movie;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ActorMovieRepositoryTest {
    private DB db=new DB("testdb","sa","");
    private ActorRepository actorRepository;
    private MovieRepository movieRepository;
    private ActorMovieRepository actorMovieRepository;
    @BeforeEach
    void init(){
        Flyway flyway=Flyway.configure().dataSource(db.getDataSource()).load();
        flyway.clean();
        flyway.migrate();
        actorRepository=new ActorRepository(db.getDataSource());
        movieRepository=new MovieRepository(db.getDataSource());
        actorMovieRepository=new ActorMovieRepository(db.getDataSource());
    }

    @Test
    void saveTest(){
        Movie movie1=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        Movie movie2=new Movie("Papírkutyák", LocalDate.of(2008,10,30));
        Movie movie3=new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989,1,1));
        movieRepository.saveBasicAndGetGeneratedKey(movie1);
        movieRepository.saveBasicAndGetGeneratedKey(movie2);
        movieRepository.saveBasicAndGetGeneratedKey(movie3);
        Movie getMovie1=movieRepository.findMovie(movie1).get();
        Movie getMovie2=movieRepository.findMovie(movie2).get();
        Movie getMovie3=movieRepository.findMovie(movie3).get();

        Actor actor1=new Actor("Scherer Péter",1961);
        Actor actor2=new Actor("Mucsi Zoltán",1957);
        actorRepository.saveBasicAndGetGeneratedKey(actor1);
        actorRepository.saveBasicAndGetGeneratedKey(actor2);
        Actor getActor1=actorRepository.findActor(actor1).get();
        Actor getActor2=actorRepository.findActor(actor2).get();

        actorMovieRepository.save(getActor2,getMovie1);
        actorMovieRepository.save(getActor2,getMovie2);
        actorMovieRepository.save(getActor1,getMovie2);

        List<Actor> actorsForMovie1=actorMovieRepository.findActorsForMovie(getMovie1);
        List<Actor> actorsForMovie2=actorMovieRepository.findActorsForMovie(getMovie2);
        List<Actor> actorsForMovie3=actorMovieRepository.findActorsForMovie(getMovie3);

        assertThat(actorsForMovie1)
                .extracting(Actor::getName,Actor::getYob)
                .containsExactly(Tuple.tuple("Mucsi Zoltán",1957));
        System.out.println(actorsForMovie2);
        assertThat(actorsForMovie2)
                .extracting(Actor::getId,Actor::getName,Actor::getYob)
                .containsExactly(Tuple.tuple(1L,"Scherer Péter",1961),Tuple.tuple(2L,"Mucsi Zoltán",1957));
        assertEquals(0, actorsForMovie3.size());

        List<Movie> moviesForActor1=actorMovieRepository.findMoviesForActor(getActor1);
        List<Movie> moviesForActor2=actorMovieRepository.findMoviesForActor(getActor2);
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