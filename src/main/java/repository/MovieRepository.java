package repository;

import model.MovieRowMapper;
import model.Movie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MovieRepository{
    private JdbcTemplate jdbcTemplate;

    public MovieRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public Optional<Long> saveBasicAndGetGeneratedKey(Movie movie) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Number key;

        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps =
                                            connection.prepareStatement("insert into movies(title,release_date) values (?,?)",
                                                    Statement.RETURN_GENERATED_KEYS);
                                    ps.setString(1, movie.getTitle());
                                    ps.setDate(2, Date.valueOf(movie.getReleaseDate()));
                                    return ps;
                                }
                            }, keyHolder
        );

        key=keyHolder.getKey();
        if(key!=null){
            return Optional.ofNullable(key.longValue());
        }
        else{
            throw new IllegalArgumentException("Error with movie saving!");
        }
    }
    public Optional<Movie> findMovie(Movie movie) {
        List<Movie> movies=jdbcTemplate.query("select movies.id AS id, movies.title AS title, movies.release_date AS release_date, COUNT(ratings.rating) AS number_of_ratings, AVG(ratings.rating) AS average_of_ratings from movies LEFT JOIN ratings ON movies.id=ratings.movie_id WHERE movies.title LIKE ? AND movies.release_date=? GROUP BY movies.id"
                , new MovieRowMapper(true)
                ,movie.getTitle(),Date.valueOf(movie.getReleaseDate()));
        if(movies.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(movies.get(0));
    }



}
