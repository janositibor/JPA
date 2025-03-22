package repository;

import model.Movie;
import model.Show;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ShowsTest {
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

        List<Show> shows = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Show show=new Show("Művészmozi", LocalTime.of(16+(2*i),0));
            shows.add(show);
        }
        movieRepository.setShows(movie1.getId(), shows);

        shows.clear();
        movieRepository.addShow(movie2.getId(), new Show("Grand Café",LocalTime.of(20,15)));

        shows.clear();
        for (int i = 0; i < 3; i ++) {
            Show show=new Show("Apolló", LocalTime.of(17+(2*i),30));
            shows.add(show);
        }
        movieRepository.setShows(movie3.getId(), shows);

        Movie getMovie1 = movieRepository.findMovieWithShows(movie1.getId()).get();
        assertThat(getMovie1.getShows())
                .hasSize(4)
                .extracting(Show::getCinema)
                .containsOnly("Művészmozi");
        assertThat(getMovie1.getShows())
                .hasSize(4)
                .extracting(Show::getStartTime)
                .contains(LocalTime.of(16,0),LocalTime.of(22,0));

        Movie getMovie2 = movieRepository.findMovieWithShows(movie2.getId()).get();
        assertThat(getMovie2.getShows())
                .hasSize(1)
                .extracting(Show::getCinema)
                .containsOnly("Grand Café");
        assertThat(getMovie2.getShows())
                .hasSize(1)
                .extracting(Show::getStartTime)
                .containsExactly(LocalTime.of(20,15));

        Movie getMovie3 = movieRepository.findMovieWithShows(movie3.getId()).get();
        assertThat(getMovie3.getShows())
                .hasSize(3)
                .extracting(Show::getCinema)
                .containsOnly("Apolló");
        assertThat(getMovie3.getShows())
                .hasSize(3)
                .extracting(Show::getStartTime)
                .contains(LocalTime.of(17,30),LocalTime.of(21,30));
    }
}
