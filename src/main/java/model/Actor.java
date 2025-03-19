package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int yob;
    @ManyToMany(mappedBy = "actors")
    private List<Movie> movies = new ArrayList<>();

    public Actor() {
    }

    public Actor(String name, int yob) {
        this.name = name;
        this.yob = yob;
    }

    public Actor(Long id, String name, int yob) {
        this(name, yob);
        this.id = id;
    }


    public List<Movie> getMovies() {
        return movies;
    }

    public void defineMovies(List<Movie> movies) {
        this.movies.clear();
        for (Movie movie:movies) {
            addMovie(movie);
        }
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
        movie.addActor(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYob() {
        return yob;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yob=" + yob +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYob(int yob) {
        this.yob = yob;
    }
}
