package repository;

import model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {
    private EntityManagerFactory factory;
    private MovieRepository movieRepository;

    @BeforeEach
    void init() {
//        factory = Persistence.createEntityManagerFactory("MariaDB-pu");
        factory = Persistence.createEntityManagerFactory("H2-pu");
        movieRepository = new MovieRepository(factory);
    }
    @AfterEach
    void close(){
        factory.close();
    }

    @Test
    void saveTest() {
        Movie movie1 = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        movieRepository.save(movie1);
        Movie movie2 = new Movie("Papírkutyák", LocalDate.of(2008, 10, 30));
        movieRepository.save(movie2);
        Movie movie3 = new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989, 1, 1));
        movieRepository.save(movie3);

        long id1 = movie1.getId();
        assertEquals(1L, id1);
        long id2 = movie2.getId();
        assertEquals(2L, id2);
        long id3 = movie3.getId();
        assertEquals(3L, id3);
    }

    @Test
    void findMovieTest() {
        Movie movie1 = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        Movie movie2 = new Movie("Papirkutyak", LocalDate.of(2008, 10, 30));
        Movie movie3 = new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989, 1, 1));

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);

        Movie getMovie2 = movieRepository.find(movie2).get();
        Movie getMovie3 = movieRepository.find(movie3).get();
        Movie getMovie1 = movieRepository.find(movie1).get();

        assertEquals(movie1.getId(), getMovie1.getId());
        assertEquals(movie2.getId(), getMovie2.getId());
        assertEquals(movie3.getId(), getMovie3.getId());
    }

    @Test
    void findMovieByIdTest() {
        Movie movie1 = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        Movie movie2 = new Movie("Papirkutyak", LocalDate.of(2008, 10, 30));
        Movie movie3 = new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989, 1, 1));

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);

        Movie getMovie2 = movieRepository.findById(movie2.getId()).get();
        Movie getMovie3 = movieRepository.findById(movie3.getId()).get();
        Movie getMovie1 = movieRepository.findById(movie1.getId()).get();

        assertEquals(movie1.getTitle(), getMovie1.getTitle());
        assertEquals(movie1.getReleaseDate(), getMovie1.getReleaseDate());
        assertEquals(movie2.getTitle(), getMovie2.getTitle());
        assertEquals(movie2.getReleaseDate(), getMovie2.getReleaseDate());
        assertEquals(movie3.getTitle(), getMovie3.getTitle());
        assertEquals(movie3.getReleaseDate(), getMovie3.getReleaseDate());

    }

    @Test
    void notFoundMovie() {
        Movie movie = new Movie("Roncsfilm", LocalDate.of(1992, 9, 1));
        Optional<Movie> getMovie = movieRepository.find(movie);
        assertFalse(getMovie.isPresent());
    }


}