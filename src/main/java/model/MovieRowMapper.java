package model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MovieRowMapper  implements RowMapper<Movie> {
    private boolean withRatings;

    public MovieRowMapper(boolean withRatings) {
//        super();
        this.withRatings = withRatings;
    }

    @Override
    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id= rs.getLong("id");
        if(id==0){
            return null;
        }
        String title = rs.getString("title");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();

        Movie movie=new Movie(id, title, releaseDate);
        if(withRatings) {
            int numberOfRatings=rs.getInt("number_of_ratings");
            double averageOfRatings=rs.getDouble("average_of_ratings");
            movie.setNumberOfRatings(numberOfRatings);
            movie.setAverageOfRatings(averageOfRatings);
        }

        return movie;
    }
}
