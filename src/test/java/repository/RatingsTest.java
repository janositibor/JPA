package repository;

import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RatingsTest {
    private EntityManagerFactory factory;
    private MovieRepository movieRepository;
    @BeforeEach
    void init(){
        factory = Persistence.createEntityManagerFactory("MariaDB-pu");
        movieRepository=new MovieRepository(factory);
    }

    @Test
    void saveTest(){
        Movie movie1=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        movieRepository.save(movie1);
        Movie movie2=new Movie("Papírkutyák", LocalDate.of(2008,10,30));
        movieRepository.save(movie2);
        Movie movie3=new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989,1,1));
        movieRepository.save(movie3);

        List<Integer> ratings=new ArrayList<>();
        for (int i = 1; i <6 ; i++) {
            ratings.add(i);
        }
        movieRepository.setRatings(movie1.getId(),ratings);

        ratings.clear();
        for (int i = 5; i <7 ; i++) {
            ratings.add(i);
        }
        movieRepository.setRatings(movie3.getId(),ratings);

        ratings.clear();
        for (int i = 1; i <11 ; i+=2) {
            ratings.add(i);
        }
        movieRepository.setRatings(movie2.getId(),ratings);

        Movie getMovie1=movieRepository.findMovieWithRatings(movie1.getId()).get();
        assertThat(getMovie1.getRatings())
                .hasSize(5)
                .containsExactly(1,2,3,4,5);

        Movie getMovie2=movieRepository.findMovieWithRatings(movie2.getId()).get();
        assertThat(getMovie2.getRatings())
                .hasSize(5)
                .containsExactly(1,3,5,7,9);

        Movie getMovie3=movieRepository.findMovieWithRatings(movie3.getId()).get();
        assertThat(getMovie3.getRatings())
                .hasSize(2)
                .containsExactly(5,6);

        assertThat(getMovie1)
                .hasFieldOrPropertyWithValue("numberOfRatings",5)
                .hasFieldOrPropertyWithValue("averageOfRatings",List.of(1,2,3,4,5).stream().collect(Collectors.averagingInt(x->x.intValue())));
        assertThat(getMovie2)
                .hasFieldOrPropertyWithValue("numberOfRatings",5)
                .hasFieldOrPropertyWithValue("averageOfRatings",List.of(1,3,5,7,9).stream().collect(Collectors.averagingInt(x->x.intValue())));
        assertThat(getMovie3)
                .hasFieldOrPropertyWithValue("numberOfRatings",2)
                .hasFieldOrPropertyWithValue("averageOfRatings",List.of(5,6).stream().collect(Collectors.averagingInt(x->x.intValue())));
    }
    @Test
    void saveWithListTest(){
        Movie movie1=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        Movie movie2=new Movie("Papírkutyák", LocalDate.of(2008,10,30));
        Movie movie3=new Movie("Mielőtt befejezi röptét a denevér", LocalDate.of(1989,1,1));

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);

        List<Integer> ratings1=List.of(1,2,2,4,1,5,6);
        List<Integer> ratings2=List.of(10,4,1,8,9);
        List<Integer> ratings3=List.of();

        movieRepository.setRatings(movie1.getId(),ratings1);
        movieRepository.setRatings(movie2.getId(),ratings2);
        movieRepository.setRatings(movie3.getId(),ratings3);

        Movie getMovie1=movieRepository.findMovieWithRatings(movie1.getId()).get();
        Movie getMovie2=movieRepository.findMovieWithRatings(movie2.getId()).get();
        Movie getMovie3=movieRepository.findMovieWithRatings(movie3.getId()).get();

        assertThat(getMovie1)
                .hasFieldOrPropertyWithValue("numberOfRatings",ratings1.size())
                .hasFieldOrPropertyWithValue("averageOfRatings",ratings1.stream().collect(Collectors.averagingInt(x->x.intValue())));
        assertThat(getMovie2)
                .hasFieldOrPropertyWithValue("numberOfRatings",ratings2.size())
                .hasFieldOrPropertyWithValue("averageOfRatings",ratings2.stream().collect(Collectors.averagingInt(x->x.intValue())));
        assertThat(getMovie3)
                .hasFieldOrPropertyWithValue("numberOfRatings",ratings3.size())
                .hasFieldOrPropertyWithValue("averageOfRatings",ratings3.stream().collect(Collectors.averagingInt(x->x.intValue())));
    }

    @Test
    void invalidRatingInListTest(){
        Movie movie1=new Movie("Roncsfilm", LocalDate.of(1992,9,1));
        movieRepository.save(movie1);
        List<Integer> ratings1=List.of(1,2,2,4,12,5,6);

        IllegalStateException ise=assertThrows(IllegalStateException.class, ()-> movieRepository.setRatings(movie1.getId(), ratings1));
        assertEquals("Invalid rating: 12",ise.getMessage());

    }
}