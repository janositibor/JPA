package repository;

import model.Movie;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RatingRepositoryTest {
    private DB db=new DB("testdb","sa","");
    private MovieRepository movieRepository;
    private RatingRepository ratingRepository;
    @BeforeEach
    void init(){
        Flyway flyway=Flyway.configure().dataSource(db.getDataSource()).load();
        flyway.clean();
        flyway.migrate();
        movieRepository=new MovieRepository(db.getDataSource());
        ratingRepository=new RatingRepository(db.getDataSource());
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

        for (int i = 1; i <6 ; i++) {
            ratingRepository.save(getMovie1, i);
        }
        for (int i = 5; i <7 ; i++) {
            ratingRepository.save(getMovie3, i);
        }
        for (int i = 1; i <11 ; i+=2) {
            ratingRepository.save(getMovie2, i);
        }

        List<Integer> rating1=ratingRepository.findRatingsForMovie(getMovie1);
        assertThat(rating1)
                .hasSize(5)
                .containsExactly(1,2,3,4,5);
        List<Integer> rating2=ratingRepository.findRatingsForMovie(getMovie2);
        assertThat(rating2)
                .hasSize(5)
                .containsExactly(1,3,5,7,9);
        List<Integer> rating3=ratingRepository.findRatingsForMovie(getMovie3);
        assertThat(rating3)
                .hasSize(2)
                .containsExactly(5,6);

        getMovie1=movieRepository.findMovie(movie1).get();
        getMovie2=movieRepository.findMovie(movie2).get();
        getMovie3=movieRepository.findMovie(movie3).get();

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

        movieRepository.saveBasicAndGetGeneratedKey(movie1);
        movieRepository.saveBasicAndGetGeneratedKey(movie2);
        movieRepository.saveBasicAndGetGeneratedKey(movie3);

        Movie getMovie1=movieRepository.findMovie(movie1).get();
        Movie getMovie2=movieRepository.findMovie(movie2).get();
        Movie getMovie3=movieRepository.findMovie(movie3).get();

        List<Integer> ratings1=List.of(1,2,2,4,1,5,6);
        List<Integer> ratings2=List.of(10,4,1,8,9);
        List<Integer> ratings3=List.of();

        ratingRepository.save(getMovie1,ratings1);
        ratingRepository.save(getMovie2,ratings2);
        ratingRepository.save(getMovie3,ratings3);

        getMovie1=movieRepository.findMovie(movie1).get();
        getMovie2=movieRepository.findMovie(movie2).get();
        getMovie3=movieRepository.findMovie(movie3).get();

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
        movieRepository.saveBasicAndGetGeneratedKey(movie1);
        Movie getMovie1=movieRepository.findMovie(movie1).get();
        List<Integer> ratings1=List.of(1,2,2,4,12,5,6);

        IllegalStateException ise=assertThrows(IllegalStateException.class, ()-> ratingRepository.save(getMovie1,ratings1));
        assertEquals("Invalid rating: 12",ise.getMessage());

    }
}