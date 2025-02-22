package repository;

import model.Movie;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {
    private DB db=new DB("testdb","sa","");
    private MovieRepository movieRepository;
    @BeforeEach
    void init(){
        Flyway flyway=Flyway.configure().dataSource(db.getDataSource()).load();
        flyway.clean();
        flyway.migrate();
        movieRepository=new MovieRepository(db.getDataSource());
    }
    @Test
    void saveTest(){
        Movie movie1=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        long id1=movieRepository.saveBasicAndGetGeneratedKey(movie1).get();
        assertEquals(1L,id1);
        Movie movie2=new Movie("Papírkutyák", LocalDate.of(2008,10,30));
        long id2=movieRepository.saveBasicAndGetGeneratedKey(movie2).get();
        assertEquals(2L,id2);
        Movie movie3=new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989,1,1));
        long id3=movieRepository.saveBasicAndGetGeneratedKey(movie3).get();
        assertEquals(3L,id3);
    }
    @Test
    void findMovieTest(){
        Movie movie1=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        Movie movie2=new Movie("Papirkutyak", LocalDate.of(2008,10,30));
        Movie movie3=new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989,1,1));

        movieRepository.saveBasicAndGetGeneratedKey(movie1).get();
        movieRepository.saveBasicAndGetGeneratedKey(movie2).get();
        movieRepository.saveBasicAndGetGeneratedKey(movie3).get();

        Movie getMovie2=movieRepository.findMovie(movie2).get();
        Movie getMovie3=movieRepository.findMovie(movie3).get();
        Movie getMovie1=movieRepository.findMovie(movie1).get();

        assertEquals(1L,getMovie1.getId());
        assertEquals(2L,getMovie2.getId());
        assertEquals(3L,getMovie3.getId());
    }
    @Test
    void notFoundMovie(){
        Movie movie=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        Optional<Movie> getMovie=movieRepository.findMovie(movie);
        assertFalse(getMovie.isPresent());
    }


}